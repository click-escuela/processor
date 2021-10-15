package click.escuela.processor.services;

import java.io.File;
import java.io.IOException;
import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import click.escuela.processor.api.ProcessApi;
import click.escuela.processor.api.StudentApiFile;
import click.escuela.processor.dtos.FileError;
import click.escuela.processor.dtos.ProcessDTO;
import click.escuela.processor.dtos.ResponseCreateProcessDTO;
import click.escuela.processor.enums.FileStatus;
import click.escuela.processor.enums.ProcessMessage;
import click.escuela.processor.exception.ProcessException;
import click.escuela.processor.mapper.Mapper;
import click.escuela.processor.repository.ProcessRepository;
import click.escuela.processor.model.Process;

@Service
public class ProcessServiceImpl implements ProcessService{

	@Autowired
	private ProcessRepository processRepository;
	
	@Autowired
	private FileProcessorImpl studentBulkUpload;

	@Override
	public ResponseCreateProcessDTO saveAndRead(String name, Integer schoolId, MultipartFile file) throws ProcessException {
		try {
			
			ProcessApi processApi = new ProcessApi(name, schoolId, file, 0);
			Process process = Mapper.mapperToProcessApi(processApi);
			process.setStartDate(LocalDateTime.now());
			process.setStatus(FileStatus.PENDING);
			File excel = Mapper.multipartToFile(file, file.getOriginalFilename());

			List<StudentApiFile> students = studentBulkUpload.readFile(excel);
			process.setStudentCount(students.size());
			processRepository.save(process);
			
			ResponseCreateProcessDTO response = new ResponseCreateProcessDTO();
			response.setStudents(students);
			response.setProcessId(process.getId().toString());
			
			return response;
		} catch (Exception e) {
			throw new ProcessException(ProcessMessage.CREATE_ERROR);
		}
	}
	
	@Override
	public String update(String processId, List<FileError> errors, String status) throws ProcessException {
		try {
			
			Process process = getById(processId);
			if(!errors.isEmpty()) {
				File file = studentBulkUpload.writeErrors(errors, process.getFile(), process.getName());
				process.setFile(Mapper.fileToBlob(file));
			}
			process.setStatus(FileStatus.valueOf(status));
			process.setEndDate(LocalDateTime.now());
			processRepository.save(process);
			
			return process.getId().toString();
		} catch (Exception e) {
			throw new ProcessException(ProcessMessage.CREATE_ERROR);
		}
	}
	
	@Override
	public List<ProcessDTO> getfindBySchoolId(Integer schoolId) {
		List<Process> processes = processRepository.findBySchoolId(schoolId);
		
		return Mapper.mapperToProcessDTO(processes);
	}
	
	@Override
	public Process getById(String processId) throws ProcessException {
		return processRepository.findById(UUID.fromString(processId))
				.orElseThrow(() -> new ProcessException(ProcessMessage.GET_ERROR));
	} 
	
	@Override
	public byte[] getFileById(String processId) throws IOException, ProcessException {
		Optional<Process> process = processRepository.findById(UUID.fromString(processId));
		if (process.isPresent()) {
			Blob blob = process.get().getFile();
			return Mapper.blobToFile(blob, "prueba");
		} else {
			throw new ProcessException(ProcessMessage.GET_ERROR);
		}
	}
		
}

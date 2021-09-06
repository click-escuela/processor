package click.escuela.processor.services;

import java.io.IOException;
import java.sql.Blob;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import click.escuela.processor.api.ProcessApi;
import click.escuela.processor.dtos.ProcessDTO;
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

	@Override
	public String save(String name, Integer schoolId, Integer studentCount, MultipartFile file) throws ProcessException {
		try {
			
			ProcessApi processApi = new ProcessApi(name, schoolId, file, studentCount);
			Process process = Mapper.mapperToProcessApi(processApi);
			process.setStartDate(LocalDateTime.now());
			process.setStatus(FileStatus.PENDING);
			processRepository.save(process);
			return process.getId().toString();
		} catch (Exception e) {
			throw new ProcessException(ProcessMessage.CREATE_ERROR);
		}
	}
	
	@Override
	public String update(String status, String processId, MultipartFile file) throws ProcessException {
		try {
			Process process = getById(processId);
			process.setStatus(FileStatus.valueOf(status));
			Blob blob = Mapper.multipartToBlob(file, process.getName());
			process.setFile(blob);
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
	public byte[] getFileById(String processId) throws IOException {
		Blob blob = processRepository.findById(UUID.fromString(processId)).get().getFile();
		return Mapper.blobToFile(blob,"prueba");
	}
}

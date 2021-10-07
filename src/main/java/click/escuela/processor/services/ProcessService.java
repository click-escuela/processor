package click.escuela.processor.services;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import click.escuela.processor.dtos.FileError;
import click.escuela.processor.dtos.ProcessDTO;
import click.escuela.processor.dtos.ResponseCreateProcessDTO;
import click.escuela.processor.exception.ProcessException;
import click.escuela.processor.model.Process;

public interface ProcessService {

	public ResponseCreateProcessDTO saveAndRead(String name, String schoolId, MultipartFile file) throws ProcessException;
	public List<ProcessDTO> getfindBySchoolId(Integer schoolId);
	public byte[] getFileById(String processId) throws IOException;
	public String update(String processId, List<FileError> errors, String status) throws ProcessException;
	public Process getById(String processId) throws ProcessException;

}

package click.escuela.processor.services;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import click.escuela.processor.dtos.ProcessDTO;
import click.escuela.processor.exception.ProcessException;
import click.escuela.processor.model.Process;

public interface ProcessService {

	public String save(String name, Integer schoolId, Integer studentCount, MultipartFile file) throws ProcessException;
	public List<ProcessDTO> getfindBySchoolId(Integer schoolId);
	public byte[] getFileById(String processId) throws IOException;
	public String update(String status, String processId, MultipartFile file) throws ProcessException;
	public Process getById(String processId) throws ProcessException;

}

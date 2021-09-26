package click.escuela.processor.dtos;

import java.util.List;

import click.escuela.processor.api.StudentApiFile;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ResponseCreateProcessDTO {

	List<StudentApiFile> students;
	String processId;
}

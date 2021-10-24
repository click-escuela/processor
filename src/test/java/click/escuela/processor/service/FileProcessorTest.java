package click.escuela.processor.service;

import static org.assertj.core.api.Assertions.assertThat;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import click.escuela.processor.api.StudentApiFile;
import click.escuela.processor.dtos.FileError;
import click.escuela.processor.enums.EducationLevels;
import click.escuela.processor.enums.GenderType;
import click.escuela.processor.services.FileProcessorImpl;


@RunWith(MockitoJUnitRunner.class)
public class FileProcessorTest {

	private List<StudentApiFile> students =  new ArrayList<>();
	private List<FileError> errors = new ArrayList<>();
	private File file;
	private StudentApiFile student;
	private SerialBlob blob;
	
	private FileProcessorImpl fileProcessor;
	
	@Before
	public void setUp() throws Exception  {
		
		fileProcessor = new FileProcessorImpl();
		student =StudentApiFile.builder().name("Tony").surname("Liendro").document("377758269").gender(GenderType.MALE.toString())
				.cellPhone("1523554622").division("3").grade("7").level(EducationLevels.TERCIARIO.toString())
				.email("tony@gmail.com").adressApi(null).parentApi(null).line(1)
				.build();
		students.add(student);		
		List<String> errorsList = new ArrayList<>();
		String error = "Ya existe el estudiante";
		errorsList.add(error);
		FileError fileError = FileError.builder().line(student.getLine()).errors(errorsList).build();
		errors.add(fileError);
		
		file = new File("prueba.xlsx");
		
	}
	
	@Test
	public void whenReadFileIsOk() throws Exception {
		assertThat(fileProcessor.readFile(file)).isNotEmpty();
	}
	
	@Test
	public void whenWriteErrorsIsOk() throws Exception {
		byte[] bytes = FileUtils.readFileToByteArray(file); 
		blob = new SerialBlob(bytes);
		String name = "prueba";
		assertThat(fileProcessor.writeErrors(errors, blob, name)).isNotEmpty();
	}
}
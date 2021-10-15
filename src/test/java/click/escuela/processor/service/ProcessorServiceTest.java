package click.escuela.processor.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.sql.rowset.serial.SerialBlob;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import click.escuela.processor.api.ProcessApi;
import click.escuela.processor.api.StudentApiFile;
import click.escuela.processor.dtos.FileError;
import click.escuela.processor.dtos.ResponseCreateProcessDTO;
import click.escuela.processor.enums.EducationLevels;
import click.escuela.processor.enums.FileStatus;
import click.escuela.processor.enums.GenderType;
import click.escuela.processor.mapper.Mapper;
import click.escuela.processor.repository.ProcessRepository;
import click.escuela.processor.services.FileProcessorImpl;
import click.escuela.processor.services.ProcessServiceImpl;
import click.escuela.processor.model.Process;

@RunWith(MockitoJUnitRunner.class)
public class ProcessorServiceTest {
	
	@Mock
	private ProcessRepository processRepository;
	
	@Mock
	private FileProcessorImpl studentBulkUpload;


	private List<StudentApiFile> students =  new ArrayList<>();
	private List<FileError> errors = new ArrayList<>();
	private MultipartFile multipart ;
	private File file = new File("prueba.xlsx");	
	private StudentApiFile student;
	private SerialBlob blob;
	private ProcessApi processApi;
	private Process process;
	private ResponseCreateProcessDTO response;
	private Integer schoolId = 1234;
	private ProcessServiceImpl processorService = new ProcessServiceImpl();
;
	private String name = "Prueba";
	
	@Before
	public void setUp() throws Exception  {
		
		
		InputStream fileInp = new FileInputStream(file);
		multipart = new MockMultipartFile("prueba.xlsx", file.getName(), "application/vnd.ms-excel",fileInp);
		student =StudentApiFile.builder().name("Tony").surname("Liendro").document("377758269").gender(GenderType.MALE.toString())
				.cellPhone("1523554622").division("3").grade("7").level(EducationLevels.TERCIARIO.toString())
				.email("tony@gmail.com").adressApi(null).parentApi(null).line(1)
				.build();
		students.add(student);	
		
		processApi = ProcessApi.builder().name(name).file(multipart).schoolId(schoolId).studentCount(0).build();
		process = Mapper.mapperToProcess(processApi);
		process.setId(UUID.randomUUID());
		process.setStudentCount(students.size());
		process.setStartDate(LocalDateTime.now());
		process.setStatus(FileStatus.PENDING);
		process.setFile(Mapper.multipartToBlob(processApi.getFile(),processApi.getName()));
		/*List<String> errorsList = new ArrayList<>();
		String error = "Ya existe el estudiante";
		errorsList.add(error);
		FileError fileError = FileError.builder().line(student.getLine()).errors(errorsList).build();
		errors.add(fileError);*/
		
		//when(studentBulkUpload.readFile(file)).thenReturn(students);
		when(processRepository.save(process)).thenReturn(process);
		//when(process.getId().toString()).thenReturn(UUID.randomUUID().toString());
		ReflectionTestUtils.setField(processorService, "processRepository", processRepository);
		ReflectionTestUtils.setField(processorService, "studentBulkUpload", studentBulkUpload);
	}
	
	@Test
	public void whenReadFileIsOk() throws Exception {
		assertThat(processorService.saveAndRead(name,schoolId, multipart)).isNotNull();
	}
}
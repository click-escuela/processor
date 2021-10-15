package click.escuela.processor.service;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
import click.escuela.processor.enums.EducationLevels;
import click.escuela.processor.enums.FileStatus;
import click.escuela.processor.enums.GenderType;
import click.escuela.processor.enums.ProcessMessage;
import click.escuela.processor.exception.ProcessException;
import click.escuela.processor.mapper.Mapper;
import click.escuela.processor.repository.ProcessRepository;
import click.escuela.processor.services.FileProcessorImpl;
import click.escuela.processor.services.ProcessServiceImpl;
import click.escuela.processor.model.Process;
import click.escuela.processor.model.School;

@RunWith(MockitoJUnitRunner.class)
public class ProcessorServiceTest {
	
	@Mock
	private ProcessRepository processRepository;
	
	@Mock
	private FileProcessorImpl studentBulkUpload;


	private List<StudentApiFile> students =  new ArrayList<>();
	private List<Process> processes = new ArrayList<>();
	private MultipartFile multipart ;
	private File file = new File("prueba.xlsx");	
	private StudentApiFile student;
	private ProcessApi processApi;
	private Process process;
	private UUID schoolId = UUID.randomUUID();
	private ProcessServiceImpl processorService = new ProcessServiceImpl();
	private String name = "Prueba";
	private UUID id;
	
	@Before
	public void setUp() throws Exception  {
	
		InputStream fileInp = new FileInputStream(file);
		multipart = new MockMultipartFile("prueba.xlsx", file.getName(), "application/vnd.ms-excel",fileInp);
		student =StudentApiFile.builder().name("Tony").surname("Liendro").document("377758269").gender(GenderType.MALE.toString())
				.cellPhone("1523554622").division("3").grade("7").level(EducationLevels.TERCIARIO.toString())
				.email("tony@gmail.com").adressApi(null).parentApi(null).line(1)
				.build();
		students.add(student);	
		id = UUID.randomUUID();
		School school = new School();
		school.setId(schoolId);
		processApi = ProcessApi.builder().name(name).file(multipart).schoolId(schoolId.toString()).studentCount(0).build();
		process = Mapper.mapperToProcess(processApi);
		process.setId(id);
		process.setSchoolId(school);
		process.setStudentCount(students.size());
		process.setStartDate(LocalDateTime.now());
		process.setStatus(FileStatus.PENDING);
		process.setFile(Mapper.multipartToBlob(processApi.getFile(),processApi.getName()));
		processes.add(process);
		Optional<Process> optional = Optional.of(process);
		
		Mockito.when(processRepository.findBySchoolId(schoolId)).thenReturn(processes);
		Mockito.when(processRepository.findById(id)).thenReturn(optional);
		ReflectionTestUtils.setField(processorService, "processRepository", processRepository);
		ReflectionTestUtils.setField(processorService, "studentBulkUpload", studentBulkUpload);
	}
	
	@Test
	public void whenReadFileIsOk() throws Exception {
		//assertThat(processorService.saveAndRead(name,schoolId, multipart)).isNotNull();
	}
	
	@Test
	public void whenReadFileIsError() throws ProcessException {
		assertThatExceptionOfType(ProcessException.class).isThrownBy(() -> {
			processorService.saveAndRead(name,schoolId, multipart);
		}).withMessage(ProcessMessage.CREATE_ERROR.getDescription());
	}
	
	@Test
	public void whenGetBySchoolIdIsOk() {
		processorService.getfindBySchoolId(schoolId);
		verify(processRepository).findBySchoolId(schoolId);
	}
	
	@Test
	public void whenGetByIdIsOk() throws ProcessException {
		processorService.getById(id.toString());
		verify(processRepository).findById(id);
	}
	
	@Test
	public void whenGetByIdIsError() throws ProcessException {
		id = UUID.randomUUID();
		assertThatExceptionOfType(ProcessException.class).isThrownBy(() -> {
			processorService.getById(id.toString());
		}).withMessage(ProcessMessage.GET_ERROR.getDescription());
	}
	
	@Test
	public void whenFileByIdIsOk() throws ProcessException, IOException {
		processorService.getFileById(id.toString());
		verify(processRepository).findById(id);
	}
	
	@Test
	public void whenFileByIdIsError() throws ProcessException {
		id = UUID.randomUUID();
		assertThatExceptionOfType(ProcessException.class).isThrownBy(() -> {
			processorService.getFileById(id.toString());
		}).withMessage(ProcessMessage.GET_ERROR.getDescription());
	}
	
}
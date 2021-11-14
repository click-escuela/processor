package click.escuela.processor.service;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

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
import click.escuela.processor.dtos.FileError;
import click.escuela.processor.enums.EducationLevels;
import click.escuela.processor.enums.FileStatus;
import click.escuela.processor.enums.GenderType;
import click.escuela.processor.enums.ProcessMessage;
import click.escuela.processor.exception.ProcessException;
import click.escuela.processor.mapper.Mapper;
import click.escuela.processor.repository.ProcessRepository;
import click.escuela.processor.services.FileProcessorImpl;
import click.escuela.processor.services.ProcessServiceImpl;
import click.escuela.processor.services.SchoolService;
import click.escuela.processor.model.Process;
import click.escuela.processor.model.School;

@RunWith(MockitoJUnitRunner.class)
public class ProcessorServiceTest {
	
	@Mock
	private ProcessRepository processRepository;
	
	@Mock
	private FileProcessorImpl studentBulkUpload;
	
	@Mock
	private SchoolService schoolService;

	private List<StudentApiFile> students =  new ArrayList<>();
	private List<Process> processes = new ArrayList<>();
	private MultipartFile multipart ;
	private File file = new File("prueba.xlsx");	
	private StudentApiFile student;
	private ProcessApi processApi;
	private Process process;
	private String schoolId = "8ea485d3-5e06-41ac-99f4-04420068387e";
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
		processApi = ProcessApi.builder().name(name).file(multipart).schoolId(schoolId).studentCount(0).build();
		process = Mapper.mapperToProcess(processApi);
		process.setId(id);
		process.setSchool(School.builder().id(UUID.fromString(schoolId)).build());
		process.setStudentCount(students.size());
		process.setStartDate(LocalDateTime.now());
		process.setStatus(FileStatus.PENDING);
		process.setFile(Mapper.multipartToBlob(processApi.getFile(),processApi.getName()));
		processes.add(process);
		Optional<Process> optional = Optional.of(process);
		
		Mockito.when(processRepository.findBySchoolId(UUID.fromString(schoolId))).thenReturn(processes);
		Mockito.when(processRepository.findById(id)).thenReturn(optional);
		Mockito.when(processRepository.save(Mockito.any())).thenReturn(process);
		Mockito.when(schoolService.getSchool(Mockito.anyString())).thenReturn(new School());
		ReflectionTestUtils.setField(processorService, "processRepository", processRepository);
		ReflectionTestUtils.setField(processorService, "studentBulkUpload", studentBulkUpload);
		ReflectionTestUtils.setField(processorService, "schoolService", schoolService);

	}
	
	@Test
	public void whenReadFileIsOk() throws ProcessException  {
		processorService.saveAndRead(name,schoolId, multipart);
		verify(processRepository).save(Mockito.any());
	}
	
	@Test
	public void whenReadFileIsError() throws ProcessException {
		assertThatExceptionOfType(ProcessException.class).isThrownBy(() -> {
			processorService.saveAndRead(name,schoolId, null);
		}).withMessage(ProcessMessage.CREATE_ERROR.getDescription());
	}
	
	@Test
	public void whenUpdateIsOk() throws ProcessException, SQLException, IOException  {
		List<FileError> errors = new ArrayList<>();
		processorService.update(id.toString(), errors , FileStatus.PENDING.toString());
		verify(processRepository).save(Mockito.any());		
	}
	
	@Test
	public void whenUpdateIsError() throws ProcessException {
		assertThatExceptionOfType(ProcessException.class).isThrownBy(() -> {
			processorService.update(id.toString(), null , FileStatus.PENDING.toString());		
		}).withMessage(ProcessMessage.CREATE_ERROR.getDescription());
	}
	
	@Test
	public void whenGetBySchoolIdIsOk() {
		processorService.getfindBySchoolId(schoolId);
		verify(processRepository).findBySchoolId(UUID.fromString(schoolId));
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
	
	@Test
	public void createBills() {
		processorService.createBills();
		verify(schoolService).automaticCreation(Mockito.anyString(), Mockito.any());
	}
	
}
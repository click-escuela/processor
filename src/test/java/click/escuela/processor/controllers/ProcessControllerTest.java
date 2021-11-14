package click.escuela.processor.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.catalina.webresources.war.Handler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import static org.assertj.core.api.Assertions.assertThat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import click.escuela.processor.dtos.FileError;
import click.escuela.processor.dtos.ProcessDTO;
import click.escuela.processor.exception.ProcessException;
import click.escuela.processor.services.ProcessService;

@EnableWebMvc
@RunWith(MockitoJUnitRunner.class)
public class ProcessControllerTest{

	private MockMvc mockMvc;

	@InjectMocks
	private ProcessController processController;

	@Mock
	private ProcessService processService;

	private final static String URL = "/school/{schoolId}/process";
	private String schoolId = UUID.randomUUID().toString();
	private String processId = UUID.randomUUID().toString();
	private String name = "prueba.xlsx";
	private File file = new File(name);
	private MockMultipartFile multipart ;
	private ObjectMapper mapper;
	private List<ProcessDTO> proceses = new ArrayList<>(); 

	@Before
	public void setup() throws IOException, ProcessException {
		mockMvc = MockMvcBuilders.standaloneSetup(processController).setControllerAdvice(new Handler()).build();
		mapper = new ObjectMapper().findAndRegisterModules().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
				.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false)
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		InputStream fileInp = new FileInputStream(file);
		multipart = new MockMultipartFile(name, file.getName(), "application/vnd.ms-excel",fileInp);
		
		Mockito.when(processService.getfindBySchoolId(schoolId)).thenReturn(proceses);
		ReflectionTestUtils.setField(processController, "processService", processService);
	}
	
	@Test
	public void whenSaveProcessOk() throws JsonProcessingException, Exception {
		assertThat(resultProcess(MockMvcRequestBuilders.multipart(URL+"?name="+name,schoolId.toString()).file("file",multipart.getBytes()))).contains("");
	}
	
	@Test
	public void whenGetBySchoolIdIsOk() throws JsonProcessingException, Exception{
		assertThat(resultProcess(get(URL,schoolId))).contains("");
	}
	
	@Test
	public void whenGetFileByIdIsOk() throws JsonProcessingException, Exception{
		assertThat(resultProcess(get(URL+"/{processId}",schoolId,processId))).contains("");
	}
	
	@Test
	public void whenUpdateIsOk() throws JsonProcessingException, Exception{
		assertThat(resultProcess(put(URL+"/{processId}?status="+"SUCESS",schoolId,processId).content(toJson(new ArrayList<FileError>())))).contains("");
	}
	
	private String resultProcess(MockHttpServletRequestBuilder requestBuilder)
			throws JsonProcessingException, Exception {
		return mockMvc.perform(requestBuilder.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse().getContentAsString();
	}
	
	private String toJson(final Object obj) throws JsonProcessingException {
		return mapper.writeValueAsString(obj);
	}
}

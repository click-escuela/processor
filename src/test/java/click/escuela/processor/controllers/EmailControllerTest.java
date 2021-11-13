package click.escuela.processor.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import java.util.UUID;

import org.apache.catalina.webresources.war.Handler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import static org.assertj.core.api.Assertions.assertThat;
import com.fasterxml.jackson.core.JsonProcessingException;
import click.escuela.processor.enums.EmailMessage;
import click.escuela.processor.services.EmailService;

@EnableWebMvc
@RunWith(MockitoJUnitRunner.class)
public class EmailControllerTest{

	private MockMvc mockMvc;

	@InjectMocks
	private EmailController emailcontroller;

	@Mock
	private EmailService emailService;

	private final static String URL = "/school/{schoolId}/email";
	private String schoolId = UUID.randomUUID().toString();
	private String password = "password1";
	private String userName = "Tony";
	private String email = "tonyliendro@gmail.com";

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(emailcontroller).setControllerAdvice(new Handler()).build();
		
		ReflectionTestUtils.setField(emailcontroller, "emailService", emailService);
	}

	@Test
	public void whenSendEmailOk() throws JsonProcessingException, Exception {
		assertThat(resultEmail(post(URL+"?password="+password+"&userName="+userName+"&email="+email, schoolId))).contains(EmailMessage.SEND_OK.name());
	}

	private String resultEmail(MockHttpServletRequestBuilder requestBuilder)
			throws JsonProcessingException, Exception {
		return mockMvc.perform(requestBuilder.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse().getContentAsString();
	}
}

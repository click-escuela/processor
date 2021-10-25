package click.escuela.processor.services;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import click.escuela.processor.enums.EmailMessage;
import click.escuela.processor.exception.EmailException;
import click.escuela.processor.exception.ProcessException;
import click.escuela.processor.repository.EmailRepository;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceTest {

	@Mock
	private SchoolServiceImpl schoolService;

	@Mock
	private EmailRepository emailRepository;

	@Mock
	private JavaMailSender emailSender;

	private EmailServiceImpl emailService = new EmailServiceImpl();
	private String schoolId = UUID.randomUUID().toString();
	private String password = "password1";
	private String userName = "Tony";
	private String email = "tonyliendro@gmail.com";

	@Before
	public void setUp() {
		
		ReflectionTestUtils.setField(emailService, "schoolService", schoolService);
		ReflectionTestUtils.setField(emailService, "emailRepository", emailRepository);
		ReflectionTestUtils.setField(emailService, "emailSender", emailSender);
	}

	@Test
	public void whenSendEmailOk() throws ProcessException, EmailException {
		emailService.create(schoolId, password, userName, email);
		verify(emailRepository).save(Mockito.any());
	}

	@Test
	public void whenSendEmailError() throws ProcessException, EmailException {
		Mockito.when(emailRepository.save(Mockito.any())).thenReturn(new EmailException(EmailMessage.SEND_ERROR));
		assertThatExceptionOfType(EmailException.class).isThrownBy(() -> {
			emailService.create(schoolId, password, userName, email);
		}).withMessage(EmailMessage.SEND_ERROR.getDescription());
	}

}
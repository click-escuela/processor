package click.escuela.processor.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import click.escuela.processor.enums.EmailMessage;
import click.escuela.processor.exception.EmailException;
import click.escuela.processor.exception.ProcessException;
import click.escuela.processor.model.Email;
import click.escuela.processor.model.School;
import click.escuela.processor.repository.EmailRepository;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private EmailRepository emailRepository;

	@Autowired
	private SchoolServiceImpl schoolService;

	@Autowired
	private JavaMailSender emailSender;

	@Override
	public void create(String schoolId, String password, String userName, String email)
			throws ProcessException, EmailException {
		School school = schoolService.getSchool(schoolId);
		try {
			Email emailEntity = new Email();
			String subject = "Alta de usuario";
			String body = "Hola " + userName + ": \n \t \t"
					+ "Tu cuenta fue activada, las creedenciales para acceder son: \n \t \t" + "Username: "
					+ userName + " \n \t \t" + "Password: " + password + " \n \t \t" + "Saludos";
			emailEntity.setBody(body);
			emailEntity.setCreationDate(LocalDateTime.now());
			emailEntity.setSchool(school);
			emailEntity.setReceiver(email);
			emailRepository.save(emailEntity);
			sendEmail(email, subject, body);
		} catch (Exception e) {
			throw new EmailException(EmailMessage.SEND_ERROR);
		}
	}

	private void sendEmail(String email, String subject, String body) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setSubject(subject);
		message.setText(body);
		emailSender.send(message);
	}

}

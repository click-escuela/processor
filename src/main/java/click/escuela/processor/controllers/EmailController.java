package click.escuela.processor.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import click.escuela.processor.enums.EmailMessage;
import click.escuela.processor.exception.EmailException;
import click.escuela.processor.exception.ProcessException;
import click.escuela.processor.services.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping(path = "/school/{schoolId}/email")
public class EmailController {

	
	@Autowired
	private EmailService emailService;
	
	@Operation(summary = "Send Email", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PostMapping()
	public ResponseEntity<EmailMessage> sendEmail(@RequestParam(value = "password") String password,
			@RequestParam("userName") String userName, @RequestParam("email") String email,
			@Parameter(name = "School id", required = true) @PathVariable("schoolId") String schoolId) throws ProcessException, EmailException {
		emailService.create(schoolId, password, userName, email);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(EmailMessage.SEND_OK);
	}

}

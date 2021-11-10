package click.escuela.processor.exception;

import click.escuela.processor.enums.EmailMessage;

public class EmailException extends TransactionException {
	
	private static final long serialVersionUID = 1L;

	public EmailException(EmailMessage emailMessage) {
		super(emailMessage.getCode(), emailMessage.getDescription());
	}

}

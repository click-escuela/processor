package click.escuela.processor.services;

import click.escuela.processor.exception.EmailException;
import click.escuela.processor.exception.ProcessException;

public interface EmailService {
	
	public void create(String schoolId, String password, String userName, String email) throws ProcessException, EmailException;
}

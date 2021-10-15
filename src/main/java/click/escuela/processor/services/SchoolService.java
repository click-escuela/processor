package click.escuela.processor.services;


import click.escuela.processor.exception.ProcessException;
import click.escuela.processor.model.School;

public interface SchoolService {
	
	public School getSchool(String schoolId) throws ProcessException;
}

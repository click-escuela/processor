package click.escuela.processor.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.processor.api.BillApi;
import click.escuela.processor.connector.SchoolConnector;
import click.escuela.processor.enums.ProcessMessage;
import click.escuela.processor.exception.ProcessException;
import click.escuela.processor.model.School;
import click.escuela.processor.repository.SchoolRepository;

@Service
public class SchoolServiceImpl implements SchoolService{

	@Autowired
	private SchoolRepository schoolRepository;
	
	@Autowired
	private SchoolConnector schoolConnector;

	@Override
	public School getSchool(String schoolId) throws ProcessException {
		return schoolRepository.findById(UUID.fromString(schoolId))
				.orElseThrow(() -> new ProcessException(ProcessMessage.GET_STUDENT));
	}
	
	@Override
	public void automaticCreation(String schoolId, BillApi billApi) {
		schoolConnector.automaticCreation(schoolId, billApi);
	}
}

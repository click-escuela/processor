package click.escuela.processor.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.processor.enums.ProcessMessage;
import click.escuela.processor.exception.ProcessException;
import click.escuela.processor.model.School;
import click.escuela.processor.repository.SchoolRepository;

@Service
public class SchoolServiceImpl implements SchoolService{

	@Autowired
	private SchoolRepository schoolRepository;

	@Override
	public School getSchool(String schoolId) throws ProcessException {
		return schoolRepository.findById(UUID.fromString(schoolId))
				.orElseThrow(() -> new ProcessException(ProcessMessage.GET_STUDENT));
	}
}

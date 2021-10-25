package click.escuela.processor.connector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.processor.api.BillApi;
import click.escuela.processor.feign.SchoolApiController;

@Service
public class SchoolConnector {

	@Autowired
	private SchoolApiController schoolController;

	public void automaticCreation(String schoolId, BillApi billApi) {
		schoolController.automaticCreation(schoolId, billApi);
	}

}

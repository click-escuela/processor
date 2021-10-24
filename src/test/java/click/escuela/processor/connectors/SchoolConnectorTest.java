package click.escuela.processor.connectors;

import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import click.escuela.processor.api.BillApi;
import click.escuela.processor.connector.SchoolConnector;
import click.escuela.processor.feign.SchoolApiController;


@RunWith(MockitoJUnitRunner.class)
public class SchoolConnectorTest {

	@Mock
	private SchoolApiController schoolController;
	
	private SchoolConnector schoolConnector = new SchoolConnector();
	private String id;
	
	@Before
	public void setUp() {

		id = UUID.randomUUID().toString();
	
		ReflectionTestUtils.setField(schoolConnector, "schoolController", schoolController);
	}

	@Test
	public void whenAutomaticCreationOk() {
		BillApi billApi = new BillApi();
		schoolConnector.automaticCreation(id, billApi);
		verify(schoolController).automaticCreation(id, billApi);
	}

}
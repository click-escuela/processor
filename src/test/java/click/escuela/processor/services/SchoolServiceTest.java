package click.escuela.processor.services;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import click.escuela.processor.api.BillApi;
import click.escuela.processor.connector.SchoolConnector;


@RunWith(MockitoJUnitRunner.class)
public class SchoolServiceTest {

	@Mock
	private SchoolConnector schoolConnector;
	
	private SchoolServiceImpl schoolServiceImpl = new SchoolServiceImpl();
	private String id;
	
	@Before
	public void setUp() {

		id = UUID.randomUUID().toString();
	
		doNothing().when(schoolConnector).automaticCreation(Mockito.anyString(), Mockito.any());
		ReflectionTestUtils.setField(schoolServiceImpl, "schoolConnector", schoolConnector);
	}

	@Test
	public void whenAutomaticCreationOk() {
		BillApi billApi = new BillApi();
		schoolServiceImpl.automaticCreation(id, billApi);
		verify(schoolConnector).automaticCreation(id, billApi);
	}

}
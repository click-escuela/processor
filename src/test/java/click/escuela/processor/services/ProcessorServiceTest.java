package click.escuela.processor.services;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;


@RunWith(MockitoJUnitRunner.class)
public class ProcessorServiceTest {

	@Mock
	private SchoolServiceImpl schoolService;
	
	private ProcessServiceImpl processServiceImpl = new ProcessServiceImpl();
	
	@Before
	public void setUp() {	
		doNothing().when(schoolService).automaticCreation(Mockito.anyString(), Mockito.any());
		ReflectionTestUtils.setField(processServiceImpl, "schoolService", schoolService);
	}

	@Test
	public void whenCreateBills() {
		processServiceImpl.createBills();
		verify(schoolService).automaticCreation(Mockito.anyString(), Mockito.any());
	}

}
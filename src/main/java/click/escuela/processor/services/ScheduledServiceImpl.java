package click.escuela.processor.services;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import click.escuela.processor.api.BillApi;

@Service
public class ScheduledServiceImpl implements ScheduledService{

	@Autowired
	private SchoolService schoolService;
	
	
	// At 00:00:00am, on the 1st day, every month
	@Scheduled(cron="0 30 13 25 * ?", zone = "America/Argentina/Buenos_Aires")
	//@Scheduled(cron = "0 0 0 1 * ?", zone = "America/Argentina/Buenos_Aires")
	@Override
	public void createBills() {
		Date date = new Date();
		LocalDate localDate = date.toInstant().atZone(ZoneId.of("America/Argentina/Buenos_Aires")).toLocalDate();
		Integer month = localDate.getMonthValue();
		Integer year = localDate.getYear();
		BillApi billApi = new BillApi();
		billApi.setAmount(1000.0);
		billApi.setFile("Cuota " + month.toString() + " de");
		billApi.setMonth(month);
		billApi.setYear(year);
		schoolService.automaticCreation(UUID.randomUUID().toString(), billApi);
	}

}

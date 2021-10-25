package click.escuela.processor.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import click.escuela.processor.api.BillApi;

@FeignClient(name = "school-admin")
public interface SchoolApiController {

	// StudentController
	@PostMapping(value = "/school/{schoolId}/bill/automatic")
	public String automaticCreation(@PathVariable("schoolId") String schoolId,
			@RequestBody @Validated BillApi billApi);


}

package click.escuela.processor.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import click.escuela.processor.dtos.ProcessDTO;
import click.escuela.processor.exception.ProcessException;
import click.escuela.processor.services.ProcessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping(path = "/school/{schoolId}/process")
public class ProcessController {

	@Autowired
	private ProcessService processService;
	
	@Operation(summary = "Save process", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> save(@RequestParam("name") String name,
			@RequestParam("schoolId") Integer schoolId,
			@RequestParam("studentCount") Integer studentCount,
			@RequestParam("file") MultipartFile file) throws ProcessException {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(processService.save(name, schoolId, studentCount, file));
	}
	
	@Operation(summary = "Update process", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PutMapping(value = "/{processId}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> upate(@PathVariable("processId") String processId,
			@RequestParam("status") String status,
			@RequestParam("file") MultipartFile file) throws ProcessException {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(processService.update(status, processId, file));
	}
	@Operation(summary = "Get by schoolId", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProcessDTO.class))) })
	@GetMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<ProcessDTO>> getStudents(@Parameter(name = "School id", required = true) @PathVariable("schoolId") String schoolId) {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(processService.getfindBySchoolId(Integer.valueOf(schoolId)));
	}
	@Operation(summary = "Get by schoolId", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProcessDTO.class))) })
	@GetMapping(value = "/{processId}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<byte[]> getFileById(@PathVariable("processId") String processId) throws IOException {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(processService.getFileById(processId));
	}

}

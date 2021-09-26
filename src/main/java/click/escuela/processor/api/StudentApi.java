package click.escuela.processor.api;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(Include.NON_EMPTY)
@Schema(description = "Student Api")
@SuperBuilder
public class StudentApi extends PersonApi {

	@JsonProperty(value = "id", required = false)
	private String id;
	
	@NotNull(message = "Parent cannot be null")
	@JsonProperty(value = "parent", required = true)
	@Valid
	private ParentApi parentApi;

	@NotBlank(message = "Grade cannot be null")
	@JsonProperty(value = "grade", required = true)
	private String grade;

	@NotBlank(message = "Division cannot be null")
	@JsonProperty(value = "division", required = true)
	private String division;

	@NotBlank(message = "Level cannot be null")
	@JsonProperty(value = "level", required = true)
	private String level;

	@JsonProperty(value = "courseApi", required = false)
	private CourseApi courseApi;

}

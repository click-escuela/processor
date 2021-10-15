package click.escuela.processor.model;

import java.sql.Blob;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import click.escuela.processor.enums.FileStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "process")
@Entity
@Builder
public class Process {
	@Id
	@Column(name = "id", columnDefinition = "BINARY(16)")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private UUID id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "startDate", nullable = false, columnDefinition = "DATETIME")
	private LocalDateTime startDate;
	
	@Column(name = "endDate", nullable = true, columnDefinition = "DATETIME")
	private LocalDateTime endDate;

	@ManyToOne(cascade={CascadeType.ALL})
    @JoinColumn(name="school_id")
	private School schoolId;

	@Column(name = "file", nullable = false)
	private Blob file;

	@Column(name = "student_count", nullable = false)
	private Integer studentCount;

	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private FileStatus status;

}

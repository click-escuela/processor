package click.escuela.processor.model;

import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import click.escuela.processor.enums.FileType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@Table(name = "file")
//@Entity
@Builder
public class File {

	@Id
	@Column(name = "id_file", columnDefinition = "BINARY(16)")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private UUID id;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "content", nullable = false)
	private Blob content;
	
	@Column(name = "creationDate", nullable = false)
	private LocalDateTime creationDate;
	
	@Column(name = "lastUpdate", nullable = true)
	private LocalDateTime lastUpdate;
	
	@Column(name = "type", nullable = false)
	private FileType type;
}

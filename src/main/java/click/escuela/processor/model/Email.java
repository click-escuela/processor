package click.escuela.processor.model;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "email")
@Entity
@Builder
public class Email {
	@Id
	@Column(name = "id_email", columnDefinition = "BINARY(16)")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private UUID id;

	@Column(name = "body", nullable = false)
	private String body;
	
	@Column(name = "receiver", nullable = false)
	private String receiver;

	@Column(name = "creation_date", nullable = false, columnDefinition = "DATETIME")
	private LocalDateTime creationDate;
	
	@ManyToOne(cascade={CascadeType.ALL})
    @JoinColumn(name = "school_id", referencedColumnName="id_school")
	private School school;
}

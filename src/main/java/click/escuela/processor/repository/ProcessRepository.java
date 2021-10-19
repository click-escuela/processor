package click.escuela.processor.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import click.escuela.processor.model.Process;


@Repository
public interface ProcessRepository extends JpaRepository<Process, UUID>{

	public List<Process> findBySchoolId(UUID school);
}

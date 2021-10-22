package click.escuela.processor.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import click.escuela.processor.model.Email;

public interface EmailRepository extends JpaRepository<Email, UUID> {

}

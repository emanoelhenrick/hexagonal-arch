package br.com.fullcycle.hexagonal.infraestructure.repositories;

import br.com.fullcycle.hexagonal.infraestructure.models.Ticket;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TicketRepository extends CrudRepository<Ticket, Long> {

    Optional<Ticket> findByEventIdAndCustomerId(Long id, Long customerId);
}

package br.com.fullcycle.hexagonal.infraestructure.repositories;

import br.com.fullcycle.hexagonal.infraestructure.models.Event;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<Event, Long> {

}

package br.com.fullcycle.hexagonal.infraestructure.services;

import br.com.fullcycle.hexagonal.infraestructure.models.Event;
import br.com.fullcycle.hexagonal.infraestructure.models.Ticket;
import br.com.fullcycle.hexagonal.infraestructure.repositories.EventRepository;
import br.com.fullcycle.hexagonal.infraestructure.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Transactional
    public Event save(Event event) {
        return eventRepository.save(event);
    }

    public Optional<Event> findById(Long id) {
        return eventRepository.findById(id);
    }
    
    public Optional<Ticket> findTicketByEventIdAndCustomerId(Long id, Long customerId) {
        return ticketRepository.findByEventIdAndCustomerId(id, customerId);
    }
}

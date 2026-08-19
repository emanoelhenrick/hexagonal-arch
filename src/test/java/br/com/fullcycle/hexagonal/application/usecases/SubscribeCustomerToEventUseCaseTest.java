package br.com.fullcycle.hexagonal.application.usecases;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.infraestructure.models.Customer;
import br.com.fullcycle.hexagonal.infraestructure.models.Event;
import br.com.fullcycle.hexagonal.infraestructure.models.Ticket;
import br.com.fullcycle.hexagonal.infraestructure.models.TicketStatus;
import br.com.fullcycle.hexagonal.infraestructure.services.CustomerService;
import br.com.fullcycle.hexagonal.infraestructure.services.EventService;
import io.hypersistence.tsid.TSID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import static org.mockito.Mockito.*;

class SubscribeCustomerToEventUseCaseTest {

    @Test
    @DisplayName("Deve comprar um ticket de um evento")
    public void testReserveTicket() throws Exception {
        //given
        final var expectedTicketsSize = 1;
        final var customerId = TSID.fast().toLong();
        final var eventId = TSID.fast().toLong();

        final var aEvent = new Event();
        aEvent.setId(eventId);
        aEvent.setName("Disney");
        aEvent.setTotalSpots(10);

        final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(aEvent.getId(), customerId);

        //when
        final var customerService = mock(CustomerService.class);
        final var eventService = mock(EventService.class);

        when(customerService.findById(customerId)).thenReturn(Optional.of(new Customer()));
        when(eventService.findById(eventId)).thenReturn(Optional.of(aEvent));
        when(eventService.findTicketByEventIdAndCustomerId(eventId, customerId)).thenReturn(Optional.empty());
        when(eventService.save(any())).thenAnswer(a -> {
            final var e = a.getArgument(0, Event.class);
            Assertions.assertEquals(expectedTicketsSize, e.getTickets().size());
            return e;
        });

        final var useCase = new SubscribeCustomerToEventUseCase(customerService, eventService);
        final var output = useCase.execute(subscribeInput);
        //then

        Assertions.assertEquals(eventId, output.eventId());
        Assertions.assertNotNull(output.reservationDate());
        Assertions.assertEquals(TicketStatus.PENDING.name(), output.ticketStatus());
    }

    @Test
    @DisplayName("Mesmo cliente nao pode comprar mais de um ticket por evento")
    public void testReserveTicketMoreThanOnce() throws Exception {
        //given
        final var expectedError = "Email already registred";
        final var customerId = TSID.fast().toLong();
        final var eventId = TSID.fast().toLong();

        final var aEvent = new Event();
        aEvent.setId(eventId);
        aEvent.setName("Disney");
        aEvent.setTotalSpots(10);

        final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(aEvent.getId(), customerId);

        //when
        final var customerService = mock(CustomerService.class);
        final var eventService = mock(EventService.class);

        when(customerService.findById(customerId)).thenReturn(Optional.of(new Customer()));
        when(eventService.findById(eventId)).thenReturn(Optional.of(aEvent));
        when(eventService.findTicketByEventIdAndCustomerId(eventId, customerId)).thenReturn(Optional.of(new Ticket()));


        final var useCase = new SubscribeCustomerToEventUseCase(customerService, eventService);
        final var actualException = Assertions.assertThrows(
            ValidationException.class, () -> useCase.execute(subscribeInput)
        );

        //then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }

    @Test
    @DisplayName("Um cliente nao pode comprar de um evento sem vagas")
    public void testReserveTicketWithoutSlots() throws Exception {
        //given
        final var expectedError = "Event sold out";
        final var customerId = TSID.fast().toLong();
        final var eventId = TSID.fast().toLong();

        final var aEvent = new Event();
        aEvent.setId(eventId);
        aEvent.setName("Disney");
        aEvent.setTotalSpots(0);

        final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(aEvent.getId(), customerId);

        //when
        final var customerService = mock(CustomerService.class);
        final var eventService = mock(EventService.class);

        when(customerService.findById(customerId)).thenReturn(Optional.of(new Customer()));
        when(eventService.findById(eventId)).thenReturn(Optional.of(aEvent));
        when(eventService.findTicketByEventIdAndCustomerId(eventId, customerId)).thenReturn(Optional.empty());


        final var useCase = new SubscribeCustomerToEventUseCase(customerService, eventService);
        final var actualException = Assertions.assertThrows(
        ValidationException.class, () -> useCase.execute(subscribeInput)
        );

        //then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }

    @Test
    @DisplayName("Nao deve comprar um ticket de um cliente que nao existe")
    public void testReserveTicketWithoutCustomer() throws Exception {
        //given
        final var expectedError = "Customer not found";
        final var customerId = TSID.fast().toLong();
        final var eventId = TSID.fast().toLong();

        final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(eventId, customerId);

        //when
        final var customerService = mock(CustomerService.class);
        final var eventService = mock(EventService.class);

        when(customerService.findById(customerId)).thenReturn(Optional.empty());

        final var useCase = new SubscribeCustomerToEventUseCase(customerService, eventService);
        final var actualException = Assertions.assertThrows(
            ValidationException.class, () -> useCase.execute(subscribeInput)
        );

        //then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }

    @Test
    @DisplayName("Nao deve comprar um ticket de um evento que nao existe")
    public void testReserveTicketWithoutEvent() throws Exception {
        //given
        final var expectedError = "Event not found";
        final var customerId = TSID.fast().toLong();
        final var eventId = TSID.fast().toLong();

        final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(eventId, customerId);

        //when
        final var customerService = mock(CustomerService.class);
        final var eventService = mock(EventService.class);

        when(customerService.findById(customerId)).thenReturn(Optional.of(new Customer()));
        when(eventService.findById(eventId)).thenReturn(Optional.empty());

        final var useCase = new SubscribeCustomerToEventUseCase(customerService, eventService);
        final var actualException = Assertions.assertThrows(
            ValidationException.class, () -> useCase.execute(subscribeInput)
        );

        //then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }
}
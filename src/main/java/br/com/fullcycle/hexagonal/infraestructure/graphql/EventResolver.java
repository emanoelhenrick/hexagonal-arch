package br.com.fullcycle.hexagonal.infraestructure.graphql;

import br.com.fullcycle.hexagonal.application.usecases.CreateEventUseCase;
import br.com.fullcycle.hexagonal.application.usecases.SubscribeCustomerToEventUseCase;
import br.com.fullcycle.hexagonal.infraestructure.dtos.SubscribeDTO;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class EventResolver {

    private final CreateEventUseCase createEventUseCase;
    private final SubscribeCustomerToEventUseCase subscribeCustomerToEventUseCase;

    public EventResolver(CreateEventUseCase createEventUseCase, SubscribeCustomerToEventUseCase subscribeCustomerToEventUseCase) {
        this.createEventUseCase = createEventUseCase;
        this.subscribeCustomerToEventUseCase = subscribeCustomerToEventUseCase;
    }

    @MutationMapping
    public CreateEventUseCase.Output createEvent(@Argument CreateEventUseCase.Input input) {
        return createEventUseCase.execute(new CreateEventUseCase.Input(
            input.date(), input.name(), input.partnerId(), input.totalSpots()
        ));
    }

    @MutationMapping
    public SubscribeCustomerToEventUseCase.Output subscribeCustomerToEvent(@Argument SubscribeDTO input) {
        return subscribeCustomerToEventUseCase.execute(new SubscribeCustomerToEventUseCase.Input(
            input.customerId(), input.eventId()
        ));
    }

}

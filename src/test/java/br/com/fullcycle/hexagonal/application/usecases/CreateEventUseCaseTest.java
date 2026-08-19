package br.com.fullcycle.hexagonal.application.usecases;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.infraestructure.models.Event;
import br.com.fullcycle.hexagonal.infraestructure.models.Partner;
import br.com.fullcycle.hexagonal.infraestructure.services.EventService;
import br.com.fullcycle.hexagonal.infraestructure.services.PartnerService;
import io.hypersistence.tsid.TSID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CreateEventUseCaseTest {

    @Test
    @DisplayName("Deve criar um evento")
    public void testCreateEvent() throws Exception {
        //given
        final var expectedDate = "2021-01-01";
        final var expectedName = "Disney on Ice";
        final var expectedTotalSpots = 100;
        final var expectedPartnerId = TSID.fast().toLong();
        final var createInput = new CreateEventUseCase.Input(
            expectedDate, expectedName, expectedPartnerId,expectedTotalSpots
        );

        //when
        final var partnerService = Mockito.mock(PartnerService.class);
        final var eventService = Mockito.mock(EventService.class);

        when(eventService.save(any())).thenAnswer(a -> {
            final var e = a.getArgument(0, Event.class);
            e.setId(TSID.fast().toLong());
            return e;
        });

        when(partnerService.findById(expectedPartnerId)).thenReturn(Optional.of(new Partner()));

        final var useCase = new CreateEventUseCase(partnerService, eventService);
        final var output = useCase.execute(createInput);

        //then
        Assertions.assertNotNull(output.id());
        Assertions.assertEquals(expectedDate, output.date());
        Assertions.assertEquals(expectedName, output.name());
        Assertions.assertEquals(expectedTotalSpots, output.totalSpots());
        Assertions.assertEquals(expectedPartnerId, output.partnerId());
    }

    @Test
    @DisplayName("Nao deve criar um evento quando partner nao for encontrado")
    public void testShouldNotCreateEventWhenPartnerInvalid() throws Exception {
        //given
        final var expectedDate = "2021-01-01";
        final var expectedName = "Disney on Ice";
        final var expectedTotalSpots = 100;
        final var expectedPartnerId = TSID.fast().toLong();
        final var expectedError = "Partner not found";
        final var createInput = new CreateEventUseCase.Input(
        expectedDate, expectedName, expectedPartnerId, expectedTotalSpots
        );

        //when
        final var partnerService = Mockito.mock(PartnerService.class);
        final var eventService = Mockito.mock(EventService.class);

        when(partnerService.findById(expectedPartnerId)).thenReturn(Optional.empty());

        final var useCase = new CreateEventUseCase(partnerService, eventService);
        final var actualException = Assertions.assertThrows(
            ValidationException.class, () -> useCase.execute(createInput)
        );

        //then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }
}
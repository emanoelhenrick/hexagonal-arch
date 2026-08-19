package br.com.fullcycle.hexagonal.application.usecases;

import br.com.fullcycle.hexagonal.IntegrationTest;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.infraestructure.models.Partner;
import br.com.fullcycle.hexagonal.infraestructure.repositories.EventRepository;
import br.com.fullcycle.hexagonal.infraestructure.repositories.PartnerRepository;
import br.com.fullcycle.hexagonal.infraestructure.services.EventService;
import br.com.fullcycle.hexagonal.infraestructure.services.PartnerService;
import io.hypersistence.tsid.TSID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.mockito.Mockito.when;

class CreateEventUseCaseIT extends IntegrationTest {

    @Autowired
    private CreateEventUseCase useCase;

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private EventRepository eventRepository;

    @BeforeEach
    void tearDown() {
        partnerRepository.deleteAll();
        eventRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve criar um evento")
    public void testCreateEvent() throws Exception {
        //given
        final var partner = createPartner("41536538000100", "john.doe@gmail.com", "John Doe");
        final var expectedDate = "2021-01-01";
        final var expectedName = "Disney on Ice";
        final var expectedTotalSpots = 100;
        final var expectedPartnerId = partner.getId();

        final var createInput = new CreateEventUseCase.Input(
            expectedDate, expectedName, expectedPartnerId,expectedTotalSpots
        );

        //when
        final var output = useCase.execute(createInput);

        //then
        Assertions.assertNotNull(output.id());
        Assertions.assertEquals(expectedDate, output.date());
        Assertions.assertEquals(expectedName, output.name());
        Assertions.assertEquals(expectedTotalSpots, output.totalSpots());
        Assertions.assertEquals(expectedPartnerId, output.partnerId());
    }

    private Partner createPartner(String cnpj, String email, String name) {
        final var aPartner = new Partner();
        aPartner.setCnpj(cnpj);
        aPartner.setEmail(email);
        aPartner.setName(name);
        return partnerRepository.save(aPartner);
    }
}
package br.com.fullcycle.hexagonal.application.usecases;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.infraestructure.models.Partner;
import br.com.fullcycle.hexagonal.infraestructure.services.PartnerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CreatePartnerUseCaseTest {

    @Test
    @DisplayName("Deve criar um parceiro")
    public void testCreatePartner() throws Exception {
        //given
        final var expectedCnpj = "41536538000100";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";

        final var createInput = new CreatePartnerUseCase.Input(expectedName, expectedCnpj, expectedEmail);

        //when
        final var partnerService = Mockito.mock(PartnerService.class);
        when(partnerService.findByCnpj(expectedCnpj)).thenReturn(Optional.empty());
        when(partnerService.findByEmail(expectedEmail)).thenReturn(Optional.empty());
        when(partnerService.save(any())).thenAnswer(a -> {
            var partner = a.getArgument(0, Partner.class);
            partner.setId(UUID.randomUUID().getMostSignificantBits());
            return partner;
        });
        final var useCase = new CreatePartnerUseCase(partnerService);
        final var output = useCase.execute(createInput);


        //then
        Assertions.assertNotNull(output.id());
        Assertions.assertEquals(expectedCnpj, output.cnpj());
        Assertions.assertEquals(expectedEmail, output.email());
        Assertions.assertEquals(expectedName, output.name());

    }

    @Test
    @DisplayName("Não deve cadastrar um parceiro com CNPJ duplicado")
    public void testCreateWithDuplicatedCNPJShouldFail() throws Exception {
        //given
        final var expectedCnpj = "41536538000100";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";
        final var expectedError = "Partner already exists";

        final var createInput = new CreatePartnerUseCase.Input(expectedName, expectedCnpj, expectedEmail);

        final var aPartner = new Partner();
        aPartner.setId(UUID.randomUUID().getMostSignificantBits());
        aPartner.setCnpj(expectedCnpj);
        aPartner.setEmail(expectedEmail);
        aPartner.setName(expectedName);

        //when
        final var partnerService = Mockito.mock(PartnerService.class);
        when(partnerService.findByCnpj(expectedCnpj)).thenReturn(Optional.of(aPartner));

        final var useCase = new CreatePartnerUseCase(partnerService);
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(createInput));


        //then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve cadastrar um parceiro com e-mail duplicado")
    public void testCreateWithDuplicatedEmailShouldFail() throws Exception {
        //given
        final var expectedCnpj = "41536538000100";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";
        final var expectedError = "Partner already exists";

        final var createInput = new CreatePartnerUseCase.Input(expectedName, expectedCnpj, expectedEmail);

        final var aCustomer = new Partner();
        aCustomer.setId(UUID.randomUUID().getMostSignificantBits());
        aCustomer.setCnpj(expectedCnpj);
        aCustomer.setEmail(expectedEmail);
        aCustomer.setName(expectedName);

        //when
        final var partnerService = Mockito.mock(PartnerService.class);
        when(partnerService.findByEmail(expectedEmail)).thenReturn(Optional.of(aCustomer));

        final var useCase = new CreatePartnerUseCase(partnerService);
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(createInput));


        //then
        Assertions.assertEquals(expectedError, actualException.getMessage());

    }

}

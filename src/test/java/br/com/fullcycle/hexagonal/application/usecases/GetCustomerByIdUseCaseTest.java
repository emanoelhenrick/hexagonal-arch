package br.com.fullcycle.hexagonal.application.usecases;

import br.com.fullcycle.hexagonal.infraestructure.models.Customer;
import br.com.fullcycle.hexagonal.infraestructure.services.CustomerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;

public class GetCustomerByIdUseCaseTest {

    @Test
    @DisplayName("Deve obter um cliente por id")
    public void testGetById() throws Exception {
        //given
        final var expectedId = UUID.randomUUID().getMostSignificantBits();
        final var expectedCpf = "12345678901";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";

        final var aCustomer = new Customer();
        aCustomer.setId(expectedId);
        aCustomer.setCpf(expectedCpf);
        aCustomer.setEmail(expectedEmail);
        aCustomer.setName(expectedName);

        final var input = new GetCustomerByIdUseCase.Input(expectedId);

        //when
        final var customerService = Mockito.mock(CustomerService.class);
        when(customerService.findById(expectedId)).thenReturn(Optional.of(aCustomer));

        final var useCase = new GetCustomerByIdUseCase(customerService);
        final var output = useCase.execute(input).get();


        //then
        Assertions.assertEquals(expectedId, output.id());
        Assertions.assertEquals(expectedCpf, output.cpf());
        Assertions.assertEquals(expectedEmail, output.email());
        Assertions.assertEquals(expectedName, output.name());
    }

    @Test
    @DisplayName("Deve obter vazio ao buscar por um cliente que nao existe por id")
    public void testGetByIdInvalid() throws Exception {
        //given
        final var expectedId = UUID.randomUUID().getMostSignificantBits();


        final var input = new GetCustomerByIdUseCase.Input(expectedId);

        //when
        final var customerService = Mockito.mock(CustomerService.class);
        when(customerService.findById(expectedId)).thenReturn(Optional.empty());

        final var useCase = new GetCustomerByIdUseCase(customerService);
        final var output = useCase.execute(input);


        //then
        Assertions.assertTrue(output.isEmpty());
    }
}

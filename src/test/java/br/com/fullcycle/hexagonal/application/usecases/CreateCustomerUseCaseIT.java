package br.com.fullcycle.hexagonal.application.usecases;

import br.com.fullcycle.hexagonal.IntegrationTest;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.infraestructure.Main;
import br.com.fullcycle.hexagonal.infraestructure.models.Customer;
import br.com.fullcycle.hexagonal.infraestructure.repositories.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

public class CreateCustomerUseCaseIT extends IntegrationTest {

    @Autowired
    private CreateCustomerUseCase useCase;

    @Autowired
    private CustomerRepository customerRepository;

    @AfterEach
    void tearDown() {
        customerRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve criar um cliente")
    public void testCreateCustomer() throws Exception {
        //given
        final var expectedCpf = "12345678901";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";

        final var createInput = new CreateCustomerUseCase.Input(expectedCpf, expectedEmail, expectedName);

        //when
        final var output = useCase.execute(createInput);

        //then
        Assertions.assertNotNull(output.id());
        Assertions.assertEquals(expectedCpf, output.cpf());
        Assertions.assertEquals(expectedEmail, output.email());
        Assertions.assertEquals(expectedName, output.name());
    }

    @Test
    @DisplayName("Não deve cadastrar um cliente com CPF duplicado")
    public void testCreateWithDuplicatedCPFShouldFail() throws Exception {
        //given
        final var expectedCpf = "12345678901";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";
        final var expectedError = "Customer already exists";

        createCustomer(expectedCpf, expectedEmail, expectedName);

        final var createInput = new CreateCustomerUseCase.Input(expectedCpf, expectedEmail, expectedName);

        //when
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(createInput));

        //then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }



    @Test
    @DisplayName("Não deve cadastrar um cliente com e-mail duplicado")
    public void testCreateWithDuplicatedEmailShouldFail() throws Exception {
        //given
        final var expectedCpf = "12345878901";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";
        final var expectedError = "Customer already exists";

        createCustomer("12345878976", expectedEmail, expectedName);

        final var createInput = new CreateCustomerUseCase.Input(expectedCpf, expectedEmail, expectedName);

        //when
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(createInput));

        //then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }

    private Customer createCustomer(String cpf, String email, String name) {
        final var aCustomer = new Customer();
        aCustomer.setCpf(cpf);
        aCustomer.setEmail(email);
        aCustomer.setName(name);
        return customerRepository.save(aCustomer);
    }

}

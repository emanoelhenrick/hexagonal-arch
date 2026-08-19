package br.com.fullcycle.hexagonal.infraestructure.repositories;

import br.com.fullcycle.hexagonal.infraestructure.models.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

    Optional<Customer> findByCpf(String cpf);

    Optional<Customer> findByEmail(String email);
}

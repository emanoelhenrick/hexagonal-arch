package br.com.fullcycle.hexagonal.infraestructure.repositories;

import br.com.fullcycle.hexagonal.infraestructure.models.Partner;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PartnerRepository extends CrudRepository<Partner, Long> {

    Optional<Partner> findByCnpj(String cnpj);

    Optional<Partner> findByEmail(String email);
}
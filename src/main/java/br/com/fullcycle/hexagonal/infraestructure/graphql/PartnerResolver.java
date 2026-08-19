package br.com.fullcycle.hexagonal.infraestructure.graphql;

import br.com.fullcycle.hexagonal.application.usecases.CreatePartnerUseCase;
import br.com.fullcycle.hexagonal.application.usecases.GetPartnerByIdUseCase;
import br.com.fullcycle.hexagonal.infraestructure.dtos.PartnerDTO;
import br.com.fullcycle.hexagonal.infraestructure.services.PartnerService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Objects;

@Controller
public class PartnerResolver {

    private final CreatePartnerUseCase createPartnerUseCase;
    private final GetPartnerByIdUseCase getPartnerByIdUseCase;

    public PartnerResolver(CreatePartnerUseCase createPartnerUseCase, GetPartnerByIdUseCase getPartnerByIdUseCase) {
        this.createPartnerUseCase = createPartnerUseCase;
        this.getPartnerByIdUseCase = getPartnerByIdUseCase;
    }

    @MutationMapping
    public CreatePartnerUseCase.Output createPartner(@Argument PartnerDTO dto) {
        final var input = new CreatePartnerUseCase.Input(dto.getName(), dto.getCnpj(), dto.getEmail());
        return createPartnerUseCase.execute(input);
    }

    @QueryMapping
    public GetPartnerByIdUseCase.Output partnerOfId(@Argument Long id) {
        return getPartnerByIdUseCase.execute(new GetPartnerByIdUseCase.Input(id))
            .orElse(null);
    }

}

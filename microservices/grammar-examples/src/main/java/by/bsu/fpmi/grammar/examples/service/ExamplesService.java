package by.bsu.fpmi.grammar.examples.service;

import by.bsu.fpmi.grammar.examples.dto.GrammarRequest;
import by.bsu.fpmi.grammar.examples.dto.GrammarResponse;
import by.bsu.fpmi.grammar.examples.dto.RequestedPage;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ExamplesService {

    Optional<GrammarResponse> getExampleById(String id);

    String insertExample(GrammarRequest exampleRequest);

    RequestedPage<GrammarResponse> getAll(Pageable pageRequest);

    void deleteById(String id);
}

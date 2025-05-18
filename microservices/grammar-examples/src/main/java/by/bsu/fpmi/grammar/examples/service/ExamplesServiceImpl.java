package by.bsu.fpmi.grammar.examples.service;

import by.bsu.fpmi.grammar.examples.dto.GrammarRequest;
import by.bsu.fpmi.grammar.examples.dto.GrammarResponse;
import by.bsu.fpmi.grammar.examples.dto.RequestedPage;
import by.bsu.fpmi.grammar.examples.entity.Grammar;
import by.bsu.fpmi.grammar.examples.mapper.GrammarMapper;
import by.bsu.fpmi.grammar.examples.repository.GrammarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExamplesServiceImpl implements ExamplesService {

    private final GrammarMapper grammarMapper;
    private final GrammarRepository grammarRepository;

    @Override
    public Optional<GrammarResponse> getExampleById(String id) {

        return grammarRepository.findById(id).map(grammarMapper::toResponse);
    }

    @Override
    public String insertExample(GrammarRequest exampleRequest) {

        Grammar grammar = grammarMapper.toEntity(exampleRequest);

        return grammarRepository.insert(grammar).getId();
    }

    @Override
    public RequestedPage<GrammarResponse> getAll(Pageable pageRequest) {

        pageRequest = PageRequest.ofSize(pageRequest.getPageSize())
                .withPage(pageRequest.getPageNumber())
                .withSort(Sort.by(Sort.Direction.ASC, "id"));

        Page<Grammar> selectedPage = grammarRepository.findAll(pageRequest);

        return RequestedPage.fromPage(selectedPage)
                .map(grammarMapper::toResponse);
    }

    @Override
    public void deleteById(String id) {

        grammarRepository.deleteById(id);
    }
}

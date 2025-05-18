package by.bsu.fpmi.grammar.examples.controller;

import by.bsu.fpmi.grammar.examples.dto.GrammarRequest;
import by.bsu.fpmi.grammar.examples.dto.GrammarResponse;
import by.bsu.fpmi.grammar.examples.dto.RequestedPage;
import by.bsu.fpmi.grammar.examples.service.ExamplesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/examples")
public class ExamplesController {

    private final ExamplesService examplesService;

    private static final int DEFAULT_PAGE_SIZE = 3;
    private static final int DEFAULT_PAGE_NUMBER = 0;


    @GetMapping("/{id}")
    public ResponseEntity<GrammarResponse> getExample(@PathVariable("id") String id) {

        return examplesService.getExampleById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping
    public RequestedPage<GrammarResponse> getPage(
            @PageableDefault(size = DEFAULT_PAGE_SIZE, page = DEFAULT_PAGE_NUMBER) Pageable pageable
    ) {

        return examplesService.getAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
    }

    @PostMapping
    public ResponseEntity<Void> insertExample(@RequestBody @Valid GrammarRequest grammarRequest) {

        String id = examplesService.insertExample(grammarRequest);

        return ResponseEntity.created(URI.create("/" + id)).build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("id") String id) {

        examplesService.deleteById(id);
    }
}

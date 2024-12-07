package by.bsu.fpmi.cfg.controller;

import by.bsu.fpmi.cfg.dto.CFGRequest;
import by.bsu.fpmi.cfg.dto.CFGResponse;
import by.bsu.fpmi.cfg.dto.RequestedPage;
import by.bsu.fpmi.cfg.service.ExamplesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/examples")
public class ExamplesController {

    private final ExamplesService examplesService;

    private static final int DEFAULT_PAGE_SIZE = 3;
    private static final int DEFAULT_PAGE_NUMBER = 0;


    @GetMapping("/{id}")
    public ResponseEntity<CFGResponse> getExample(@PathVariable("id") String id) {

        return examplesService.getExampleById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping
    public RequestedPage<CFGResponse> getPage(
            @PageableDefault(size = DEFAULT_PAGE_SIZE, page = DEFAULT_PAGE_NUMBER) Pageable pageable
    ) {

        return examplesService.getAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
    }

    @PostMapping
    public ResponseEntity<Void> insertExample(@RequestBody CFGRequest cfgRequest) {

        String id = examplesService.insertExample(cfgRequest);

        return ResponseEntity.created(URI.create("/" + id)).build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("id") String id) {

        examplesService.deleteById(id);
    }
}

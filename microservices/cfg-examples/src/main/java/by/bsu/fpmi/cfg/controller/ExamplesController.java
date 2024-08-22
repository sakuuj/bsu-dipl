package by.bsu.fpmi.cfg.controller;

import by.bsu.fpmi.cfg.dto.CFGRequest;
import by.bsu.fpmi.cfg.dto.CFGResponse;
import by.bsu.fpmi.cfg.dto.Page;
import by.bsu.fpmi.cfg.dto.PageRequest;
import by.bsu.fpmi.cfg.service.ExamplesService;
import com.google.common.net.HttpHeaders;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
@RequestMapping("/examples")
public class ExamplesController {

    private final ExamplesService examplesService;

    private static final long DEFAULT_PAGE_SIZE = 3;
    private static final long DEFAULT_PAGE_NUMBER = 3;


    @GetMapping("/{id}")
    public ResponseEntity<CFGResponse> getExample(@PathVariable("id") long id) {

        ResponseEntity<CFGResponse> cfgResponseResponseEntity = examplesService.getExampleById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
        return cfgResponseResponseEntity;
    }



    @GetMapping
    public ResponseEntity<Page<CFGResponse>> getPage(@RequestParam(value = "page-number", defaultValue = "" + DEFAULT_PAGE_NUMBER) long pageNumber,
                                                     @RequestParam(value = "page-size", defaultValue = "" + DEFAULT_PAGE_SIZE) long pageSize) {

        ResponseEntity<Page<CFGResponse>> body = ResponseEntity.ok()
                .body(examplesService.getPage(new PageRequest(pageNumber, pageSize)));

        return body;

    }

    @PostMapping
    public ResponseEntity<Void> insertExample(@RequestBody CFGRequest cfgRequest) {
        long id = examplesService.insertExample(cfgRequest);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(id)
                        .toUri())
                .build();
    }
}

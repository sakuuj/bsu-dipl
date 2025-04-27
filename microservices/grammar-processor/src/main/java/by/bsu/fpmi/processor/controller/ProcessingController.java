package by.bsu.fpmi.processor.controller;

import by.bsu.fpmi.processor.dto.First1Response;
import by.bsu.fpmi.processor.dto.FollowResponse;
import by.bsu.fpmi.processor.dto.GrammarRequest;
import by.bsu.fpmi.processor.dto.GrammarResponse;
import by.bsu.fpmi.processor.dto.ParsingRequest;
import by.bsu.fpmi.processor.dto.ParsingResponse;
import by.bsu.fpmi.processor.service.ProcessorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
//@CrossOrigin(origins = "http://localhost:3000", methods = {RequestMethod.POST})
@RestController
@RequiredArgsConstructor
@RequestMapping(
        path = "/processor",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class ProcessingController {

    private final ProcessorService processorService;

    @PostMapping("/first1")
    public First1Response findFirst1(@RequestBody @Valid GrammarRequest request) {

        return processorService.normalizeAndCalculateFirst1(request);
    }

    @PostMapping("/follow")
    public FollowResponse findFollow(@RequestBody @Valid GrammarRequest request) {

        return processorService.normalizeAndCalculateFollow(request);
    }

    @PostMapping("/retain-generating-nt")
    public GrammarResponse retainGeneratingNonTerminals(@RequestBody @Valid GrammarRequest request) {

        return processorService.retainGeneratingNonTerminals(request);
    }

    @PostMapping("/retain-reachable-nt")
    public GrammarResponse retainReachableNonTerminals(@RequestBody @Valid GrammarRequest request) {

        return processorService.retainReachableNonTerminals(request);
    }

    @PostMapping("/parse")
    public ParsingResponse parseText(@RequestBody @Valid ParsingRequest request) {

        return processorService.parseInput(request);
    }

}

package by.bsu.fpmi.processor.controller;

import by.bsu.fpmi.processor.dto.CFGRequest;
import by.bsu.fpmi.processor.dto.CFGResponse;
import by.bsu.fpmi.processor.dto.First1Response;
import by.bsu.fpmi.processor.dto.FollowResponse;
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
    public First1Response findFirst1(@RequestBody @Valid CFGRequest request) {

        return processorService.normalizeAndCalculateFirst1(request);
    }

    @PostMapping("/follow")
    public FollowResponse findFollow(@RequestBody @Valid CFGRequest request) {

        return processorService.normalizeAndCalculateFollow(request);
    }

    @PostMapping("/retain-generating-nt")
    public CFGResponse retainGeneratingNonTerminals(@RequestBody @Valid CFGRequest request) {

        return processorService.retainGeneratingNonTerminals(request);
    }

    @PostMapping("/retain-reachable-nt")
    public CFGResponse retainReachableNonTerminals(@RequestBody @Valid CFGRequest request) {

        return processorService.retainReachableNonTerminals(request);
    }

}

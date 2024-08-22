package by.bsu.fpmi.processor.controller;

import by.bsu.fpmi.processor.dto.CFGRequest;
import by.bsu.fpmi.processor.dto.CFGResponse;
import by.bsu.fpmi.processor.service.ProcessorService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
@RequestMapping
public class ProcessingController {

    private final ProcessorService processorService;

    @PostMapping(value = "processor/first-k/{k}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CFGResponse> findFirstK(@RequestBody @Valid CFGRequest request,
                                                 @PathVariable("k") @Min(1) @Max(2) int k) {

        return ResponseEntity.ok()
                .body(processorService.normalizeAndCalculateFirstK(request, k));
    }

}

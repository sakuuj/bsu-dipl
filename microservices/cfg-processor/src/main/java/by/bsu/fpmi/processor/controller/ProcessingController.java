package by.bsu.fpmi.processor.controller;

import by.bsu.fpmi.processor.dto.CFGRequest;
import by.bsu.fpmi.processor.dto.CFGResponse;
import by.bsu.fpmi.processor.service.ProcessorService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/processor")
public class ProcessingController {

    private final ProcessorService processorService;

    @PostMapping("/first-k/{k}")
    public ResponseEntity<CFGResponse> findFirstK(@RequestBody @Valid CFGRequest request,
                                                 @PathVariable("k") @Min(1) @Max(2) int k) {

        return ResponseEntity.ok(processorService.normalizeAndCalculateFirstK(request, k));
    }

}

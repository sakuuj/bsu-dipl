package by.bsu.fpmi.firstkservice.controller;

import by.bsu.fpmi.firstkservice.dto.ContextFreeGrammarRequest;
import by.bsu.fpmi.firstkservice.service.FirstKService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/first-k")
public class FirstKController {
    private final FirstKService firstKService;

    @GetMapping("/{k}")
    public Map<Character, Set<String>> findFirstK(@RequestBody @Valid ContextFreeGrammarRequest request,
                                                  @PathVariable("k") int k) {

        log.info("REQUEST ISSUED: " + request.toString());

        Map<Character, Set<String>> response = firstKService.findFirstK(k, request);

        log.info("RESPONSE IS: " + response);

        return response;
    }

}

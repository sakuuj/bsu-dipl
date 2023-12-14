package by.bsu.fpmi.firstkservice.controller;

import by.bsu.fpmi.firstkservice.dto.ContextFreeGrammarRequest;
import by.bsu.fpmi.firstkservice.service.FirstKService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/firstk")
public class FirstKController {
    private final FirstKService firstKService;

    @GetMapping("/{k}")
    public Map<Character, Set<String>> findFirstK(@RequestBody @Valid ContextFreeGrammarRequest request,
                                                  @PathVariable("k") int k) {
        return firstKService.findFirstK(k, request);
    }

}

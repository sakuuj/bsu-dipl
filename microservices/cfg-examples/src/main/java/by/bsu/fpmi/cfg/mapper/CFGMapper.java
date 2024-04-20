package by.bsu.fpmi.cfg.mapper;

import by.bsu.fpmi.cfg.dto.CFGRequest;
import by.bsu.fpmi.cfg.dto.CFGResponse;
import by.bsu.fpmi.cfg.entity.CFG;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CFGMapper {

    private final ObjectMapper objectMapper;

    public CFG fromRequest(CFGRequest request) {
        try {
            String content = objectMapper.writeValueAsString(request);
            return new CFG(-1L, content, LocalDateTime.MIN);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public CFGResponse toResponse(CFG cfg) {
        try {
            return new CFGResponse(cfg.getId(), objectMapper.readTree(cfg.getContent()));

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

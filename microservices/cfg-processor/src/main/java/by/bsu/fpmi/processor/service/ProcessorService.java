package by.bsu.fpmi.processor.service;

import by.bsu.fpmi.processor.dto.CFGRequest;
import by.bsu.fpmi.processor.dto.CFGResponse;
import by.bsu.fpmi.processor.dto.First1Response;
import by.bsu.fpmi.processor.dto.FollowResponse;
import by.bsu.fpmi.processor.exception.MalformedGrammarException;
import by.bsu.fpmi.processor.mapper.ToDtoCfgMapper;
import by.bsu.fpmi.processor.mapper.ToEntityCfgMapper;
import by.bsu.fpmi.processor.model.CFG;
import by.bsu.fpmi.processor.model.Symbol;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessorService {

    private final ToDtoCfgMapper toDtoCfgMapper;
    private final ToEntityCfgMapper toEntityCfgMapper;

    private final NormalizationService normalizationService;
    private final First1Service first1Service;
    private final FollowService followService;

    public First1Response normalizeAndCalculateFirst1(CFGRequest request) {

        CFG cfg = toEntityCfgMapper.toEntity(request);

        normalizationService.removeUselessNonTerminals(cfg);

        if (cfg.getStartSymbol() == null) {
            throw new MalformedGrammarException("некорректно заданная грамматика, стартовый нетерминал - бесполезный символ " + "(непорождающий)");
        }

        log.info(cfg.toString());

        Map<Symbol, Set<Symbol>> first1Map = first1Service.first1(cfg);

        return toDtoCfgMapper.toFirst1Response(cfg, first1Map);
    }

    public FollowResponse normalizeAndCalculateFollow(CFGRequest request) {

        CFG cfg = toEntityCfgMapper.toEntity(request);

        normalizationService.removeUselessNonTerminals(cfg);

        if (cfg.getStartSymbol() == null) {
            throw new MalformedGrammarException("некорректно заданная грамматика, стартовый нетерминал - бесполезный символ " + "(непорождающий)");
        }

        log.info(cfg.toString());

        Map<Symbol, Set<Symbol>> first1Map = first1Service.first1(cfg);
        Map<Symbol, Set<Symbol>> followMap = followService.follow(cfg, first1Map);

        return toDtoCfgMapper.toFollowResponse(cfg, first1Map, followMap);
    }

    public CFGResponse retainGeneratingNonTerminals(CFGRequest request) {

        CFG cfg = toEntityCfgMapper.toEntity(request);

        normalizationService.retainGeneratingNonTerminals(cfg);

        return toDtoCfgMapper.toCFGResponse(cfg);
    }

    public CFGResponse retainReachableNonTerminals(CFGRequest request) {

        CFG cfg = toEntityCfgMapper.toEntity(request);

        normalizationService.retainReachableNonTerminals(cfg);

        return toDtoCfgMapper.toCFGResponse(cfg);
    }
}

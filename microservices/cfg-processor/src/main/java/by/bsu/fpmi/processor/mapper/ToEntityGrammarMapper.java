package by.bsu.fpmi.processor.mapper;

import by.bsu.fpmi.processor.dto.GrammarRequest;
import by.bsu.fpmi.processor.model.Grammar;

public interface ToEntityGrammarMapper {

    Grammar toEntity(GrammarRequest request);
}

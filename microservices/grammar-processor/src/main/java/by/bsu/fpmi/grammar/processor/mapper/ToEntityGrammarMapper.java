package by.bsu.fpmi.grammar.processor.mapper;

import by.bsu.fpmi.grammar.processor.dto.GrammarRequest;
import by.bsu.fpmi.grammar.processor.model.Grammar;

public interface ToEntityGrammarMapper {

    Grammar toEntity(GrammarRequest request);
}

package by.bsu.fpmi.grammar.examples.mapper;

import by.bsu.fpmi.grammar.examples.dto.GrammarRequest;
import by.bsu.fpmi.grammar.examples.dto.GrammarResponse;
import by.bsu.fpmi.grammar.examples.entity.Grammar;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface GrammarMapper {

    Grammar toEntity(GrammarRequest request);

    GrammarResponse toResponse(Grammar grammar);
}

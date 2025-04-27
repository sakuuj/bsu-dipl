package by.bsu.fpmi.processor.mapper;

import by.bsu.fpmi.processor.dto.GrammarResponse;
import by.bsu.fpmi.processor.dto.First1Response;
import by.bsu.fpmi.processor.dto.FollowResponse;
import by.bsu.fpmi.processor.model.Grammar;
import by.bsu.fpmi.processor.model.Symbol;
import by.bsu.fpmi.processor.model.Word;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ToDtoGrammarMapper {

    @Mapping(target = "grammarResponse", source = "grammar")
    @Mapping(target = "first1Map", source = "first1Map")
    First1Response toFirst1Response(Grammar grammar, Map<Symbol, Set<Symbol>> first1Map);

    @Mapping(target = "grammarResponse", source = "grammar")
    @Mapping(target = "first1Map", source = "first1Map")
    @Mapping(target = "followMap", source = "followMap")
    FollowResponse toFollowResponse(Grammar grammar, Map<Symbol, Set<Symbol>> first1Map, Map<Symbol, Set<Symbol>> followMap);

    GrammarResponse toGrammarResponse(Grammar grammar);

    default String mapToString(Symbol s) {
        return s.toString();
    }

    default List<String> mapToList(LinkedHashSet<Symbol> set) {

        return set.stream()
                .map(Object::toString)
                .toList();
    }

    default Map<String, Set<String>> mapSymbolMapToDto(Map<Symbol, Set<Symbol>> symbolMap) {

        return symbolMap.entrySet().stream()
                .flatMap(e -> e.getValue().stream()
                        .map(
                                elem -> Map.entry(e.getKey().toString(), elem.toString())
                        )
                )
                .collect(
                        Collectors.groupingBy(
                                Entry::getKey,
                                Collectors.mapping(Entry::getValue, Collectors.toCollection(LinkedHashSet::new))
                        )
                );
    }

    default List<String> mapDefiningEquations(Map<Symbol, Set<Word>> defEqs) {

        List<String> result = new ArrayList<>();
        defEqs.forEach((k, v) -> {

            String options = v.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining("|"));

            result.add("%s = %s".formatted(k, options));
        });

        return result;
    }
}

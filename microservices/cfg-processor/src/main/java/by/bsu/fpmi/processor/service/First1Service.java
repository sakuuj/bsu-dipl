package by.bsu.fpmi.processor.service;

import by.bsu.fpmi.processor.model.CFG;
import by.bsu.fpmi.processor.model.Symbol;
import by.bsu.fpmi.processor.model.Word;

import java.util.Map;
import java.util.Set;

public interface First1Service {

    Map<Symbol, Set<Symbol>> first1(CFG cfg);

    Set<Symbol> first1OverWord(Word word, Map<Symbol, Set<Symbol>> first1, Set<Symbol> terminals);
}

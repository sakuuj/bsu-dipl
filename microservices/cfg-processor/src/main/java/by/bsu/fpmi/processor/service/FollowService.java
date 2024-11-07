package by.bsu.fpmi.processor.service;

import by.bsu.fpmi.processor.model.CFG;
import by.bsu.fpmi.processor.model.Symbol;

import java.util.Map;
import java.util.Set;

public interface FollowService {

    Map<Symbol, Set<Symbol>> follow(CFG cfg, Map<Symbol, Set<Symbol>> first1);
}

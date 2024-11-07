package by.bsu.fpmi.processor.service;

import by.bsu.fpmi.processor.model.CFG;

public interface NormalizationService {

    void retainGeneratingNonTerminals(CFG cfg);

    void retainReachableNonTerminals(CFG cfg);

    default void removeUselessNonTerminals(CFG cfg) {
        retainGeneratingNonTerminals(cfg);
        retainReachableNonTerminals(cfg);
    }
}

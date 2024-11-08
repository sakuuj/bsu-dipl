package by.bsu.fpmi.processor.mapper;

import by.bsu.fpmi.processor.dto.CFGRequest;
import by.bsu.fpmi.processor.model.CFG;

public interface ToEntityCfgMapper {

    CFG toEntity(CFGRequest request);
}

package by.bsu.fpmi.cfg.mapper;

import by.bsu.fpmi.cfg.dto.CFGRequest;
import by.bsu.fpmi.cfg.dto.CFGResponse;
import by.bsu.fpmi.cfg.entity.CFG;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CFGMapper {

    @Mapping(target = "id", ignore = true)
    CFG toEntity(CFGRequest request);

    CFGResponse toResponse(CFG cfg);

}

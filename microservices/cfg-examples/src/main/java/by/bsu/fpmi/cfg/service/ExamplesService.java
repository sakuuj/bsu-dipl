package by.bsu.fpmi.cfg.service;

import by.bsu.fpmi.cfg.dto.CFGRequest;
import by.bsu.fpmi.cfg.dto.CFGResponse;
import by.bsu.fpmi.cfg.dto.Page;
import by.bsu.fpmi.cfg.dto.PageRequest;
import by.bsu.fpmi.cfg.entity.CFG;

import java.util.Optional;

public interface ExamplesService {

    Optional<CFGResponse> getExampleById(long id);

    long insertExample(CFGRequest exampleRequest);

    Page<CFGResponse> getPage(PageRequest pageRequest);
}

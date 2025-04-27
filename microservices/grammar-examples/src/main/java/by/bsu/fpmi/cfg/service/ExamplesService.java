package by.bsu.fpmi.cfg.service;

import by.bsu.fpmi.cfg.dto.CFGRequest;
import by.bsu.fpmi.cfg.dto.CFGResponse;
import by.bsu.fpmi.cfg.dto.RequestedPage;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ExamplesService {

    Optional<CFGResponse> getExampleById(String id);

    String insertExample(CFGRequest exampleRequest);

    RequestedPage<CFGResponse> getAll(Pageable pageRequest);

    void deleteById(String id);
}

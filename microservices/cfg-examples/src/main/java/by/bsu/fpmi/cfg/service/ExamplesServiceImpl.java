package by.bsu.fpmi.cfg.service;

import by.bsu.fpmi.cfg.dto.CFGRequest;
import by.bsu.fpmi.cfg.dto.CFGResponse;
import by.bsu.fpmi.cfg.dto.RequestedPage;
import by.bsu.fpmi.cfg.entity.CFG;
import by.bsu.fpmi.cfg.mapper.CFGMapper;
import by.bsu.fpmi.cfg.repository.CFGRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExamplesServiceImpl implements ExamplesService {

    private final CFGMapper cfgMapper;
    private final CFGRepository cfgRepository;


    @Override
    public Optional<CFGResponse> getExampleById(String id) {

        return cfgRepository.findById(id).map(cfgMapper::toResponse);
    }

    @Override
    public String insertExample(CFGRequest exampleRequest) {

        CFG cfg = cfgMapper.toEntity(exampleRequest);

        return cfgRepository.insert(cfg).getId();
    }

    @Override
    public RequestedPage<CFGResponse> getAll(Pageable pageRequest) {

        Page<CFG> selectedPage = cfgRepository.findAll(pageRequest);

        var requestedPage = RequestedPage.fromPage(selectedPage);

        return requestedPage.map(cfgMapper::toResponse);
    }

    @Override
    public void deleteById(String id) {

        cfgRepository.deleteById(id);
    }
}

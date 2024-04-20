package by.bsu.fpmi.cfg.service;

import by.bsu.fpmi.cfg.dto.CFGRequest;
import by.bsu.fpmi.cfg.dto.CFGResponse;
import by.bsu.fpmi.cfg.dto.Page;
import by.bsu.fpmi.cfg.dto.PageRequest;
import by.bsu.fpmi.cfg.entity.CFG;
import by.bsu.fpmi.cfg.mapper.CFGMapper;
import by.bsu.fpmi.cfg.repository.CFGRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExamplesServiceImpl implements ExamplesService {

    private final CFGMapper cfgMapper;
    private final CFGRepository cfgRepository;


    @Override
    public Optional<CFGResponse> getExampleById(long id) {

        return cfgRepository.selectById(id)
                .map(cfgMapper::toResponse);
    }

    @Override
    public long insertExample(CFGRequest exampleRequest) {

        CFG cfg = cfgMapper.fromRequest(exampleRequest);
        return cfgRepository.insert(cfg);
    }

    @Override
    public Page<CFGResponse> getPage(PageRequest pageRequest) {

        Page<CFG> selected = cfgRepository.selectPage(pageRequest);
        return selected.map(cfgMapper::toResponse);
    }
}

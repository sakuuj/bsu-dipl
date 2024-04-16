package by.bsu.fpmi.cfg.repository;

import by.bsu.fpmi.cfg.dto.Page;
import by.bsu.fpmi.cfg.dto.PageRequest;
import by.bsu.fpmi.cfg.entity.CFG;

import java.util.Optional;

public interface CFGRepository {

    long insert(CFG cfg);

    Optional<CFG> selectById(long id);

    Page<CFG> selectPage(PageRequest pageRequest);
}

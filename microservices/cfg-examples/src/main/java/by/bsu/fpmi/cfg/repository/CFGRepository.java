package by.bsu.fpmi.cfg.repository;

import by.bsu.fpmi.cfg.entity.CFG;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface CFGRepository extends Repository<CFG, String> {

    CFG insert(CFG cfg);

    Optional<CFG> findById(String id);

    void deleteById(String id);

    Page<CFG> findAll(Pageable pageable);
}

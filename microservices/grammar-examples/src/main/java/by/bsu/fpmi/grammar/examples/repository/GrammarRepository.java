package by.bsu.fpmi.grammar.examples.repository;

import by.bsu.fpmi.grammar.examples.entity.Grammar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface GrammarRepository extends Repository<Grammar, String> {

    Grammar insert(Grammar grammar);

    Optional<Grammar> findById(String id);

    void deleteById(String id);

    Page<Grammar> findAll(Pageable pageable);
}

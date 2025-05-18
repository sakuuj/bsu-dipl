package by.bsu.fpmi.grammar.examples;

import by.bsu.fpmi.grammar.examples.entity.Grammar;
import by.bsu.fpmi.grammar.examples.repository.GrammarRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@SpringBootApplication
public class ExamplesApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExamplesApplication.class);
    }

    @Bean
    public CommandLineRunner commandLineRunner(GrammarRepository grammarRepository) {

        return (args) -> {
            Grammar grammar = new Grammar(
                    "fpm-1-1",
                    List.of("S"),
                    List.of("s"),
                    List.of("S=s|_"),
                    "S"
            );
            grammarRepository.insert(grammar);
            Page<Grammar> all = grammarRepository.findAll(Pageable.ofSize(10));
            System.out.println(all.getContent());
        };
    }
}

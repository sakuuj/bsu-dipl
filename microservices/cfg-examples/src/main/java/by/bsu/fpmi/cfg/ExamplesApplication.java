package by.bsu.fpmi.cfg;

import by.bsu.fpmi.cfg.entity.CFG;
import by.bsu.fpmi.cfg.repository.CFGRepository;
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
    public CommandLineRunner commandLineRunner(CFGRepository cfgRepository) {

        return (args) -> {
            CFG cfg = new CFG(
                    null,
                    List.of("S"),
                    List.of("s"),
                    List.of("S=s|_"),
                    "S"
            );
            cfgRepository.insert(cfg);
            Page<CFG> all = cfgRepository.findAll(Pageable.ofSize(10));
            System.out.println(all.getContent());
        };
    }
}

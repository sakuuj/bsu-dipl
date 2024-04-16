package by.bsu.fpmi.cfg.integration;

import by.bsu.fpmi.cfg.dto.Page;
import by.bsu.fpmi.cfg.dto.PageRequest;
import by.bsu.fpmi.cfg.entity.CFG;
import by.bsu.fpmi.cfg.repository.CFGRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
public class CFGRepositoryTests extends CommonPostgresContainerInitializer {

    @Autowired
    private CFGRepository cfgRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void shouldInsertCFG() throws JsonProcessingException {

        // given
        CFG cfgToInsert = CFGTestBuilder.newCFG().build();

        LocalDateTime timeBeforeInsert = LocalDateTime.now();

        // when
        long idOfInserted = cfgRepository.insert(cfgToInsert);
        CFG cfgSelected = cfgRepository.selectById(idOfInserted);

        JsonNode selectedContent = objectMapper.readTree(cfgSelected.getContent());
        JsonNode insertedContent = objectMapper.readTree(cfgToInsert.getContent());

        // then
        assertThat(selectedContent).isEqualTo(insertedContent);
        assertThat(cfgSelected.getTimeCreated()).isAfter(timeBeforeInsert);
    }

    @Test
    public void shouldSelectCorrectPage() {

        // given
        CFG cfgToInsert1 = CFGTestBuilder.newCFG().build();
        CFG cfgToInsert2 = CFGTestBuilder.newCFG()
                .withContent("""
                        {
                            "startSymbol" : "R",
                            "nonTerminals": [
                                "E", "T", "R", "S", "L"
                            ],
                            "terminals": [
                                "+", "-", "a", "i", "(", ")", "#", "_"
                            ],
                            "definingEquations": [
                                "L = LSSLT | LTR | S+",
                                "S = S-",
                                "E = TR",
                                "R = +TR|-TR| _",
                                "T = a|i|(E)|-TR|+T#T"
                            ]
                        }
                        """)
                .build();
        CFG cfgToInsert3 = CFGTestBuilder.newCFG()
                .withContent("""
                        {
                            "startSymbol" : "R",
                            "nonTerminals": [
                                "E", "T", "R", "S", "L"
                            ],
                            "terminals": [
                                "+", "-", "a", "i", "(", ")", "#", "_"
                            ],
                            "definingEquations": [
                                "L = LTR | S+",
                                "S = S-",
                                "E = TR",
                                "R = +TR|-TR| _",
                                "T = a|i|(E)|-TR|+T#T"
                            ]
                        }
                        """)
                .build();
        CFG cfgToInsert4 = CFGTestBuilder.newCFG()
                .withContent("""
                        {
                            "startSymbol" : "R",
                            "nonTerminals": [
                                "E", "T", "R", "S", "L"
                            ],
                            "terminals": [
                                "+", "-", "a", "i", "(", ")", "#", "_"
                            ],
                            "definingEquations": [
                                "L = LSSLT | LTR | S+",
                                "S = S-",
                                "E = TR",
                                "R = +TR|-TR| _",
                                "T = a|i|-TR|+T#T"
                            ]
                        }
                        """)
                .build();

        long id1 = cfgRepository.insert(cfgToInsert1);
        long id2 = cfgRepository.insert(cfgToInsert2);
        long id3 = cfgRepository.insert(cfgToInsert3);
        long id4 = cfgRepository.insert(cfgToInsert4);

        CFG cfg1 = cfgRepository.selectById(id1);
        CFG cfg2 = cfgRepository.selectById(id2);
        CFG cfg3 = cfgRepository.selectById(id3);
        CFG cfg4 = cfgRepository.selectById(id4);

        // when, then
        Page<CFG> cfgPage1 = cfgRepository.selectPage(new PageRequest(1, 2));

        assertThat(cfgPage1.getContent()).containsExactly(cfg1, cfg2);
        assertThat(cfgPage1.getTotalCount()).isEqualTo(4L);
        assertThat(cfgPage1.getPerPageCount()).isEqualTo(2L);
        assertThat(cfgPage1.getPageNumber()).isEqualTo(1);
        assertThat(cfgPage1.isFirst()).isTrue();
        assertThat(cfgPage1.isLast()).isFalse();


        Page<CFG> cfgPage2 = cfgRepository.selectPage(new PageRequest(2, 2));

        assertThat(cfgPage2.getContent()).containsExactly(cfg3, cfg4);
        assertThat(cfgPage2.getTotalCount()).isEqualTo(4L);
        assertThat(cfgPage2.getPerPageCount()).isEqualTo(2L);
        assertThat(cfgPage2.getPageNumber()).isEqualTo(2);
        assertThat(cfgPage2.isFirst()).isFalse();
        assertThat(cfgPage2.isLast()).isTrue();


        Page<CFG> cfgPage3 = cfgRepository.selectPage(new PageRequest(3, 2));

        assertThat(cfgPage3.getContent()).isEmpty();
        assertThat(cfgPage3.getTotalCount()).isEqualTo(4L);
        assertThat(cfgPage3.getPerPageCount()).isEqualTo(2L);
        assertThat(cfgPage3.getPageNumber()).isEqualTo(3);
        assertThat(cfgPage3.isFirst()).isFalse();
        assertThat(cfgPage3.isLast()).isFalse();
    }
}

package by.bsu.fpmi.firstkservice;

import by.bsu.fpmi.firstkservice.model.Symbol;
import by.bsu.fpmi.firstkservice.model.Word;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ProcessingApplication {

    public static void main(String[] args) {
//        System.out.println(new Symbol("что-то").compareTo(new Symbol("a")));
        SpringApplication.run(ProcessingApplication.class, args);
        Word word = new Word();
        word.append(new Symbol("b"));
        word.append(new Symbol("f"));
        word.append(new Symbol("d"));
        word.append(new Symbol("l"));

        Word x = new Word();
        System.out.println(x);
        x.append(Symbol.EMPTY_SYMBOL);

        System.out.println(x);
        x.insertAt(0, word);

        System.out.println(x);
    }
}
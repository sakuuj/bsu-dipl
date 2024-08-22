package by.bsu.fpmi.processor;

import com.github.loki4j.client.http.HttpHeader;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpHead;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ProcessingApplication {

    //    @Bean
//    public UserDetailsService userDetailsService() {
//        return new InMemoryUserDetailsManager(User.withUsername("df").password("2342341234").build());
//    }
    public static void main(String[] args) {
//        System.out.println(new Symbol("что-то").compareTo(new Symbol("a")));
        SpringApplication.run(ProcessingApplication.class, args);

    }


}
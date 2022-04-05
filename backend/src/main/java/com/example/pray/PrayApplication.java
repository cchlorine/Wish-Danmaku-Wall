package cc.sends.pray;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class PrayApplication{
    public static void main(String[] args) {
        SpringApplication.run(PrayApplication.class, args);
    }
}
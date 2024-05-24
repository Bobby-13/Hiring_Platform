package com.divum.hiring_platform;

import com.divum.hiring_platform.entity.Rounds;
import com.divum.hiring_platform.repository.ContestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.parameters.P;

import java.util.List;


@SpringBootApplication
public class HiringPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(HiringPlatformApplication.class, args);
    }

}

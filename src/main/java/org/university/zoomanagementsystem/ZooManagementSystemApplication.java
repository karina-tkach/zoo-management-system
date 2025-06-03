package org.university.zoomanagementsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ZooManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZooManagementSystemApplication.class, args);
    }

}

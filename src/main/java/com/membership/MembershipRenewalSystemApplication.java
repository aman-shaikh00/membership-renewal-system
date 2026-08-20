package com.membership;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MembershipRenewalSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(
                MembershipRenewalSystemApplication.class,
                args
        );
    }
}
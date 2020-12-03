package com.bsoft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class InterfaceEureka2App {
        public static void main(String[] args) {

            SpringApplication.run(InterfaceEureka2App.class,args);

        }

}

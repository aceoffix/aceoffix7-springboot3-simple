package com.acesoft.aceoffix7springbootsimple;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Aceoffix7SpringbootSimpleApplication {
    @Value("${posyspath}")
    private String poSysPath;

    public static void main(String[] args) {
        SpringApplication.run(Aceoffix7SpringbootSimpleApplication.class, args);
    }

    @Bean
    public ServletRegistrationBean aceoffixRegistrationBean() {
        com.acesoftcorp.aceoffix.aceserver.Server aceerver = new com.acesoftcorp.aceoffix.aceserver.Server();
        //Set the directory where the license.lic file is stored after successful registration of Aceoffix.
        aceerver.setSysPath(poSysPath);
        ServletRegistrationBean srb = new ServletRegistrationBean(aceerver);
        srb.addUrlMappings("/server.ace");
        srb.addUrlMappings("/aceclient");
        srb.addUrlMappings("/aceoffix.js");
        return srb;//
    }
}

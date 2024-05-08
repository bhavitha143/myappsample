package com.bh.realtrack.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

/**
 *
 * @author Anand Kumar
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = ("com.bh.**"))
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class })
@EnableConfigurationProperties
public class ProjectDetailsApplication {

	public static void main(final String[] args) {
		System.setProperty("spring.devtools.restart.enabled", "true");
		SpringApplication.run(ProjectDetailsApplication.class, args);
	}

}

package org.university.zoomanagementsystem.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("org.university.zoomanagementsystem")
@PropertySource("classpath:/application.properties")
public class ApplicationConfiguration {
}


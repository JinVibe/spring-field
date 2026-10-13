package dev.jinvibe.springfield;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SpringFieldApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringFieldApplication.class, args);
	}

}

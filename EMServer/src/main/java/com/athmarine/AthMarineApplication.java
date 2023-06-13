package com.athmarine;

import com.athmarine.resources.AppConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@PropertySources(value = { @PropertySource(AppConstant.Config.APPLICATION_PROPERTY_SOURCE_PATH) })
@SpringBootApplication
@EnableScheduling
public class AthMarineApplication {

	public static void main(String[] args) {


		SpringApplication.run(AthMarineApplication.class, args);

	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}

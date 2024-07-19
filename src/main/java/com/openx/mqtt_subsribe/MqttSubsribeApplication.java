package com.openx.mqtt_subsribe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication(scanBasePackages = "com.openx.mqtt_subsribe")
@EnableWebMvc
public class MqttSubsribeApplication {

	public static void main(String[] args) {
		SpringApplication.run(MqttSubsribeApplication.class, args);
	}

}

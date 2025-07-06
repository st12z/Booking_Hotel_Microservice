package com.thuc.messages;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class MessagesApplication {

	public static void main(String[] args) {

		SpringApplication app = new SpringApplication(MessagesApplication.class);

		app.addInitializers(applicationContext -> {
			try {
				Dotenv dotenv = Dotenv.configure()
						.directory(System.getProperty("user.dir")+"/messages")
						.load();

				Map<String, Object> dotenvMap = new HashMap<>();
				dotenv.entries().forEach(entry -> dotenvMap.put(entry.getKey(), entry.getValue()));

				ConfigurableEnvironment env = applicationContext.getEnvironment();
				env.getPropertySources().addFirst(new MapPropertySource("dotenv", dotenvMap));

			} catch (Exception e) {
				System.err.println("Không load được file .env: " + e.getMessage());
				throw e;
			}
		});
		app.run(args);
	}

}

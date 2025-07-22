package com.thuc.rooms;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableFeignClients
@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")
public class RoomsApplication {

	public static void main(String[] args) throws Exception {

		SpringApplication app = new SpringApplication(RoomsApplication.class);

		app.addInitializers(applicationContext -> {
			try {
//				Dotenv dotenv = Dotenv.configure()
//						.directory(System.getProperty("user.dir")+"/rooms")
//						.load();
				Dotenv dotenv = Dotenv.configure()
						.directory("/app")
						.filename(".env")
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

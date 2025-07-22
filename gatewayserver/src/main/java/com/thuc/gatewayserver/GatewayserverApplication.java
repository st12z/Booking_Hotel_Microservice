package com.thuc.gatewayserver;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication

public class GatewayserverApplication {

	public static void main(String[] args) {

		SpringApplication app = new SpringApplication(GatewayserverApplication.class);

		app.addInitializers(applicationContext -> {
			try {
				Dotenv dotenv = Dotenv.configure()
						.directory(System.getProperty("user.dir")+"/gatewayserver")
						.load();
//				Dotenv dotenv = Dotenv.configure()
//						.directory("/app")
//						.filename(".env")
//						.load();
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
	@Bean
	public RouteLocator BooKingHotelRoutesConfig(RouteLocatorBuilder routeLocatorBuilder) {
		return routeLocatorBuilder.routes()
				.route(p -> p
						.path("/bookinghotel/rooms/**")  // Định nghĩa route khi URL bắt đầu bằng "/bookinghotel/rooms/"
						.filters(f -> f
								.rewritePath("/bookinghotel/rooms/(?<segment>.*)", "/${segment}") // Chuyển hướng URL
						)
						.uri("lb://ROOMS")
				)
				.route(p->p
						.path("/bookinghotel/users/**")
						.filters(f->f
								.rewritePath("/bookinghotel/users/(?<segment>.*)","/${segment}")
						)
						.uri("lb://USERS")
				)
				.route(p -> p
						.path("/bookinghotel/bookings/**")  // Định nghĩa route khi URL bắt đầu bằng "/bookinghotel/booking/"
						.filters(f -> f
								.rewritePath("/bookinghotel/bookings/(?<segment>.*)", "/${segment}") // Chuyển hướng URL
						)
						.uri("lb://BOOKINGS")
				)
				.route(p-> p
						.path("/bookinghotel/payments/**")
						.filters(f-> f
								.rewritePath("/bookinghotel/payments/(?<segment>.*)","/${segment}")
						)
						.uri("lb://PAYMENTS")
				)
				.build();

	}
}

package com.thuc.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication

public class GatewayserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
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
				.build();

	}
}

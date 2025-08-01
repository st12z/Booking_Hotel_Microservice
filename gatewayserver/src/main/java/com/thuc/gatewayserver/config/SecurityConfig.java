package com.thuc.gatewayserver.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

import java.util.List;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    @Value("${URL_FRONTEND}")
    private String urlFrontEnd;
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .authorizeExchange(exchange ->
                        exchange
                                .pathMatchers("/bookinghotel/rooms/api/room-chats/all/**",
                                        "/bookinghotel/rooms/api/room-chats/update/**",
                                        "/bookinghotel/rooms/api/chats/messages-page/**",
                                        "/bookinghotel/rooms/api/roles/all/**",
                                        "/bookinghotel/rooms/api/roles/create/**",
                                        "/bookinghotel/users/api/users/update-roles/**",
                                        "/bookinghotel/users/api/users/reset-password/**",
                                        "/bookinghotel/users/api/users/create-staff/**"
                                        ).hasAnyRole("ADMIN","MANAGER")
                                .pathMatchers("/bookinghotel/bookings/api/bills/amount-bills-today",
                                        "/bookinghotel/bookings/api/bills/amount-bills-month",
                                        "/bookinghotel/bookings/api/bills/amount-revenue-today",
                                        "/bookinghotel/bookings/api/bills/amount-revenue-month",
                                        "/bookinghotel/bookings/api/bills/amount-bills-property/**",
                                        "/bookinghotel/rooms/api/reviews/amount-reviews",
                                        "/bookinghotel/users/api/users/amount-visits-today",
                                        "/bookinghotel/users/api/users/amount-users",
                                        "/bookinghotel/users/api/users/amount-visits-month",
                                        "/bookinghotel/rooms/api/properties/amount-properties",
                                        "/bookinghotel/rooms/api/properties/search",
                                        "/bookinghotel/rooms/api/properties/create",
                                        "/bookinghotel/rooms/api/notifications/**",
                                        "/bookinghotel/rooms/api/export/**",
                                        "/bookinghotel/rooms/api/property-types/**",
                                        "/bookinghotel/rooms/api/roomtypes/create",
                                        "/bookinghotel/rooms/api/roomtypes/update",
                                        "/bookinghotel/rooms/api/rooms/create",
                                        "/bookinghotel/rooms/api/rooms/delete",
                                        "/bookinghotel/rooms/api/cities/update/**",
                                        "/bookinghotel/rooms/api/cities/create/**",
                                        "/bookinghotel/rooms/api/trip/update/**",
                                        "/bookinghotel/rooms/api/trip/create/**",
                                        "/bookinghotel/rooms/api/triptypes/create/**",
                                        "/bookinghotel/rooms/api/triptypes/update/**",
                                        "/bookinghotel/rooms/api/facilities/create/**",
                                        "/bookinghotel/rooms/api/facilities/update/**",
                                        "/bookinghotel/bookings/api/bills/search",
                                        "/bookinghotel/bookings/api/refund-bills/filter",
                                        "/bookinghotel/bookings/api/refund-bills/amount-refund-month",
                                        "/bookinghotel/bookings/api/prints/**",
                                        "/bookinghotel/payments/api/payments/amount-transaction-month/**",
                                        "/bookinghotel/payments/api/payments/revenue-transaction-month/**",
                                        "/bookinghotel/payments/api/payments/statistic-transaction-month/**",
                                        "/bookinghotel/payments/api/payments/list-transactions/**",
                                        "/bookinghotel/payments/api/payments/transaction-types/**",
                                        "/bookinghotel/payments/api/payments/search/**",
                                        "/bookinghotel/payments/api/export/**",
                                        "/bookinghotel/payments/api/suspicious-transaction/**"
                                ).hasAnyRole("ADMIN","MANAGER","STAFF")
                                .pathMatchers("/bookinghotel/bookings/api/bills/**",
                                        "/bookinghotel/bookings/api/bookingcars/**",
                                        "/bookinghotel/bookings/api/bookings/**",
                                        "/bookinghotel/bookings/api/vehicles/**",
                                        "/bookinghotel/rooms/api/chats/**",
                                        "/bookinghotel/rooms/api/reviews/**",
                                        "/bookinghotel/rooms/api/upload/**",
                                        "/bookinghotel/rooms/api/room-chats/rooms/**",
                                        "/bookinghotel/rooms/api/discounts/save/**",
                                        "/bookinghotel/rooms/api/discounts/my-discounts/**",
                                        "/bookinghotel/rooms/api/discounts/my-discounts-page/**",
                                        "/bookinghotel/rooms/api/discounts/discount-types/**",
                                        "/bookinghotel/rooms/api/discounts/filter/**",
                                        "/bookinghotel/rooms/api/discount-cars/my-discounts/**",
                                        "/bookinghotel/rooms/api/discount-cars/save-discount/**",
                                        "/bookinghotel/rooms/api/discount-cars/my-discounts-page/**",
                                        "/bookinghotel/rooms/api/discount-cars/search/**",
                                        "/bookinghotel/rooms/api/discount-cars/update/**",
                                        "/bookinghotel/rooms/api/discount-cars/create/**",
                                        "/bookinghotel/payments/api/payments/refund/**",
                                        "/bookinghotel/payments/api/payments/get-url/**",
                                        "/bookinghotel/payments/api/payments/check-booking/**",
                                        "/bookinghotel/payments/api/payments/check-otp/**",
                                        "/bookinghotel/rooms/api/roomtypes/**",
                                        "/ws/**"
                                )
                                .hasAnyRole("ADMIN","MANAGER","STAFF","USER")
                                .anyExchange().permitAll()
                )
                .oauth2ResourceServer(oAuth2ResourceServerSpec -> oAuth2ResourceServerSpec
                        .jwt(jwt->jwt.jwtAuthenticationConverter(grantedAuthoritiesExtractor()))
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                )
                .cors(corsSpec -> corsSpec.configurationSource(corsConfigurationSource()))
                .csrf(csrfSpec -> csrfSpec.disable())
                .build();
    }
    private Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
        JwtAuthenticationConverter jwtAuthenticationConverter =
                new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter
                (new KeyCloakRoleConverter());

        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(urlFrontEnd));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
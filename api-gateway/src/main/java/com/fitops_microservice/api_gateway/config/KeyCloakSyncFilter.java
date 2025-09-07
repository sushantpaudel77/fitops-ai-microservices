package com.fitops_microservice.api_gateway.config;

import com.fitops_microservice.api_gateway.user.RegisterRequest;
import com.fitops_microservice.api_gateway.user.UserService;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.text.ParseException;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class KeyCloakSyncFilter implements WebFilter {

    private final UserService userService;

    @Nonnull
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String userId = extractUserId(exchange);
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (shouldProcessUser(userId, token)) {
            return processUserSync(exchange, chain, userId, token);
        }

        return chain.filter(exchange);
    }

    private String extractUserId(ServerWebExchange exchange) {
        String userId = exchange.getRequest().getHeaders().getFirst("X-User-Id");

        // If no userId in header, try to get it from token
        if (userId == null || userId.isEmpty()) {
            String token = exchange.getRequest().getHeaders().getFirst("Authorization");
            RegisterRequest registerRequest = getUserDetails(token);
            if (registerRequest != null) {
                userId = registerRequest.getKeycloakId();
            }
        }

        return userId;
    }

    private boolean shouldProcessUser(String userId, String token) {
        return userId != null && !userId.isEmpty() &&
                token != null && !token.isEmpty();
    }

    private Mono<Void> processUserSync(ServerWebExchange exchange, WebFilterChain chain,
                                       String userId, String token) {
        RegisterRequest registerRequest = getUserDetails(token);

        return userService.validateUser(userId)
                .flatMap(exists -> handleUserValidation(exists, registerRequest))
                .then(continueFilterChain(exchange, chain, userId))
                .onErrorResume(throwable -> handleSyncError(exchange, chain, userId, throwable));
    }

    private Mono<Void> handleUserValidation(boolean exists, RegisterRequest registerRequest) {
        if (!exists && registerRequest != null) {
            log.info("User does not exist, registering new user with ID: {}",
                    registerRequest.getKeycloakId());
            return userService.registerUser(registerRequest).then();
        }

        if (exists) {
            log.info("User already exists, skipping sync");
        }

        return Mono.empty();
    }

    private Mono<Void> continueFilterChain(ServerWebExchange exchange, WebFilterChain chain,
                                           String userId) {
        return Mono.defer(() -> {
            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                    .header("X-User-Id", userId)
                    .build();

            ServerWebExchange mutatedExchange = exchange.mutate()
                    .request(mutatedRequest)
                    .build();

            return chain.filter(mutatedExchange);
        });
    }

    private Mono<Void> handleSyncError(ServerWebExchange exchange, WebFilterChain chain,
                                       String userId, Throwable throwable) {
        log.error("Error in KeyCloak sync filter for userId {}: {}",
                userId, throwable.getMessage(), throwable);
        // Continue with original exchange to not break the request flow
        return chain.filter(exchange);
    }

    private RegisterRequest getUserDetails(String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }

        try {
            String cleanToken = token.replace("Bearer ", "").trim();
            SignedJWT signedJWT = SignedJWT.parse(cleanToken);
            log.info("KeyCloak sync filter: JWT token parsed successfully");
            return getRegisterRequest(signedJWT);
        } catch (ParseException e) {
            log.error("Failed to parse JWT token: {}", e.getMessage());
            return null; // Return null instead of throwing to allow graceful degradation
        }
    }

    private static RegisterRequest getRegisterRequest(SignedJWT signedJWT) throws ParseException {
        JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setKeycloakId(claimsSet.getStringClaim("sub"));
        registerRequest.setEmail(claimsSet.getStringClaim("email"));
        registerRequest.setPassword("NoPassword");
        registerRequest.setFirstName(claimsSet.getStringClaim("given_name"));
        registerRequest.setLastName(claimsSet.getStringClaim("family_name"));
        return registerRequest;
    }
}
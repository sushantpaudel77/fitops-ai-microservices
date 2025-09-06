package fitops_microservices.ai_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class GeminiService {

    private final WebClient.Builder webClientBuilder;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public String getRecommendations(String details) {
        return webClientBuilder.build()
                .post()
                .uri(geminiApiUrl)
                .header("Content-Type", "application/json")
                .header("X-goog-api-key", geminiApiKey)
                .bodyValue(Map.of(
                        "contents", new Object[] {
                                Map.of("parts", new Object[] {
                                        Map.of("text", details)
                                })
                        }
                ))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}

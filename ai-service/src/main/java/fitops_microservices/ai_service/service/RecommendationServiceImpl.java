package fitops_microservices.ai_service.service;

import fitops_microservices.ai_service.model.Recommendation;
import fitops_microservices.ai_service.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationServiceImpl {

    private final RecommendationRepository recommendationRepository;

    public List<Recommendation> getUserRecommendation(String userId) {
        return recommendationRepository.findByUserId(userId);
    }

    public Recommendation getActivityRecommendation(String activityId) {
        return recommendationRepository.findByActivityId(activityId)
                .orElseThrow(() -> new RuntimeException("Activity Id Not found with this ID: " + activityId));
    }
}

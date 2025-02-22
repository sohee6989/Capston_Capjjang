// mediapipe 사용의 경우 파이썬의 결과를 가져와서 데이터 저장 및 반환만 담단
// 임의로 써둠 나중에 수정할 것

package capston.capston_spring.service;

import capston.capston_spring.dto.AccuracySessionResponse;
import capston.capston_spring.entity.AccuracySession;
import capston.capston_spring.entity.AppUser;
import capston.capston_spring.repository.AccuracySessionRepository;
import capston.capston_spring.repository.UserRepository;
import capston.capston_spring.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.core.ParameterizedTypeReference;

import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccuracySessionService {
    private final AccuracySessionRepository accuracySessionRepository;
    private final UserRepository userRepository;
    private final SongRepository songRepository;
    private final WebClient.Builder webClientBuilder;  // ✅ WebClient.Builder 사용하여 생성

    /**
     * Python 서버에 분석 요청 후 결과를 받아서 DB에 저장
     */
    public AccuracySession analyzeAndSaveSession(Long userId, Long songId, String videoPath) {
        // ✅ userId로 AppUser 조회
        AppUser appUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + userId));

        // ✅ songId로 Song 조회
        var song = songRepository.findById(songId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid song ID: " + songId));

        try {
            WebClient webClient = webClientBuilder.build(); // ✅ WebClient 인스턴스 생성

            // ✅ WebClient로 Python 서버 호출
            Map<String, Object> responseBody = webClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("http")
                            .host("localhost")
                            .port(5000)
                            .path("/analyze")
                            .queryParam("user_id", userId)
                            .queryParam("song_id", songId)
                            .queryParam("video_path", videoPath)
                            .build())
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::isError,  // ✅ 람다 표현식 최적화
                            clientResponse -> clientResponse.bodyToMono(String.class)
                                    .flatMap(errorMessage -> {
                                        throw new RuntimeException("Error calling Python Server: " + clientResponse.statusCode() + " - " + errorMessage);
                                    })
                    )
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})  // ✅ 안전한 타입 변환
                    .block();

            if (responseBody == null) {
                throw new RuntimeException("Failed to analyze video from Python server.");
            }

            // 응답에서 score와 feedback 추출
            Object scoreObject = responseBody.get("score");
            double score = scoreObject instanceof Number ? ((Number) scoreObject).doubleValue() : Double.parseDouble(scoreObject.toString());
            String feedback = (String) responseBody.get("feedback");

            // AccuracySession 객체 생성 및 저장
            AccuracySession session = new AccuracySession();
            session.setUser(appUser);
            session.setSong(song);
            session.setScore(score);
            session.setFeedback(feedback);

            return accuracySessionRepository.save(session);

        } catch (WebClientResponseException e) {
            throw new RuntimeException("Error calling Python Server: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred while processing the session", e);
        }

    }

    // 기존 AccuracySessionResponse를 반환하는 메서드
    public List<AccuracySessionResponse> getAccuracyHistory(Long userId) {
        List<AccuracySession> sessions = accuracySessionRepository.findByUserId(userId);

        return sessions.stream().map(session -> new AccuracySessionResponse(
                session.getSong().getTitle(),
                session.getSong().getCoverImagePath(),
                session.getSong().getArtist(),
                session.getScore(),
                session.getFeedback()
        )).collect(Collectors.toList());
    }
}

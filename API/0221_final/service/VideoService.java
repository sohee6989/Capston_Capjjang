package capston.capston_spring.service;

import capston.capston_spring.dto.MyVideoResponse;
import capston.capston_spring.dto.RecordedVideoDto;
import capston.capston_spring.entity.*;
import capston.capston_spring.exception.VideoNotFoundException;
import capston.capston_spring.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VideoService {
    private final RecordedVideoRepository recordedVideoRepository;
    private final UserRepository userRepository;
    private final PracticeSessionRepository practiceSessionRepository;
    private final ChallengeSessionRepository challengeSessionRepository;

    private static final String VIDEO_STORAGE_DIR = "D:\\capston-spring\\uploads\\";

    // 비디오 정보를 저장하고, RecordedVideo 엔티티로 변환 후 저장
    public RecordedVideo saveRecordedVideo(RecordedVideoDto dto) {
        RecordedVideo video = convertToEntity(dto);
        return recordedVideoRepository.save(video);
    }

    // DTO 데이터를 Entity로 변환하는 메서드
    private RecordedVideo convertToEntity(RecordedVideoDto dto) {
        AppUser user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + dto.getUserId()));

        PracticeSession practiceSession = null;
        if (dto.getPracticeSessionId() != null) {
            practiceSession = practiceSessionRepository.findById(dto.getPracticeSessionId())
                    .orElseThrow(() -> new RuntimeException("PracticeSession not found with ID: " + dto.getPracticeSessionId()));
        }

        ChallengeSession challengeSession = null;
        if (dto.getChallengeSessionId() != null) {
            challengeSession = challengeSessionRepository.findById(dto.getChallengeSessionId())
                    .orElseThrow(() -> new RuntimeException("ChallengeSession not found with ID: " + dto.getChallengeSessionId()));
        }

        // ✅ mode 자동 설정 (연습 모드면 PRACTICE, 챌린지 모드면 CHALLENGE)
        VideoMode mode;
        if (practiceSession != null) {
            mode = VideoMode.PRACTICE;
        } else if (challengeSession != null) {
            mode = VideoMode.CHALLENGE;
        } else {
            throw new RuntimeException("Invalid session data: Both practiceSessionId and challengeSessionId are null.");
        }

        return new RecordedVideo();
    }


    // 특정 비디오 ID로 영상 조회 후 DTO 변환하여 반환
    public MyVideoResponse getVideo(Long videoId) {
        return recordedVideoRepository.findById(videoId)
                .map(this::convertToResponse)
                .orElseThrow(() -> new VideoNotFoundException("Video not found"));
    }

    // 특정 사용자 ID와 모드(PRACTICE, CHALLENGE)로 영상을 조회하여 반환
    public List<MyVideoResponse> getVideosByMode(Long userId, VideoMode mode) {
        return recordedVideoRepository.findByUserIdAndMode(userId, mode)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // 특정 연습 모드 녹화 영상 조회
    public List<RecordedVideo> getRecordedVideosByPracticeSession(Long sessionId) {
        PracticeSession practiceSession = practiceSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Practice session not found with ID: " + sessionId));
        return recordedVideoRepository.findByPracticeSession(practiceSession);
    }

    // 특정 챌린지 모드 녹화 영상 조회
    public List<RecordedVideo> getRecordedVideosByChallengeSession(Long sessionId) {
        ChallengeSession challengeSession = challengeSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Challenge session not found with ID: " + sessionId));
        return recordedVideoRepository.findByChallengeSession(challengeSession);
    }

    // 특정 사용자의 모든 녹화 영상을 조회하여 반환
    public List<MyVideoResponse> getAllVideosByUser(Long userId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        List<RecordedVideo> videos = recordedVideoRepository.findByUser(user); // ✅ findByUser 사용
        return videos.stream().map(this::convertToResponse).collect(Collectors.toList()); // ✅ 변환 확실히 수행
    }


    // 기존 영상 수정 및 파일 재업로드 기능
    public ResponseEntity<String> editVideo(Long videoId, MultipartFile file) {
        return recordedVideoRepository.findById(videoId)
                .map(video -> {
                    Path absolutePath = Paths.get(VIDEO_STORAGE_DIR, video.getUser().getId().toString(), video.getMode().toString(), videoId.toString() + ".mp4");
                    return handleFileUpload(file, absolutePath, video);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Video not found"));
    }

    // 비디오 파일 저장 및 덮어쓰기 기능
    private ResponseEntity<String> handleFileUpload(MultipartFile file, Path path, RecordedVideo video) {
        try {
            File existingFile = new File(path.toString());
            if (existingFile.exists()) {
                existingFile.delete();
            }
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            video.setRecordedAt(LocalDateTime.now());
            recordedVideoRepository.save(video);
            return ResponseEntity.ok("Video overwritten successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to overwrite video");
        }
    }

    // RecordedVideo 엔티티를 DTO로 변환하는 메서드
    private MyVideoResponse convertToResponse(RecordedVideo video) {
        String title, artist, coverImagePath;
        if (video.getMode() == VideoMode.PRACTICE && video.getPracticeSession() != null) {
            title = video.getPracticeSession().getSong().getTitle();
            artist = video.getPracticeSession().getSong().getArtist();
            coverImagePath = video.getPracticeSession().getSong().getCoverImagePath();
        } else if (video.getMode() == VideoMode.CHALLENGE && video.getChallengeSession() != null) {
            title = video.getChallengeSession().getSong().getTitle();
            artist = video.getChallengeSession().getSong().getArtist();
            coverImagePath = video.getChallengeSession().getSong().getCoverImagePath();
        } else {
            throw new IllegalStateException("RecordedVideo has an invalid session state.");
        }
        return new MyVideoResponse(video.getId(), title, artist, video.getMode().name(), coverImagePath, video.getVideoPath());
    }
}

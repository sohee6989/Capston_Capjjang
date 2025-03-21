package capston.capston_spring.service;

import capston.capston_spring.dto.MyVideoResponse;
import capston.capston_spring.dto.RecordedVideoDto;
import capston.capston_spring.entity.*;
import capston.capston_spring.exception.UserNotFoundException;
import capston.capston_spring.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final RecordedVideoRepository recordedVideoRepository;
    private final UserRepository userRepository;
    private final PracticeSessionRepository practiceSessionRepository;
    private final ChallengeSessionRepository challengeSessionRepository;
    private final S3Client s3Client;

    private static final String BUCKET_NAME = "danzle-s3-bucket";
    private static final String VIDEO_STORAGE_DIR = "videos/";



    /** 사용자의 모든 영상 조회 **/
    public List<MyVideoResponse> getAllUserVideos(Long userId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        return recordedVideoRepository.findByUserId(userId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /** 특정 세션의 영상 조회 **/
    public List<MyVideoResponse> getVideosBySession(Long sessionId, VideoMode mode) {
        List<RecordedVideo> videos = (mode == VideoMode.PRACTICE)
                ? recordedVideoRepository.findByPracticeSessionId(sessionId)
                : recordedVideoRepository.findByChallengeSessionId(sessionId);
        return videos.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    /** 특정 영상 조회 **/
    public Optional<MyVideoResponse> getVideoById(Long videoId) {
        return recordedVideoRepository.findById(videoId).map(this::convertToResponse);
    }

    /** 특정 모드의 영상 조회 **/
    public List<MyVideoResponse> getVideosByMode(Long userId, VideoMode mode) {
        return recordedVideoRepository.findByUserIdAndMode(userId, mode)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /** 특정 연습 모드 녹화 영상 조회 **/
    public List<RecordedVideo> getRecordedVideosByPracticeSession(Long sessionId) {
        PracticeSession practiceSession = practiceSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Practice session not found with ID: " + sessionId));
        return recordedVideoRepository.findByPracticeSession(practiceSession);
    }

    /** 특정 챌린지 모드 녹화 영상 조회 **/
    public List<RecordedVideo> getRecordedVideosByChallengeSession(Long sessionId) {
        ChallengeSession challengeSession = challengeSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Challenge session not found with ID: " + sessionId));
        return recordedVideoRepository.findByChallengeSession(challengeSession);
    }


    /** 녹화된 영상 저장하고, RecordedVideo 엔티티로 변환 후 저장 (S3 업로드) **/
    public RecordedVideo saveRecordedVideo(RecordedVideoDto dto, MultipartFile file) {
        String fileName = VIDEO_STORAGE_DIR + dto.getUserId() + "/" + UUID.randomUUID().toString() + ".mp4";
        String videoUrl = uploadToS3(file, fileName);

        RecordedVideo video = convertToEntity(dto);
        video.setVideoPath(videoUrl);
        return recordedVideoRepository.save(video);
    }

    /** 기존 영상 수정 및 파일 재업로드 기능 (S3 파일 덮어쓰기) **/
    public ResponseEntity<String> editVideo(Long videoId, MultipartFile file) {
        return recordedVideoRepository.findById(videoId).map(video -> {
            String fileName = video.getVideoPath().replace("https://" + BUCKET_NAME + ".s3.ap-northeast-2.amazonaws.com/", "");
            String newVideoUrl = uploadToS3(file, fileName);
            video.setVideoPath(newVideoUrl);
            recordedVideoRepository.save(video);
            return ResponseEntity.ok("Video updated successfully");
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Video not found"));
    }

    /** S3 파일 업로드 (handleFileUpload를 대신하는 메소드 -> 비디오 파일 저장 및 덮어쓰기 기능) **/
    private String uploadToS3(MultipartFile file, String fileName) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
            return "https://" + BUCKET_NAME + ".s3.ap-northeast-2.amazonaws.com/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("비디오 업로드 중 오류 발생", e);
        }
    }

    /** RecordedVideo 엔티티를 DTO로 변환하는 메서드 **/
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

        return new MyVideoResponse(video.getId(), title, artist, video.getMode(), coverImagePath, video.getVideoPath());
    }

    /** DTO 데이터를 Entity로 변환하는 메서드 **/
    private RecordedVideo convertToEntity(RecordedVideoDto dto) {
        AppUser user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + dto.getUserId()));

        PracticeSession practiceSession = (dto.getPracticeSessionId() != null)
                ? practiceSessionRepository.findById(dto.getPracticeSessionId()).orElse(null)
                : null;

        ChallengeSession challengeSession = (dto.getChallengeSessionId() != null)
                ? challengeSessionRepository.findById(dto.getChallengeSessionId()).orElse(null)
                : null;

        RecordedVideo video = new RecordedVideo();
        video.setUser(user);
        video.setPracticeSession(practiceSession);
        video.setChallengeSession(challengeSession);
        video.setVideoPath(dto.getVideoPath());
        video.setRecordedAt(dto.getRecordedAt());
        video.setDuration(dto.getDuration());

        return video;
    }
}

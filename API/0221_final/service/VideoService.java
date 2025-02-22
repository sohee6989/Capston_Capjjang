package capston.capston_spring.service;

import capston.capston_spring.dto.MyVideoResponse;
import capston.capston_spring.entity.RecordedVideo;
import capston.capston_spring.entity.VideoMode;
import capston.capston_spring.exception.VideoNotFoundException;
import capston.capston_spring.repository.RecordedVideoRepository;
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

    private static final String VIDEO_STORAGE_DIR = "D:\\capston-spring\\uploads\\"; // 비디오 저장 디렉토리 경로

    /**
     * RecordedVideo 엔티티를 MyVideoResponse DTO로 변환하는 메서드
     * @param video 변환할 RecordedVideo 엔티티
     * @return 변환된 MyVideoResponse 객체
     */
    private MyVideoResponse convertToResponse(RecordedVideo video){
        String title, artist, coverImagePath;

        // 연습 모드 비디오인 경우 PracticeSession에서 정보 가져오기
        if (video.getMode() == VideoMode.PRACTICE && video.getPracticeSession() != null) {
            title = video.getPracticeSession().getSong().getTitle();
            artist = video.getPracticeSession().getSong().getArtist();
            coverImagePath = video.getPracticeSession().getSong().getCoverImagePath();
        }
        // 챌린지 모드 비디오인 경우 ChallengeSession에서 정보 가져오기
        else if (video.getMode() == VideoMode.CHALLENGE && video.getChallengeSession() != null) {
            title = video.getChallengeSession().getSong().getTitle();
            artist = video.getChallengeSession().getSong().getArtist();
            coverImagePath = video.getChallengeSession().getSong().getCoverImagePath();
        } 
        else {
            throw new IllegalStateException("RecordedVideo has an invalid session state."); // 예외 처리
        }

        return new MyVideoResponse(
                video.getId(),
                title,
                artist,
                video.getMode().name(),
                coverImagePath,
                video.getVideoPath()
        );
    }

    /**
     * 비디오 파일을 덮어쓰는 메서드
     * @param file 업로드할 새 비디오 파일
     * @param path 저장될 파일 경로
     * @param video 기존 RecordedVideo 엔티티
     * @return 파일 저장 결과에 대한 HTTP 응답
     */
    private ResponseEntity<String> handleFileUpload(MultipartFile file, Path path, RecordedVideo video){
        try {
            // 기존 파일 삭제 (덮어쓰기 전에)
            File existingFile = new File(path.toString());
            if (existingFile.exists()) {
                existingFile.delete();
            }

            // 새로운 파일 저장
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            // 파일 덮어쓰기가 정상적으로 되었으면 videoPath 갱신
            video.setRecordedAt(LocalDateTime.now()); // 녹화 시간 업데이트
            this.recordedVideoRepository.save(video);

            return ResponseEntity.ok("Video overwritten successfully"); // 성공 메시지 반환
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to overwrite video"); // 예외 발생 시 에러 메시지 반환
        }
    }

    /**
     * 특정 영상 조회 (비디오 ID 기준)
     * @param videoId 조회할 비디오 ID
     * @return MyVideoResponse DTO 반환
     */
    public MyVideoResponse getVideo(Long videoId){
        return this.recordedVideoRepository.findById(videoId)
                .map(this::convertToResponse)
                .orElseThrow(() -> new VideoNotFoundException("Video not found")); // 비디오가 없으면 예외 발생
    }

    /**
     * 특정 사용자 ID와 비디오 모드(PRACTICE, CHALLENGE)로 영상 목록 조회
     * @param userId 사용자 ID
     * @param mode 비디오 모드 (연습 또는 챌린지)
     * @return 조회된 MyVideoResponse 리스트 반환
     */
    public List<MyVideoResponse> getVideosByMode(Long userId, VideoMode mode){
        return this.recordedVideoRepository.findByUserIdAndMode(userId, mode)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 영상 수정 및 재업로드
     * @param videoId 수정할 비디오 ID
     * @param file 업로드할 새 비디오 파일
     * @return 파일 업로드 결과에 대한 HTTP 응답
     */
    public ResponseEntity<String> editVideo(Long videoId, MultipartFile file){
        return this.recordedVideoRepository.findById(videoId)
                .map(video -> {
                    // 사용자별 디렉토리 생성 (예: D:/capston-spring/uploads/65/CHALLENGE/14.mp4)
                    Path absolutePath = Paths.get(VIDEO_STORAGE_DIR, video.getUser().getId().toString(), video.getMode().toString(), videoId.toString() + ".mp4");

                    // 파일 업로드 실행
                    return handleFileUpload(file, absolutePath, video);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Video not found")); // 비디오가 없으면 에러 메시지 반환
    }
}

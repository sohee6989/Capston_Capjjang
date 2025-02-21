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

    private static final String VIDEO_STORAGE_DIR = "D:\\capston-spring\\uploads\\";

    /** MyResponse Dto 반환 **/
    private MyVideoResponse convertToResponse(RecordedVideo video){
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

        return new MyVideoResponse(
                video.getId(),
                title,
                artist,
                video.getMode().name(),
                coverImagePath,
                video.getVideoPath()
        );
    }

    /** 비디오 덮어쓰기 **/
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
            video.setRecordedAt(LocalDateTime.now());
            this.recordedVideoRepository.save(video);

            return ResponseEntity.ok("Video overwritten successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to overwrite video");
        }
    }


    /** 특정 영상 조회 **/
    public MyVideoResponse getVideo(Long videoId){
        return this.recordedVideoRepository.findById(videoId)
                .map(this::convertToResponse)
                .orElseThrow(() -> new VideoNotFoundException("Video not found"));
    }


    /** 영상 조회 통합 **/
    public List<MyVideoResponse> getVideosByMode(Long userId, VideoMode mode){
        return this.recordedVideoRepository.findByUserIdAndMode(userId, mode)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /** 영상 수정 및 재업로드 **/
    public ResponseEntity<String> editVideo(Long videoId, MultipartFile file){
        return this.recordedVideoRepository.findById(videoId)
                .map(video -> {
                    // 사용자별 디렉토리 생성 (예: D:/capston-spring/uploads/65/CHALLENGE/14.mp4)
                    Path absolutePath = Paths.get(VIDEO_STORAGE_DIR, video.getUser().getId().toString(), video.getMode().toString(), videoId.toString() + ".mp4");

                    // 파일 업로드 실행
                    return handleFileUpload(file, absolutePath, video);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Video not found"));
    }


}

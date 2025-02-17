package capston.capston_spring.service;

import capston.capston_spring.dto.MyVideoResponse;
import capston.capston_spring.entity.RecordedVideo;
import capston.capston_spring.exception.VideoNotFoundException;
import capston.capston_spring.repository.RecordedVideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    // Practice 모드 영상 조회
    public List<MyVideoResponse> getPracticeVideos(Long userId) {
        List<RecordedVideo> videos = this.recordedVideoRepository.findByUserIdAndMode(userId, "PRACTICE");

        return videos.stream().map(video -> new MyVideoResponse(
                video.getId(),
                video.getPracticeSession().getSong().getTitle(),
                video.getPracticeSession().getSong().getArtist(),
                video.getMode().toString(),
                video.getPracticeSession().getSong().getCoverImagePath(),
                video.getVideoPath()
        )).collect(Collectors.toList());
    }

    // Challenge 모드 영상 조회
    public List<MyVideoResponse> getChallengeVideos(Long userId) {
        List<RecordedVideo> videos = this.recordedVideoRepository.findByUserIdAndMode(userId, "CHALLENGE");

        return videos.stream().map(video -> new MyVideoResponse(
                video.getId(),
                video.getChallengeSession().getSong().getTitle(),
                video.getChallengeSession().getSong().getArtist(),
                video.getMode(),
                video.getChallengeSession().getSong().getCoverImagePath(),
                video.getVideoPath()
        )).collect(Collectors.toList());
    }

    // 영상 수정 및 재업로드
    public ResponseEntity<String> editVideo(Long videoId, MultipartFile file){
        // 기존 영상 조회
        RecordedVideo existingVideo = this.recordedVideoRepository.findById(videoId)
                .orElseThrow(() -> new VideoNotFoundException("Video not found"));

        // 기존 영상 경로 가져오기
        Path path = Paths.get(existingVideo.getVideoPath());

        try {
            // 기존 영상 덮어쓰기 (overwrite)
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            // 변경된 영상 정보 업데이트 (예: 업데이트 시간 기록)
            existingVideo.setRecordedAt(LocalDateTime.now());
            this.recordedVideoRepository.save(existingVideo);

            return ResponseEntity.ok("Video overwritten successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to overwrite video");
        }
    }
}


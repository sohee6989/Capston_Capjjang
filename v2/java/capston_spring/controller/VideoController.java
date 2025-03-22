package capston.capston_spring.controller;

import capston.capston_spring.dto.MyVideoResponse;
import capston.capston_spring.dto.RecordedVideoDto;
import capston.capston_spring.entity.RecordedVideo;
import capston.capston_spring.entity.VideoMode;
import capston.capston_spring.exception.VideoNotFoundException;
import capston.capston_spring.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/recorded-video")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;


    /** 사용자의 녹화 영상 조회 (모드별 조회 가능) **/
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MyVideoResponse>> getVideosByUserId(@PathVariable Long userId, @RequestParam(name="mode", required = false) VideoMode mode){
        return ResponseEntity.ok(mode == null ? videoService.getAllUserVideos(userId) : videoService.getVideosByMode(userId, mode));
    }

    /** 특정 비디오 조회 **/
    @GetMapping("/{videoId}")
    public ResponseEntity<MyVideoResponse> getMyVideo(@PathVariable("videoId") Long videoId) {
        return videoService.getVideoById(videoId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new VideoNotFoundException("해당 비디오를 찾을 수 없습니다."));
    }

    /** 특정 세션의 녹화 영상 조회 (연습 & 챌린지 통합) **/
    @GetMapping("/session/{sessionId}")
    public ResponseEntity<List<MyVideoResponse>> getVideosBySession(@PathVariable Long sessionId, @RequestParam VideoMode mode) {
        return ResponseEntity.ok(videoService.getVideosBySession(sessionId, mode));
    }

    /** 녹화된 영상 저장 (DTO 기반) **/
    @PostMapping("/saveVideo")
    public ResponseEntity<RecordedVideo> saveRecordedVideo(@RequestPart("file") MultipartFile file, @RequestPart("data") RecordedVideoDto recordedVideoDto) {
        return ResponseEntity.ok(videoService.saveRecordedVideo(recordedVideoDto, file));
    }

    /** 특정 비디오 수정 (재업로드) **/
    @PostMapping(value = "/{videoId}/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadVideo(
            @RequestParam("file") MultipartFile file,
            @PathVariable("videoId") Long videoId){
        if (file.isEmpty() || file.getSize() == 0) {
            return ResponseEntity.badRequest().body("파일이 비어 있습니다.");
        }
        return videoService.editVideo(videoId, file);
    }
}

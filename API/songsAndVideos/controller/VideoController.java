package com.example._4.controller;

import com.example._4.entity.RecordedVideo;
import com.example._4.service.VideoService;
import com.example._4.dto.PlaybackRange;
import com.example._4.dto.PlaybackSpeed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/{mode}/videos")
@RequiredArgsConstructor
public class VideoController {
    private final VideoService videoService;

    // 전체 비디오룰 조회
    @GetMapping
    public List<RecordedVideo> getAllVideos() {
        return videoService.getAllVideos();
    }

    // 비디오 조회(연습모드에서만)
    @GetMapping("/{video_id}")
    public ResponseEntity<?> getVideoById(@PathVariable String mode, @PathVariable Long video_id) {
        if (!mode.equals("practice")) {
            return ResponseEntity.badRequest().body("Video playback is only available in practice mode.");
        }
        Optional<RecordedVideo> video = videoService.getVideoById(video_id);
        return video.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 재생 구간 설정
    @PostMapping("/{video_id}/playback-range")
    public ResponseEntity<String> setPlaybackRange(
            @PathVariable Long video_id,
            @RequestBody PlaybackRange range) {

        videoService.setPlaybackRange(video_id, range);
        return ResponseEntity.ok("Playback range set: " + range.getStartTime() + "s to " +
                (range.getEndTime() > 0 ? range.getEndTime() + "s" : "end"));
    }

    // 재생 속도 설정 (버튼 클릭)
    @PostMapping("/{video_id}/playback-speed")
    public ResponseEntity<String> setPlaybackSpeed(
            @PathVariable Long video_id,
            @RequestBody PlaybackSpeed speed) {

        videoService.setPlaybackSpeed(video_id, speed);
        return ResponseEntity.ok("Playback speed set to: " + speed.getSpeed() + "x");
    }
}

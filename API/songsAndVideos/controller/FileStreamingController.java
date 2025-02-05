package com.example._4.controller;

import com.example._4.service.FileStreamingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stream")
@RequiredArgsConstructor
public class FileStreamingController {
    private final FileStreamingService fileStreamingService;

    // MP3 스트리밍 API
    @GetMapping(value = "/audio/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> streamAudio(@PathVariable Long id) {
        byte[] audioData = fileStreamingService.getAudioData(id);
        if (audioData == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(audioData);
    }

    // MP4 스트리밍 API
    @GetMapping(value = "/video/{id}", produces = "video/mp4")
    public ResponseEntity<byte[]> streamVideo(@PathVariable Long id) {
        byte[] videoData = fileStreamingService.getVideoData(id);
        if (videoData == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("video/mp4"))
                .body(videoData);
    }
}

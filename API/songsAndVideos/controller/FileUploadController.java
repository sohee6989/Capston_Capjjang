package com.example._4.controller;

import com.example._4.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class FileUploadController {
    private final FileUploadService fileUploadService;

    // 비디오 업로드
    @PostMapping("/video")
    public ResponseEntity<String> uploadVideo(@RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.ok(fileUploadService.uploadVideo(file));
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Video upload failed.");
        }
    }

    // 노래 업로드
    @PostMapping("/song")
    public ResponseEntity<String> uploadSong(@RequestParam("file") MultipartFile file,
                                             @RequestParam("title") String title,
                                             @RequestParam("artist") String artist) {
        try {
            return ResponseEntity.ok(fileUploadService.uploadSong(file, title, artist));
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Song upload failed.");
        }
    }
}

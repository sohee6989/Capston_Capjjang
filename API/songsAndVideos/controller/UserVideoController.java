package com.example._4.controller; 

import com.example._4.entity.UserVideo;
import com.example._4.service.UserVideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/user/videos")
@RequiredArgsConstructor
public class UserVideoController {
    private final UserVideoService userVideoService;

    // 사용자가 촬영한 영상 업로드 (연습,챌린지 모드)
    @PostMapping("/upload")
    public ResponseEntity<String> uploadUserVideo(@RequestParam("userId") Long userId,
                                                  @RequestParam("file") MultipartFile file,
                                                  @RequestParam("mode") String mode) {
        try {
            return ResponseEntity.ok(userVideoService.saveUserVideo(userId, file, mode));
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Video upload failed.");
        }
    }

    // 마이페이지에서 사용자 영상 목록 가져오기
    @GetMapping("/{userId}")
    public ResponseEntity<List<UserVideo>> getUserVideos(@PathVariable Long userId) {
        List<UserVideo> videos = userVideoService.getUserVideos(userId);
        return ResponseEntity.ok(videos);
    }
}

package com.example._4.service; 

import com.example._4.entity.UserVideo;
import com.example._4.repository.UserVideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserVideoService {
    private final UserVideoRepository userVideoRepository;

    // 사용자 영상 저장
    public String saveUserVideo(Long userId, MultipartFile file, String mode) throws IOException {
        UserVideo userVideo = new UserVideo();
        userVideo.setUserId(userId);
        userVideo.setVideoData(file.getBytes()); // MP4 데이터를 저장
        userVideo.setMode(mode);
        userVideo.setRecordedAt(LocalDateTime.now());

        userVideoRepository.save(userVideo);
        return "Video saved successfully.";
    }

    // 마이페이지에서 사용자 영상 목록 가져오기
    public List<UserVideo> getUserVideos(Long userId) {
        return userVideoRepository.findByUserId(userId);
    }
}

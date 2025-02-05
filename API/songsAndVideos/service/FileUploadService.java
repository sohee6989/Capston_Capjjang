package com.example._4.service;

import com.example._4.entity.RecordedVideo;
import com.example._4.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FileUploadService {
    private final VideoRepository videoRepository;

    public String uploadVideo(MultipartFile file) throws IOException {
        RecordedVideo video = new RecordedVideo();
        video.setRecordedAt(LocalDateTime.now());
        video.setVideoData(file.getBytes()); // MP4 파일을 바이너리로 저장

        videoRepository.save(video);
        return "Video uploaded successfully";
    }

    // 노래 업로드 기능
    public String uploadSong(MultipartFile file, String title, String artist) throws IOException {
        return "Song '" + title + "' by " + artist + " uploaded successfully";
    }
}

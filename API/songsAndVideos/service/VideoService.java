package com.example._4.service;

import com.example._4.entity.RecordedVideo;
import com.example._4.repository.VideoRepository;
import com.example._4.dto.PlaybackRange;
import com.example._4.dto.PlaybackSpeed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VideoService {
    private final VideoRepository videoRepository;

    // 전체 비디오를 조회
    public List<RecordedVideo> getAllVideos() {
        return videoRepository.findAll();
    }

    // 비디오 조회
    public Optional<RecordedVideo> getVideoById(Long id) {
        return videoRepository.findById(id);
    }

    // 재생 구간 설정 (컨트롤바? 연동)
    public void setPlaybackRange(Long videoId, PlaybackRange range) {
        Optional<RecordedVideo> videoOptional = videoRepository.findById(videoId);
        if (videoOptional.isPresent()) {
            RecordedVideo video = videoOptional.get();

            // 사용자가 startTime만 설정하면 endTime을 null로 유지
            video.setStartTime(range.getStartTime());
            video.setEndTime(range.getEndTime() > 0 ? range.getEndTime() : null);

            videoRepository.save(video);
        }
    }

    // 재생 속도 설정 (버튼 클릭)
    public void setPlaybackSpeed(Long videoId, PlaybackSpeed speed) {
        Optional<RecordedVideo> videoOptional = videoRepository.findById(videoId);
        if (videoOptional.isPresent()) {
            RecordedVideo video = videoOptional.get();
            video.setSpeed(speed.getSpeed());
            videoRepository.save(video);
        }
    }
}

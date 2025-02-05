package com.example._4.service;

import com.example._4.entity.Song;
import com.example._4.entity.RecordedVideo;
import com.example._4.repository.SongRepository;
import com.example._4.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileStreamingService {
    private final SongRepository songRepository;
    private final VideoRepository videoRepository;

    public byte[] getAudioData(Long songId) {
        Optional<Song> song = songRepository.findById(songId);
        return song.map(Song::getAudioData).orElse(null);
    }

    public byte[] getVideoData(Long videoId) {
        Optional<RecordedVideo> video = videoRepository.findById(videoId);
        return video.map(RecordedVideo::getVideoData).orElse(null);
    }
}

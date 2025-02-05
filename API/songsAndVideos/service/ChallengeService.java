package com.example._4.service; 

import com.example._4.entity.Song;
import com.example._4.entity.ChallengeBackground;
import com.example._4.repository.SongRepository;
import com.example._4.repository.ChallengeBackgroundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChallengeService {
    private final SongRepository songRepository;
    private final ChallengeBackgroundRepository challengeBackgroundRepository;

    // 챌린지 모드에서 사용할 노래 부분 가져오기
    public byte[] getChallengeAudio(Long songId) {
        Optional<Song> song = songRepository.findById(songId);
        if (song.isPresent() && song.get().getChallengeStart() != null) {
            return song.get().getAudioData(); // MP3 데이터 반환.
        }
        return null;
    }

    // 챌린지 모드에서 사용할 가상 배경 가져오기
    public String getChallengeBackground(Long backgroundId) {
        Optional<ChallengeBackground> background = challengeBackgroundRepository.findById(backgroundId);
        return background.map(ChallengeBackground::getBackgroundUrl).orElse(null);
    }
}

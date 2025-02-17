package capston.capston_spring.service;

import lombok.RequiredArgsConstructor;
import capston.capston_spring.dto.PracticeSessionDto;
import capston.capston_spring.entity.AppUser;
import capston.capston_spring.entity.PracticeSession;
import capston.capston_spring.entity.Song;
import capston.capston_spring.repository.PracticeSessionRepository;
import capston.capston_spring.repository.UserRepository;
import capston.capston_spring.repository.SongRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PracticeSessionService {

    private final PracticeSessionRepository practiceSessionRepository;
    private final UserRepository userRepository;
    private final SongRepository songRepository;

    public PracticeSession savePracticeSession(PracticeSessionDto dto) {
        PracticeSession session = convertToEntity(dto);
        return practiceSessionRepository.save(session);
    }

    private PracticeSession convertToEntity(PracticeSessionDto dto) {
        AppUser user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + dto.getUserId()));

        Song song = songRepository.findById(dto.getSongId())
                .orElseThrow(() -> new RuntimeException("Song not found with ID: " + dto.getSongId()));

        PracticeSession session = new PracticeSession();
        session.setUser(user);
        session.setSong(song);
        session.setStartTime(dto.getStartTime());
        session.setEndTime(dto.getEndTime());
        return session;
    }

    public List<PracticeSession> getPracticeSessionsByUser(Long userId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        return practiceSessionRepository.findByUser(user);
    }

    public List<PracticeSession> getPracticeSessionsBySong(Long songId) {
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found with ID: " + songId));
        return practiceSessionRepository.findBySong(song);
    }

    // 연습 모드에서 선택 가능한 곡 리스트 반환
    public List<Song> getSongsByMode(String mode) {
        return songRepository.findByMode(mode);
    }
}

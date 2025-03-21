package capston.capston_spring.service;

import capston.capston_spring.dto.PracticeSessionDto;
import capston.capston_spring.entity.AppUser;
import capston.capston_spring.entity.PracticeSession;
import capston.capston_spring.entity.Song;
import capston.capston_spring.exception.SongNotFoundException;
import capston.capston_spring.exception.UserNotFoundException;
import capston.capston_spring.repository.PracticeSessionRepository;
import capston.capston_spring.repository.SongRepository;
import capston.capston_spring.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PracticeSessionService {

    private final PracticeSessionRepository practiceSessionRepository;
    private final UserRepository userRepository;
    private final SongRepository songRepository;

    @Value("${flask.api.segment}") // Flask의 /segment-video 엔드포인트 추가
    private String flaskSegmentUrl;

    /** 연습 세션 저장 **/
    public PracticeSession savePracticeSession(PracticeSessionDto dto) {
        PracticeSession session = convertToEntity(dto);
        return practiceSessionRepository.save(session);
    }

    /** (수정 : user, song 코드 간결화)  **/
    private PracticeSession convertToEntity(PracticeSessionDto dto) {
        AppUser user = getUserById(dto.getUserId());
        Song song = getSongById(dto.getSongId());

        PracticeSession session = new PracticeSession();
        session.setUser(user);
        session.setSong(song);

        // LocalDateTime -> 초 단위 변환 후 저장
        session.setStartTimeFromSeconds((int) dto.getStartTime().toEpochSecond(ZoneOffset.UTC));
        session.setEndTimeFromSeconds((int) dto.getEndTime().toEpochSecond(ZoneOffset.UTC));

        return session;
    }

    /** 사용자 조회 (수정 : 메소드 추가) **/
    private AppUser getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
    }

    /** ID로 노래 조회 (수정 : 메소드 추가) */
    private Song getSongById(Long songId) {
        return songRepository.findById(songId)
                .orElseThrow(() -> new SongNotFoundException("Song not found: " + songId));
    }

    /** 세션 ID로 연습 세션 조회 (수정 : 메소드 추가) **/
    public PracticeSession getById(Long sessionId){
        return practiceSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found: " + sessionId));
    }

    /** 사용자 ID로 연습 세션 조회 (수정 : 메서드 이름 간결하게 수정 및 사용자 검증 코드 제거) **/
    public List<PracticeSession> getByUserId(Long userId) {
        return practiceSessionRepository.findByUserId(userId);
    }

    /** 특정 곡과 사용자 ID로 연습 세션 조회 (수정 : 메서드 추가)**/
    public List<PracticeSession> getBySongAndUser(Long songId, Long userId) {
        return practiceSessionRepository.findBySongIdAndUserId(songId, userId);
    }

    /** 1절 끝나는 시간을 초 단위로 반환 **/
    public int getVerseEndTime(Long songId) {
        Song song = getSongById(songId);
        return song.getVerseEndTime();
    }

    /** 하이라이트 구간을 초 단위로 반환 **/
    public int[] getHighlightRange(Long songId) {
        Song song = getSongById(songId);
        return new int[]{song.getHighlightStartTime(), song.getHighlightEndTime()};
    }

    /** 연습 모드에서 안무 실루엣 생성 (`/segment-video` 호출) **/
    public String segmentVideo(MultipartFile videoFile) throws IOException {
        File tempFile = convertMultiPartToFile(videoFile); // MultipartFile → File 변환

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<FileSystemResource> request = new HttpEntity<>(new FileSystemResource(tempFile), headers);
        ResponseEntity<Map> response = restTemplate.exchange(flaskSegmentUrl, HttpMethod.POST, request, Map.class);

        tempFile.delete(); // 사용 후 임시 파일 삭제

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Failed to generate silhouette video.");
        }

        return (String) response.getBody().get("output_video");
    }

    /** `MultipartFile`을 `File`로 변환하는 메서드 **/
    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }
        return convFile;
    }
}


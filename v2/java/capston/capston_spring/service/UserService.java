package capston.capston_spring.service;

import capston.capston_spring.dto.CustomUserDetails;
import capston.capston_spring.dto.UserResponseDto;
import capston.capston_spring.entity.AppUser;
import capston.capston_spring.entity.Role;
import capston.capston_spring.entity.VideoMode;
import capston.capston_spring.exception.UserNotFoundException;
import capston.capston_spring.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final VideoService videoService;
    private final AccuracySessionService accuracySessionService;
    private final RecordedVideoRepository recordedVideoRepository;
    private final PracticeSessionRepository practiceSessionRepository;
    private final ChallengeSessionRepository challengeSessionRepository;
    private final AccuracySessionRepository accuracySessionRepository;
    private final PasswordEncoder passwordEncoder;

    /** 사용자 프로필 이미지 경로
     * 일단 로컬 디렉토리에 저장하고 배포 시 서버에 저장해야됨
     **/
    private final S3Client s3Client;
    private static final String BUCKET_NAME = "danzle-s3-bucket";
    private static final String PROFILE_IMAGE_DIR = "profile_images/";
    private static final String DEFAULT_PROFILE_IMAGE = "https://" + BUCKET_NAME + ".s3.ap-northeast-2.amazonaws.com/" + PROFILE_IMAGE_DIR + "defaultUserIcon.jpg";

    /** 회원 생성 **/
    public AppUser create(String name, String email, String password){
        // 새로운 사용자 생성
        AppUser user = new AppUser();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setProfileImageUrl(DEFAULT_PROFILE_IMAGE);
        user.setRole(email.equals("admin@admin.com") ? Role.ADMIN : Role.USER);
        return userRepository.save(user);
    }


    /** 회원 조회 (ID 또는 Email 기반) **/
    public AppUser getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
    }

    public AppUser getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
    }

    public Long getUserId(Authentication authentication){
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return getUser(userDetails.getEmail()).getId();
    }


    /** 사용자 정보 (프로필, 비디오, 점수 기록 포함) **/
    public UserResponseDto getUserDetails(Long userId){
        AppUser user = getUser(userId);
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getProfileImageUrl(),
                videoService.getVideosByMode(userId, VideoMode.PRACTICE),
                videoService.getVideosByMode(userId, VideoMode.CHALLENGE),
                accuracySessionService.getByUserId(userId)
        );
    }

    /** 사용자 이름 변경 **/
    @Transactional
    public void updateName(Long userId, String newName){
        AppUser user = getUser(userId);
        user.setName(newName);
    }

    /** 비밀번호 변경 **/
    @Transactional
    public void updatePassword(Long userId,  String currentPassword, String newPassword) {
        AppUser user = getUser(userId);
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
    }

    /** 프로필 이미지 변경 (경로 문제 해결) **/
    @Transactional
    public String updateProfileImage(Long userId, MultipartFile file) {
        // 1. 파일 유효성 검사
        if (file.isEmpty() || !file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("유효한 이미지 파일이 아닙니다.");
        }

        // 2. 사용자 정보 조회
        AppUser user = getUser(userId);

        try {
            // 3. 새로운 파일 저장 (기존 프로필 이미지 삭제 -> S3에서는 삭제하지 않고 덮어쓰기)
            String fileName = PROFILE_IMAGE_DIR + UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            // 4. S3 업로드
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

            // 5. DB에 S3 URL 저장
            String s3Url = "https://" + BUCKET_NAME + ".s3.ap-northeast-2.amazonaws.com/" + fileName;
            user.setProfileImageUrl(s3Url);

            // 6. 저장된 이미지 URL 반환
            return s3Url;

        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 중 오류가 발생했습니다.", e);
        }
    }

    /** 계정 삭제 **/
    @Transactional
    public void deleteAccount(Long userId){
        AppUser user = getUser(userId);
        this.accuracySessionRepository.deleteByUserId(userId);
        this.recordedVideoRepository.deleteByUserId(userId);
        this.practiceSessionRepository.deleteByUserId(userId);
        this.challengeSessionRepository.deleteByUserId(userId);
        this.userRepository.delete(user);
    }
}

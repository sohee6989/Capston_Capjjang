package capston.capston_spring.service;

import capston.capston_spring.dto.UserProfile;
import capston.capston_spring.entity.AppUser;
import capston.capston_spring.entity.Role;
import capston.capston_spring.exception.UserNotFoundException;
import capston.capston_spring.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RecordedVideoRepository recordedVideoRepository;
    private final PracticeSessionRepository practiceSessionRepository;
    private final ChallengeSessionRepository challengeSessionRepository;
    private final AccuracySessionRepository accuracySessionRepository;
    private final PasswordEncoder passwordEncoder;
    
    /** 사용자 프로필 이미지 경로 
     * 일단 로컬 디렉토리에 저장하고 배포 시 서버에 저장해야됨
     * **/
    private static final String PROFILE_IMAGE_DIR = "D:\\capston-spring\\src\\main\\resources\\static\\profile_images\\";
    private static final String DEFAULT_PROFILE_IMAGE = PROFILE_IMAGE_DIR + "defaultUserIcon.jpg";

    /** 회원 생성 **/
    public AppUser create(String name, String email, String password){
        // 새로운 사용자 생성
        AppUser user = new AppUser();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setProfileImageUrl(DEFAULT_PROFILE_IMAGE);

        // 특정 이메일만 관리자로 설정
        if("admin@admin.com".equals(email)) {
            user.setRole(Role.ADMIN);
        } else {
            user.setRole(Role.USER);
        }


        this.userRepository.save(user);

        return user;
    }


    /** 회원 조회 **/
    public AppUser getUser(Long id){
        Optional<AppUser> user = this.userRepository.findById(id);

        if(user.isPresent()){
            return user.get();
        }
        else{
            throw new UserNotFoundException("User with id " + id + " not found");
        }
    }

    public AppUser getUser(String email){
        Optional<AppUser> user = this.userRepository.findByEmail(email);
        if(user.isPresent()){
            return user.get();
        } else{
            throw new UserNotFoundException("User with email " + email + " not found");
        }
    }

    /** 회원 프로필 조회 **/
    public UserProfile getProfile(Long id){
        Optional<AppUser> user = this.userRepository.findById(id);

        if(user.isEmpty()){
            throw new UserNotFoundException("사용자가 존재하지 않습니다.");
        }

        String name = user.get().getName();
        String email = user.get().getEmail();
        String profileImageUrl = user.get().getProfileImageUrl();

        return new UserProfile(name, email, profileImageUrl);
    }

    /** 회원 정보 수정 **/
    @Transactional
    public void updateName(Long id, String name){
        Optional<AppUser> user = this.userRepository.findById(id);
        user.get().setName(name);
    }

    @Transactional
    public boolean  updatePassword(Long id,  String currentPassword, String newPassword) {
        Optional<AppUser> user = this.userRepository.findById(id);

        if (!passwordEncoder.matches(currentPassword, user.get().getPassword())) {
            return false; // 현재 비밀번호 불일치
        }

        user.get().setPassword(passwordEncoder.encode(newPassword));
        return true;
    }

    @Transactional
    public String updateProfileImage(Long id, MultipartFile file) {
        // 1. 파일 유효성 검사
        if (file.isEmpty() || !file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("유효한 이미지 파일이 아닙니다.");
        }

        // 2. 사용자 정보 조회
        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        try {
            // 3. 기존 프로필 이미지 삭제
            if (user.getProfileImageUrl() != null && !user.getProfileImageUrl().equals(DEFAULT_PROFILE_IMAGE)) {
                File oldFile = new File(PROFILE_IMAGE_DIR + user.getProfileImageUrl());
                if (oldFile.exists()) {
                    oldFile.delete();
                }
            }

            // 4. 새로운 파일 저장
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            String filePath = PROFILE_IMAGE_DIR + fileName;


            File dest = new File(filePath);
            file.transferTo(dest);  // 파일 저장

            // 5. DB에 이미지 URL 업데이트
            user.setProfileImageUrl("/profile_images/" + fileName);

            // 6. 저장된 이미지 URL 반환
            return user.getProfileImageUrl();

        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 중 오류가 발생했습니다.", e);
        }
    }

    /** 회원 계정 삭제 **/
    @Transactional
    public void deleteAccount(Long id){
        Optional<AppUser> user = this.userRepository.findById(id);
        this.accuracySessionRepository.deleteByUserId(id);
        this.recordedVideoRepository.deleteByUserId(id);
        this.practiceSessionRepository.deleteByUserId(id);
        this.challengeSessionRepository.deleteByUserId(id);
        this.userRepository.delete(user.get());
    }
}

package capston.capston_spring.service;

import lombok.RequiredArgsConstructor;
import capston.capston_spring.dto.UserProfile;
import capston.capston_spring.entity.AppUser;
import capston.capston_spring.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화를 위한 추가


    public AppUser getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    // userId로 유저 조회
    public AppUser getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

    public List<AppUser> getUsersByName(String name) {
        return userRepository.findByNameContaining(name);
    }

    public AppUser saveUser(AppUser user) {
        return userRepository.save(user);
    }

    public UserProfile getProfile(Long userId) {
        AppUser user = getUserById(userId);
        return new UserProfile(user.getName(), user.getEmail(), user.getProfileImageUrl());
    }

    // 사용자 이름 업데이트
    public void updateName(Long userId, String newName) {
        AppUser user = getUserById(userId);
        user.setName(newName);
        userRepository.save(user);
    }

    // 사용자 비밀번호 업데이트 (암호화 적용)
    public boolean updatePassword(Long userId, String currentPassword, String newPassword) {
        AppUser user = getUserById(userId);
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return false; // 현재 비밀번호 불일치 시 false 반환
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }

    // 사용자 프로필 이미지 업데이트
    public void updateProfileImage(Long userId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Invalid profile image file");
        }
        AppUser user = getUserById(userId);
        user.setProfileImageUrl(file.getOriginalFilename()); // 실제 파일 저장 로직 필요
        userRepository.save(user);
    }

    // 사용자 계정 삭제
    public void deleteAccount(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with ID: " + userId);
        }
        userRepository.deleteById(userId);
    }
}
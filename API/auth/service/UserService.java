package capston.capston_spring.service;

import capston.capston_spring.dto.UserProfile;
import capston.capston_spring.entity.AppUser;
import capston.capston_spring.entity.Role;
import capston.capston_spring.exception.UserNotFoundException;
import capston.capston_spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    // 사용자 프로필 이미지 경로
    private static final String PROFILE_IMAGE_DIR = "C:\\Users\\ymdwa\\capston\\profile_images\\";
    private static final String DEFAULT_PROFILE_IMAGE = PROFILE_IMAGE_DIR + "defaultUserIcon.jpg";

    // 회원 생성
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


    // 회원 조회
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

    // 회원 프로필 조회
    public UserProfile getProfile(Long id){
        Optional<AppUser> user = this.userRepository.findById(id);

        if(user.isEmpty()){
            throw new UserNotFoundException("사용자가 존재하지 않습니다.");
        }


        String name = user.get().getName();
        String email = user.get().getEmail();
        String password = user.get().getPassword();
        String profileImageUrl = user.get().getProfileImageUrl();


        return new UserProfile(name, email, password, profileImageUrl);

    }


}

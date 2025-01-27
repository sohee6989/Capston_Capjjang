package capston.capston_spring.service;

import capston.capston_spring.entity.AppUser;
import capston.capston_spring.exception.UserNotFoundException;
import capston.capston_spring.jwt.JWTUtil;
import capston.capston_spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordEncoder encoder;
    private final JWTUtil jwtUtil;

    // 회원 생성
    public AppUser create(String username, String email, String password){
        // 새로운 사용자 생성
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        this.userRepository.save(user);

        return user;
    }

    // 회원 조회
    public AppUser getUser(String email){
        Optional<AppUser> user = this.userRepository.findByEmail(email);
        if(user.isPresent()){
            return user.get();
        } else{
            throw new UserNotFoundException("User with email " + email + " not found");
        }
    }


}

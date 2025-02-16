package capston.capston_spring.service;

import capston.capston_spring.entity.AppUser;
import capston.capston_spring.exception.UserNotFoundException;
import capston.capston_spring.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    private final Map<String, String> verificationCodes = new HashMap<>();

    // 1. 이메일 입력 후 인증 코드 전송
    public void sendVerificationCode(String email){
        AppUser user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // 6자리 인증 코드 생성
        String code = generateVerificationCode();
        verificationCodes.put(email, code);

        sendEmail(email, code);
    }

    // 랜덤 코드 생성
    private String generateVerificationCode(){
        return String.valueOf(100000 + new Random().nextInt(900000));
    }
    
    // 이메일로 인증 코드 전송
    private void sendEmail(String email, String code){
        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setTo(email);
            helper.setSubject("Password Reset Code");
            helper.setText("Your verification code is: " + code);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email");
        }
    }

    // 2. 인증 코드 검증
    public boolean verifyCode(String email, String code){
        return verificationCodes.containsKey(email) && verificationCodes.get(email).equals(code);
    }

    // 3. 새 비밀번호 설정 (이메일만 확인)
    @Transactional
    public void resetPassword(String email, String newPassword){
        AppUser user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));


        // 비밀번호 암호화 후 저장
        user.setPassword(passwordEncoder.encode(newPassword));
        this.userRepository.save(user);

        // 인증 코드 제거
        verificationCodes.remove(email);
    }

}

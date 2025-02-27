package capston.capston_spring.controller;

import capston.capston_spring.dto.JoinRequest;
import capston.capston_spring.dto.LoginRequest;
import capston.capston_spring.jwt.JWTUtil;
import capston.capston_spring.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final UserService userService;
    private final JWTUtil jwtUtil;


    /**
     * 회원 가입
     **/
    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody @Valid JoinRequest joinRequest, BindingResult bindingResult) {
        // 회원가입 폼 오류 (전체 필드 유효성 검사)
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errorMessages);
        }

        // 비밀번호 확인 불일치
        if (!joinRequest.getPassword1().equals(joinRequest.getPassword2())) {
            return ResponseEntity.badRequest().body("The passwords do not match.");
        }

        // 사용자 생성
        try {
            this.userService.create(joinRequest.getName(), joinRequest.getEmail(), joinRequest.getPassword1());
        } catch (DataIntegrityViolationException e) {
            // 중복 사용자 오류 처리
            e.printStackTrace();
            return ResponseEntity.badRequest().body("User already exists.");
        } catch (Exception e) {
            // 다른 예외 처리
            e.printStackTrace();
            return ResponseEntity.badRequest().body("An unexpected error occurred: " + e.getMessage());
        }

        return ResponseEntity.ok().body("User successfully registered.");
    }


    /**
     * 로그인 토큰
     * 헤더의 토큰을 확인하기 위한 테스트 메소드
     * 나중에 제거
     **/
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest, BindingResult bindingResult) {
        // 로그인 폼 오류 (전체 필드 유효성 검사)
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errorMessages);
        }

        return ResponseEntity.ok().body("The token in the header was verified.");
    }

    @GetMapping({"/loginNaver", "/loginGoogle"})
    public ModelAndView loginOauth(){
        ModelAndView modelAndView = new ModelAndView("socialLogin"); // "socialLogin" 뷰를 반환
        return modelAndView;
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(){
        return ResponseEntity.ok("로그아웃되었습니다. 클라이언트에서 JWT를 삭제하세요.");
    }
}

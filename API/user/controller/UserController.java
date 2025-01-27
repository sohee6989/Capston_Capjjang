package capston.capston_spring.controller;

import capston.capston_spring.dto.SigninRequest;
import capston.capston_spring.dto.SignupRequest;
import capston.capston_spring.jwt.JWTUtil;
import capston.capston_spring.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final JWTUtil jwtUtil;


    /** 회원 가입 **/
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid SignupRequest signupRequest, BindingResult bindingResult){
        // 회원가입 폼 오류 (전체 필드 유효성 검사)
        if(bindingResult.hasErrors()){
            List<String> errorMessages = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errorMessages);
        }

        // 비밀번호 확인 불일치
        if(!signupRequest.getPassword1().equals(signupRequest.getPassword2())){
            return ResponseEntity.badRequest().body("The passwords do not match.");
        }

        // 사용자 생성
        try{
            this.userService.create(signupRequest.getUsername(), signupRequest.getEmail(), signupRequest.getPassword1());
        }catch(DataIntegrityViolationException e){
            // 중복 사용자 오류 처리
            e.printStackTrace();
            return ResponseEntity.badRequest().body("User already exists.");
        }catch (Exception e){
            // 다른 예외 처리
            e.printStackTrace();
            return ResponseEntity.badRequest().body("An unexpected error occurred: " + e.getMessage());
        }

        return ResponseEntity.ok().body("User successfully registered.");
    }



    /** 로그인 **/
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody SigninRequest signinRequest, BindingResult bindingResult){
        // 로그인 폼 오류 (전체 필드 유효성 검사)
        if(bindingResult.hasErrors()){
            List<String> errorMessages = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errorMessages);
        }

        return null;
    }






}

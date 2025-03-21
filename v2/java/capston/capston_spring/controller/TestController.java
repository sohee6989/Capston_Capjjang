package capston.capston_spring.controller;

import capston.capston_spring.dto.CustomUserDetails;
import capston.capston_spring.entity.AppUser;
import capston.capston_spring.entity.VideoMode;
import capston.capston_spring.repository.RecordedVideoRepository;
import capston.capston_spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TestController {

    private final UserService userService;
    private final RecordedVideoRepository recordedVideoRepository;

    @PostMapping("/test")
    @ResponseBody
    public ResponseEntity<String> test(Authentication authentication){
        if (authentication == null || !authentication.isAuthenticated()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요한 요청입니다.");
        }
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        AppUser user = this.userService.getUser(userDetails.getEmail());
        String name = user.getName();
        return ResponseEntity.ok( name + "님 안녕하세요");
    }


    @ResponseBody
    @GetMapping("/")
    public String show(){
        return "루트페이지";
    }


    private void assertEquals(VideoMode videoMode, VideoMode mode) {
    }

}

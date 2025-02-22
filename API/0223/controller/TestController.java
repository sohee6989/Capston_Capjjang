package capston.capston_spring.controller;

import capston.capston_spring.dto.CustomUserDetails;
import capston.capston_spring.entity.AppUser;
import capston.capston_spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TestController {

    private final UserService userService;

    @PostMapping("/test")
    @ResponseBody
    public String test(Authentication authentication){
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        AppUser user = this.userService.getUser(userDetails.getEmail());
        String name = user.getName();
        return name + "님 안녕하세요";
    }


    @ResponseBody
    @GetMapping("/")
    public String show(){
        return "루트페이지";
    }
}

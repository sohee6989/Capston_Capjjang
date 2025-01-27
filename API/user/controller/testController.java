package capston.capston_spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@PreAuthorize("isAuthenticated()")
public class testController {
    @PostMapping("/test")
    @ResponseBody
    public String test(){
        return "인증된 사용자이므로 다른 api 호출 가능";
    }

}

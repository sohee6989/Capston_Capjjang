package capston.capston_spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TestController {

    @PostMapping("/test")
    @ResponseBody
    public String test(){
        return "인증된 사용자이므로 다른 api 호출 가능";
    }


    @ResponseBody
    @GetMapping("/")
    public String show(){
        return "루트페이지";
    }
}

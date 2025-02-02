package capston.capston_spring.controller;

import capston.capston_spring.dto.CustomUserDetails;
import capston.capston_spring.dto.UserProfile;
import capston.capston_spring.entity.AppUser;
import capston.capston_spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    /** 마이페이지 메인
     * user info 전달 -> 프로필사진, 이름, 이메일
     * **/
    @GetMapping("/home")
    public ResponseEntity<?> getProfile(Authentication authentication){
        // 현재 로그인된 사용자 정보 가져오기
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        AppUser user = userService.getUser(userDetails.getEmail());


        UserProfile userProfile = userService.getProfile(user.getId());

        return ResponseEntity.ok(userProfile);

    }



    /** edit profile
     * 이름, 비밀번호, 프로필사진 수정 가능
     * 계정 삭제 가능
     * **/

    /** my video
     * practie, challenge 비디오 확인 가능
     * recording on/off 변경 가능
     */


    /** my score
     * 사용자가 플레이한 노래에 대한 점수 확인
     * 노래 이미지, 노래 제목, 가수, 점수(BEST, GOOD, BAD)
     */

    /** info App
     * Licentse페이지 이동
     * Help Center 페이지 이동
     */

    /** logout
     *
     */


}

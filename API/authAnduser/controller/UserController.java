package capston.capston_spring.controller;

import capston.capston_spring.dto.*;
import capston.capston_spring.entity.AppUser;
import capston.capston_spring.service.PracticeSessionService;
import capston.capston_spring.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final PracticeSessionService practiceSessionService;

    /**
     * 마이페이지 메인, my profile
     * user info 전달 -> 프로필사진, 이름, 이메일
     **/
    @GetMapping({"/home", "/profile"})
    public ResponseEntity<UserProfile> getProfile(Authentication authentication) {
        // 현재 로그인된 사용자 정보 가져오기
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        AppUser user = userService.getUser(userDetails.getEmail());

        UserProfile userProfile = userService.getProfile(user.getId());

        return ResponseEntity.ok(userProfile);
    }


    /**
     * edit profile
     * 이름, 비밀번호, 프로필사진 수정 가능
     * 계정 삭제 가능
     **/
    @PatchMapping("/profile/edit/name")
    public ResponseEntity<?> editProfileName(Authentication authentication, @RequestBody @Valid EditNameRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("이름은 비어 있을 수 없습니다.");
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        AppUser user = userService.getUser(userDetails.getEmail());

        this.userService.updateName(user.getId(), request.getName());

        return ResponseEntity.ok("이름이 성공적으로 수정되었습니다.");
    }

    @PatchMapping("/profile/edit/password")
    public ResponseEntity<?> editProfilePassword(Authentication authentication, @RequestBody @Valid EditPasswordRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("비밀번호를 정확히 입력하세요.");
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        AppUser user = userService.getUser(userDetails.getEmail());

        boolean success = this.userService.updatePassword(user.getId(), request.getCurrentPassword(), request.getNewPassword());

        if (!success) {
            return ResponseEntity.badRequest().body("현재 비밀번호가 일치하지 않습니다.");
        }

        return ResponseEntity.ok("비밀번호가 성공적으로 수정되었습니다.");
    }

    @PatchMapping(value = "/profile/edit/img", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> editProfileImg(Authentication authentication, @RequestParam("file") MultipartFile file) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        AppUser user = userService.getUser(userDetails.getEmail());

        this.userService.updateProfileImage(user.getId(), file);

        return ResponseEntity.ok("프로필 이미지가 성공적으로 수정되었습니다.");
    }

    @DeleteMapping("/profile/deleteAccount")
    public ResponseEntity<?> deleteAccount(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        AppUser user = userService.getUser(userDetails.getEmail());

        this.userService.deleteAccount(user.getId());

        return ResponseEntity.ok("계정이 성공적으로 삭제되었습니다.");
    }


    /** my video
     * practie, challenge 비디오 확인 가능
     * recording on/off 변경 가능
     */


    /**
     * my score
     * 사용자가 플레이한 노래에 대한 점수 확인
     * 노래 이미지, 노래 제목, 가수, 점수(BEST, GOOD, BAD)
     */
    @GetMapping("/history")
    public List<PracticeSessionRequest> getPracticeHistory(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        AppUser user = this.userService.getUser(userDetails.getEmail());
        Long userId = user.getId();

        return this.practiceSessionService.getPracticeHistory(userId);
    }

    /** info App
     * Licentse페이지 이동
     * Help Center 페이지 이동
     */

    /** logout
     *
     */


}

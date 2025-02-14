# must read

# 1. 회원가입 및 로그인 인증
### config - SecurityConfig, SwaggerConfiguration: Swagger에서 Authorization사용하려면 설정 해야합니다
### controller - AuthController : 회원가입, 로그인
### dto - CustomUserDetails, JoinRequest, LoginRequest
### entity - AppUser: 사용자 객체 정의, Role: 사용자 역할 정의
### exception - UserNotFoundException
### jwt - JWTFilter, JWTUtil, LoginFilter
### oauth2 - OAuth2AuthenticationSuccessHandler
### repository - UserRepository
### service - UserService, CustomUserDetailsService, CustomOAuth2UserService

</br>

로직
1. 사용자 로그인 (/login) 하면 클라이언트에게 JWT 발급 됩니다. 
2. 사용자로부터 Request를 받을 때 Header의 Authorization 필드에 JWT토큰이 들어있습니다. (사용자 인증 과정은 JWTFilter에 구현되어있으니 신경쓰지 않으셔도 됩니다.)
3. 사용자 인증이 필요한 메서드의 경우 
```
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
```
위 메소드 처럼 Authentication으로 매개변수를 받고 CustomUserDetails로 캐스팅한 후 userService의 getUser메소드를 이용하시면 사용자 객체를 이용할 수 있습니다. 

</br>

*** 
CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

AppUser user = this.userService.getUser(userDetails.getEmail());
***
위 파일들 그대로 가져가신 다음, 메소드 구현하실 때 위 두 줄을 그냥 그대로 사용하셔서 사용자 테이블에서 데이터 조회하면 됩니다.

오류 시 연락 주세요
   
</br>

# 2. 마이페이지 
### controller - UserController 
### dto - AccuracySessonResponse, EditNameRequest EditPasswordRequest, EditPasswordRequest, MyvideoResponse, UserProfile
### entity - ...
### exception - VideorNotFoundException
### repository - ..
### service - ..




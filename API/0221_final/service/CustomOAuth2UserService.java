package capston.capston_spring.service;

import capston.capston_spring.entity.AppUser;
import capston.capston_spring.entity.Role;
import capston.capston_spring.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@AllArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    /** 사용자 프로필 이미지 경로 **/
    private static final String PROFILE_IMAGE_DIR = "C:\\Users\\ymdwa\\capston\\profile_images\\";
    private static final String DEFAULT_PROFILE_IMAGE = PROFILE_IMAGE_DIR + "defaultUserIcon.jpg";

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        // 어떤 OAuth 제공자인지 확인 (Google, Kakao, Naver)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email;
        String name;
        String userId;

        if ("google".equals(registrationId)) {
            email = (String) attributes.get("email");
            name = (String) attributes.get("name");
            userId = (String) attributes.get("sub"); // Google에서는 'sub' 필드를 사용
        } else if ("kakao".equals(registrationId)) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            email = (String) kakaoAccount.get("email");
            name = (String) kakaoAccount.get("nickname");
            userId = String.valueOf(attributes.get("id")); // Kakao에서 'id'를 사용
        } else if ("naver".equals(registrationId)) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response"); // Naver는 'response' 내부에 정보
            email = (String) response.get("email");
            name = (String) response.get("name");
            userId = String.valueOf(response.get("id")); // Naver에서 'id'를 사용
            attributes = response;  // 'response' 안의 값으로 덮어쓰기 -> Naver에서는 response로 묶어서 데이터가 전달됨
        } else {
            throw new OAuth2AuthenticationException("Unsupported OAuth provider.");
        }

        if (email == null) {
            throw new OAuth2AuthenticationException("Email not found from OAuth2 provider.");
        }

        // DB에서 사용자 조회 또는 새로 생성
        AppUser appUser = this.userRepository.findByEmail(email)
                .orElseGet(() -> {
                    AppUser newUser = new AppUser();
                    newUser.setEmail(email);
                    newUser.setName(name);
                    newUser.setPassword(""); // OAuth2 사용자는 비밀번호 없음
                    newUser.setRole(Role.USER); // 기본 ROLE 설정
                    newUser.setProfileImageUrl(DEFAULT_PROFILE_IMAGE); // 기본 프로필 이미지 설정
                    return userRepository.save(newUser);
                });

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(appUser.getRole().toString())),
                attributes,
                userId // OAuth provider의 ID 필드를 사용
        );
    }
}

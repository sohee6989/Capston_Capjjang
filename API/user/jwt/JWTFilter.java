package capston.capston_spring.jwt;

import capston.capston_spring.dto.CustomUserDetails;
import capston.capston_spring.entity.AppUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/** 스프링 시큐리티 filter chain에 요청에 담긴 JWT를 검증하기 위한 커스텀 필터를 등록
 * 해당 필터를 통해 요청 헤더 Authorization 키에 JWT가 존재하는 경우 JWT를 검증하고 강제로SecurityContextHolder에 세션을 생성
 * **/

@AllArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        
        // request에서 Authorization 헤더를 찾음
        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer")) {
            System.out.println("token null");
            filterChain.doFilter(request, response);

            return; //조건이 해당되면 메소드 종료 (필수)
        }

        System.out.println("authorization now");

        // Bearer 부분 제거 후 순수 토큰만 획득
        String token = authorization.split(" ")[1];

        if (jwtUtil.isExpired(token)) {
            System.out.println("token expired");
            filterChain.doFilter(request, response);

            return; //조건이 해당되면 메소드 종료 (필수)
        }

        String email = jwtUtil.getEmail(token);
        String role = jwtUtil.getRole(token);

        AppUser appUser = new AppUser();
        appUser.setEmail(email);
        appUser.setUsername("tempusername");
        appUser.setPassword("temppassword");
        appUser.setRole(role);

        CustomUserDetails customUserDetails = new CustomUserDetails(appUser);

        // 스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        // 세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);


        filterChain.doFilter(request, response);

    }
}

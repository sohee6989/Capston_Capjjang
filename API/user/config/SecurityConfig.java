package capston.capston_spring.config;

import capston.capston_spring.jwt.JWTFilter;
import capston.capston_spring.jwt.JWTUtil;
import capston.capston_spring.jwt.LoginFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //AuthenticationManager가 인자로 받을 AuthenticationConfiguraion 객체 생성자 주입
    private final AuthenticationConfiguration authenticationConfiguration;
    private final ObjectMapper objectMapper;
    private final JWTUtil jwtUtil;


    // AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        // CSRF, CORS -> JWT방식을 사용하므로 disable로 설정
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(Customizer.withDefaults());


        // 폼 로그인 방식, http basic 인증 방식 disable
        http.httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable);


        // Session Stateless 설정
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        // 권한 규칙 작성
        http
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                                .requestMatchers("/user/signup", "/", "/user/signin", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**").permitAll() // 회원가입, 로그인 요청 모두 접근 허가
                                .anyRequest().authenticated() // 그 외 모든 요청 인증처리
                );

        // 필터 추가
        http
                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);


        // 필터 추가
        // LoginFilter()는 인자를 받음 (AuthenticationManager() 메소드에 authenticationConfiguration 객체를 넣어야 함) 따라서 등록 필요
        System.out.println("Registering LoginFilter");
        LoginFilter loginFilter = new LoginFilter(authenticationManager(authenticationConfiguration), objectMapper, jwtUtil);
        loginFilter.setFilterProcessesUrl("/user/signin");
        http
                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);
        System.out.println("LoginFilter is working");

        // 리턴
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

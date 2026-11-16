//package me.kkanyo.springbootpractice.config;
//
//import lombok.RequiredArgsConstructor;
//import me.kkanyo.springbootpractice.service.UserDetailService;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.ProviderManager;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;
//
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class WebSecurityConfig {
//
//    private final UserDetailService userService;
//
//    // 스프링 시큐리티 기능 비활설화 (일반적으로 정적 리소스(이미지, HTML 파일)에 설정)
//    // 즉, 인증, 인가 서비스를 모든 곳에 적용하지는 않는다.
//    @Bean
//    public WebSecurityCustomizer configure() {
//        return (web) -> web.ignoring()
//                .requestMatchers(toH2Console())
//                .requestMatchers(new AntPathRequestMatcher("/static/**"));
//    }
//
//    // 특정 HTTP 요청에 대한 웹 기반 보안 구성
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        return http
//                .authorizeHttpRequests(auth -> auth // 인증, 인가 설정
//                        .requestMatchers(   // 특정 요청과 일치하는 url에 대한 액세스를 설정
//                        new AntPathRequestMatcher("/login"),
//                        new AntPathRequestMatcher("/signup"),
//                        new AntPathRequestMatcher("/user")
//                    ).permitAll()   // 누구나 접근 가능하게 설정
//                    .anyRequest()   // 위에서 설정한 url 이외의 요청에 대해서 설정
//                    .authenticated())   // 별도의 인가는 필요하지만 인증이 성공된 상태여야 접근 가능
//                .formLogin(formLogin -> formLogin   // 폼 기반 로그인 설정
//                        .loginPage("/login")
//                        .defaultSuccessUrl("/articles")
//                )
//                .logout(logout -> logout    // 로그아웃 설정
//                        .logoutSuccessUrl("/login")
//                        .invalidateHttpSession(true)    // 로그아웃 이후에 세션을 전체 삭제할지 여부를 설정
//                )
//                .csrf(AbstractHttpConfigurer::disable)  // csrf 비활성화 (CSRF 공격을 방지하기 위해서는 활성화 필요!)
//                .build();
//    }
//
//    // 인증 관리자 관련 설정
//    // 사용자 정보를 가져올 서비스를 재정의하거나, 인증 방법을 설정할 때 사용
//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http,
//                                                       BCryptPasswordEncoder bCryptPasswordEncoder,
//                                                       UserDetailService userService)
//            throws Exception {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userService); // 사용자 정보 서비스 설정
//        authProvider.setPasswordEncoder(bCryptPasswordEncoder);
//
//        return new ProviderManager(authProvider);
//    }
//
//    // 패스워드 인코더로 사용할 빈 등록
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}

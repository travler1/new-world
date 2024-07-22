package myproject.web.config;

import lombok.RequiredArgsConstructor;
import myproject.web.config.auth.PrincipalDetailsService;
import myproject.web.config.oauth.PrincipalOauth2UserService;
import myproject.web.config.oauth.loginValid.CustomAuthenticationFailureHandler;
import myproject.web.config.oauth.loginValid.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

//1.코드받기(인증) 2. 엑세스토큰(권한)
//3. 사용자프로필 정보를 가져오고 4-1. 그 정보를 토대로 회원가입을 자동으로 진행시키기도 함.
//4-2. (이메일, 전화번호, 이름, 아이디) ... 쇼핑몰 -> (집주소), 백화점몰 -> (vip등급, 일반등급) 추가적인 회원가입창이 나오게.
//하지만, 이메일, 전화번호, 이름, 아이디만 필요하다면 이 정보만 가지고 회원가입을 자동으로 시킴.

@Configuration
@EnableWebSecurity //스프링 시큐리티 필터(SecurityConfig)가 스프링 필터체인에 등록이 됩니다.
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true) //@Secured 어노테이션 활성화. 컨트롤러 메서드에 @Secured("Role_admin") 이런 식으로. 두번째는 PreAuthorize 어노테이션 활성화. @PreAuthorize("hasRole("ROLE_User")") 이런식으로. 얘는 메서드 실행되기 직전에 실행됨. 여러 개의 권한설정 가능. @PostAuthorize도 있음. 메서드가 종료되고 나서 실행인데 @Pre가 @Post도 포함. @Post만 쓸일은 잘 없음.
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationProvider customAuthenticationProvider;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final PrincipalOauth2UserService principalOauth2UserService;
    private final PrincipalDetailsService principalDetailsService;


    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.authenticationProvider(customAuthenticationProvider);
        customAuthenticationProvider.setUserDetailsService(principalDetailsService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        //SpringSecurity 6.1 이후로 람다식 설정으로 바뀜.
        http.csrf(AbstractHttpConfigurer::disable); //csrf 불필요

        http.authorizeHttpRequests(authorize -> authorize.requestMatchers("/myPage/**").authenticated() //인증만 되면 들어갈 수 있는 주소
                        //ADMIN으로 들어오게 되면 ADMIN 권한이 있어야함.
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        //다른 주소로는 다 권한이 허용
                        .anyRequest().permitAll())
                //권한이 필요한 경우 로그인 페이지로 이동함.,             //로그인 주소가 호출이 되면 스프링 시큐리티가 낚아채서 로그인 진행, 컨트롤러에 /login이 없어도 가능
                .formLogin(formLogin -> formLogin.loginPage("/loginForm").permitAll()
                        .failureHandler(customAuthenticationFailureHandler)
                        .loginProcessingUrl("/login") // 로그인 폼의 action 경로를 "/login"으로 설정
                        .defaultSuccessUrl("/logged"))// 로그인 성공 시 기본적으로 리디렉션되는 URL을 "/"로 설정
        //oauth로그인페이지나 일반 로그인페이지나 똑같이 설정
        .oauth2Login(oauth2Login -> oauth2Login.loginPage("/loginForm") //구글 로그인이 완료된 뒤의 후처리가 필요함
        // Tip.코드X,(엑세스토큰+사용자프로필정보O) 한 방에 받음. //userService의 타입은 OAuth2UserService? loadUser라는 함수에서 후처리가 됨.
              .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.userService(principalOauth2UserService)));

        return http.build();
    }
}

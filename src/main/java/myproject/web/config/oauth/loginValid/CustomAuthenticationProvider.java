package myproject.web.config.oauth.loginValid;

import myproject.web.config.CustomBCryptoPasswordEncoder;
import myproject.web.config.auth.PrincipalDetailsService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@Configuration
public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

    private final CustomBCryptoPasswordEncoder customBCryptoPasswordEncoder;

    public CustomAuthenticationProvider(PrincipalDetailsService principalDetailsService, CustomBCryptoPasswordEncoder customBCryptoPasswordEncoder) {
        super.setUserDetailsService(principalDetailsService);
        //비밀번호 검증을 위해 설정.
        super.setPasswordEncoder(customBCryptoPasswordEncoder);
        this.customBCryptoPasswordEncoder = customBCryptoPasswordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();

        if(username == null || username.isEmpty()){
            throw new IllegalUsernameException("이메일을 입력해주세요");
        }
        if(password == null || password.isEmpty()){
            throw new IllegalPasswordException("비밀번호를 입력해주세요.");
        }

        return super.authenticate(authentication);
    }

}

package myproject.web.config.oauth.loginValid;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String error = "object";
        String errorMessage = "이메일 또는 비밀번호를 입력하세요";

        if(exception instanceof IllegalUsernameException){
            error = "username";
            errorMessage = exception.getMessage();
        }else if(exception instanceof IllegalPasswordException){
            error = "password";
            errorMessage = exception.getMessage();
        }else if(exception instanceof UsernameNotFoundException){
            errorMessage = "사용자가 존재하지 않습니다.";
        } else if (exception instanceof BadCredentialsException) {
            errorMessage = "이메일 또는 비밀번호를 확인해주세요.";
        }

        errorMessage = URLEncoder.encode(errorMessage, "UTF-8");
        setDefaultFailureUrl("/loginForm?error=" + error + "&errorMessage=" + errorMessage);
        super.onAuthenticationFailure(request, response, exception);
    }
}

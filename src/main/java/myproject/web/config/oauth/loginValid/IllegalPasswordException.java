package myproject.web.config.oauth.loginValid;

import org.springframework.security.core.AuthenticationException;

public class IllegalPasswordException extends AuthenticationException {

    public IllegalPasswordException(String msg) {
        super(msg);
    }
}

package myproject.web.config.oauth.loginValid;

import org.springframework.security.core.AuthenticationException;

public class IllegalUsernameException extends AuthenticationException {

    public IllegalUsernameException(String msg) {
        super(msg);
    }
}

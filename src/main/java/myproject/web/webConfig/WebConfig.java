package myproject.web.webConfig;

import lombok.RequiredArgsConstructor;
import myproject.LoginAccountArgumentResolver;
import myproject.web.webConfig.interceptor.LogInterceptor;
import myproject.web.webConfig.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import java.util.List;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer, WebSocketConfigurer {

    private final LoginAccountArgumentResolver loginAccountArgumentResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/*.ico", "/error", "/resources/**",
                "/images/**", "/js/**");

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns( "/", "/members/register/**", "/noNeedLogin/resultAlert","members/findPassword",
                        "/login", "/logout",
                        "/css/**", "/*.ico", "/error", "/resources/**","/images/**", "/js/**");
    }

    //웹 소켓 핸들러(채팅기능구현)
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry){
        registry.addHandler(new SocketHandler(), "message-ws").setAllowedOrigins("*");
    }


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
        resolvers.add(loginAccountArgumentResolver);
    }
}

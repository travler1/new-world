package myproject.web.config.auth;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import myproject.domain.member.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Data
@Slf4j
public class PrincipalDetails implements UserDetails, OAuth2User {

    private Member member;
    private Map<String, Object> attributes;

    //일반 로그인
    public PrincipalDetails(Member member, Map<String, Object> attributes) {
        this.member = member;
        this.attributes = attributes;
    }

    public PrincipalDetails(Member member) {
        this.member = member;
    }

    public String getEmail() {
        if (member != null) {
            return member.getEmail();
        }else{
            return attributes.get("email").toString();
        }
    }

    //해당 유저의 관심을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return member.getGrade().toString();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getUsername();
    }

    //계정이 만료되었는지
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //계정이 잠겼는지
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //비밀번호 기간이 지났는지
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //계정이 활성화되었는지
    @Override
    public boolean isEnabled() {
        //우리 사이트!! 1년 동안 회원이 로그인을 안하면!! 휴면 계정이라 함.
        //user.getLoginDate();
        //현재시간 - 로긴시간 => 1년을 초과하면 return false;

        return true;
    }
    //==================OAuth2User 상속 후 오버라이딩======================//
    @Override
    public String getName() {
       return attributes.get("providerId").toString();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}

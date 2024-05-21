/*
package myproject.HelloWorld;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringRunner.class) //스프링과 관련된 걸로 테스트
@SpringBootTest
class MemberRepository1Test {

    @Autowired
    MemberRepository1 memberRepository1;

    @Test
    @Transactional
    @Rollback(false)
    public void testMember() throws Exception{
        //given
        Member1 member = new Member1();
        member.setUsername("memberA");

        //when
        Long saveId = memberRepository1.save(member);
        Member1 findMember = memberRepository1.find(saveId);

        //then
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member);


    }
}*/

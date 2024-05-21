package myproject.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    //회원정보 저장
    //public Member save(Member member);

    //이메일 중복체크
    @Query("")
    public Optional<Member> findMembersByEmailAndPassword(String email, String password);

    //회원정보수정
    //public Member updateMemberById(Long id, Member member);

    //비밀번호 변경
    @Query("update Member m set m.password =: password where m.id =: id")
    public int updatePasswordById(@Param("password") String password, @Param("id") Long id);

    //회원삭제
    public void deleteMemberById(Long member_id);

    //자동로그인
    //@Query("update Member m set m.au_id =: au_id WHERE m.id =: id")
    //public Long updateAu_idById(@Param("au_id") String au_id, @Param("id") Long id);
    //public Member findMemberByAu_id(String au_id);
    //public int deleteAu_idById(Long id);

    //채팅회원이름검색
    public List<Member> findMemberByUsername(String username);


    Member findMemberById(Long id);
}

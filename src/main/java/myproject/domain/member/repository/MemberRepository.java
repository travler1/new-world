package myproject.domain.member.repository;

import myproject.domain.member.Member;
import myproject.web.file.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    //이메일 중복체크
    @Query("")
    Optional<Member> findMembersByEmailAndPassword(String email, String password);

    //회원정보수정
    //public Member updateMemberById(Long id, Member member);

    //비밀번호 변경
    @Query("update Member m set m.password =: password where m.id =: id")
    int updatePasswordById(@Param("password") String password, @Param("id") Long id);

    //회원삭제
    void deleteMemberById(Long member_id);

    //채팅회원이름검색
    List<Member> findMemberByUsername(String username);

    Member findMemberById(Long id);

    //회원 프로필 이미지 등록
    //@Query("update Member m set m.profileImage =:profileImage where m.id =:id")
    //public void updateProfileById(@Param("id") Long id, @Param("profileImage") UploadFile uploadFile);

    //회원 프로필 이미지 찾기
    @Query("select m.profileImage from Member m where m.id=:id")
    UploadFile findUploadFileById(@Param("id") Long id);

    //회원 프로필 이미지 등록
    @Modifying
    @Query("update Member m set m.profileImage =:profileImage where m.id =:id")
    void updateUploadFileById(@Param("id") Long id, @Param("profileImage") UploadFile uploadFile);

    //이메일로 회원 조회
    @Query("select m from Member m where m.email=:email")
    Optional<Member> findMemberByEmail(@Param("email") String email);

    //이름으로 회원조회
    Optional<Member> findMemberByName(String name);
}

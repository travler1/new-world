package myproject.domain.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import lombok.extern.slf4j.Slf4j;
import myproject.domain.member.QMember;
import myproject.web.file.UploadFile;

import static myproject.domain.member.QMember.*;

@Slf4j
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;
    public MemberRepositoryCustomImpl(EntityManager em){
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override //회원 프로필 이미지 등록
    public void updateProfileById(Long id, UploadFile uploadFile) {

        log.info("MemberRepositoryCustomImpl 진입");

        jpaQueryFactory.update(member)
                .set(member.profileImage, uploadFile)
                .where(member.id.eq(id))
                .execute();
    }

    @Override
    public void deleteProfileById(Long id, UploadFile profileImage) {

        jpaQueryFactory.update(member)
                .set(member.profileImage, new UploadFile(null, null))
                .where(member.id.eq(id))
                .execute();
    }
}

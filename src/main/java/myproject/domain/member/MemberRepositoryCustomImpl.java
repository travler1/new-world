package myproject.domain.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.web.file.UploadFile;

@Slf4j
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;
    public MemberRepositoryCustomImpl(EntityManager em){
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override //회원 프로필 이미지 등록
    public void updateProfileById(Long id, UploadFile uploadFile) {

        log.info("MemberRepositoryCustomImpl 진입");

        jpaQueryFactory.update(QMember.member)
                .set(QMember.member.profileImage, uploadFile)
                .where(QMember.member.id.eq(id))
                .execute();
    }

    @Override
    public void deleteProfileById(Long id, UploadFile profileImage) {

        jpaQueryFactory.update(QMember.member)
                .set(QMember.member.profileImage, new UploadFile(null, null))
                .where(QMember.member.id.eq(id))
                .execute();
    }
}

package myproject.domain.matching.advice;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
public class AdviceRepositoryCustomImpl implements AdviceRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;
    public AdviceRepositoryCustomImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }


}

package myproject.domain.matching.advice.repository;

import myproject.domain.matching.advice.entity.Advice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdviceRepository extends JpaRepository<Advice, Long>, AdviceRepositoryCustom {
    Advice findAdviceById(Long id);
}

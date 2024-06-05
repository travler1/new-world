package myproject.domain.matching.advice;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdviceRepository extends JpaRepository<Advice, Long>, AdviceRepositoryCustom {
    Advice findAdviceById(Long id);
}

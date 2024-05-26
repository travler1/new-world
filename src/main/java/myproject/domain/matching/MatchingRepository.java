package myproject.domain.matching;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchingRepository extends JpaRepository<EmpInfo, Long> , MatchingRepositoryCustom{


}

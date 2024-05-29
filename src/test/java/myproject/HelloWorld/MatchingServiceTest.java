package myproject.HelloWorld;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import myproject.domain.matching.MatchingRepository;
import myproject.domain.matching.MatchingService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Transactional
public class MatchingServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    MatchingRepository matchingRepository;

    @Test //쿼리검증
    public void empInfoFindTop1000() throws JsonProcessingException {
        List<Long> allEmpMemberIdTop1000 = matchingRepository.findEmpMemberIdTop1000();
        System.out.println(allEmpMemberIdTop1000);
    }
}

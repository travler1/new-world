package myproject.domain.matching;

import myproject.web.file.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatchingRepository extends JpaRepository<EmpInfo, Long> , MatchingRepositoryCustom{

    @Query("select e.uploadFile from EmpInfo e where e.id =:id")
    public UploadFile findUploadFileById(@Param("id") Long id);

}

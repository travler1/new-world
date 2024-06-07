package myproject.domain.matching.emp;

import myproject.web.file.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmpRepository extends JpaRepository<EmpInfo, Long> , EmpRepositoryCustom {

    @Query("select e.uploadFile from EmpInfo e where e.id =:id")
    public UploadFile findUploadFileById(@Param("id") Long id);

}

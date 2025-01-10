package rs.raf.web3.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.raf.web3.model.ErrorMessage;

@Repository
public interface ErrorRepository extends JpaRepository<ErrorMessage,Long> {
    @Query("SELECT e FROM ErrorMessage e")
    Page<ErrorMessage> findAll(Pageable pageable);
    Page<ErrorMessage> findErrorMessageByUser_Id(Long id, Pageable pageable);
}

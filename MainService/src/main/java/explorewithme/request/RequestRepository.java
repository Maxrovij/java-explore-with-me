package explorewithme.request;

import explorewithme.request.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query(value = "select count(*) from requests where event_id=?1 and status='CONFIRMED'", nativeQuery = true)
    int getConfirmed(Long id);

    List<Request> findAllByRequesterId(long id);
}

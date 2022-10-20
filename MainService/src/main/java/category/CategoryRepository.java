package category;

import category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(value = "select * from categories limit ?2 offset ?1 order by id", nativeQuery = true)
    List<Category> getAll(int from, int size);
}

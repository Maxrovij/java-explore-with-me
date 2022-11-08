package ru.practicum.explorewithme.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.category.model.Category;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(value = "select * from categories order by id limit ?2 offset ?1", nativeQuery = true)
    List<Category> getAll(int from, int size);
}

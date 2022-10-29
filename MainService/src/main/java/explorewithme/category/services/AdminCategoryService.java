package explorewithme.category.services;

import explorewithme.category.CategoryMapper;
import explorewithme.category.CategoryRepository;
import explorewithme.category.model.Category;
import explorewithme.category.model.CategoryDto;
import explorewithme.exception.EntityAlreadyExistsException;
import explorewithme.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminCategoryService {

    private final CategoryRepository repository;

    public CategoryDto patch(CategoryDto dtoToPatch) {
        if (repository.existsById(dtoToPatch.getId())) {
            Category category = repository.findById(dtoToPatch.getId()).get();
            category.setName(dtoToPatch.getName());
            return CategoryMapper.toDto(repository.save(category));
        } else throw new EntityNotFoundException(String.format("Category with id %s not found", dtoToPatch.getId()));
    }

    public CategoryDto post(CategoryDto dtoToPost) {
        Category newCategory = new Category();
        newCategory.setName(dtoToPost.getName());
        try {
            return CategoryMapper.toDto(repository.save(newCategory));
        } catch (DuplicateKeyException e) {
            throw new EntityAlreadyExistsException(
                    String.format("Category with name %s already exists", dtoToPost.getName())
            );
        }
    }

    public void delete(Long catId) {
        try {
            repository.deleteById(catId);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(String.format("Category with id %d does not exists!", catId));
        }

    }
}

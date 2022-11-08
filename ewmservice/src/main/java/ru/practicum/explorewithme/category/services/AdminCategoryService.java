package ru.practicum.explorewithme.category.services;

import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.category.CategoryRepository;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.category.model.CategoryDto;
import ru.practicum.explorewithme.exceptions.EntityAlreadyExistsException;
import ru.practicum.explorewithme.exceptions.EntityNotFoundException;

@Service
@AllArgsConstructor
public class AdminCategoryService {

    private final CategoryRepository repository;

    public CategoryDto patch(CategoryDto dtoToPatch) {
        if (repository.existsById(dtoToPatch.getId())) {
            Category category = repository.findById(dtoToPatch.getId()).get();
            category.setName(dtoToPatch.getName());
            return toDto(repository.save(category));
        } else throw new EntityNotFoundException(String.format("Category with id %s not found", dtoToPatch.getId()));
    }

    public CategoryDto post(CategoryDto dtoToPost) {
        Category newCategory = new Category();
        newCategory.setName(dtoToPost.getName());
        try {
            return toDto(repository.save(newCategory));
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

    private CategoryDto toDto(Category c) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(c.getId());
        categoryDto.setName(c.getName());
        return categoryDto;
    }
}

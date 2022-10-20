package category.services;

import category.CategoryMapper;
import category.CategoryRepository;
import category.model.Category;
import category.model.CategoryDto;
import exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PublicCategoryService {
    private final CategoryRepository repository;

    public List<CategoryDto> getAll(int from, int size) {
        List<Category> categories = repository.getAll(from, size);
        return categories.stream().map(CategoryMapper::toDto).collect(Collectors.toList());
    }

    public CategoryDto getById(Long catId) {
        if (repository.existsById(catId)) return CategoryMapper.toDto(repository.findById(catId).get());
        else throw new EntityNotFoundException(String.format("Category with id %s not found", catId));

    }


}

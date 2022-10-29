package explorewithme.category.services;

import explorewithme.category.CategoryMapper;
import explorewithme.category.CategoryRepository;
import explorewithme.category.model.Category;
import explorewithme.category.model.CategoryDto;
import explorewithme.exception.EntityNotFoundException;
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

package ru.practicum.explorewithme.category.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.category.CategoryRepository;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.category.model.CategoryDto;
import ru.practicum.explorewithme.exception.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PublicCategoryService {
    private final CategoryRepository repository;

    public List<CategoryDto> getAll(int from, int size) {
        List<Category> categories = repository.getAll(from, size);
        return categories.stream().map(this::toDto).collect(Collectors.toList());
    }

    public CategoryDto getById(Long catId) {
        if (repository.existsById(catId)) return toDto(repository.findById(catId).get());
        else throw new EntityNotFoundException(String.format("Category with id %s not found", catId));

    }

    private CategoryDto toDto(Category c) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(c.getId());
        categoryDto.setName(c.getName());
        return categoryDto;
    }
}

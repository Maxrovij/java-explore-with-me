package ru.practicum.explorewithme.category.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.category.model.CategoryDto;
import ru.practicum.explorewithme.category.services.PublicCategoryService;

import java.util.Collection;

@RestController
@RequestMapping("/categories")
@AllArgsConstructor
public class PublicCategoryController {
    private final PublicCategoryService service;

    @GetMapping
    public Collection<CategoryDto> getAll(@RequestParam(defaultValue = "0") int from,
                                          @RequestParam(defaultValue = "10") int size) {
        return service.getAll(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getById(@PathVariable Long catId) {
        return service.getById(catId);
    }
}

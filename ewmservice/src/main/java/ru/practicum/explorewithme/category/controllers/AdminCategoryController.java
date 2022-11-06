package ru.practicum.explorewithme.category.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.category.model.CategoryDto;
import ru.practicum.explorewithme.category.services.AdminCategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/categories")
@AllArgsConstructor
public class AdminCategoryController {

    private final AdminCategoryService service;

    @PatchMapping
    public CategoryDto patch(@Valid @RequestBody CategoryDto categoryDto) {
        return service.patch(categoryDto);
    }

    @PostMapping
    public CategoryDto post(@Valid @RequestBody CategoryDto categoryDto) {
        return service.post(categoryDto);
    }

    @DeleteMapping("/{catId}")
    public void delete(@PathVariable Long catId) {
        service.delete(catId);
    }
}

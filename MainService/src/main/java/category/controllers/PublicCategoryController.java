package category.controllers;

import category.model.CategoryDto;
import category.services.PublicCategoryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

package explorewithme.category.controllers;

import explorewithme.category.model.CategoryDto;
import explorewithme.category.services.AdminCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
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

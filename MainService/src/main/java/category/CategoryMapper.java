package category;

import category.model.Category;
import category.model.CategoryDto;

public class CategoryMapper {
    public static CategoryDto toDto(Category c) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(c.getId());
        categoryDto.setName(c.getName());
        return categoryDto;
    }
}

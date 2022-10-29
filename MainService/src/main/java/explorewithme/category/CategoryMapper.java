package explorewithme.category;

import explorewithme.category.model.Category;
import explorewithme.category.model.CategoryDto;

public class CategoryMapper {
    public static CategoryDto toDto(Category c) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(c.getId());
        categoryDto.setName(c.getName());
        return categoryDto;
    }
}

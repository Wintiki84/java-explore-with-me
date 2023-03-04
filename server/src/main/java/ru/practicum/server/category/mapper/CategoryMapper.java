package ru.practicum.server.category.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import ru.practicum.server.category.dto.CategoryDto;
import ru.practicum.server.category.model.Category;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category mapToCategory(CategoryDto сategoryDto);

    @Mapping(source = "categoryId", target = "id")
    CategoryDto mapToCategoryDto(Category category);

    List<CategoryDto> mapToListCategories(Page<Category> page);
}

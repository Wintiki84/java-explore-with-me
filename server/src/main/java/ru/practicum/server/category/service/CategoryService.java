package ru.practicum.server.category.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.server.category.dto.ListCategoryDto;
import ru.practicum.server.category.dto.CategoryDto;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto, Long catId);

    void deleteCategory(Long catId);

    ListCategoryDto getCategories(Pageable pageable);

    CategoryDto getCategoryById(Long catId);
}

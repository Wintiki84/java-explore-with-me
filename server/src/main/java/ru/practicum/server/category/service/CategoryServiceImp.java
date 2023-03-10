package ru.practicum.server.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.server.category.dto.CategoryDto;
import ru.practicum.server.category.dto.ListCategoryDto;
import ru.practicum.server.category.mapper.CategoryMapper;
import ru.practicum.server.category.model.Category;
import ru.practicum.server.category.repository.CategoryRepository;
import ru.practicum.server.handler.exception.NotFoundException;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional(readOnly = true)
public class CategoryServiceImp implements CategoryService {
    private final CategoryRepository categories;
    private final CategoryMapper mapper;

    @Override
    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto) {
        return mapper.mapToCategoryDto(categories.save(mapper.mapToCategory(categoryDto)));
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(CategoryDto updateCategory, Long categoryId) {
        findByCategoryId(categoryId);
        return mapper.mapToCategoryDto(categories.save(mapper.mapToCategory(updateCategory)));
    }

    @Override
    @Transactional
    public void deleteCategory(Long categoryId) {
        findByCategoryId(categoryId);
        categories.deleteById(categoryId);
    }

    @Override
    public ListCategoryDto getCategories(Pageable pageable) {
        return ListCategoryDto
                .builder()
                .catList(mapper.mapToListCategories(categories.findAll(pageable)))
                .build();
    }

    @Override
    public CategoryDto getCategoryById(Long categoryId) {
        return mapper.mapToCategoryDto(findByCategoryId(categoryId));
    }

    private Category findByCategoryId(Long categoryId) {
        return categories.findById(categoryId).orElseThrow(
                () -> new NotFoundException("Категория с id=" + categoryId + " не найдена"));
    }
}

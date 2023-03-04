package ru.practicum.server.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.server.category.dto.CategoryDto;
import ru.practicum.server.category.dto.ListCategoryDto;
import ru.practicum.server.category.mapper.CategoryMapper;
import ru.practicum.server.category.repository.CategoryRepository;
import ru.practicum.server.handler.exception.NotFoundException;


@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CategoryServiceImp implements CategoryService {
    private final CategoryRepository categories;
    private final CategoryMapper mapper;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        return mapper.mapToCategoryDto(categories.save(mapper.mapToCategory(categoryDto)));
    }

    @Override
    public CategoryDto updateCategory(CategoryDto updateCategory, Long catId) {
        if (categories.existsById(catId)) {
            return mapper.mapToCategoryDto(categories.save(mapper.mapToCategory(updateCategory)));
        } else {
            throw new NotFoundException("Категория с id=" + catId + " не найдена");
        }
    }

    @Override
    public void deleteCategory(Long catId) {
        if (!categories.existsById(catId)) {
            throw new NotFoundException("Категория с id=" + catId + " не найдена");
        } else {
            //TODO связь с событием
            categories.deleteById(catId);
        }
    }

    @Override
    public ListCategoryDto getCategories(Pageable pageable) {
        return ListCategoryDto
                .builder()
                .catList(mapper.mapToListCategories(categories.findAll(pageable)))
                .build();
    }

    @Override
    public CategoryDto getCategoryById(Long catId) {
        return mapper.mapToCategoryDto(categories.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория с id=" + catId + " не найдена")));
    }
}

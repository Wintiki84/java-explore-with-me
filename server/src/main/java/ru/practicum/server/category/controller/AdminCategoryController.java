package ru.practicum.server.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.category.dto.CategoryDto;
import ru.practicum.server.category.service.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/admin/categories")
@Slf4j
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody @Valid CategoryDto category) {
        log.info("создать категорию:{}", category);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(category));
    }

    @PatchMapping("{catId}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable @Min(1) Long catId,
                                                             @RequestBody @Valid CategoryDto updateCategory) {
        log.info("обнавление категории:{} with id={}",updateCategory, catId);
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.updateCategory(updateCategory, catId));
    }

    @DeleteMapping("{catId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable @Min(1) Long catId) {
        log.info("удалить категорию с id={}", catId);
        categoryService.deleteCategory(catId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

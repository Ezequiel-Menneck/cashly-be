package org.cashly.Category;

import org.cashly.Category.DTOs.UpdateCategoryDTO;
import org.cashly.Category.Exceptions.CategoryErrorCodeException;
import org.cashly.Category.Exceptions.CategoryNotFoundException;
import org.cashly.Category.Exceptions.DuplicateCategoryException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public Category createCategory(String name) {
        Optional<Category> optionalCategory = categoryRepository.findByName(name);
        if (optionalCategory.isPresent()) {
            throw new DuplicateCategoryException(name, CategoryErrorCodeException.DUPLICATED_CATEGORY.name());
        }
        Category category = categoryMapper.toEntity(name);
        categoryRepository.insert(category);
        return category;
    }

    public Category updateCategory(UpdateCategoryDTO updateCategoryDTO) {
        Optional<Category> optionalCategoryByNewName = categoryRepository.findByName(updateCategoryDTO.newName());
        if (optionalCategoryByNewName.isPresent()) {
            throw new DuplicateCategoryException(updateCategoryDTO.newName(), CategoryErrorCodeException.DUPLICATED_CATEGORY.name());
        }
        Optional<Category> optionalCategory = categoryRepository.findByName(updateCategoryDTO.oldName());
        if (optionalCategory.isEmpty()) {
            throw new CategoryNotFoundException(updateCategoryDTO.oldName(), CategoryErrorCodeException.CATEGORY_NOT_FOUND.name());
        }
        Category category = optionalCategory.get();
        category.setName(updateCategoryDTO.newName());
        categoryRepository.save(category);
        return category;
    }

    public Boolean deleteCategory(String id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty()) {
            throw new CategoryNotFoundException(id, CategoryErrorCodeException.CATEGORY_NOT_FOUND.name());
        }

        categoryRepository.delete(category.get());
        return true;
    }

    public Optional<Category> findByName(String name) {
        return categoryRepository.findByName(name);
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }
}

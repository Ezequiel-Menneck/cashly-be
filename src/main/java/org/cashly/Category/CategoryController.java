package org.cashly.Category;

import org.cashly.Category.DTOs.UpdateCategoryDTO;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @MutationMapping
    public Category createCategory(@Argument String name) {
        return categoryService.createCategory(name);
    }

    @MutationMapping
    public Category updateCategory(@Argument UpdateCategoryDTO updateCategoryDTO) {
        return categoryService.updateCategory(updateCategoryDTO);
    }

    @MutationMapping
    public Boolean deleteCategoryById(@Argument String id) {
        return categoryService.deleteCategory(id);
    }

    @QueryMapping
    public List<Category> findAll() {
        return categoryService.findAll();
    }

}

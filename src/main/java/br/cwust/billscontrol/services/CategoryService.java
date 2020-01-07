package br.cwust.billscontrol.services;

import java.util.Optional;

import br.cwust.billscontrol.dto.CategoryDto;
import br.cwust.billscontrol.model.Category;

public interface CategoryService {
	Optional<Category> findCategoryForCurrentUser(Long categoryId);
	Category createCategory(CategoryDto category);
}

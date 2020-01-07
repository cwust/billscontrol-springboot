package br.cwust.billscontrol.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.cwust.billscontrol.converters.CategoryDtoToEntityConverter;
import br.cwust.billscontrol.dto.CategoryDto;
import br.cwust.billscontrol.model.Category;
import br.cwust.billscontrol.repositories.CategoryRepository;
import br.cwust.billscontrol.security.CurrentUser;
import br.cwust.billscontrol.services.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private CurrentUser currentUser;
	
	@Autowired
	private CategoryDtoToEntityConverter categoryDtoToEntityConverter; 
	
	@Override
	public Optional<Category> findCategoryForCurrentUser(Long categoryId) {
		return categoryRepository.getByIdAndUserEmailWithAccess(categoryId, currentUser.getEmail());
	}

	@Override
	public Category createCategory(CategoryDto dto) {
		if (dto.getId() != null) {
			throw new IllegalArgumentException("CategoryService.createCategory cannot create categories that already have an id");
		}
		
		Category entity = categoryDtoToEntityConverter.convert(dto);
		entity.setUser(currentUser.getUserEntity());
		return categoryRepository.save(entity);		
	}

}

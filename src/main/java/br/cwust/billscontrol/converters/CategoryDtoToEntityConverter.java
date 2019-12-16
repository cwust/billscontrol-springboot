package br.cwust.billscontrol.converters;

import org.springframework.stereotype.Component;

import br.cwust.billscontrol.dto.CategoryDto;
import br.cwust.billscontrol.model.Category;

@Component
public class CategoryDtoToEntityConverter {
	public Category convert(CategoryDto dto) {
		Category category = new Category();
		
		category.setId(dto.getId());
		category.setName(dto.getName());
		
		return category;
	}
}

package br.cwust.billscontrol.converters;

import org.springframework.stereotype.Component;

import br.cwust.billscontrol.dto.CategoryDto;
import br.cwust.billscontrol.model.Category;

@Component
public class CategoryEntityToDtoConverter {
	public CategoryDto convert(Category entity) {
		CategoryDto dto = new CategoryDto();
		
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		
		return dto;
	}
}

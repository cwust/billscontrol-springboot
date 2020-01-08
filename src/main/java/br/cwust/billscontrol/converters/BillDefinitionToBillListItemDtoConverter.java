package br.cwust.billscontrol.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cwust.billscontrol.dto.BillListItemDto;
import br.cwust.billscontrol.model.BillDefinition;

@Component
public class BillDefinitionToBillListItemDtoConverter {
	@Autowired
	private CategoryEntityToDtoConverter categoryEntityToDtoConverter;
	
	public BillListItemDto convert(BillDefinition entity) {
		BillListItemDto dto = new BillListItemDto();
		
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setCategory(categoryEntityToDtoConverter.convert(entity.getCategory()));
		dto.setValue(entity.getDefaultValue());

		return dto;
	}	
}

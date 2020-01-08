package br.cwust.billscontrol.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cwust.billscontrol.dto.BillDetailsDto;
import br.cwust.billscontrol.model.BillDefinition;

@Component
public class BillDefinitionToBillDetailsDtoConverter {
	@Autowired
	private CategoryEntityToDtoConverter categoryEntityToDtoConverter;
	
	public BillDetailsDto convert(BillDefinition entity) {
		BillDetailsDto dto = new BillDetailsDto();
		
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setValue(entity.getDefaultValue());
		dto.setRecurrenceType(entity.getRecurrenceType().toString());
		dto.setCategory(categoryEntityToDtoConverter.convert(entity.getCategory()));

		return dto;
	}	
}

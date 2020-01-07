package br.cwust.billscontrol.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cwust.billscontrol.date.DateUtils;
import br.cwust.billscontrol.dto.BillCreateDto;
import br.cwust.billscontrol.enums.RecurrenceType;
import br.cwust.billscontrol.model.BillDefinition;

@Component
public class BillCreateDtoToBillDefinitionConverter {
	@Autowired
	private DateUtils dateUtils;
	
	public BillDefinition convert(BillCreateDto dto) {
		BillDefinition billDef = new BillDefinition();

		billDef.setName(dto.getName());
		billDef.setDefaultValue(dto.getValue());
		billDef.setStartDate(dateUtils.parseLocalDate(dto.getStartDate()));
		billDef.setEndDate(dateUtils.parseLocalDate(dto.getStartDate()));
		
		//billDef.category and user will be dealt by the service
		
		billDef.setRecurrenceType(Enum.valueOf(RecurrenceType.class, dto.getRecurrenceType()));
		
		return billDef;
	}	
}

package br.cwust.billscontrol.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.cwust.billscontrol.converters.BillCreateDtoToBillDefinitionConverter;
import br.cwust.billscontrol.dto.BillCreateDto;
import br.cwust.billscontrol.dto.CategoryDto;
import br.cwust.billscontrol.enums.RecurrenceType;
import br.cwust.billscontrol.exception.BillsControlRuntimeException;
import br.cwust.billscontrol.model.BillDefinition;
import br.cwust.billscontrol.model.Category;
import br.cwust.billscontrol.repositories.BillDefinitionRepository;
import br.cwust.billscontrol.security.CurrentUser;
import br.cwust.billscontrol.services.BillService;
import br.cwust.billscontrol.services.CategoryService;

@Service
public class BillServiceImpl implements BillService {

	@Autowired
	private BillCreateDtoToBillDefinitionConverter billCreateDtoToBillDefinitionConverter;

	@Autowired
	private CurrentUser currentUser;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private BillDefinitionRepository billDefinitionRepository;

	@Override
	public void createBill(BillCreateDto bill) {
		BillDefinition billDef = billCreateDtoToBillDefinitionConverter.convert(bill);
		billDef.setUser(currentUser.getUserEntity());
		billDef.setCategory(loadCategory(bill.getCategory()));
		
		if (billDef.getRecurrenceType() == RecurrenceType.ONCE) {
			billDef.setEndDate(billDef.getStartDate());
		}
		
		billDefinitionRepository.save(billDef);
	}

	private Category loadCategory(CategoryDto dto) {
		if (dto.getId() == null) {
			return categoryService.createCategory(dto);
		} else {
			Optional<Category> categoryOpt = categoryService.findCategoryForCurrentUser(dto.getId());
			return categoryOpt
					.orElseThrow(() -> new BillsControlRuntimeException("Could not find category with id " + dto.getId()));
		}
	}
}

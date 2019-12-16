package br.cwust.billscontrol.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.cwust.billscontrol.converters.BillCreateDtoToBillDefinitionConverter;
import br.cwust.billscontrol.dto.BillCreateDto;
import br.cwust.billscontrol.enums.RecurrenceType;
import br.cwust.billscontrol.model.BillDefinition;
import br.cwust.billscontrol.model.Category;
import br.cwust.billscontrol.repositories.BillDefinitionRepository;
import br.cwust.billscontrol.repositories.CategoryRepository;
import br.cwust.billscontrol.security.CurrentUser;
import br.cwust.billscontrol.services.BillService;

@Service
public class BillServiceImpl implements BillService {

	@Autowired
	private BillCreateDtoToBillDefinitionConverter billCreateDtoToBillDefinitionConverter;

	@Autowired
	private CurrentUser currentUser;
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private BillDefinitionRepository billDefinitionRepository;

	@Override
	public void createBill(BillCreateDto bill) {
		BillDefinition billDef = billCreateDtoToBillDefinitionConverter.convert(bill);
		billDef.setUser(currentUser.getUserEntity());
		loadCategory(billDef);
		
		if (billDef.getRecurrenceType() == RecurrenceType.ONCE) {
			billDef.setEndDate(billDef.getStartDate());
		}
		
		billDefinitionRepository.save(billDef);
	}

	private void loadCategory(BillDefinition billDef) {
		if (billDef.getCategory().getId() == null) {
			Category newCategory = billDef.getCategory();
			newCategory.setUser(billDef.getUser());
			Category savedCategory = categoryRepository.save(billDef.getCategory());
			billDef.setCategory(savedCategory);
		} else {
			Long categoryId = billDef.getCategory().getId();
			Long userId = billDef.getUser().getId();
			Optional<Category> categoryOpt = categoryRepository.findByCategoryIdAndUserId(categoryId, userId);
			if (categoryOpt.isPresent()) {
				billDef.setCategory(categoryOpt.get());
			} else {
				throw new RuntimeException("Could not find category with id " + billDef.getCategory().getId());
			}
		}
	}
}

package br.cwust.billscontrol.services.impl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.cwust.billscontrol.converters.BillCreateDtoToBillDefinitionConverter;
import br.cwust.billscontrol.converters.BillDefinitionToBillListItemDtoConverter;
import br.cwust.billscontrol.dto.BillCreateDto;
import br.cwust.billscontrol.dto.BillListItemDto;
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
	private BillDefinitionToBillListItemDtoConverter billDefinitionToBillListItemDtoConverter;
	
	@Autowired
	private CurrentUser currentUser;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private BillDefinitionRepository billDefinitionRepository;

	@Override
	public BillDefinition createBill(BillCreateDto bill) {
		BillDefinition billDef = billCreateDtoToBillDefinitionConverter.convert(bill);
		billDef.setUser(currentUser.getUserEntity());
		billDef.setCategory(loadCategory(bill.getCategory()));
		
		if (billDef.getRecurrenceType() == RecurrenceType.ONCE) {
			billDef.setEndDate(billDef.getStartDate());
		}
		
		return billDefinitionRepository.save(billDef);
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

	@Override
	public List<BillListItemDto> getBillsInMonth(int year, int month) {
		LocalDate periodStart = LocalDate.of(year, month, 1);
		LocalDate periodEnd = periodStart.plus(1, ChronoUnit.MONTHS);
		
		List<BillDefinition> billDefinitions = billDefinitionRepository.findByUserEmailAndPeriod(currentUser.getEmail(), periodStart, periodEnd);
		
		List<BillListItemDto> listItems = billDefinitions.stream()
			.map(billDefinitionToBillListItemDtoConverter::convert)
			.collect(Collectors.toList());
		
		return listItems;
	}
}

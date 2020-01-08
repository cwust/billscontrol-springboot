package br.cwust.billscontrol.services.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.cwust.billscontrol.converters.BillCreateDtoToBillDefinitionConverter;
import br.cwust.billscontrol.converters.BillDefinitionToBillDetailsDtoConverter;
import br.cwust.billscontrol.converters.BillDefinitionToBillListItemDtoConverter;
import br.cwust.billscontrol.date.DateUtils;
import br.cwust.billscontrol.date.TimePeriod;
import br.cwust.billscontrol.dto.BillCreateDto;
import br.cwust.billscontrol.dto.BillDetailsDto;
import br.cwust.billscontrol.dto.BillListItemDto;
import br.cwust.billscontrol.dto.CategoryDto;
import br.cwust.billscontrol.enums.RecurrenceType;
import br.cwust.billscontrol.exception.BillsControlRuntimeException;
import br.cwust.billscontrol.exception.MultiUserMessageException;
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
	private BillDefinitionToBillDetailsDtoConverter billDefinitionToBillDetailsDtoConverter;

	@Autowired
	private DateUtils dateUtils;

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
		TimePeriod period = TimePeriod.forMonth(year, month);
		
		List<BillDefinition> billDefinitions = billDefinitionRepository.findByUserEmailAndPeriod(currentUser.getEmail(),
				period.getStart(), period.getEnd());
		
		List<BillListItemDto> listItems = billDefinitions.stream()
			.map(billDefinitionToBillListItemDtoConverter::convert)
			.collect(Collectors.toList());
		
		return listItems;
	}

	@Override
	public BillDetailsDto getBillDetails(Long billDefId, int year, int month) {
		Optional<BillDefinition> billDefOpt = billDefinitionRepository.findByIdAndUserEmail(billDefId, currentUser.getEmail());
		
		if (!billDefOpt.isPresent()) {
			throw new MultiUserMessageException(String.format("There is no bill with id %d for user %s", billDefId, currentUser.getEmail()));
		}
		
		BillDefinition billDef = billDefOpt.get(); 
		
		TimePeriod billDefVigency = new TimePeriod(billDef.getStartDate(), billDef.getEndDate());
		TimePeriod monthPeriod = TimePeriod.forMonth(year, month);
		
		if (!billDefVigency.periodsOverlap(monthPeriod)) {
			throw new MultiUserMessageException(String.format("Bill no longer exists in month %d-%d", year, month));
		}
		
		BillDetailsDto response = billDefinitionToBillDetailsDtoConverter.convert(billDef);
		
		response.setRecurrencePeriod(getRecurrencePeriod(billDef.getRecurrenceType(), year, month));

		LocalDate defaultDueDate = getDefaultDueDate(billDef.getRecurrenceType(), billDef.getStartDate(), year, month);
		response.setDueDate(dateUtils.format(defaultDueDate));
		
		return response;
	}
	
	Integer getRecurrencePeriod(RecurrenceType recurrenceType, int year, int month) {
		switch (recurrenceType) {
		case ONCE:
			return null;
		case MONTHLY:
			return (year * 12) + month;
		default:
			throw new IllegalArgumentException("No recurrence period defined for recurrenceType " + recurrenceType);
		}
	}
	
	LocalDate getDefaultDueDate(RecurrenceType recurrenceType, LocalDate baseDate, int year, int month) {
		switch (recurrenceType) {
		case ONCE:
			return baseDate;
		case MONTHLY:
			return baseDate.withYear(year).withMonth(month);
		default:
			throw new IllegalArgumentException("No defaultDueDate defined for recurrenceType " + recurrenceType);
		}	
	}
}

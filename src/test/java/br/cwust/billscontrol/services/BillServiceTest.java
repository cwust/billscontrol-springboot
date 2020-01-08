package br.cwust.billscontrol.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.cwust.billscontrol.dto.BillCreateDto;
import br.cwust.billscontrol.dto.BillDetailsDto;
import br.cwust.billscontrol.dto.BillListItemDto;
import br.cwust.billscontrol.dto.CategoryDto;
import br.cwust.billscontrol.enums.RecurrenceType;
import br.cwust.billscontrol.exception.MultiUserMessageException;
import br.cwust.billscontrol.model.BillDefinition;
import br.cwust.billscontrol.model.Category;
import br.cwust.billscontrol.model.User;
import br.cwust.billscontrol.repositories.BillDefinitionRepository;
import br.cwust.billscontrol.security.CurrentUser;
import br.cwust.billscontrol.test.TestUtils;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class BillServiceTest {

	public static final Long USER_ID = 112233l;
	public static final String USER_EMAIL = "email@email.com";
	
	public static final String BILL_NAME = "Test Bill";
	public static final BigDecimal BILL_VALUE = new BigDecimal("100");
	public static final String BILL_START_DATE = "2019-12-16";

	@MockBean
	private CurrentUser currentUser;
	
	@MockBean
	private CategoryService categoryService;
	
	@MockBean
	private BillDefinitionRepository billDefinitionRepository;
	
	@MockBean
	@Qualifier("passwordEncoder")
	private PasswordEncoder passwordEncoder;

	@Autowired
	private BillService billService;

	@BeforeEach
	public void init() {
		User mockUser = new User();
		mockUser.setId(USER_ID);
		mockUser.setEmail(USER_EMAIL);
		given(currentUser.getUserEntity()).willReturn(mockUser);
		given(currentUser.getEmail()).willReturn(USER_EMAIL);
	}

	private BillCreateDto createMockBillCreateDto() {
		BillCreateDto dto = new BillCreateDto();

		dto.setName(BILL_NAME);
		dto.setValue(BILL_VALUE);
		dto.setStartDate(BILL_START_DATE);
		dto.setCategory(new CategoryDto());
		
		return dto;
	}

	@Test
	public void testBillRecurrenceOnceNewCategory() {
		RecurrenceType recurrence = RecurrenceType.ONCE;
		
		BillCreateDto billCreateDto = createMockBillCreateDto();
		billCreateDto.setRecurrenceType(recurrence.toString());
		
		Category categoryEntity = new Category();
		given(categoryService.createCategory(billCreateDto.getCategory())).willReturn(categoryEntity);
		TestUtils.mockRepositorySave(billDefinitionRepository);
		
		BillDefinition result = billService.createBill(billCreateDto);

		ArgumentCaptor<BillDefinition> argCaptorSaveBillDef = ArgumentCaptor.forClass(BillDefinition.class);
		verify(billDefinitionRepository).save(argCaptorSaveBillDef.capture());
		
		BillDefinition savedBillDef = argCaptorSaveBillDef.getValue();
		assertEquals(BILL_NAME, savedBillDef.getName());
		assertEquals(RecurrenceType.ONCE, savedBillDef.getRecurrenceType());
		assertEquals(LocalDate.parse(BILL_START_DATE), savedBillDef.getStartDate());
		assertEquals(LocalDate.parse(BILL_START_DATE), savedBillDef.getEndDate()); //ONCE bills start and end on the same date
		assertSame(categoryEntity, savedBillDef.getCategory());
		assertSame(savedBillDef, result);
	}	

	@Test
	public void testBillRecurrenceMonthlyExistingCategory() {
		RecurrenceType recurrence = RecurrenceType.MONTHLY;
		Long categoryId = 123l;

		BillCreateDto billCreateDto = createMockBillCreateDto();
		billCreateDto.setRecurrenceType(recurrence.toString());
		billCreateDto.getCategory().setId(categoryId);

		Category categoryEntity = new Category();
		given(categoryService.findCategoryForCurrentUser(categoryId)).willReturn(Optional.of(categoryEntity));
		TestUtils.mockRepositorySave(billDefinitionRepository);
				
		BillDefinition result = billService.createBill(billCreateDto);

		verify(categoryService, never()).createCategory(any());
		
		ArgumentCaptor<BillDefinition> argCaptorSaveBillDef = ArgumentCaptor.forClass(BillDefinition.class);
		verify(billDefinitionRepository).save(argCaptorSaveBillDef.capture());
		
		BillDefinition savedBillDef = argCaptorSaveBillDef.getValue();
		assertEquals(BILL_NAME, savedBillDef.getName());
		assertEquals(RecurrenceType.MONTHLY, savedBillDef.getRecurrenceType());
		assertEquals(LocalDate.parse(BILL_START_DATE), savedBillDef.getStartDate());
		assertNull(savedBillDef.getEndDate());
		assertSame(categoryEntity, savedBillDef.getCategory());
		assertSame(savedBillDef, result);
	}

	private BillDefinition createBillDefinition(Long id, String name, String defaultValue, Long categoryId) {
		return createBillDefinition(id, name, defaultValue, categoryId, null, null, null);
	}

	private BillDefinition createBillDefinition(Long id, String name, String defaultValue, Long categoryId,
			LocalDate startDate, LocalDate endDate, RecurrenceType recurrenceType) {
		BillDefinition billDef = new BillDefinition();
		
		billDef.setId(id);
		billDef.setName(name);
		billDef.setDefaultValue(new BigDecimal(defaultValue));
		
		Category category = new Category();
		category.setId(categoryId);
		category.setName("Category " + categoryId);
		billDef.setCategory(category);
		
		billDef.setStartDate(startDate);
		billDef.setEndDate(endDate);
		billDef.setRecurrenceType(recurrenceType);
		
		return billDef;
	}
	
	@Test
	public void testGetBillsInMonth() {
		final int year = 2020;
		final int month = 1;
		
		final LocalDate expectedPeriodStart = LocalDate.of(2020, 1, 1);
		final LocalDate expectedPeriodEnd = LocalDate.of(2020, 1, 31);
		
		final long idBill1 = 111l;
		final String nameBill1 = "Bill 1";
		final String valueBill1 = "100.0";
		
		final long idBill2 = 222l;
		final String nameBill2 = "Bill 2";
		final String valueBill2 = "200.0";
		
		final long idBill3 = 333l;
		final String nameBill3 = "Bill 3";
		final String valueBill3 = "300.0";
	
		final Long defaultCategoryId = 1010l;
		
		given(
				billDefinitionRepository.findByUserEmailAndPeriod(USER_EMAIL, expectedPeriodStart, expectedPeriodEnd))
		.willReturn(
				Arrays.asList(
						createBillDefinition(idBill1, nameBill1, valueBill1, defaultCategoryId),
						createBillDefinition(idBill2, nameBill2, valueBill2, defaultCategoryId),
						createBillDefinition(idBill3, nameBill3, valueBill3, defaultCategoryId)));
		
		List<BillListItemDto> result = billService.getBillsInMonth(year, month);
		
		assertBillListItemDto(result.get(0), 
				idBill1, 
				nameBill1, 
				valueBill1, 
				null, 
				defaultCategoryId,
				null);

		assertBillListItemDto(result.get(1), 
				idBill2, 
				nameBill2, 
				valueBill2, 
				null, 
				defaultCategoryId,
				null);

		assertBillListItemDto(result.get(2), 
				idBill3, 
				nameBill3, 
				valueBill3, 
				null, 
				defaultCategoryId,
				null);

		assertEquals(3, result.size());
	}
	
	private void assertBillListItemDto(BillListItemDto dto, Long id, String name, String value, String date, Long categoryId, Boolean isPaid) {
		assertEquals(id, dto.getId());
		assertEquals(name, dto.getName());
		assertEquals(new BigDecimal(value), dto.getValue());
		//assertEquals(date, dto.getDate());
		assertEquals(categoryId, dto.getCategory().getId());
		assertEquals(isPaid, dto.getIsPaid());
	}

	@Test
	public void testGetBillDetailsBillOnceExists() {
		final Long idBill = 313l;
		final int year = 2020;
		final int month = 1;
		
		final String nameBill = "Bill X";
		final String valueBill = "100.0";
		final Long defaultCategoryId = 3312l;
		final LocalDate dateBill = LocalDate.of(2020, 1, 8);
		
		BillDefinition billDef = createBillDefinition(idBill, nameBill, valueBill, defaultCategoryId, dateBill, dateBill, RecurrenceType.ONCE);

		given(
				billDefinitionRepository.findByIdAndUserEmail(idBill, USER_EMAIL))
		.willReturn(
				Optional.of(billDef));
		
		
		BillDetailsDto result = billService.getBillDetails(idBill, year, month);
		
		assertEquals(idBill, result.getId());
		assertEquals("2020-01-08", result.getDueDate());
		assertEquals(RecurrenceType.ONCE.toString(), result.getRecurrenceType());
		assertEquals(new BigDecimal(valueBill), result.getValue());
		assertEquals(defaultCategoryId, result.getCategory().getId());
		
		assertNull(result.getAdditionalInfo());
		assertNull(result.getPaidDate());
		assertNull(result.getPaidValue());
		assertNull(result.getRecurrencePeriod());
	}
	
	@Test
	public void testGetBillDetailsBillMonthlyExists() {
		final Long idBill = 313l;
		final int year = 2020;
		final int month = 1;
		
		final String nameBill = "Bill X";
		final String valueBill = "100.0";
		final Long defaultCategoryId = 3312l;
		final LocalDate dateStartBill = LocalDate.of(2019, 10, 8);
		
		BillDefinition billDef = createBillDefinition(idBill, nameBill, valueBill, defaultCategoryId, dateStartBill, null, RecurrenceType.MONTHLY);

		given(
				billDefinitionRepository.findByIdAndUserEmail(idBill, USER_EMAIL))
		.willReturn(
				Optional.of(billDef));
		
		
		BillDetailsDto result = billService.getBillDetails(idBill, year, month);
		
		assertEquals(idBill, result.getId());
		assertEquals("2020-01-08", result.getDueDate());
		assertEquals(RecurrenceType.MONTHLY.toString(), result.getRecurrenceType());
		assertEquals(new BigDecimal(valueBill), result.getValue());
		assertEquals(defaultCategoryId, result.getCategory().getId());
		assertEquals((year * 12) + month, result.getRecurrencePeriod());

		assertNull(result.getAdditionalInfo());
		assertNull(result.getPaidDate());
		assertNull(result.getPaidValue());
	}
	
	@Test
	public void testGetBillDetailsBillMonthlyExistsNoMore() {
		final Long idBill = 313l;
		final int year = 2020;
		final int month = 1;
		
		final String nameBill = "Bill X";
		final String valueBill = "100.0";
		final Long defaultCategoryId = 3312l;
		final LocalDate dateStartBill = LocalDate.of(2019, 10, 8);
		final LocalDate dateEndBill = LocalDate.of(2019, 12, 8);
		
		BillDefinition billDef = createBillDefinition(idBill, nameBill, valueBill, defaultCategoryId, dateStartBill, dateEndBill, RecurrenceType.MONTHLY);

		given(
				billDefinitionRepository.findByIdAndUserEmail(idBill, USER_EMAIL))
		.willReturn(
				Optional.of(billDef));
		
		assertThrows(MultiUserMessageException.class, () -> billService.getBillDetails(idBill, year, month));
	}
	
	@Test
	public void testGetBillDetailsBillNotExists() {
		final Long idBill = 313l;
		final int year = 2020;
		final int month = 1;
		
		given(
				billDefinitionRepository.findByIdAndUserEmail(idBill, USER_EMAIL))
		.willReturn(
				Optional.empty());
		
		assertThrows(MultiUserMessageException.class, () -> billService.getBillDetails(idBill, year, month));
	}
}

package br.cwust.billscontrol.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.time.LocalDate;
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

import br.cwust.billscontrol.converters.BillCreateDtoToBillDefinitionConverter;
import br.cwust.billscontrol.dto.BillCreateDto;
import br.cwust.billscontrol.dto.CategoryDto;
import br.cwust.billscontrol.enums.RecurrenceType;
import br.cwust.billscontrol.model.BillDefinition;
import br.cwust.billscontrol.model.Category;
import br.cwust.billscontrol.model.User;
import br.cwust.billscontrol.repositories.BillDefinitionRepository;
import br.cwust.billscontrol.security.CurrentUser;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class BillServiceTest {

	public static final Long USER_ID = 112233l;
	public static final String USER_EMAIL = "email@email.com";
	
	public static final String BILL_NAME = "Test Bill";
	public static final BigDecimal BILL_VALUE = new BigDecimal("100");
	public static final LocalDate BILL_START_DATE = LocalDate.parse("2019-12-16");

	@MockBean
	private CurrentUser currentUser;
	
	@MockBean
	private CategoryService categoryService;
	
	@MockBean
	private BillDefinitionRepository billDefinitionRepository;
	
	@MockBean
	private BillCreateDtoToBillDefinitionConverter billCreateDtoToBillDefinitionConverter;

	@MockBean
	@Qualifier("passwordEncoder")
	private PasswordEncoder passwordEncoder;

	@Autowired
	private BillService billService;

	private BillDefinition mockBillDef;

	private User mockUser;

	@BeforeEach
	public void init() {
		mockUser = new User();
		mockUser.setId(USER_ID);
		mockUser.setEmail(USER_EMAIL);
		given(currentUser.getUserEntity()).willReturn(mockUser);
		
		mockBillDef = new BillDefinition();
		mockBillDef.setName(BILL_NAME);
		mockBillDef.setDefaultValue(BILL_VALUE);
		mockBillDef.setStartDate(BILL_START_DATE);
	}
	
	@Test
	public void testBillRecurrenceOnceNewCategory() {
		CategoryDto categoryDto = new CategoryDto();
		
		BillCreateDto billCreateDto = new BillCreateDto();
		billCreateDto.setCategory(categoryDto);
		
		Category categoryEntity = mockCategory(categoryDto);

		mockBillDef.setRecurrenceType(RecurrenceType.ONCE);
		
		given(billCreateDtoToBillDefinitionConverter.convert(billCreateDto)).willReturn(mockBillDef);
		given(categoryService.createCategory(categoryDto)).willReturn(categoryEntity);
		
		billService.createBill(billCreateDto);

		ArgumentCaptor<BillDefinition> argCaptorSaveBillDef = ArgumentCaptor.forClass(BillDefinition.class);
		verify(billDefinitionRepository).save(argCaptorSaveBillDef.capture());
		
		BillDefinition savedBillDef = argCaptorSaveBillDef.getValue();
		assertEquals(BILL_NAME, savedBillDef.getName());
		assertEquals(RecurrenceType.ONCE, savedBillDef.getRecurrenceType());
		assertEquals(BILL_START_DATE, savedBillDef.getStartDate());
		assertEquals(BILL_START_DATE, savedBillDef.getEndDate()); //ONCE bills start and end on the same date
		assertSame(categoryEntity, savedBillDef.getCategory());
	}	

	@Test
	public void testBillRecurrenceMonthlyExistingCategory() {
		Long categoryId = 123l;

		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setId(categoryId);

		BillCreateDto billCreateDto = new BillCreateDto();
		billCreateDto.setCategory(categoryDto);

		mockBillDef.setRecurrenceType(RecurrenceType.MONTHLY);
		Category categoryEntity = mockCategory(categoryDto);

		given(billCreateDtoToBillDefinitionConverter.convert(billCreateDto)).willReturn(mockBillDef);
		given(categoryService.findCategoryForCurrentUser(categoryId)).willReturn(Optional.of(categoryEntity));
				
		billService.createBill(billCreateDto);

		verify(categoryService, never()).createCategory(any());
		
		ArgumentCaptor<BillDefinition> argCaptorSaveBillDef = ArgumentCaptor.forClass(BillDefinition.class);
		verify(billDefinitionRepository).save(argCaptorSaveBillDef.capture());
		
		BillDefinition savedBillDef = argCaptorSaveBillDef.getValue();
		assertEquals(BILL_NAME, savedBillDef.getName());
		assertEquals(RecurrenceType.MONTHLY, savedBillDef.getRecurrenceType());
		assertEquals(BILL_START_DATE, savedBillDef.getStartDate());
		assertNull(savedBillDef.getEndDate());
		assertSame(categoryEntity, savedBillDef.getCategory());
	}
	
	private Category mockCategory(CategoryDto dto) {
		Category entity = new Category();
		entity.setId(dto.getId());
		entity.setName(dto.getName());
		return entity;
	}
}

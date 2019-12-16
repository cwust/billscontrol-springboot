package br.cwust.billscontrol.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
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
import br.cwust.billscontrol.enums.RecurrenceType;
import br.cwust.billscontrol.model.BillDefinition;
import br.cwust.billscontrol.model.Category;
import br.cwust.billscontrol.model.User;
import br.cwust.billscontrol.repositories.BillDefinitionRepository;
import br.cwust.billscontrol.repositories.CategoryRepository;
import br.cwust.billscontrol.security.CurrentUser;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class BillServiceTest {

	public static final Long USER_ID = 112233l;
	public static final String BILL_NAME = "Test Bill";
	public static final BigDecimal BILL_VALUE = new BigDecimal("100");
	public static final LocalDate BILL_START_DATE = LocalDate.parse("2019-12-16");

	@MockBean
	private CurrentUser currentUser;
	
	@MockBean
	private CategoryRepository categoryRepository;
	
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
		given(currentUser.getUserEntity()).willReturn(mockUser);
		
		mockBillDef = new BillDefinition();
		mockBillDef.setName(BILL_NAME);
		mockBillDef.setDefaultValue(BILL_VALUE);
		mockBillDef.setStartDate(BILL_START_DATE);
		mockBillDef.setCategory(new Category());	
	}
	
	@Test
	public void testBillRecurrenceOnceNewCategory() {
		String categoryName = "NEW CATEGORY";

		BillCreateDto dto = new BillCreateDto();
		given(billCreateDtoToBillDefinitionConverter.convert(dto)).willReturn(mockBillDef);
		given(categoryRepository.save(any())).will(invocation -> invocation.getArgument(0));
		
		mockBillDef.setRecurrenceType(RecurrenceType.ONCE);
		mockBillDef.getCategory().setName(categoryName);
		
		billService.createBill(dto);

		ArgumentCaptor<Category> argCaptorSaveCategory = ArgumentCaptor.forClass(Category.class);
		verify(categoryRepository).save(argCaptorSaveCategory.capture());
		
		Category savedCategory = argCaptorSaveCategory.getValue();
		assertEquals(categoryName, savedCategory.getName());
		assertSame(mockUser, savedCategory.getUser());
		
		ArgumentCaptor<BillDefinition> argCaptorSaveBillDef = ArgumentCaptor.forClass(BillDefinition.class);
		verify(billDefinitionRepository).save(argCaptorSaveBillDef.capture());
		
		BillDefinition savedBillDef = argCaptorSaveBillDef.getValue();
		assertEquals(BILL_NAME, savedBillDef.getName());
		assertEquals(RecurrenceType.ONCE, savedBillDef.getRecurrenceType());
		assertEquals(BILL_START_DATE, savedBillDef.getStartDate());
		assertEquals(BILL_START_DATE, savedBillDef.getEndDate()); //ONCE bills start and end on the same date
		assertSame(savedCategory, savedBillDef.getCategory());
	}	

	@Test
	public void testBillRecurrenceMonthlyExistingCategory() {
		Long categoryId = 123l;
		String categoryName = "Test Category";

		BillCreateDto dto = new BillCreateDto();
		given(billCreateDtoToBillDefinitionConverter.convert(dto)).willReturn(mockBillDef);

		Category categoryInDb = new Category();
		categoryInDb.setId(categoryId);
		categoryInDb.setName(categoryName);
		categoryInDb.setUser(mockUser);
		given(categoryRepository.findByCategoryIdAndUserId(categoryId, USER_ID)).willReturn(Optional.of(categoryInDb));

		mockBillDef.setRecurrenceType(RecurrenceType.MONTHLY);
		mockBillDef.getCategory().setId(categoryId);
		mockBillDef.getCategory().setName("whatever");
		
		billService.createBill(dto);

		verify(categoryRepository, never()).save(any());
		
		ArgumentCaptor<BillDefinition> argCaptorSaveBillDef = ArgumentCaptor.forClass(BillDefinition.class);
		verify(billDefinitionRepository).save(argCaptorSaveBillDef.capture());
		
		BillDefinition savedBillDef = argCaptorSaveBillDef.getValue();
		assertEquals(BILL_NAME, savedBillDef.getName());
		assertEquals(RecurrenceType.MONTHLY, savedBillDef.getRecurrenceType());
		assertEquals(BILL_START_DATE, savedBillDef.getStartDate());
		assertNull(savedBillDef.getEndDate());
		assertSame(categoryInDb, savedBillDef.getCategory());
	}
}

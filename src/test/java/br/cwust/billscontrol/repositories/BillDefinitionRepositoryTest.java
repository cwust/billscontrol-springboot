package br.cwust.billscontrol.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.cwust.billscontrol.date.DateUtils;
import br.cwust.billscontrol.enums.RecurrenceType;
import br.cwust.billscontrol.enums.SupportedLanguage;
import br.cwust.billscontrol.enums.UserRole;
import br.cwust.billscontrol.model.BillDefinition;
import br.cwust.billscontrol.model.Category;
import br.cwust.billscontrol.model.User;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class BillDefinitionRepositoryTest {
	@Autowired
	private BillDefinitionRepository billDefinitionRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private DateUtils dateUtils;
	
	private User user1;

	private User user2;
	
	private Category category;

	@BeforeEach
	private void init() {
		billDefinitionRepository.deleteAll();
		userRepository.deleteAll();
		this.user1 = createUser("User 1");
		this.user2 = createUser("User 2");
		this.category = createCategory("Default Category");
	}
	
	private User createUser(String name) {
		User newUser = new User();
		newUser.setName(name);
		newUser.setEmail(name.replaceAll("\\s", "@") + "@email.com");
		newUser.setPassword("XXXX");
		newUser.setRole(UserRole.USER);
		newUser.setLanguage(SupportedLanguage.EN);
		
		return userRepository.save(newUser);
	}
	
	private Category createCategory(String categoryName) {
		Category newCategory = new Category();
		newCategory.setName(categoryName);
		return categoryRepository.save(newCategory);
	}
	
	private BillDefinition createBillDefinition(User user, String name, String defaultValue, String startDate, String endDate, RecurrenceType recurrenceType) {
		BillDefinition newBillDef = new BillDefinition();
		newBillDef.setUser(user);
		newBillDef.setName(name);
		newBillDef.setDefaultValue(new BigDecimal(defaultValue));
		newBillDef.setStartDate(dateUtils.parseLocalDate(startDate));		
		newBillDef.setEndDate(dateUtils.parseLocalDate(endDate));
		newBillDef.setRecurrenceType(recurrenceType);
		newBillDef.setCategory(this.category);
		return billDefinitionRepository.save(newBillDef);
	}
	
	@SuppressWarnings("unused")
	@Test
	public void findByUserAndPeriod() {
		LocalDate periodStart = LocalDate.parse("2020-01-01");
		LocalDate periodEnd = LocalDate.parse("2020-02-01");

		//Bill without recurrence, within period
		BillDefinition billDef1 = createBillDefinition(this.user1, "Bill 1", "100.0", "2020-01-08", "2020-01-08", RecurrenceType.ONCE);
		
		//Bill without recurrence, outside period
		BillDefinition billDef2 = createBillDefinition(this.user1, "Bill 2", "200.0", "2020-02-01", "2020-02-01", RecurrenceType.ONCE);

		//Monthly bill, started before period and without endDate (should be returned)
		BillDefinition billDef3 = createBillDefinition(this.user1, "Bill 3", "300.0", "2019-12-08", null, RecurrenceType.MONTHLY);

		//Monthly bill, started within period and without endDate (should be returned)
		BillDefinition billDef4 = createBillDefinition(this.user1, "Bill 4", "400.0", "2020-01-08", null, RecurrenceType.MONTHLY);

		//Monthly bill, started after period and without endDate (should NOT be returned)
		BillDefinition billDef5 = createBillDefinition(this.user1, "Bill 5", "500.0", "2020-02-08", null, RecurrenceType.MONTHLY);

		//Monthly bill, started and ended before period (should NOT be returned)
		BillDefinition billDef6 = createBillDefinition(this.user1, "Bill 6", "600.0", "2019-10-08", "2019-12-08", RecurrenceType.MONTHLY);

		//Monthly bill, started before period and ended within period (should be returned)
		BillDefinition billDef7 = createBillDefinition(this.user1, "Bill 7", "700.0", "2019-10-08", "2020-01-08", RecurrenceType.MONTHLY);

		//Monthly bill, started before period and ended after period (should be returned)
		BillDefinition billDef8 = createBillDefinition(this.user1, "Bill 8", "800.0", "2019-10-08", "2020-03-08", RecurrenceType.MONTHLY);

		//Monthly bill, started and ended within period (should be returned)
		BillDefinition billDef9 = createBillDefinition(this.user1, "Bill 9", "900.0", "2020-01-08", "2020-01-08", RecurrenceType.MONTHLY);

		//Monthly bill, started within period and ended after (should be returned)
		BillDefinition billDef10 = createBillDefinition(this.user1, "Bill 10", "1000.0", "2020-01-08", "2020-03-08", RecurrenceType.MONTHLY);

		//Monthly bill, started and ended after period (should NOT be returned)
		BillDefinition billDef11 = createBillDefinition(this.user1, "Bill 11", "1100.0", "2020-02-08", "2020-04-08", RecurrenceType.MONTHLY);

		//Bill without recurrence, within period, but from another user
		BillDefinition billDef12 = createBillDefinition(this.user2, "Bill 12", "1200.0", "2020-01-08", "2020-01-08", RecurrenceType.ONCE);

		//Monthly bill, started within period and without endDate, but from another user
		BillDefinition billDef13 = createBillDefinition(this.user2, "Bill 13", "1300.0", "2020-01-08", null, RecurrenceType.MONTHLY);

		//Bill without recurrence, within period (right at the start, border situation)
		BillDefinition billDef14 = createBillDefinition(this.user1, "Bill 14", "1400.0", "2020-01-01", "2020-01-01", RecurrenceType.ONCE);

		//Bill without recurrence, before period (right before the start, border situation)
		BillDefinition billDef15 = createBillDefinition(this.user1, "Bill 15", "1500.0", "2019-12-31", "2019-12-31", RecurrenceType.ONCE);

		//Bill without recurrence, within period (right before the end, border situation)
		BillDefinition billDef16 = createBillDefinition(this.user1, "Bill 16", "1600.0", "2020-01-31", "2020-01-31", RecurrenceType.ONCE);

		List<BillDefinition> result = billDefinitionRepository.findByUserEmailAndPeriod(user1.getEmail(), periodStart, periodEnd);
		
		assertEquals(billDef1.getId(), result.get(0).getId());
		assertEquals(billDef3.getId(), result.get(1).getId());
		assertEquals(billDef4.getId(), result.get(2).getId());
		assertEquals(billDef7.getId(), result.get(3).getId());
		assertEquals(billDef8.getId(), result.get(4).getId());
		assertEquals(billDef9.getId(), result.get(5).getId());
		assertEquals(billDef10.getId(), result.get(6).getId());
		assertEquals(billDef14.getId(), result.get(7).getId());
		assertEquals(billDef16.getId(), result.get(8).getId());
		assertEquals(9, result.size());
	}	
}
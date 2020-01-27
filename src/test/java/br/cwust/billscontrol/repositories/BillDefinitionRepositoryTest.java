package br.cwust.billscontrol.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.cwust.billscontrol.enums.RecurrenceType;
import br.cwust.billscontrol.model.BillDefinition;
import br.cwust.billscontrol.model.Category;
import br.cwust.billscontrol.model.User;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class BillDefinitionRepositoryTest extends AbstractRepositoryTest {
	private User user1;

	private User user2;
	
	private Category category;

	@BeforeEach
	private void init() {
		this.user1 = createUser("User 1");
		this.user2 = createUser("User 2");
		this.category = createCategory("Default Category");
	}
	
	@SuppressWarnings("unused")
	@Test
	public void testFindByUserAndPeriod() {
		LocalDate periodStart = LocalDate.parse("2020-01-01");
		LocalDate periodEnd = LocalDate.parse("2020-01-31");

		//Bill without recurrence, within period
		BillDefinition billDef1 = createBillDefinition(this.user1, "Bill 1", "100.0", "2020-01-08", "2020-01-08", RecurrenceType.ONCE, this.category);
		
		//Bill without recurrence, outside period
		BillDefinition billDef2 = createBillDefinition(this.user1, "Bill 2", "200.0", "2020-02-01", "2020-02-01", RecurrenceType.ONCE, this.category);

		//Monthly bill, started before period and without endDate (should be returned)
		BillDefinition billDef3 = createBillDefinition(this.user1, "Bill 3", "300.0", "2019-12-08", null, RecurrenceType.MONTHLY, this.category);

		//Monthly bill, started within period and without endDate (should be returned)
		BillDefinition billDef4 = createBillDefinition(this.user1, "Bill 4", "400.0", "2020-01-08", null, RecurrenceType.MONTHLY, this.category);

		//Monthly bill, started after period and without endDate (should NOT be returned)
		BillDefinition billDef5 = createBillDefinition(this.user1, "Bill 5", "500.0", "2020-02-08", null, RecurrenceType.MONTHLY, this.category);

		//Monthly bill, started and ended before period (should NOT be returned)
		BillDefinition billDef6 = createBillDefinition(this.user1, "Bill 6", "600.0", "2019-10-08", "2019-12-08", RecurrenceType.MONTHLY, this.category);

		//Monthly bill, started before period and ended within period (should be returned)
		BillDefinition billDef7 = createBillDefinition(this.user1, "Bill 7", "700.0", "2019-10-08", "2020-01-08", RecurrenceType.MONTHLY, this.category);

		//Monthly bill, started before period and ended after period (should be returned)
		BillDefinition billDef8 = createBillDefinition(this.user1, "Bill 8", "800.0", "2019-10-08", "2020-03-08", RecurrenceType.MONTHLY, this.category);

		//Monthly bill, started and ended within period (should be returned)
		BillDefinition billDef9 = createBillDefinition(this.user1, "Bill 9", "900.0", "2020-01-08", "2020-01-08", RecurrenceType.MONTHLY, this.category);

		//Monthly bill, started within period and ended after (should be returned)
		BillDefinition billDef10 = createBillDefinition(this.user1, "Bill 10", "1000.0", "2020-01-08", "2020-03-08", RecurrenceType.MONTHLY, this.category);

		//Monthly bill, started and ended after period (should NOT be returned)
		BillDefinition billDef11 = createBillDefinition(this.user1, "Bill 11", "1100.0", "2020-02-08", "2020-04-08", RecurrenceType.MONTHLY, this.category);

		//Bill without recurrence, within period, but from another user
		BillDefinition billDef12 = createBillDefinition(this.user2, "Bill 12", "1200.0", "2020-01-08", "2020-01-08", RecurrenceType.ONCE, this.category);

		//Monthly bill, started within period and without endDate, but from another user
		BillDefinition billDef13 = createBillDefinition(this.user2, "Bill 13", "1300.0", "2020-01-08", null, RecurrenceType.MONTHLY, this.category);

		//Bill without recurrence, within period (right at the start, border situation)
		BillDefinition billDef14 = createBillDefinition(this.user1, "Bill 14", "1400.0", "2020-01-01", "2020-01-01", RecurrenceType.ONCE, this.category);

		//Bill without recurrence, before period (right before the start, border situation)
		BillDefinition billDef15 = createBillDefinition(this.user1, "Bill 15", "1500.0", "2019-12-31", "2019-12-31", RecurrenceType.ONCE, this.category);

		//Bill without recurrence, within period (right before the end, border situation)
		BillDefinition billDef16 = createBillDefinition(this.user1, "Bill 16", "1600.0", "2020-01-31", "2020-01-31", RecurrenceType.ONCE, this.category);

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
	
	@Test
	public void testFindByIdAndUserEmailOk() {
		BillDefinition billDef1 = createBillDefinition(this.user1, "Bill 1", "100.0", "2020-01-08", "2020-01-08", RecurrenceType.ONCE, this.category);
		
		Optional<BillDefinition> result1 = billDefinitionRepository.findByIdAndUserEmail(billDef1.getId(), user1.getEmail());
		assertTrue(result1.isPresent());
		assertEquals(billDef1.getId(), result1.get().getId());
	}
	
	@Test
	public void testFindByIdAndUserEmailOtherUser() {
		BillDefinition billDef1 = createBillDefinition(this.user1, "Bill 1", "100.0", "2020-01-08", "2020-01-08", RecurrenceType.ONCE, this.category);
		
		Optional<BillDefinition> result1 = billDefinitionRepository.findByIdAndUserEmail(billDef1.getId(), user2.getEmail());
		assertFalse(result1.isPresent());
	}
	

}
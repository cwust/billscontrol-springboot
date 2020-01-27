package br.cwust.billscontrol.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.cwust.billscontrol.enums.RecurrenceType;
import br.cwust.billscontrol.model.BillDefinition;
import br.cwust.billscontrol.model.BillInstance;
import br.cwust.billscontrol.model.Category;
import br.cwust.billscontrol.model.User;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class BillInstanceRepositoryTest extends AbstractRepositoryTest {
	private static final String VALUE = "100.0";
	private static final String DUE_DATE = "2020-01-27";
	private User user;
	private Category category;
	
	@BeforeEach
	private void init() {
		this.user = createUser("User");
		this.category = createCategory("Default Category");
	}

	private BillDefinition createBillDefinition(RecurrenceType recurrenceType) {
		return createBillDefinition(this.user, "Bill Def", VALUE, DUE_DATE, null, recurrenceType, category);
	}
	
	@Test
	public void testFindByBillDefinitionIdAndRecurrencePeriodBillOnce() {
		BillDefinition billDefOnce = createBillDefinition(RecurrenceType.ONCE);
		
		BillInstance billInst = createBillInstance(billDefOnce, null, DUE_DATE, VALUE, "2020-01-29" , "110.0", "Paid late");
		
		Optional<BillInstance> actual = billInstanceRepository.findByBillDefinitionIdAndRecurrencePeriod(billDefOnce.getId(), null);
		
		assertTrue(actual.isPresent());
		assertEquals(billInst.getId(), actual.get().getId());
	}
	
	@Test
	public void testFindByBillDefinitionIdAndRecurrencePeriodBillMonthly() {
		BillDefinition billDefOnce = createBillDefinition(RecurrenceType.MONTHLY);
		
		int recurrencePeriod = (2020 * 12) + 1;
		BillInstance billInst = createBillInstance(billDefOnce, recurrencePeriod, DUE_DATE, VALUE, "2020-01-29" , "110.0", "Paid late");
		
		Optional<BillInstance> actual = billInstanceRepository.findByBillDefinitionIdAndRecurrencePeriod(billDefOnce.getId(), recurrencePeriod);
		
		assertTrue(actual.isPresent());
		assertEquals(billInst.getId(), actual.get().getId());
	}

}
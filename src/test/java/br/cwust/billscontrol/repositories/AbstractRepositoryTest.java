package br.cwust.billscontrol.repositories;

import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;

import br.cwust.billscontrol.date.DateUtils;
import br.cwust.billscontrol.enums.RecurrenceType;
import br.cwust.billscontrol.enums.SupportedLanguage;
import br.cwust.billscontrol.enums.UserRole;
import br.cwust.billscontrol.model.BillDefinition;
import br.cwust.billscontrol.model.BillInstance;
import br.cwust.billscontrol.model.Category;
import br.cwust.billscontrol.model.User;

public abstract class AbstractRepositoryTest {
	@Autowired
	protected BillDefinitionRepository billDefinitionRepository;

	@Autowired
	protected BillInstanceRepository billInstanceRepository;

	@Autowired
	protected UserRepository userRepository;

	@Autowired
	protected CategoryRepository categoryRepository;

	@Autowired
	protected DateUtils dateUtils;
	
	protected Category createCategory(String categoryName) {
		Category newCategory = new Category();
		newCategory.setName(categoryName);
		return categoryRepository.save(newCategory);
	}
	
	protected User createUser(String name) {
		User newUser = new User();
		newUser.setName(name);
		newUser.setEmail(name.replaceAll("\\s", "@") + "@email.com");
		newUser.setPassword("XXXX");
		newUser.setRole(UserRole.USER);
		newUser.setLanguage(SupportedLanguage.EN);
		
		return userRepository.save(newUser);
	}
	
	protected BillDefinition createBillDefinition(User user, String name, String defaultValue, String startDate, String endDate, RecurrenceType recurrenceType, Category category) {
		BillDefinition newBillDef = new BillDefinition();
		newBillDef.setUser(user);
		newBillDef.setName(name);
		newBillDef.setDefaultValue(new BigDecimal(defaultValue));
		newBillDef.setStartDate(dateUtils.parseLocalDate(startDate));		
		newBillDef.setEndDate(dateUtils.parseLocalDate(endDate));
		newBillDef.setRecurrenceType(recurrenceType);
		newBillDef.setCategory(category);
		return billDefinitionRepository.save(newBillDef);
	}

	protected BillInstance createBillInstance(BillDefinition billDef, Integer recurrencePeriod, String dueDate, String value, String paidDate, String paidValue, String additionalInfo) {
		BillInstance newBillInst = new BillInstance();
		newBillInst.setBillDefinition(billDef);
		newBillInst.setRecurrencePeriod(recurrencePeriod);
		newBillInst.setDueDate(dateUtils.parseLocalDate(dueDate));
		newBillInst.setValue(new BigDecimal(value));
		newBillInst.setPaidDate(dateUtils.parseLocalDate(paidDate));
		newBillInst.setPaidValue(new BigDecimal(paidValue));
		newBillInst.setAdditionalInfo(additionalInfo);
		
		return billInstanceRepository.save(newBillInst);
	}

	@AfterEach
	private void cleanup() {
		billInstanceRepository.deleteAll();
		billDefinitionRepository.deleteAll();
		userRepository.deleteAll();
		categoryRepository.deleteAll();
	}
	

}

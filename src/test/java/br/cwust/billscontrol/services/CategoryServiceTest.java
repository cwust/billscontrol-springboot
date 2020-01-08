package br.cwust.billscontrol.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.cwust.billscontrol.dto.CategoryDto;
import br.cwust.billscontrol.model.Category;
import br.cwust.billscontrol.model.User;
import br.cwust.billscontrol.repositories.CategoryRepository;
import br.cwust.billscontrol.security.CurrentUser;
import br.cwust.billscontrol.test.TestUtils;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class CategoryServiceTest {
	
	@Autowired
	private CategoryService categoryService;

	@MockBean
	private CategoryRepository categoryRepository;
	
	@MockBean
	private CurrentUser currentUser;

	@Test
	public void testFindCategoryForCurrentUser() {
		String userEmail = "email@email.com";
		Long categoryId = 213l;
		
		Category category = new Category();
		
		given(currentUser.getEmail()).willReturn(userEmail);
		given(categoryRepository.findByIdAndUserEmailWithAccess(categoryId, userEmail)).willReturn(Optional.of(category));
		
		Optional<Category> result = categoryService.findCategoryForCurrentUser(categoryId);
		
		assertTrue(result.isPresent());
		assertSame(category, result.get());
	}

	@Test
	public void testCreateCategory() {
		String categoryName = "Category Name";
		CategoryDto dto = new CategoryDto();
		dto.setName(categoryName);
		
		User user = new User();
		given(currentUser.getUserEntity()).willReturn(user);
		TestUtils.mockRepositorySave(categoryRepository);
		
		Category result = categoryService.createCategory(dto);
		
		ArgumentCaptor<Category> argCaptor = ArgumentCaptor.forClass(Category.class);
		verify(categoryRepository).save(argCaptor.capture());
		
		Category savedCategory = argCaptor.getValue();
		assertEquals(categoryName, savedCategory.getName());
		assertSame(savedCategory, result);
	}
}

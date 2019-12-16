package br.cwust.billscontrol.repositories;

import java.util.Optional;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import br.cwust.billscontrol.model.Category;

@NamedQueries({
	@NamedQuery(name = "CategoryRepository.findByCategoryIdAndUserId", 
			query = "SELECT category FROM Category cat WHERE cat.id = :categoryId AND cat.user.id = :userId") })
public interface CategoryRepository extends JpaRepository<Category, Long>{
	Optional<Category> findByCategoryIdAndUserId(@Param("categoryId") Long categoryId, @Param("userId") Long userId);
}

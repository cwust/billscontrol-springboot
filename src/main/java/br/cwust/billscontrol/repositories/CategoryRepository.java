package br.cwust.billscontrol.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.cwust.billscontrol.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{
	@Query(" SELECT cat "
					+ " FROM Category cat "
					+ " WHERE cat.id = :categoryId "
					+ "   AND (cat.user IS NULL OR cat.user.email = :userEmail)")
	Optional<Category> findByIdAndUserEmailWithAccess(@Param("categoryId") Long categoryId, @Param("userEmail") String userEmail);
}

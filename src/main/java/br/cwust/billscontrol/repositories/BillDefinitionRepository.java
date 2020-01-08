package br.cwust.billscontrol.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.cwust.billscontrol.model.BillDefinition;

public interface BillDefinitionRepository extends JpaRepository<BillDefinition, Long> {
	@Query(" SELECT billDef "
			+ " FROM BillDefinition billDef "
			+ " WHERE billDef.user.email = :userEmail"
			+ "   AND billDef.startDate < :periodEnd "
			+ "   AND (billDef.endDate IS NULL OR billDef.endDate >= :periodStart) "
			+ " ORDER BY "
			+ "   billDef.id ")
	List<BillDefinition> findByUserEmailAndPeriod(@Param("userEmail") String userEmail, @Param("periodStart") LocalDate periodStart,
			@Param("periodEnd") LocalDate periodEnd);	
}

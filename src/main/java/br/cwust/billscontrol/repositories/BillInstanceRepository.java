package br.cwust.billscontrol.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import br.cwust.billscontrol.model.BillInstance;

public interface BillInstanceRepository extends JpaRepository<BillInstance, Long> {
	Optional<BillInstance> findByBillDefinitionIdAndRecurrencePeriod(@Param("billDefId") Long billDefId, @Param("recurrencePeriod") Integer recurrencePeriod);
}

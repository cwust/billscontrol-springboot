package br.cwust.billscontrol.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.cwust.billscontrol.model.BillDefinition;

public interface BillDefinitionRepository extends JpaRepository<BillDefinition, Long> {

}

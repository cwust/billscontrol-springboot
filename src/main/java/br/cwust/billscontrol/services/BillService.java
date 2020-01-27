package br.cwust.billscontrol.services;

import java.util.List;

import br.cwust.billscontrol.dto.BillCreateDto;
import br.cwust.billscontrol.dto.BillDetailsDto;
import br.cwust.billscontrol.dto.BillListItemDto;
import br.cwust.billscontrol.dto.BillUpdateDto;
import br.cwust.billscontrol.model.BillDefinition;

public interface BillService {
	BillDefinition createBill(BillCreateDto bill);
	List<BillListItemDto> getBillsInMonth(int year, int month);
	BillDetailsDto getBillDetails(Long billDefId, int year, int month);
	void updateBill(BillUpdateDto dto);
}

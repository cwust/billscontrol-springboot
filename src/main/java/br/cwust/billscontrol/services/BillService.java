package br.cwust.billscontrol.services;

import br.cwust.billscontrol.dto.BillCreateDto;

public interface BillService {
	void createBill(BillCreateDto bill);
}

package br.cwust.billscontrol.exception;

public class BillsControlRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -7854597671335298913L;

	public BillsControlRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public BillsControlRuntimeException(String message) {
		super(message);
	}

}

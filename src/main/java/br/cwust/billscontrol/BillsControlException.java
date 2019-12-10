package br.cwust.billscontrol;

public abstract class BillsControlException extends Exception {

	private static final long serialVersionUID = -7854597671335298913L;

	public BillsControlException(String message, Throwable cause) {
		super(message, cause);
	}

	public BillsControlException(String message) {
		super(message);
	}

}

package br.cwust.billscontrol.exception;

import java.util.ArrayList;
import java.util.List;

public class MultiUserMessageException extends BillsControlRuntimeException {

	private static final long serialVersionUID = -7854597671335298913L;
	
	private List<String> errors = new ArrayList<>();

	public MultiUserMessageException(String message, String ...otherMessages) {
		super("MultiUserMessageException:" + message + (otherMessages.length > 0 ? " + " + otherMessages.length + " messages" : ""));
		this.getErrors().add(message);
		
		for (String msg: otherMessages) {
			getErrors().add(msg);
		}
	}

	public List<String> getErrors() {
		return errors;
	}
}

package br.cwust.billscontrol.dto;

import br.cwust.billscontrol.enums.MessageType;

public class Message {
	private String message;
	private MessageType type;

	private Message(String message, MessageType type) {
		this.message = message;
		this.type = type;
	}

	public static Message error(String message) {
		return new Message(message, MessageType.ERROR);
	}

	public static Message warn(String message) {
		return new Message(message, MessageType.WARN);
	}

	public static Message info(String message) {
		return new Message(message, MessageType.INFO);
	}

	public static Message success(String message) {
		return new Message(message, MessageType.SUCCESS);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

}

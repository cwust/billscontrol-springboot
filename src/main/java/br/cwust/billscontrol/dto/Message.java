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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message other = (Message) obj;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
}

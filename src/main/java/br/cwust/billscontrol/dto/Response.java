package br.cwust.billscontrol.dto;

import java.util.List;

public class Response<T> {
	private boolean success;
	private T data;
	private List<Message> messages;
	
	public Response(boolean success, T data) {
		super();
		this.success = success;
		this.data = data;
	}

	public static <T> Response<T> success() {
		return success((T) null);		
	}

	public static <T> Response<T> success(T data) {
		return new Response<T>(true, data);		
	}

	public static <T> Response<T> success(T data, List<Message> messages) {
		return success(data).withMessages(messages);
	}

	public static <T> Response<T> success(List<Message> messages) {
		Response<T> response = success();
		return response.withMessages(messages);
	}

	public static <T> Response<T> error(List<Message> messages) {
		return new Response<T>(false, null).withMessages(messages);
	}	

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}
	
	public Response<T> withMessages(List<Message> messages) {
		this.setMessages(messages);
		return this;
	}

}

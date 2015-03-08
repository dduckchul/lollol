package com.teampk.lollol.dto;

public class ChattingMessageDTO {
	
	private String name;
	private String message;
	private boolean left;
	
	public ChattingMessageDTO(){};
	public ChattingMessageDTO(String name, String message, boolean left){
		this.name = name;
		this.message = message;
		this.left = left;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isLeft() {
		return left;
	}
	public void setLeft(boolean left) {
		this.left = left;
	}
	public String getName() {
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ChattingMessageDTO [message=");
		builder.append(message);
		builder.append(", left=");
		builder.append(left);
		builder.append("]");
		return builder.toString();
	};
	
}

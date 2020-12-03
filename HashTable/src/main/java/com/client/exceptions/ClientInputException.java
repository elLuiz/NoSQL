package com.client.exceptions;

public class ClientInputException extends Exception{
	
	private String message;
	
	public ClientInputException(String field){
		this.setMessage("Invalid field: " + field);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}

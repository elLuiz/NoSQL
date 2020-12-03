package com.client;

public class ClientInputException extends Exception{
	
	private String message;
	
	public ClientInputException(String campo){
		this.setMessage("Invalid field: "+ campo);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}

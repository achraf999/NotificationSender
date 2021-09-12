package com.smsSender.server.dto;



public class CSVResponseMessage {

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public CSVResponseMessage(String message) {
		super();
		this.message = message;
	}

	
}

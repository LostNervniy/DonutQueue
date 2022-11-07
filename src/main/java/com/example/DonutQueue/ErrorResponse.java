package com.example.DonutQueue;

public class ErrorResponse {
	public String getError() {
		return error;
	}
	
	public void setError(String error) {
		this.error = error;
	}
	
	private String error;
	public ErrorResponse(String e){
		this.error = e;
	}
}

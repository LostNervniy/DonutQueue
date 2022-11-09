package com.example.DonutQueue;
import com.example.DonutQueue.Models.Client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Response {
	/*
	{
		status: success,
		data: [object]
	}
	
	{
		status: error
		data: []
	}
	 */
	private final String status;
	private final String message;
	private final Object data;
	//Success Constructor, has data
	public Response(String status, String message, Object data){
		this.status = status;
		this.message = message;
		this.data =  data ;
	}
	//Error Constructor, no data
	public Response(String status, String message){
		this.status = status;
		this.message = message;
		this.data = new ArrayList<>();
	}
	
	public String getStatus() {
		return status;
	}
	
	public Object getData() {
		return data;
	}
	
	public String getMessage() {
		return message;
	}
}

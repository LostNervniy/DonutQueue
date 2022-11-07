package com.example.DonutQueue;

public class Estimation {
	public int getPosition() {
		return position;
	}
	
	public void setPosition(int position) {
		this.position = position;
	}
	
	private int position;
	
	public int getApproximatedWaitTimeInSeconds() {
		return approximatedWaitTimeInSeconds;
	}
	
	public void setApproximatedWaitTimeInSeconds(int approximatedWaitTime) {
		this.approximatedWaitTimeInSeconds = approximatedWaitTime;
	}
	
	private int approximatedWaitTimeInSeconds;
	
	public Estimation(int pos, int app){
		this.position = pos;
		this.approximatedWaitTimeInSeconds = app;
	}
}

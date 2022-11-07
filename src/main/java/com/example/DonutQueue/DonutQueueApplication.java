package com.example.DonutQueue;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class DonutQueueApplication  {
	
	public static void main(String[] args) {
		new SpringApplicationBuilder( DonutQueueApplication.class )
				.run( args );
		//SpringApplication.run( DonutQueueApplication.class, args );
	}
}

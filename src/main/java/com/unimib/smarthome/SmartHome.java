package com.unimib.smarthome;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.buffer.Buffer;

public class SmartHome {
	
	public static void main(String[] args) {

		try {
			MQTTServer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Thread cli = new Thread(() -> {
			System.out.println("Starting cli interface");
	        BufferedReader reader =
	                   new BufferedReader(new InputStreamReader(System.in));
	        String input;
	        while(!Thread.interrupted()) {
	        	try {
	        		input = reader.readLine();
	        		String[] a = input.split(",");
	        		if(a.length == 2) {
	        			MQTTServer.endpoint.publish(a[0], Buffer.buffer(a[1]), MqttQoS.AT_MOST_ONCE, false, false);
	        		}
	        	} catch (IOException e) {
					e.printStackTrace();
				}
	        }   
		});
		cli.start();
		
	}
}

package com.unimib.smarthome.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.unimib.smarthome.broker.BrokerServer;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.buffer.Buffer;

public class CLIService extends Thread{

	public CLIService() {
		
	}
	
	@Override
	public void run() {
		super.run();
		
		System.out.println("Starting cli interface");
        BufferedReader reader =
                   new BufferedReader(new InputStreamReader(System.in));
        String input;
        while(!Thread.interrupted()) {
        	try {
        		input = reader.readLine();
        		String[] a = input.split(",");
        	} catch (IOException e) {
				e.printStackTrace();
			}
        }  
	}
	
}

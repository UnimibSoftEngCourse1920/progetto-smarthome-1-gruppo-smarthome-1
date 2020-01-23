package com.unimib.smarthome;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.buffer.Buffer;

public class UIThread extends Thread{

	static UIThread instance;
	
	private UIThread() {
	}
	
	public static UIThread getInstance() {
		if(instance == null) {
			instance = new UIThread();
		}
		return instance;
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
        		SECThread.getInstance().addElement(input);
        		String[] a = input.split(",");
        		if(a.length == 2) {
        			MQTTServer.endpoint.publish(a[0], Buffer.buffer(a[1]), MqttQoS.AT_MOST_ONCE, false, false);
        		}
        	} catch (IOException e) {
				e.printStackTrace();
			}
        }  
	}
	
}

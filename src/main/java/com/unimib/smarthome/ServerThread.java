package com.unimib.smarthome;

public class ServerThread extends Thread{
	
	static ServerThread instance;
	
	private ServerThread() {
	}
	
	public static ServerThread getInstance() {
		if(instance == null) {
			instance = new ServerThread();
		}
		return instance;
	}
	

	@Override
	public void run() {
		super.run();
		try {
			MQTTServer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
}

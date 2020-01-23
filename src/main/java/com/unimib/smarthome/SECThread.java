package com.unimib.smarthome;

import java.util.ArrayList;
import java.util.List;

public class SECThread extends Thread{

	static SECThread instance;
	
	private SECThread() {
	}
	
	public static SECThread getInstance() {
		if(instance == null) {
			instance = new SECThread();
		}
		return instance;
	}
	
	private List<String> l = new ArrayList<>();
	
	@Override
	public void run() {
		while(!Thread.interrupted()) {
			if(!l.isEmpty()) {
				System.out.println("[SEC] " + l.get(0));
				l.remove(0);
			}
		}
	}
	
	public void addElement(String e) {
		l.add(e);
	}
	
}

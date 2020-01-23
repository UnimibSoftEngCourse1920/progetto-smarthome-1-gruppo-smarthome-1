package com.unimib.smarthome;

public class SmartHome {
	
	public static void main(String[] args) {

		ServerThread st = ServerThread.getInstance();
		UIThread ut = UIThread.getInstance();
		SECThread set = SECThread.getInstance();
		
		st.start();
		ut.start();
		set.start();
	}
}

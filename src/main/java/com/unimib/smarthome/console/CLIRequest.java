package com.unimib.smarthome.console;
import com.unimib.smarthome.request.*;
import com.unimib.smarthome.sec.SEC;
public class CLIRequest {
	
	private final SEC sec = SEC.getInstance();
	
	//creo le richieste dell'utente.
	//caso in cui non specifica il retain e la priorita'.
	public  void createRequest(int id, String state) {
		int priority = Integer.MAX_VALUE;
		EntityStatus[] s = new EntityStatus[1];
		s[0] = new EntityStatus(id, state);
		Request r1 = new Request(null, s, false, priority);
		sec.addRequestToSECQueue(r1); 
		
		
	}
	
	public void createRequest(int id, String state, boolean retain, int priority) {
		
		EntityStatus[] s = new EntityStatus[1];
		s[0] = new EntityStatus(id, state);
		Request r2 = new Request(null, s, retain, priority);
		sec.addRequestToSECQueue(r2);
	}
}

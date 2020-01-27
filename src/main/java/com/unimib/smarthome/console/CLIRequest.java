package com.unimib.smarthome.console;
import com.unimib.smarthome.request.*;
public class CLIRequest {
	
	//creo le richieste dell'utente.
	//caso in cui non specifica il retain e la priorità.
	public  void createRequest(int id, String state) {
		int priority = Integer.MAX_VALUE;
		EntityStatus[] s = new EntityStatus[1];
		s[0] = new EntityStatus(id, state);
		Request r1 = new Request(null, s,false, priority);
		System.out.print(r1.toString());
		r1.executeRequest();
		
		
	}
	
	public void createRequest(int id, String state, boolean retain, int priority) {
		
		EntityStatus[] s = new EntityStatus[1];
		s[0] = new EntityStatus(id, state);
		Request r2 = new Request(null, s,retain, priority);
		r2.executeRequest();
	}
}

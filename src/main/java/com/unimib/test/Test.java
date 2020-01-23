package com.unimib.test;

import com.unimib.monitor.Monitor;
import com.unimib.common.*;

public class Test {

	public static void main(String[] args) {
		Monitor monitor = Monitor.getInstance();
		Light l1 = new Light();
		Light l2 = new Light();
		
		l1.flip();
		
		l1.attach(monitor);
		l2.attach(monitor);
		
		System.out.println(monitor.getMappedEntity());
		
		System.out.println(l1.getState());
		System.out.println(l2.getState());
		
		l2.detach(monitor);
		
		System.out.println(monitor.getMappedEntity());
		
		System.out.println(l1.observers);
		System.out.println(l2.observers);
		
		
		
		

	}

}

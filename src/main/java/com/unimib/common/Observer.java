package com.unimib.common;


import java.util.HashMap;




public interface Observer {
	public HashMap<Integer, Subject> subjects = new HashMap<Integer, Subject>();
	
	
	
	public default void updateAdd(Subject subject) {
		subjects.put(subject.hashCode(), subject);
		
	}
	
	
	public default void updateRemove(Subject subject) {
		subjects.remove(subject.hashCode(), subject);
	}
	

}

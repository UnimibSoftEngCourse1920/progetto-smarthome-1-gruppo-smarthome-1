package com.unimib.common;

import java.util.HashMap;


public interface Observer {
	public HashMap<Integer, Subject> mappedSubject = new HashMap<Integer, Subject>();
	
	
	public default void updateAdd(Subject subject) {
		mappedSubject.put(subject.hashCode(), subject);
	}
	
	
	public default void updateRemove(Subject subject ) {
		mappedSubject.remove(subject.hashCode(), subject);
	}
	

}

package com.unimib.common;


import java.util.HashMap;




public interface Observer {
	public HashMap<Integer, Subject> subjects = new HashMap<Integer, Subject>();
	
	
	
	public default void updateAdd(int subjectID, Subject subject) {
		subjects.put(subjectID, subject);
		
	}
	
	
	public default void updateRemove(int subjectID, Subject subject) {
		subjects.remove(subjectID, subject);
	}
	

}

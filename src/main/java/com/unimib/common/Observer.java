package com.unimib.common;

import java.util.HashMap;
import java.util.Map;

public interface Observer {
	public Map<Integer, String> subjects = new HashMap<Integer, String>();

	
	
	
	public default void update(Integer subjectId, String subjectState) {
		subjects.put(subjectId, subjectState);
	}

	

}
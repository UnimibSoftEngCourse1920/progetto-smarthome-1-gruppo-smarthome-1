package com.unimib.smarthome.entity.exceptions;

public class MissingEntityTopicException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public MissingEntityTopicException(String topic) {
		super("The topic " + topic + " is not associated with any entity");
	}


}

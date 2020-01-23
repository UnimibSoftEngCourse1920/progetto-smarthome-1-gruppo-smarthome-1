package com.unimib.test;

public class Light extends Entity {
	

	public Light() {
		super();
	}
	
	public void flip() {
		if (this.getState() == 0) {
			this.setState(1);
		}
		else {
			this.setState(0);
		}
	}
	
	
	

	
	
	
}

package com.unimib.common;

import com.unimib.test.Entity;


public interface Observer {
	public void updateAdd(Entity s);
	public void updateRemove(Entity s);
	

}

package com.unimib.test;

import com.unimib.common.Observer;

public interface Subject {
	public void attach (Observer o);
	public boolean detach (Observer o);
	public void notifyAdd(Observer o);
	public void notifyRemove(Observer o);
	public void notifyAddAll();
	public void notifyRemoveAll();
	

}

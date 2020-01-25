package com.unimib.common;

import java.util.ArrayList;
import java.util.List;


public interface Subject {

	public void attach(Observer o);
	public boolean detach(Observer o);
	public void notifyObservers();

}

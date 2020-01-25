package com.unimib.common;

import java.util.ArrayList;

public interface Subject {
	public ArrayList<Observer> observers = new ArrayList<>();

	public default void attach(Observer o) {
		observers.add(o);
		this.notifyAdd(o);
	}
	
	public default boolean detach(Observer o) {
		boolean a = observers.remove(o);
		o.updateRemove(this);
		return a;
	}

	public default void notifyAdd(Observer o) {
		o.updateAdd(this);
	}

	public default void notifyAddAll() {
		for (Observer o : observers) {
			o.updateAdd(this);
		}
	}

	public default void notifyRemove(Observer o) {
		o.updateRemove(this);
	}

	public default void notifyRemoveAll() {
		for (Observer o : observers) {
			o.updateRemove(this);
		}
	}
	

}

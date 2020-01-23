package com.unimib.test;

import java.util.ArrayList;
import com.unimib.common.Observer;

public abstract class Entity implements Subject {
	
protected int state;
protected ArrayList<Observer> observers;



public Entity() {
	this.setState(0);
	observers = new ArrayList<Observer>();
}

public int getState() {
	return this.state;
}

public void setState(int newState) {
	this.state = newState;
	this.notifyAddAll();
}
public void attach(Observer o) {
	observers.add(o);
	this.notifyAdd(o);
}
public boolean detach(Observer o) {
	boolean a = observers.remove(o);
	o.updateRemove(this);
	return a;
}

public void notifyAdd(Observer o) {
	o.updateAdd(this);
}

public void notifyAddAll() {
	for (Observer o : observers) {
		o.updateAdd(this);
	}
}

public void notifyRemove(Observer o) {
	o.updateRemove(this);
}

public void notifyRemoveAll() {
	for (Observer o : observers) {
		o.updateRemove(this);
	}
}


}


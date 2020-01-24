package com.unimib.smarthome.entity;

import com.unimib.smarthome.entity.enums.EntityType;

/**
 * Classe che rappresenta in astratto un'entita
 */

public abstract class Entity {

	private int id;
	public EntityType type;
	private String name;
	
	public Entity(EntityType type, int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	//Metodo per permettere all'entita di comunicare con l'esterno
	abstract void onIncomingMessage(String newState);
	
	public int getId() {
		return this.id;
	}
	
	public String getName() {
		return name;
	}

}

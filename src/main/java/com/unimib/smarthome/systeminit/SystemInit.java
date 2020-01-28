package com.unimib.smarthome.systeminit;

import com.unimib.smarthome.entity.EntityManager;
import com.unimib.smarthome.emac.*;
import com.unimib.smarthome.entity.Device;
import com.unimib.smarthome.entity.Sensor;
import com.unimib.smarthome.entity.enums.EntityType;
import com.unimib.smarthome.entity.exceptions.DuplicatedEntityException;
import com.unimib.smarthome.request.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SystemInit {
	private static Logger logger = LogManager.getLogger();
	
	public static void initilizer() {
		try {
			initEntities();
			initAutomatations();
		}catch(Exception e) {
			logger.printf(Level.INFO, "%s",  e.toString());
		}
	}
	
	
	
	 
	
	// leggo da un file json le varie entità che devo registrare nel sistema.
	@SuppressWarnings("unchecked")
	public static void initEntities() throws IOException, ParseException, DuplicatedEntityException {
		JSONParser parser = new JSONParser();
		EntityManager Entity = EntityManager.getInstance();
		logger.info("Inizializzazione delle entità.");

		try {
			// leggo il file.
			
			Object obj = parser.parse(new FileReader("src/main/resources/Entities.json"));

			JSONObject jsonObject = (JSONObject) obj;

			JSONArray entitiesList = (JSONArray) jsonObject.get("entities");
			
			Iterator<JSONObject> iterator = entitiesList.iterator();
			while (iterator.hasNext()) {
				JSONObject list = iterator.next();

				String name = ((String) list.get("Name"));
				String topic = ((String) list.get("Topic"));
				String type = ((String) list.get("Type"));
				int id = (int) ((long) list.get("Id"));
				//setto lo stato solo dei sensori commandable, i sensor avranno un loro stato in automatico.
				//String state = ((String) list.get("State"));
				// se è commandable, creo una classe device, altrimenti creo Sensor.
				if ((Boolean) list.get("Commandable")) {
					Entity.registerEntity(new Device(EntityType.getEntityType(type), id, name, topic));
				} else
					Entity.registerEntity(new Sensor(EntityType.getEntityType(type), id, name, topic));
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	public static void initAutomatations() throws IOException, ParseException {
		JSONParser parser = new JSONParser();
		logger.info("Inizializzazione delle automazioni.");
		try {

			Object obj = parser.parse(new FileReader("src/main/resources/Automatations.json"));

			JSONObject jsonObject = (JSONObject) obj;
			JSONArray automationsList = (JSONArray) jsonObject.get("automatations");
			
			String a;
			String value;
			Iterator<JSONObject> iterator = automationsList.iterator();
			while (iterator.hasNext()) {
				LinkedList<EntityCondition> condition = new LinkedList<EntityCondition>();
				LinkedList<EntityStatus> then =  new LinkedList<EntityStatus>();
				Emac emac = Emac.getInstance();
				JSONObject list = iterator.next();
				// L'if è un array di condizioni.
				JSONArray ifList = (JSONArray) list.get("if");
				Iterator<JSONObject> ifIterator = ifList.iterator();
				while (ifIterator.hasNext()) {
					JSONObject conditionlist = ifIterator.next();
					a = ((String) conditionlist.get("rel"));
					value = Integer.toString((int) ((long) conditionlist.get("value")));
					EntityCondition cond = new EntityCondition((int) ((long) conditionlist.get("id")), value,
							a.charAt(0));
					condition.add(cond);
					
				}
				// Anche le conseguenze, possono contenere più azioni.
				JSONArray thenList = (JSONArray) list.get("then");
				Iterator<JSONObject> thenIterator = thenList.iterator();
				while (thenIterator.hasNext()) {
					JSONObject thenlist = thenIterator.next();
					value = Integer.toString((int) ((long) thenlist.get("value")));
					EntityStatus status = new EntityStatus((int) ((long) thenlist.get("id")), value);

					then.add(status);
					
				}

				boolean retain = (boolean) list.get("retain");
				int retain_level = (int) ((long) list.get("retain_level"));
				EntityCondition[] cond = new EntityCondition[condition.size()];
				EntityStatus[] stat = new EntityStatus[then.size()];
				int i = 0;
				int j =0;
				for(EntityCondition c : condition) {
					cond[i]=c;
					i++;
				}
				for(EntityStatus s : then) {
					stat[j]=s;
					j++;
				}
				Request r = new Request(cond, stat, retain, retain_level);
				// chiamo registerAutomation in emac, per registrare l'automazione.
				//logger.printf(Level.INFO, "la richiesta e': %s", r.toString());
				emac.registerAutomation(r);

			}

		} catch (FileNotFoundException e) {
			logger.printf(Level.INFO, "%s",  e.toString());
		}
	}

}

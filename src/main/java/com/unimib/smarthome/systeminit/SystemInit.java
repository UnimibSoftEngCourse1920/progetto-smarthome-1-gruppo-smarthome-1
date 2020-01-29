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

	public static void initConfig() {
		try {
			initEntities();
			initAutomations();
		} catch (Exception e) {
			logger.error(e.toString());
		}
	}

	// leggo da un file json le varie entita' che devo registrare nel sistema.
	@SuppressWarnings("unchecked")
	public static void initEntities() throws IOException, ParseException, DuplicatedEntityException {
		JSONParser parser = new JSONParser();
		EntityManager Entity = EntityManager.getInstance();
		logger.info("Inizializzazione delle entita'.");

		try {
			// leggo il file.

			Object obj = parser.parse(new FileReader("src/main/resources/entities.json"));

			JSONObject jsonObject = (JSONObject) obj;

			JSONArray entitiesList = (JSONArray) jsonObject.get("entities");

			entitiesList.forEach((listEntities) -> {
				JSONObject list = (JSONObject) listEntities;
				String name = ((String) list.get("name"));
				String topic = ((String) list.get("topic"));
				String type = ((String) list.get("type"));
				int id = (int) ((long) list.get("id"));
				// setto lo stato solo dei sensori commandable, i sensor avranno un loro stato
				// in automatico.
				// String state = ((String) list.get("State"));

				// se è commandable, creo una classe device, altrimenti creo Sensor.
				try {
					if ((Boolean) list.get("commandable")) {
						Entity.registerEntity(new Device(EntityType.getEntityType(type), id, name, topic));
					} else
						Entity.registerEntity(new Sensor(EntityType.getEntityType(type), id, name, topic));

				} catch (Exception e) {

				}
			});

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	public static void initAutomations() throws IOException, ParseException {
		JSONParser parser = new JSONParser();
		logger.info("Inizializzazione delle automazioni.");
		try {

			Object obj = parser.parse(new FileReader("src/main/resources/automations.json"));

			JSONObject jsonObject = (JSONObject) obj;
			JSONArray automationsList = (JSONArray) jsonObject.get("automations");

			automationsList.forEach((l) -> {
				JSONObject list = (JSONObject) l;
				LinkedList<EntityCondition> condition = new LinkedList<EntityCondition>();
				LinkedList<EntityStatus> then = new LinkedList<EntityStatus>();
				EMAC emac = EMAC.getInstance();
				// L'if è un array di condizioni.
				JSONArray ifList = (JSONArray) list.get("conditions");

				ifList.forEach((condList) -> {
					String a;
					String value;
					JSONObject conditionList = (JSONObject) condList;
					a = ((String) conditionList.get("rel"));
					value = Integer.toString((int) ((long) conditionList.get("value")));
					
					EntityCondition cond = new EntityCondition((int) ((long) conditionList.get("id")), value,
							a.charAt(0));
					
					condition.add(cond);

				});
				// Anche le conseguenze, possono contenere più azioni.
				JSONArray thenList = (JSONArray) list.get("consequences");

				thenList.forEach((thenL) -> {
					String value;
					JSONObject consequences = (JSONObject) thenL;
					value = Integer.toString((int) ((long) consequences.get("value")));
					EntityStatus status = new EntityStatus((int) ((long) consequences.get("id")), value);
					then.add(status);

				});

				boolean retain = (boolean) list.get("retain");
				int retain_level = (int) ((long) list.get("priority"));
				EntityCondition[] cond = new EntityCondition[condition.size()];
				EntityStatus[] stat = new EntityStatus[then.size()];
				int i = 0;
				int j = 0;
				for (EntityCondition c : condition) {
					cond[i] = c;
					i++;
				}
				for (EntityStatus s : then) {
					stat[j] = s;
					j++;
				}
				Request r = new Request(cond, stat, retain, retain_level);
				// chiamo registerAutomation in emac, per registrare l'automazione.
				emac.registerAutomation(r);

			});

		} catch (FileNotFoundException e) {
			logger.printf(Level.INFO, "%s", e.toString());
		}
	}

}

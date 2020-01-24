package com.unimib.systeminit;
import com.unimib.smarthome.entity.EntityManager;
import com.unimib.smarthome.entity.Device;
import com.unimib.smarthome.entity.Sensor;
import com.unimib.smarthome.entity.enums.EntityType;
import com.unimib.smarthome.entity.exceptions.DuplicatedEntityException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



 
public class SystemInit {

	public static void main(String arg[]) throws IOException, ParseException, DuplicatedEntityException {
		
		InitEntities();
		InitAutomatations();
	}
	
	public static void InitEntities() throws IOException, ParseException, DuplicatedEntityException {
		JSONParser parser = new JSONParser();
		EntityManager Entity = EntityManager.getInstance();
		
		try {
			 
            Object obj = parser.parse(new FileReader("src/main/resources/Entities.json"));
 
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray entitiesList = (JSONArray) jsonObject.get("entities");
            @SuppressWarnings("unchecked")
			Iterator<JSONObject> iterator = entitiesList.iterator();
            while (iterator.hasNext()) {
	            JSONObject list = iterator.next();
	            
	            String name = ((String) list.get("Name"));
	            String topic = ((String) list.get("Topic"));
	            String type = ((String) list.get("Type"));
	            int id = (int) ((long) list.get("Id"));
	            if ((Boolean) list.get("Commandable")) {
	            	Entity.registerEntity(new Device(EntityType.getEntityType(type), id, name, topic));
	            }
	            else 
	            	Entity.registerEntity(new Sensor(EntityType.getEntityType(type), id, name, topic));
	          
            }
            
            
 
        }
		catch (FileNotFoundException e) {
            e.printStackTrace();
        }
		
	}
	public static void InitAutomatations() throws IOException, ParseException {
		JSONParser parser = new JSONParser();
		EntityManager Entity = EntityManager.getInstance();
		
		try {
			 
            Object obj = parser.parse(new FileReader("src/main/resources/Automatations.json"));
 
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray entitiesList = (JSONArray) jsonObject.get("entities");
            @SuppressWarnings("unchecked")
			Iterator<JSONObject> iterator = entitiesList.iterator();
            while (iterator.hasNext()) {
	            JSONObject list = iterator.next();
	            
	            String name = ((String) list.get("Name"));
	            String topic = ((String) list.get("Topic"));
	            String type = ((String) list.get("Type"));
	            int id = (int) ((long) list.get("Id"));
	            
	          
            }
            
            
 
        }
		catch (FileNotFoundException e) {
            e.printStackTrace();
        }
	}
	
}
	




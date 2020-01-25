package com.unimib.systeminit;
import com.unimib.smarthome.entity.EntityManager;
import com.unimib.smarthome.entity.Device;
import com.unimib.smarthome.entity.Sensor;
import com.unimib.smarthome.entity.enums.EntityType;
import com.unimib.smarthome.entity.exceptions.DuplicatedEntityException;
import com.unimib.smarthome.command.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
	@SuppressWarnings("unchecked")
	public static void InitAutomatations() throws IOException, ParseException {
		JSONParser parser = new JSONParser();
		
		try {
			 
            Object obj = parser.parse(new FileReader("src/main/resources/Automatations.json"));
 
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray automationsList = (JSONArray) jsonObject.get("automatations");
            int i;
            int j;
            String a;
            String value;
			Iterator<JSONObject> iterator = automationsList.iterator();
            while (iterator.hasNext()) {
            	EntityCondition[] condition = new EntityCondition[1000];
        		EntityStatus[] then = new EntityStatus[1000];
        		i = 0;
        		j = 0;
	            JSONObject list = iterator.next();
	            JSONArray ifList = (JSONArray) list.get("if");
	            Iterator<JSONObject> ifIterator = ifList.iterator();
	            while(ifIterator.hasNext()) {
	            	JSONObject conditionlist = ifIterator.next();
	            	System.out.println(conditionlist.toString());
	            	a = ((String) conditionlist.get("rel"));
	            	value = Integer.toString((int) ((long) conditionlist.get("value")));
	            	
	            	EntityCondition cond = new EntityCondition((int) ((long) conditionlist.get("id")), 
	            			value, a.charAt(0));
	            	condition[i] = cond;
	            	i++;
	            }
	            JSONArray thenList = (JSONArray) list.get("then");
	            Iterator<JSONObject> thenIterator = thenList.iterator();
	            while(thenIterator.hasNext()) {
	            	JSONObject thenlist = thenIterator.next();
	            	value = Integer.toString((int) ((long) thenlist.get("value")));
	            	EntityStatus status = new EntityStatus((int) ((long) thenlist.get("id")), 
	            			value);
	            	  System.out.print(value);
	            	then[j] = status;
	            	j++;
	            }
	            
	            boolean retain = (boolean) list.get("retain");
	            int retain_level = (int) ((long) list.get("retain_level"));
	            System.out.print(retain);
	          
	            Request r = new Request(condition, then, retain, retain_level);
	            //registerAutomation(new Request(condition, then, retain, retain_level));
	            
	          System.out.print(r.toString());
            }
            
            
            
            
 
        }
		catch (FileNotFoundException e) {
            e.printStackTrace();
        }
	}
	
}
	




package com.unimib.systeminit;


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

	public static void main(String arg[]) throws IOException, ParseException {
		InitEntities();
		InitAutomatation();
	}
	
	public static ArrayList<Entities> InitEntities() throws IOException, ParseException {
		JSONParser parser = new JSONParser();
		ArrayList<Entities> element = new ArrayList<Entities>();
		
		try {
			 
            Object obj = parser.parse(new FileReader("src/main/resources/Entities.json"));
 
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray entitiesList = (JSONArray) jsonObject.get("entities");
            Iterator<JSONObject> iterator = entitiesList.iterator();
            while (iterator.hasNext()) {
	            JSONObject list = iterator.next();
	            Entities ent = new Entities();
	            ent.setName((String) list.get("name"));
	            ent.setTopic((String) list.get("topic"));
	            ent.setType((String) list.get("type"));
	            ent.setID((long) list.get("ID"));
	            element.add(ent);
            }
            for(int i = 0; i < element.size(); i++) {   
                System.out.print(element.get(i).getTopic());
            }  
            
 
        }
		catch (FileNotFoundException e) {
            e.printStackTrace();
        }
		return element;
	}
	public static void InitAutomatation() {
		
	}
	
}
	




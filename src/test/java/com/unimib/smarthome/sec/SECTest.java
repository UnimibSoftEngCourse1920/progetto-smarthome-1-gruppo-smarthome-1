package com.unimib.smarthome.sec;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;

import com.unimib.smarthome.SmartHome;
import com.unimib.smarthome.entity.CommandableEntity;
import com.unimib.smarthome.entity.EntityManager;
import com.unimib.smarthome.entity.enums.EntityType;
import com.unimib.smarthome.entity.exceptions.DuplicatedEntityException;
import com.unimib.smarthome.entity.exceptions.EntityIncomingMessageException;
import com.unimib.smarthome.sec.SEC;
import com.unimib.smarthome.temp.EA;
import com.unimib.smarthome.temp.EC;
import com.unimib.smarthome.temp.Request;
import com.unimib.smarthome.entities.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SECTest {
	
	private static boolean setUpIsDone = false;
	static EntityManager em = EntityManager.getInstance();
	SEC sec = SEC.getInstance();
	
	
	@BeforeAll
	protected static void setUp() {
		if (setUpIsDone) {
	        return;
	    }
	    setUpIsDone = true;
		
		SmartHome.main(null);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initEntityManager();
	}
	
	
	
	
	public static void initEntityManager() {
		try {
			em.registerEntity(new Luce(1, "Luce1", "/luce", "0"));
		} catch (DuplicatedEntityException e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	@Order(1)
	void testEasyRequestExecution() {
		System.out.println("---------------------------------------- testEasyRequestExecution");
		Request r = new Request();
		EC ec = new EC();
		ec.id = 1;
		ec.state = "0";
		ec.relation = "=";
		
		EA ea = new EA();
		ea.id = 1;
		ea.state = "1";
		
		r.conditions = new LinkedList<EC>();
		r.consequences = new LinkedList<EA>();
		r.conditions.add(ec);
		r.consequences.add(ea);
		
		sec.evaluateRequest(r);
		
		try {
			Thread.sleep(1000);  //Aspetto che il sec setti in nuovo stato
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
 
		assertEquals(em.getEntityState(1), "1");
		
		
	}
	
	@Test
	@Order(2)
	public void testSimpleRetainedRequestExecution() throws InterruptedException {
		System.out.println("---------------------------------------- testRetainedRequestExecution");
		//Richiesta di accendere la luce retained
		Request r = new Request();
		
		EA ea = new EA();
		ea.id = 1;
		ea.state = "0";
		
		r.conditions = new LinkedList<EC>();
		r.consequences = new LinkedList<EA>();
		r.consequences.add(ea);
		r.retain = true;
		
		//Richiesta di spegnere la luce
		Request r1 = new Request();
		
		EA ea1 = new EA();
		ea1.id = 1;
		ea1.state = "1";
		
		r1.conditions = new LinkedList<EC>();
		r1.consequences = new LinkedList<EA>();
		r1.consequences.add(ea1);
		
		sec.evaluateRequest(r);
		Thread.sleep(1000);
		assertEquals(em.getEntityState(1), "0");
		
		sec.evaluateRequest(r1);
		
		assertEquals(em.getEntityState(1), "0");
		
		
	}
	
	@Test
	@Order(3)
	public void testPrioritedRetainedRequestExecution() throws InterruptedException {
		System.out.println("---------------------------------------- testRetainedRequestExecution");
		//Richiesta di accendere la luce retained
		Request r = new Request();
		
		EA ea = new EA();
		ea.id = 1;
		ea.state = "0";
		
		r.conditions = new LinkedList<EC>();
		r.consequences = new LinkedList<EA>();
		r.consequences.add(ea);
		r.retain = true;
		r.priority = 0;
		
		//Richiesta di spegnere la luce
		Request r1 = new Request();
		
		EA ea1 = new EA();
		ea1.id = 1;
		ea1.state = "1";
		
		r1.conditions = new LinkedList<EC>();
		r1.consequences = new LinkedList<EA>();
		r1.consequences.add(ea1);
		r1.priority = 2;
		
		sec.evaluateRequest(r);
		Thread.sleep(1000);
		assertEquals(em.getEntityState(1), "0");
	
		sec.evaluateRequest(r1);	
		Thread.sleep(1000);
		assertEquals(em.getEntityState(1), "1");
		
		
	}
	

}

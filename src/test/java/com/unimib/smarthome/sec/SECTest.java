package com.unimib.smarthome.sec;
import static org.junit.Assert.assertEquals;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.unimib.smarthome.SmartHome;
import com.unimib.smarthome.entity.EntityManager;
import com.unimib.smarthome.entity.exceptions.DuplicatedEntityException;
import com.unimib.smarthome.request.EntityCondition;
import com.unimib.smarthome.request.EntityStatus;
import com.unimib.smarthome.request.Request;
import com.unimib.smarthome.sec.SEC;
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
			Luce luce = new Luce(1, "Luce1", "/luce", "0");
			em.registerEntity(luce);
		} catch (DuplicatedEntityException e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	@Order(0)
	void testFailRequestExecution() {
		System.out.println("---------------------------------------- testEasyRequestExecution");
		 
		EntityCondition[] conditions = {new EntityCondition(1, "2", '=')};
		EntityStatus[] consequences = {new EntityStatus(1, "1")};
		Request r1 = new Request(conditions, consequences, true, 0);

		sec.evaluateRequest(r1);
		
		try {
			Thread.sleep(1000);  //Aspetto che il sec setti in nuovo stato
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
 
		assertEquals(em.getEntityState(1), "0");	
	}
	
	@Test
	@Order(1)
	void testEasyRequestExecution() {
		System.out.println("---------------------------------------- testEasyRequestExecution");
		 
		EntityCondition[] conditions = {new EntityCondition(1, "0", '=')};
		EntityStatus[] consequences = {new EntityStatus(1, "1")};
		Request r1 = new Request(conditions, consequences, true, 3);

		sec.evaluateRequest(r1);
		
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
		
		EntityCondition[] conditions = {};
		EntityStatus[] consequences = {new EntityStatus(1, "2")};
		Request r2 = new Request(conditions, consequences, true, 3);
		
		sec.evaluateRequest(r2);
		Thread.sleep(1000);
		
		assertEquals(em.getEntityState(1), "2");
		
	}

	@Test
	@Order(3)
	public void testPrioritedRequestExecution() throws InterruptedException {
		System.out.println("---------------------------------------- testPrioritedRetainedRequestExecution");
		
		EntityCondition[] conditions = {new EntityCondition(1, "2", '=')};
		EntityStatus[] consequences = {new EntityStatus(1, "3")};
		Request r3 = new Request(conditions, consequences, false, 10);
		
		;
		sec.evaluateRequest(r3);
		
		Thread.sleep(1000);
		assertEquals(em.getEntityState(1), "3");
		
		
	}
	
	@Test
	@Order(4)
	public void testLowPrioritedRequestExecution() throws InterruptedException {
		System.out.println("---------------------------------------- testLowPrioritedRequestExecution");
		
		EntityCondition[] conditions = {};
		EntityStatus[] consequences = {new EntityStatus(1, "2")};
		Request r4a = new Request(conditions, consequences, true, 5);
		
		sec.evaluateRequest(r4a);
		
		Thread.sleep(1000);
		assertEquals(em.getEntityState(1), "2");
		
		EntityCondition[] conditionsb = {new EntityCondition(1, "2", '=')};
		EntityStatus[] consequencesb = {new EntityStatus(1, "4")};
		Request r4b = new Request(conditionsb, consequencesb, false, 2);
		
		//sec.getConflictPool().wait();
		sec.evaluateRequest(r4b);	
		Thread.sleep(500);
		assertEquals(sec.getConflictPool().countRequestOnPool(), 1);
		assertEquals(em.getEntityState(1), "2");
		//sec.getConflictPool().notify();
	}
	
	@Test
	@Order(5)
	public void testHighPrioritedRequestExecution() throws InterruptedException {
		System.out.println("---------------------------------------- testHighPrioritedRequestExecution");
		
		EntityCondition[] conditions = {};
		EntityStatus[] consequences = {new EntityStatus(1, "2")};
		Request r5 = new Request(conditions, consequences, false, 6);
		
		assertEquals(sec.getConflictPool().countRequestOnPool(), 1);
		//sec.getConflictPool().wait();
		sec.evaluateRequest(r5);
		assertEquals(sec.getConflictPool().countRequestOnPool(), 1);
		
		Thread.sleep(1000);
		assertEquals(em.getEntityState(1), "2");
		
	}
	
	@Test
	@Order(6)
	public void testConflictPoolExecution() throws InterruptedException {
		System.out.println("---------------------------------------- testConflictPoolFailExecution");
		
		
		//sec.getConflictPool().notify();
		Thread.sleep(21000);
		assertEquals(em.getEntityState(1), "4");
		
	}

}

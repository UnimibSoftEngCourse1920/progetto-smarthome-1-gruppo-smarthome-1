package com.unimib.smarthome;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import com.unimib.smarthome.SmartHome;
import com.unimib.smarthome.console.CLIEvaluation;
import com.unimib.smarthome.emac.EMAC;
import com.unimib.smarthome.entities.Luce;
import com.unimib.smarthome.entity.Entity;
import com.unimib.smarthome.entity.EntityManager;
import com.unimib.smarthome.entity.exceptions.DuplicatedEntityException;
import com.unimib.smarthome.request.EntityCondition;
import com.unimib.smarthome.request.EntityStatus;
import com.unimib.smarthome.request.Request;
import com.unimib.smarthome.sec.SEC;

import static org.awaitility.Awaitility.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SmartHomeTest {
	
	private static boolean setUpIsDone = false;
	static EntityManager em = EntityManager.getInstance();
	SEC sec = SEC.getInstance();
	EMAC emac = EMAC.getInstance();
	public CLIEvaluation eval = new CLIEvaluation();
	private final static int ENTITY_TEST_ID = 100;
	
	@BeforeAll
	protected static void setUp() {
		if (setUpIsDone) {
	        return;
	    }
	    setUpIsDone = true;
		
		SmartHome.main(null);
		
		Luce luce = new Luce(ENTITY_TEST_ID, "Luce1", "/luce");
		try {
			em.registerEntity(luce);
		} catch (DuplicatedEntityException e) {
		}
		
		await().atMost(5, TimeUnit.SECONDS).until(allThreadsAreStarted());
	}
	
	@AfterAll
	protected static void shutDown() {
		SmartHome.shutdown();
		await().atMost(5, TimeUnit.SECONDS); 
	}
	
	private static Callable<Boolean> allThreadsAreStarted() {
	      return () -> Thread.getAllStackTraces().keySet().size() > 7;

	}

	

	
	private Callable<Boolean> entityHasState(int entityID, String state) {
	      return () -> em.getEntityState(entityID).equals(state);
	}
	
	private Callable<Boolean> cfHasRequest(int nRequest) {
	      return () -> sec.getConflictPool().countRequestOnPool() == nRequest;
	}
	
	@Test
	@Order(0)
	void initEntityManager() {
		try {
			Luce luce = new Luce(ENTITY_TEST_ID + 2, "Luce1", "/luce");
			em.registerEntity(luce);
			assertEquals(luce, em.getEntity(ENTITY_TEST_ID + 2));
		} catch (DuplicatedEntityException e) {
			fail();
		}
	}
	
	@Test
	@Order(1)
	void testFailRequestExecution() {
		System.out.println("---------------------------------------- testFailRequestExecution");
		 
		EntityCondition[] conditions = {new EntityCondition(ENTITY_TEST_ID, "2", '=')};
		EntityStatus[] consequences = {new EntityStatus(ENTITY_TEST_ID, "1")};
		Request r1 = new Request(conditions, consequences, true, 0);

		sec.addRequestToSECQueue(r1);
		
		await().atMost(2, TimeUnit.SECONDS).until(entityHasState(ENTITY_TEST_ID, "0")); 
		
	}
	
	@Test
	@Order(2)
	void testEasyRequestExecution() {
		System.out.println("---------------------------------------- testEasyRequestExecution");
		 
		EntityCondition[] conditions = {new EntityCondition(ENTITY_TEST_ID, "0", '=')};
		EntityStatus[] consequences = {new EntityStatus(ENTITY_TEST_ID, "1")};
		Request r1 = new Request(conditions, consequences, true, 3);

		sec.addRequestToSECQueue(r1);
		
		await().atMost(2, TimeUnit.SECONDS).until(entityHasState(ENTITY_TEST_ID, "1")); 
	}
	
	@Test
	@Order(2)
	public void testSimpleRetainedRequestExecution() throws InterruptedException {
		System.out.println("---------------------------------------- testSimpleRetainedRequestExecution");
		
		EntityCondition[] conditions = {};
		EntityStatus[] consequences = {new EntityStatus(ENTITY_TEST_ID, "2")};
		Request r2 = new Request(conditions, consequences, true, 3);
		
		sec.addRequestToSECQueue(r2);
		
		await().atMost(2, TimeUnit.SECONDS).until(entityHasState(ENTITY_TEST_ID, "2")); 
		
	}

	@Test
	@Order(3)
	public void testPrioritedRequestExecution() throws InterruptedException {
		System.out.println("---------------------------------------- testPrioritedRequestExecution");
		
		EntityCondition[] conditions = {new EntityCondition(ENTITY_TEST_ID, "2", '=')};
		EntityStatus[] consequences = {new EntityStatus(ENTITY_TEST_ID, "3")};
		Request r3 = new Request(conditions, consequences, false, 10);
		
		;
		sec.addRequestToSECQueue(r3);
		
		await().atMost(2, TimeUnit.SECONDS).until(entityHasState(ENTITY_TEST_ID, "3")); 
		
		
	}
	
	@Test
	@Order(4)
	public void testLowPrioritedRequestExecution() throws InterruptedException {
		System.out.println("---------------------------------------- testLowPrioritedRequestExecution");
		
		EntityCondition[] conditions = {};
		EntityStatus[] consequences = {new EntityStatus(ENTITY_TEST_ID, "2")};
		Request r4a = new Request(conditions, consequences, true, 5);
		
		sec.addRequestToSECQueue(r4a);
		
		await().atMost(1, TimeUnit.SECONDS).until(entityHasState(ENTITY_TEST_ID, "2")); 
		
		EntityCondition[] conditionsb = {new EntityCondition(ENTITY_TEST_ID, "2", '=')};
		EntityStatus[] consequencesb = {new EntityStatus(ENTITY_TEST_ID, "4")};
		Request r4b = new Request(conditionsb, consequencesb, false, 2);
		
		sec.addRequestToSECQueue(r4b);	
		await().atMost(2, TimeUnit.SECONDS).until(entityHasState(ENTITY_TEST_ID, "2")); 
		await().atMost(2, TimeUnit.SECONDS).until(cfHasRequest(1)); 
	}
	
	@Test
	@Order(5)
	public void testHighPrioritedRequestExecution() throws InterruptedException {
		System.out.println("---------------------------------------- testHighPrioritedRequestExecution");
		
		EntityCondition[] conditions = {};
		EntityStatus[] consequences = {new EntityStatus(ENTITY_TEST_ID, "2")};
		Request r5 = new Request(conditions, consequences, false, 6);
		
		
		sec.addRequestToSECQueue(r5);
		await().atMost(2, TimeUnit.SECONDS).until(cfHasRequest(1)); 
		await().atMost(2, TimeUnit.SECONDS).until(entityHasState(ENTITY_TEST_ID, "2")); 
		
	}
	
	@Test
	@Order(6)
	public void testConflictPoolExecution() throws InterruptedException {
		System.out.println("---------------------------------------- testConflictPoolExecution");
		await().atMost(11, TimeUnit.SECONDS).until(entityHasState(ENTITY_TEST_ID, "4")); 
	}
	
	@Test
	void testSet() {
		String test = "set 12 1";
		eval.evaluation(test);
		await().atMost(2, TimeUnit.SECONDS).until(entityHasState(12, "1")); 
	}
	
	@Test
	void testSet1() {
		String test = "set 12 1 false 10";
		eval.evaluation(test);
		await().atMost(2, TimeUnit.SECONDS).until(entityHasState(12, "1")); 
	}
	
	@Test
	public void emacTest() {
		String test = "set 1 20";
		eval.evaluation(test);
		//emac.controlNewStatus();
		ConcurrentLinkedQueue<Entity> SUDTest = new ConcurrentLinkedQueue<>();
		assertFalse(SUDTest.equals(emac.getStatusUpdateQueue()));	
	}

}

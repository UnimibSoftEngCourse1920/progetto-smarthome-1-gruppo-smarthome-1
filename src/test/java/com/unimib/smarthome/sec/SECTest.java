package com.unimib.smarthome.sec;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import com.unimib.smarthome.SmartHome;
import com.unimib.smarthome.entities.Luce;
import com.unimib.smarthome.entity.EntityManager;
import com.unimib.smarthome.entity.exceptions.DuplicatedEntityException;
import com.unimib.smarthome.request.EntityCondition;
import com.unimib.smarthome.request.EntityStatus;
import com.unimib.smarthome.request.Request;
import static org.awaitility.Awaitility.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SECTest {
	
	private static boolean setUpIsDone = false;
	static EntityManager em = EntityManager.getInstance();
	SEC sec = SEC.getInstance();
	
	private final static int ENTITY_TEST_ID = 100;
	
	
	@BeforeAll
	protected static void setUp() {
		if (setUpIsDone) {
	        return;
	    }
	    setUpIsDone = true;
		
		SmartHome.main(null);
		initEntityManager();
	}
	
	
	
	
	public static void initEntityManager() {
		try {
			Luce luce = new Luce(ENTITY_TEST_ID, "Luce1", "/luce", "0");
			em.registerEntity(luce);
		} catch (DuplicatedEntityException e) {
			e.printStackTrace();
		}
	}
	
	private Callable<Boolean> entityHasState(int entityID, String state) {
	      return () -> em.getEntityState(entityID).equals(state);
	}
	
	private Callable<Boolean> cfHasRequest(int nRequest) {
	      return () -> sec.getConflictPool().countRequestOnPool() == nRequest;
	}
	
	
	@Test
	@Order(0)
	void testFailRequestExecution() {
		System.out.println("---------------------------------------- testEasyRequestExecution");
		 
		EntityCondition[] conditions = {new EntityCondition(ENTITY_TEST_ID, "2", '=')};
		EntityStatus[] consequences = {new EntityStatus(ENTITY_TEST_ID, "1")};
		Request r1 = new Request(conditions, consequences, true, 0);

		sec.addRequestToSECQueue(r1);
		
		await().atMost(1, TimeUnit.SECONDS).until(entityHasState(ENTITY_TEST_ID, "0")); 
		
	}
	
	@Test
	@Order(1)
	void testEasyRequestExecution() {
		System.out.println("---------------------------------------- testEasyRequestExecution");
		 
		EntityCondition[] conditions = {new EntityCondition(ENTITY_TEST_ID, "0", '=')};
		EntityStatus[] consequences = {new EntityStatus(ENTITY_TEST_ID, "1")};
		Request r1 = new Request(conditions, consequences, true, 3);

		sec.addRequestToSECQueue(r1);
		
		await().atMost(1, TimeUnit.SECONDS).until(entityHasState(ENTITY_TEST_ID, "1")); 
	}
	
	@Test
	@Order(2)
	public void testSimpleRetainedRequestExecution() throws InterruptedException {
		System.out.println("---------------------------------------- testRetainedRequestExecution");
		
		EntityCondition[] conditions = {};
		EntityStatus[] consequences = {new EntityStatus(ENTITY_TEST_ID, "2")};
		Request r2 = new Request(conditions, consequences, true, 3);
		
		sec.addRequestToSECQueue(r2);
		
		await().atMost(1, TimeUnit.SECONDS).until(entityHasState(ENTITY_TEST_ID, "2")); 
		
	}

	@Test
	@Order(3)
	public void testPrioritedRequestExecution() throws InterruptedException {
		System.out.println("---------------------------------------- testPrioritedRetainedRequestExecution");
		
		EntityCondition[] conditions = {new EntityCondition(ENTITY_TEST_ID, "2", '=')};
		EntityStatus[] consequences = {new EntityStatus(ENTITY_TEST_ID, "3")};
		Request r3 = new Request(conditions, consequences, false, 10);
		
		;
		sec.addRequestToSECQueue(r3);
		
		await().atMost(1, TimeUnit.SECONDS).until(entityHasState(ENTITY_TEST_ID, "3")); 
		
		
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
		await().atMost(1, TimeUnit.SECONDS).until(entityHasState(ENTITY_TEST_ID, "2")); 
		await().atMost(1, TimeUnit.SECONDS).until(cfHasRequest(1)); 
	}
	
	@Test
	@Order(5)
	public void testHighPrioritedRequestExecution() throws InterruptedException {
		System.out.println("---------------------------------------- testHighPrioritedRequestExecution");
		
		EntityCondition[] conditions = {};
		EntityStatus[] consequences = {new EntityStatus(ENTITY_TEST_ID, "2")};
		Request r5 = new Request(conditions, consequences, false, 6);
		
		
		sec.addRequestToSECQueue(r5);
		await().atMost(1, TimeUnit.SECONDS).until(cfHasRequest(1)); 
		await().atMost(1, TimeUnit.SECONDS).until(entityHasState(ENTITY_TEST_ID, "2")); 
		
	}
	
	@Test
	@Order(6)
	public void testConflictPoolExecution() throws InterruptedException {
		System.out.println("---------------------------------------- testConflictPoolFailExecution");
		await().atMost(11, TimeUnit.SECONDS).until(entityHasState(ENTITY_TEST_ID, "4")); 
	}

}

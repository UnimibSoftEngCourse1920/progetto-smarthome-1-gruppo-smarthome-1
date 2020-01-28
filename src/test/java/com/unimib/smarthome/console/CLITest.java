package com.unimib.smarthome.console;

import static org.awaitility.Awaitility.await;


import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;



import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import com.unimib.smarthome.SmartHome;
import com.unimib.smarthome.entity.EntityManager;



@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CLITest {
	public CLIEvaluation eval = new CLIEvaluation();
	private static boolean setUpIsDone = false;
	
	static EntityManager em = EntityManager.getInstance();
	
	
	@BeforeAll
	protected static void setUp() {
		if (setUpIsDone) {
	        return;
	    }
	    setUpIsDone = true;
		
		SmartHome.main(null);
		
		
	}

	private Callable<Boolean> entityHasState(int entityID, String state) {
	      return () -> em.getEntityState(entityID).equals(state);
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
	void testGet() {
		String test = "get 12";
		eval.evaluation(test);
		
		await().atMost(2, TimeUnit.SECONDS); 
	}
	
	
}

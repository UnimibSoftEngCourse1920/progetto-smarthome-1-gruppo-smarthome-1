package com.unimib.smarthome.emac;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import com.unimib.smarthome.SmartHome;
import com.unimib.smarthome.console.CLIEvaluation;
import com.unimib.smarthome.entities.Luce;
import com.unimib.smarthome.entity.Entity;
import com.unimib.smarthome.entity.EntityManager;
import com.unimib.smarthome.entity.exceptions.DuplicatedEntityException;

import com.unimib.smarthome.request.Request;
import static org.awaitility.Awaitility.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmacTest {
	Emac emac = Emac.getInstance();
	private static boolean setUpIsDone = false;
	static EntityManager em = EntityManager.getInstance();
	CLIEvaluation eval = new CLIEvaluation();
	private final static int ENTITY_TEST_ID = 100;
	
	@BeforeAll
	protected static void setUp() {
		if (setUpIsDone) {
	        return;
	    }
	    setUpIsDone = true;
		
		SmartHome.main(null);
		initEntityManager();
		await().atMost(5, TimeUnit.SECONDS).until(allThreadsAreStarted());
	}
	
	public static void initEntityManager() {
		try {
			Luce luce = new Luce(ENTITY_TEST_ID, "Luce1", "/luce");
			em.registerEntity(luce);
		} catch (DuplicatedEntityException e) {
			e.printStackTrace();
		}
	}
	
	private Callable<Boolean> entityHasState(int entityID, String state) {
	      return () -> em.getEntityState(entityID).equals(state);
	}
	
	
	
	private static Callable<Boolean> allThreadsAreStarted() {
	      return () -> Thread.getAllStackTraces().keySet().size() > 7;

	}
	@Test
	public void emacTest() {
		String test = "set 1 20";
		eval.evaluation(test);
		emac.controlNewStatus();
		ConcurrentLinkedQueue<Entity> SUDTest = new ConcurrentLinkedQueue<>();
		assertFalse(SUDTest.equals(emac.getStatusUpdateQueue()));
		
		
	}
}













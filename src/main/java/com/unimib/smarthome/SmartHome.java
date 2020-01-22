package com.unimib.smarthome;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class SmartHome {
	
	public static void main(String[] args) {

		MQTTServer server = new MQTTServer();
		try {
			VertxOptions options = new VertxOptions().setClustered(false);
			Vertx vertx = Vertx.vertx(options);
			vertx.deployVerticle(server);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

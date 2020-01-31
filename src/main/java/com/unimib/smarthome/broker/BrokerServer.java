package com.unimib.smarthome.broker;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.MqttEndpoint;
import io.vertx.mqtt.MqttServer;
import io.vertx.mqtt.MqttServerOptions;

public class BrokerServer {

	private Logger logger = LogManager.getLogger();
	static final Level BROKER_LEVEL = Level.getLevel("BROKER");

	private static MqttEndpoint simulatorEndpoint = null;

	BrokerManager brokerManager = BrokerManager.getInstance();
	
	
	public void start() {

		VertxOptions voptions = new VertxOptions();
		Vertx vertx = Vertx.vertx(voptions);

		//Creo un server MQTT
		logger.log(BROKER_LEVEL, "Starting MQTT server");
		MqttServerOptions options = new MqttServerOptions().setPort(1883).setHost("0.0.0.0");
		MqttServer server = MqttServer.create(vertx, options);

		//Connessione dei client
		server.endpointHandler(end -> {

			
			//Tengo traccia di un solo client, il simulatore
			if(end != null && !end.equals(simulatorEndpoint)) {
				simulatorEndpoint = end;
	
				logger.printf(BROKER_LEVEL, "Simulator connected! [%s]", simulatorEndpoint.clientIdentifier());
		
				simulatorEndpoint.publishHandler(message -> {
					
					brokerManager.enqueueMessageToEntity(message);
	
					// specifing handlers for handling QoS 1 and 2
					simulatorEndpoint.publishAcknowledgeHandler(messageId -> {
	
					}).publishReceivedHandler(messageId -> {
	
						simulatorEndpoint.publishRelease(messageId);
	
					}).publishCompletionHandler(messageId -> {
	
					});
				});
				
				simulatorEndpoint.accept(false);
				logger.log(BROKER_LEVEL, "Getting update from all entities");
				simulatorEndpoint.publish("update", Buffer.buffer("all"), MqttQoS.AT_MOST_ONCE, false,
						false);
			}
		});

		server.listen(ar -> {
			if (ar.succeeded()) {
				logger.printf(BROKER_LEVEL, "MQTT server started and listening on port %d", server.actualPort());
			} else {
				logger.printf(BROKER_LEVEL, "MQTT server error on start %s", ar.cause().getMessage());
			}
		});
	}
	
	public MqttEndpoint getSimulatorEndpoint() {
		return simulatorEndpoint;
	}
}

package com.unimib.smarthome;

import io.vertx.core.AbstractVerticle;
import io.vertx.mqtt.MqttServer;
import io.vertx.mqtt.MqttServerOptions;

public class MQTTServer extends AbstractVerticle {
	
	 @Override
	  public void start() throws Exception {

	    MqttServerOptions options = new MqttServerOptions()
	      .setPort(1883)
	      .setHost("0.0.0.0");

	    MqttServer server = MqttServer.create(vertx, options);

	    server.endpointHandler(endpoint -> {

	      System.out.println("connected client " + endpoint.clientIdentifier());

	      endpoint.publishHandler(message -> {

	        System.out.println("Just received message on [" + message.topicName() + "] payload [" +
	          message.payload() + "] with QoS [" +
	          message.qosLevel() + "]");
	      });

	      endpoint.accept(false);
	    });

	    server.listen(ar -> {
	      if (ar.succeeded()) {
	        System.out.println("MQTT server started and listening on port " + server.actualPort());
	      } else {
	        System.err.println("MQTT server error on start" + ar.cause().getMessage());
	      }
	    });
	  }
}

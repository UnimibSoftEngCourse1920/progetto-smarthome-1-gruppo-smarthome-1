package com.unimib.smarthome;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.MqttEndpoint;
import io.vertx.mqtt.MqttServer;
import io.vertx.mqtt.MqttServerOptions;

public class MQTTServer {
	
	static MqttServer server;
	static MqttEndpoint endpoint;
	
	  public static void start() throws Exception {

	  VertxOptions voptions = new VertxOptions();
	  Vertx vertx = Vertx.vertx(voptions);
		  
	    MqttServerOptions options = new MqttServerOptions()
	      .setPort(1883)
	      .setHost("0.0.0.0");

	    server = MqttServer.create(vertx, options);

	    server.endpointHandler(end -> {
	    	
	    endpoint = end;

	      System.out.println("connected client " + endpoint.clientIdentifier());

	      endpoint.publishHandler(message -> {

	        System.out.println("Just received message on [" + message.topicName() + "] payload [" +
	          message.payload() + "] with QoS [" +
	          message.qosLevel() + "]");
	        
//	       endpoint.publish("control/temp2/value",
//	        		  Buffer.buffer("56"),
//	        		  MqttQoS.AT_MOST_ONCE,
//	        		  false,
//	        		  false);

	        		// specifing handlers for handling QoS 1 and 2
    		endpoint.publishAcknowledgeHandler(messageId -> {

    		  System.out.println("Received ack for message = " +  messageId);

    		}).publishReceivedHandler(messageId -> {

    		  endpoint.publishRelease(messageId);

    		}).publishCompletionHandler(messageId -> {

    		  System.out.println("Received ack for message = " +  messageId);
    		});
	        
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

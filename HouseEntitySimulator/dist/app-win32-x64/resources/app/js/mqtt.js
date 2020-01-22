let mqtt = require('mqtt')
let client  = mqtt.connect('mqtt://localhost:1883')
let mapTopic = new Array();

client.on('connect', function () {
    console.log("Connected to broker")
})

client.on('message', function (topic, message) {
    console.log("Recived message on topic " + topic + " " + mapTopic[topic] + " : " + message.toString())
    if(mapTopic[topic] !== undefined){
        setNewState(mapTopic[topic], message, topic)
    }
})

function mapTopicToEntity(topic, entity){
    console.log("Mapped topic " + topic + " : " + entity)
    mapTopic[topic] = entity;
}

let mqtt = require('mqtt')
let client  = mqtt.connect('mqtt://localhost:1883')
let mapTopic = new Array();

client.on('connect', function () {
    console.log("Connected to broker")
    document.querySelector("#connectionStatus").classList.remove("disconnected");
    document.querySelector("#connectionStatus").classList.add("connected");
    document.querySelector("#connectionStatus").textContent = "Connesso";
})

client.on('reconnect', function () {
    document.querySelector("#connectionStatus").classList.remove("connected");
    document.querySelector("#connectionStatus").classList.add("disconnected");
    document.querySelector("#connectionStatus").textContent = "Disconnesso";
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

var interval = setInterval(() => {
    if(!client.connected){
        client.reconnect()
    }
}, 5000);

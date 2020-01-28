/*
* range_state
* multistate
* range
* switch
* */

'use strict';
const fs = require('fs');

let entitiesState = new Array();

fs.readFile('entities.json', (err, data) => {
    if (err) throw err;
    let entitiesData = JSON.parse(data);
    drawEntities(entitiesData);
});

document.addEventListener("keydown", function (e) {
    if (e.which === 123) {
        require('electron').remote.getCurrentWindow().toggleDevTools();
    } else if (e.which === 116) {
        location.reload();
    }
});

function drawEntities(entitiesConfig){

    let entities = entitiesConfig.entities;
    let idCounter = 0;
    entities.forEach(entity => {
        let template = null;
        switch (entity.type) {
            case "range":
                template = document.querySelector("#entity_range").content;
                template.querySelector(".entity").setAttribute("entity", `${idCounter}`);
                template.querySelector(".entity-name").textContent = entity.name;
                let slider = template.querySelector(".slider");
                slider.setAttribute("min", `${entity.range_min_val}`);
                slider.setAttribute("max", `${entity.range_max_val}`);
                slider.setAttribute("entity", `${idCounter}`);
                appendEntityTemplate(template);
                entitiesState[idCounter] = entity;
                entitiesState[idCounter].value = entity.range_min_val;
                entitiesState[idCounter].id = idCounter;
                document.querySelector(`.slider[entity='${idCounter}']`).addEventListener("change", rangeChange);
                mapTopicToEntity(entitiesState[idCounter].range_topic, idCounter);
                break;
            case "switch":
                template = document.querySelector("#entity_switch").content;
                template.querySelector(".entity").setAttribute("entity", `${idCounter}`);
                template.querySelector(".entity-name").textContent = entity.name;
                template.querySelector(".custom-btn").setAttribute("entity", `${idCounter}`);
                appendEntityTemplate(template);
                entitiesState[idCounter] = entity;
                entitiesState[idCounter].state = 0;
                entitiesState[idCounter].id = idCounter;
                document.querySelector(`.custom-btn[entity='${idCounter}']`).addEventListener("click", switchButton);
                mapTopicToEntity(entitiesState[idCounter].switch_topic, idCounter);
                break;
            case "range_state":
                //entity_range_state
                template = document.querySelector("#entity_range_state").content;
                template.querySelector(".entity").setAttribute("entity", `${idCounter}`);
                template.querySelector(".entity-name").textContent = entity.name;

                //Slider
                let slider1 = template.querySelector(".slider");
                slider1.setAttribute("min", `${entity.range_min_val}`);
                slider1.setAttribute("max", `${entity.range_max_val}`);
                slider1.setAttribute("entity", `${idCounter}`);

                //Select
                let select1 = template.querySelector(".select");
                while (select1.firstChild) {
                    select1.removeChild(select1.firstChild);
                }
                var opt = null
                entity.states.forEach((option)=>{
                    opt = document.createElement("option");
                    opt.text = option.name;
                    opt.value = option.value;
                    select1.appendChild(opt);
                })
                select1.setAttribute("entity", `${idCounter}`);

                appendEntityTemplate(template);

                entitiesState[idCounter] = entity;
                entitiesState[idCounter].state = entity.states[0].value;
                entitiesState[idCounter].value = entity.range_min_val;
                entitiesState[idCounter].id = idCounter;

                document.querySelector(`.select[entity='${idCounter}']`).addEventListener("change", stateChange);
                document.querySelector(`.slider[entity='${idCounter}']`).addEventListener("change", rangeChange);
                mapTopicToEntity(entity.state_topic, idCounter);
                mapTopicToEntity(entity.range_topic, idCounter);
                break;
            case "state":
                template = document.querySelector("#entity_state").content;
                template.querySelector(".entity").setAttribute("entity", `${idCounter}`);
                template.querySelector(".entity-name").textContent = entity.name;
                let select = template.querySelector(".select");
                while (select.firstChild) {
                    select.removeChild(select.firstChild);
                }
                var opt = null
                entity.states.forEach((option)=>{
                    opt = document.createElement("option");
                    opt.text = option.name;
                    opt.value = option.value;
                    select.appendChild(opt);
                })
                select.setAttribute("entity", `${idCounter}`);
                appendEntityTemplate(template);
                entitiesState[idCounter] = entity;
                entitiesState[idCounter].state = entity.states[0].value;
                entitiesState[idCounter].id = idCounter;
                document.querySelector(`.select[entity='${idCounter}']`).addEventListener("change", stateChange);
                mapTopicToEntity(entitiesState[idCounter].state_topic, idCounter);
                break;
        }
        updateEntity(idCounter);
        idCounter++;
    })
}

function appendEntityTemplate(template){
    if(template !== null){
        document.querySelector(".entities").appendChild(document.importNode(template, true));
    }
}

function rangeChange(){
    let entityID = this.getAttribute("entity");
    entitiesState[entityID].value = this.value;
    client.publish(entitiesState[entityID].range_topic, `${entitiesState[entityID].value}`,{"qos": 2, "retain": true}, (err)=>{
        console.log(err)
    })
    updateEntity(entityID);
}

function stateChange(){
    let entityID = this.getAttribute("entity");
    entitiesState[entityID].state = this.value;
    client.publish(entitiesState[entityID].state_topic, `${entitiesState[entityID].state}`,{"qos": 2, "retain": true}, (err)=>{
        console.log(err)
    })
    updateEntity(entityID);
}

function switchButton(){
    let entityID = this.getAttribute("entity");
    let nextState = entitiesState[entityID].state === 0 ? 1 : 0;
    entitiesState[entityID].state = nextState;
    client.publish(entitiesState[entityID].switch_topic, `${nextState}`,{"qos": 2, "retain": true}, (err)=>{
        console.log(err)
    })
    updateEntity(entityID);
}

function setNewState(entityID, newState, topic=""){
    const entity = entitiesState[entityID];
    var intNewState = parseInt(newState, 10);
    if(entity !== undefined){
        switch (entity.type) {
            case "range":
                entitiesState[entityID].value = intNewState;
                break;
            case "switch":
                console.log("Setting state for entity " + entityID + " : " + intNewState);
                entitiesState[entityID].state = intNewState;
                break;
            case "range_state":
                if(topic === entity.state_topic){
                    entitiesState[entityID].state = intNewState
                }else if(topic === entity.range_topic){
                    entitiesState[entityID].value = intNewState;
                }
                break;
            case "state":
                console.log("Setting state for entity " + entityID + " : " + intNewState);
                entitiesState[entityID].state = intNewState;
                break;
        }
        updateEntity(entityID);
    }

}

function updateEntity(entityID){
    const entity = entitiesState[entityID];

    if(entity !== undefined){
        let entityElement = document.querySelector(`.entity[entity='${entityID}']`);
        if(entityElement !== undefined){
            switch (entity.type) {
                case "range":
                    entityElement.querySelector(".entity-value").textContent = entity.value;
                    entityElement.querySelector(".slider").value = entity.value;
                    break;
                case "switch":
                    entityElement.querySelector(".entity-state").textContent = entity.state === 0 ? "Disattivato" : "Attivato";
                    break;
                case "range_state":
                    entityElement.querySelector(".entity-value").textContent = entity.value;
                    entityElement.querySelector(".slider").value = entity.value;
                    entityElement.querySelector(".select").value = entity.state;
                    entityElement.querySelector(".entity-state").textContent = entityElement.querySelector(".select").options[entity.state].text;
                    break;
                case "state":
                    entityElement.querySelector(".select").value = entity.state;
                    entityElement.querySelector(".entity-value").textContent = entityElement.querySelector(".select").options[entity.state].text;
                    break;
            }
        }
    }
    //Update grafico nuovo stato;
}
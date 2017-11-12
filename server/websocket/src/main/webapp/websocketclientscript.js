/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//var wsUri = "ws://" + document.location.host + document.location.pathname + "endpoint";
var wsUri = "ws://" + document.location.host + document.location.pathname + "upnpendpoint";//If running with server app
var websocket = new WebSocket(wsUri);

//Function that outputs a message on webpage
function writeToScreen(message) {
    output.innerHTML += message + "<br>";
}

//Payload json
//{"request":{"operation":"test","deviceId":"dev","deviceState":"state"},"response":null}
//
//Set of functions to send messages to web socket endpoint
   var reqDiscover = JSON.stringify({"request":{
        "operation": "discover",
        "deviceId": "test",
        "deviceState": "1"}
    });


//Adds an event listener to button when it is clicked
var btnDisc = document.getElementById("btnDisc");
btnDisc.addEventListener("click", sendDiscoveryReq, false);

function sendDiscoveryReq(){
    console.log("--sending discovery request--");
    websocket.send(reqDiscover);
}

//Action on Error
websocket.onerror = function(evt) { onError(evt); };
function onError(evt) {
    writeToScreen('<span style="color: red;">ERROR:</span> ' + evt.data);
}

//Action on open
var output = document.getElementById("output");
websocket.onopen = function(evt) { onOpen(evt) };

function onOpen(evt) {
    writeToScreen("Connected to " + wsUri);
}

//On receiving message
websocket.onmessage = function(evt) { onMessage(evt) };
function onMessage(evt) {
    console.log("received: " + evt.data);
    writeToScreen("Message received: " + evt.data);
}

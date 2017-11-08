/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//var wsUri = "ws://" + document.location.host + document.location.pathname + "endpoint";
var wsUri = "ws://" + document.location.host + document.location.pathname + "upnpendpoint";
var websocket = new WebSocket(wsUri);

//Function that outputs a message on webpage
function writeToScreen(message) {
    output.innerHTML += message + "<br>";
}

//Adds an event listener to canvas element when it is clicked
var canvas = document.getElementById("myCanvas");
canvas.addEventListener("click", getText, false);

//Payload json
//{"request":{"operation":"test","deviceId":"dev","deviceState":"state"},"response":null}
//
//Set of functions to send messages to web socket endpoint
   var reqDiscover = JSON.stringify({"request":{
        "operation": "discover",
        "deviceId": "test",
        "deviceState": "1"},
    "response":null
    });
function getText(evt){
    var d = new Date();
    var n = d.getTime();
    console.log("Inside get text");
    sendText(reqDiscover);
}
function sendText(json) {
    console.log("sending text: " + json);
    websocket.send(json);
}


//Adds an event listener to button when it is clicked
var btnDisc = document.getElementById("btnDisc");
btnDisc.addEventListener("click", sendDiscoveryReq, false);

//  var discvryReq = JSON.stringify({
//        "operation": "discover",
//        "deviceId": "",
//        "deviceState": ""
//    });

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
//    drawImageText(evt.data);
}
// End test functions

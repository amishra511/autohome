/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.List;
import javax.json.JsonObject;
import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import socket.communicate.Request;
import socket.upnp.Controller;
import socket.upnp.Device;

/**
 *
 * @author Ashish
 */

@ClientEndpoint
public class StndAloneClient {

    private static final Object WAIT_LOCK = new Object();
//       private static final String ENDPOINT_URL = "ws://homeau2ma10n-env.pke2qi6wrq.us-east-1.elasticbeanstalk.com/upnpendpoint";
    private static final String ENDPOINT_URL = "ws://localhost:8080/websocket/upnpendpoint";

    Session userSession = null;

//    @OnOpen
//    public void onOpen(Session userSession) {
//        System.out.println("Opening websocket");
//        this.userSession = userSession;
//    }

    @OnMessage
    public void onMessage(String param, Session session) {
        try {
            System.out.println("Recieved message at java client");
            System.out.println(param);
            ObjectMapper mapper = new ObjectMapper();
            Request req = mapper.readValue(param, Request.class);
            req.setDeviceId("Weemo");
            String outJson = mapper.writeValueAsString(req);
            
            System.out.println("---Requested operation name---");
            System.out.println(req.getOperation());
            if (null != req.getOperation()) {
                if ("discover".equals(req.getOperation())) {
                    List<Device> deviceDetails = Controller.discoverUpnpDevices();
                    System.out.println("---Device discovery results---");
                    for(Device dev:deviceDetails){
                        System.out.println(dev.toString());
                    }
                    if(null != deviceDetails){
                        session.getBasicRemote().sendText(outJson);
                    }
                    else{
                        System.out.println("-----Data is null-----------");
                    }
                }
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }

//        JsonObject jsonObject = new JsonObject(param);
    }

    private static void wait4TerminateSignal() {

        synchronized (WAIT_LOCK) {
            try {
                WAIT_LOCK.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        WebSocketContainer container = null;//
        Session session = null;
        try {
            container = ContainerProvider.getWebSocketContainer();
            session = container.connectToServer(StndAloneClient.class, URI.create(ENDPOINT_URL));
            wait4TerminateSignal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

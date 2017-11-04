/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upnp.socket.test.encoderdecoder;

import java.net.URI;
import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import upnp.socket.communicate.RequestMessage;
import upnp.socket.test.StanaloneClient;

/**
 *
 * @author Ashish
 */
@ClientEndpoint
public class TestStandAloneWthCompMesge {
       private static final Object waitLock = new Object();

    @OnMessage
    public void onMessage(RequestMessage param) {
        System.out.println("Recieved message at java client");
        System.out.println(param.toString());
    }

    private static void wait4TerminateSignal() {

        synchronized (waitLock) {
            try {
                waitLock.wait();
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
            session = container.connectToServer(StanaloneClient.class, URI.create("ws://localhost:8080/websocket/upnpendpoint"));
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

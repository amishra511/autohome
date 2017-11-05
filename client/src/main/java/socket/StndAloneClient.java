/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socket;

import java.net.URI;
import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
/**
 *
 * @author Ashish
 */

@ClientEndpoint
public class StndAloneClient {
          private static final Object WAIT_LOCK = new Object();
       private static final String ENDPOINT_URL = "ws://homeau2ma10n-env.pke2qi6wrq.us-east-1.elasticbeanstalk.com/upnpendpoint";

    @OnMessage
    public void onMessage(String param) {
        System.out.println("Recieved message at java client");
        System.out.println(param);
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

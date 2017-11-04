/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upnp.socket.test.encoderdecoder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.websocket.ClientEndpointConfig;
import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import upnp.socket.communicate.translate.MessageDecoder;
import upnp.socket.communicate.translate.MessageEncoder;

/**
 *
 * @author Ashish
 */
public class TestStndAloneEndPntMain {
       private static Object waitLock = new Object();
    private static final String soapEndPointUrl = "http://10.0.0.191:49153/upnp/control/basicevent1";
    private static final String soapActionSetState = "\"urn:Belkin:service:basicevent:1#SetBinaryState\"";
    private static final String DISCOVER = "discover";
    private static final String SET_STATE = "setState";

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

            //Tyrus is plugged via ServiceLoader API. See notes above
            container = ContainerProvider.getWebSocketContainer();
            List decoders = new ArrayList();
            decoders.add(MessageDecoder.class);
            List encoders = new ArrayList();
            encoders.add(MessageEncoder.class);

            ClientEndpointConfig cec = ClientEndpointConfig.Builder.create().decoders(decoders).encoders(encoders).build();
            container.connectToServer(TestStdAloneClientEndPoint.class, cec, URI.create("ws://localhost:8080/websocket/upnpendpoint"));
            
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

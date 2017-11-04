/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upnp.socket.test.encoderdecoder;

import java.io.IOException;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import upnp.socket.communicate.RequestMessage;

/**
 *
 * @author Ashish
 */
public class TestStdAloneClientEndPoint extends Endpoint{
       
    @Override
    public void onOpen(final Session session, EndpointConfig ec) {
        final RemoteEndpoint remote = session.getBasicRemote();
        session.addMessageHandler(new MessageHandler.Whole<RequestMessage>() {
            
            @Override
            public void onMessage(RequestMessage param) {
                try{
                System.out.println("Received response in client from endpoint: " + param.toString());
                //remote.("Got your message (" + param.toString() + "). Thanks !");
                }
                catch(Exception  io){
                }
            }
        });
    }
}

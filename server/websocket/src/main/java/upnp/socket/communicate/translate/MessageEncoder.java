/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upnp.socket.communicate.translate;

/**
 *
 * @author Ashish
 */

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import upnp.socket.communicate.RequestMessage;

/**
 *
 * @author Ashish
 */
public class MessageEncoder implements Encoder.Text<RequestMessage> {

    @Override
    public String encode(RequestMessage reqMsg) throws EncodeException {
        return reqMsg.getJson().toString();
    }

    @Override
    public void init(EndpointConfig config) {
        System.out.println("Inside Init");
    }

    @Override
    public void destroy() {
        System.out.println("Inside destroy");
    }

    public MessageEncoder() {
    }
    
}


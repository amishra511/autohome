/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socket;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import socket.communicate.RequestMessage;
import socket.communicate.translate.MessageDecoder;
import socket.communicate.translate.MessageEncoder;

/**
 *
 * @author Ashish
 */
@ServerEndpoint(value = "/upnpendpoint", encoders = {MessageEncoder.class}, decoders = {MessageDecoder.class})
public class WSEndpoint {

    private static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());

    @OnMessage
    public void onMessage(RequestMessage message, Session session) throws IOException, EncodeException {
        System.out.println("--Message--" + message);
        for (Session peer : peers) {
            if (!peer.equals(session)) {
                peer.getBasicRemote().sendObject(message);
            }
        }
    }

    @OnOpen
    public void onOpen(Session peer) {
        peers.add(peer);
    }

    @OnClose
    public void onClose(Session peer) {
        peers.remove(peer);
    }
}

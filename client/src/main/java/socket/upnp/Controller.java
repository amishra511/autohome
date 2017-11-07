/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socket.upnp;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.util.List;
import java.util.concurrent.TimeUnit;
import socket.upnp.utils.GenericUtils;

/**
 *
 * @author Ashish
 */
public class Controller {

    public Controller(){
        
    }
    public static void main(String[] args) {
            List<Device> deviceDetails = discoverUpnpDevices();
                    System.out.println("---Device discovery results---");
                    for(Device dev:deviceDetails){
                        System.out.println(dev.toString());
                    }
    }
    
    public static List discoverUpnpDevices(){
       return  GenericUtils.findDevices();
    }
}

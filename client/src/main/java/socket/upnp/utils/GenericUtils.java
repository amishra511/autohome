/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socket.upnp.utils;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Ashish
 */
public class GenericUtils {
    private static final String LOCAL_IP_ADDRESS = "10.0.0.33"; //Use Ipconfig and get local Ip address for Wireless Interface
    private static final String UPNP_IP_ADDRESS = "239.255.255.250";
    private static final String MULTICAST_IP_ADDRESS = "0.0.0.0";
    private static final Integer MULTICAST_PORT = 1901;
    private static final Integer UPNP_PORT = 1900;
    private static final String DEVICE_TYPE_UPNP_ROOT = "upnp:rootdevice";
    private static final Integer LISTEN_WAIT_TIME = 4; //In seconds

    public static String findDevices(){
        sendDiscoveryRequest();
        return listenDiscoveryResponse();
    }
    
     private static void sendDiscoveryRequest() {
        try {
            InetSocketAddress socketAddress = new InetSocketAddress(InetAddress.getByName(UPNP_IP_ADDRESS), UPNP_PORT);
            MulticastSocket socket = new MulticastSocket(null);
            try {
                socket.bind(new InetSocketAddress(LOCAL_IP_ADDRESS, MULTICAST_PORT));
                StringBuilder packet = new StringBuilder();
                packet.append("M-SEARCH * HTTP/1.1\r\n");
                packet.append("HOST: ").append(UPNP_IP_ADDRESS).append(":").append(UPNP_PORT).append("\r\n");
                packet.append("MAN: \"ssdp:discover\"\r\n");
                packet.append("MX: ").append("2").append("\r\n");
//                packet.append("ST: ").append("ssdp:all").append("\r\n").append("\r\n"); // Gives router
                packet.append("ST: ").append(DEVICE_TYPE_UPNP_ROOT).append("\r\n").append("\r\n"); //Gives belkin
//            packet.append( "ST: " ).append( "urn:Belkin:device:controllee:1" ).append( "\r\n" ).append( "\r\n" ); //Gives belkin
                byte[] data = packet.toString().getBytes();
                System.out.println("sending discovery packet"+ packet.toString());
                socket.send(new DatagramPacket(data, data.length, socketAddress));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                socket.disconnect();
                socket.close();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
    private static String listenDiscoveryResponse() {
        String deviceDetails = null;
        try {
            MulticastSocket recSocket = new MulticastSocket(null);
            recSocket.bind(new InetSocketAddress(InetAddress.getByName(MULTICAST_IP_ADDRESS), MULTICAST_PORT));
            recSocket.setTimeToLive(10);
            recSocket.setSoTimeout(1000);
            recSocket.joinGroup(InetAddress.getByName(UPNP_IP_ADDRESS));

            int timer = 0;
//        while (timer<2) {  //inService is a variable controlled by a thread to stop the listener
            for (long stop = System.nanoTime() + TimeUnit.SECONDS.toNanos(LISTEN_WAIT_TIME); stop > System.nanoTime();) {

                byte[] buf = new byte[2048];
                DatagramPacket input = new DatagramPacket(buf, buf.length);
                try {
                    recSocket.receive(input);
                    String rawResponse = new String(input.getData());
                    String originalAddr = input.getAddress().getHostName();
                    
//                    System.out.println("Data: " + rawResponse);
//                    System.out.println("Host Name: " + originalAddr);
                    deviceDetails = rawResponse;
//                    System.out.println(input.getSocketAddress().toString());

                } catch (Exception e) {
                }
//                timer++;
            }
            recSocket.disconnect();
            recSocket.close();
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return deviceDetails;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socket.upnp.utils;

import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import socket.upnp.Device;
import socket.upnp.DeviceGeneral;

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
    private static final Integer LISTEN_WAIT_TIME = 10; //In seconds

    public static List findDevices() {
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
                System.out.println("sending discovery packet" + packet.toString());
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

    private static List listenDiscoveryResponse() {
        List devList = new ArrayList<>();
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
                    String data = new String(input.getData());
                    String hostName = input.getAddress().getHostName();
                    Device retDevc = getUpnpDevice(data);
                    System.out.println("Device General: "+retDevc.getDeviceGeneral().toString());
                    if (null != retDevc) {
                        devList.add(retDevc);
                    }
//                    System.out.println("Data: " + rawResponse);
//                    System.out.println("Host Name: " + originalAddr);
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
        return devList;
    }
    
    private static Device getUpnpDevice(String payload) {
        System.out.println("----------Inside parse method---------");
        Device dvc = null;
        String[] lines = payload.split("\r\n|\r|\n");
        for (String line : lines) {
            if (line.startsWith("LOCATION")) {
                String location = line.substring(line.indexOf(":") + 1);
                System.out.println(location);
                dvc = getDeviceDetails(location);
            }
        }
        return dvc;
    }

    public static Device getDeviceDetails(String path) {
        Device devc = new Device();
        DeviceGeneral devGenral = new DeviceGeneral();
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();        
        try {
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            URL url = new URL(path);
            InputStream stream = url.openStream();
            Document doc = docBuilder.parse(stream);
            doc.getDocumentElement().normalize();
//            System.out.println("Root element of the doc is "
//                    + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("device");
            Node device = nList.item(0);
            NodeList devProp = device.getChildNodes();
            for (int i = 0; i < devProp.getLength(); i++) {
                Node prop = devProp.item(i);
                if ("deviceType".equalsIgnoreCase(prop.getNodeName())) {
                    System.out.println("Device Type:" + prop.getTextContent());
                    devGenral.setDeviceType(prop.getTextContent());
                }
                if ("friendlyName".equalsIgnoreCase(prop.getNodeName())) {
                    System.out.println("Device Name:" + prop.getTextContent());
                    devGenral.setDeviceName(prop.getTextContent());
                }
                if ("manufacturer".equalsIgnoreCase(prop.getNodeName())) {
                    System.out.println("Device manufacturer:" + prop.getTextContent());
                    devGenral.setManufacturer(prop.getTextContent());
                }
                if ("modelName".equalsIgnoreCase(prop.getNodeName())) {
                    System.out.println("Device manufacturer:" + prop.getTextContent());
                    devGenral.setModelName(prop.getTextContent());
                }
                if ("modelDescription".equalsIgnoreCase(prop.getNodeName())) {
                    System.out.println("Device manufacturer:" + prop.getTextContent());
                    devGenral.setModelDesc(prop.getTextContent());
                }
                if("UDN".equalsIgnoreCase(prop.getNodeName())){
                    System.out.println("Device manufacturer:" + prop.getTextContent());
                    devGenral.setUdn(prop.getTextContent());
                }
                devGenral.setDeviceState("0");
                //Add general device data to device
                devc.setDeviceGeneral(devGenral);
                //Add other device attributes
                if("macAddress".equalsIgnoreCase(prop.getNodeName())){
                    System.out.println("Device MAC:" + prop.getTextContent());
                    devc.setMacAddress(prop.getTextContent());
                }
                if("serialNumber".equalsIgnoreCase(prop.getNodeName())){
                    System.out.println("Device Serial #:" + prop.getTextContent());
                    devc.setSerialNumber(prop.getTextContent());
                }
                if("UPC".equalsIgnoreCase(prop.getNodeName())){
                    System.out.println("Device UPC #:" + prop.getTextContent());
                    devc.setUpc(prop.getTextContent());
                }
                devc.setMacAddress(LOCAL_IP_ADDRESS);
              //Get full host address from path param
              URL devUrl = new URL(path);
              devc.setHostAddress(devUrl.getProtocol()+"://"+devUrl.getHost()+":"+devUrl.getPort());
            }
            
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return devc;
    }
    
    public static List copyDeviceDetails(List<Device> devList){
        List<DeviceGeneral> genDevic = new ArrayList<>();
        for(Device dev: devList){
            genDevic.add(dev.getDeviceGeneral());
        }
        return genDevic;
    }
}

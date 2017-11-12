/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socket.upnp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import socket.communicate.Payload;
import socket.communicate.Request;
import socket.communicate.Response;
import socket.upnp.utils.GenericUtils;

/**
 *
 * @author Ashish
 */
public class Controller {
     private List<Device> networkDevices;

  

    public Controller() {
            networkDevices = new ArrayList<>();
    }

    public static void main(String[] args) {
//            List<Device> deviceDetails = discoverUpnpDevices();
//                    System.out.println("---Device discovery results---");
//                    for(Device dev:deviceDetails){
//                        System.out.println(dev.toString());
//                    }

//        Payload pay = new Payload();
//        Request req = new Request();
//        req.setDeviceId("dev");
//        req.setDeviceState("state");
//        req.setOperation("test");
//        Response resp = new Response();
//        Device dev = new Device();
//        dev.setDeviceType("Dev1");
//        dev.setManufacturer("manuf");
//        List devList = new ArrayList();  
//        devList.add(dev);
//        resp.setDeviceDetails(devList);
//        
//        pay.setRequest(req);
////        pay.setResponse(resp);
//        ObjectMapper mapper = new ObjectMapper();
//        try{
//        String jsonInString = mapper.writeValueAsString(pay);
//        System.out.println(jsonInString);
//        }
//        catch(Exception exp){
//            exp.printStackTrace();
//        }

     GenericUtils.findDevices();
    }

    public String getRespJson(Payload reqPld) {
        String respJson = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            respJson = mapper.writeValueAsString(processRequest(reqPld));
        } catch (JsonProcessingException jexp) {
            jexp.printStackTrace();
        }
        return respJson;
    }

    public Payload processRequest(Payload pld) {
        Payload resPld = new Payload();
        Request req = pld.getRequest();
        if (null != req) {
            if (null != req.getOperation()) {
                if ("discover".equals(req.getOperation().trim())) {
                    List deviceDetails = GenericUtils.findDevices();
                    networkDevices = deviceDetails;
                    System.out.println("device list length: "+deviceDetails.size());
                    resPld.setRequest(pld.getRequest());
                    Response resp = new Response();
                    resp.setDeviceDetails(GenericUtils.copyDeviceDetails(deviceDetails));
                    resPld.setResponse(resp);
                }
//                if("flipState".equals(req.getOperation().trim())){
//                    if(null != networkDevices){
//                        for(Device dev: networkDevices){
//                            if(req.getDeviceId().equals(dev.getUdn())){
//                                
//                            }
//                        }
//                      }
//                }
            } else {
                //No operation specified in request
            }
        } else {
            //No request found in the payload
        }
        return resPld;
    }
    
    public List<Device> getNetworkDevices() {
        return networkDevices;
    }

    public void setNetworkDevices(List<Device> networkDevices) {
        this.networkDevices = networkDevices;
    }
}

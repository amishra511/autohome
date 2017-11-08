/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socket.communicate;

/**
 *
 * @author Ashish
 */
import java.util.List;
import socket.upnp.Device;

/**
 *
 * @author Ashish
 */
public class Response {
    private List<Device> deviceDetails;
    private Error error;

    public List getDeviceDetails() {
        return deviceDetails;
    }

    public void setDeviceDetails(List deviceDetails) {
        this.deviceDetails = deviceDetails;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
    
}


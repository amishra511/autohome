/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upnp.socket.communicate;

/**
 *
 * @author Ashish
 */
public class Request {
    
    private String operation;
    private String deviceId;
    private String deviceState;
    
    public Request(){
        
    }
    
    public Request(String operation, String deviceId, String deviceState){
        this.operation = operation;
        this.deviceId = deviceId;
        this.deviceState = deviceState;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setDeviceState(String deviceState) {
        this.deviceState = deviceState;
    }

    public String getOperation() {
        return operation;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getDeviceState() {
        return deviceState;
    }
    
}


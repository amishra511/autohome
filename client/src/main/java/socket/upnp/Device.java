/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socket.upnp;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 *
 * @author Ashish
 */
public class Device {

    private DeviceGeneral deviceGeneral;
    private String serialNumber;
    private String upc;
    private String macAddress;
    private String binaryState;
    private String hostAddress;

    public String getHostAddress() {
        return hostAddress;
    }

    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public DeviceGeneral getDeviceGeneral() {
        return deviceGeneral;
    }

    public void setDeviceGeneral(DeviceGeneral deviceGeneral) {
        this.deviceGeneral = deviceGeneral;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getBinaryState() {
        return binaryState;
    }

    public void setBinaryState(String binaryState) {
        this.binaryState = binaryState;
    }
    
    @Override
    public String toString() {
     return ReflectionToStringBuilder.toString(this);
 }

    
}

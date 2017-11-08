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
public class Payload {
    
private Request request;
private Response response;

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    
    public Payload(){
        
    }
    
//    public Payload(String operation, String deviceId, String deviceState){
//        this.operation = operation;
//        this.deviceId = deviceId;
//        this.deviceState = deviceState;
//    }


    
}



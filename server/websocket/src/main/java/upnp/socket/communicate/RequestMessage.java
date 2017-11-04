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
import java.io.StringWriter;
import javax.json.Json;
import javax.json.JsonObject;

/**
 *
 * @author Ashish
 */
public class RequestMessage {
    private JsonObject json;

    public RequestMessage(){        
    }
    public RequestMessage(JsonObject json){
        this.json = json;
    }
    public void setJson(JsonObject json) {
        this.json = json;
    }

    public JsonObject getJson() {
        return json;
    }
    
    @Override
    public String toString() {
        StringWriter writer = new StringWriter();
        Json.createWriter(writer).write(json);
        return writer.toString();
    }
}


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socket.upnp.utils;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

/**
 *
 * @author Ashish
 */
public class WemoServiceUtils {
    	    private static String EVENTS_XML = "http://10.0.0.191:49153/eventservice.xml";
	    private static final String soapEndPointUrl = "http://10.0.0.191:49153/upnp/control/basicevent1";
	//private static final String soapAction = "http://10.0.0.191:49153/GetBinaryState";
	    private static final String soapActionGetState = "\"urn:Belkin:service:basicevent:1#GetBinaryState\"";
	    private static final String soapActionSetState = "\"urn:Belkin:service:basicevent:1#SetBinaryState\"";

	    
	    public static String switchDeviceState(String soapEndpointUrl, String soapAction, String nextState) {
	    	String response = callSoapWebService(soapEndpointUrl, soapAction, nextState);
	    	return response;
	    }
	    
//	    private static void createSoapEnvelope(SOAPMessage soapMessage) throws SOAPException {
//	        SOAPPart soapPart = soapMessage.getSOAPPart();
//
//	        String myNamespace = "u";
//	        String myNamespaceURI = "urn:Belkin:service:basicevent:1";
//
//	        // SOAP Envelope
//	        SOAPEnvelope envelope = soapPart.getEnvelope();
//	        envelope.addNamespaceDeclaration(myNamespace, myNamespaceURI);
//
//	        /*
//	        Constructed SOAP Request Message:
//	        <SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/" xmlns:myNamespace="http://www.webserviceX.NET">
//	        <SOAP-ENV:Header/>
//	        <SOAP-ENV:Body>
//	        <myNamespace:GetInfoByCity>
//	        <myNamespace:USCity>New York</myNamespace:USCity>
//	        </myNamespace:GetInfoByCity>
//	        </SOAP-ENV:Body>
//	        </SOAP-ENV:Envelope>
//	         */
//
//	        // SOAP Body
//	        SOAPBody soapBody = envelope.getBody();
//	        SOAPElement soapBodyElem = soapBody.addChildElement("GetBinaryState", myNamespace);
//	        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("BinaryState");
//	        soapBodyElem1.addTextNode("1");
//	    }

	    private static void createSoapEnvelopeSetState(SOAPMessage soapMessage, String nextState) throws SOAPException {
	        SOAPPart soapPart = soapMessage.getSOAPPart();

	        String myNamespace = "u";
	        String myNamespaceURI = "urn:Belkin:service:basicevent:1";

	        // SOAP Envelope
	        SOAPEnvelope envelope = soapPart.getEnvelope();
	        envelope.addNamespaceDeclaration(myNamespace, myNamespaceURI);

	        /*
	        Constructed SOAP Request Message:
	        <SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/" xmlns:myNamespace="http://www.webserviceX.NET">
	        <SOAP-ENV:Header/>
	        <SOAP-ENV:Body>
	        <myNamespace:GetInfoByCity>
	        <myNamespace:USCity>New York</myNamespace:USCity>
	        </myNamespace:GetInfoByCity>
	        </SOAP-ENV:Body>
	        </SOAP-ENV:Envelope>
	         */

	        // SOAP Body
	        SOAPBody soapBody = envelope.getBody();
	        SOAPElement soapBodyElem = soapBody.addChildElement("SetBinaryState", myNamespace);
	        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("BinaryState");
	        soapBodyElem1.addTextNode(nextState);
	    }

	    private static String callSoapWebService(String soapEndpointUrl, String soapAction, String nextState) {
	    	String response = null;
	        try {
	        	
	            // Create SOAP Connection
	            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
	            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

	            // Send SOAP Message to SOAP Server
	            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(soapAction, nextState), soapEndpointUrl);

	            // Print the SOAP Response
	            System.out.println("Response SOAP Message:");
	            soapResponse.writeTo(System.out);
	            response = soapResponse.getSOAPPart().toString();
	            System.out.println();

	            soapConnection.close();
	        } catch (Exception e) {
	            System.err.println("\nError occurred while sending SOAP Request to Server!\nMake sure you have the correct endpoint URL and SOAPAction!\n");
	            e.printStackTrace();
	        }
	        return response;
	    }

	    private static SOAPMessage createSOAPRequest(String soapAction, String nextState) throws Exception {
	        MessageFactory messageFactory = MessageFactory.newInstance();
	        SOAPMessage soapMessage = messageFactory.createMessage();

//	        createSoapEnvelope(soapMessage);
	        createSoapEnvelopeSetState(soapMessage, nextState);

	        MimeHeaders headers = soapMessage.getMimeHeaders();
	        headers.addHeader("SOAPAction", soapAction);

	        soapMessage.saveChanges();

	        /* Print the request message, just for debugging purposes */
	        System.out.println("Request SOAP Message:");
	        soapMessage.writeTo(System.out);
	        System.out.println("\n");

	        return soapMessage;
	    }
}

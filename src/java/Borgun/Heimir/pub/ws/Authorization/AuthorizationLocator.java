/**
 * AuthorizationLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package Borgun.Heimir.pub.ws.Authorization;

public class AuthorizationLocator extends org.apache.axis.client.Service implements Borgun.Heimir.pub.ws.Authorization.Authorization {

    public AuthorizationLocator() {
    }


    public AuthorizationLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public AuthorizationLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for Heimir_pub_ws_Authorization_Port
    private java.lang.String Heimir_pub_ws_Authorization_Port_address = "http://WM9XTEST02.europay.is:5555/ws/Heimir.pub.ws:Authorization/Heimir_pub_ws_Authorization_Port";

    public java.lang.String getHeimir_pub_ws_Authorization_PortAddress() {
        return Heimir_pub_ws_Authorization_Port_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String Heimir_pub_ws_Authorization_PortWSDDServiceName = "Heimir_pub_ws_Authorization_Port";

    public java.lang.String getHeimir_pub_ws_Authorization_PortWSDDServiceName() {
        return Heimir_pub_ws_Authorization_PortWSDDServiceName;
    }

    public void setHeimir_pub_ws_Authorization_PortWSDDServiceName(java.lang.String name) {
        Heimir_pub_ws_Authorization_PortWSDDServiceName = name;
    }

    public Borgun.Heimir.pub.ws.Authorization.Authorization_PortType getHeimir_pub_ws_Authorization_Port() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(Heimir_pub_ws_Authorization_Port_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getHeimir_pub_ws_Authorization_Port(endpoint);
    }

    public Borgun.Heimir.pub.ws.Authorization.Authorization_PortType getHeimir_pub_ws_Authorization_Port(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            Borgun.Heimir.pub.ws.Authorization.Heimir_pub_ws_Authorization_BinderStub _stub = new Borgun.Heimir.pub.ws.Authorization.Heimir_pub_ws_Authorization_BinderStub(portAddress, this);
            _stub.setPortName(getHeimir_pub_ws_Authorization_PortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setHeimir_pub_ws_Authorization_PortEndpointAddress(java.lang.String address) {
        Heimir_pub_ws_Authorization_Port_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (Borgun.Heimir.pub.ws.Authorization.Authorization_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                Borgun.Heimir.pub.ws.Authorization.Heimir_pub_ws_Authorization_BinderStub _stub = new Borgun.Heimir.pub.ws.Authorization.Heimir_pub_ws_Authorization_BinderStub(new java.net.URL(Heimir_pub_ws_Authorization_Port_address), this);
                _stub.setPortName(getHeimir_pub_ws_Authorization_PortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("Heimir_pub_ws_Authorization_Port".equals(inputPortName)) {
            return getHeimir_pub_ws_Authorization_Port();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://Borgun/Heimir/pub/ws/Authorization", "Authorization");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://Borgun/Heimir/pub/ws/Authorization", "Heimir_pub_ws_Authorization_Port"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("Heimir_pub_ws_Authorization_Port".equals(portName)) {
            setHeimir_pub_ws_Authorization_PortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}

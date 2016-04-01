package Borgun.Heimir.pub.ws.Authorization;

public class Authorization_PortTypeProxy implements Borgun.Heimir.pub.ws.Authorization.Authorization_PortType {
  private String _endpoint = null;
  private String login = null;
  private String password = null;
  
  private Borgun.Heimir.pub.ws.Authorization.Authorization_PortType authorization_PortType = null;
  
  public Authorization_PortTypeProxy() {
    _initAuthorization_PortTypeProxy();
  }
  
  public Authorization_PortTypeProxy(String login, String password) {
	  	this.login = login;
	  	this.password = password;
	    _initAuthorization_PortTypeProxy();
  }
  
  public Authorization_PortTypeProxy(String endpoint) {
    _endpoint = endpoint;
    _initAuthorization_PortTypeProxy();
  }
  
  public Authorization_PortTypeProxy(String endpoint, String login, String password) {
	    _endpoint = endpoint;
	  	this.login = login;
	  	this.password = password;
	    _initAuthorization_PortTypeProxy();
  }
  
  private void _initAuthorization_PortTypeProxy() {
    try {
      authorization_PortType = (new Borgun.Heimir.pub.ws.Authorization.AuthorizationLocator()).getHeimir_pub_ws_Authorization_Port();
      if (authorization_PortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)authorization_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)authorization_PortType)._getProperty("javax.xml.rpc.service.endpoint.address");
        if ((login != null) && (password != null)){
        	((javax.xml.rpc.Stub)authorization_PortType)._setProperty(javax.xml.rpc.Stub.USERNAME_PROPERTY, login);
        	((javax.xml.rpc.Stub)authorization_PortType)._setProperty(javax.xml.rpc.Stub.PASSWORD_PROPERTY, password);
        }
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (authorization_PortType != null)
      ((javax.xml.rpc.Stub)authorization_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public Borgun.Heimir.pub.ws.Authorization.Authorization_PortType getAuthorization_PortType() {
    if (authorization_PortType == null)
      _initAuthorization_PortTypeProxy();
    return authorization_PortType;
  }
  
  public java.lang.String getSettlementChargeback(java.lang.String settlementChargebackRequestXML) throws java.rmi.RemoteException{
    if (authorization_PortType == null)
      _initAuthorization_PortTypeProxy();
    return authorization_PortType.getSettlementChargeback(settlementChargebackRequestXML);
  }
  
  public java.lang.String sendDetailData(java.lang.String detailDataXML) throws java.rmi.RemoteException{
    if (authorization_PortType == null)
      _initAuthorization_PortTypeProxy();
    return authorization_PortType.sendDetailData(detailDataXML);
  }
  
  public java.lang.String getActionCodeTexts(java.lang.String actionCode) throws java.rmi.RemoteException{
    if (authorization_PortType == null)
      _initAuthorization_PortTypeProxy();
    return authorization_PortType.getActionCodeTexts(actionCode);
  }
  
  public java.lang.String getSettlementTransactions(java.lang.String settlementTransactionRequestXML) throws java.rmi.RemoteException{
    if (authorization_PortType == null)
      _initAuthorization_PortTypeProxy();
    return authorization_PortType.getSettlementTransactions(settlementTransactionRequestXML);
  }
  
  public java.lang.String removeBlacklistedCard(java.lang.String blacklistCardRemovalRequestXML) throws java.rmi.RemoteException{
    if (authorization_PortType == null)
      _initAuthorization_PortTypeProxy();
    return authorization_PortType.removeBlacklistedCard(blacklistCardRemovalRequestXML);
  }
  
  public java.lang.String getSettlementBatch(java.lang.String settlementRequestXML) throws java.rmi.RemoteException{
    if (authorization_PortType == null)
      _initAuthorization_PortTypeProxy();
    return authorization_PortType.getSettlementBatch(settlementRequestXML);
  }
  
  public java.lang.String newBatch(java.lang.String newBatchReqXml) throws java.rmi.RemoteException{
    if (authorization_PortType == null)
      _initAuthorization_PortTypeProxy();
    return authorization_PortType.newBatch(newBatchReqXml);
  }
  
  public java.lang.String getTransactionList(java.lang.String transactionListReqXML) throws java.rmi.RemoteException{
    if (authorization_PortType == null)
      _initAuthorization_PortTypeProxy();
    return authorization_PortType.getTransactionList(transactionListReqXML);
  }
  
  public java.lang.String getSettlementSummary(java.lang.String settlementSummaryRequestXML) throws java.rmi.RemoteException{
    if (authorization_PortType == null)
      _initAuthorization_PortTypeProxy();
    return authorization_PortType.getSettlementSummary(settlementSummaryRequestXML);
  }
  
  public java.lang.String getVirtualCard(java.lang.String virtualCardRequestXML) throws java.rmi.RemoteException{
    if (authorization_PortType == null)
      _initAuthorization_PortTypeProxy();
    return authorization_PortType.getVirtualCard(virtualCardRequestXML);
  }
  
  public java.lang.String cancelAuthorization(java.lang.String cancelAuthReqXml) throws java.rmi.RemoteException{
    if (authorization_PortType == null)
      _initAuthorization_PortTypeProxy();
    return authorization_PortType.cancelAuthorization(cancelAuthReqXml);
  }
  
  public java.lang.String getAuthorization(java.lang.String getAuthReqXml) throws java.rmi.RemoteException{
    if (authorization_PortType == null)
      _initAuthorization_PortTypeProxy();
    return authorization_PortType.getAuthorization(getAuthReqXml);
  }
  
  public java.lang.String getDisputedTransactions(java.lang.String disputedRequestXML) throws java.rmi.RemoteException{
    if (authorization_PortType == null)
      _initAuthorization_PortTypeProxy();
    return authorization_PortType.getDisputedTransactions(disputedRequestXML);
  }
  
  public java.lang.String getSettlements(java.lang.String settlementRequestXML) throws java.rmi.RemoteException{
    if (authorization_PortType == null)
      _initAuthorization_PortTypeProxy();
    return authorization_PortType.getSettlements(settlementRequestXML);
  }
  
  public java.lang.String getFraudTransactions(java.lang.String fraudRequestXML) throws java.rmi.RemoteException{
    if (authorization_PortType == null)
      _initAuthorization_PortTypeProxy();
    return authorization_PortType.getFraudTransactions(fraudRequestXML);
  }
  
  public java.lang.String getSettlementFee(java.lang.String settlementIsoFeeRequestXML) throws java.rmi.RemoteException{
    if (authorization_PortType == null)
      _initAuthorization_PortTypeProxy();
    return authorization_PortType.getSettlementFee(settlementIsoFeeRequestXML);
  }
  
  public java.lang.String setBlacklistedCard(java.lang.String blacklistCardRequestXML) throws java.rmi.RemoteException{
    if (authorization_PortType == null)
      _initAuthorization_PortTypeProxy();
    return authorization_PortType.setBlacklistedCard(blacklistCardRequestXML);
  }
  
  
}
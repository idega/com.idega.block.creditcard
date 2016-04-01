/**
 * Authorization_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package Borgun.Heimir.pub.ws.Authorization;

public interface Authorization_PortType extends java.rmi.Remote {
    public java.lang.String getSettlementChargeback(java.lang.String settlementChargebackRequestXML) throws java.rmi.RemoteException;
    public java.lang.String sendDetailData(java.lang.String detailDataXML) throws java.rmi.RemoteException;
    public java.lang.String getActionCodeTexts(java.lang.String actionCode) throws java.rmi.RemoteException;
    public java.lang.String getSettlementTransactions(java.lang.String settlementTransactionRequestXML) throws java.rmi.RemoteException;
    public java.lang.String removeBlacklistedCard(java.lang.String blacklistCardRemovalRequestXML) throws java.rmi.RemoteException;
    public java.lang.String getSettlementBatch(java.lang.String settlementRequestXML) throws java.rmi.RemoteException;
    public java.lang.String newBatch(java.lang.String newBatchReqXml) throws java.rmi.RemoteException;
    public java.lang.String getTransactionList(java.lang.String transactionListReqXML) throws java.rmi.RemoteException;
    public java.lang.String getSettlementSummary(java.lang.String settlementSummaryRequestXML) throws java.rmi.RemoteException;
    public java.lang.String getVirtualCard(java.lang.String virtualCardRequestXML) throws java.rmi.RemoteException;
    public java.lang.String cancelAuthorization(java.lang.String cancelAuthReqXml) throws java.rmi.RemoteException;
    public java.lang.String getAuthorization(java.lang.String getAuthReqXml) throws java.rmi.RemoteException;
    public java.lang.String getDisputedTransactions(java.lang.String disputedRequestXML) throws java.rmi.RemoteException;
    public java.lang.String getSettlements(java.lang.String settlementRequestXML) throws java.rmi.RemoteException;
    public java.lang.String getFraudTransactions(java.lang.String fraudRequestXML) throws java.rmi.RemoteException;
    public java.lang.String getSettlementFee(java.lang.String settlementIsoFeeRequestXML) throws java.rmi.RemoteException;
    public java.lang.String setBlacklistedCard(java.lang.String blacklistCardRequestXML) throws java.rmi.RemoteException;
}

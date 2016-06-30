
package borgun.heimir.pub.ws.authorization;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "Authorization_PortType", targetNamespace = "http://Borgun/Heimir/pub/ws/Authorization")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface AuthorizationPortType {


    /**
     * 
     * @param settlementChargebackRequestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(action = "Heimir_pub_ws_Authorization_Binder_getSettlementChargeback")
    @WebResult(name = "SettlementChargebackResponseXML", targetNamespace = "")
    @RequestWrapper(localName = "getSettlementChargeback", targetNamespace = "http://Borgun/Heimir/pub/ws/Authorization", className = "borgun.heimir.pub.ws.authorization.GetSettlementChargeback")
    @ResponseWrapper(localName = "getSettlementChargebackResponse", targetNamespace = "http://Borgun/Heimir/pub/ws/Authorization", className = "borgun.heimir.pub.ws.authorization.GetSettlementChargebackResponse")
    public String getSettlementChargeback(
        @WebParam(name = "SettlementChargebackRequestXML", targetNamespace = "")
        String settlementChargebackRequestXML);

    /**
     * 
     * @param detailDataXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(action = "Heimir_pub_ws_Authorization_Binder_sendDetailData")
    @WebResult(name = "detailDataResXML", targetNamespace = "")
    @RequestWrapper(localName = "sendDetailData", targetNamespace = "http://Borgun/Heimir/pub/ws/Authorization", className = "borgun.heimir.pub.ws.authorization.SendDetailData")
    @ResponseWrapper(localName = "sendDetailDataResponse", targetNamespace = "http://Borgun/Heimir/pub/ws/Authorization", className = "borgun.heimir.pub.ws.authorization.SendDetailDataResponse")
    public String sendDetailData(
        @WebParam(name = "detailDataXML", targetNamespace = "")
        String detailDataXML);

    /**
     * 
     * @param actionCode
     * @return
     *     returns java.lang.String
     */
    @WebMethod(action = "Heimir_pub_ws_Authorization_Binder_getActionCodeTexts")
    @WebResult(name = "ActionCodeTextsXML", targetNamespace = "")
    @RequestWrapper(localName = "getActionCodeTexts", targetNamespace = "http://Borgun/Heimir/pub/ws/Authorization", className = "borgun.heimir.pub.ws.authorization.GetActionCodeTexts")
    @ResponseWrapper(localName = "getActionCodeTextsResponse", targetNamespace = "http://Borgun/Heimir/pub/ws/Authorization", className = "borgun.heimir.pub.ws.authorization.GetActionCodeTextsResponse")
    public String getActionCodeTexts(
        @WebParam(name = "ActionCode", targetNamespace = "")
        String actionCode);

    /**
     * 
     * @param settlementTransactionRequestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(action = "Heimir_pub_ws_Authorization_Binder_getSettlementTransactions")
    @WebResult(name = "SettlementTransactionsResponseXML", targetNamespace = "")
    @RequestWrapper(localName = "getSettlementTransactions", targetNamespace = "http://Borgun/Heimir/pub/ws/Authorization", className = "borgun.heimir.pub.ws.authorization.GetSettlementTransactions")
    @ResponseWrapper(localName = "getSettlementTransactionsResponse", targetNamespace = "http://Borgun/Heimir/pub/ws/Authorization", className = "borgun.heimir.pub.ws.authorization.GetSettlementTransactionsResponse")
    public String getSettlementTransactions(
        @WebParam(name = "SettlementTransactionRequestXML", targetNamespace = "")
        String settlementTransactionRequestXML);

    /**
     * 
     * @param blacklistCardRemovalRequestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(action = "Heimir_pub_ws_Authorization_Binder_removeBlacklistedCard")
    @WebResult(name = "BlacklistCardRemovalResponseXML", targetNamespace = "")
    @RequestWrapper(localName = "removeBlacklistedCard", targetNamespace = "http://Borgun/Heimir/pub/ws/Authorization", className = "borgun.heimir.pub.ws.authorization.RemoveBlacklistedCard")
    @ResponseWrapper(localName = "removeBlacklistedCardResponse", targetNamespace = "http://Borgun/Heimir/pub/ws/Authorization", className = "borgun.heimir.pub.ws.authorization.RemoveBlacklistedCardResponse")
    public String removeBlacklistedCard(
        @WebParam(name = "BlacklistCardRemovalRequestXML", targetNamespace = "")
        String blacklistCardRemovalRequestXML);

    /**
     * 
     * @param settlementRequestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(action = "Heimir_pub_ws_Authorization_Binder_getSettlementBatch")
    @WebResult(name = "SettlementBatchResponseXML", targetNamespace = "")
    @RequestWrapper(localName = "getSettlementBatch", targetNamespace = "http://Borgun/Heimir/pub/ws/Authorization", className = "borgun.heimir.pub.ws.authorization.GetSettlementBatch")
    @ResponseWrapper(localName = "getSettlementBatchResponse", targetNamespace = "http://Borgun/Heimir/pub/ws/Authorization", className = "borgun.heimir.pub.ws.authorization.GetSettlementBatchResponse")
    public String getSettlementBatch(
        @WebParam(name = "SettlementRequestXML", targetNamespace = "")
        String settlementRequestXML);

    /**
     * 
     * @param newBatchReqXml
     * @return
     *     returns java.lang.String
     */
    @WebMethod(action = "Heimir_pub_ws_Authorization_Binder_newBatch")
    @WebResult(name = "newBatchResXml", targetNamespace = "")
    @RequestWrapper(localName = "newBatch", targetNamespace = "http://Borgun/Heimir/pub/ws/Authorization", className = "borgun.heimir.pub.ws.authorization.NewBatch")
    @ResponseWrapper(localName = "newBatchResponse", targetNamespace = "http://Borgun/Heimir/pub/ws/Authorization", className = "borgun.heimir.pub.ws.authorization.NewBatchResponse")
    public String newBatch(
        @WebParam(name = "newBatchReqXml", targetNamespace = "")
        String newBatchReqXml);

    /**
     * 
     * @param transactionListReqXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(action = "Heimir_pub_ws_Authorization_Binder_getTransactionList")
    @WebResult(name = "transactionListXML", targetNamespace = "")
    @RequestWrapper(localName = "getTransactionList", targetNamespace = "http://Borgun/Heimir/pub/ws/Authorization", className = "borgun.heimir.pub.ws.authorization.GetTransactionList")
    @ResponseWrapper(localName = "getTransactionListResponse", targetNamespace = "http://Borgun/Heimir/pub/ws/Authorization", className = "borgun.heimir.pub.ws.authorization.GetTransactionListResponse")
    public String getTransactionList(
        @WebParam(name = "transactionListReqXML", targetNamespace = "")
        String transactionListReqXML);

    /**
     * 
     * @param settlementSummaryRequestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(action = "Heimir_pub_ws_Authorization_Binder_getSettlementSummary")
    @WebResult(name = "SettlementSummaryResponseXML", targetNamespace = "")
    @RequestWrapper(localName = "getSettlementSummary", targetNamespace = "http://Borgun/Heimir/pub/ws/Authorization", className = "borgun.heimir.pub.ws.authorization.GetSettlementSummary")
    @ResponseWrapper(localName = "getSettlementSummaryResponse", targetNamespace = "http://Borgun/Heimir/pub/ws/Authorization", className = "borgun.heimir.pub.ws.authorization.GetSettlementSummaryResponse")
    public String getSettlementSummary(
        @WebParam(name = "SettlementSummaryRequestXML", targetNamespace = "")
        String settlementSummaryRequestXML);

    /**
     * 
     * @param virtualCardRequestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(action = "Heimir_pub_ws_Authorization_Binder_getVirtualCard")
    @WebResult(name = "virtualCardResponseXML", targetNamespace = "")
    @RequestWrapper(localName = "getVirtualCard", targetNamespace = "http://Borgun/Heimir/pub/ws/Authorization", className = "borgun.heimir.pub.ws.authorization.GetVirtualCard")
    @ResponseWrapper(localName = "getVirtualCardResponse", targetNamespace = "http://Borgun/Heimir/pub/ws/Authorization", className = "borgun.heimir.pub.ws.authorization.GetVirtualCardResponse")
    public String getVirtualCard(
        @WebParam(name = "virtualCardRequestXML", targetNamespace = "")
        String virtualCardRequestXML);

    /**
     * 
     * @param parameters
     * @return
     *     returns borgun.heimir.pub.ws.authorization.CancelAuthorizationResponse
     */
    @WebMethod(action = "Heimir_pub_ws_Authorization_Binder_cancelAuthorization")
    @WebResult(name = "cancelAuthorizationResponse", targetNamespace = "http://Borgun/Heimir/pub/ws/Authorization", partName = "parameters")
    @SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
    public CancelAuthorizationResponse cancelAuthorization(
        @WebParam(name = "cancelAuthorizationInput", targetNamespace = "http://Borgun/Heimir/pub/ws/Authorization", partName = "parameters")
        CancelAuthorizationInput parameters);

    /**
     * 
     * @param parameters
     * @return
     *     returns borgun.heimir.pub.ws.authorization.GetAuthorizationOutput
     */
    @WebMethod(action = "Heimir_pub_ws_Authorization_Binder_getAuthorization")
    @WebResult(name = "getAuthorizationOutput", targetNamespace = "http://Borgun/Heimir/pub/ws/Authorization", partName = "parameters")
    @SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
    public GetAuthorizationOutput getAuthorization(
        @WebParam(name = "getAuthorizationInput", targetNamespace = "http://Borgun/Heimir/pub/ws/Authorization", partName = "parameters")
        GetAuthorizationInput parameters);

    /**
     * 
     * @param disputedRequestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(action = "Heimir_pub_ws_Authorization_Binder_getDisputedTransactions")
    @WebResult(name = "DisputedTransactionResponseXML", targetNamespace = "")
    @RequestWrapper(localName = "getDisputedTransactions", targetNamespace = "http://Borgun/Heimir/pub/ws/Authorization", className = "borgun.heimir.pub.ws.authorization.GetDisputedTransactions")
    @ResponseWrapper(localName = "getDisputedTransactionsResponse", targetNamespace = "http://Borgun/Heimir/pub/ws/Authorization", className = "borgun.heimir.pub.ws.authorization.GetDisputedTransactionsResponse")
    public String getDisputedTransactions(
        @WebParam(name = "DisputedRequestXML", targetNamespace = "")
        String disputedRequestXML);

    /**
     * 
     * @param settlementRequestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(action = "Heimir_pub_ws_Authorization_Binder_getSettlements")
    @WebResult(name = "SettlementsXML", targetNamespace = "")
    @RequestWrapper(localName = "getSettlements", targetNamespace = "http://Borgun/Heimir/pub/ws/Authorization", className = "borgun.heimir.pub.ws.authorization.GetSettlements")
    @ResponseWrapper(localName = "getSettlementsResponse", targetNamespace = "http://Borgun/Heimir/pub/ws/Authorization", className = "borgun.heimir.pub.ws.authorization.GetSettlementsResponse")
    public String getSettlements(
        @WebParam(name = "settlementRequestXML", targetNamespace = "")
        String settlementRequestXML);

    /**
     * 
     * @param fraudRequestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(action = "Heimir_pub_ws_Authorization_Binder_getFraudTransactions")
    @WebResult(name = "FraudTransactionResponseXML", targetNamespace = "")
    @RequestWrapper(localName = "getFraudTransactions", targetNamespace = "http://Borgun/Heimir/pub/ws/Authorization", className = "borgun.heimir.pub.ws.authorization.GetFraudTransactions")
    @ResponseWrapper(localName = "getFraudTransactionsResponse", targetNamespace = "http://Borgun/Heimir/pub/ws/Authorization", className = "borgun.heimir.pub.ws.authorization.GetFraudTransactionsResponse")
    public String getFraudTransactions(
        @WebParam(name = "FraudRequestXML", targetNamespace = "")
        String fraudRequestXML);

    /**
     * 
     * @param settlementIsoFeeRequestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(action = "Heimir_pub_ws_Authorization_Binder_getSettlementFee")
    @WebResult(name = "SettlementFeeResponseXML", targetNamespace = "")
    @RequestWrapper(localName = "getSettlementFee", targetNamespace = "http://Borgun/Heimir/pub/ws/Authorization", className = "borgun.heimir.pub.ws.authorization.GetSettlementFee")
    @ResponseWrapper(localName = "getSettlementFeeResponse", targetNamespace = "http://Borgun/Heimir/pub/ws/Authorization", className = "borgun.heimir.pub.ws.authorization.GetSettlementFeeResponse")
    public String getSettlementFee(
        @WebParam(name = "SettlementIsoFeeRequestXML", targetNamespace = "")
        String settlementIsoFeeRequestXML);

    /**
     * 
     * @param blacklistCardRequestXML
     * @return
     *     returns java.lang.String
     */
    @WebMethod(action = "Heimir_pub_ws_Authorization_Binder_setBlacklistedCard")
    @WebResult(name = "BlacklistCardResponseXML", targetNamespace = "")
    @RequestWrapper(localName = "setBlacklistedCard", targetNamespace = "http://Borgun/Heimir/pub/ws/Authorization", className = "borgun.heimir.pub.ws.authorization.SetBlacklistedCard")
    @ResponseWrapper(localName = "setBlacklistedCardResponse", targetNamespace = "http://Borgun/Heimir/pub/ws/Authorization", className = "borgun.heimir.pub.ws.authorization.SetBlacklistedCardResponse")
    public String setBlacklistedCard(
        @WebParam(name = "BlacklistCardRequestXML", targetNamespace = "")
        String blacklistCardRequestXML);

}
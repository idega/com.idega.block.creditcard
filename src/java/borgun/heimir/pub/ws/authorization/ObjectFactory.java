
package borgun.heimir.pub.ws.authorization;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the borgun.heimir.pub.ws.authorization package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _NewBatchResponse_QNAME = new QName("http://Borgun/Heimir/pub/ws/Authorization", "newBatchResponse");
    private final static QName _GetActionCodeTexts_QNAME = new QName("http://Borgun/Heimir/pub/ws/Authorization", "getActionCodeTexts");
    private final static QName _NewBatch_QNAME = new QName("http://Borgun/Heimir/pub/ws/Authorization", "newBatch");
    private final static QName _GetDisputedTransactionsResponse_QNAME = new QName("http://Borgun/Heimir/pub/ws/Authorization", "getDisputedTransactionsResponse");
    private final static QName _RemoveBlacklistedCardResponse_QNAME = new QName("http://Borgun/Heimir/pub/ws/Authorization", "removeBlacklistedCardResponse");
    private final static QName _GetFraudTransactionsResponse_QNAME = new QName("http://Borgun/Heimir/pub/ws/Authorization", "getFraudTransactionsResponse");
    private final static QName _GetSettlementTransactionsResponse_QNAME = new QName("http://Borgun/Heimir/pub/ws/Authorization", "getSettlementTransactionsResponse");
    private final static QName _GetSettlementSummaryResponse_QNAME = new QName("http://Borgun/Heimir/pub/ws/Authorization", "getSettlementSummaryResponse");
    private final static QName _GetSettlementFeeResponse_QNAME = new QName("http://Borgun/Heimir/pub/ws/Authorization", "getSettlementFeeResponse");
    private final static QName _SendDetailDataResponse_QNAME = new QName("http://Borgun/Heimir/pub/ws/Authorization", "sendDetailDataResponse");
    private final static QName _GetActionCodeTextsResponse_QNAME = new QName("http://Borgun/Heimir/pub/ws/Authorization", "getActionCodeTextsResponse");
    private final static QName _GetDisputedTransactions_QNAME = new QName("http://Borgun/Heimir/pub/ws/Authorization", "getDisputedTransactions");
    private final static QName _GetSettlementChargeback_QNAME = new QName("http://Borgun/Heimir/pub/ws/Authorization", "getSettlementChargeback");
    private final static QName _SendDetailData_QNAME = new QName("http://Borgun/Heimir/pub/ws/Authorization", "sendDetailData");
    private final static QName _GetTransactionList_QNAME = new QName("http://Borgun/Heimir/pub/ws/Authorization", "getTransactionList");
    private final static QName _CancelAuthorizationInput_QNAME = new QName("http://Borgun/Heimir/pub/ws/Authorization", "cancelAuthorizationInput");
    private final static QName _CancelAuthorizationResponse_QNAME = new QName("http://Borgun/Heimir/pub/ws/Authorization", "cancelAuthorizationResponse");
    private final static QName _GetAuthorizationOutput_QNAME = new QName("http://Borgun/Heimir/pub/ws/Authorization", "getAuthorizationOutput");
    private final static QName _GetAuthorizationInput_QNAME = new QName("http://Borgun/Heimir/pub/ws/Authorization", "getAuthorizationInput");
    private final static QName _GetSettlementChargebackResponse_QNAME = new QName("http://Borgun/Heimir/pub/ws/Authorization", "getSettlementChargebackResponse");
    private final static QName _SetBlacklistedCard_QNAME = new QName("http://Borgun/Heimir/pub/ws/Authorization", "setBlacklistedCard");
    private final static QName _SetBlacklistedCardResponse_QNAME = new QName("http://Borgun/Heimir/pub/ws/Authorization", "setBlacklistedCardResponse");
    private final static QName _GetVirtualCardResponse_QNAME = new QName("http://Borgun/Heimir/pub/ws/Authorization", "getVirtualCardResponse");
    private final static QName _GetFraudTransactions_QNAME = new QName("http://Borgun/Heimir/pub/ws/Authorization", "getFraudTransactions");
    private final static QName _GetSettlementSummary_QNAME = new QName("http://Borgun/Heimir/pub/ws/Authorization", "getSettlementSummary");
    private final static QName _RemoveBlacklistedCard_QNAME = new QName("http://Borgun/Heimir/pub/ws/Authorization", "removeBlacklistedCard");
    private final static QName _GetSettlementTransactions_QNAME = new QName("http://Borgun/Heimir/pub/ws/Authorization", "getSettlementTransactions");
    private final static QName _GetSettlementBatchResponse_QNAME = new QName("http://Borgun/Heimir/pub/ws/Authorization", "getSettlementBatchResponse");
    private final static QName _GetTransactionListResponse_QNAME = new QName("http://Borgun/Heimir/pub/ws/Authorization", "getTransactionListResponse");
    private final static QName _GetVirtualCard_QNAME = new QName("http://Borgun/Heimir/pub/ws/Authorization", "getVirtualCard");
    private final static QName _GetSettlementBatch_QNAME = new QName("http://Borgun/Heimir/pub/ws/Authorization", "getSettlementBatch");
    private final static QName _GetSettlements_QNAME = new QName("http://Borgun/Heimir/pub/ws/Authorization", "getSettlements");
    private final static QName _GetSettlementFee_QNAME = new QName("http://Borgun/Heimir/pub/ws/Authorization", "getSettlementFee");
    private final static QName _GetSettlementsResponse_QNAME = new QName("http://Borgun/Heimir/pub/ws/Authorization", "getSettlementsResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: borgun.heimir.pub.ws.authorization
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetSettlementChargeback }
     * 
     */
    public GetSettlementChargeback createGetSettlementChargeback() {
        return new GetSettlementChargeback();
    }

    /**
     * Create an instance of {@link SendDetailData }
     * 
     */
    public SendDetailData createSendDetailData() {
        return new SendDetailData();
    }

    /**
     * Create an instance of {@link GetTransactionList }
     * 
     */
    public GetTransactionList createGetTransactionList() {
        return new GetTransactionList();
    }

    /**
     * Create an instance of {@link GetDisputedTransactions }
     * 
     */
    public GetDisputedTransactions createGetDisputedTransactions() {
        return new GetDisputedTransactions();
    }

    /**
     * Create an instance of {@link GetSettlementSummaryResponse }
     * 
     */
    public GetSettlementSummaryResponse createGetSettlementSummaryResponse() {
        return new GetSettlementSummaryResponse();
    }

    /**
     * Create an instance of {@link GetSettlementFeeResponse }
     * 
     */
    public GetSettlementFeeResponse createGetSettlementFeeResponse() {
        return new GetSettlementFeeResponse();
    }

    /**
     * Create an instance of {@link SendDetailDataResponse }
     * 
     */
    public SendDetailDataResponse createSendDetailDataResponse() {
        return new SendDetailDataResponse();
    }

    /**
     * Create an instance of {@link GetActionCodeTextsResponse }
     * 
     */
    public GetActionCodeTextsResponse createGetActionCodeTextsResponse() {
        return new GetActionCodeTextsResponse();
    }

    /**
     * Create an instance of {@link GetSettlementTransactionsResponse }
     * 
     */
    public GetSettlementTransactionsResponse createGetSettlementTransactionsResponse() {
        return new GetSettlementTransactionsResponse();
    }

    /**
     * Create an instance of {@link GetDisputedTransactionsResponse }
     * 
     */
    public GetDisputedTransactionsResponse createGetDisputedTransactionsResponse() {
        return new GetDisputedTransactionsResponse();
    }

    /**
     * Create an instance of {@link RemoveBlacklistedCardResponse }
     * 
     */
    public RemoveBlacklistedCardResponse createRemoveBlacklistedCardResponse() {
        return new RemoveBlacklistedCardResponse();
    }

    /**
     * Create an instance of {@link GetFraudTransactionsResponse }
     * 
     */
    public GetFraudTransactionsResponse createGetFraudTransactionsResponse() {
        return new GetFraudTransactionsResponse();
    }

    /**
     * Create an instance of {@link GetActionCodeTexts }
     * 
     */
    public GetActionCodeTexts createGetActionCodeTexts() {
        return new GetActionCodeTexts();
    }

    /**
     * Create an instance of {@link NewBatch }
     * 
     */
    public NewBatch createNewBatch() {
        return new NewBatch();
    }

    /**
     * Create an instance of {@link NewBatchResponse }
     * 
     */
    public NewBatchResponse createNewBatchResponse() {
        return new NewBatchResponse();
    }

    /**
     * Create an instance of {@link SetBlacklistedCardResponse }
     * 
     */
    public SetBlacklistedCardResponse createSetBlacklistedCardResponse() {
        return new SetBlacklistedCardResponse();
    }

    /**
     * Create an instance of {@link SetBlacklistedCard }
     * 
     */
    public SetBlacklistedCard createSetBlacklistedCard() {
        return new SetBlacklistedCard();
    }

    /**
     * Create an instance of {@link GetSettlementChargebackResponse }
     * 
     */
    public GetSettlementChargebackResponse createGetSettlementChargebackResponse() {
        return new GetSettlementChargebackResponse();
    }

    /**
     * Create an instance of {@link GetAuthorizationInput }
     * 
     */
    public GetAuthorizationInput createGetAuthorizationInput() {
        return new GetAuthorizationInput();
    }

    /**
     * Create an instance of {@link GetAuthorizationOutput }
     * 
     */
    public GetAuthorizationOutput createGetAuthorizationOutput() {
        return new GetAuthorizationOutput();
    }

    /**
     * Create an instance of {@link CancelAuthorizationInput }
     * 
     */
    public CancelAuthorizationInput createCancelAuthorizationInput() {
        return new CancelAuthorizationInput();
    }

    /**
     * Create an instance of {@link CancelAuthorizationResponse }
     * 
     */
    public CancelAuthorizationResponse createCancelAuthorizationResponse() {
        return new CancelAuthorizationResponse();
    }

    /**
     * Create an instance of {@link GetSettlementTransactions }
     * 
     */
    public GetSettlementTransactions createGetSettlementTransactions() {
        return new GetSettlementTransactions();
    }

    /**
     * Create an instance of {@link GetSettlementSummary }
     * 
     */
    public GetSettlementSummary createGetSettlementSummary() {
        return new GetSettlementSummary();
    }

    /**
     * Create an instance of {@link RemoveBlacklistedCard }
     * 
     */
    public RemoveBlacklistedCard createRemoveBlacklistedCard() {
        return new RemoveBlacklistedCard();
    }

    /**
     * Create an instance of {@link GetFraudTransactions }
     * 
     */
    public GetFraudTransactions createGetFraudTransactions() {
        return new GetFraudTransactions();
    }

    /**
     * Create an instance of {@link GetVirtualCardResponse }
     * 
     */
    public GetVirtualCardResponse createGetVirtualCardResponse() {
        return new GetVirtualCardResponse();
    }

    /**
     * Create an instance of {@link GetSettlementsResponse }
     * 
     */
    public GetSettlementsResponse createGetSettlementsResponse() {
        return new GetSettlementsResponse();
    }

    /**
     * Create an instance of {@link GetSettlementFee }
     * 
     */
    public GetSettlementFee createGetSettlementFee() {
        return new GetSettlementFee();
    }

    /**
     * Create an instance of {@link GetSettlementBatch }
     * 
     */
    public GetSettlementBatch createGetSettlementBatch() {
        return new GetSettlementBatch();
    }

    /**
     * Create an instance of {@link GetSettlements }
     * 
     */
    public GetSettlements createGetSettlements() {
        return new GetSettlements();
    }

    /**
     * Create an instance of {@link GetTransactionListResponse }
     * 
     */
    public GetTransactionListResponse createGetTransactionListResponse() {
        return new GetTransactionListResponse();
    }

    /**
     * Create an instance of {@link GetVirtualCard }
     * 
     */
    public GetVirtualCard createGetVirtualCard() {
        return new GetVirtualCard();
    }

    /**
     * Create an instance of {@link GetSettlementBatchResponse }
     * 
     */
    public GetSettlementBatchResponse createGetSettlementBatchResponse() {
        return new GetSettlementBatchResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NewBatchResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Borgun/Heimir/pub/ws/Authorization", name = "newBatchResponse")
    public JAXBElement<NewBatchResponse> createNewBatchResponse(NewBatchResponse value) {
        return new JAXBElement<NewBatchResponse>(_NewBatchResponse_QNAME, NewBatchResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetActionCodeTexts }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Borgun/Heimir/pub/ws/Authorization", name = "getActionCodeTexts")
    public JAXBElement<GetActionCodeTexts> createGetActionCodeTexts(GetActionCodeTexts value) {
        return new JAXBElement<GetActionCodeTexts>(_GetActionCodeTexts_QNAME, GetActionCodeTexts.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NewBatch }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Borgun/Heimir/pub/ws/Authorization", name = "newBatch")
    public JAXBElement<NewBatch> createNewBatch(NewBatch value) {
        return new JAXBElement<NewBatch>(_NewBatch_QNAME, NewBatch.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDisputedTransactionsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Borgun/Heimir/pub/ws/Authorization", name = "getDisputedTransactionsResponse")
    public JAXBElement<GetDisputedTransactionsResponse> createGetDisputedTransactionsResponse(GetDisputedTransactionsResponse value) {
        return new JAXBElement<GetDisputedTransactionsResponse>(_GetDisputedTransactionsResponse_QNAME, GetDisputedTransactionsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveBlacklistedCardResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Borgun/Heimir/pub/ws/Authorization", name = "removeBlacklistedCardResponse")
    public JAXBElement<RemoveBlacklistedCardResponse> createRemoveBlacklistedCardResponse(RemoveBlacklistedCardResponse value) {
        return new JAXBElement<RemoveBlacklistedCardResponse>(_RemoveBlacklistedCardResponse_QNAME, RemoveBlacklistedCardResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFraudTransactionsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Borgun/Heimir/pub/ws/Authorization", name = "getFraudTransactionsResponse")
    public JAXBElement<GetFraudTransactionsResponse> createGetFraudTransactionsResponse(GetFraudTransactionsResponse value) {
        return new JAXBElement<GetFraudTransactionsResponse>(_GetFraudTransactionsResponse_QNAME, GetFraudTransactionsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSettlementTransactionsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Borgun/Heimir/pub/ws/Authorization", name = "getSettlementTransactionsResponse")
    public JAXBElement<GetSettlementTransactionsResponse> createGetSettlementTransactionsResponse(GetSettlementTransactionsResponse value) {
        return new JAXBElement<GetSettlementTransactionsResponse>(_GetSettlementTransactionsResponse_QNAME, GetSettlementTransactionsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSettlementSummaryResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Borgun/Heimir/pub/ws/Authorization", name = "getSettlementSummaryResponse")
    public JAXBElement<GetSettlementSummaryResponse> createGetSettlementSummaryResponse(GetSettlementSummaryResponse value) {
        return new JAXBElement<GetSettlementSummaryResponse>(_GetSettlementSummaryResponse_QNAME, GetSettlementSummaryResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSettlementFeeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Borgun/Heimir/pub/ws/Authorization", name = "getSettlementFeeResponse")
    public JAXBElement<GetSettlementFeeResponse> createGetSettlementFeeResponse(GetSettlementFeeResponse value) {
        return new JAXBElement<GetSettlementFeeResponse>(_GetSettlementFeeResponse_QNAME, GetSettlementFeeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendDetailDataResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Borgun/Heimir/pub/ws/Authorization", name = "sendDetailDataResponse")
    public JAXBElement<SendDetailDataResponse> createSendDetailDataResponse(SendDetailDataResponse value) {
        return new JAXBElement<SendDetailDataResponse>(_SendDetailDataResponse_QNAME, SendDetailDataResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetActionCodeTextsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Borgun/Heimir/pub/ws/Authorization", name = "getActionCodeTextsResponse")
    public JAXBElement<GetActionCodeTextsResponse> createGetActionCodeTextsResponse(GetActionCodeTextsResponse value) {
        return new JAXBElement<GetActionCodeTextsResponse>(_GetActionCodeTextsResponse_QNAME, GetActionCodeTextsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDisputedTransactions }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Borgun/Heimir/pub/ws/Authorization", name = "getDisputedTransactions")
    public JAXBElement<GetDisputedTransactions> createGetDisputedTransactions(GetDisputedTransactions value) {
        return new JAXBElement<GetDisputedTransactions>(_GetDisputedTransactions_QNAME, GetDisputedTransactions.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSettlementChargeback }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Borgun/Heimir/pub/ws/Authorization", name = "getSettlementChargeback")
    public JAXBElement<GetSettlementChargeback> createGetSettlementChargeback(GetSettlementChargeback value) {
        return new JAXBElement<GetSettlementChargeback>(_GetSettlementChargeback_QNAME, GetSettlementChargeback.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendDetailData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Borgun/Heimir/pub/ws/Authorization", name = "sendDetailData")
    public JAXBElement<SendDetailData> createSendDetailData(SendDetailData value) {
        return new JAXBElement<SendDetailData>(_SendDetailData_QNAME, SendDetailData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTransactionList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Borgun/Heimir/pub/ws/Authorization", name = "getTransactionList")
    public JAXBElement<GetTransactionList> createGetTransactionList(GetTransactionList value) {
        return new JAXBElement<GetTransactionList>(_GetTransactionList_QNAME, GetTransactionList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CancelAuthorizationInput }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Borgun/Heimir/pub/ws/Authorization", name = "cancelAuthorizationInput")
    public JAXBElement<CancelAuthorizationInput> createCancelAuthorizationInput(CancelAuthorizationInput value) {
        return new JAXBElement<CancelAuthorizationInput>(_CancelAuthorizationInput_QNAME, CancelAuthorizationInput.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CancelAuthorizationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Borgun/Heimir/pub/ws/Authorization", name = "cancelAuthorizationResponse")
    public JAXBElement<CancelAuthorizationResponse> createCancelAuthorizationResponse(CancelAuthorizationResponse value) {
        return new JAXBElement<CancelAuthorizationResponse>(_CancelAuthorizationResponse_QNAME, CancelAuthorizationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAuthorizationOutput }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Borgun/Heimir/pub/ws/Authorization", name = "getAuthorizationOutput")
    public JAXBElement<GetAuthorizationOutput> createGetAuthorizationOutput(GetAuthorizationOutput value) {
        return new JAXBElement<GetAuthorizationOutput>(_GetAuthorizationOutput_QNAME, GetAuthorizationOutput.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAuthorizationInput }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Borgun/Heimir/pub/ws/Authorization", name = "getAuthorizationInput")
    public JAXBElement<GetAuthorizationInput> createGetAuthorizationInput(GetAuthorizationInput value) {
        return new JAXBElement<GetAuthorizationInput>(_GetAuthorizationInput_QNAME, GetAuthorizationInput.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSettlementChargebackResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Borgun/Heimir/pub/ws/Authorization", name = "getSettlementChargebackResponse")
    public JAXBElement<GetSettlementChargebackResponse> createGetSettlementChargebackResponse(GetSettlementChargebackResponse value) {
        return new JAXBElement<GetSettlementChargebackResponse>(_GetSettlementChargebackResponse_QNAME, GetSettlementChargebackResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetBlacklistedCard }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Borgun/Heimir/pub/ws/Authorization", name = "setBlacklistedCard")
    public JAXBElement<SetBlacklistedCard> createSetBlacklistedCard(SetBlacklistedCard value) {
        return new JAXBElement<SetBlacklistedCard>(_SetBlacklistedCard_QNAME, SetBlacklistedCard.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetBlacklistedCardResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Borgun/Heimir/pub/ws/Authorization", name = "setBlacklistedCardResponse")
    public JAXBElement<SetBlacklistedCardResponse> createSetBlacklistedCardResponse(SetBlacklistedCardResponse value) {
        return new JAXBElement<SetBlacklistedCardResponse>(_SetBlacklistedCardResponse_QNAME, SetBlacklistedCardResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVirtualCardResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Borgun/Heimir/pub/ws/Authorization", name = "getVirtualCardResponse")
    public JAXBElement<GetVirtualCardResponse> createGetVirtualCardResponse(GetVirtualCardResponse value) {
        return new JAXBElement<GetVirtualCardResponse>(_GetVirtualCardResponse_QNAME, GetVirtualCardResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFraudTransactions }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Borgun/Heimir/pub/ws/Authorization", name = "getFraudTransactions")
    public JAXBElement<GetFraudTransactions> createGetFraudTransactions(GetFraudTransactions value) {
        return new JAXBElement<GetFraudTransactions>(_GetFraudTransactions_QNAME, GetFraudTransactions.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSettlementSummary }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Borgun/Heimir/pub/ws/Authorization", name = "getSettlementSummary")
    public JAXBElement<GetSettlementSummary> createGetSettlementSummary(GetSettlementSummary value) {
        return new JAXBElement<GetSettlementSummary>(_GetSettlementSummary_QNAME, GetSettlementSummary.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveBlacklistedCard }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Borgun/Heimir/pub/ws/Authorization", name = "removeBlacklistedCard")
    public JAXBElement<RemoveBlacklistedCard> createRemoveBlacklistedCard(RemoveBlacklistedCard value) {
        return new JAXBElement<RemoveBlacklistedCard>(_RemoveBlacklistedCard_QNAME, RemoveBlacklistedCard.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSettlementTransactions }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Borgun/Heimir/pub/ws/Authorization", name = "getSettlementTransactions")
    public JAXBElement<GetSettlementTransactions> createGetSettlementTransactions(GetSettlementTransactions value) {
        return new JAXBElement<GetSettlementTransactions>(_GetSettlementTransactions_QNAME, GetSettlementTransactions.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSettlementBatchResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Borgun/Heimir/pub/ws/Authorization", name = "getSettlementBatchResponse")
    public JAXBElement<GetSettlementBatchResponse> createGetSettlementBatchResponse(GetSettlementBatchResponse value) {
        return new JAXBElement<GetSettlementBatchResponse>(_GetSettlementBatchResponse_QNAME, GetSettlementBatchResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTransactionListResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Borgun/Heimir/pub/ws/Authorization", name = "getTransactionListResponse")
    public JAXBElement<GetTransactionListResponse> createGetTransactionListResponse(GetTransactionListResponse value) {
        return new JAXBElement<GetTransactionListResponse>(_GetTransactionListResponse_QNAME, GetTransactionListResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVirtualCard }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Borgun/Heimir/pub/ws/Authorization", name = "getVirtualCard")
    public JAXBElement<GetVirtualCard> createGetVirtualCard(GetVirtualCard value) {
        return new JAXBElement<GetVirtualCard>(_GetVirtualCard_QNAME, GetVirtualCard.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSettlementBatch }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Borgun/Heimir/pub/ws/Authorization", name = "getSettlementBatch")
    public JAXBElement<GetSettlementBatch> createGetSettlementBatch(GetSettlementBatch value) {
        return new JAXBElement<GetSettlementBatch>(_GetSettlementBatch_QNAME, GetSettlementBatch.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSettlements }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Borgun/Heimir/pub/ws/Authorization", name = "getSettlements")
    public JAXBElement<GetSettlements> createGetSettlements(GetSettlements value) {
        return new JAXBElement<GetSettlements>(_GetSettlements_QNAME, GetSettlements.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSettlementFee }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Borgun/Heimir/pub/ws/Authorization", name = "getSettlementFee")
    public JAXBElement<GetSettlementFee> createGetSettlementFee(GetSettlementFee value) {
        return new JAXBElement<GetSettlementFee>(_GetSettlementFee_QNAME, GetSettlementFee.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSettlementsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Borgun/Heimir/pub/ws/Authorization", name = "getSettlementsResponse")
    public JAXBElement<GetSettlementsResponse> createGetSettlementsResponse(GetSettlementsResponse value) {
        return new JAXBElement<GetSettlementsResponse>(_GetSettlementsResponse_QNAME, GetSettlementsResponse.class, null, value);
    }

}

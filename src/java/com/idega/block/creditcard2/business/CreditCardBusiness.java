package com.idega.block.creditcard2.business;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.transaction.TransactionManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.idega.block.creditcard2.data.beans.BorgunMerchant;
import com.idega.block.creditcard2.data.beans.DummyMerchant;
import com.idega.block.creditcard2.data.beans.KortathjonustanMerchant;
import com.idega.block.creditcard2.data.beans.TPosMerchant;
import com.idega.block.creditcard2.data.dao.AuthorisationEntriesDAO;
import com.idega.block.creditcard2.data.dao.MerchantDAO;
import com.idega.block.creditcard2.data.dao.impl.BorgunAuthorisationEntryDAO;
import com.idega.block.creditcard2.data.dao.impl.BorgunMerchantDAO;
import com.idega.block.creditcard2.data.dao.impl.DummyAuthorisationEntryDAO;
import com.idega.block.creditcard2.data.dao.impl.DummyMerchantDAO;
import com.idega.block.creditcard2.data.dao.impl.KortathjonustanAuthorisationEntryDAO;
import com.idega.block.creditcard2.data.dao.impl.KortathjonustanMerchantDAO;
import com.idega.block.creditcard2.data.dao.impl.TPosAuthorisationEntryDAO;
import com.idega.block.creditcard2.data.dao.impl.TPosMerchantDAO;
import com.idega.block.trade.data.bean.CreditCardInformation;
import com.idega.block.trade.data.dao.CreditCardInformationDAO;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.transaction.IdegaTransactionManager;
import com.idega.user.dao.GroupDAO;
import com.idega.user.data.bean.Group;
import com.idega.util.Encrypter;
import com.idega.util.IWTimestamp;
import com.idega.util.expression.ELUtil;

@Service(CreditCardBusiness.BEAN_NAME)
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class CreditCardBusiness {

	@Autowired
	private CreditCardInformationDAO creditCardInformationDAO;

	public final static String CARD_TYPE_VISA = CreditCardType.VISA.name();
	public final static String CARD_TYPE_ELECTRON = CreditCardType.ELECTRON.name();
	public final static String CARD_TYPE_DINERS = CreditCardType.DINERS.name();
	public final static String CARD_TYPE_DANKORT = CreditCardType.DANKORT.name();
	public final static String CARD_TYPE_MASTERCARD = CreditCardType.MASTERCARD.name();
	public final static String CARD_TYPE_JCB = CreditCardType.JCB.name();
	public final static String CARD_TYPE_AMERICAN_EXPRESS = CreditCardType.AMERICAN_EXRESS.name();

	public final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.creditcard";

	private final static String PROPERTY_KORTATHJONUSTAN_HOST_NAME = "kortathjonustan_host_name";
	private final static String PROPERTY_KORTATHJONUSTAN_HOST_PORT = "kortathjonustan_host_port";
	private final static String PROPERTY_KORTATHJONUSTAN_KEYSTORE = "kortathjonustan_keystore";
	private final static String PROPERTY_KORTATHJONUSTAN_KEYSTORE_PASS = "kortathjonustan_keystore_pass";

	public final static int CLIENT_TYPE_TPOS = 1;
	public final static int CLIENT_TYPE_KORTATHJONUSTAN = 2;
	public final static int CLIENT_TYPE_DUMMY = 3;
	public final static int CLIENT_TYPE_BORGUN = 4;

	public static final String BEAN_NAME = "CreditCardBusiness";

	public CreditCardInformationDAO getCreditCardInformationDAO(){
		if (creditCardInformationDAO==null) ELUtil.getInstance().autowire(this);
		return creditCardInformationDAO;
	}

	public void setCreditCardInformationDAO(CreditCardInformationDAO creditCardInformationDAO){
		this.creditCardInformationDAO = creditCardInformationDAO;
	}

	public MerchantDAO<TPosMerchant> getTposMerchantDao() {
		return ELUtil.getInstance().getBean(TPosMerchantDAO.BEAN_NAME);
	}

	public MerchantDAO<KortathjonustanMerchant> getKortathjonustanMerchantDao() {
		return ELUtil.getInstance().getBean(KortathjonustanMerchantDAO.BEAN_NAME);
	}

	public MerchantDAO<DummyMerchant> getDummyMerchantDao() {
		return ELUtil.getInstance().getBean(DummyMerchantDAO.BEAN_NAME);
	}

	public TPosAuthorisationEntryDAO getTposAuthorisationEntryDAO() {
		return ELUtil.getInstance().getBean(TPosAuthorisationEntryDAO.BEAN_NAME);
	}

	public KortathjonustanAuthorisationEntryDAO getKortathjonustanAuthorisationEntryDAO() {
		return ELUtil.getInstance().getBean(KortathjonustanAuthorisationEntryDAO.BEAN_NAME);
	}

	public DummyAuthorisationEntryDAO getDummyAuthorisationEntryDAO() {
		return ELUtil.getInstance().getBean(DummyAuthorisationEntryDAO.BEAN_NAME);
	}

	public AuthorisationEntriesDAO<?> getAuthorisationEntriesDAO(int clientType){
		if (clientType > 0) {
			if (clientType == CLIENT_TYPE_KORTATHJONUSTAN) {
				return getKortathjonustanAuthorisationEntryDAO();
			}
			else if (clientType == CLIENT_TYPE_TPOS) {
				return getTposAuthorisationEntryDAO();
			}
			else if (clientType == CLIENT_TYPE_DUMMY) {
				return getDummyAuthorisationEntryDAO();
			} else if (clientType == CLIENT_TYPE_BORGUN){
				return getBorgunAuthorisationEntryDAO();
			}
		}
		return null;
	}

	public AuthorisationEntriesDAO<?> getAuthorisationEntriesDAO(CreditCardInformation info){
		if (CreditCardMerchant.MERCHANT_TYPE_TPOS.equals(info.getType())) {
			return getTposAuthorisationEntryDAO();
		}
		else if (CreditCardMerchant.MERCHANT_TYPE_KORTHATHJONUSTAN.equals(info.getType())) {
			return getKortathjonustanAuthorisationEntryDAO();
		}
		else if (CreditCardMerchant.MERCHANT_TYPE_DUMMY.equals(info.getType())) {
			return getDummyAuthorisationEntryDAO();
		} else if (CreditCardMerchant.MERCHANT_TYPE_BORGUN.equals(info.getType())) {
			return getBorgunAuthorisationEntryDAO();
		}
		return null;
	}

	public MerchantDAO<?> getCreditCardMerchantDAO(CreditCardInformation ccInfo){
		String type = ccInfo.getType();
		if (CreditCardMerchant.MERCHANT_TYPE_TPOS.equals(type)) {
			return getTposMerchantDao();
		}
		else if (CreditCardMerchant.MERCHANT_TYPE_KORTHATHJONUSTAN.equals(type)) {
			return getKortathjonustanMerchantDao();
		}
		else if (CreditCardMerchant.MERCHANT_TYPE_DUMMY.equals(type)) {
			return getDummyMerchantDao();
		}
		else if (CreditCardMerchant.MERCHANT_TYPE_BORGUN.equals(type)) {
			return getBorgunMerchantDao();
		}

		return null;
	}

	public MerchantDAO<?> getCreditCardMerchantDAO(String type){
		if (CreditCardMerchant.MERCHANT_TYPE_TPOS.equals(type)) {
			return getTposMerchantDao();
		}
		else if (CreditCardMerchant.MERCHANT_TYPE_KORTHATHJONUSTAN.equals(type)) {
			return getKortathjonustanMerchantDao();
		}
		else if (CreditCardMerchant.MERCHANT_TYPE_DUMMY.equals(type)) {
			return getDummyMerchantDao();
		}
		else if (CreditCardMerchant.MERCHANT_TYPE_BORGUN.equals(type)) {
			return getBorgunMerchantDao();
		}
		return null;
	}

	public enum CreditCardType {
		VISA, ELECTRON, DINERS, DANKORT, MASTERCARD, JCB, AMERICAN_EXRESS;
	}

	private IWApplicationContext iwac;

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	@SuppressWarnings("unchecked")
	public List<CreditCardAuthorizationEntry> getAuthorizationEntries(int clientType, String merchantID, IWTimestamp from, IWTimestamp to) {
		return getAuthorisationEntriesDAO(clientType).findByDates(from.getSQLDate(), to.getSQLDate());
	}

	public DropdownMenu getCreditCardTypes(CreditCardClient client, IWResourceBundle iwrb, String dropdownName) {
		Collection<String> types = client.getValidCardTypes();
		if (types != null && !types.isEmpty()) {
			DropdownMenu menu = new DropdownMenu(dropdownName);
			Iterator<String> iter = types.iterator();
			String type;
			menu.addMenuElement("-1", iwrb.getLocalizedString("select_one", "Select one:"));
			while (iter.hasNext()) {
				type = iter.next();
				menu.addMenuElement(type, iwrb.getLocalizedString("card_type." + type, type));
			}
			return menu;
		}
		return null;
	}

	private IWBundle getBundle(){
		return IWContext.getCurrentInstance().getApplicationContext().getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER);
	}

	public Collection<Image> getCreditCardTypeImages(CreditCardClient client) {
		Collection<String> types = client.getValidCardTypes();
		Collection<Image> images = new ArrayList<Image>();
		if (types != null && !types.isEmpty()) {
			Iterator<String> iter = types.iterator();
			IWBundle bundle = getBundle();
			String type;
			while (iter.hasNext()) {
				type = iter.next();
				if (CreditCardBusiness.CARD_TYPE_DANKORT.equals(type)) {
					images.add(bundle.getImage("logos/dankort.gif"));
				}
				else if (CreditCardBusiness.CARD_TYPE_DINERS.equals(type)) {
					images.add(bundle.getImage("logos/diners.gif"));
				}
				else if (CreditCardBusiness.CARD_TYPE_ELECTRON.equals(type)) {
					images.add(bundle.getImage("logos/electron.gif"));
				}
				else if (CreditCardBusiness.CARD_TYPE_JCB.equals(type)) {
					images.add(bundle.getImage("logos/jcb.gif"));
				}
				else if (CreditCardBusiness.CARD_TYPE_MASTERCARD.equals(type)) {
					images.add(bundle.getImage("logos/mastercard.gif"));
				}
				else if (CreditCardBusiness.CARD_TYPE_VISA.equals(type)) {
					images.add(bundle.getImage("logos/visa.gif"));
				}
				else if (CreditCardBusiness.CARD_TYPE_AMERICAN_EXPRESS.equals(type)) {
					images.add(bundle.getImage("logos/ae.gif"));
				}
			}
		}

		return images;
	}


	public CreditCardClient getCreditCardClient(Supplier supplier, IWTimestamp stamp) throws Exception {

		CreditCardMerchant merchant = getCreditCardMerchant(supplier, stamp);
		CreditCardClient client = getCreditCardClient(merchant);

		return client;
	}


	public CreditCardClient getCreditCardClient(Group supplierManager, IWTimestamp stamp) throws Exception {
		CreditCardMerchant m = getCreditCardMerchant(supplierManager, stamp);
		return getCreditCardClient(m);
	}


	public CreditCardClient getCreditCardClient(CreditCardMerchant merchant) throws Exception {
		if (merchant != null && merchant.getType() != null) {
			if (CreditCardMerchant.MERCHANT_TYPE_TPOS.equals(merchant.getType())) {
				return new TPosClient(getIWApplicationContext(), merchant);
			}
			else if (CreditCardMerchant.MERCHANT_TYPE_KORTHATHJONUSTAN.equals(merchant.getType())) {
				String hostName = getBundle().getProperty(PROPERTY_KORTATHJONUSTAN_HOST_NAME);
				String hostPort = getBundle().getProperty(PROPERTY_KORTATHJONUSTAN_HOST_PORT);
				String keystore = getBundle().getProperty(PROPERTY_KORTATHJONUSTAN_KEYSTORE);
				String keystorePass = getBundle().getProperty(PROPERTY_KORTATHJONUSTAN_KEYSTORE_PASS);

				return new KortathjonustanCreditCardClient(getIWApplicationContext(), hostName, Integer.parseInt(hostPort), keystore, keystorePass, merchant);
			}
			else if (CreditCardMerchant.MERCHANT_TYPE_DUMMY.equals(merchant.getType())) {
				return new DummyCreditCardClient(getIWApplicationContext());
			}
		}
		return null;
		// Default client
		// return new TPosClient(getIWApplicationContext());
	}


	public CreditCardMerchant getCreditCardMerchant(String merchantPK, String merchantType) {
		CreditCardInformation ccInfo = getCreditCardInformation(merchantPK, merchantType);
		return getCreditCardMerchant(ccInfo);
	}


	public CreditCardMerchant getCreditCardMerchant(Supplier supplier, IWTimestamp stamp) {
		CreditCardInformation ccInfo = getCreditCardInformation(supplier, stamp);
		return getCreditCardMerchant(ccInfo);
	}


	public CreditCardMerchant getCreditCardMerchant(Group supplierManager, IWTimestamp stamp) {
		CreditCardInformation ccInfo = getCreditCardInformation(supplierManager, stamp);
		return getCreditCardMerchant(ccInfo);
	}


	public CreditCardMerchant getCreditCardMerchant(CreditCardInformation ccInfo) {
		if (ccInfo != null) {
			return getCreditCardMerchantDAO(ccInfo).findById(Integer.parseInt(ccInfo.getMerchantPK()));
		}
		return null;
	}


	public CreditCardInformation getCreditCardInformation(String merchantPK, String merchantType) {
		CreditCardInformationDAO ccInfoHome = ELUtil.getInstance().getBean(CreditCardInformationDAO.BEAN_NAME);
		return ccInfoHome.findByMerchant(merchantPK, merchantType);
	}


	public CreditCardInformation getCreditCardInformation(Supplier supplier, IWTimestamp stamp) {
		try {
			Timestamp toCheck = null;
			if (stamp != null) {
				toCheck = stamp.getTimestamp();
			}
			else {
				toCheck = IWTimestamp.getTimestampRightNow();
			}

			CreditCardInformation ccInfo = null;

			// Checking for merchants configured to this supplier
			Collection coll = this.getCreditCardInformations(supplier);
			if (coll != null) {
				Iterator iter = coll.iterator();
				ccInfo = getCreditCardInformationInUse(iter, toCheck);
			}

			// Checking for merchants configured to this supplier's supplierManager
			if (ccInfo == null) {
				GroupDAO grpDAO = ELUtil.getInstance().getBean("groupDAO");
				Group group = grpDAO.findGroup((Integer)supplier.getSupplierManager().getPrimaryKey());
				ccInfo = getCreditCardInformation(group, stamp);
			}

			return ccInfo;

		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}
		return null;
	}

	/**
	 * @param supplier
	 * @param toCheck
	 * @param ccInfo
	 * @return
	 * @throws FinderException
	 * @throws IDOLookupException
	 */

	public CreditCardInformation getCreditCardInformation(Group supplierManager, IWTimestamp stamp) {
		Timestamp toCheck = null;
		if (stamp != null) {
			toCheck = stamp.getTimestamp();
		}
		else {
			toCheck = IWTimestamp.getTimestampRightNow();
		}

		CreditCardInformation ccInfo = null;
		try {
			Collection coll;
			coll = getCreditCardInformations(supplierManager);
			Iterator iter = coll.iterator();
			ccInfo = getCreditCardInformationInUse(iter, toCheck);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return ccInfo;
	}

	private CreditCardInformation getCreditCardInformationInUse(Iterator iter, Timestamp toCheck) {
		Timestamp starts = null;
		Timestamp ends = null;
		CreditCardInformation info;
		CreditCardMerchant merchant;
		while (iter.hasNext()) {
			info = (CreditCardInformation) iter.next();
			merchant = getCreditCardMerchant(info);
			if (merchant != null && !merchant.getIsDeleted()) {
				if (merchant.getStartDate()!=null)
					starts = new Timestamp(merchant.getStartDate().getTime());
				if (merchant.getEndDate()!=null)
					ends = new Timestamp(merchant.getEndDate().getTime());

				if (ends == null) {
					return info;
				}
				else if (starts != null && starts.before(toCheck) && ends.after(toCheck)) {
					return info;
				}
				else if (starts == null) {
					return info;
				}
			}
		}
		return null;
	}


	public CreditCardMerchant getCreditCardMerchant(Supplier supplier, Object PK) {
		try {
			Collection coll = supplier.getCreditCardInformation();
			CreditCardMerchant returner = getCreditCardMerchant(PK, coll);
			return returner;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	public CreditCardMerchant getCreditCardMerchant(Group supplierManager, Object PK) {
		try {
			Collection coll = getCreditCardInformations(supplierManager);
			CreditCardMerchant returner = getCreditCardMerchant(PK, coll);
			return returner;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param PK
	 * @param creditcardInformations
	 * @param returner
	 * @return
	 */
	private CreditCardMerchant getCreditCardMerchant(Object PK, Collection creditcardInformations) {
		if (creditcardInformations != null) {
			Iterator iter = creditcardInformations.iterator();
			CreditCardInformation info;
			CreditCardMerchant merchant;
			while (iter.hasNext()) {
				info = (CreditCardInformation) iter.next();
				merchant = getCreditCardMerchant(info);
				if (merchant != null && merchant.getPrimaryKey().toString().equals(PK.toString())) {
					return merchant;
				}
			}
		}
		return null;
	}


	public CreditCardMerchant createCreditCardMerchant(String type) {
			if (CreditCardMerchant.MERCHANT_TYPE_TPOS.equals(type)) {
				return new TPosMerchant();
			}
			else if (CreditCardMerchant.MERCHANT_TYPE_KORTHATHJONUSTAN.equals(type)) {
				return new KortathjonustanMerchant();
			}
			else if (CreditCardMerchant.MERCHANT_TYPE_DUMMY.equals(type)) {
				return new DummyMerchant();
			}
			return null;
	}


	public void addCreditCardMerchant(Group supplierManager, CreditCardMerchant merchant) throws CreateException {
		addCreditCardMerchant((Object) supplierManager, merchant);
	}


	public void addCreditCardMerchant(Supplier supplier, CreditCardMerchant merchant) throws CreateException {
		addCreditCardMerchant((Object) supplier, merchant);
	}

	private void addCreditCardMerchant(Object merchantType, CreditCardMerchant merchant) throws CreateException {
		TransactionManager t = IdegaTransactionManager.getInstance();
		try {
			t.begin();
			boolean isSupplier = (merchantType instanceof Supplier);
			boolean isSupplierManager = (merchantType instanceof Group);

			// Setting other merchants to deleted
			Collection coll = null;
			if (isSupplier) {
				coll = ((Supplier) merchantType).getCreditCardInformation();
			}
			else if (isSupplierManager) {
				coll = getCreditCardInformations((Group) merchantType);
			}

			if (coll != null) {
				Iterator iter = coll.iterator();
				CreditCardInformation info;
				CreditCardMerchant tmpMerchant = null;
				while (iter.hasNext()) {
					info = (CreditCardInformation) iter.next();

					if (isSupplier) {
						tmpMerchant = getCreditCardMerchant((Supplier) merchantType, new Integer(info.getMerchantPK()));
					}
					else if (isSupplierManager) {
						tmpMerchant = getCreditCardMerchant((Group) merchantType, new Integer(info.getMerchantPK()));
					}
					if (tmpMerchant != null && !tmpMerchant.getIsDeleted()) {
						MerchantDAO merchantDAO = getCreditCardMerchantDAO(tmpMerchant.getType());
						merchantDAO.removeMerchant(tmpMerchant);
					}
				}
			}

			CreditCardInformation info = new CreditCardInformation();
			info.setType(merchant.getType());
			info.setMerchantPK(merchant.getPrimaryKey().toString());
			if (isSupplierManager) {
				info.setSupplierManager((Group) merchantType);
			}

			getCreditCardInformationDAO().store(info);


			if (isSupplier) {
				((Supplier) merchantType).addCreditCardInformationPK(info.getId());
			}

			t.commit();
		}
		catch (Exception e) {
			try {
				t.rollback();
			}
			catch (Exception e1) {
				e1.printStackTrace();
				throw new CreateException(e.getMessage());
			}
			throw new CreateException(e.getMessage());
		}
	}

	public Collection getCreditCardInformations(Supplier supplier) throws IDORelationshipException {
		Collection coll = supplier.getCreditCardInformation();
		if (coll == null || coll.isEmpty()) {
			int TPosID = supplier.getTPosMerchantId();
			if (TPosID > 0) {
				try {
					System.out.println("---- Starting backwards.... -----");
					System.out.println("---- ... TPosID = " + TPosID + " -----");

					TPosMerchant merchant = (TPosMerchant) getTposMerchantDao().findById(new Integer(TPosID));
					addCreditCardMerchant(supplier, merchant);
					log("CreditCardBusiness : backwards compatability fix for CreditCard merchant");
					return getCreditCardInformations(supplier);
				}
				catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return coll;
	}

	public Collection getCreditCardInformations(Group supplierManager) throws FinderException, IDOLookupException {
		CreditCardInformationDAO ccInfoHome = ELUtil.getInstance().getBean(CreditCardInformationDAO.BEAN_NAME);
		return ccInfoHome.findBySupplierManager(supplierManager);
	}

	public static String encodeCreditCardNumber(String originalNumber) throws IllegalArgumentException {
		if (originalNumber != null && originalNumber.length() >= 10) {
			int length = originalNumber.length();
			String enc = Encrypter.encryptOneWay(originalNumber.substring(length - 10, length));

			return hexEncode(enc);

		}
		throw new IllegalArgumentException("Number must be at least 10 characters long");
	}

	private static String hexEncode(String enc) {
		try {
			String str = "";
			char[] pass = enc.toCharArray();
			for (int i = 0; i < pass.length; i++) {
				String hex = Integer.toHexString(pass[i]);
				while (hex.length() < 2) {
					String s = "0";
					s += hex;
					hex = s;
				}
				str += hex;
			}
			if (str.equals("") && !enc.equals("")) {
				str = null;
			}

			return str;
		}
		catch (Exception ex) {
			ex.printStackTrace(System.err);
			return null;
		}
	}


	public boolean verifyCreditCardNumber(String numberToCheck, CreditCardAuthorizationEntry entry) throws IllegalArgumentException {
		if (numberToCheck != null && numberToCheck.length() >= 10) {
			int length = numberToCheck.length();
			numberToCheck = numberToCheck.substring(length - 10, length);

			String hex = hexEncode(Encrypter.encryptOneWay(numberToCheck));

			return hex.equals(entry.getCardNumber());
		}
		throw new IllegalArgumentException("Number must be at least 10 characters long");
	}


	public CreditCardAuthorizationEntry getAuthorizationEntry(Group supplierManager, String authorizationCode, IWTimestamp stamp) {
		CreditCardInformation info = getCreditCardInformation(supplierManager, stamp);
		return getAuthorizationEntry(info, authorizationCode, stamp);
	}


	public CreditCardAuthorizationEntry getAuthorizationEntry(Supplier supplier, String authorizationCode, IWTimestamp stamp) {
		CreditCardInformation info = getCreditCardInformation(supplier, stamp);
		CreditCardAuthorizationEntry entry = getAuthorizationEntry(info, authorizationCode, stamp);
		if (entry == null) {
			Group group = null;
			if ((supplier != null) && (supplier.getSupplierManager()!=null) && (supplier.getSupplierManager().getPrimaryKey()!=null))	{
				if (supplier.getSupplierManager().getPrimaryKey() instanceof Integer){
					GroupDAO grpDAO = ELUtil.getInstance().getBean("groupDAO");
					group = grpDAO.findGroup((Integer)supplier.getSupplierManager().getPrimaryKey());
				}
				entry = getAuthorizationEntry(group, authorizationCode, stamp);
			}
		}
		return entry;
	}


	public CreditCardAuthorizationEntry getAuthorizationEntry(CreditCardInformation info, String authorizationCode, IWTimestamp stamp) {
		return getAuthorisationEntriesDAO(info).findByAuthorizationCode(authorizationCode, stamp.getSQLDate());
	}


	public boolean getUseCVC(CreditCardClient client) {
		return !(client instanceof TPosClient);
	}


	public boolean getUseCVC(CreditCardMerchant merchant) {
		if (merchant != null) {
			return !CreditCardMerchant.MERCHANT_TYPE_TPOS.equals(merchant.getType());
		}
		return false;
	}


	public boolean getUseCVC(Supplier supplier, IWTimestamp stamp) {
		return getUseCVC(getCreditCardMerchant(supplier, stamp));
	}


	public List<CreditCardAuthorizationEntry> getAllRefunds(IWTimestamp from, IWTimestamp to, int clientType) throws IDOLookupException, FinderException {
		if (clientType > 0) {
			return getAuthorisationEntriesDAO(clientType).findRefunds(from.getSQLDate(), to.getSQLDate());
		}
		return null;
	}

	private Logger getLogger(){
		return Logger.getLogger(getClass().getName());
	}

	private void log(String msg) {
		getLogger().log(Level.INFO,msg);
	}

	private IWApplicationContext getIWApplicationContext(){
		if(this.iwac==null){
			return IWMainApplication.getDefaultIWApplicationContext();
		}
	    return this.iwac;
	  }

	public MerchantDAO<BorgunMerchant> getBorgunMerchantDao() {
		return ELUtil.getInstance().getBean(BorgunMerchantDAO.BEAN_NAME);
	}

	public BorgunAuthorisationEntryDAO getBorgunAuthorisationEntryDAO() {
		return ELUtil.getInstance().getBean(BorgunAuthorisationEntryDAO.BEAN_NAME);
	}

}
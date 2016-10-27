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

import com.idega.block.creditcard.business.CreditCardClient;
import com.idega.block.creditcard.data.CreditCardAuthorizationEntry;
import com.idega.block.creditcard.data.CreditCardMerchant;
import com.idega.block.creditcard2.data.beans.BorgunMerchant;
import com.idega.block.creditcard2.data.beans.DummyMerchant;
import com.idega.block.creditcard2.data.beans.KortathjonustanMerchant;
import com.idega.block.creditcard2.data.beans.TPosMerchant;
import com.idega.block.creditcard2.data.beans.ValitorMerchant;
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
import com.idega.block.creditcard2.data.dao.impl.ValitorAuthorisationEntryDAO;
import com.idega.block.creditcard2.data.dao.impl.ValitorMerchantDAO;
import com.idega.block.trade.data.bean.DebitCardInformation;
import com.idega.block.trade.data.dao.DebitCardInformationDAO;
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

@Service(DebitCardBusiness.BEAN_NAME)
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class DebitCardBusiness {

	@Autowired
	private DebitCardInformationDAO debitCardInformationDAO;

	public final static String CARD_TYPE_VISA = DebitCardType.VISA.name();
	public final static String CARD_TYPE_ELECTRON = DebitCardType.ELECTRON.name();
	public final static String CARD_TYPE_DINERS = DebitCardType.DINERS.name();
	public final static String CARD_TYPE_DANKORT = DebitCardType.DANKORT.name();
	public final static String CARD_TYPE_MASTERCARD = DebitCardType.MASTERCARD.name();
	public final static String CARD_TYPE_JCB = DebitCardType.JCB.name();
	public final static String CARD_TYPE_AMERICAN_EXPRESS = DebitCardType.AMERICAN_EXRESS.name();

	public final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.creditcard";

	private final static String PROPERTY_KORTATHJONUSTAN_HOST_NAME = "kortathjonustan_host_name";
	private final static String PROPERTY_KORTATHJONUSTAN_HOST_PORT = "kortathjonustan_host_port";
	private final static String PROPERTY_KORTATHJONUSTAN_KEYSTORE = "kortathjonustan_keystore";
	private final static String PROPERTY_KORTATHJONUSTAN_KEYSTORE_PASS = "kortathjonustan_keystore_pass";

	public final static int CLIENT_TYPE_TPOS = 1;
	public final static int CLIENT_TYPE_KORTATHJONUSTAN = 2;
	public final static int CLIENT_TYPE_DUMMY = 3;
	public final static int CLIENT_TYPE_BORGUN = 4;
	public final static int CLIENT_TYPE_VALITOR = 5;

	public static final String BEAN_NAME = "DebitCardBusiness";

	public DebitCardInformationDAO getDebitCardInformationDAO() {
		if (debitCardInformationDAO == null)
			ELUtil.getInstance().autowire(this);
		return debitCardInformationDAO;
	}

	public void setDebitCardInformationDAO(DebitCardInformationDAO debitCardInformationDAO) {
		this.debitCardInformationDAO = debitCardInformationDAO;
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

	public MerchantDAO<ValitorMerchant> getValitorMerchantDao() {
		return ELUtil.getInstance().getBean(ValitorMerchantDAO.BEAN_NAME);
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

	public ValitorAuthorisationEntryDAO getValitorAuthorisationEntryDAO() {
		return ELUtil.getInstance().getBean(ValitorAuthorisationEntryDAO.BEAN_NAME);
	}

	public AuthorisationEntriesDAO<?> getAuthorisationEntriesDAO(int clientType) {
		if (clientType > 0) {
			if (clientType == CLIENT_TYPE_KORTATHJONUSTAN) {
				return getKortathjonustanAuthorisationEntryDAO();
			} else if (clientType == CLIENT_TYPE_TPOS) {
				return getTposAuthorisationEntryDAO();
			} else if (clientType == CLIENT_TYPE_DUMMY) {
				return getDummyAuthorisationEntryDAO();
			} else if (clientType == CLIENT_TYPE_BORGUN) {
				return getBorgunAuthorisationEntryDAO();
			} else if (clientType == CLIENT_TYPE_VALITOR) {
				return getValitorAuthorisationEntryDAO();
			}
		}
		return null;
	}

	public AuthorisationEntriesDAO<?> getAuthorisationEntriesDAO(DebitCardInformation info) {
		if (CreditCardMerchant.MERCHANT_TYPE_TPOS.equals(info.getType())) {
			return getTposAuthorisationEntryDAO();
		} else if (CreditCardMerchant.MERCHANT_TYPE_KORTHATHJONUSTAN.equals(info.getType())) {
			return getKortathjonustanAuthorisationEntryDAO();
		} else if (CreditCardMerchant.MERCHANT_TYPE_DUMMY.equals(info.getType())) {
			return getDummyAuthorisationEntryDAO();
		} else if (CreditCardMerchant.MERCHANT_TYPE_BORGUN.equals(info.getType())) {
			return getBorgunAuthorisationEntryDAO();
		} else if (CreditCardMerchant.MERCHANT_TYPE_VALITOR.equals(info.getType())) {
			return getValitorAuthorisationEntryDAO();
		}
		return null;
	}

	public MerchantDAO<?> getDebitCardMerchantDAO(DebitCardInformation ccInfo) {
		String type = ccInfo.getType();
		if (CreditCardMerchant.MERCHANT_TYPE_TPOS.equals(type)) {
			return getTposMerchantDao();
		} else if (CreditCardMerchant.MERCHANT_TYPE_KORTHATHJONUSTAN.equals(type)) {
			return getKortathjonustanMerchantDao();
		} else if (CreditCardMerchant.MERCHANT_TYPE_DUMMY.equals(type)) {
			return getDummyMerchantDao();
		} else if (CreditCardMerchant.MERCHANT_TYPE_BORGUN.equals(type)) {
			return getBorgunMerchantDao();
		} else if (CreditCardMerchant.MERCHANT_TYPE_VALITOR.equals(type)) {
			return getValitorMerchantDao();
		}

		return null;
	}

	public MerchantDAO<?> getDebitCardMerchantDAO(String type) {
		if (CreditCardMerchant.MERCHANT_TYPE_TPOS.equals(type)) {
			return getTposMerchantDao();
		} else if (CreditCardMerchant.MERCHANT_TYPE_KORTHATHJONUSTAN.equals(type)) {
			return getKortathjonustanMerchantDao();
		} else if (CreditCardMerchant.MERCHANT_TYPE_DUMMY.equals(type)) {
			return getDummyMerchantDao();
		} else if (CreditCardMerchant.MERCHANT_TYPE_BORGUN.equals(type)) {
			return getBorgunMerchantDao();
		} else if (CreditCardMerchant.MERCHANT_TYPE_VALITOR.equals(type)) {
			return getValitorMerchantDao();
		}
		return null;
	}

	public enum DebitCardType {
		VISA, ELECTRON, DINERS, DANKORT, MASTERCARD, JCB, AMERICAN_EXRESS;
	}

	private IWApplicationContext iwac;

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	@SuppressWarnings("unchecked")
	public List<CreditCardAuthorizationEntry> getAuthorizationEntries(int clientType, String merchantID,
			IWTimestamp from, IWTimestamp to) {
		return getAuthorisationEntriesDAO(clientType).findByDates(from.getSQLDate(), to.getSQLDate());
	}

	public DropdownMenu getDebitCardTypes(CreditCardClient client, IWResourceBundle iwrb, String dropdownName) {
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

	private IWBundle getBundle() {
		return IWContext.getCurrentInstance().getApplicationContext().getIWMainApplication()
				.getBundle(IW_BUNDLE_IDENTIFIER);
	}

	public Collection<Image> getDebitCardTypeImages(CreditCardClient client) {
		Collection<String> types = client.getValidCardTypes();
		Collection<Image> images = new ArrayList<Image>();
		if (types != null && !types.isEmpty()) {
			Iterator<String> iter = types.iterator();
			IWBundle bundle = getBundle();
			String type;
			while (iter.hasNext()) {
				type = iter.next();
				if (DebitCardBusiness.CARD_TYPE_DANKORT.equals(type)) {
					images.add(bundle.getImage("logos/dankort.gif"));
				} else if (DebitCardBusiness.CARD_TYPE_DINERS.equals(type)) {
					images.add(bundle.getImage("logos/diners.gif"));
				} else if (DebitCardBusiness.CARD_TYPE_ELECTRON.equals(type)) {
					images.add(bundle.getImage("logos/electron.gif"));
				} else if (DebitCardBusiness.CARD_TYPE_JCB.equals(type)) {
					images.add(bundle.getImage("logos/jcb.gif"));
				} else if (DebitCardBusiness.CARD_TYPE_MASTERCARD.equals(type)) {
					images.add(bundle.getImage("logos/mastercard.gif"));
				} else if (DebitCardBusiness.CARD_TYPE_VISA.equals(type)) {
					images.add(bundle.getImage("logos/visa.gif"));
				} else if (DebitCardBusiness.CARD_TYPE_AMERICAN_EXPRESS.equals(type)) {
					images.add(bundle.getImage("logos/ae.gif"));
				}
			}
		}

		return images;
	}

	public CreditCardClient getDebitCardClient(Supplier supplier, IWTimestamp stamp) throws Exception {

		CreditCardMerchant merchant = getDebitCardMerchant(supplier, stamp);
		CreditCardClient client = getDebitCardClient(merchant);

		return client;
	}

	public CreditCardClient getDebitCardClient(Group supplierManager, IWTimestamp stamp) throws Exception {
		CreditCardMerchant m = getDebitCardMerchant(supplierManager, stamp);
		return getDebitCardClient(m);
	}

	public CreditCardClient getDebitCardClient(CreditCardMerchant merchant) throws Exception {
		if (merchant != null && merchant.getType() != null) {
			if (CreditCardMerchant.MERCHANT_TYPE_TPOS.equals(merchant.getType())) {
				return new TPosClient(getIWApplicationContext(), merchant);
			} else if (CreditCardMerchant.MERCHANT_TYPE_KORTHATHJONUSTAN.equals(merchant.getType())) {
				String hostName = getBundle().getProperty(PROPERTY_KORTATHJONUSTAN_HOST_NAME);
				String hostPort = getBundle().getProperty(PROPERTY_KORTATHJONUSTAN_HOST_PORT);
				String keystore = getBundle().getProperty(PROPERTY_KORTATHJONUSTAN_KEYSTORE);
				String keystorePass = getBundle().getProperty(PROPERTY_KORTATHJONUSTAN_KEYSTORE_PASS);

				return new KortathjonustanCreditCardClient(getIWApplicationContext(), hostName,
						Integer.parseInt(hostPort), keystore, keystorePass, merchant);
			} else if (CreditCardMerchant.MERCHANT_TYPE_DUMMY.equals(merchant.getType())) {
				return new DummyCreditCardClient(getIWApplicationContext());
			} else if (CreditCardMerchant.MERCHANT_TYPE_BORGUN.equals(merchant.getType())) {
				return new BorgunCreditCardClient(merchant);
			} else if (CreditCardMerchant.MERCHANT_TYPE_VALITOR.equals(merchant.getType())) {
				return new ValitorCreditCardClient(merchant);
			}
		}
		return null;
		// Default client
		// return new TPosClient(getIWApplicationContext());
	}

	public CreditCardMerchant getDebitCardMerchant(String merchantPK, String merchantType) {
		DebitCardInformation ccInfo = getDebitCardInformation(merchantPK, merchantType);
		return getDebitCardMerchant(ccInfo);
	}

	public CreditCardMerchant getDebitCardMerchant(Supplier supplier, IWTimestamp stamp) {
		DebitCardInformation ccInfo = getDebitCardInformation(supplier, stamp);
		return getDebitCardMerchant(ccInfo);
	}

	public CreditCardMerchant getDebitCardMerchant(Group supplierManager, IWTimestamp stamp) {
		DebitCardInformation ccInfo = getDebitCardInformation(supplierManager, stamp);
		return getDebitCardMerchant(ccInfo);
	}

	public CreditCardMerchant getDebitCardMerchant(DebitCardInformation ccInfo) {
		if (ccInfo != null) {
			return getDebitCardMerchantDAO(ccInfo).findById(Integer.parseInt(ccInfo.getMerchantPK()));
		}
		return null;
	}

	public DebitCardInformation getDebitCardInformation(String merchantPK, String merchantType) {
		DebitCardInformationDAO ccInfoHome = ELUtil.getInstance().getBean(DebitCardInformationDAO.BEAN_NAME);
		return ccInfoHome.findByMerchant(merchantPK, merchantType);
	}

	public DebitCardInformation getDebitCardInformation(Supplier supplier, IWTimestamp stamp) {
		try {
			Timestamp toCheck = null;
			if (stamp != null) {
				toCheck = stamp.getTimestamp();
			} else {
				toCheck = IWTimestamp.getTimestampRightNow();
			}

			DebitCardInformation ccInfo = null;

			// Checking for merchants configured to this supplier
			List<DebitCardInformation> coll = this.getDebitCardInformations(supplier);
			if (coll != null) {
				Iterator<DebitCardInformation> iter = coll.iterator();
				ccInfo = getDebitCardInformationInUse(iter, toCheck);
			}

			// Checking for merchants configured to this supplier's
			// supplierManager
			if (ccInfo == null) {
				GroupDAO grpDAO = ELUtil.getInstance().getBean("groupDAO");
				Group group = grpDAO.findGroup((Integer) supplier.getSupplierManager().getPrimaryKey());
				ccInfo = getDebitCardInformation(group, stamp);
			}

			return ccInfo;

		} catch (Exception e) {
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

	public DebitCardInformation getDebitCardInformation(Group supplierManager, IWTimestamp stamp) {
		Timestamp toCheck = null;
		if (stamp != null) {
			toCheck = stamp.getTimestamp();
		} else {
			toCheck = IWTimestamp.getTimestampRightNow();
		}

		DebitCardInformation ccInfo = null;
		try {
			List<DebitCardInformation> coll;
			coll = getDebitCardInformations(supplierManager);
			Iterator<DebitCardInformation> iter = coll.iterator();
			ccInfo = getDebitCardInformationInUse(iter, toCheck);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ccInfo;
	}

	private DebitCardInformation getDebitCardInformationInUse(Iterator<DebitCardInformation> iter,
			Timestamp toCheck) {
		Timestamp starts = null;
		Timestamp ends = null;
		DebitCardInformation info;
		CreditCardMerchant merchant;
		while (iter.hasNext()) {
			info = iter.next();
			merchant = getDebitCardMerchant(info);
			if (merchant != null && !merchant.getIsDeleted()) {
				if (merchant.getStartDate() != null)
					starts = new Timestamp(merchant.getStartDate().getTime());
				if (merchant.getEndDate() != null)
					ends = new Timestamp(merchant.getEndDate().getTime());

				if (ends == null) {
					return info;
				} else if (starts != null && starts.before(toCheck) && ends.after(toCheck)) {
					return info;
				} else if (starts == null) {
					return info;
				}
			}
		}
		return null;
	}

	public CreditCardMerchant getDebitCardMerchant(Group supplierManager, Object PK) {
		try {
			List<DebitCardInformation> coll = getDebitCardInformations(supplierManager);
			CreditCardMerchant returner = getDebitCardMerchant(PK, coll);
			return returner;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private CreditCardMerchant getDebitCardMerchant(Object PK, List<DebitCardInformation> creditcardInformations) {
		for (DebitCardInformation info : creditcardInformations) {
			CreditCardMerchant merchant = getDebitCardMerchant(info);
			if (merchant != null && merchant.getPrimaryKey().toString().equals(PK.toString())) {
				return merchant;
			}
		}
		return null;
	}

	public CreditCardMerchant createDebitCardMerchant(String type) {
		if (CreditCardMerchant.MERCHANT_TYPE_TPOS.equals(type)) {
			return new TPosMerchant();
		} else if (CreditCardMerchant.MERCHANT_TYPE_KORTHATHJONUSTAN.equals(type)) {
			return new KortathjonustanMerchant();
		} else if (CreditCardMerchant.MERCHANT_TYPE_DUMMY.equals(type)) {
			return new DummyMerchant();
		} else if (CreditCardMerchant.MERCHANT_TYPE_BORGUN.equals(type)) {
			return new BorgunMerchant();
		} else if (CreditCardMerchant.MERCHANT_TYPE_VALITOR.equals(type)) {
			return new ValitorMerchant();
		}
		return null;
	}

	public void addDebitCardMerchant(Group supplierManager, CreditCardMerchant merchant) throws CreateException {
		addDebitCardMerchant((Object) supplierManager, merchant);
	}

	public void addDebitCardMerchant(Supplier supplier, CreditCardMerchant merchant) throws CreateException {
		addDebitCardMerchant((Object) supplier, merchant);
	}

	private void addDebitCardMerchant(Object merchantType, CreditCardMerchant merchant) throws CreateException {
		TransactionManager t = IdegaTransactionManager.getInstance();
		try {
			t.begin();
			boolean isSupplier = (merchantType instanceof Supplier);
			boolean isSupplierManager = (merchantType instanceof Group);

			// Setting other merchants to deleted
			List<DebitCardInformation> coll = null;
			if (isSupplier) {
				coll = getDebitCardInformations(((Supplier) merchantType));
			} else if (isSupplierManager) {
				coll = getDebitCardInformations((Group) merchantType);
			}

			if (coll != null) {
				Iterator<DebitCardInformation> iter = coll.iterator();
				DebitCardInformation info;
				CreditCardMerchant tmpMerchant = null;
				while (iter.hasNext()) {
					info = iter.next();

					if (isSupplierManager) {
						tmpMerchant = getDebitCardMerchant((Group) merchantType, new Integer(info.getMerchantPK()));
					}
					if (tmpMerchant != null && !tmpMerchant.getIsDeleted()) {
						MerchantDAO merchantDAO = getDebitCardMerchantDAO(tmpMerchant.getType());
						merchantDAO.removeMerchant(tmpMerchant);
					}
				}
			}

			DebitCardInformation info = new DebitCardInformation();
			info.setType(merchant.getType());
			info.setMerchantPK(merchant.getPrimaryKey().toString());
			if (isSupplierManager) {
				info.setSupplierManager((Group) merchantType);
			}

			getDebitCardInformationDAO().store(info);

			t.commit();
		} catch (Exception e) {
			try {
				t.rollback();
			} catch (Exception e1) {
				e1.printStackTrace();
				throw new CreateException(e.getMessage());
			}
			throw new CreateException(e.getMessage());
		}
	}


	public List<DebitCardInformation> getDebitCardInformations(Supplier supplier) throws IDORelationshipException {
		DebitCardInformationDAO ccInfoHome = ELUtil.getInstance().getBean(DebitCardInformationDAO.BEAN_NAME);
		return ccInfoHome.findBySupplierManager(supplier.getGroupId());
	}

	public List<DebitCardInformation> getDebitCardInformations(Group supplierManager) {
		DebitCardInformationDAO ccInfoHome = ELUtil.getInstance().getBean(DebitCardInformationDAO.BEAN_NAME);
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
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
			return null;
		}
	}

	public boolean verifyCreditCardNumber(String numberToCheck, CreditCardAuthorizationEntry entry)
			throws IllegalArgumentException {
		if (numberToCheck != null && numberToCheck.length() >= 10) {
			int length = numberToCheck.length();
			numberToCheck = numberToCheck.substring(length - 10, length);

			String hex = hexEncode(Encrypter.encryptOneWay(numberToCheck));

			return hex.equals(entry.getCardNumber());
		}
		throw new IllegalArgumentException("Number must be at least 10 characters long");
	}

	public CreditCardAuthorizationEntry getAuthorizationEntry(Group supplierManager, String authorizationCode,
			IWTimestamp stamp) {
		DebitCardInformation info = getDebitCardInformation(supplierManager, stamp);
		return getAuthorizationEntry(info, authorizationCode, stamp);
	}

	public CreditCardAuthorizationEntry getAuthorizationEntry(Supplier supplier, String authorizationCode,
			IWTimestamp stamp) {
		DebitCardInformation info = getDebitCardInformation(supplier, stamp);
		CreditCardAuthorizationEntry entry = getAuthorizationEntry(info, authorizationCode, stamp);
		if (entry == null) {
			Group group = null;
			if ((supplier != null) && (supplier.getSupplierManager() != null)
					&& (supplier.getSupplierManager().getPrimaryKey() != null)) {
				if (supplier.getSupplierManager().getPrimaryKey() instanceof Integer) {
					GroupDAO grpDAO = ELUtil.getInstance().getBean("groupDAO");
					group = grpDAO.findGroup((Integer) supplier.getSupplierManager().getPrimaryKey());
				}
				entry = getAuthorizationEntry(group, authorizationCode, stamp);
			}
		}
		return entry;
	}

	public CreditCardAuthorizationEntry getAuthorizationEntry(DebitCardInformation info, String authorizationCode,
			IWTimestamp stamp) {
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
		return getUseCVC(getDebitCardMerchant(supplier, stamp));
	}

	public List<CreditCardAuthorizationEntry> getAllRefunds(IWTimestamp from, IWTimestamp to, int clientType)
			throws IDOLookupException, FinderException {
		if (clientType > 0) {
			return getAuthorisationEntriesDAO(clientType).findRefunds(from.getSQLDate(), to.getSQLDate());
		}
		return null;
	}

	private Logger getLogger() {
		return Logger.getLogger(getClass().getName());
	}

	private void log(String msg) {
		getLogger().log(Level.INFO, msg);
	}

	private IWApplicationContext getIWApplicationContext() {
		if (this.iwac == null) {
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
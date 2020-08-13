package com.idega.block.creditcard2.business;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.transaction.TransactionManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idega.block.creditcard.business.CreditCardClient;
import com.idega.block.creditcard.data.CreditCardAuthorizationEntry;
import com.idega.block.creditcard.data.CreditCardMerchant;
import com.idega.block.creditcard2.data.beans.BorgunMerchant;
import com.idega.block.creditcard2.data.beans.DummyMerchant;
import com.idega.block.creditcard2.data.beans.KortathjonustanMerchant;
import com.idega.block.creditcard2.data.beans.TPosMerchant;
import com.idega.block.creditcard2.data.beans.ValitorMerchant;
import com.idega.block.creditcard2.data.beans.VirtualCard;
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
import com.idega.block.trade.data.bean.CreditCardInformation;
import com.idega.block.trade.data.dao.CreditCardInformationDAO;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.core.business.DefaultSpringBean;
import com.idega.core.persistence.Param;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.transaction.IdegaTransactionManager;
import com.idega.user.dao.GroupDAO;
import com.idega.user.data.bean.Group;
import com.idega.util.Encrypter;
import com.idega.util.IWTimestamp;
import com.idega.util.ListUtil;
import com.idega.util.StringUtil;
import com.idega.util.expression.ELUtil;

@Service(CreditCardBusiness.BEAN_NAME)
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class CreditCardBusiness extends DefaultSpringBean implements CardBusiness {

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
	public final static int CLIENT_TYPE_VALITOR = 5;

	public static final String BEAN_NAME = "CreditCardBusiness";

	public CreditCardInformationDAO getCreditCardInformationDAO() {
		if (creditCardInformationDAO == null) {
			ELUtil.getInstance().autowire(this);
		}
		return creditCardInformationDAO;
	}

	public void setCreditCardInformationDAO(CreditCardInformationDAO creditCardInformationDAO) {
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

	public AuthorisationEntriesDAO<?> getAuthorisationEntriesDAO(CreditCardInformation info) {
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

	public MerchantDAO<?> getCreditCardMerchantDAO(CreditCardInformation ccInfo) {
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

	public MerchantDAO<?> getCreditCardMerchantDAO(String type) {
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

	public enum CreditCardType {
		VISA, ELECTRON, DINERS, DANKORT, MASTERCARD, JCB, AMERICAN_EXRESS;
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	public List<CreditCardAuthorizationEntry> getAuthorizationEntries(int clientType, String merchantID,
			IWTimestamp from, IWTimestamp to) {
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

	private IWBundle getBundle() {
		return IWContext.getCurrentInstance().getApplicationContext().getIWMainApplication()
				.getBundle(IW_BUNDLE_IDENTIFIER);
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
				} else if (CreditCardBusiness.CARD_TYPE_DINERS.equals(type)) {
					images.add(bundle.getImage("logos/diners.gif"));
				} else if (CreditCardBusiness.CARD_TYPE_ELECTRON.equals(type)) {
					images.add(bundle.getImage("logos/electron.gif"));
				} else if (CreditCardBusiness.CARD_TYPE_JCB.equals(type)) {
					images.add(bundle.getImage("logos/jcb.gif"));
				} else if (CreditCardBusiness.CARD_TYPE_MASTERCARD.equals(type)) {
					images.add(bundle.getImage("logos/mastercard.gif"));
				} else if (CreditCardBusiness.CARD_TYPE_VISA.equals(type)) {
					images.add(bundle.getImage("logos/visa.gif"));
				} else if (CreditCardBusiness.CARD_TYPE_AMERICAN_EXPRESS.equals(type)) {
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

	public CreditCardMerchant getCreditCardMerchant(String merchantPK, String merchantType) {
		CreditCardInformation ccInfo = getCreditCardInformation(merchantPK, merchantType);
		return getCreditCardMerchant(ccInfo);
	}

	public CreditCardMerchant getCreditCardMerchant(Supplier supplier, IWTimestamp stamp) {
		if (supplier == null) {
			return null;
		}
		CreditCardInformation ccInfo = getCreditCardInformation(supplier, stamp);
		return getCreditCardMerchant(ccInfo);
	}

	public CreditCardMerchant getCreditCardMerchant(Group supplierManager, IWTimestamp stamp) {
		if (supplierManager == null) {
			return null;
		}
		CreditCardInformation ccInfo = getCreditCardInformation(supplierManager, stamp);
		return getCreditCardMerchant(ccInfo);
	}

	public CreditCardMerchant getCreditCardMerchant(com.idega.block.trade.data.CreditCardInformation ccInfo) {
		if (ccInfo != null) {
			CreditCardInformationDAO cciDAO = ELUtil.getInstance().getBean(CreditCardInformationDAO.BEAN_NAME);
			CreditCardInformation info = cciDAO.findByPrimaryKey((Integer) ccInfo.getPrimaryKey());
			if (info != null) {
				return getCreditCardMerchantDAO(info).findById(Integer.parseInt(info.getMerchantPK()));
			}
		}
		return null;
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
			} else {
				toCheck = IWTimestamp.getTimestampRightNow();
			}

			CreditCardInformation ccInfo = null;

			// Checking for merchants configured to this supplier
			List<CreditCardInformation> coll = this.getCreditCardInformations(supplier);
			if (coll != null) {
				Iterator<CreditCardInformation> iter = coll.iterator();
				ccInfo = getCreditCardInformationInUse(iter, toCheck);
			}

			// Checking for merchants configured to this supplier's
			// supplierManager
			if (ccInfo == null) {
				GroupDAO grpDAO = ELUtil.getInstance().getBean(GroupDAO.class);
				Group group = grpDAO.findGroup((Integer) supplier.getSupplierManager().getPrimaryKey());
				ccInfo = getCreditCardInformation(group, stamp);
			}

			return ccInfo;

		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error getting credit card information. Supplier: " + supplier + ", stamp: " + stamp, e);
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
		} else {
			toCheck = IWTimestamp.getTimestampRightNow();
		}

		CreditCardInformation ccInfo = null;
		try {
			List<CreditCardInformation> coll;
			coll = getCreditCardInformations(supplierManager);
			Iterator<CreditCardInformation> iter = coll.iterator();
			ccInfo = getCreditCardInformationInUse(iter, toCheck);
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error getting credit card information. Supplier manager group: " + supplierManager + ", stamp: " + stamp, e);
		}

		return ccInfo;
	}

	private CreditCardInformation getCreditCardInformationInUse(Iterator<CreditCardInformation> iter,
			Timestamp toCheck) {
		Timestamp starts = null;
		Timestamp ends = null;
		CreditCardInformation info;
		CreditCardMerchant merchant;
		while (iter.hasNext()) {
			info = iter.next();
			merchant = getCreditCardMerchant(info);
			if (merchant != null && !merchant.getIsDeleted()) {
				if (merchant.getStartDate() != null) {
					starts = new Timestamp(merchant.getStartDate().getTime());
				}
				if (merchant.getEndDate() != null) {
					ends = new Timestamp(merchant.getEndDate().getTime());
				}

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

	public CreditCardMerchant getCreditCardMerchant(Supplier supplier, Object id) {
		try {
			Collection<com.idega.block.trade.data.CreditCardInformation> coll = supplier.getCreditCardInformation();
			CreditCardMerchant returner = getCreditCardMerchant(id, coll);
			return returner;
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error getting credit card merchant. Supplier: " + supplier + ", object ID: " + id, e);
		}
		return null;
	}

	public CreditCardMerchant getCreditCardMerchant(Group supplierManager, Object id) {
		try {
			List<CreditCardInformation> coll = getCreditCardInformations(supplierManager);
			CreditCardMerchant returner = getCreditCardMerchant(id, coll);
			return returner;
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error getting credit card merchant. Supplier manager group: " + supplierManager + ", object ID: " + id, e);
		}
		return null;
	}

	/**
	 * @param PK
	 * @param creditcardInformations
	 * @param returner
	 * @return
	 */
	private CreditCardMerchant getCreditCardMerchant(Object PK,
			Collection<com.idega.block.trade.data.CreditCardInformation> creditcardInformations) {
		return getCreditCardMerchant(PK, getCreditCardInformationEntityList(creditcardInformations));
	}

	private CreditCardMerchant getCreditCardMerchant(Object PK, List<CreditCardInformation> creditcardInformations) {
		for (CreditCardInformation info : creditcardInformations) {
			CreditCardMerchant merchant = getCreditCardMerchant(info);
			if (merchant != null && merchant.getPrimaryKey().toString().equals(PK.toString())) {
				return merchant;
			}
		}
		return null;
	}

	public CreditCardMerchant createCreditCardMerchant(String type) {
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
			List<CreditCardInformation> coll = null;
			if (isSupplier) {
				coll = getCreditCardInformations(((Supplier) merchantType));
			} else if (isSupplierManager) {
				coll = getCreditCardInformations((Group) merchantType);
			}

			if (coll != null) {
				Iterator<CreditCardInformation> iter = coll.iterator();
				CreditCardInformation info;
				CreditCardMerchant tmpMerchant = null;
				while (iter.hasNext()) {
					info = iter.next();

					if (isSupplier) {
						tmpMerchant = getCreditCardMerchant((Supplier) merchantType, new Integer(info.getMerchantPK()));
					} else if (isSupplierManager) {
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
		} catch (Exception e) {
			try {
				t.rollback();
			} catch (Exception e1) {
				getLogger().log(Level.WARNING, "Error rolling back transaction. Merchant type: " + merchantType + ", merchant: " + merchant, e1);
				throw new CreateException(e.getMessage());
			}
			throw new CreateException(e.getMessage());
		}
	}

	private List<CreditCardInformation> getCreditCardInformationEntityList(
			Collection<com.idega.block.trade.data.CreditCardInformation> coll) {
		List<CreditCardInformation> result = new ArrayList<CreditCardInformation>();
		CreditCardInformationDAO ccInfoHome = ELUtil.getInstance().getBean(CreditCardInformationDAO.BEAN_NAME);
		if (coll != null) {
			Iterator<com.idega.block.trade.data.CreditCardInformation> iter = coll.iterator();
			com.idega.block.trade.data.CreditCardInformation info;
			while (iter.hasNext()) {
				info = iter.next();
				result.add(ccInfoHome.findByPrimaryKey((Integer) info.getPrimaryKey()));
			}
		}
		return ListUtil.isEmpty(result) ? null : result;
	}

	public List<CreditCardInformation> getCreditCardInformations(Supplier supplier) throws IDORelationshipException {
		Collection<com.idega.block.trade.data.CreditCardInformation> coll = supplier.getCreditCardInformation();
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
				} catch (Exception e) {
					getLogger().log(Level.WARNING, "Error getting credit card info for supplier " + supplier, e);
				}
			}
		}
		return getCreditCardInformationEntityList(coll);
	}

	public List<CreditCardInformation> getCreditCardInformations(Group supplierManager) {
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
		} catch (Exception ex) {
			getLogger(CreditCardBusiness.class).log(Level.WARNING, "Error getting hex for " + enc, ex);
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
		CreditCardInformation info = getCreditCardInformation(supplierManager, stamp);
		return getAuthorizationEntry(info, authorizationCode, stamp);
	}

	public CreditCardAuthorizationEntry getAuthorizationEntry(Supplier supplier, String authorizationCode,
			IWTimestamp stamp) {
		CreditCardInformation info = getCreditCardInformation(supplier, stamp);
		CreditCardAuthorizationEntry entry = getAuthorizationEntry(info, authorizationCode, stamp);
		if (entry == null) {
			Group group = null;
			if ((supplier != null) && (supplier.getSupplierManager() != null)
					&& (supplier.getSupplierManager().getPrimaryKey() != null)) {
				if (supplier.getSupplierManager().getPrimaryKey() instanceof Integer) {
					GroupDAO grpDAO = ELUtil.getInstance().getBean(GroupDAO.class);
					group = grpDAO.findGroup((Integer) supplier.getSupplierManager().getPrimaryKey());
				}
				entry = getAuthorizationEntry(group, authorizationCode, stamp);
			}
		}
		return entry;
	}

	public CreditCardAuthorizationEntry getAuthorizationEntry(CreditCardInformation info, String authorizationCode,
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
		return getUseCVC(getCreditCardMerchant(supplier, stamp));
	}

	public List<CreditCardAuthorizationEntry> getAllRefunds(IWTimestamp from, IWTimestamp to, int clientType)
			throws IDOLookupException, FinderException {
		if (clientType > 0) {
			return getAuthorisationEntriesDAO(clientType).findRefunds(from.getSQLDate(), to.getSQLDate());
		}
		return null;
	}

	private void log(String msg) {
		getLogger().log(Level.INFO, msg);
	}

	public MerchantDAO<BorgunMerchant> getBorgunMerchantDao() {
		return ELUtil.getInstance().getBean(BorgunMerchantDAO.BEAN_NAME);
	}

	public BorgunAuthorisationEntryDAO getBorgunAuthorisationEntryDAO() {
		return ELUtil.getInstance().getBean(BorgunAuthorisationEntryDAO.BEAN_NAME);
	}

	@Override
	public CreditCardClient getCardClient(Supplier supplier, IWTimestamp timestamp) throws Exception {
		return getCreditCardClient(supplier, timestamp);
	}

	@Override
	@Transactional(readOnly = false)
	public VirtualCard getNewVirtualCard(String identifier) {
		try {
			VirtualCard vCard = new VirtualCard(identifier);
			creditCardInformationDAO.persist(vCard);
			return vCard.getId() == null ? null : vCard;
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error creating new virtual card with identifier " + identifier, e);
		}

		return null;
	}

	@Override
	public VirtualCard getVirtualCard(String token) {
		if (StringUtil.isEmpty(token)) {
			return null;
		}

		try {
			return creditCardInformationDAO.getSingleResult(VirtualCard.QUERY_FIND_BY_TOKEN, VirtualCard.class, new Param(VirtualCard.COLUMN_TOKEN, token));
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error getting virtual card by token " + token, e);
		}

		return null;
	}

}
package com.idega.block.creditcard2.business;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.transaction.TransactionManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idega.block.creditcard.CreditCardConstants;
import com.idega.block.creditcard.business.CreditCardClient;
import com.idega.block.creditcard.business.ValitorPayException;
import com.idega.block.creditcard.data.CreditCardAuthorizationEntry;
import com.idega.block.creditcard.data.CreditCardMerchant;
import com.idega.block.creditcard.event.PaymentBySubscriptionEvent;
import com.idega.block.creditcard.model.AuthEntryData;
import com.idega.block.creditcard2.data.beans.BorgunMerchant;
import com.idega.block.creditcard2.data.beans.DummyMerchant;
import com.idega.block.creditcard2.data.beans.KortathjonustanMerchant;
import com.idega.block.creditcard2.data.beans.Subscription;
import com.idega.block.creditcard2.data.beans.TPosMerchant;
import com.idega.block.creditcard2.data.beans.ValitorAuthorisationEntry;
import com.idega.block.creditcard2.data.beans.ValitorMerchant;
import com.idega.block.creditcard2.data.beans.VirtualCard;
import com.idega.block.creditcard2.data.dao.AuthorisationEntriesDAO;
import com.idega.block.creditcard2.data.dao.MerchantDAO;
import com.idega.block.creditcard2.data.dao.SubscriptionDAO;
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
import com.idega.block.trade.business.CurrencyHolder;
import com.idega.block.trade.data.CreditCardInformationHome;
import com.idega.block.trade.data.bean.CreditCardInformation;
import com.idega.block.trade.data.dao.CreditCardInformationDAO;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.block.trade.stockroom.data.SupplierHome;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.business.DefaultSpringBean;
import com.idega.core.persistence.Param;
import com.idega.data.IDOLookup;
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
import com.idega.user.dao.UserDAO;
import com.idega.user.data.bean.Group;
import com.idega.user.data.bean.User;
import com.idega.util.CoreConstants;
import com.idega.util.CoreUtil;
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

	@Autowired
	private SubscriptionDAO subscriptionDAO;

	@Autowired
	private UserDAO userDAO;

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
		Collection<Image> images = new ArrayList<>();
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
				Group group = null;
				if (supplier.getSupplierManager() != null) {
					group = grpDAO.findGroup((Integer) supplier.getSupplierManager().getPrimaryKey());
				}
				if (group != null) {
					ccInfo = getCreditCardInformation(group, stamp);
				}
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
		List<CreditCardInformation> result = new ArrayList<>();
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
			getLogger().warning("Token is not provided");
			return null;
		}

		try {
			return creditCardInformationDAO.getSingleResult(VirtualCard.QUERY_FIND_BY_TOKEN, VirtualCard.class, new Param(VirtualCard.COLUMN_TOKEN, token));
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error getting virtual card by token " + token, e);
		}

		return null;
	}

	@Override
	@Transactional(readOnly = false)
	public boolean doUpdateVirtualCard(
			String token,
			String transactionId,
			String card4,
			String brand,
			Integer expireYear,
			Integer expireMonth,
			Boolean enabled,
			User owner
	) {
		VirtualCard vCard = getVirtualCard(token);
		if (vCard == null) {
			getLogger().warning("Virtual card not found by token: " + token);
			return false;
		}

		try {
			vCard.setTransactionId(transactionId);
			vCard.setLast4(card4);
			vCard.setBrand(brand);
			vCard.setOwner(owner);
			vCard.setExpYear(expireYear);
			vCard.setExpMonth(expireMonth);
			vCard.setEnabled(Boolean.TRUE);
			creditCardInformationDAO.merge(vCard);
			return true;
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error updating virtual card " + vCard, e);
		}
		return false;
	}

	@Override
	@Transactional(readOnly = false)
	public VirtualCard createVirtualCard(String identifier, User owner, String cardUniqueId) {
		try {
			VirtualCard vCard = new VirtualCard();
			vCard.setToken(identifier);
			vCard.setUniqueId(cardUniqueId);
			vCard.setOwner(owner);
			creditCardInformationDAO.persist(vCard);
			return vCard.getId() == null ? null : vCard;
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error creating new virtual card with identifier " + identifier, e);
		}

		return null;
	}

	@Override
	@Transactional(readOnly = false)
	public VirtualCard createVirtualCard(
			String cardUniqueId,
			String token,
			User owner,
			Integer groupId,
			String transactionId,
			String card4,
			String brand,
			Integer expireYear,
			Integer expireMonth,
			Boolean enabled
	) {
		try {
			if (owner == null || StringUtil.isEmpty(token)) {
				getLogger().warning("Owner or token not provided");
				return null;
			}

			List<VirtualCard> cards = creditCardInformationDAO.getResultList(
					VirtualCard.QUERY_FIND_ACTIVE_BY_OWNER_AND_GROUP, VirtualCard.class,
					new Param(VirtualCard.PARAM_OWNER_ID, owner.getId()),
					new Param(VirtualCard.PARAM_GROUP_ID, groupId)
			);
			VirtualCard vCard = null;
			if (!ListUtil.isEmpty(cards)) {
				for (Iterator<VirtualCard> iter = cards.iterator(); (vCard == null && iter.hasNext());) {
					vCard = iter.next();
					String cardToken = vCard.getToken();
					if (StringUtil.isEmpty(cardToken) && !cardToken.equals(token)) {
						vCard = null;
					}
				}
			}

			if (vCard == null) {
				vCard = new VirtualCard();
			}
			vCard.setUniqueId(cardUniqueId);
			vCard.setToken(token);
			vCard.setOwner(owner);
			vCard.setGroupId(groupId);
			vCard.setTransactionId(transactionId);
			vCard.setLast4(card4);
			vCard.setBrand(brand);
			vCard.setOwner(owner);
			vCard.setExpYear(expireYear);
			vCard.setExpMonth(expireMonth);
			vCard.setEnabled(enabled);
			vCard.setDeleted(Boolean.FALSE);
			vCard.setDeletedWhen(null);
			vCard.setDeletedBy(null);

			if (vCard.getId() == null) {
				creditCardInformationDAO.persist(vCard);
			} else {
				creditCardInformationDAO.merge(vCard);
			}
			return vCard.getId() == null ? null : vCard;
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error creating new virtual card with identifier/uniqueId: " + cardUniqueId + ", card token: " + token, e);
		}
		return null;
	}

	@Override
	public VirtualCard getVirtualCardByOwner(Integer userId) {
		if (userId == null) {
			getLogger().warning("User id is not provided");
			return null;
		}

		try {
			return creditCardInformationDAO.getSingleResult(VirtualCard.QUERY_FIND_BY_OWNER, VirtualCard.class, new Param(VirtualCard.PARAM_OWNER_ID, userId));
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error getting virtual card by user id " + userId, e);
		}

		return null;
	}

	@Override
	public void doMakeSubscriptionPayments() {
		try {

			//**** GET ACTIVE SUBSCRIPTIONS ****
			List<Subscription> subscriptions = getSubscriptionDAO().getAllActive();

			if (!ListUtil.isEmpty(subscriptions)) {

				CreditCardClient creditCardClient = getCreditCardClient(IWMainApplication.getDefaultIWApplicationContext());

				if (creditCardClient == null) {
					getLogger().warning("Could not find the credit card client. Will not execute automatic subscription payments.");
				}

				for (Subscription subscription : subscriptions) {

					if (subscription == null || subscription.getUserId() == null) {
						continue;
					}

					//**** Check, if valid for subscription payment *****
					boolean validForPayment = isValidForForSubscriptionPayment(subscription);

					if (validForPayment) {
						try {
							boolean successfulPayment = false;

							User user = getUserDAO().getUser(subscription.getUserId());
							if (user == null) {
								continue;
							}

							//Can retry to pay 3 times per month
							if (canRetryToPay(subscription) == false) {
								continue;
							}

							//**** Get virtual cards for user *****
							List<VirtualCard> virtualCardsForUser = getValidVirtualCardsForUser(user);

							if (ListUtil.isEmpty(virtualCardsForUser)) {
								continue;
							}

							//**** If virtual card exist, make the payment *****
							for (Iterator<VirtualCard> iter = virtualCardsForUser.iterator(); (!successfulPayment && iter.hasNext());) {
								VirtualCard vc = iter.next();
								String token = vc == null ? null : vc.getToken();
								if (StringUtil.isEmpty(token)) {
									getLogger().warning("Token unknown for virtual card: " + vc + " and user: " + user);
									continue;
								}

								String paymentUniqueId = UUID.randomUUID().toString();

								AuthEntryData data = null;
								try {
									data = creditCardClient.doSaleWithCardToken(
											vc.getToken(),
											subscription.getUniqueId(),
											subscription.getAmount(),
											CurrencyHolder.ICELANDIC_KRONA,
											vc.getToken(),
											paymentUniqueId
									);
								} catch (ValitorPayException eV) {
									getLogger().log(Level.WARNING, "Error executing automatic subscription payment. Subscription: " + subscription + " for " + user + " using " + vc, eV);
									//Create the authorization entry about failed transaction
									storeValitorAuthorizationEntry(
											paymentUniqueId,
											vc,
											subscription.getAmount(),
											user,
											CurrencyHolder.ICELANDIC_KRONA,
											eV,
											creditCardClient
									);

								} catch (Throwable t) {
									getLogger().log(Level.WARNING, "Error executing automatic subscription payment. Subscription: " + subscription + " for " + user + " using " + vc, t);
								}

								String authCode = data == null ? null : data.getAuthCode();
								if (StringUtil.isEmpty(authCode)) {
									getLogger().warning("Auth. code unknown for subscription: " + subscription
											+ ", virtual card: " + vc + ". Can not execute automatic subscription payment for " + user);
									continue;
								}

								successfulPayment = true;
								getLogger().info("Successful payment with virtual card: " + vc + " for user: " + user +
										". Auth. code " + authCode + " (unique ID: " + data.getUniqueId() + ").");

								getSubscriptionDAO().doCreateSubscriptionPayment(user.getId(), subscription.getId(), authCode);

								String authEntryUniqueId = data.getUniqueId();
								setMetaData(creditCardClient, authEntryUniqueId);
							}

							//**** Update the subscription after the successful payment *****
							if (successfulPayment) {
								getLogger().info("Successful automatic subscription payment for subscription: " + subscription);
								subscription.setLastPaymentDate(IWTimestamp.getTimestampRightNow());
								getSubscriptionDAO().createUpdateSubscription(subscription);

								//*** Fire the PaymentBySubscriptionEvent ***
								ELUtil.getInstance().publishEvent(new PaymentBySubscriptionEvent(this, subscription, Boolean.TRUE, null));

							} else {
								getLogger().info("Failed to make automatic subscription payment for subscription: " + subscription);

								//Update the subscription
								IWTimestamp now = IWTimestamp.RightNow();
								subscription.setLastUnsuccessfulPaymentDate(now.getTimestamp());
								Integer failedPaymentsPerMonth = subscription.getFailedPaymentsPerMonth() != null ? subscription.getFailedPaymentsPerMonth() : 0;
								failedPaymentsPerMonth++;
								subscription.setFailedPaymentsPerMonth(failedPaymentsPerMonth);
								Integer failedPayments = subscription.getFailedPayments() != null ? subscription.getFailedPayments() : 0;
								failedPayments++;
								subscription.setFailedPayments(failedPayments);
								getSubscriptionDAO().createUpdateSubscription(subscription);

								//*** Fire the PaymentBySubscriptionEvent ***
								ELUtil.getInstance().publishEvent(new PaymentBySubscriptionEvent(this, subscription, Boolean.FALSE, "Failed to make automatic subscription payment for subscription."));
							}
						} catch (Exception eSubP) {
							getLogger().log(Level.WARNING, "Failed to make automatic subscription payment for subscription: " + subscription, eSubP);

							//*** Fire the PaymentBySubscriptionEvent ***
							ELUtil.getInstance().publishEvent(new PaymentBySubscriptionEvent(this, subscription, Boolean.FALSE, "Failed to make automatic subscription payment for subscription. " + eSubP.getLocalizedMessage()));
						}
					}
				}
			}
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Could not execute automatic subscription payments.", e);
		}
	}

	private boolean canRetryToPay(Subscription subscription) {
		try {
			if (subscription != null && subscription.getFailedPaymentsPerMonth() != null && subscription.getLastUnsuccessfulPaymentDate() != null) {
				IWTimestamp now = IWTimestamp.RightNow();
				IWTimestamp lastUnsuccessfulPaymentIWT = new IWTimestamp(subscription.getLastUnsuccessfulPaymentDate());
				if (now.getYear() == lastUnsuccessfulPaymentIWT.getYear() && now.getMonth() != lastUnsuccessfulPaymentIWT.getMonth()) {
					subscription.setFailedPaymentsPerMonth(0);	//	Re-setting counter

				} else if (
						now.getYear() == lastUnsuccessfulPaymentIWT.getYear()
						&& now.getMonth() == lastUnsuccessfulPaymentIWT.getMonth()
						&& subscription.getFailedPaymentsPerMonth() >= 3
				) {
					return false;

				} else {
					subscription.setFailedPaymentsPerMonth(0);	//	Re-setting counter, i.e. year has changed
				}
			}
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Could not check, if subscription can be paid and how many time it was paid already: " + subscription);
		}
		return true;
	}

	private boolean isValidForForSubscriptionPayment(Subscription subscription) {
		try {
			if (isValidForForSubscriptionPayment(subscription == null ? null : subscription.getLastPaymentDate())) {
				getLogger().info("Valid for a new subscription payment. Subscription: " + subscription);
				return true;
			}
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Could not check if subscription is valid for automatic payment: " + subscription, e);
		}

		getLogger().info("NOT valid for a new subscription payment. Subscription: " + subscription);
		return false;
	}

	@Override
	public boolean isValidForForSubscriptionPayment(Timestamp lastPaymentDate) {
		boolean validForSubscriptionPayment = false;
		IWTimestamp nowIWT = new IWTimestamp();
		nowIWT.setHour(0);
		nowIWT.setMinute(0);
		nowIWT.setSecond(0);
		nowIWT.setMilliSecond(0);

		try {
			if (lastPaymentDate != null) {
				IWTimestamp newPaymentDateIWT = new IWTimestamp(lastPaymentDate);
				newPaymentDateIWT.setHour(0);
				newPaymentDateIWT.setMinute(0);
				newPaymentDateIWT.setSecond(0);
				newPaymentDateIWT.setMilliSecond(0);
				newPaymentDateIWT.addMonths(1);

				if (
						(
								nowIWT.getYear() == newPaymentDateIWT.getYear()
								&& nowIWT.getMonth() >= newPaymentDateIWT.getMonth()
								&& nowIWT.getDay() >= newPaymentDateIWT.getDay()
						)
						||
						(
								nowIWT.getYear() > newPaymentDateIWT.getYear()
								&& nowIWT.getDay() >= newPaymentDateIWT.getDay()
						)
						||
						(
								IWTimestamp.getDaysBetween(newPaymentDateIWT, nowIWT) >= 0
						)
						||
						(
								nowIWT.isLaterThanOrEquals(newPaymentDateIWT)
						)
				) {
					validForSubscriptionPayment = true;
				} else {
					validForSubscriptionPayment = false;
				}

			}
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Failed checking if subscription is valid for next payment. Last payment date: " + lastPaymentDate, e);
		}

		return validForSubscriptionPayment;
	}

	private SubscriptionDAO getSubscriptionDAO() {
		if (subscriptionDAO == null) {
			ELUtil.getInstance().autowire(this);
		}
		return subscriptionDAO;
	}

	public List<VirtualCard> getValidVirtualCardsForUser(com.idega.user.data.bean.User user) {
		try {
			if (user == null) {
				return null;
			}

			List<VirtualCard> cards = creditCardInformationDAO.getResultList(
					VirtualCard.QUERY_FIND_ACTIVE_BY_OWNER,
					VirtualCard.class,
					new Param(VirtualCard.PARAM_OWNER_ID, user.getId())
			);

			if (ListUtil.isEmpty(cards)) {
				return null;
			}

			IWTimestamp now = IWTimestamp.RightNow();
			int month = now.getMonth();
			int year = now.getYear();
			boolean validIfNoExpireDate = getSettings().getBoolean("virtual_card.vc_valid_without_date", true);
			List<VirtualCard> results = new ArrayList<>();
			for (VirtualCard card: cards) {
				Boolean deleted = card.getDeleted();
				if (deleted != null && deleted) {
					getLogger().warning(card + " for " + user + " is deleted");
					continue;
				}

				Boolean enabled = card.getEnabled();
				if (enabled == null || !enabled) {
					getLogger().warning(card + " for " + user + " is disabled");
					continue;
				}

				boolean expired = false;
				Integer expireYear = card.getExpYear();
				if (expireYear == null) {
					if (validIfNoExpireDate) {
						results.add(card);
					} else {
						getLogger().warning(card + " for " + user + " - unknown expire year");
					}
					continue;
				}
				if (expireYear.toString().length() == 2) {
					expireYear = expireYear + 2000;
				}
				expired = expireYear < year;
				if (expired) {
					getLogger().warning(card + " for " + user + " has expired (year: " + expireYear + ")");
					continue;
				}

				Integer expireMonth = card.getExpMonth();
				if (expireMonth == null) {
					if (validIfNoExpireDate) {
						results.add(card);
					} else {
						getLogger().warning(card + " for " + user + " - unknown expire month");
					}
					continue;
				}
				expired = expireYear == year && expireMonth < month;
				if (expired) {
					if (validIfNoExpireDate) {
						results.add(card);
					} else {
						getLogger().warning(card + " for " + user + " has expired");
					}
					continue;
				}

				results.add(card);
			}
			return results;
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error getting virtual cards for " + user, e);
		}
		return null;
	}

	private UserDAO getUserDAO() {
		if (userDAO == null) {
			ELUtil.getInstance().autowire(this);
		}
		return userDAO;
	}


	private CreditCardClient getCreditCardClient(IWApplicationContext iwac) {
		CreditCardClient creditCardClient = null;

		try {
			String paymentCCInfoId = getSettings().getProperty(CreditCardConstants.APP_PROPERTY_CC_PAYMENT_INFO_ID, CreditCardConstants.DEFAULT_CC_PAYMENT_INFO_ID);
			if (!StringUtil.isEmpty(paymentCCInfoId)) {
				CreditCardInformationHome ccInfoHome = (CreditCardInformationHome) IDOLookup.getHome(com.idega.block.trade.data.CreditCardInformation.class);
				com.idega.block.trade.data.CreditCardInformation ccInfo = ccInfoHome.findByPrimaryKey(paymentCCInfoId);
				if (ccInfo != null) {
					String merchantId = ccInfo.getMerchantPKString();
					String merhcantType = ccInfo.getType();
					if (!StringUtil.isEmpty(merchantId) && !StringUtil.isEmpty(merhcantType)) {
						try {
							CreditCardMerchant merchant = getCreditCardMerchant(merchantId, merhcantType);
							creditCardClient = getCreditCardClient(merchant);
						} catch (Exception e) {
							getLogger().log(Level.WARNING, "Credit card client was not found by supplier.", e);
						}
					}
				}
			}
		} catch (Exception eCCI) {
			getLogger().log(Level.WARNING, "Credit card client was nnot found by credit card info id", eCCI);
		}

		if (creditCardClient != null) {
			return creditCardClient;
		} else {
			try {
				String productSupplierId = getSettings().getProperty(CreditCardConstants.APP_PROPERTY_CC_SUPPLIER_ID, CreditCardConstants.DEFAULT_CC_SUPPLIER_ID);
				if (!StringUtil.isEmpty(productSupplierId)) {
					Supplier suppTemp = ((SupplierHome) IDOLookup.getHomeLegacy(Supplier.class)).findByPrimaryKeyLegacy(Integer.valueOf(productSupplierId));
					if (suppTemp != null) {
						creditCardClient = getCreditCardBusiness(iwac).getCreditCardClient(suppTemp, new IWTimestamp());
					}
				}
			} catch (Exception e) {
				getLogger().log(Level.WARNING, "Credit card client was not found by supplier.", e);
			}
		}

		return creditCardClient;
	}

	protected com.idega.block.creditcard.business.CreditCardBusiness getCreditCardBusiness(IWApplicationContext iwac) {
		try {
			return IBOLookup.getServiceInstance(iwac == null ? IWMainApplication.getDefaultIWApplicationContext() : iwac, com.idega.block.creditcard.business.CreditCardBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}

	public void setMetaData(CreditCardClient ccClient, String authEntryUniqueId) {
		if (StringUtil.isEmpty(authEntryUniqueId)) {
			getLogger().warning("Auth. entry unique ID not provided. Credit card client " + ccClient);
			return;
		}
		if (ccClient == null) {
			getLogger().warning("Credit card client not provided. Auth. entry UUID: " + authEntryUniqueId);
			return;
		}

		Object merchantId = null;
		try {
			CreditCardMerchant ccMerchant = ccClient.getCreditCardMerchant();
			if (ccMerchant == null) {
				getLogger().warning("Credit card merchant is not known fo credit card client " + ccClient + ". Auth. entry UUID: " + authEntryUniqueId);
				return;
			}

			CreditCardAuthorizationEntry entry = getValitorAuthorisationEntryDAO().findByAuthorizationCode(authEntryUniqueId, null);

			merchantId = ccMerchant.getId();

			if (entry == null) {
				getLogger().warning("Credit card authorization entry not found with authorization entry UUID: " + authEntryUniqueId);
				return;
			} else {
				getLogger().info("Adding the meta data with merchantId: " + merchantId + " to the authorization entry: " + entry);
			}

			entry.setMetaData(CreditCardConstants.CREDIT_CARD_MERCHANT_ID, String.valueOf(merchantId));
			entry.store();
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error setting meta data (merchant ID: " + merchantId + ") for auth. entry with unique ID " +
					authEntryUniqueId + ". Credit card client " + ccClient, e);
		}
	}

	private ValitorAuthorisationEntry storeValitorAuthorizationEntry(
			String paymentUniqueId,
			VirtualCard virtualCard,
			Double fullAmountToPay,
			com.idega.user.data.bean.User user,
			String currency,
			ValitorPayException valitorPayException,
			CreditCardClient creditCardClient
	) {
		try {
			if (
					creditCardClient == null
					|| creditCardClient.getCreditCardMerchant() == null
					|| !CreditCardMerchant.MERCHANT_TYPE_VALITOR.equals(creditCardClient.getCreditCardMerchant().getType())
			) {
				getLogger().warning("This is not a Valitor merchant. Can not create the Valitor authorization entry.");
				return null;
			}

			ValitorAuthorisationEntry auth = new ValitorAuthorisationEntry();

			auth.setAmount(fullAmountToPay);
			auth.setCardNumber(virtualCard.getToken());
			auth.setSuccess(Boolean.FALSE);
			auth.setCurrency(currency);
			IWTimestamp timestamp = new IWTimestamp();
			auth.setDate(timestamp.getDate());
			auth.setTimestamp(timestamp.getTimestamp());
			auth.setUniqueId(paymentUniqueId);		//	Authorization entry must be resolved by unique id. Merchant reference id is saved as unique id only
			auth.setErrorNumber(valitorPayException != null ? valitorPayException.getErrorNumber() : CoreConstants.EMPTY);
			auth.setErrorText(valitorPayException != null ? valitorPayException.getErrorMessage() : CoreConstants.EMPTY);
			auth.setMerchant(creditCardClient.getCreditCardMerchant());
			getValitorAuthorisationEntryDAO().store(auth);
			return auth.getId() == null ? null : auth;
		} catch (Exception e) {
			String error = "Could not store the ValitorAuthorisationEntry after the ValitorPay failed transaction with virtual card. "
					+ " Virtual card: " + virtualCard
					+ ". paymentUniqueId: " + paymentUniqueId
					+ ". fullAmountToPay: " + fullAmountToPay
					+ ". user: " + user;
			getLogger().log(Level.WARNING, error, e);
			CoreUtil.sendExceptionNotification(error, e);
		} finally {
			CoreUtil.clearAllCaches();
		}
		return null;
	}

}
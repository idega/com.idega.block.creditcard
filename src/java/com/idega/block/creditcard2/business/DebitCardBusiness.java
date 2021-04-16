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
import com.idega.block.creditcard2.data.beans.ValitorDebitMerchant;
import com.idega.block.creditcard2.data.beans.VirtualCard;
import com.idega.block.creditcard2.data.dao.AuthorisationEntriesDAO;
import com.idega.block.creditcard2.data.dao.MerchantDAO;
import com.idega.block.creditcard2.data.dao.impl.ValitorDebitAuthorisationEntryDAO;
import com.idega.block.creditcard2.data.dao.impl.ValitorDebitMerchantDAO;
import com.idega.block.trade.data.bean.DebitCardInformation;
import com.idega.block.trade.data.dao.DebitCardInformationDAO;
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
import com.idega.user.data.bean.User;
import com.idega.util.Encrypter;
import com.idega.util.IWTimestamp;
import com.idega.util.StringUtil;
import com.idega.util.expression.ELUtil;

@Service(DebitCardBusiness.BEAN_NAME)
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class DebitCardBusiness extends DefaultSpringBean implements CardBusiness {

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

	public final static int CLIENT_TYPE_VALITOR = 1;

	public static final String BEAN_NAME = "DebitCardBusiness";

	@Autowired
	private GroupDAO groupDAO;

	public DebitCardInformationDAO getDebitCardInformationDAO() {
		if (debitCardInformationDAO == null) {
			ELUtil.getInstance().autowire(this);
		}
		return debitCardInformationDAO;
	}

	public void setDebitCardInformationDAO(DebitCardInformationDAO debitCardInformationDAO) {
		this.debitCardInformationDAO = debitCardInformationDAO;
	}

	public MerchantDAO<ValitorDebitMerchant> getValitorDebitMerchantDao() {
		return ELUtil.getInstance().getBean(ValitorDebitMerchantDAO.BEAN_NAME);
	}

	public ValitorDebitAuthorisationEntryDAO getValitorDebitAuthorisationEntryDAO() {
		return ELUtil.getInstance().getBean(ValitorDebitAuthorisationEntryDAO.BEAN_NAME);
	}

	public AuthorisationEntriesDAO<?> getAuthorisationEntriesDAO(int clientType) {
		if (clientType > 0) {
			if (clientType == CLIENT_TYPE_VALITOR) {
				return getValitorDebitAuthorisationEntryDAO();
			}
		}
		return null;
	}

	public AuthorisationEntriesDAO<?> getAuthorisationEntriesDAO(DebitCardInformation info) {
		if  (CreditCardMerchant.MERCHANT_TYPE_VALITOR_DEBIT.equals(info.getType())) {
			return getValitorDebitAuthorisationEntryDAO();
		}
		return null;
	}

	public MerchantDAO<?> getDebitCardMerchantDAO(DebitCardInformation ccInfo) {
		String type = ccInfo.getType();
		if (CreditCardMerchant.MERCHANT_TYPE_VALITOR_DEBIT.equals(type)) {
			return getValitorDebitMerchantDao();
		}

		return null;
	}

	public MerchantDAO<?> getDebitCardMerchantDAO(String type) {
		if (CreditCardMerchant.MERCHANT_TYPE_VALITOR_DEBIT.equals(type)) {
			return getValitorDebitMerchantDao();
		}
		return null;
	}

	public enum DebitCardType {
		VISA, ELECTRON, DINERS, DANKORT, MASTERCARD, JCB, AMERICAN_EXRESS;
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

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
		Collection<Image> images = new ArrayList<>();
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
			if (CreditCardMerchant.MERCHANT_TYPE_VALITOR_DEBIT.equals(merchant.getType())) {
				return new ValitorDebitCardClient(merchant);
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
				GroupDAO grpDAO = getGroupDAO();
				Group group;
				if (supplier.getSupplierManager()!=null){
					group = grpDAO.findGroup((Integer) supplier.getSupplierManager().getPrimaryKey());
					ccInfo = getDebitCardInformation(group, stamp);
				} else if (supplier.getGroupId()>0) {
					group = grpDAO.findGroup(supplier.getGroupId());
					ccInfo = getDebitCardInformation(group, stamp);
				}
			}

			return ccInfo;

		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error getting debit card information. Supplier: " + supplier + ", stamp: " + stamp, e);
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
			getLogger().log(Level.WARNING, "Error getting debit card information. Supplier manager group: " + supplierManager + ", stamp: " + stamp, e);
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

	public CreditCardMerchant getDebitCardMerchant(Group supplierManager, Object id) {
		try {
			List<DebitCardInformation> coll = getDebitCardInformations(supplierManager);
			CreditCardMerchant returner = getDebitCardMerchant(id, coll);
			return returner;
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error getting card merchant. Supplier manager group: " + supplierManager + ", object ID: " + id, e);
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
		if (CreditCardMerchant.MERCHANT_TYPE_VALITOR_DEBIT.equals(type)) {
			return new ValitorDebitMerchant();
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
			if (isSupplier) {
				if (((Supplier) merchantType).getSupplierManager()!=null){
					info.setSupplierManager(getGroupDAO().findGroup(((Supplier) merchantType).getSupplierManagerID()));
				} else {
					info.setSupplierManager(getGroupDAO().findGroup(((Supplier) merchantType).getGroupId()));
				}
			}

			getDebitCardInformationDAO().store(info);

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
			getLogger(DebitCardBusiness.class).log(Level.WARNING, "Error getting hex from " + enc, ex);
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
					GroupDAO grpDAO = getGroupDAO();
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

	public GroupDAO getGroupDAO() {
		if (groupDAO == null) {
			ELUtil.getInstance().autowire(this);
		}
		return groupDAO;
	}

	public void setGroupDAO(GroupDAO groupDAO) {
		this.groupDAO = groupDAO;
	}

	@Override
	public CreditCardClient getCardClient(Supplier supplier, IWTimestamp timestamp) throws Exception {
		return getDebitCardClient(supplier, timestamp);
	}

	@Override
	@Transactional(readOnly = false)
	public VirtualCard getNewVirtualCard(String identifier) {
		try {
			VirtualCard vCard = new VirtualCard(identifier);
			debitCardInformationDAO.persist(vCard);
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
			return debitCardInformationDAO.getSingleResult(VirtualCard.QUERY_FIND_BY_TOKEN, VirtualCard.class, new Param(VirtualCard.COLUMN_TOKEN, token));
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
			return false;
		}

		try {
			vCard.setTransactionId(transactionId);
			vCard.setLast4(card4);
			vCard.setBrand(brand);
			vCard.setOwner(owner);
			vCard.setExpYear(expireYear);
			vCard.setExpMonth(expireMonth);
			vCard.setEnabled(enabled);
			debitCardInformationDAO.merge(vCard);
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
			debitCardInformationDAO.persist(vCard);
			return vCard.getId() == null ? null : vCard;
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error creating new virtual card with identifier " + identifier, e);
		}

		return null;
	}

}
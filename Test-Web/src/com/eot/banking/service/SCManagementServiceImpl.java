package com.eot.banking.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eot.banking.common.AppConfigurations;
import com.eot.banking.common.EOTConstants;
import com.eot.banking.dao.BankDao;
import com.eot.banking.dao.CustomerDao;
import com.eot.banking.dao.LocationDao;
import com.eot.banking.dao.SCManagementDao;
import com.eot.banking.dao.TransactionRulesDao;
import com.eot.banking.dao.WebUserDao;
import com.eot.banking.dto.SCSearchDTO;
import com.eot.banking.dto.ServiceChargeDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.utils.DateUtil;
import com.eot.banking.utils.Page;
import com.eot.entity.Bank;
import com.eot.entity.BankGroup;
import com.eot.entity.BankGroupAdmin;
import com.eot.entity.BankTellers;
import com.eot.entity.CustomerProfiles;
import com.eot.entity.ScapplicableTime;
import com.eot.entity.ServiceChargeRule;
import com.eot.entity.ServiceChargeRuleTxn;
import com.eot.entity.ServiceChargeRuleValue;
import com.eot.entity.ServiceChargeSubscription;
import com.eot.entity.TimeZone;
import com.eot.entity.TransactionType;
import com.eot.entity.WebUser;

@Service("scManagementService")
@Transactional(readOnly=true)
public class SCManagementServiceImpl implements SCManagementService {

	@Autowired
	private LocationDao locationDao;
	@Autowired
	private SCManagementDao scManagementDao;
	@Autowired
	private TransactionRulesDao transactionRulesDao;
	@Autowired 
	private BankDao bankDao;
	@Autowired
	private CustomerDao customerDao;
	@Autowired
	private WebUserDao webUserDao;
	@Autowired
	private AppConfigurations appConfigurations;

	@Override
	public Map<String, List> getMasterData(String locale) throws EOTException {
		Map<String,List> model = new HashMap<String,List>();
		model.put("timeZoneList", locationDao.getTimeZones());
		model.put("transTypeList", transactionRulesDao.getTransactionTypesWithServiceCharge(locale));		
		return model;
	}

	public BankTellers getBankTellers() throws EOTException{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		WebUser webUser=webUserDao.getUser(auth.getName());
		if(webUser==null){
			throw new EOTException(ErrorConstants.INVALID_USER);
		}

		BankTellers teller = bankDao.getTellerByUsername(webUser.getUserName());
		if(teller == null){
			throw new EOTException(ErrorConstants.INVALID_TELLER);
		}

		return teller;
	}

	private BankGroupAdmin getGroupAdmin() throws EOTException{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		WebUser webUser=webUserDao.getUser(auth.getName());
		if(webUser==null){
			throw new EOTException(ErrorConstants.INVALID_USER);
		}

		BankGroupAdmin bankGroupAdmin = webUserDao.getbankGroupAdmin(auth.getName());
		if(bankGroupAdmin == null){
			throw new EOTException(ErrorConstants.INVALID_TELLER);
		}

		return bankGroupAdmin;
	}

	public List<CustomerProfiles> getCustomerProfiles()throws EOTException{
		return customerDao.getCustomerProfilesByBankId(getBankTellers().getBank().getBankId());
	}

	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=Throwable.class )
	public void saveOrUpdateServiceCharge(ServiceChargeDTO scDTO) throws EOTException {

		List<ServiceChargeRule> scRules = scManagementDao.getServiceChargeRulesByRuleLevel(scDTO.getRuleLevel(),scDTO.getRuleName());
		if(scDTO.getServiceChargeRuleId()!=null){
			ServiceChargeRule sr=scManagementDao.getServiceChargeRule(scDTO.getServiceChargeRuleId());
			if(!sr.getServiceChargeRuleName().equalsIgnoreCase(scDTO.getRuleName())){
				if(scRules.size()>0){
					throw new EOTException(ErrorConstants.SC_RULE_EXIST);}
			}			
		}
		else{
			if(scRules.size()>0 && scDTO.getServiceChargeRuleId()==null){
				throw new EOTException(ErrorConstants.SC_RULE_EXIST);}
		}
		for (Integer transationType : scDTO.getTransactions()) {
				List<ServiceChargeRuleTxn> rules=scManagementDao.getServiceChargeRulesByTxnType(transationType);
				if(rules.size()>0  && scDTO.getServiceChargeRuleId()==null)
					throw new EOTException(ErrorConstants.SC_RULE_EXIST);
		}
		Calendar dtoCalendar = Calendar.getInstance();
		dtoCalendar.setTime(DateUtil.strToSqlDate(scDTO.getApplicableFrom()));

		Calendar sysCalendar = Calendar.getInstance();
		sysCalendar.setTime(new Date());

		if(compare(dtoCalendar, sysCalendar)<0)
			throw new EOTException(ErrorConstants.INVALID_DATE);

		ServiceChargeRule serviceChargeRule=new ServiceChargeRule();

		serviceChargeRule.setServiceChargeRuleName(scDTO.getRuleName());
		serviceChargeRule.setRuleLevel(scDTO.getRuleLevel());
		serviceChargeRule.setApplicableFrom(DateUtil.strToSqlDate(scDTO.getApplicableFrom()));
		serviceChargeRule.setApplicableTo(DateUtil.strToSqlDate(scDTO.getApplicableTo()));
		TimeZone timeZone = new TimeZone();
		timeZone.setTimeZoneId(scDTO.getTimeZoneId());
		serviceChargeRule.setTimeZone(timeZone);
		serviceChargeRule.setImposedOn(scDTO.getImposedOn());

		if(serviceChargeRule.getRuleLevel()==EOTConstants.RULE_LEVEL_GLOBAL||serviceChargeRule.getRuleLevel()==EOTConstants.RULE_LEVEL_INTER_BANK){
			serviceChargeRule.setReferenceId(0);
		}
		if(serviceChargeRule.getRuleLevel()==EOTConstants.RULE_LEVEL_BANK_GROUP){
			serviceChargeRule.setReferenceId(getGroupAdmin().getBankGroup().getBankGroupId());
		}
		if(serviceChargeRule.getRuleLevel()==EOTConstants.RULE_LEVEL_CUSTOMER_PROFILE){
			serviceChargeRule.setReferenceId(scDTO.getProfileId());
		}

		if(scDTO.getServiceChargeRuleId()==null){
			scManagementDao.save(serviceChargeRule);
		}
		else{
			serviceChargeRule.setServiceChargeRuleId(scDTO.getServiceChargeRuleId());
			scManagementDao.merge(serviceChargeRule);
			scManagementDao.deleteServiceChargeRuleTxns(scDTO.getServiceChargeRuleId());
			scManagementDao.deleteServiceChargeRuleValues(scDTO.getServiceChargeRuleId());
			scManagementDao.deleteScapplicableTimes(scDTO.getServiceChargeRuleId());
			scManagementDao.deleteSubscriptionChargeRuleValues(scDTO.getServiceChargeRuleId());

		}

		saveSCRuleValueList(scDTO.getScPercentage(),scDTO.getScFixed(),scDTO.getDiscountLimit(),scDTO.getMinSC(),scDTO.getMaxSC(),scDTO.getMinTxnValue(),scDTO.getMaxTxnValue(),serviceChargeRule);
		saveSCApplicableTimeList(scDTO.getFromHours(),scDTO.getToHours(),scDTO.getDays(),serviceChargeRule);
		saveSubscriptionChargeList(scDTO.getSubscription(),scDTO.getCostPerPackage(),scDTO.getNoOfTxn(),serviceChargeRule);
		for (Integer transationType : scDTO.getTransactions()) {

			// set source type as wallet account for txns
			// which does not involve wallet/card/bank acc.(eg. activation)
			Integer[] sourceTypeFromRequest=scDTO.getSourceType();
			
			if(appConfigurations.getMobileTxnTypeMap().get(transationType+"")!=null){
				scDTO.setSourceType(new Integer[]{EOTConstants.ALIAS_TYPE_MOBILE_ACC});
			}
			if(transationType==95||transationType==99)
			{
				scDTO.setSourceType(new Integer[]{EOTConstants.ALIAS_TYPE_MOBILE_ACC});
			}
			if(transationType==115||transationType==116||transationType==126||transationType==128||transationType==30||transationType==133
					||transationType==35||transationType==98||transationType==20||transationType==70||transationType==75
					||transationType==55||transationType==82||transationType==80||transationType==90||transationType==83||transationType==84 || transationType==131||transationType==137||transationType==138 || transationType==140 ||transationType==141||transationType==144 ||transationType==146)					
			{
				scDTO.setSourceType(new Integer[]{EOTConstants.SOURCE_TYPE_WALLET});
			}
			if(transationType == EOTConstants.TXN_ID_ADDCARD){
				scDTO.setSourceType(new Integer[]{EOTConstants.ALIAS_TYPE_CARD_ACC});
			}
			if(transationType == EOTConstants.TXN_ID_CHEQUE_STATUS){
				scDTO.setSourceType(new Integer[]{3});
			}

			if (scDTO.getSourceType()!=null) {
				for (int sourceType : scDTO.getSourceType()) {
					TransactionType transactionType = new TransactionType();
					transactionType.setTransactionType(transationType);

					ServiceChargeRuleTxn scTxn = new ServiceChargeRuleTxn();
					scTxn.setServiceChargeRule(serviceChargeRule);
					scTxn.setTransactionType(transactionType);
					scTxn.setSourceType(sourceType);

					scManagementDao.save(scTxn);
				} 
			}
		}

	}
	private void saveSCRuleValueList(Float[] scPerc,Long[] scFixed,Long[] dLimit,Long[] minSC,Long[] maxSC,Long[] minTxn,Long[] maxTxn,ServiceChargeRule scRule){

		for (int i = 0; i < scPerc.length; i++){	
			ServiceChargeRuleValue scRuleValue = new ServiceChargeRuleValue();
			if(scPerc[i]!=null && scFixed[i]!=null && dLimit[i]!=null && minSC[i]!=null && maxSC[i]!=null && minTxn[i]!=null && maxTxn[i]!=null){
			scRuleValue.setServiceChargePct(scPerc[i]);
			scRuleValue.setServiceChargeFxd(scFixed[i]);
			scRuleValue.setDiscountLimit(dLimit[i]);
			scRuleValue.setMinServiceCharge(minSC[i]);
			scRuleValue.setMaxServiceCharge(maxSC[i]);
			scRuleValue.setMinTxnValue(minTxn[i]);
			scRuleValue.setMaxTxnValue(maxTxn[i]);
			scRuleValue.setServiceChargeRule(scRule);
			scManagementDao.save(scRuleValue);
			}
		}

	}

	private void saveSCApplicableTimeList(Integer[] fromHours,Integer[] toHours,Integer[] days,ServiceChargeRule scRule){

		for (int i = 0; i < fromHours.length; i++){	
			System.out.println(fromHours[i]+"\t"+toHours[i]+"\t"+days[i]);	

			if(days[i]==8){
				for (int j = 1; j <8; j++) {				 
					ScapplicableTime scAppTime=new ScapplicableTime();
					scAppTime.setFromhh(fromHours[i]);
					scAppTime.setTohh(toHours[i]);
					scAppTime.setDay(j);
					scAppTime.setServiceChargeRule(scRule);
					scManagementDao.save(scAppTime);				
				}
			}else{				
				ScapplicableTime scAppTime=new ScapplicableTime();
				scAppTime.setFromhh(fromHours[i]);
				scAppTime.setTohh(toHours[i]);
				scAppTime.setDay(days[i]);
				scAppTime.setServiceChargeRule(scRule);
				scManagementDao.save(scAppTime);				
			}
		}

	}

	public void getServiceChargeRule(Long scRuleId,Map<String,Object> model,String locale) throws EOTException {

		ServiceChargeRule serviceChargeRule = scManagementDao.getServiceChargeRule(scRuleId) ;
		if(serviceChargeRule == null){
			throw new EOTException(ErrorConstants.SERVICE_CHARGE_NOT_DEFINED);
		}

		ServiceChargeDTO serviceChargeDTO=new ServiceChargeDTO();
		serviceChargeDTO.setServiceChargeRuleId(serviceChargeRule.getServiceChargeRuleId());
		serviceChargeDTO.setRuleName(serviceChargeRule.getServiceChargeRuleName());
		serviceChargeDTO.setRuleLevel(serviceChargeRule.getRuleLevel());
		serviceChargeDTO.setApplicableFrom(DateUtil.formatDateToStr(serviceChargeRule.getApplicableFrom()));
		serviceChargeDTO.setApplicableTo(DateUtil.formatDateToStr(serviceChargeRule.getApplicableTo()));
		serviceChargeDTO.setTimeZone(serviceChargeRule.getTimeZone());
		serviceChargeDTO.setTimeZoneId(serviceChargeRule.getTimeZone().getTimeZoneId());
		serviceChargeDTO.setImposedOn(serviceChargeRule.getImposedOn());
		serviceChargeDTO.setProfileId(serviceChargeRule.getReferenceId());



		Set<ServiceChargeRuleTxn> transactions=serviceChargeRule.getServiceChargeRuleTxns();
		
		if(transactions.size()!=0){
			Integer[] sourceType = new Integer[transactions.size()];
			Integer[] txntype=new Integer[transactions.size()];int i=0;
			for (ServiceChargeRuleTxn txn : transactions) {			
				txntype[i]=txn.getTransactionType().getTransactionType();			
				sourceType[i] = txn.getSourceType();
				i++;
			}
			serviceChargeDTO.setTransactions(txntype);	
			serviceChargeDTO.setSourceType(sourceType);			

		}
		serviceChargeDTO.setScRuleTxns(serviceChargeRule.getServiceChargeRuleTxns());
		
		Set<ServiceChargeRuleValue> serviceChargeRuleValues = serviceChargeRule.getServiceChargeRuleValues();
		
		ArrayList<ServiceChargeRuleValue> sortedSCRVList=new ArrayList<>();
		sortedSCRVList.addAll(serviceChargeRuleValues);
		Collections.sort(sortedSCRVList, new Comparator<ServiceChargeRuleValue>(){
			   public int compare(ServiceChargeRuleValue o1, ServiceChargeRuleValue o2){
			      return (int)o1.getMaxTxnValue().longValue() - (int)o2.getMaxTxnValue().longValue();
			   }
			});
		//Collections.sort(sortedSCRVList, Comparator.comparing(ServiceChargeRuleValue::getMinTxnValue));
		
		serviceChargeDTO.setScRuleValue(sortedSCRVList);
		
		serviceChargeDTO.setScDays(serviceChargeRule.getScapplicableTimes());
		serviceChargeDTO.setScSubscriptions(serviceChargeRule.getServiceChargeSubscriptions());

		model.put("masterData", getMasterData(locale));
		model.put("serviceChargeDTO",serviceChargeDTO);

	}

	@Override
	public void searchServiceChargeRules(String userName, SCSearchDTO scSearchDTO, Map<String, Object> model) throws EOTException {

		WebUser user = webUserDao.getUser(userName);
		if( user == null ){
			throw new EOTException(ErrorConstants.INVALID_USER);
		}

		int referenceId = 0 ;

		if(EOTConstants.RULE_LEVEL_BANK_GROUP == scSearchDTO.getRuleLevel()){
			referenceId = scSearchDTO.getBankGroupId() ;
		}else if(EOTConstants.RULE_LEVEL_CUSTOMER_PROFILE == scSearchDTO.getRuleLevel()){
			referenceId = scSearchDTO.getProfileId() ;
		}

		Page page = scManagementDao.searchServiceChargeRules(scSearchDTO.getRuleLevel(),referenceId,scSearchDTO.getPageNumber() != null ?scSearchDTO.getPageNumber() : 1);
		page.requestPage = "listServiceChargeRules.htm";
		model.put("page", page);
		model.put("scSearchDTO", scSearchDTO);

		if(user.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_ADMIN || user.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_BACKOFFICE_LEAD || user.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_SUPPORT_LEAD){

			model.put("bankGroupList", bankDao.getAllBankGroups());
			model.put("bankList",bankDao.getActiveBanks());
			model.put("profileList" ,scSearchDTO.getProfileId()!=null?scManagementDao.getCustomerProfilesByBankId(scSearchDTO.getBankId()):null);

		}else if(user.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_GROUP_ADMIN){
			BankGroupAdmin admin = webUserDao.getbankGroupAdmin(userName);
			List<BankGroup> list = new ArrayList<BankGroup>();
			list.add(admin.getBankGroup());
			model.put("bankGroupList" ,list );
			model.put("bankList",bankDao.getBanksByGroupId(admin.getBankGroup().getBankGroupId()));
			model.put("profileList",scSearchDTO.getProfileId()!=null? scManagementDao.getCustomerProfilesByBankId(scSearchDTO.getBankId()):null);
		}else if(user.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN || user.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPER_ADMIN || user.getWebUserRole().getRoleId() == EOTConstants.ROLEID_PARAMETER || user.getWebUserRole().getRoleId() == EOTConstants.ROLEID_AUDIT || user.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPPORT_BANK
				|| user.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BRANCH_MANAGER || user.getWebUserRole().getRoleId() == EOTConstants.ROLEID_RELATIONSHIP_MANAGER || user.getWebUserRole().getRoleId() == EOTConstants.ROLEID_PERSONAL_RELATIONSHIP_EXCE){
			Bank bank=webUserDao.getBankTeller(userName).getBank();
			if(bank.getBankGroup()!=null){
				List<BankGroup> list = new ArrayList<BankGroup>() ;	list.add(bank.getBankGroup());
				model.put( "bankGroupList" ,list );}
			List<Bank> bankList = new ArrayList<Bank>() ;bankList.add(webUserDao.getBankTeller(userName).getBank());
			model.put("bankList",bankList);
			model.put( "profileList" , scManagementDao.getCustomerProfilesByBankId(webUserDao.getBankTeller(userName).getBank().getBankId()));
		}

	}

	public List<Bank> getBanksByGroupId(Integer bankGroupId) throws EOTException{
		return bankDao.getBanksByGroupId(bankGroupId);
	}

	@Override
	public List<CustomerProfiles> getCustomerProfilesByBankId(Integer bankId) throws EOTException{
		return scManagementDao.getCustomerProfilesByBankId(bankId);
	}

	public int compare(Calendar c1, Calendar c2) {
		if (c1.get(Calendar.YEAR) != c2.get(Calendar.YEAR)) 
			return c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR);
		if (c1.get(Calendar.MONTH) != c2.get(Calendar.MONTH)) 
			return c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH);
		return c1.get(Calendar.DAY_OF_MONTH) - c2.get(Calendar.DAY_OF_MONTH);
	}

	private void saveSubscriptionChargeList(Integer[] subscriptionType, Double[] costPerTransaction, Integer[] numberOfTxn, ServiceChargeRule serviceChargeRule){
		for (int i = 0; i < subscriptionType.length; i++){

			ServiceChargeSubscription serviceChargeSubscription = new ServiceChargeSubscription();

			serviceChargeSubscription.setSubscriptionType(subscriptionType[i]);
			serviceChargeSubscription.setCostPerSubscription(costPerTransaction[i]);
			serviceChargeSubscription.setNumberOfTxn(numberOfTxn[i]);
			serviceChargeSubscription.setServiceChargeRule(serviceChargeRule);

			scManagementDao.save(serviceChargeSubscription);
		}
	}
}
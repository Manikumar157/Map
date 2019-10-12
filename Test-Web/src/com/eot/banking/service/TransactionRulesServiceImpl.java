package com.eot.banking.service;

import java.util.ArrayList;
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

import com.eot.banking.common.EOTConstants;
import com.eot.banking.dao.BankDao;
import com.eot.banking.dao.CustomerDao;
import com.eot.banking.dao.LocationDao;
import com.eot.banking.dao.SCManagementDao;
import com.eot.banking.dao.TransactionRulesDao;
import com.eot.banking.dao.WebUserDao;
import com.eot.banking.dto.TransactionRulesDTO;
import com.eot.banking.dto.TxnRuleSearchDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.utils.Page;
import com.eot.entity.Bank;
import com.eot.entity.BankGroup;
import com.eot.entity.BankGroupAdmin;
import com.eot.entity.BankTellers;
import com.eot.entity.CustomerProfiles;
import com.eot.entity.TransactionRule;
import com.eot.entity.TransactionRuleTxn;
import com.eot.entity.TransactionRuleValues;
import com.eot.entity.TransactionType;
import com.eot.entity.TransactionTypesDesc;
import com.eot.entity.WebUser;

@Service("transactionRulesService")
@Transactional(readOnly=true)
public class TransactionRulesServiceImpl implements TransactionRulesService {

	@Autowired
	private TransactionRulesDao transactionRulesDao;	
	@Autowired
	private SCManagementDao scManagementDao;
	@Autowired
	private LocationDao locationDao;
	@Autowired
	private BankDao bankDao;
	@Autowired
	private CustomerDao customerDao;
	@Autowired
	private WebUserDao webUserDao;

	@Override
	public Map<String, List> getMasterData(String locale) {
		Map<String,List> model = new HashMap<String,List>();
		model.put("timeZoneList", locationDao.getTimeZones());
		model.put("transTypeList", transactionRulesDao.getTransactionTypesWithTxnRules(locale.substring(0, 2)));
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
	public void saveOrUpdateTransactionRules(TransactionRulesDTO txnDTO) throws EOTException {

		TransactionRule  transactionRule = null ;	
		
		validateTxnRule( txnDTO );
		
		if(txnDTO.getTransactionRuleId() == null ){

			transactionRule = new TransactionRule();
		}
		else{

			transactionRule =  transactionRulesDao.getTransactionRule(txnDTO.getTransactionRuleId());

			transactionRulesDao.deleteTransactionRuleValues(transactionRule.getTransactionRuleId());
			transactionRulesDao.deleteTransactionRuleTxns(transactionRule.getTransactionRuleId());
		}

		if(txnDTO.getRuleLevel() == EOTConstants.RULE_LEVEL_GLOBAL || txnDTO.getRuleLevel()==EOTConstants.RULE_LEVEL_INTER_BANK){
			transactionRule.setReferenceId(0);	

		}else if( txnDTO.getRuleLevel() == EOTConstants.RULE_LEVEL_BANK_GROUP  ){
			transactionRule.setReferenceId(getGroupAdmin().getBankGroup().getBankGroupId());

		}else if( txnDTO.getRuleLevel() == EOTConstants.RULE_LEVEL_CUSTOMER_PROFILE ) {

			transactionRule.setReferenceId(txnDTO.getProfileId());
		}	

		for(int i:txnDTO.getSourceType()){
			//TransactionType transactionType=new TransactionType();
			//transactionType.setTransactionType(txnDTO.getTransactions());
			
			TransactionType transactionType = transactionRulesDao.getTransactionTypeById(txnDTO.getTransactions());
			
			TransactionRuleTxn transactionRuleTxn=new TransactionRuleTxn();
			transactionRuleTxn.setTransactionType(transactionType);
			transactionRuleTxn.setTransactionRule(transactionRule);
			transactionRuleTxn.setSourceType(i);	
			transactionRulesDao.save(transactionRuleTxn);
		}			

		transactionRule.setMaxValueLimit(txnDTO.getMaxValueLimit());
		transactionRule.setRuleType(txnDTO.getRuleType());
		transactionRule.setRuleLevel(txnDTO.getRuleLevel());
		if(txnDTO.getApprovalLimit()!=null)
		{
		transactionRule.setApprovalLimit(txnDTO.getApprovalLimit());
		}else {
			transactionRule.setApprovalLimit(0L);	
		}
		transactionRulesDao.saveOrUpdate(transactionRule);		

		List<Object> txnRuleValueList=getTxnRuleValueList(transactionRule,txnDTO.getMaxCumValueLimit(),txnDTO.getMaxNumTimes(),txnDTO.getAllowedPer(),txnDTO.getAllowedPerUnit());
		transactionRulesDao.saveList(txnRuleValueList);

	}
	
	

	private List<Object> getTxnRuleValueList(TransactionRule transactionRule,Long[] maxCumValueLimit,Long[] maxNumTimes,Integer[] allowedPer,Integer[] allowedPerUnit){

		List<Object> txnRuleValueList=new ArrayList<Object>();	

		for (int i = 0; i < maxCumValueLimit.length; i++){	

			TransactionRuleValues transactionRuleValues=new TransactionRuleValues();
			transactionRuleValues.setTransactionRule(transactionRule);
			transactionRuleValues.setMaxCumValueLimit(maxCumValueLimit[i]);
			transactionRuleValues.setMaxNumTimes(maxNumTimes[i]);
			transactionRuleValues.setAllowedPer(allowedPer[i]);
			transactionRuleValues.setAllowedPerUnit(allowedPerUnit[i]);				
			txnRuleValueList.add(transactionRuleValues);	

		}

		return txnRuleValueList;		
	}
	
	public void validateTxnRule( TransactionRulesDTO txnDTO ) throws EOTException {
		
		List<TransactionRule> txnRules=transactionRulesDao.getTransactionRulesByRuleLevel(txnDTO.getRuleLevel());
		Integer[] apu=txnDTO.getAllowedPerUnit();
						
		for (int i=0;i<txnRules.size();i++){
			
			if( txnDTO.getTransactionRuleId()!=null ){
				if( txnDTO.getTransactionRuleId().equals(txnRules.get(i).getTransactionRuleId()) )
				{				
					if( (i+1)==txnRules.size() )
						break;
					else 
						i=i+1;
				}
			}
			
			if( txnDTO.getRuleLevel() != EOTConstants.RULE_LEVEL_CUSTOMER_PROFILE || 
					(txnDTO.getProfileId()!=null ?( txnDTO.getProfileId().equals(txnRules.get(i).getReferenceId())?true:false):false) ){
			
			Set<TransactionRuleTxn> txnRuleSet=txnRules.get(i).getTransactionRuleTxns();
			Set<TransactionRuleValues> txnVal=txnRules.get(i).getTransactionRuleValues();
						
				/*commented by bidyut
				 * for (TransactionRuleTxn txn : txnRuleSet) {
				 * 
				 * if(txn.getTransactionType().getTransactionType().equals(txnDTO.
				 * getTransactions())){ for(int sourceType:txnDTO.getSourceType()){
				 * if(sourceType==txn.getSourceType()){ for (int j = 0; j < apu.length; j++) {
				 * for (TransactionRuleValues val : txnVal) {
				 * if(apu[j].equals(val.getAllowedPerUnit())) throw new
				 * EOTException(ErrorConstants.TR_RULE_EXIST); } }} } } }
				 */
				for (TransactionRuleTxn txn : txnRuleSet) {

					if (txn.getTransactionType().getTransactionType().equals(txnDTO.getTransactions())) {
						for (int sourceType : txnDTO.getSourceType()) {
							if (sourceType == txn.getSourceType()) {
								for (int j = 0; j < apu.length; j++) {
									for (TransactionRuleValues val : txnVal) {
										if (apu[j].equals(val.getAllowedPerUnit())&& txnDTO.getProfileId().intValue()==txnRules.get(i).getReferenceId())
											throw new EOTException(ErrorConstants.TR_RULE_EXIST);
									}
								}
							}
						}
					}
				}
			 
			}
		
		}
		
	}

	public void getTransactionRuleDetails(Long trRuleId,Map<String,Object> model,String locale) throws EOTException{

		TransactionRule transactionRule=transactionRulesDao.getTransactionRule(trRuleId);

		if(transactionRule == null){
			throw new EOTException(ErrorConstants.TXN_RULE_NOT_DEFINED);
		}

		TransactionRulesDTO transactionRulesDTO = new TransactionRulesDTO();

		transactionRulesDTO.setTransactionRuleId(transactionRule.getTransactionRuleId());
		Set<TransactionRuleTxn> transactions=transactionRule.getTransactionRuleTxns();
		Integer[] sourceType = new Integer[transactions.size()];int i=0;

		for (TransactionRuleTxn transactionRuleTxn : transactions) {			
			sourceType[i] = transactionRuleTxn.getSourceType();
			i++;
			transactionRulesDTO.setTransactions(transactionRuleTxn.getTransactionType().getTransactionType());
			@SuppressWarnings("unchecked")
			Set<TransactionTypesDesc> transactionTypesDescSet = transactionRuleTxn.getTransactionType().getTransactionTypesDescs();
			for(TransactionTypesDesc transactionTypesDesc : transactionTypesDescSet){
				if(transactionRuleTxn.getTransactionType().getTransactionType().equals(transactionTypesDesc.getComp_id().getTransactionType()) && 
						transactionTypesDesc.getComp_id().getLocale().equals(locale.substring(0, 2))){
					transactionRulesDTO.setTransDesc(transactionTypesDesc.getDescription());
				}
			}
			//transactionRulesDTO.setTransDesc(transactionRuleTxn.getTransactionType().getDescription());
			transactionRulesDTO.setSourceType(sourceType);
		}		
		transactionRulesDTO.setMaxValueLimit(transactionRule.getMaxValueLimit());
		transactionRulesDTO.setRuleType(transactionRule.getRuleType());
		transactionRulesDTO.setRuleLevel(transactionRule.getRuleLevel());
		transactionRulesDTO.setProfileId(transactionRule.getReferenceId());
		transactionRulesDTO.setApprovalLimit(transactionRule.getApprovalLimit());

		transactionRulesDTO.setTrRuleValues(transactionRule.getTransactionRuleValues());		
		model.put("masterData", getMasterData(locale));
		model.put("transactionRulesDTO",transactionRulesDTO);
		model.put("profileId",transactionRule.getReferenceId());
	}

	@Override
	public void searchTransactionRules(String userName,TxnRuleSearchDTO trSearchDTO, Map<String, Object> model) throws EOTException {

		WebUser user = webUserDao.getUser(userName);
		if( user == null ){
			throw new EOTException(ErrorConstants.INVALID_USER);
		}
		
		int referenceId = 0 ;
		
		if(EOTConstants.RULE_LEVEL_BANK_GROUP == trSearchDTO.getRuleLevel()){
			referenceId = trSearchDTO.getBankGroupId() ;
		}else if(EOTConstants.RULE_LEVEL_CUSTOMER_PROFILE == trSearchDTO.getRuleLevel()){
			referenceId = trSearchDTO.getProfileId() ;
		}
//		referenceId = trSearchDTO.getProfileId() ;//bidyut: adding profile skipping rule level condition
		Integer pageNumber = null != trSearchDTO.getPageNumber() ? trSearchDTO.getPageNumber() : 1;
		Page page = transactionRulesDao.searchTransactionRulesWithProfile(trSearchDTO.getRuleLevel(),referenceId,pageNumber);
		page.requestPage = "listTransactionRules.htm";
		model.put("page", page);
		model.put("trSearchDTO", trSearchDTO);
		
		if(user.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_ADMIN || user.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_BACKOFFICE_LEAD || user.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_SUPPORT_LEAD){
			model.put("bankGroupList", bankDao.getAllBankGroups());
			model.put("bankList",bankDao.getActiveBanks());
			model.put("profileList",trSearchDTO.getProfileId()!=null? scManagementDao.getCustomerProfilesByBankId(trSearchDTO.getBankId()):null);
		}else if(user.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_GROUP_ADMIN){
			BankGroupAdmin admin = webUserDao.getbankGroupAdmin(userName);
			List<BankGroup> list = new ArrayList<BankGroup>() ;list.add(admin.getBankGroup());
			model.put("bankGroupList" ,list );
			model.put("bankList",bankDao.getBanksByGroupId(admin.getBankGroup().getBankGroupId()));
			model.put("profileList",trSearchDTO.getProfileId()!=null? scManagementDao.getCustomerProfilesByBankId(trSearchDTO.getBankId()):null);
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
		return customerDao.getCustomerProfilesByBankId(bankId);
	}
	
	

}

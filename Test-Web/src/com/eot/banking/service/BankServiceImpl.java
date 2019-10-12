package com.eot.banking.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eot.banking.common.EOTConstants;
import com.eot.banking.dao.BankDao;
import com.eot.banking.dao.LocationDao;
import com.eot.banking.dao.OperatorDao;
import com.eot.banking.dao.SCManagementDao;
import com.eot.banking.dao.TransactionRulesDao;
import com.eot.banking.dao.WebUserDao;
import com.eot.banking.dto.BankDTO;
import com.eot.banking.dto.BranchDTO;
import com.eot.banking.dto.CardDto;
import com.eot.banking.dto.InterBankSCDTO;
import com.eot.banking.dto.WebUserDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.utils.EOTUtil;
import com.eot.banking.utils.FileUtil;
import com.eot.banking.utils.Page;
import com.eot.entity.Account;
import com.eot.entity.AccountHead;
import com.eot.entity.AccountHeadMapping;
import com.eot.entity.Bank;
import com.eot.entity.BankAgreementModel;
import com.eot.entity.BankGroup;
import com.eot.entity.BankGroupAdmin;
import com.eot.entity.Branch;
import com.eot.entity.City;
import com.eot.entity.ClearingHousePoolMember;
import com.eot.entity.Country;
import com.eot.entity.Currency;
import com.eot.entity.CustomerCard;
import com.eot.entity.Quarter;
import com.eot.entity.ServiceChargeSplit;
import com.eot.entity.TimeZone;
import com.eot.entity.TransactionType;
import com.eot.entity.WebUser;
import com.thinkways.kms.KMS;
import com.thinkways.kms.security.KMSSecurityException;
import com.thinkways.util.HexString;

@Service("bankService")
@Transactional(readOnly=true)
public class BankServiceImpl implements BankService{

	private KMS kmsHandle;

	@Autowired
	private WebUserService webUserService;
	@Autowired
	private BankDao bankDao;
	@Autowired
	private LocationDao locationDao;
	@Autowired
	private OperatorDao operatorDao;
	@Autowired
	private WebUserDao webUserDao;
	@Autowired
	private TransactionRulesDao transactionRulesDao;
	@Autowired
	private SCManagementDao scManagementDao;

	public void setKmsHandle(KMS kmsHandle) {

		this.kmsHandle = kmsHandle;
	}

	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=Throwable.class)
	public void addBankDetails(BankDTO bankDTO,String locale)throws EOTException{

		Bank bank=bankDao.getBankByName(bankDTO.getBankName());

		if(bank!=null){
			throw new EOTException(ErrorConstants.BANK_NAME_EXIST);
		}

		bank=bankDao.getBankCode(bankDTO.getBankCode());

		if(bank!=null){
			throw new EOTException(ErrorConstants.BANK_CODE_EXIST);
		}

		bank=bankDao.getBankBySwiftCode(bankDTO.getSwiftCode());

		if(bank!=null){
			throw new EOTException(ErrorConstants.SWIFT_CODE_EXIST);
		}

		bank=bankDao.getEOTCardBankCode(bankDTO.getEotCardBankCode());

		if(bank!=null){
			throw new EOTException(ErrorConstants.EOT_CARD_BANK_CODE_EXIST);
		}

		bank=new Bank();
		bank.setBankName(bankDTO.getBankName());
		bank.setSwiftCode(bankDTO.getSwiftCode());
		bank.setSwiftBranch(bankDTO.getSwiftBranch());
		bank.setStatus(bankDTO.getStatus());
		bank.setBankShortName(bankDTO.getBankShortName());
		bank.setBankCode(bankDTO.getBankCode().toUpperCase());
		bank.setDescription(bankDTO.getDescription());
		bank.setEotCardBankCode(bankDTO.getEotCardBankCode());
		bank.setTerminalId(bankDTO.getTerminalId());
		bank.setOutletNumber(bankDTO.getOutletNumber());
		bank.setKycFlag(bankDTO.getKycFlag());
		
		//No need to fetch this value from UI
		bank.setCollaborationLogo("");
		bank.setApplicationName("mGurush-Wallet-Web");
		bank.setChannelCost(0.0D);

		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		WebUser webUser=webUserDao.getUser(userName);	
		BankGroup bankGroup=new BankGroup();
		if(webUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_EOT_ADMIN){
			if(bankDTO.getBankGroupId()!=null ){

				bankGroup.setBankGroupId(bankDTO.getBankGroupId());
				bank.setBankGroup(bankGroup);

			}else{
				bank.setBankGroup(null);

			}

		}else if(webUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BANK_GROUP_ADMIN){
			BankGroupAdmin bankGroupAdmin=bankDao.getBankGroupByUsername(webUser.getUserName());		
			bankGroup.setBankGroupId(bankGroupAdmin.getBankGroup().getBankGroupId());
			bank.setBankGroup(bankGroup);
		}		
		Currency curr = new Currency();
		curr.setCurrencyId(bankDTO.getCurrencyId());
		bank.setCurrency(curr);

		Country country=new Country();
		country.setCountryId(bankDTO.getCountryId());
		bank.setCountry(country);

		TimeZone timeZone=new TimeZone();
		timeZone.setTimeZoneId(new Integer( bankDTO.getTimezoneId()));
		bank.setTimeZone(timeZone);

		bankDao.save(bank);
		//bankDTO.setBankId(bank.getBankId());

		Long accSeq = bankDao.getNextAccountNumberSequence() ;

		Account acc = new Account();

		acc.setAccountNumber(EOTUtil.generateAccountNumber(accSeq));
		acc.setAccountType(EOTConstants.ACCOUNT_TYPE_PERSONAL);
		acc.setActive(EOTConstants.ACCOUNT_STATUS_ACTIVE);
		acc.setAlias(EOTConstants.ACCOUNT_ALIAS_BANK);
		acc.setCurrentBalance(0.0);
		acc.setCurrentBalanceType(EOTConstants.ACCOUNT_BALANCE_TYPE_CREDIT);
		acc.setReferenceId(bank.getBankId().toString());
		acc.setReferenceType(EOTConstants.REFERENCE_TYPE_BANK);
		bankDao.save(acc);

		bank.setAccount(acc);
		bankDao.update(bank);

		AccountHead accountHead = new AccountHead();
		accountHead.setHeaderId(EOTConstants.ACCOUNT_HEAD_BANK_ACCOUNT);

		AccountHeadMapping mapping = new AccountHeadMapping();

		mapping.setAccountNumber(acc.getAccountNumber());
		mapping.setAccountHead(accountHead);
		mapping.setBank(bank);

		bankDao.save(mapping);

		List<AccountHead> accountHeads = bankDao.getAccountHeadsForBook(EOTConstants.BOOKID_BANK);

		for(AccountHead head:accountHeads){

			Account account = new Account();

			account.setAccountNumber(EOTUtil.generateAccountNumber(++accSeq));
			account.setAccountType(EOTConstants.ACCOUNT_TYPE_PERSONAL);
			account.setActive(EOTConstants.ACCOUNT_STATUS_ACTIVE);
			account.setAlias(head.getAccountHead());
			account.setCurrentBalance(0.0);
			account.setCurrentBalanceType(EOTConstants.ACCOUNT_BALANCE_TYPE_CREDIT);
			account.setReferenceId(bank.getBankId()+"");
			account.setReferenceType(EOTConstants.REFERENCE_TYPE_BANK);
			bankDao.save(account);

			AccountHeadMapping headMapping = new AccountHeadMapping();

			headMapping.setAccountNumber(account.getAccountNumber());
			headMapping.setAccountHead(head);
			headMapping.setBank(bank);

			bankDao.save(headMapping);

		}

		List<TransactionType> txnTypes = transactionRulesDao.getTransactionTypesWithServiceCharge(locale.substring(0, 2));

		for(TransactionType txnType:txnTypes){

			ServiceChargeSplit tax = new ServiceChargeSplit();

			tax.setAmountType(EOTConstants.AMT_TYPE_TAX_AMOUNT);
			tax.setBank(bank);
			tax.setIsInter(EOTConstants.TXN_TYPE_INTRA_BANK);
			tax.setServiceChargePct(bankDTO.getGovtTax());
			tax.setTransactionType(txnType);

			transactionRulesDao.save(tax);

			ServiceChargeSplit gimShare = new ServiceChargeSplit();

			gimShare.setAmountType(EOTConstants.AMT_TYPE_EOT_SHARE);
			gimShare.setBank(bank);
			gimShare.setIsInter(EOTConstants.TXN_TYPE_INTRA_BANK);
			gimShare.setServiceChargePct(bankDTO.getEotCharge());
			gimShare.setTransactionType(txnType);

			transactionRulesDao.save(gimShare);

			ServiceChargeSplit bankRev = new ServiceChargeSplit();

			bankRev.setAmountType(EOTConstants.AMT_TYPE_BANK_REV);
			bankRev.setBank(bank);
			bankRev.setIsInter(EOTConstants.TXN_TYPE_INTRA_BANK);
			bankRev.setServiceChargePct(bankDTO.getBankCharge());
			bankRev.setTransactionType(txnType);

			transactionRulesDao.save(bankRev);

			ServiceChargeSplit stampFee = new ServiceChargeSplit();

			stampFee.setAmountType(EOTConstants.AMT_TYPE_STAMP_FEE);
			stampFee.setBank(bank);
			stampFee.setIsInter(EOTConstants.TXN_TYPE_INTRA_BANK);
			stampFee.setServiceChargePct(bankDTO.getStampFee());
			stampFee.setTransactionType(txnType);

			transactionRulesDao.save(stampFee);

		}

		for(int i=0;i<bankDTO.getAgreementFromField().length;i++){
			BankAgreementModel bankAgreement=new BankAgreementModel();
			bankAgreement.setBank(bank);
			bankAgreement.setAgreementType((bankDTO.getAgreementModelId())[i]);
			bankAgreement.setAgreementFrom(bankDTO.getAgreementFromField()[i]);
			bankAgreement.setAgreementTo(bankDTO.getAgreementToField()[i]);
			bankDao.save(bankAgreement);
		}
		int i = 0 ;
		for(Integer ch : bankDTO.getClearingHouses()){
			ClearingHousePoolMember clearingPool=new ClearingHousePoolMember();
			clearingPool.setClearingPoolId(ch);
			clearingPool.setBank(bank);
			clearingPool.setPoolPriority(bankDTO.getClearingHousePriorities()[i++]);
			bankDao.save(clearingPool);
		}
		List<BranchDTO> branchDTOList = FileUtil.parseBranchFile(bankDTO.getBranchFile());

		List<String> csvInvalidQuarter = new ArrayList<String>();
		List<String> branchCodeLength=new ArrayList<String>();
		List<String> duplicateBranch = new ArrayList<String>();
		List<String> duplicateBranchCode = new ArrayList<String>();
		List<Object> branchList = new ArrayList<Object>();
		List<String> invalidBranchCode = new ArrayList<String>();
		Map<String,String> branchCodeMap=new HashMap<String,String>();
		List<Integer> branchCodeList=new ArrayList<Integer>();
		List<String> branchSlNum=new ArrayList<String>();
		List<String> csvDuplicateBranchCode = new ArrayList<String>();
		List<String> csvInValidCity = new ArrayList<String>();
		bankDTO.setBranchErrorList(csvInvalidQuarter);
		bankDTO.setBranchCodeLength(branchCodeLength);
		bankDTO.setDuplicateBranchCode(duplicateBranchCode);
		bankDTO.setDuplicateBranch(duplicateBranch);
		bankDTO.setInvalidBranchCode(invalidBranchCode);
		bankDTO.setCsvInvalidCity(csvInValidCity);
		int j=0;

		for(j=0;j<branchDTOList.size();j++){

			if(Pattern.matches("[a-zA-Z0-9]*", branchDTOList.get(j).getBranchCode())){
				continue;
			}else{
				invalidBranchCode.add(branchDTOList.get(j).getSerialNum()+"-"+branchDTOList.get(j).getBranchCode());
				continue;
			}

		}  
		if(invalidBranchCode.size()>0){

			throw new EOTException(ErrorConstants.INVALID_BRANCH_CODE_VALUES);

		}


		for(j=0;j<branchDTOList.size();j++){
			for(int k=j+1;k<branchDTOList.size();k++){

				if(branchDTOList.get(j).getBranchCode().toUpperCase().equals(branchDTOList.get(k).getBranchCode().toUpperCase())){
					branchSlNum.add(branchDTOList.get(k).getSerialNum());
					branchCodeMap.put(branchDTOList.get(k).getSerialNum(),branchDTOList.get(k).getBranchCode());
					continue;
				}
			}  
		}
		if(branchSlNum.size()>0){
			for(int k=0;k<branchSlNum.size();k++){
				branchCodeList.add(Integer.parseInt(branchSlNum.get(k)));

			}
		}
		HashSet<Integer> branchCodeSet=new HashSet<Integer>(branchCodeList);
		List<Integer> list=new ArrayList<Integer>(branchCodeSet);
		Collections.sort(list);
		for(int k=0;k<list.size();k++){
			csvDuplicateBranchCode.add(list.get(k)+"-"+branchCodeMap.get(String.valueOf(list.get(k))));
		}
		bankDTO.setCsvduplicateBranchCode(csvDuplicateBranchCode);
		if(csvDuplicateBranchCode.size()>0){
			throw new EOTException(ErrorConstants.DUPLICATE_BRANCH_CODE);
		}

		for(j=0;j<branchDTOList.size();j++){

			if(branchDTOList.get(j).getBranchCode().length()<5 ||branchDTOList.get(j).getBranchCode().length()>5){
				branchCodeLength.add(branchDTOList.get(j).getSerialNum()+"-"+branchDTOList.get(j).getBranchCode());
				continue;

			}  
		}
		if(branchCodeLength.size()>0){
			throw new EOTException(ErrorConstants.BRANCH_CODE_LENGTH);
		}
		for(j=0;j<branchDTOList.size();j++){
			Branch branchCode=bankDao.verifyBranchCode(branchDTOList.get(j).getBranchCode(), bankDTO.getCountryId());
			if(branchCode!=null){
				duplicateBranchCode.add(branchDTOList.get(j).getSerialNum()+"-"+branchDTOList.get(j).getBranchCode());
				continue;
			}
			Branch branchLocation = bankDao.getBranchFromLocation(branchDTOList.get(j).getLocation(), bank.getBankId());
			if(branchLocation!=null){
				duplicateBranch.add(branchDTOList.get(j).getSerialNum()+"-"+branchDTOList.get(j).getLocation());
				continue;
			}
		}

		if(duplicateBranch.size()>0){
			throw new EOTException(ErrorConstants.BRANCH_LOCATION_EXIST);
		}
		if(duplicateBranchCode.size()>0){
			throw new EOTException(ErrorConstants.BRANCH_CODE_EXIST);
		}


		for (BranchDTO br : branchDTOList) {

			City city = locationDao.getCity(br.getCity().trim(), bankDTO.getCountryId());
			if(city==null){
				csvInValidCity.add( br.getSerialNum() +" - " +br.getCity().trim());
				continue;
			}


			Quarter quarter = locationDao.getQuarter(br.getQuarter().trim(), city.getCityId());
			if(quarter==null){
				csvInvalidQuarter.add( br.getSerialNum() +" - " +br.getQuarter().trim());
				continue;
			}

			Branch branch = new Branch();
			branch.setCountry(country);
			branch.setCity(city);
			branch.setQuarter(quarter);
			branch.setLocation(br.getLocation().trim());
			branch.setAddress(br.getAddress().trim());
			branch.setDescription(br.getDescription().trim());
			branch.setBranchCode(br.getBranchCode().toUpperCase());
			branch.setBank(bank);
			branchList.add(branch);

		}
		if(csvInvalidQuarter.size()>0 ||csvInValidCity.size()>0 ){

			throw new EOTException(ErrorConstants.INVALID_BRANCH_FILE);
		}
		else 			
			bankDao.saveList(branchList);
	}

	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=Throwable.class )
	public void updateBankDetails(BankDTO bankDTO,String locale) throws EOTException  {

		Bank bank = bankDao.getBankByName(bankDTO.getBankId(),bankDTO.getBankName());

		if(bank!=null){
			throw new EOTException(ErrorConstants.BANK_NAME_EXIST);
		}

		bank = bankDao.getBankByIdCode(bankDTO.getBankId(),bankDTO.getBankCode());

		if(bank!=null){
			throw new EOTException(ErrorConstants.BANK_CODE_EXIST);
		}

		bank = bankDao.getBankBySwiftCode(bankDTO.getBankId(),bankDTO.getSwiftCode());

		if(bank!=null){
			throw new EOTException(ErrorConstants.SWIFT_CODE_EXIST);
		}

		bank=bankDao.getEOTCardBankCode(bankDTO.getBankId(),bankDTO.getEotCardBankCode());

		if(bank!=null){
			throw new EOTException(ErrorConstants.EOT_CARD_BANK_CODE_EXIST);
		}
		

		bank = bankDao.getBank(bankDTO.getBankId());
		bank.setBankName(bankDTO.getBankName());
		bank.setSwiftCode(bankDTO.getSwiftCode());
		bank.setSwiftBranch(bankDTO.getSwiftBranch());
		bank.setStatus(bankDTO.getStatus());
		bank.setBankShortName(bankDTO.getBankShortName());
		bank.setBankCode(bankDTO.getBankCode().toUpperCase());
		bank.setDescription(bankDTO.getDescription());
		bank.setEotCardBankCode(bankDTO.getEotCardBankCode());
		bank.setTerminalId(bankDTO.getTerminalId());
		bank.setOutletNumber(bankDTO.getOutletNumber());
		bank.setKycFlag(bankDTO.getKycFlag());
		
		//No need to fetch these values from DB
		bank.setCollaborationLogo("");
		bank.setApplicationName("mGurush-Wallet-Web");
		bank.setChannelCost(0.0D);
		
		Currency curr = new Currency();
		curr.setCurrencyId(bankDTO.getCurrencyId());
		bank.setCurrency(curr);

		Country country=new Country();
		country.setCountryId(bankDTO.getCountryId());
		bank.setCountry(country);

		List<ServiceChargeSplit> splits = bankDao.getServiceChargeSplits(bankDTO.getBankId()) ;

		if(splits.size()>0){

			for(ServiceChargeSplit split : splits){

				if(EOTConstants.AMT_TYPE_BANK_REV.equals(split.getAmountType())){
					split.setServiceChargePct(bankDTO.getBankCharge());
				}else if(EOTConstants.AMT_TYPE_EOT_SHARE.equals(split.getAmountType())){
					split.setServiceChargePct(bankDTO.getEotCharge());
				}else if(EOTConstants.AMT_TYPE_TAX_AMOUNT.equals(split.getAmountType())){
					split.setServiceChargePct(bankDTO.getGovtTax());
				}else if(EOTConstants.AMT_TYPE_STAMP_FEE.equals(split.getAmountType())){

					split.setServiceChargePct(bankDTO.getStampFee());
				}

				bankDao.update(split);

			}
		}else{
			List<TransactionType> txnTypes = transactionRulesDao.getTransactionTypesWithServiceCharge(locale);

			for(TransactionType txnType:txnTypes){

				ServiceChargeSplit tax = new ServiceChargeSplit();

				tax.setAmountType(EOTConstants.AMT_TYPE_TAX_AMOUNT);
				tax.setBank(bank);
				tax.setIsInter(EOTConstants.TXN_TYPE_INTRA_BANK);
				tax.setServiceChargePct(bankDTO.getGovtTax());
				tax.setTransactionType(txnType);

				transactionRulesDao.save(tax);

				ServiceChargeSplit gimShare = new ServiceChargeSplit();

				gimShare.setAmountType(EOTConstants.AMT_TYPE_EOT_SHARE);
				gimShare.setBank(bank);
				gimShare.setIsInter(EOTConstants.TXN_TYPE_INTRA_BANK);
				gimShare.setServiceChargePct(bankDTO.getEotCharge());
				gimShare.setTransactionType(txnType);

				transactionRulesDao.save(gimShare);

				ServiceChargeSplit bankRev = new ServiceChargeSplit();

				bankRev.setAmountType(EOTConstants.AMT_TYPE_BANK_REV);
				bankRev.setBank(bank);
				bankRev.setIsInter(EOTConstants.TXN_TYPE_INTRA_BANK);
				bankRev.setServiceChargePct(bankDTO.getBankCharge());
				bankRev.setTransactionType(txnType);

				transactionRulesDao.save(bankRev);
				ServiceChargeSplit stampFee = new ServiceChargeSplit();

				stampFee.setAmountType(EOTConstants.AMT_TYPE_STAMP_FEE);
				stampFee.setBank(bank);
				stampFee.setIsInter(EOTConstants.TXN_TYPE_INTRA_BANK);
				stampFee.setServiceChargePct(bankDTO.getStampFee());
				stampFee.setTransactionType(txnType);

				transactionRulesDao.save(stampFee);

			}
		}

		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		WebUser webUser=webUserDao.getUser(userName);	
		if(webUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_EOT_ADMIN){
			if(bankDTO.getBankGroupId()!=null ){
				BankGroup bankGroup=new BankGroup();
				bankGroup.setBankGroupId(bankDTO.getBankGroupId());
				bank.setBankGroup(bankGroup);

			}else{
				bank.setBankGroup(null);

			}

		}else if(webUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BANK_GROUP_ADMIN){
			BankGroupAdmin bankGroupAdmin=bankDao.getBankGroupByUsername(webUser.getUserName());		
			BankGroup bankGroup=new BankGroup();
			bankGroup.setBankGroupId(bankGroupAdmin.getBankGroup().getBankGroupId());
			bank.setBankGroup(bankGroup);
		}	

		TimeZone timeZone=new TimeZone();
		timeZone.setTimeZoneId(new Integer( bankDTO.getTimezoneId()));
		bank.setTimeZone(timeZone);
		bankDao.update(bank);

		Set bankAgreement= bank.getBankAgreementModels();
		bankDao.deleteList(bankAgreement);
   
		for(int j=0;j<bankDTO.getAgreementFromField().length;j++){
			
			
			BankAgreementModel bAgreement=new BankAgreementModel();
			bAgreement.setAgreementType((bankDTO.getAgreementModelId())[j]);
			bAgreement.setAgreementFrom(bankDTO.getAgreementFromField()[j]);
			bAgreement.setAgreementTo(bankDTO.getAgreementToField()[j]);
			bAgreement.setBank(bank);
			bankDao.save(bAgreement);				
		}

		bankDao.deleteList(bank.getClearingHousePoolMembers());

		int i = 0 ;
		for(Integer ch : bankDTO.getClearingHouses()){
			System.out.println("ch id :: "+ch);
			ClearingHousePoolMember clearingPool=new ClearingHousePoolMember();
			clearingPool.setClearingPoolId(ch);
			clearingPool.setBank(bank);
			clearingPool.setPoolPriority(bankDTO.getClearingHousePriorities()[i++]);
			bankDao.save(clearingPool);
		}
		List<BranchDTO> branchDTOList = FileUtil.parseBranchFile(bankDTO.getBranchFile());

		List<String> csvInvalidQuarter = new ArrayList<String>();
		List<String> branchCodeLength=new ArrayList<String>();
		List<String> duplicateBranch = new ArrayList<String>();
		List<String> duplicateBranchCode = new ArrayList<String>();
		List<String> invalidBranchCode = new ArrayList<String>();
		List<String> csvDuplicateBranchCode = new ArrayList<String>();
		List<String> csvInValidCity = new ArrayList<String>();
		bankDTO.setBranchErrorList(csvInvalidQuarter);
		bankDTO.setBranchCodeLength(branchCodeLength);
		bankDTO.setDuplicateBranchCode(duplicateBranchCode);
		bankDTO.setDuplicateBranch(duplicateBranch);
		bankDTO.setInvalidBranchCode(invalidBranchCode);
		bankDTO.setCsvInvalidCity(csvInValidCity);
		List<Object> branchList = new ArrayList<Object>();

		int j=0;
		// vineeth changes, on 07-08-2018, bug no: 5847, as the address field is mandatory in branch file adding the below code
		
				for(j=0;j<branchDTOList.size();j++){

					if(branchDTOList.get(j).getAddress().length()<4||branchDTOList.get(j).getAddress().length()>100){
					
						throw new EOTException(ErrorConstants.BRANCH_ADDRESS_LENGTH);
					}
				}
				
		// vineeth change over
				
		for(j=0;j<branchDTOList.size();j++){

			if(Pattern.matches("[a-zA-Z0-9]*", branchDTOList.get(j).getBranchCode())){
				continue;
			}else{
				invalidBranchCode.add(branchDTOList.get(j).getSerialNum()+"-"+branchDTOList.get(j).getBranchCode());
				continue;
			}

		}
		
		if(invalidBranchCode.size()>0){

			throw new EOTException(ErrorConstants.INVALID_BRANCH_CODE_VALUES);

		}
		Map<String,String> branchCodeMap=new HashMap<String,String>();
		List<Integer> branchCodeList=new ArrayList<Integer>();
		List<String> branchSlNum=new ArrayList<String>();
		for(j=0;j<branchDTOList.size();j++){
			for(int k=j+1;k<branchDTOList.size();k++){

				if(branchDTOList.get(j).getBranchCode().toUpperCase().equals(branchDTOList.get(k).getBranchCode().toUpperCase())){
					branchSlNum.add(branchDTOList.get(k).getSerialNum());
					branchCodeMap.put(branchDTOList.get(k).getSerialNum(),branchDTOList.get(k).getBranchCode());
					continue;
				}
			}  
		}
		if(branchSlNum.size()>0){
			for(int k=0;k<branchSlNum.size();k++){
				branchCodeList.add(Integer.parseInt(branchSlNum.get(k)));

			}
		}
		HashSet<Integer> branchCodeSet=new HashSet<Integer>(branchCodeList);
		List<Integer> list=new ArrayList<Integer>(branchCodeSet);
		Collections.sort(list);
		for(int k=0;k<list.size();k++){
			csvDuplicateBranchCode.add(list.get(k)+"-"+branchCodeMap.get(String.valueOf(list.get(k))));
		}
		bankDTO.setCsvduplicateBranchCode(csvDuplicateBranchCode);
		if(csvDuplicateBranchCode.size()>0){
			throw new EOTException(ErrorConstants.DUPLICATE_BRANCH_CODE);
		}
		for(j=0;j<branchDTOList.size();j++){

			if(branchDTOList.get(j).getBranchCode().length()<5||branchDTOList.get(j).getBranchCode().length()>5){
				branchCodeLength.add(branchDTOList.get(j).getSerialNum()+"-"+branchDTOList.get(j).getBranchCode());
				continue;

			}
		}
		if(branchCodeLength.size()>0){
			throw new EOTException(ErrorConstants.BRANCH_CODE_LENGTH);
		}
		for(j=0;j<branchDTOList.size();j++){
			Branch branchCode=bankDao.verifyBranchCode(branchDTOList.get(j).getBranchCode(), bankDTO.getCountryId());
			if(branchCode!=null){
				duplicateBranchCode.add(branchDTOList.get(j).getSerialNum()+"-"+branchDTOList.get(j).getBranchCode());
				continue;
			}
			Branch branchLocation = bankDao.getBranchFromLocation(branchDTOList.get(j).getLocation(), bank.getBankId());
			if(branchLocation!=null){
				duplicateBranch.add(branchDTOList.get(j).getSerialNum()+"-"+branchDTOList.get(j).getLocation());
				continue;
			}
		}

		if(duplicateBranch.size()>0){
			// change by vineeth, on 08-08-2018, location is representing branch name in branch file
			//	throw new EOTException(ErrorConstants.BRANCH_LOCATION_EXIST);
			throw new EOTException(ErrorConstants.BRANCH_NAME_EXIST);
		}
		if(duplicateBranchCode.size()>0){
			throw new EOTException(ErrorConstants.BRANCH_CODE_EXIST);
		}

		for (BranchDTO br : branchDTOList) {

			City city = locationDao.getCity(br.getCity().trim(), bankDTO.getCountryId());
			if(city==null){
				csvInValidCity.add( br.getSerialNum() +" - " +br.getCity().trim());
				continue;
			}


			Quarter quarter = locationDao.getQuarter(br.getQuarter().trim(), city.getCityId());
			if(quarter==null){
				csvInvalidQuarter.add( br.getSerialNum() +" - " +br.getQuarter().trim());
				continue;
			}

			Branch branch = new Branch();
			branch.setCountry(country);
			branch.setCity(city);
			branch.setQuarter(quarter);
			branch.setLocation(br.getLocation().trim());
			branch.setAddress(br.getAddress().trim());
			branch.setDescription(br.getDescription().trim());
			branch.setBranchCode(br.getBranchCode().toUpperCase());
			branch.setBank(bank);
			branchList.add(branch);

		}
		if(csvInvalidQuarter.size()>0 ||csvInValidCity.size()>0 ){

			throw new EOTException(ErrorConstants.INVALID_BRANCH_FILE);
		}
		else 			
			bankDao.saveList(branchList);
	}

	@Override
	public BankDTO getBankDetails(Integer bankid,String locale) throws EOTException{
		BankDTO dto=new BankDTO();
		Bank bank = bankDao.getBank(bankid);

		if(bank==null){
			throw new EOTException(ErrorConstants.INVALID_BANK);
		}
		dto.setBankId(bank.getBankId());
		dto.setBankName(bank.getBankName());
		dto.setSwiftCode(bank.getSwiftCode());
		dto.setSwiftBranch(bank.getSwiftBranch());
		dto.setBankCode(bank.getBankCode());
		dto.setBankShortName(bank.getBankShortName());
		dto.setDescription(bank.getDescription());
		dto.setCurrencyId(bank.getCurrency().getCurrencyId());
		dto.setCountryId(bank.getCountry().getCountryId());
		dto.setTimezoneId(bank.getTimeZone().getTimeZoneId());
		dto.setStatus(bank.getStatus());
		dto.setEotCardBankCode(bank.getEotCardBankCode());
		dto.setTerminalId(bank.getTerminalId());
		dto.setOutletNumber(bank.getOutletNumber());
		dto.setKycFlag(bank.getKycFlag());
		dto.setCollaborationLogo(bank.getCollaborationLogo());
		dto.setApplicationName(bank.getApplicationName());
		dto.setChannelCost(bank.getChannelCost().toString());

		if(bank.getBankGroup()!=null){
			dto.setBankGroupId(bank.getBankGroup().getBankGroupId());
		}		
		dto.setAgreements(bank.getBankAgreementModels());

		Map<String,Float> tmp = new HashMap<String, Float>();

		for(ServiceChargeSplit sc : bank.getServiceChargeSplits()){
			tmp.put(sc.getAmountType(), sc.getServiceChargePct()) ;
		}

		float tax = tmp.get(EOTConstants.AMT_TYPE_TAX_AMOUNT) != null ? tmp.get(EOTConstants.AMT_TYPE_TAX_AMOUNT).floatValue() : 0 ;
		float bankRev = tmp.get(EOTConstants.AMT_TYPE_BANK_REV) != null ? tmp.get(EOTConstants.AMT_TYPE_BANK_REV).floatValue() : 0 ;
		float gimShare = tmp.get(EOTConstants.AMT_TYPE_EOT_SHARE) != null ? tmp.get(EOTConstants.AMT_TYPE_EOT_SHARE).floatValue() : 0 ;
		float stampFee = tmp.get(EOTConstants.AMT_TYPE_STAMP_FEE) != null ? tmp.get(EOTConstants.AMT_TYPE_STAMP_FEE).floatValue() : 0 ;
		dto.setEotCharge(gimShare);
		dto.setGovtTax(tax);
		dto.setBankCharge(bankRev);
		dto.setStampFee(stampFee);
		dto.setChPool(bank.getClearingHousePoolMembers());


		/*Set<ClearingHousePoolMember> members = bank.getClearingHousePoolMembers();

		Integer[] clearingHouses = new Integer[members.size()];int i=0;
		Integer[] priorities=new Integer[members.size()];
		for(ClearingHousePoolMember member:members){
			clearingHouses[i] = member.getClearingPoolId();
			priorities[i]=member.getPoolPriority();
			i++;
		}

		dto.setClearingHouses(clearingHouses);
		dto.setClearingHousePriorities(priorities);
		 */
		return dto;
	}

	//@Override
	@SuppressWarnings("unchecked")
	public Map getMasterData(int pageNumber,String language)throws EOTException {

		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		WebUserDTO user=webUserService.getUser(userName);
		Map model = new HashMap();
		model.put("countryList",operatorDao.getCountries(language));
		model.put("currencyList",bankDao.getCurrencies());
		model.put("timeZoneList",locationDao.getTimeZones());
		model.put("clearingHouseList",bankDao.getActiveClearingHouses());
		model.put("agreementList", bankDao.viewBankAgreementDates());		

		WebUser webUser=webUserDao.getUser(userName);			
		if(webUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_EOT_ADMIN){  			

			model.put("bankGroupList",bankDao.getAllBankGroups());	
		}		
		else if(webUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BANK_GROUP_ADMIN){			

		}	
		return model;
	}

	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=Throwable.class )
	public void saveInterBankServiceCharges(InterBankSCDTO dto,String locale) {

		List<ServiceChargeSplit> splits = scManagementDao.getInterBankServiceCharges() ;

		if(splits.size()==0){

			List<TransactionType> txnTypes = transactionRulesDao.getTransactionTypesWithServiceCharge(locale.substring(0, 2));

			for(TransactionType txnType:txnTypes){
				ServiceChargeSplit gimShare = new ServiceChargeSplit();

				gimShare.setAmountType(EOTConstants.AMT_TYPE_EOT_SHARE);
				gimShare.setIsInter(EOTConstants.TXN_TYPE_INTER_BANK);
				gimShare.setServiceChargePct(dto.getGimShare().floatValue());
				gimShare.setTransactionType(txnType);

				transactionRulesDao.save(gimShare);

				ServiceChargeSplit interbankFee = new ServiceChargeSplit();

				interbankFee.setAmountType(EOTConstants.AMT_TYPE_INTER_BANK_FEE);
				interbankFee.setIsInter(EOTConstants.TXN_TYPE_INTER_BANK);
				interbankFee.setServiceChargePct(dto.getAquirerBank().floatValue());
				interbankFee.setTransactionType(txnType);

				transactionRulesDao.save(interbankFee);

				ServiceChargeSplit bankRev = new ServiceChargeSplit();

				bankRev.setAmountType(EOTConstants.AMT_TYPE_BANK_REV);
				bankRev.setIsInter(EOTConstants.TXN_TYPE_INTER_BANK);
				bankRev.setServiceChargePct(dto.getIssuerBank().floatValue());
				bankRev.setTransactionType(txnType);
				transactionRulesDao.save(bankRev);

			}

		}else{

			for(ServiceChargeSplit split : splits){

				if(EOTConstants.AMT_TYPE_BANK_REV.equals(split.getAmountType())){
					split.setServiceChargePct(dto.getIssuerBank().floatValue());
				}else if(EOTConstants.AMT_TYPE_EOT_SHARE.equals(split.getAmountType())){
					split.setServiceChargePct(dto.getGimShare().floatValue());
				}else if(EOTConstants.AMT_TYPE_INTER_BANK_FEE.equals(split.getAmountType())){
					split.setServiceChargePct(dto.getAquirerBank().floatValue());
				}

				bankDao.update(split);

			}

		}

	}

	@Override
	public InterBankSCDTO getInterBankServiceCharges() {

		List<ServiceChargeSplit> splits = scManagementDao.getInterBankServiceCharges() ;

		Map<String,Float> tmp = new HashMap<String, Float>();

		for(ServiceChargeSplit sc : splits){
			tmp.put(sc.getAmountType(), sc.getServiceChargePct()) ;
		}


		float bankRev = tmp.get(EOTConstants.AMT_TYPE_BANK_REV) != null ? tmp.get(EOTConstants.AMT_TYPE_BANK_REV) : 0 ;
		float gimShare = tmp.get(EOTConstants.AMT_TYPE_EOT_SHARE) != null ? tmp.get(EOTConstants.AMT_TYPE_EOT_SHARE) : 0 ;
		float interBankFee = tmp.get(EOTConstants.AMT_TYPE_INTER_BANK_FEE) != null ? tmp.get(EOTConstants.AMT_TYPE_INTER_BANK_FEE) : 0 ;

		InterBankSCDTO dto = new InterBankSCDTO();
		dto.setGimShare(gimShare);

		dto.setAquirerBank(interBankFee);
		dto.setIssuerBank(bankRev);

		return dto;

	}

	public CardDto getCardDetailsByBankId(Integer bankId){

		CustomerCard card=bankDao.getCardDetailsByBankId(bankId);

		CardDto cardDto=new CardDto();
		cardDto.setBankId(bankId);

		if( card != null){	
			cardDto.setCardId(card.getCardId());
			try {
				cardDto.setCardNo(new String( kmsHandle.externalDbDesOperation( HexString.hexToBuffer(card.getCardNumber()), false)));
				cardDto.setCvv(new String( kmsHandle.externalDbDesOperation( HexString.hexToBuffer(card.getCvv()), false)));
				cardDto.setCardExpiry(new String( kmsHandle.externalDbDesOperation( HexString.hexToBuffer(card.getCardExpiry()), false)));
			} catch (KMSSecurityException e) {
				e.printStackTrace();

			}
			cardDto.setStatus(card.getStatus());
		}

		return cardDto;
	}

	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=Throwable.class )
	public void saveOrUpdateCard(CardDto cardDto,Map<String,Object> model) throws EOTException{
		CustomerCard card=null;

		if( cardDto.getCardId()==null ){
			try {
				card=bankDao.getBankVertualCard(HexString.bufferToHex(kmsHandle.externalDbDesOperation( cardDto.getCardNo().getBytes(), true) ));
			} catch (KMSSecurityException e) {
				e.printStackTrace();
			}
			if(card!=null)
				throw new EOTException(ErrorConstants.CUSTOMER_CARD_EXIST);
		}else{
			card=bankDao.getCardDetailsByBankId(cardDto.getBankId(),cardDto.getCardNo());
			if(card!=null)
				throw new EOTException(ErrorConstants.CUSTOMER_CARD_EXIST);
		}

		card=new CustomerCard();

		Bank bank=new Bank();
		bank.setBankId(cardDto.getBankId());
		card.setBank(bank);

		card.setReferenceId(cardDto.getBankId()+"");
		try {
			card.setCardNumber(HexString.bufferToHex(kmsHandle.externalDbDesOperation( cardDto.getCardNo().getBytes(), true) ));
			card.setCvv(HexString.bufferToHex(kmsHandle.externalDbDesOperation( cardDto.getCvv().getBytes(), true) ));
			card.setCardExpiry(HexString.bufferToHex(kmsHandle.externalDbDesOperation( cardDto.getCardExpiry().getBytes(), true) ));
		} catch (KMSSecurityException e) {
			e.printStackTrace();
		}

		card.setStatus(cardDto.getStatus());
		card.setReferenceType(EOTConstants.REFERENCE_TYPE_BANK);
		card.setAlias(EOTConstants.CUSTOMER_VIRTUAL_CARD);

		if(cardDto.getCardId()==null){			
			bankDao.save(card);
			cardDto.setCardId(card.getCardId());
			model.put("message","ADD_CARD_SUCCESS");
		}else{
			card.setCardId(cardDto.getCardId());
			bankDao.update(card);
			model.put("message","UPDATE_CARD_SUCCESS");
		}
		model.put("cardDto",cardDto);

	}

	@Override
	public Page searchBank(BankDTO bankDTO, int pageNumber,String language) throws EOTException{
		Page page=null;
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		WebUserDTO user=webUserService.getUser(userName);


		WebUser webUser=webUserDao.getUser(userName);			
		if(webUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_EOT_ADMIN || webUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_EOT_BACKOFFICE_EXEC || webUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_EOT_BACKOFFICE_LEAD || webUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_EOT_SUPPORT_LEAD){  			
			page = bankDao.searchBank(null,bankDTO,pageNumber);

			/* for(int i =0; i<page.results.size();i++){

				 Bank bank = (Bank) page.results.get(i);

				 System.out.println("Bank Name : " + bank.getBankName());

			 }*/
			if(page.getResults().size() == 0 || page.getResults().isEmpty() || page.getResults().equals(""))
				throw new EOTException(ErrorConstants.BANK_NOT_EXIST);

		}		
		else if(webUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BANK_GROUP_ADMIN){			
			BankGroupAdmin bankGroupAdmin=bankDao.getBankGroupByUsername(webUser.getUserName());
			if(bankGroupAdmin==null){			
				throw new EOTException(ErrorConstants.INVALID_GROUPADMIN);
			}
			page = bankDao.searchBank(bankGroupAdmin.getBankGroup().getBankGroupId(),bankDTO,pageNumber);
			if(page.getResults().size() == 0 || page.getResults().isEmpty() || page.getResults().equals(""))
				throw new EOTException(ErrorConstants.BANK_NOT_EXIST);

		}	
		return page;

	}

	@Override
	public List<Bank> getBankList() {
		return bankDao.getBankList();
	}


}

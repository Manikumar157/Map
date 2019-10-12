package com.eot.banking.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eot.banking.common.EOTConstants;
import com.eot.banking.dao.BankDao;
import com.eot.banking.dao.BillerDao;
import com.eot.banking.dao.OperatorDao;
import com.eot.banking.dao.WebUserDao;
import com.eot.banking.dto.BillerDTO;
import com.eot.banking.dto.SenelecBillSearchDTO;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.utils.EOTUtil;
import com.eot.banking.utils.Page;
import com.eot.entity.Account;
import com.eot.entity.Bank;
import com.eot.entity.BankTellers;
import com.eot.entity.Biller;
import com.eot.entity.BillerTypes;
import com.eot.entity.Country;
import com.eot.entity.SenelecBills;
import com.eot.entity.SenelecControlFile;
import com.eot.entity.WebUser;

@Service("billerService")

public class BillerServiceImpl implements BillerService{

	@Autowired
	private OperatorDao operatorDao;
	@Autowired
	private BillerDao billerDao;
	@Autowired
	private BankDao bankDao;
	@Autowired
	private WebUserDao webUserDao;

	@Override
	public List<Bank> getBankList() {
		return bankDao.getActiveBanks();
	}

	@Override
	public List<Country> getCountryList(String language) {

		return operatorDao.getCountries(language);
	}

	@Override
	public List<BillerTypes> getBillerTypes() {

		return billerDao.getBillerTypes();
	}

	public Page getBillersList(BillerDTO billerDTO,Integer pageNumber) throws EOTException{
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		WebUser webUser=webUserDao.getUser(auth.getName());
		if(webUser==null){
			throw new EOTException(ErrorConstants.INVALID_USER);
		}

		BankTellers teller = bankDao.getTellerByUsername(webUser.getUserName());
		Page page = billerDao.getBillersList(billerDTO, pageNumber,teller.getBank().getBankId());
		
		if(page.results.size() == 0){
			throw new EOTException(ErrorConstants.BILLER_NOT_EXIST);
		}

		return page;
	}
	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=Throwable.class )
	public void addBillerDetails(BillerDTO billerDTO)throws EOTException {

		Biller biller=billerDao.getBillerNameByName(billerDTO.getBillerName(),billerDTO.getCountryId());
		if(biller!=null){
			throw new EOTException(ErrorConstants.BILLER_NAME_EXIST);
		}	
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		WebUser webUser=webUserDao.getUser(auth.getName());
		if(webUser==null){
			throw new EOTException(ErrorConstants.INVALID_USER);
		}

		BankTellers teller = bankDao.getTellerByUsername(webUser.getUserName());
		
		biller=new Biller();
		biller.setBillerName(billerDTO.getBillerName());
		biller.setPartialPayments(billerDTO.getPartialPayments());
	//	BillerTypes billerTypes=new BillerTypes();
	//	billerTypes.setBillerTypeId(billerDTO.getBillerTypeId());
		BillerTypes billerTypes=billerDao.loadBillerType(billerDTO.getBillerTypeId());		
		biller.setBillerType(billerTypes);
	//	Country country=new Country();
	//	country.setCountryId(billerDTO.getCountryId());
		Country country=billerDao.loadCountry(billerDTO.getCountryId());
		biller.setCountry(country);

		Account acc = new Account();

		acc.setAccountNumber(EOTUtil.generateAccountNumber(bankDao.getNextAccountNumberSequence()));
		acc.setAccountType(EOTConstants.ACCOUNT_TYPE_PERSONAL);
		acc.setActive(EOTConstants.ACCOUNT_STATUS_ACTIVE);
		acc.setAlias(EOTConstants.ACCOUNT_ALIAS_CUSTOMER+"-"+biller.getBillerName());
		acc.setCurrentBalance(0.0);
		acc.setCurrentBalanceType(EOTConstants.ACCOUNT_BALANCE_TYPE_CREDIT);
		acc.setReferenceId(" ");
		acc.setReferenceType(EOTConstants.REFERENCE_TYPE_MERCHANT);
		bankDao.save(acc);

		biller.setAccount(acc);

		/* Code updated by bidyut biller bank shoud be loaded by default login
		 * Bank bank = new Bank();
		bank.setBankId(billerDTO.getBankId());*/
		
		biller.setBank(teller.getBank());

		billerDao.save(biller);

		acc.setReferenceId(biller.getBillerId().toString());
		billerDao.update(acc);

	}
	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=Throwable.class )
	public void updateBillerDetails(BillerDTO billerDTO) throws EOTException{
		Biller biller=billerDao.getBillerNameByName(billerDTO.getBillerId(), billerDTO.getBillerName(), billerDTO.getCountryId());
		if(biller!=null){
			throw new EOTException(ErrorConstants.BILLER_NAME_EXIST);
		}
		biller = billerDao.getBillerDeatils(billerDTO.getBillerId());

		if(biller==null){
			throw new EOTException(ErrorConstants.INVALID_BILLER);
		}

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		WebUser webUser=webUserDao.getUser(auth.getName());
		if(webUser==null){
			throw new EOTException(ErrorConstants.INVALID_USER);
		}

		BankTellers teller = bankDao.getTellerByUsername(webUser.getUserName());		
		
		
		biller.setBillerId(billerDTO.getBillerId());
		biller.setBillerName(billerDTO.getBillerName());
		biller.setPartialPayments(billerDTO.getPartialPayments());
	//	BillerTypes billerTypes=new BillerTypes();
	//	billerTypes.setBillerTypeId(billerDTO.getBillerTypeId());
		BillerTypes billerTypes=billerDao.loadBillerType(billerDTO.getBillerTypeId());		
		biller.setBillerType(billerTypes);
	//	Country country=new Country();
	//	country.setCountryId(billerDTO.getCountryId());
		Country country=billerDao.loadCountry(billerDTO.getCountryId());
		biller.setCountry(country);
		if( biller.getBank()==null|| biller.getAccount()== null){
			Account acc = new Account();

			acc.setAccountNumber(EOTUtil.generateAccountNumber(bankDao.getNextAccountNumberSequence()));
			acc.setAccountType(EOTConstants.ACCOUNT_TYPE_PERSONAL);
			acc.setActive(EOTConstants.ACCOUNT_STATUS_ACTIVE);
			acc.setAlias(EOTConstants.ACCOUNT_ALIAS_CUSTOMER+"-"+biller.getBillerName());
			acc.setCurrentBalance(0.0);
			acc.setCurrentBalanceType(EOTConstants.ACCOUNT_BALANCE_TYPE_CREDIT);
			acc.setReferenceId(biller.getBillerId().toString());
			acc.setReferenceType(EOTConstants.REFERENCE_TYPE_MERCHANT);
			bankDao.save(acc);

			biller.setAccount(acc);
			/*Bank bank = new Bank();
			bank.setBankId(billerDTO.getBankId());*/
			biller.setBank(teller.getBank());

		}
		billerDao.update(biller);
}

@Override
public BillerDTO getBillerDetails(Integer billerId) throws EOTException{

	BillerDTO billerDTO=new BillerDTO();
	Biller biller = billerDao.getBillerDeatils(billerId);

	if(biller==null){
		throw new EOTException(ErrorConstants.INVALID_BILLER);
	}
	billerDTO.setBillerId(biller.getBillerId());
	billerDTO.setBillerName(biller.getBillerName());
	billerDTO.setBillerTypeId(biller.getBillerType().getBillerTypeId());
	billerDTO.setCountryId(biller.getCountry().getCountryId());
	billerDTO.setPartialPayments(biller.getPartialPayments());
	billerDTO.setBankId(biller.getBank().getBankId());

	return billerDTO;
}

public Page getSenelecBills(Integer pageNumber) throws EOTException {

	Page page=billerDao.getSenegalBills(pageNumber);
	if(page.getResults()==null)
		throw new EOTException(ErrorConstants.BILL_NOT_FOUND);

	return page;
}

public Page searchSenelecBill(SenelecBillSearchDTO senelecBillSearchDTO,Integer pageNumber) throws EOTException {

	Page page = billerDao.searchSenelecBill(senelecBillSearchDTO,pageNumber);
	if(page.getResults().size()==0)
		throw new EOTException(ErrorConstants.BILL_NOT_FOUND);

	return page;
}


//Author Name:Abu Kalam Azad Date:23/07/2018 purpouses:Bill Payment pdf data should be based on criteria..(5761)
//start...
@Override
public List<SenelecBillSearchDTO> searchSenelecBillForPdf(SenelecBillSearchDTO senelecBillSearchDTO)
		throws EOTException {
	List<SenelecBillSearchDTO> list=billerDao.searchSenelecBillForPdf(senelecBillSearchDTO);
	return list;
}
//...End

public void getPolicyDetailsByPolicyNo(Map<String,Object> model,Integer pageNumber,String policyNo,Integer recordNo) throws EOTException{

	Double creditAmount = 0D;
	Double debitAmount = 0D;
	String customer="";Page page=null;
	page = billerDao.getPolicyDetailsByPolicyNo(pageNumber,policyNo,recordNo);

	if(page.getResults().size()==0){

		page = billerDao.getSenelecBillByPolicyNo(pageNumber, policyNo, recordNo);
		List<SenelecBills> senelecBillFile = page.getResults();

		customer=senelecBillFile.get(0).getSenelecCustomers().getCustomerName();
		debitAmount=senelecBillFile.get(0).getRecordAmount();
		creditAmount=senelecBillFile.get(0).getAmountPaid();

		if(senelecBillFile.get(0).getStatus()==1){
			SenelecBills senelecBills=new SenelecBills();
			senelecBills.setRecordNumber(senelecBillFile.get(0).getRecordNumber());
			senelecBills.setOrderNumber(senelecBillFile.get(0).getOrderNumber());
			senelecBills.setRecordAmount(senelecBillFile.get(0).getAmountPaid());
			senelecBills.setOrderNumber(senelecBillFile.get(0).getOrderNumber());
			senelecBills.setTxnType("C");
			senelecBills.setRecordDate(senelecBillFile.get(0).getRecordDate());
			senelecBills.setPaymentDate(senelecBillFile.get(0).getPaymentDate());
			senelecBillFile.add(senelecBills);

		}

	}else{

		List<SenelecControlFile> senelecControlFiles = page.getResults();
		customer = senelecControlFiles.get(0).getSenelecCustomers().getCustomerName();

		for(int i=0;i<senelecControlFiles.size();i++){

			if(senelecControlFiles.get(i).getTxnType().equalsIgnoreCase("C")){
				creditAmount=creditAmount+senelecControlFiles.get(i).getRecordAmount();
			}else{
				debitAmount=debitAmount+senelecControlFiles.get(i).getRecordAmount();
			}

		}

		Page page1 = billerDao.getSenelecBillByPolicyNo(pageNumber, policyNo, recordNo);
		List<SenelecBills> senelecBillFile = page1.getResults();

		if(senelecBillFile.get(0).getStatus()==1){
			SenelecControlFile senelecControlFile=new SenelecControlFile();
			senelecControlFile.setRecordNumber(senelecBillFile.get(0).getRecordNumber());
			senelecControlFile.setOrderNumber(senelecBillFile.get(0).getOrderNumber());
			senelecControlFile.setRecordAmount(senelecBillFile.get(0).getAmountPaid());
			senelecControlFile.setOrderNumber(senelecBillFile.get(0).getOrderNumber());
			senelecControlFile.setTxnType("C");
			senelecControlFile.setRecordDate(senelecBillFile.get(0).getRecordDate());
			senelecControlFile.setPaymentDate(senelecBillFile.get(0).getPaymentDate());
			senelecControlFiles.add(senelecControlFile);
			creditAmount = creditAmount + senelecBillFile.get(0).getAmountPaid();
		}
	}
	page.requestPage = "viewDetailsByPolicyNo.htm";
	model.put("pno", policyNo);
	model.put("recordNo", recordNo);
	model.put("page",page);
	model.put("customer",customer);
	model.put("creditAmount", creditAmount.intValue());
	model.put("debitAmount", debitAmount.intValue());
	model.put("balanceAmount", (debitAmount.intValue()-creditAmount.intValue()));

}
public List<Object> getAllPaidBills() throws EOTException{
	return billerDao.getAllPaidBills();
}

public double getTotalTransactionAmount() throws EOTException{
	return billerDao.getTotalTransactionAmount();
}

@Override
public Page getBillersList(int pageNumber) throws EOTException{
	
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	WebUser webUser=webUserDao.getUser(auth.getName());
	if(webUser==null){
		throw new EOTException(ErrorConstants.INVALID_USER);
	}

	BankTellers teller = bankDao.getTellerByUsername(webUser.getUserName());	
//	Page page=billerDao.getBillersList(pageNumber);
	Page page=billerDao.getBillersList(pageNumber,teller.getBank().getBankId());
	return page;
}



}

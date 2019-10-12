package com.eot.banking.service;



import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.eot.banking.common.CoreUrls;
import com.eot.banking.common.EOTConstants;
import com.eot.banking.dao.AccountHeadMappingDao;
import com.eot.banking.dao.BankDao;
import com.eot.banking.dao.BusinessPartnerDao;
import com.eot.banking.dao.CustomerDao;
import com.eot.banking.dao.TransactionDao;
import com.eot.banking.dao.TransactionDescDao;
import com.eot.dtos.banking.CommissionDTO;
import com.eot.dtos.common.Fault;
import com.eot.entity.Account;
import com.eot.entity.Bank;
import com.eot.entity.BusinessPartner;
import com.eot.entity.CommisionSplits;
import com.eot.entity.CommissionReport;
import com.eot.entity.Customer;
import com.eot.entity.CustomerAccount;


@Service("schedulerTask")
public class CommissionDistributionServiceImpl implements CommissionDistributionService {

	@Autowired
	private BankDao bankDao;
	@Autowired
	private CustomerDao customerDao;
	@Autowired
	private BusinessPartnerDao businessPartnerDao;
	@Autowired
	private AccountHeadMappingDao accountHeadMappingDao ;
	@Autowired
	private TransactionDao transactionDao;
	@Autowired
	private TransactionDescDao transactionDescDao;
	@Autowired
	private TransactionService transactionService;

	public CommissionDTO postCommissionTransaction(String AccountNumber,Double amount,Integer bankId,Long branchID, Account bankAccount,String referenceId ) throws Exception
	{
		com.eot.dtos.common.Account accountDto = new com.eot.dtos.common.Account();
		accountDto.setAccountAlias("Commission Shared");//hardcoded for testing
		accountDto.setAccountNO(AccountNumber);
		accountDto.setAccountType(EOTConstants.ALIAS_TYPE_MOBILE_ACC+"");
		accountDto.setBankCode(bankId+"");
		accountDto.setBranchCode(branchID+"");

		//Bank Revinue Account Load
		com.eot.dtos.common.Account otherAccount = new  com.eot.dtos.common.Account();
		otherAccount.setAccountAlias(bankAccount.getAlias());
		otherAccount.setAccountNO(bankAccount.getAccountNumber());
		otherAccount.setAccountType(EOTConstants.ALIAS_TYPE_BANK_ACC+"");
		otherAccount.setBankCode(bankId+"");
		otherAccount.setBranchCode(branchID+"");

		CommissionDTO commissionDTO = new CommissionDTO();
		commissionDTO.setReferenceID(referenceId);
		commissionDTO.setAmount(amount);
		commissionDTO.setChannelType(EOTConstants.EOT_CHANNEL);
		commissionDTO.setCustomerAccount(accountDto);
		commissionDTO.setOtherAccount(otherAccount);
		commissionDTO.setTransactionType(EOTConstants.TXN_ID_COMMISSION_SHARE+"");

		commissionDTO=processRequest(CoreUrls.COMMISSION_URL, commissionDTO, com.eot.dtos.banking.CommissionDTO.class);
		return commissionDTO;
	}

	/*@author
	 * bidyut kalita 
	 * rest service call to core*/
	public <T extends com.eot.dtos.common.Header> T processRequest(String url, T obj,Class<T> type)  throws Fault
	{
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		RestTemplate restTemplate= new RestTemplate();
		restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
		obj.setRequestChannel("WEB");
		obj=restTemplate.postForObject(url, obj,  type);

		return obj;
	}

	@Override
	public void processCommission() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE,-1);
		Date date = cal.getTime();
		String strDate = format.format(date);
		List<Customer> list = customerDao.loadAgentProcessTxnByDate(strDate);
		if(CollectionUtils.isNotEmpty(list)) {
			for(Customer customer : list) {
				String commissionShareAccount=accountHeadMappingDao.getAccountByHeadId(EOTConstants.ACCOUNT_HEAD_ID_COMMISSION_SHARE);
				List<Object[]> commissionResults = customerDao.loadTotalCommissionByAgentId(customer.getCustomerId(), strDate, commissionShareAccount);
				processCommissionByTxnType(commissionResults,customer,commissionShareAccount);
			}
		}
	}

	private void processCommissionByTxnType(List<Object[]> commissionResults,Customer customer,String commissionAccount) {
		if(CollectionUtils.isNotEmpty(commissionResults)) {
			for (Object[] object : commissionResults){
				splitAndPostCommission(customerDao.getCommissionPercentage((Integer)object[0]),(Double)object[1],((Number)object[2]).longValue(),customer,commissionAccount);
			}
		}
	}

	private void splitAndPostCommission(List<CommisionSplits> commisionSplits,Double commissionAmount,Long refTxnId, Customer customer,String commissionAccount) {
		if(CollectionUtils.isNotEmpty(commisionSplits)) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE,-1);

			Date date = cal.getTime();
			String strDate = format.format(date);
			String accountNumber = null;
			String referenceId = null;
			CustomerAccount agentAccount = null;
			Account account =customerDao.getAccount(commissionAccount);
			BusinessPartner partner=businessPartnerDao.getBusinessPartnerbyId(customer.getBusinessPartner().getId()+"");
			if(customer.getType()==EOTConstants.REFERENCE_TYPE_AGENT)
				agentAccount=customerDao.findAgentCommissionAccount(customer.getCustomerId(), partner.getBank().getBankId());
			else
				agentAccount=customerDao.findCustomerAccount(customer.getCustomerId(), partner.getBank().getBankId());

			Integer bankId = agentAccount.getBank().getBankId();
			Long branchId  = agentAccount.getBranch().getBranchId();
			String partnerName = null;
			String partnerCode = null;
			for(CommisionSplits cp : commisionSplits) {
				String businessPartner = cp.getBusinessPartner();
				if(StringUtils.isNotEmpty(businessPartner) && cp.getCommisionPct() > 0D) {
					switch (businessPartner) {
					case "mGURUSH":
					{
						accountNumber = accountHeadMappingDao.getAccountByHeadId(55,1);
						Bank bank = bankDao.getBank(1);
						partnerName = bank.getBankName();
						partnerCode = bank.getBankCode();
						referenceId = bank.getBankCode();
						break;
					}
					case "PRINCIPAL_AGENT":
					{
						BusinessPartner bp=businessPartnerDao.getBusinessPartner(partner.getSeniorPartner().longValue());
						partnerName = bp.getName();
						partnerCode = bp.getCode();
						accountNumber = bp.getCommissionAccount();
						referenceId="BP_"+bp.getId()+"_"+customer.getCustomerId();
						break;
					}
					case "SUPER_AGENT":
					{
						accountNumber = partner.getCommissionAccount();
						partnerName = partner.getName();
						partnerCode = partner.getCode();
						referenceId="BP_"+customer.getBusinessPartner().getId()+"_"+customer.getCustomerId();
						break;
					}
					case "AGENT":
					{
						accountNumber = agentAccount.getAccountNumber();
						partnerName = customer.getFirstName();
						partnerCode = customer.getAgentCode();
						referenceId=customer.getCustomerId().toString();
						break;
					}
					}
					Double amount = cp.getCommisionPct()*commissionAmount/100;
					try {
						CommissionDTO commissionDTO = postCommissionTransaction(accountNumber, amount, bankId, branchId, account, referenceId);
						transactionDao.updateCommissionProcessedTxnByTxnId(customer.getCustomerId(), strDate,EOTConstants.STATUS_COMMISSION_SUCCESS,refTxnId);
						CommissionReport commissionReport = new CommissionReport();
						commissionReport.setTransactionId(Long.parseLong(commissionDTO.getTransactionNO()));
						commissionReport.setTransactionType(transactionDescDao.getTransactionDescForType(cp.getTransactionType().getTransactionType().toString(),"en"));
						commissionReport.setTransactionDate(new Date());
						commissionReport.setPartnerName(partnerName);
						commissionReport.setPartnerCode(partnerCode);
						commissionReport.setRefTransactionId(refTxnId);
						commissionReport.setCommissionAmount(amount);
						commissionReport.setServiceCharge(commissionAmount);
						commissionReport.setStatus(EOTConstants.STATUS_COMMISSION_SUCCESS);
						transactionService.saveCommissionData(commissionReport);
					} catch (Exception e) {
						e.printStackTrace();
						transactionDao.updateCommissionProcessedTxnByAgnetId(customer.getCustomerId(), strDate,EOTConstants.STATUS_COMMISSION_FAIL,refTxnId);
					}

				}
			}
		}
	}

}

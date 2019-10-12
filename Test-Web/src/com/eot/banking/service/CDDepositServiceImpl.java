package com.eot.banking.service;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.eot.banking.common.CoreUrls;
import com.eot.banking.common.EOTConstants;
import com.eot.banking.dao.AccountHeadMappingDao;
import com.eot.banking.dao.BusinessPartnerDao;
import com.eot.banking.dao.CustomerDao;
import com.eot.banking.dao.TransactionDao;
import com.eot.dtos.banking.CommissionDTO;
import com.eot.dtos.common.Fault;
import com.eot.entity.Account;
import com.eot.entity.BusinessPartner;
import com.eot.entity.Customer;
import com.eot.entity.CustomerAccount;


@Service("schedulerTaskDeposit")
public class CDDepositServiceImpl implements CommissionDistributionService {
	
	
	@Autowired
	private CustomerDao customerDao;
	@Autowired
	private BusinessPartnerDao businessPartnerDao;
	
	@Autowired
	private AccountHeadMappingDao accountHeadMappingDao ;
	@Autowired
	private TransactionDao transactionDao;
	
	
	
	@Override
	public void processCommission() {
		//System.out.println("commission processing..");
		List<Customer> list= null;
		double agentComissinPercent=0d;
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE,-1);
		Date date = cal.getTime();
		String strDate = format.format(date);
		System.out.println("deposit Formated date:  "+strDate);
		
		try
		{
			BusinessPartner partner=null;
//			list=customerDao.loadAgentProcessDepoTxnByDate(strDate);
			while(list.size()!=0)
			{
				Customer agent= list.remove(0);
				double share_Agent=0;
				double share_businessPartnerL1=0;
				double share_businessPartnerL2=0;
				System.out.println("First Name: "+agent.getFirstName());
				agentComissinPercent=agent.getCommission();
				if(agent.getBusinessPartner().getId()==null)
				{
					/*
					 * CustomerAccount agentAccount=null;
					 * if(agent.getType()==EOTConstants.REFERENCE_TYPE_AGENT)
					 * agentAccount=customerDao.findAgentCommissionAccountBy(agent.getCustomerId());
					 * else agentAccount=customerDao.findCustomerAccountBy(agent.getCustomerId());
					 * 
					 * String
					 * commissionShareAccount=accountHeadMappingDao.getAccountByHeadId(EOTConstants.
					 * ACCOUNT_HEAD_ID_COMMISSION_SHARE); double
					 * totalSharableAmount=customerDao.loadTotalSharableAmountByAgent(agent.
					 * getCustomerId(),strDate,commissionShareAccount);
					 * share_Agent=(totalSharableAmount*agentComissinPercent)/100;
					 * System.out.println("Agent share:  "+share_Agent); //Agent Account Loaded
					 * //Account bankAccount=agentAccount.getBank().getAccount(); Account
					 * CommissionAccount=customerDao.getAccount(commissionShareAccount); try {
					 * //Posting agent Commission
					 * postCommissionTransaction(agentAccount.getAccountNumber()+"",
					 * share_Agent,agentAccount.getBank().getBankId(),
					 * agentAccount.getBranch().getBranchId(), CommissionAccount,
					 * agent.getCustomerId()+"");
					 * transactionDao.updateCommissionProcessedTxnByAgnetId(agent.getCustomerId(),
					 * strDate,EOTConstants.STATUS_COMMISSION_SUCCESS);
					 * 
					 * }catch (Exception e) { e.printStackTrace();
					 * transactionDao.updateCommissionProcessedTxnByAgnetId(agent.getCustomerId(),
					 * strDate,EOTConstants.STATUS_COMMISSION_FAIL); }
					 */}
				else
				{
					partner=businessPartnerDao.getBusinessPartnerbyId(agent.getBusinessPartner().getId()+"");
					if(partner.getPartnerType().intValue()==EOTConstants.BUSINESS_PARTNER_TYPE_L1.intValue())
					{
						double commission__L1=partner.getCommissionPercent();
						String commissionShareAccount=accountHeadMappingDao.getAccountByHeadId(EOTConstants.ACCOUNT_HEAD_ID_COMMISSION_SHARE);
						double totalSharableAmount=customerDao.loadTotalSharableAmountDeposite(agent.getCustomerId(),strDate,commissionShareAccount);
					
						share_businessPartnerL1=(20*totalSharableAmount)/100;
						System.out.println("L1 partner share: "+share_businessPartnerL1);
						
						share_Agent=totalSharableAmount-share_businessPartnerL1;
						System.out.println("Agent share:  "+share_Agent);

						//Agent Account Loaded
						CustomerAccount agentAccount=null;
						if(agent.getType()==EOTConstants.REFERENCE_TYPE_AGENT)
							agentAccount=customerDao.findAgentCommissionAccount(agent.getCustomerId(), partner.getBank().getBankId());
						else
							agentAccount=customerDao.findCustomerAccount(agent.getCustomerId(), partner.getBank().getBankId());
						
						//Account bankAccount=agentAccount.getBank().getAccount();
						Account CommissionAccount=customerDao.getAccount(commissionShareAccount);
						Account L1_Account=customerDao.getAccount(partner.getCommissionAccount());
						try
						{
							
							//Posting L1 Commission
							postCommissionTransaction(partner.getCommissionAccount()+"", share_businessPartnerL1,agentAccount.getBank().getBankId(), agentAccount.getBranch().getBranchId(), CommissionAccount, "BP_"+partner.getId()+"_"+agent.getCustomerId());
							//Posting agent Commission
							postCommissionTransaction(agentAccount.getAccountNumber()+"", share_Agent,agentAccount.getBank().getBankId(), agentAccount.getBranch().getBranchId(), CommissionAccount, agent.getCustomerId()+"");
							
							transactionDao.updateCommissionProcessedTxnByAgnetId(agent.getCustomerId(), strDate,EOTConstants.STATUS_COMMISSION_SUCCESS);
							
						}catch (SQLException e) {
							e.printStackTrace();
							transactionDao.updateCommissionProcessedTxnByAgnetId(agent.getCustomerId(), strDate,EOTConstants.STATUS_COMMISSION_UNKNOWN);
						}catch (Exception e) {
							e.printStackTrace();
							transactionDao.updateCommissionProcessedTxnByAgnetId(agent.getCustomerId(), strDate,EOTConstants.STATUS_COMMISSION_FAIL);
						}
						
						
					}else if(partner.getPartnerType().intValue()==EOTConstants.BUSINESS_PARTNER_TYPE_L2.intValue())
					{
						
						BusinessPartner partnerL1=businessPartnerDao.getBusinessPartnerbyId(partner.getSeniorPartner()+"");
						double commission__L2=partner.getCommissionPercent();
						double commission__L1=partnerL1.getCommissionPercent();
						
						String commissionShareAccount=accountHeadMappingDao.getAccountByHeadId(EOTConstants.ACCOUNT_HEAD_ID_COMMISSION_SHARE);
						double totalSharableAmount=customerDao.loadTotalSharableAmountDeposite(agent.getCustomerId(),strDate,commissionShareAccount);
						
						/*
						 * share_businessPartnerL1=(commission__L1*totalSharableAmount)/100;
						 * System.out.println("L1 partner share: "+share_businessPartnerL1);
						 */
						
						share_businessPartnerL2=(20*totalSharableAmount)/100;
						System.out.println("L2 partner share: "+share_businessPartnerL2);
						
						share_Agent=totalSharableAmount-share_businessPartnerL2;
						System.out.println("Agent share:  "+share_Agent);
						
						
						//Agent Account Loaded
						CustomerAccount agentAccount=null;
						if(agent.getType()==EOTConstants.REFERENCE_TYPE_AGENT)
							agentAccount=customerDao.findAgentCommissionAccount(agent.getCustomerId(), partner.getBank().getBankId());
						else
							agentAccount=customerDao.findCustomerAccount(agent.getCustomerId(), partner.getBank().getBankId());
						
						//Account bankAccount=agentAccount.getBank().getAccount();
						Account CommissionAccount=customerDao.getAccount(commissionShareAccount);
						Account L2_Account=customerDao.getAccount(partner.getCommissionAccount());
						Account L1_Account=customerDao.getAccount(partnerL1.getCommissionAccount());
						try
						{
							//Posting L1 Commission
//							postCommissionTransaction(partnerL1.getAccountNumber()+"", share_businessPartnerL1,agentAccount.getBank().getBankId(), agentAccount.getBranch().getBranchId(), CommissionAccount, "BP_"+partnerL1.getId()+"_"+agent.getCustomerId());
							
							//Posting L2 Commission
							postCommissionTransaction(partner.getCommissionAccount()+"", share_businessPartnerL2,agentAccount.getBank().getBankId(), agentAccount.getBranch().getBranchId(), CommissionAccount, "BP_"+partner.getId()+"_"+agent.getCustomerId());
							
							//Posting agent Commission
							postCommissionTransaction(agentAccount.getAccountNumber()+"", share_Agent,agentAccount.getBank().getBankId(), agentAccount.getBranch().getBranchId(), CommissionAccount, agent.getCustomerId()+"");
							
							
							transactionDao.updateCommissionProcessedTxnByAgnetId(agent.getCustomerId(), strDate,EOTConstants.STATUS_COMMISSION_SUCCESS);
						}catch (SQLException e) {
							e.printStackTrace();
							transactionDao.updateCommissionProcessedTxnByAgnetId(agent.getCustomerId(), strDate,EOTConstants.STATUS_COMMISSION_UNKNOWN);
						}
						catch (Exception e) {
							e.printStackTrace();
							transactionDao.updateCommissionProcessedTxnByAgnetId(agent.getCustomerId(), strDate,EOTConstants.STATUS_COMMISSION_FAIL);
						}
						
						
					} /*
						 * else
						 * if(partner.getPartnerType().intValue()==EOTConstants.BUSINESS_PARTNER_TYPE_L3
						 * .intValue()) {
						 * 
						 * BusinessPartner
						 * partnerL2=businessPartnerDao.getBusinessPartnerbyId(partner.getSeniorPartner(
						 * )+""); double commission__L2=partnerL2.getCommissionPercent();
						 * BusinessPartner
						 * partnerL1=businessPartnerDao.getBusinessPartnerbyId(partnerL2.
						 * getSeniorPartner()+""); double
						 * commission__L1=partnerL1.getCommissionPercent();
						 * 
						 * 
						 * String
						 * commissionShareAccount=accountHeadMappingDao.getAccountByHeadId(EOTConstants.
						 * ACCOUNT_HEAD_ID_COMMISSION_SHARE); double
						 * totalSharableAmount=customerDao.loadTotalSharableAmountByAgent(agent.
						 * getCustomerId(),strDate,commissionShareAccount);
						 * 
						 * share_businessPartnerL1=(commission__L1*totalSharableAmount)/100;
						 * System.out.println("L1 partner share: "+share_businessPartnerL1);
						 * 
						 * share_businessPartnerL2=(commission__L2*share_businessPartnerL1)/100;
						 * System.out.println("L2 partner share: "+share_businessPartnerL2);
						 * 
						 * share_businessPartnerL3=(commission__L2*share_businessPartnerL2)/100;
						 * System.out.println("L3 partner share: "+share_businessPartnerL3);
						 * 
						 * share_Agent=(share_businessPartnerL3*agentComissinPercent)/100;
						 * System.out.println("Agent share:  "+share_Agent);
						 * 
						 * 
						 * //Agent Account Loaded CustomerAccount agentAccount=null;
						 * if(agent.getType()==EOTConstants.REFERENCE_TYPE_AGENT)
						 * agentAccount=customerDao.findAgentCommissionAccount(agent.getCustomerId(),
						 * partner.getBank().getBankId()); else
						 * agentAccount=customerDao.findCustomerAccount(agent.getCustomerId(),
						 * partner.getBank().getBankId());
						 * 
						 * //Account bankAccount=agentAccount.getBank().getAccount(); Account
						 * CommissionAccount=customerDao.getAccount(commissionShareAccount); Account
						 * L3_Account=customerDao.getAccount(partner.getCommissionAccount()); Account
						 * L2_Account=customerDao.getAccount(partnerL2.getCommissionAccount()); Account
						 * L1_Account=customerDao.getAccount(partnerL1.getCommissionAccount());
						 * 
						 * try { //Posting L1 Commission
						 * postCommissionTransaction(partnerL1.getCommissionAccount()+"",
						 * share_businessPartnerL1,agentAccount.getBank().getBankId(),
						 * agentAccount.getBranch().getBranchId(),
						 * CommissionAccount,"BP_"+partnerL1.getId()+"_"+agent.getCustomerId());
						 * 
						 * //Posting L2 Commission
						 * postCommissionTransaction(partnerL2.getCommissionAccount()+"",
						 * share_businessPartnerL2,agentAccount.getBank().getBankId(),
						 * agentAccount.getBranch().getBranchId(),
						 * L1_Account,"BP_"+partnerL2.getId()+"_"+agent.getCustomerId());
						 * 
						 * //Posting L3 Commission
						 * postCommissionTransaction(partner.getCommissionAccount()+"",
						 * share_businessPartnerL3,agentAccount.getBank().getBankId(),
						 * agentAccount.getBranch().getBranchId(),
						 * L2_Account,"BP_"+partner.getId()+"_"+agent.getCustomerId());
						 * 
						 * 
						 * //Posting agent Commission
						 * postCommissionTransaction(agentAccount.getAccountNumber()+"",
						 * share_Agent,agentAccount.getBank().getBankId(),
						 * agentAccount.getBranch().getBranchId(), L3_Account,
						 * agent.getCustomerId()+"");
						 * 
						 * 
						 * transactionDao.updateCommissionProcessedTxnByAgnetId(agent.getCustomerId(),
						 * strDate,EOTConstants.STATUS_COMMISSION_SUCCESS);
						 * 
						 * }catch (SQLException e) { e.printStackTrace();
						 * transactionDao.updateCommissionProcessedTxnByAgnetId(agent.getCustomerId(),
						 * strDate,EOTConstants.STATUS_COMMISSION_UNKNOWN); }catch (Exception e) {
						 * e.printStackTrace();
						 * transactionDao.updateCommissionProcessedTxnByAgnetId(agent.getCustomerId(),
						 * strDate,EOTConstants.STATUS_COMMISSION_FAIL); } }
						 */
				}
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}

	}
	
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
		//otherAccount.setBankCode(agentAccount.getBank().getBankId().toString());
		//otherAccount.setBranchCode(agentAccount.getBranch().getBranchId().toString());
		//otherAccount.setBankCode(agentAccount.getBank().getBankId().toString());
		//otherAccount.setBranchCode(agentAccount.getBranch().getBranchId().toString());
		otherAccount.setBankCode(bankId+"");
		otherAccount.setBranchCode(branchID+"");

		CommissionDTO commissionDTO = new CommissionDTO();
		commissionDTO.setReferenceID(referenceId);
		commissionDTO.setAmount(amount);
		commissionDTO.setChannelType(EOTConstants.EOT_CHANNEL);
		commissionDTO.setCustomerAccount(accountDto);
		commissionDTO.setOtherAccount(otherAccount);
		commissionDTO.setTransactionType(EOTConstants.TXN_ID_COMMISSION_SHARE+"");
		
		//commissionDTO=bankingServiceClientStub.commissionShare(commissionDTO);
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
}

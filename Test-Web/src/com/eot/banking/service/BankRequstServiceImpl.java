package com.eot.banking.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.eot.banking.common.CoreUrls;
import com.eot.banking.common.EOTConstants;
import com.eot.banking.dao.BusinessPartnerDao;
import com.eot.banking.dao.CustomerDao;
import com.eot.banking.dto.LimitUpdateReqDTO;
import com.eot.banking.dto.LimitUpdateRespDTO;
import com.eot.dtos.banking.LimitDTO;
import com.eot.dtos.common.Fault;
import com.eot.entity.Account;
import com.eot.entity.ApiCredientials;
import com.eot.entity.ApiLogs;
import com.eot.entity.BusinessPartner;
import com.eot.entity.Customer;
import com.eot.entity.CustomerAccount;

@Service("bankRequestService")
@Transactional(readOnly = false)
public class BankRequstServiceImpl implements BankRequstService {

	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private BusinessPartnerDao businessPartnerDao;

	@Override
	
	public LimitUpdateRespDTO processLimitDepostiTransaction(LimitUpdateReqDTO limitUpdateReqDTO) {

		LimitUpdateRespDTO limitUpdateRespDTO = new LimitUpdateRespDTO();
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE,0);
		Date date = cal.getTime();
		String strDate = format.format(date);
		//System.out.println("Formated date:  "+strDate);

		ApiLogs apiLogs= new ApiLogs();
		apiLogs.setBankReference(limitUpdateReqDTO.getBankreference());
		apiLogs.setBillAmount(limitUpdateReqDTO.getBillAmount());
		apiLogs.setBillNumber(limitUpdateReqDTO.getBillNumber());
		apiLogs.setPhoneNumber(limitUpdateReqDTO.getPhonenumber());
		apiLogs.setTransactionDate(strDate);
		
		//varifying duplicate
		ApiLogs logs=businessPartnerDao.fetchApiLogs(limitUpdateReqDTO.getBankreference());
		if(logs!=null)
		{
			limitUpdateRespDTO.setMessage("Fail");
			limitUpdateRespDTO.setResponse(2);
			apiLogs.setStatus(EOTConstants.API_STATUS_FAIL);
			customerDao.save(apiLogs);
			System.out.println("Duplidate refrence Id: "+limitUpdateReqDTO.getBankreference());
			return limitUpdateRespDTO;
		}
		
		apiLogs.setStatus(EOTConstants.API_STATUS_NEW);
		customerDao.save(apiLogs);
		
		ApiCredientials cred=businessPartnerDao.varifyApiRequest(limitUpdateReqDTO.getUsername(),limitUpdateReqDTO.getPassword());
		if(cred==null)
		{
			limitUpdateRespDTO.setMessage("invalid credientials");
			System.out.println("invalid credientials");
			limitUpdateRespDTO.setResponse(1);
			return limitUpdateRespDTO;
		}

		String billCode = limitUpdateReqDTO.getBillNumber();

		if (billCode.isEmpty()) {
			limitUpdateRespDTO.setMessage("Empty Bill Number");
			System.out.println("Empty Bill Number");
			limitUpdateRespDTO.setResponse(1);
			return limitUpdateRespDTO;
		} else {

			List<LimitDTO> limitDtoList = new ArrayList<LimitDTO>();
			/**
			 * Seraching Registered mobile number in customer table
			 */
			String referenceId = "";

			BusinessPartner businessPartner = null;
			// Customer agent=customerDao.getCustomerByMobileNumber(registeredMobileNumber);
			Customer agent = customerDao.getAgentByAgentCode(billCode);
			if (agent == null) {
				businessPartner = businessPartnerDao.getBusinessPartnerByCode(billCode);
				if (businessPartner == null) {
					limitUpdateRespDTO.setMessage("Bill Number is not Registered");
					System.out.println("Bill Number is not Registered");
					apiLogs.setStatus(EOTConstants.API_STATUS_FAIL);
					customerDao.update(apiLogs);
					limitUpdateRespDTO.setResponse(1);
					return limitUpdateRespDTO;
				} else if (businessPartner != null) {
					referenceId = "BP_" + businessPartner.getId();
					// businessPartner=agent.getBusinessPartner();
					String PoolAccountNumber = businessPartner.getBank().getAccount().getAccountNumber();
					Integer poolAccountType = businessPartner.getBank().getAccount().getAccountType();
					String poolAccountAlias = businessPartner.getBank().getAccount().getAlias();
					if (businessPartner.getPartnerType().intValue() == EOTConstants.BUSINESS_PARTNER_TYPE_L1.intValue()) {

						Account account = null;// need to load account form account table
						String businessPartnerL1Acc = businessPartner.getAccountNumber();
						// ---------------------------------
						com.eot.dtos.common.Account thirdPartyMirroorAc = getAccount("1000000000109", EOTConstants.ACCOUNT_TYPE_WALLET + "", "BANK_TP_POOL_ACC", businessPartner.getBank().getBankId() + "");
						com.eot.dtos.common.Account mgurushPoolAc = getAccount(PoolAccountNumber, poolAccountType.toString(), poolAccountAlias, businessPartner.getBank().getBankId() + "");
						com.eot.dtos.common.Account businesssPartnerL1_poolAcc = getAccount(businessPartnerL1Acc, EOTConstants.ACCOUNT_TYPE_WALLET + "", "EOT Mobile - mGurush", businessPartner.getBank().getBankId() + "");

						LimitDTO limit_mGurush_pool = new LimitDTO();
						limit_mGurush_pool.setCustomerAccount(thirdPartyMirroorAc);
						limit_mGurush_pool.setAmount(limitUpdateReqDTO.getBillAmount());
						limit_mGurush_pool.setOtherAccount(mgurushPoolAc);
						limit_mGurush_pool.setLimit_cr_desc("Limit credited for acc" + businessPartnerL1Acc);
						limit_mGurush_pool.setLimit_dr_desc("Limit debited for acc" + businessPartnerL1Acc);
						limit_mGurush_pool.setChannelType(EOTConstants.EOT_CHANNEL);
						limit_mGurush_pool.setReferenceID(referenceId);
						limit_mGurush_pool.setAmount(limitUpdateReqDTO.getBillAmount());
						limit_mGurush_pool.setTransactionType(EOTConstants.TXN_TYPE_LIMIT_UPDATE + "");
						limit_mGurush_pool.setReferenceID(businessPartner.getCode());
						limit_mGurush_pool.setChannelType(EOTConstants.EOT_CHANNEL);
						limit_mGurush_pool.setBusinessPartnerCode(businessPartner.getCode());

						LimitDTO limit_businessPartnerL1_acc = new LimitDTO();
						limit_businessPartnerL1_acc.setCustomerAccount(mgurushPoolAc);
						limit_businessPartnerL1_acc.setAmount(limitUpdateReqDTO.getBillAmount());
						limit_businessPartnerL1_acc.setOtherAccount(businesssPartnerL1_poolAcc);
						limit_businessPartnerL1_acc.setLimit_cr_desc("Limit credited " + businessPartnerL1Acc);
						limit_businessPartnerL1_acc.setLimit_dr_desc("Limit debited for acc" + businessPartnerL1Acc);
						limit_businessPartnerL1_acc.setTransactionType(EOTConstants.TXN_TYPE_LIMIT_UPDATE + "");
						limit_businessPartnerL1_acc.setChannelType(EOTConstants.EOT_CHANNEL);
						limit_businessPartnerL1_acc.setReferenceID(referenceId);
						limit_businessPartnerL1_acc.setAmount(limitUpdateReqDTO.getBillAmount());
						limit_businessPartnerL1_acc.setReferenceID(businessPartner.getCode());
						limit_businessPartnerL1_acc.setChannelType(EOTConstants.EOT_CHANNEL);
						limit_businessPartnerL1_acc.setBusinessPartnerCode(businessPartner.getCode());

						/*
						 * limitDtoList.set(0, limit_mGurush_pool); limitDtoList.set(1,
						 * limit_businessPartnerL1_acc);
						 */

						limitDtoList.add(0, limit_mGurush_pool);
						limitDtoList.add(1, limit_businessPartnerL1_acc);

						LimitDTO transactionLimitDTO = new LimitDTO();
						transactionLimitDTO.setHeaderList(limitDtoList);
						try {
							transactionLimitDTO = processRequest(CoreUrls.LIMIT_POSTING_URL, transactionLimitDTO, com.eot.dtos.banking.LimitDTO.class);
							if (transactionLimitDTO.getErrorCode() != 0) {
								// throw new EOTException("ERROR_"+transactionLimitDTO.getErrorCode());
								limitUpdateRespDTO.setMessage("FAILED");
								limitUpdateRespDTO.setResponse(1);
								limitUpdateRespDTO.setMessage("Bill Number is not Registered");
								System.out.println("Bill Number is not Registered");
								apiLogs.setStatus(EOTConstants.API_STATUS_FAIL);
								customerDao.update(apiLogs);
								return limitUpdateRespDTO;

							}
							limitUpdateRespDTO.setMessage("SUCCESSFUL");
							limitUpdateRespDTO.setResponse(0);
							apiLogs.setStatus(EOTConstants.API_STATUS_SUCCESS);
							customerDao.saveOrUpdate(apiLogs);

						} catch (Exception e) {
							
							System.out.println("****************************EXCEPTION****************************");
							e.printStackTrace();
							limitUpdateRespDTO.setMessage("Fail");
							limitUpdateRespDTO.setResponse(1);
							// TODO: handle exception
							// throw new EOTException(e.getErrorCode());
						}

					}
					if (businessPartner.getPartnerType().intValue() == EOTConstants.BUSINESS_PARTNER_TYPE_L2.intValue()) {
						BusinessPartner businessPartner1 = businessPartnerDao.getBusinessPartner(businessPartner.getSeniorPartner().longValue());
						String businessPartnerL2Acc = businessPartner.getAccountNumber();
						String businessPartnerL1Acc = businessPartner1.getAccountNumber();

						com.eot.dtos.common.Account thirdPartyMirrorAc = getAccount("1000000000109", EOTConstants.ACCOUNT_TYPE_WALLET + "", "BANK_TP_POOL_ACC", businessPartner.getBank().getBankId() + "");
						com.eot.dtos.common.Account mgurushPoolAc = getAccount(PoolAccountNumber, poolAccountType.toString(), poolAccountAlias, businessPartner.getBank().getBankId() + "");
						com.eot.dtos.common.Account businesssPartnerL1_poolAcc = getAccount(businessPartnerL1Acc, EOTConstants.ACCOUNT_TYPE_WALLET + "", "EOT Mobile - mGurush", businessPartner.getBank().getBankId() + "");
						com.eot.dtos.common.Account businesssPartnerL2_poolAcc = getAccount(businessPartnerL2Acc, EOTConstants.ACCOUNT_TYPE_WALLET + "", "EOT Mobile - mGurush", businessPartner.getBank().getBankId() + "");

						LimitDTO limit_mGurush_pool = new LimitDTO();
						limit_mGurush_pool.setCustomerAccount(thirdPartyMirrorAc);
						limit_mGurush_pool.setAmount(limitUpdateReqDTO.getBillAmount());
						limit_mGurush_pool.setOtherAccount(mgurushPoolAc);
						limit_mGurush_pool.setLimit_cr_desc("Limit credited for acc" + businessPartnerL2Acc);
						limit_mGurush_pool.setLimit_dr_desc("Limit debited for acc" + businessPartnerL2Acc);
						limit_mGurush_pool.setTransactionType(EOTConstants.TXN_TYPE_LIMIT_UPDATE + "");
						limit_mGurush_pool.setAmount(limitUpdateReqDTO.getBillAmount());
						limit_mGurush_pool.setReferenceID(businessPartner.getCode());
						limit_mGurush_pool.setChannelType(EOTConstants.EOT_CHANNEL);
						limit_mGurush_pool.setBusinessPartnerCode(businessPartner.getCode());

						LimitDTO limit_businessPartnerL1_acc = new LimitDTO();
						limit_businessPartnerL1_acc.setCustomerAccount(mgurushPoolAc);
						limit_businessPartnerL1_acc.setAmount(limitUpdateReqDTO.getBillAmount());
						limit_businessPartnerL1_acc.setOtherAccount(businesssPartnerL1_poolAcc);
						limit_businessPartnerL1_acc.setLimit_cr_desc("Limit credited " + businessPartnerL2Acc);
						limit_businessPartnerL1_acc.setLimit_dr_desc("Limit debited for acc" + businessPartnerL2Acc);
						limit_businessPartnerL1_acc.setTransactionType(EOTConstants.TXN_TYPE_LIMIT_UPDATE + "");
						limit_businessPartnerL1_acc.setAmount(limitUpdateReqDTO.getBillAmount());
						limit_businessPartnerL1_acc.setReferenceID(businessPartner.getCode());
						limit_businessPartnerL1_acc.setChannelType(EOTConstants.EOT_CHANNEL);
						limit_businessPartnerL1_acc.setBusinessPartnerCode(businessPartner.getCode());
						
						LimitDTO limit_businessPartnerL2_acc = new LimitDTO();
						limit_businessPartnerL2_acc.setCustomerAccount(businesssPartnerL1_poolAcc);
						limit_businessPartnerL2_acc.setAmount(limitUpdateReqDTO.getBillAmount());
						limit_businessPartnerL2_acc.setOtherAccount(businesssPartnerL2_poolAcc);
						limit_businessPartnerL2_acc.setLimit_cr_desc("Limit credited " + businessPartnerL2Acc);
						limit_businessPartnerL2_acc.setLimit_dr_desc("Limit debited for acc" + businessPartnerL2Acc);
						limit_businessPartnerL2_acc.setTransactionType(EOTConstants.TXN_TYPE_LIMIT_UPDATE + "");
						limit_businessPartnerL2_acc.setAmount(limitUpdateReqDTO.getBillAmount());
						limit_businessPartnerL2_acc.setReferenceID(businessPartner.getCode());
						limit_businessPartnerL2_acc.setChannelType(EOTConstants.EOT_CHANNEL);
						limit_businessPartnerL2_acc.setBusinessPartnerCode(businessPartner.getCode());

						limitDtoList.add(0, limit_mGurush_pool);
						limitDtoList.add(1, limit_businessPartnerL1_acc);
						limitDtoList.add(2, limit_businessPartnerL2_acc);

						// core account processing
						LimitDTO transactionLimitDTO = new LimitDTO();
						transactionLimitDTO.setHeaderList(limitDtoList);
						try {
							transactionLimitDTO = processRequest(CoreUrls.LIMIT_POSTING_URL, transactionLimitDTO, com.eot.dtos.banking.LimitDTO.class);
							if (transactionLimitDTO.getErrorCode() != 0) {
								// throw new EOTException("ERROR_"+transactionLimitDTO.getErrorCode());
								limitUpdateRespDTO.setMessage("FAILED");
								limitUpdateRespDTO.setResponse(1);
								apiLogs.setStatus(EOTConstants.API_STATUS_FAIL);
								customerDao.update(apiLogs);
								System.out.println("*******************FAILED FROM CORE************************");
								return limitUpdateRespDTO;

							}
							limitUpdateRespDTO.setMessage("SUCCESSFUL");
							limitUpdateRespDTO.setResponse(0);
							apiLogs.setStatus(EOTConstants.API_STATUS_SUCCESS);
							customerDao.update(apiLogs);

						} catch (Exception e) {
							System.out.println("*******************EXCEPTION************************");
							e.printStackTrace();
							limitUpdateRespDTO.setMessage("Fail");
							limitUpdateRespDTO.setResponse(1);
							// TODO: handle exception
							// throw new EOTException(e.getErrorCode());
						}

					}
					if (businessPartner.getPartnerType().intValue() == EOTConstants.BUSINESS_PARTNER_TYPE_L3.intValue()) {
						BusinessPartner businessPartner2 = businessPartnerDao.getBusinessPartner(businessPartner.getSeniorPartner().longValue());
						BusinessPartner businessPartner1 = businessPartnerDao.getBusinessPartner(businessPartner2.getSeniorPartner().longValue());

						String businessPartnerL3Acc = businessPartner.getAccountNumber();
						String businessPartnerL2Acc = businessPartner2.getAccountNumber();
						String businessPartnerL1Acc = businessPartner1.getAccountNumber();

						com.eot.dtos.common.Account thirdPartyMirroAc = getAccount("1000000000109", EOTConstants.ACCOUNT_TYPE_WALLET + "", "BANK_TP_POOL_ACC", businessPartner.getBank().getBankId() + "");
						com.eot.dtos.common.Account mgurushPoolAc = getAccount(PoolAccountNumber, poolAccountType.toString(), poolAccountAlias, businessPartner.getBank().getBankId() + "");
						com.eot.dtos.common.Account businesssPartnerL1_poolAcc = getAccount(businessPartnerL1Acc, EOTConstants.ACCOUNT_TYPE_WALLET + "", "EOT Mobile - mGurush", businessPartner.getBank().getBankId() + "");
						com.eot.dtos.common.Account businesssPartnerL2_poolAcc = getAccount(businessPartnerL2Acc, EOTConstants.ACCOUNT_TYPE_WALLET + "", "EOT Mobile - mGurush", businessPartner.getBank().getBankId() + "");
						com.eot.dtos.common.Account businesssPartnerL3_poolAcc = getAccount(businessPartnerL3Acc, EOTConstants.ACCOUNT_TYPE_WALLET + "", "EOT Mobile - mGurush", businessPartner.getBank().getBankId() + "");

						LimitDTO limit_mGurush_pool = new LimitDTO();
						limit_mGurush_pool.setCustomerAccount(thirdPartyMirroAc);
						limit_mGurush_pool.setAmount(limitUpdateReqDTO.getBillAmount());
						limit_mGurush_pool.setOtherAccount(mgurushPoolAc);
						limit_mGurush_pool.setLimit_cr_desc("Limit credited for acc" + businesssPartnerL3_poolAcc);
						limit_mGurush_pool.setLimit_dr_desc("Limit debited for acc" + businesssPartnerL3_poolAcc);
						limit_mGurush_pool.setTransactionType(EOTConstants.TXN_TYPE_LIMIT_UPDATE + "");
						limit_mGurush_pool.setAmount(limitUpdateReqDTO.getBillAmount());
						limit_mGurush_pool.setReferenceID(businessPartner.getCode());
						limit_mGurush_pool.setChannelType(EOTConstants.EOT_CHANNEL);
						limit_mGurush_pool.setBusinessPartnerCode(businessPartner.getCode());
						
						LimitDTO limit_businessPartnerL1_acc = new LimitDTO();
						limit_businessPartnerL1_acc.setCustomerAccount(mgurushPoolAc);
						limit_businessPartnerL1_acc.setAmount(limitUpdateReqDTO.getBillAmount());
						limit_businessPartnerL1_acc.setAmount(limitUpdateReqDTO.getBillAmount());
						limit_businessPartnerL1_acc.setOtherAccount(businesssPartnerL1_poolAcc);
						limit_businessPartnerL1_acc.setLimit_cr_desc("Limit credited " + businesssPartnerL3_poolAcc);
						limit_businessPartnerL1_acc.setLimit_dr_desc("Limit debited for acc" + businesssPartnerL3_poolAcc);
						limit_businessPartnerL1_acc.setTransactionType(EOTConstants.TXN_TYPE_LIMIT_UPDATE + "");
						limit_businessPartnerL1_acc.setAmount(limitUpdateReqDTO.getBillAmount());
						limit_businessPartnerL1_acc.setReferenceID(businessPartner.getCode());
						limit_businessPartnerL1_acc.setChannelType(EOTConstants.EOT_CHANNEL);
						limit_businessPartnerL1_acc.setBusinessPartnerCode(businessPartner.getCode());

						LimitDTO limit_businessPartnerL2_acc = new LimitDTO();
						limit_businessPartnerL2_acc.setCustomerAccount(businesssPartnerL1_poolAcc);
						limit_businessPartnerL2_acc.setAmount(limitUpdateReqDTO.getBillAmount());
						limit_businessPartnerL2_acc.setOtherAccount(businesssPartnerL2_poolAcc);
						limit_businessPartnerL2_acc.setAmount(limitUpdateReqDTO.getBillAmount());
						limit_businessPartnerL2_acc.setLimit_cr_desc("Limit credited " + businesssPartnerL3_poolAcc);
						limit_businessPartnerL2_acc.setLimit_dr_desc("Limit debited for acc" + businesssPartnerL3_poolAcc);
						limit_businessPartnerL2_acc.setTransactionType(EOTConstants.TXN_TYPE_LIMIT_UPDATE + "");
						limit_businessPartnerL2_acc.setAmount(limitUpdateReqDTO.getBillAmount());
						limit_businessPartnerL2_acc.setReferenceID(businessPartner.getCode());
						limit_businessPartnerL2_acc.setChannelType(EOTConstants.EOT_CHANNEL);
						limit_businessPartnerL2_acc.setBusinessPartnerCode(businessPartner.getCode());

						LimitDTO limit_businessPartnerL3_acc = new LimitDTO();
						limit_businessPartnerL3_acc.setCustomerAccount(businesssPartnerL2_poolAcc);
						limit_businessPartnerL3_acc.setAmount(limitUpdateReqDTO.getBillAmount());
						limit_businessPartnerL3_acc.setOtherAccount(businesssPartnerL3_poolAcc);
						limit_businessPartnerL3_acc.setLimit_cr_desc("Limit credited " + businesssPartnerL3_poolAcc);
						limit_businessPartnerL3_acc.setLimit_dr_desc("Limit debited for acc" + businesssPartnerL3_poolAcc);
						limit_businessPartnerL3_acc.setTransactionType(EOTConstants.TXN_TYPE_LIMIT_UPDATE + "");
						limit_businessPartnerL3_acc.setAmount(limitUpdateReqDTO.getBillAmount());
						limit_businessPartnerL3_acc.setReferenceID(businessPartner.getCode());
						limit_businessPartnerL3_acc.setChannelType(EOTConstants.EOT_CHANNEL);
						limit_businessPartnerL3_acc.setBusinessPartnerCode(businessPartner.getCode());

						limitDtoList.add(0, limit_mGurush_pool);
						limitDtoList.add(1, limit_businessPartnerL1_acc);
						limitDtoList.add(2, limit_businessPartnerL2_acc);
						limitDtoList.add(3, limit_businessPartnerL3_acc);

						// core account processing
						LimitDTO transactionLimitDTO = new LimitDTO();
						transactionLimitDTO.setHeaderList(limitDtoList);
						try {
							transactionLimitDTO = processRequest(CoreUrls.LIMIT_POSTING_URL, transactionLimitDTO, com.eot.dtos.banking.LimitDTO.class);
							if (transactionLimitDTO.getErrorCode() != 0) {
								// throw new EOTException("ERROR_"+transactionLimitDTO.getErrorCode());
								limitUpdateRespDTO.setMessage("FAILED");
								limitUpdateRespDTO.setResponse(1);
								apiLogs.setStatus(EOTConstants.API_STATUS_FAIL);
								System.out.println("*******************FAILED FROM CORE************************");
								customerDao.update(apiLogs);
								return limitUpdateRespDTO;

							}
							limitUpdateRespDTO.setMessage("SUCCESSFUL");
							limitUpdateRespDTO.setResponse(0);
							apiLogs.setStatus(EOTConstants.API_STATUS_SUCCESS);
							customerDao.update(apiLogs);

						} catch (Exception e) {
							System.out.println("*******************EXCEPTION************************");
							e.printStackTrace();
							limitUpdateRespDTO.setMessage("Fail");
							limitUpdateRespDTO.setResponse(1);
							// TODO: handle exception
							// throw new EOTException(e.getErrorCode());
						}

					}
					if (businessPartner.getPartnerType().intValue() == EOTConstants.BULKPAYMENT_PARTNER_TYPE.intValue()) {

						Account account = null;// need to load account form account table
						String bulkPayPartnerAcc = businessPartner.getAccountNumber();
						// ---------------------------------
						com.eot.dtos.common.Account thirdPartyMirroorAc = getAccount("1000000000109", EOTConstants.ACCOUNT_TYPE_WALLET + "", "BANK_TP_POOL_ACC", businessPartner.getBank().getBankId() + "");
						com.eot.dtos.common.Account mgurushPoolAc = getAccount(PoolAccountNumber, poolAccountType.toString(), poolAccountAlias, businessPartner.getBank().getBankId() + "");
						com.eot.dtos.common.Account businesssPartnerL1_poolAcc = getAccount(bulkPayPartnerAcc, EOTConstants.ACCOUNT_TYPE_WALLET + "", "EOT Mobile - mGurush", businessPartner.getBank().getBankId() + "");

						LimitDTO limit_mGurush_pool = new LimitDTO();
						limit_mGurush_pool.setCustomerAccount(thirdPartyMirroorAc);
						limit_mGurush_pool.setAmount(limitUpdateReqDTO.getBillAmount());
						limit_mGurush_pool.setOtherAccount(mgurushPoolAc);
						limit_mGurush_pool.setLimit_cr_desc("Limit credited for acc" + bulkPayPartnerAcc);
						limit_mGurush_pool.setLimit_dr_desc("Limit debited for acc" + bulkPayPartnerAcc);
						limit_mGurush_pool.setChannelType(EOTConstants.EOT_CHANNEL);
						limit_mGurush_pool.setReferenceID(referenceId);
						limit_mGurush_pool.setAmount(limitUpdateReqDTO.getBillAmount());
						limit_mGurush_pool.setTransactionType(EOTConstants.TXN_TYPE_LIMIT_UPDATE + "");
						limit_mGurush_pool.setReferenceID(businessPartner.getCode());
						limit_mGurush_pool.setChannelType(EOTConstants.EOT_CHANNEL);
						limit_mGurush_pool.setBusinessPartnerCode(businessPartner.getCode());

						LimitDTO limit_businessPartnerL1_acc = new LimitDTO();
						limit_businessPartnerL1_acc.setCustomerAccount(mgurushPoolAc);
						limit_businessPartnerL1_acc.setAmount(limitUpdateReqDTO.getBillAmount());
						limit_businessPartnerL1_acc.setOtherAccount(businesssPartnerL1_poolAcc);
						limit_businessPartnerL1_acc.setLimit_cr_desc("Limit credited " + bulkPayPartnerAcc);
						limit_businessPartnerL1_acc.setLimit_dr_desc("Limit debited for acc" + bulkPayPartnerAcc);
						limit_businessPartnerL1_acc.setTransactionType(EOTConstants.TXN_TYPE_LIMIT_UPDATE + "");
						limit_businessPartnerL1_acc.setChannelType(EOTConstants.EOT_CHANNEL);
						limit_businessPartnerL1_acc.setReferenceID(referenceId);
						limit_businessPartnerL1_acc.setAmount(limitUpdateReqDTO.getBillAmount());
						limit_businessPartnerL1_acc.setReferenceID(businessPartner.getCode());
						limit_businessPartnerL1_acc.setChannelType(EOTConstants.EOT_CHANNEL);
						limit_businessPartnerL1_acc.setBusinessPartnerCode(businessPartner.getCode());

						/*
						 * limitDtoList.set(0, limit_mGurush_pool); limitDtoList.set(1,
						 * limit_businessPartnerL1_acc);
						 */

						limitDtoList.add(0, limit_mGurush_pool);
						limitDtoList.add(1, limit_businessPartnerL1_acc);

						LimitDTO transactionLimitDTO = new LimitDTO();
						transactionLimitDTO.setHeaderList(limitDtoList);
						try {
							transactionLimitDTO = processRequest(CoreUrls.LIMIT_POSTING_URL, transactionLimitDTO, com.eot.dtos.banking.LimitDTO.class);
							if (transactionLimitDTO.getErrorCode() != 0) {
								// throw new EOTException("ERROR_"+transactionLimitDTO.getErrorCode());
								System.out.println("*******************FAILED FROM CORE************************");
								limitUpdateRespDTO.setMessage("FAILED");
								limitUpdateRespDTO.setResponse(1);
								limitUpdateRespDTO.setMessage("Bill Number is not Registered");
								apiLogs.setStatus(EOTConstants.API_STATUS_FAIL);
								customerDao.update(apiLogs);
								return limitUpdateRespDTO;

							}
							limitUpdateRespDTO.setMessage("SUCCESSFUL");
							limitUpdateRespDTO.setResponse(0);
							apiLogs.setStatus(EOTConstants.API_STATUS_SUCCESS);
							customerDao.saveOrUpdate(apiLogs);

						} catch (Exception e) {
							System.out.println("*******************EXCEPTION************************");
							e.printStackTrace();
							limitUpdateRespDTO.setMessage("Fail");
							limitUpdateRespDTO.setResponse(1);
							// TODO: handle exception
							// throw new EOTException(e.getErrorCode());
						}

					}
				}
			} else {
				if (agent != null && agent.getType() == 0) {
					limitUpdateRespDTO.setMessage("Not an agent");
					limitUpdateRespDTO.setResponse(1);
					return limitUpdateRespDTO;
				} else if (agent != null && agent.getType() != 0) {
					referenceId = agent.getCustomerId() + "";
					businessPartner = agent.getBusinessPartner();

					Set<CustomerAccount> setAccouts = agent.getCustomerAccounts();
					CustomerAccount customerAccounts = null;
					for (CustomerAccount customerAccount : setAccouts) {
						if(customerAccount.getAccount().getAliasType()==1)
							customerAccounts = customerAccount;
					}
					if (businessPartner == null) {
						// CustomerAccount customerAccount =
						// customerDao.findCustomerAccountBy(agent.getCustomerId());
						String bankPoolAccount = customerAccounts.getBank().getAccount().getAccountNumber();
						String bankPoolAccountAlias = customerAccounts.getBank().getAccount().getAlias();

						com.eot.dtos.common.Account thirdPartyMirrorAc = getAccount("1000000000109", EOTConstants.ACCOUNT_TYPE_WALLET + "", "BANK_TP_POOL_ACC", customerAccounts.getBank().getBankId() + "");
						com.eot.dtos.common.Account mgurushPoolAc = getAccount(bankPoolAccount, EOTConstants.ACCOUNT_TYPE_WALLET + "", bankPoolAccountAlias, customerAccounts.getBank().getBankId() + "");
						com.eot.dtos.common.Account agentAccount = getAccount(customerAccounts.getAccountNumber(), EOTConstants.ACCOUNT_TYPE_WALLET + "", customerAccounts.getAccount().getAlias(), customerAccounts.getBank().getBankId() + "");

						LimitDTO limit_mGurush_pool = new LimitDTO();
						limit_mGurush_pool.setCustomerAccount(thirdPartyMirrorAc);
						limit_mGurush_pool.setOtherAccount(mgurushPoolAc);
						limit_mGurush_pool.setLimit_cr_desc("Limit credited for acc" + agentAccount.getAccountNO());
						limit_mGurush_pool.setLimit_dr_desc("Limit debited for acc" + agentAccount.getAccountNO());
						limit_mGurush_pool.setTransactionType(EOTConstants.TXN_TYPE_LIMIT_UPDATE + "");
						limit_mGurush_pool.setChannelType(EOTConstants.EOT_CHANNEL);
						limit_mGurush_pool.setReferenceID(referenceId);
						limit_mGurush_pool.setAmount(limitUpdateReqDTO.getBillAmount());

						LimitDTO limit_Agent_acc = new LimitDTO();
						limit_Agent_acc.setCustomerAccount(mgurushPoolAc);
						limit_Agent_acc.setOtherAccount(agentAccount);
						limit_Agent_acc.setLimit_cr_desc("Limit credited " + agentAccount.getAccountNO());
						limit_Agent_acc.setLimit_dr_desc("Limit debited for acc" + agentAccount.getAccountNO());
						limit_Agent_acc.setTransactionType(EOTConstants.TXN_TYPE_LIMIT_UPDATE + "");
						limit_Agent_acc.setChannelType(EOTConstants.EOT_CHANNEL);
						limit_Agent_acc.setReferenceID(referenceId);
						limit_Agent_acc.setAmount(limitUpdateReqDTO.getBillAmount());

						limitDtoList.add(0, limit_mGurush_pool);
						limitDtoList.add(1, limit_Agent_acc);

						// core account processing
						LimitDTO transactionLimitDTO = new LimitDTO();
						transactionLimitDTO.setHeaderList(limitDtoList);
						try {
							transactionLimitDTO = processRequest(CoreUrls.LIMIT_POSTING_URL, transactionLimitDTO, com.eot.dtos.banking.LimitDTO.class);
							if (transactionLimitDTO.getErrorCode() != 0) {
								// throw new EOTException("ERROR_"+transactionLimitDTO.getErrorCode());
								limitUpdateRespDTO.setMessage("FAILED");
								limitUpdateRespDTO.setResponse(1);
								apiLogs.setStatus(EOTConstants.API_STATUS_FAIL);
								customerDao.update(apiLogs);
								return limitUpdateRespDTO;

							}
							limitUpdateRespDTO.setMessage("SUCCESSFUL");
							limitUpdateRespDTO.setResponse(0);
							apiLogs.setStatus(EOTConstants.API_STATUS_SUCCESS);
							customerDao.update(apiLogs);

						} catch (Exception e) {
							e.printStackTrace();
							limitUpdateRespDTO.setMessage("Fail");
							limitUpdateRespDTO.setResponse(1);
							// TODO: handle exception
							// throw new EOTException(e.getErrorCode());
						}
					} else {
						String PoolAccountNumber = businessPartner.getBank().getAccount().getAccountNumber();
						Integer poolAccountType = businessPartner.getBank().getAccount().getAccountType();
						String poolAccountAlias = businessPartner.getBank().getAccount().getAlias();

						if (customerAccounts == null) {
							// throw new EOTCoreException("");
						}
						com.eot.dtos.common.Account thirdPartyMirrorAc = getAccount("1000000000109", EOTConstants.ACCOUNT_TYPE_WALLET + "", "BANK_TP_POOL_ACC", businessPartner.getBank().getBankId() + "");
						com.eot.dtos.common.Account mgurushPoolAc = getAccount(PoolAccountNumber, EOTConstants.ACCOUNT_TYPE_WALLET + "", poolAccountAlias, businessPartner.getBank().getBankId() + "");
						com.eot.dtos.common.Account agentAccount = getAccount(customerAccounts.getAccountNumber(), EOTConstants.ACCOUNT_TYPE_WALLET + "", customerAccounts.getAccount().getAlias(), businessPartner.getBank().getBankId() + "");

						if (businessPartner.getPartnerType().intValue() == EOTConstants.BUSINESS_PARTNER_TYPE_L1.intValue()) {
							String businessPartnerL1Acc = businessPartner.getAccountNumber();
							// -----------------------

							com.eot.dtos.common.Account businesssPartnerL1_poolAcc = getAccount(businessPartnerL1Acc, EOTConstants.ACCOUNT_TYPE_WALLET + "", "EOT Mobile - mGurush", businessPartner.getBank().getBankId() + "");

							LimitDTO limit_mGurush_pool = new LimitDTO();
							limit_mGurush_pool.setCustomerAccount(thirdPartyMirrorAc);
							limit_mGurush_pool.setOtherAccount(mgurushPoolAc);
							limit_mGurush_pool.setLimit_cr_desc("Limit credited for acc" + agentAccount.getAccountNO());
							limit_mGurush_pool.setLimit_dr_desc("Limit debited for acc" + agentAccount.getAccountNO());
							limit_mGurush_pool.setTransactionType(EOTConstants.TXN_TYPE_LIMIT_UPDATE + "");
							limit_mGurush_pool.setChannelType(EOTConstants.EOT_CHANNEL);
							limit_mGurush_pool.setReferenceID(referenceId);
							limit_mGurush_pool.setAmount(limitUpdateReqDTO.getBillAmount());

							LimitDTO limit_businessPartnerL1_acc = new LimitDTO();
							limit_businessPartnerL1_acc.setCustomerAccount(mgurushPoolAc);
							limit_businessPartnerL1_acc.setOtherAccount(businesssPartnerL1_poolAcc);
							limit_businessPartnerL1_acc.setLimit_cr_desc("Limit credited " + agentAccount.getAccountNO());
							limit_businessPartnerL1_acc.setLimit_dr_desc("Limit debited for acc" + agentAccount.getAccountNO());
							limit_businessPartnerL1_acc.setTransactionType(EOTConstants.TXN_TYPE_LIMIT_UPDATE + "");
							limit_businessPartnerL1_acc.setChannelType(EOTConstants.EOT_CHANNEL);
							limit_businessPartnerL1_acc.setReferenceID(referenceId);
							limit_businessPartnerL1_acc.setAmount(limitUpdateReqDTO.getBillAmount());

							LimitDTO limit_Agent_acc = new LimitDTO();
							limit_Agent_acc.setCustomerAccount(businesssPartnerL1_poolAcc);
							limit_Agent_acc.setOtherAccount(agentAccount);
							limit_Agent_acc.setLimit_cr_desc("Limit credited " + agentAccount.getAccountNO());
							limit_Agent_acc.setLimit_dr_desc("Limit debited for acc" + agentAccount.getAccountNO());
							limit_Agent_acc.setTransactionType(EOTConstants.TXN_TYPE_LIMIT_UPDATE + "");
							limit_Agent_acc.setChannelType(EOTConstants.EOT_CHANNEL);
							limit_Agent_acc.setReferenceID(referenceId);
							limit_Agent_acc.setAmount(limitUpdateReqDTO.getBillAmount());

							limitDtoList.add(0, limit_mGurush_pool);
							limitDtoList.add(1, limit_businessPartnerL1_acc);
							limitDtoList.add(2, limit_Agent_acc);

							// core account processing
							LimitDTO transactionLimitDTO = new LimitDTO();
							transactionLimitDTO.setHeaderList(limitDtoList);
							try {
								transactionLimitDTO = processRequest(CoreUrls.LIMIT_POSTING_URL, transactionLimitDTO, com.eot.dtos.banking.LimitDTO.class);
								if (transactionLimitDTO.getErrorCode() != 0) {
									// throw new EOTException("ERROR_"+transactionLimitDTO.getErrorCode());
									System.out.println("*******************FAILED FROM CORE************************");
									limitUpdateRespDTO.setMessage("FAILED");
									limitUpdateRespDTO.setResponse(1);
									apiLogs.setStatus(EOTConstants.API_STATUS_FAIL);
									customerDao.update(apiLogs);
									return limitUpdateRespDTO;

								}
								limitUpdateRespDTO.setMessage("SUCCESSFUL");
								limitUpdateRespDTO.setResponse(0);
								apiLogs.setStatus(EOTConstants.API_STATUS_SUCCESS);
								customerDao.update(apiLogs);

							} catch (Exception e) {
								System.out.println("*******************EXCEPTION************************");
								e.printStackTrace();
								limitUpdateRespDTO.setMessage("Fail");
								limitUpdateRespDTO.setResponse(1);
								// TODO: handle exception
								// throw new EOTException(e.getErrorCode());
							}

						}
						if (businessPartner.getPartnerType().intValue() == EOTConstants.BUSINESS_PARTNER_TYPE_L2.intValue()) {
							BusinessPartner businessPartner1 = businessPartnerDao.getBusinessPartner(businessPartner.getSeniorPartner().longValue());
							String businessPartnerL2Acc = businessPartner.getAccountNumber();
							String businessPartnerL1Acc = businessPartner1.getAccountNumber();

							com.eot.dtos.common.Account businesssPartnerL1_poolAcc = getAccount(businessPartnerL1Acc, EOTConstants.ACCOUNT_TYPE_WALLET + "", "EOT Mobile - mGurush", businessPartner.getBank().getBankId() + "");
							com.eot.dtos.common.Account businesssPartnerL2_poolAcc = getAccount(businessPartnerL2Acc, EOTConstants.ACCOUNT_TYPE_WALLET + "", "EOT Mobile - mGurush", businessPartner.getBank().getBankId() + "");

							LimitDTO limit_mGurush_pool = new LimitDTO();
							limit_mGurush_pool.setCustomerAccount(thirdPartyMirrorAc);
							limit_mGurush_pool.setOtherAccount(mgurushPoolAc);
							limit_mGurush_pool.setLimit_cr_desc("Limit credited for acc" + businessPartnerL2Acc);
							limit_mGurush_pool.setLimit_dr_desc("Limit debited for acc" + businessPartnerL2Acc);
							limit_mGurush_pool.setTransactionType(EOTConstants.TXN_TYPE_LIMIT_UPDATE + "");
							limit_mGurush_pool.setChannelType(EOTConstants.EOT_CHANNEL);
							limit_mGurush_pool.setReferenceID(referenceId);
							limit_mGurush_pool.setAmount(limitUpdateReqDTO.getBillAmount());

							LimitDTO limit_businessPartnerL1_acc = new LimitDTO();
							limit_businessPartnerL1_acc.setTransactionType(EOTConstants.TXN_TYPE_LIMIT_UPDATE + "");
							limit_businessPartnerL1_acc.setCustomerAccount(mgurushPoolAc);
							limit_businessPartnerL1_acc.setOtherAccount(businesssPartnerL1_poolAcc);
							limit_businessPartnerL1_acc.setLimit_cr_desc("Limit credited " + businessPartnerL2Acc);
							limit_businessPartnerL1_acc.setLimit_dr_desc("Limit debited for acc" + businessPartnerL2Acc);
							limit_businessPartnerL1_acc.setChannelType(EOTConstants.EOT_CHANNEL);
							limit_businessPartnerL1_acc.setReferenceID(referenceId);
							limit_businessPartnerL1_acc.setAmount(limitUpdateReqDTO.getBillAmount());

							LimitDTO limit_businessPartnerL2_acc = new LimitDTO();
							limit_businessPartnerL2_acc.setTransactionType(EOTConstants.TXN_TYPE_LIMIT_UPDATE + "");
							limit_businessPartnerL2_acc.setCustomerAccount(businesssPartnerL1_poolAcc);
							limit_businessPartnerL2_acc.setOtherAccount(businesssPartnerL2_poolAcc);
							limit_businessPartnerL2_acc.setLimit_cr_desc("Limit credited " + businessPartnerL2Acc);
							limit_businessPartnerL2_acc.setLimit_dr_desc("Limit debited for acc" + businessPartnerL2Acc);
							limit_businessPartnerL2_acc.setChannelType(EOTConstants.EOT_CHANNEL);
							limit_businessPartnerL2_acc.setReferenceID(referenceId);
							limit_businessPartnerL2_acc.setAmount(limitUpdateReqDTO.getBillAmount());

							LimitDTO limit_Agent_acc = new LimitDTO();
							limit_Agent_acc.setTransactionType(EOTConstants.TXN_TYPE_LIMIT_UPDATE + "");
							limit_Agent_acc.setCustomerAccount(businesssPartnerL2_poolAcc);
							limit_Agent_acc.setOtherAccount(agentAccount);
							limit_Agent_acc.setLimit_cr_desc("Limit credited " + agentAccount.getAccountNO());
							limit_Agent_acc.setLimit_dr_desc("Limit debited for acc" + agentAccount.getAccountNO());
							limit_Agent_acc.setChannelType(EOTConstants.EOT_CHANNEL);
							limit_Agent_acc.setReferenceID(referenceId);
							limit_Agent_acc.setAmount(limitUpdateReqDTO.getBillAmount());

							limitDtoList.add(0, limit_mGurush_pool);
							limitDtoList.add(1, limit_businessPartnerL1_acc);
							limitDtoList.add(2, limit_businessPartnerL2_acc);
							limitDtoList.add(3, limit_Agent_acc);

							// core transaction call
							// core account processing
							LimitDTO transactionLimitDTO = new LimitDTO();
							transactionLimitDTO.setHeaderList(limitDtoList);
							try {
								transactionLimitDTO = processRequest(CoreUrls.LIMIT_POSTING_URL, transactionLimitDTO, com.eot.dtos.banking.LimitDTO.class);
								if (transactionLimitDTO.getErrorCode() != 0) {
									// throw new EOTException("ERROR_"+transactionLimitDTO.getErrorCode());
									System.out.println("*******************FAILED FROM CORE************************");
									limitUpdateRespDTO.setMessage("FAILED");
									limitUpdateRespDTO.setResponse(1);
									apiLogs.setStatus(EOTConstants.API_STATUS_FAIL);
									customerDao.update(apiLogs);
									return limitUpdateRespDTO;

								}
								limitUpdateRespDTO.setMessage("SUCCESSFUL");
								limitUpdateRespDTO.setResponse(0);
								apiLogs.setStatus(EOTConstants.API_STATUS_SUCCESS);
								customerDao.update(apiLogs);

							} catch (Exception e) {
								System.out.println("*******************EXCEPTION************************");
								e.printStackTrace();
								limitUpdateRespDTO.setMessage("Fail");
								limitUpdateRespDTO.setResponse(1);
								// TODO: handle exception
								// throw new EOTException(e.getErrorCode());
							}
						}
						if (businessPartner.getPartnerType().intValue() == EOTConstants.BUSINESS_PARTNER_TYPE_L3.intValue()) {
							BusinessPartner businessPartner2 = businessPartnerDao.getBusinessPartner(businessPartner.getSeniorPartner().longValue());
							BusinessPartner businessPartner1 = businessPartnerDao.getBusinessPartner(businessPartner2.getSeniorPartner().longValue());

							String businessPartnerL3Acc = businessPartner.getAccountNumber();
							String businessPartnerL2Acc = businessPartner2.getAccountNumber();
							String businessPartnerL1Acc = businessPartner1.getAccountNumber();

							com.eot.dtos.common.Account businesssPartnerL1_poolAcc = getAccount(businessPartnerL1Acc, EOTConstants.ACCOUNT_TYPE_WALLET + "", "EOT Mobile - mGurush", businessPartner.getBank().getBankId() + "");
							com.eot.dtos.common.Account businesssPartnerL2_poolAcc = getAccount(businessPartnerL2Acc, EOTConstants.ACCOUNT_TYPE_WALLET + "", "EOT Mobile - mGurush", businessPartner.getBank().getBankId() + "");
							com.eot.dtos.common.Account businesssPartnerL3_poolAcc = getAccount(businessPartnerL3Acc, EOTConstants.ACCOUNT_TYPE_WALLET + "", "EOT Mobile - mGurush", businessPartner.getBank().getBankId() + "");

							LimitDTO limit_mGurush_pool = new LimitDTO();
							limit_mGurush_pool.setTransactionType(EOTConstants.TXN_TYPE_LIMIT_UPDATE + "");
							limit_mGurush_pool.setCustomerAccount(thirdPartyMirrorAc);
							limit_mGurush_pool.setOtherAccount(mgurushPoolAc);
							limit_mGurush_pool.setLimit_cr_desc("Limit credited for acc" + agentAccount.getAccountNO());
							limit_mGurush_pool.setLimit_dr_desc("Limit debited for acc" + agentAccount.getAccountNO());
							limit_mGurush_pool.setChannelType(EOTConstants.EOT_CHANNEL);
							limit_mGurush_pool.setReferenceID(referenceId);
							limit_mGurush_pool.setAmount(limitUpdateReqDTO.getBillAmount());

							LimitDTO limit_businessPartnerL1_acc = new LimitDTO();
							limit_businessPartnerL1_acc.setTransactionType(EOTConstants.TXN_TYPE_LIMIT_UPDATE + "");
							limit_businessPartnerL1_acc.setCustomerAccount(mgurushPoolAc);
							limit_businessPartnerL1_acc.setOtherAccount(businesssPartnerL1_poolAcc);
							limit_businessPartnerL1_acc.setLimit_cr_desc("Limit credited " + agentAccount.getAccountNO());
							limit_businessPartnerL1_acc.setLimit_dr_desc("Limit debited for acc" + agentAccount.getAccountNO());
							limit_businessPartnerL1_acc.setChannelType(EOTConstants.EOT_CHANNEL);
							limit_businessPartnerL1_acc.setReferenceID(referenceId);
							limit_businessPartnerL1_acc.setAmount(limitUpdateReqDTO.getBillAmount());

							LimitDTO limit_businessPartnerL2_acc = new LimitDTO();
							limit_businessPartnerL2_acc.setTransactionType(EOTConstants.TXN_TYPE_LIMIT_UPDATE + "");
							limit_businessPartnerL2_acc.setCustomerAccount(businesssPartnerL1_poolAcc);
							limit_businessPartnerL2_acc.setOtherAccount(businesssPartnerL2_poolAcc);
							limit_businessPartnerL2_acc.setLimit_cr_desc("Limit credited " + agentAccount.getAccountNO());
							limit_businessPartnerL2_acc.setLimit_dr_desc("Limit debited for acc" + agentAccount.getAccountNO());
							limit_businessPartnerL2_acc.setChannelType(EOTConstants.EOT_CHANNEL);
							limit_businessPartnerL2_acc.setReferenceID(referenceId);
							limit_businessPartnerL2_acc.setAmount(limitUpdateReqDTO.getBillAmount());

							LimitDTO limit_businessPartnerL3_acc = new LimitDTO();
							limit_businessPartnerL3_acc.setTransactionType(EOTConstants.TXN_TYPE_LIMIT_UPDATE + "");
							limit_businessPartnerL3_acc.setCustomerAccount(businesssPartnerL2_poolAcc);
							limit_businessPartnerL3_acc.setOtherAccount(businesssPartnerL3_poolAcc);
							limit_businessPartnerL3_acc.setLimit_cr_desc("Limit credited " + agentAccount.getAccountNO());
							limit_businessPartnerL3_acc.setLimit_dr_desc("Limit debited for acc" + agentAccount.getAccountNO());
							limit_businessPartnerL3_acc.setChannelType(EOTConstants.EOT_CHANNEL);
							limit_businessPartnerL3_acc.setReferenceID(referenceId);
							limit_businessPartnerL3_acc.setAmount(limitUpdateReqDTO.getBillAmount());

							LimitDTO limit_Agent_acc = new LimitDTO();
							limit_Agent_acc.setTransactionType(EOTConstants.TXN_TYPE_LIMIT_UPDATE + "");
							limit_Agent_acc.setCustomerAccount(businesssPartnerL3_poolAcc);
							limit_Agent_acc.setOtherAccount(agentAccount);
							limit_Agent_acc.setLimit_cr_desc("Limit credited " + agentAccount.getAccountNO());
							limit_Agent_acc.setLimit_dr_desc("Limit debited for acc" + agentAccount.getAccountNO());
							limit_Agent_acc.setChannelType(EOTConstants.EOT_CHANNEL);
							limit_Agent_acc.setReferenceID(referenceId);
							limit_Agent_acc.setAmount(limitUpdateReqDTO.getBillAmount());

							limitDtoList.add(0, limit_mGurush_pool);
							limitDtoList.add(1, limit_businessPartnerL1_acc);
							limitDtoList.add(2, limit_businessPartnerL2_acc);
							limitDtoList.add(3, limit_businessPartnerL3_acc);
							limitDtoList.add(4, limit_Agent_acc);

							// core transaction call
							// core account processing
							LimitDTO transactionLimitDTO = new LimitDTO();
							transactionLimitDTO.setTransactionType(EOTConstants.TXN_TYPE_LIMIT_UPDATE + "");
							transactionLimitDTO.setHeaderList(limitDtoList);
							try {
								transactionLimitDTO = processRequest(CoreUrls.LIMIT_POSTING_URL, transactionLimitDTO, com.eot.dtos.banking.LimitDTO.class);
								if (transactionLimitDTO.getErrorCode() != 0) {
									// throw new EOTException("ERROR_"+transactionLimitDTO.getErrorCode());
									System.out.println("*******************FAILED FROM CORE************************");
									limitUpdateRespDTO.setMessage("FAILED");
									limitUpdateRespDTO.setResponse(1);
									apiLogs.setStatus(EOTConstants.API_STATUS_FAIL);
									customerDao.update(apiLogs);
									return limitUpdateRespDTO;

								}
								limitUpdateRespDTO.setMessage("SUCCESSFUL");
								limitUpdateRespDTO.setResponse(0);
								apiLogs.setStatus(EOTConstants.API_STATUS_SUCCESS);
								customerDao.update(apiLogs);

							} catch (Exception e) {
								System.out.println("*******************EXCEPTION************************");
								e.printStackTrace();
								limitUpdateRespDTO.setMessage("Fail");
								limitUpdateRespDTO.setResponse(1);
								// TODO: handle exception
								// throw new EOTException(e.getErrorCode());
							}

						}
					}
				}

			}
		}
		return limitUpdateRespDTO;
	}

	private com.eot.dtos.common.Account getAccount(String accountNumber, String accountType, String accountAlias, String bankCode) {
		com.eot.dtos.common.Account account = new com.eot.dtos.common.Account();
		account.setAccountAlias(accountAlias);
		account.setAccountNO(accountNumber);
		account.setAccountType(accountType);
		account.setBankCode(bankCode);
		return account;

	}
	
	@Override
	public LimitUpdateRespDTO fetchDetails(LimitUpdateReqDTO limitUpdateReqDTO) {
		
		LimitUpdateRespDTO limitUpdateRespDTO = new LimitUpdateRespDTO();
		String billCode = limitUpdateReqDTO.getBillNumber();

		if (billCode.isEmpty()) {
			limitUpdateRespDTO.setMessage("Empty Bill Number");
			limitUpdateRespDTO.setResponse(1);
			return limitUpdateRespDTO;
		} else {

			List<LimitDTO> limitDtoList = new ArrayList<LimitDTO>();
			String referenceId = "";

			BusinessPartner businessPartner = null;
			Customer agent = customerDao.getAgentByAgentCode(billCode);
			if (agent == null) {
				businessPartner = businessPartnerDao.getBusinessPartnerByContactNumber(billCode);
				if (businessPartner == null) {
					limitUpdateRespDTO.setMessage("Bill Number is not Registered");
					limitUpdateRespDTO.setResponse(1);
					return limitUpdateRespDTO;
				} else if (businessPartner != null) {
					limitUpdateRespDTO.setMessage("OK");
					limitUpdateRespDTO.setResponse(0);
				}
			}else
			{
				limitUpdateRespDTO.setMessage("OK");
				limitUpdateRespDTO.setResponse(0);
			}
		}
		return limitUpdateRespDTO;
	}

	/**
	 * @author bidyut kalita rest service call to core
	 */
	public <T extends com.eot.dtos.common.Header> T processRequest(String url, T obj, Class<T> type) throws Fault {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
		obj.setRequestChannel("WEB");
		obj.setChannelType(EOTConstants.EOT_CHANNEL);
		obj = restTemplate.postForObject(url, obj, type);

		return obj;

	}

}

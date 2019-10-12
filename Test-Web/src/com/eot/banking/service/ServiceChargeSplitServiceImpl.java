package com.eot.banking.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eot.banking.dao.BankDao;
import com.eot.banking.dao.StakeHolderDao;
import com.eot.banking.dao.TransactionRulesDao;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.exceptions.EOTException;
import com.eot.entity.Bank;
import com.eot.entity.ServiceChargeSplit;
import com.eot.entity.StakeHolder;
import com.eot.entity.TransactionType;

@Service("serviceChargeSplitService")
@Transactional(readOnly=true)
public class ServiceChargeSplitServiceImpl implements ServiceChargeSplitService {

	@Autowired 
	private BankDao bankDao ;
	@Autowired
	private StakeHolderDao stakeHolderDao;
	@Autowired
	private TransactionRulesDao transactionRulesDao;

	@Override
	public List<TransactionType> getSCApplicableTransactionTypes(String locale) {
		return transactionRulesDao.getTransactionTypesWithServiceCharge(locale.substring(0, 2));
	}

	@Override
	@Transactional(readOnly=false,rollbackFor=Throwable.class)
	public void saveServiceChargeSplit(Integer bankId, Map<String,String[]> reqParam) throws EOTException {
		
		Map<String,Float> scTxnTypemap = new HashMap<String, Float>();
		
		Bank bank = bankDao.getBank(bankId);
		if(bank == null){
			throw new EOTException(ErrorConstants.INVALID_BANK);
		}
		Set<String> keys = reqParam.keySet();
			for(String key : keys ){
				if(!key.equals("bankId")){
				String[] tmp = key.split("-");
	
				String accNum = tmp[0];
				String txnTypeStr = tmp[1];
				String serviceChargePctStr = reqParam.get(key)[0] == null ||  "".equals( reqParam.get(key)[0].trim() ) ? "0" : reqParam.get(key)[0] ;
				
				float serviceChargePct =  0 ;
				
				try{
					serviceChargePct = Float.parseFloat( serviceChargePctStr );
				}catch(NumberFormatException ex){
					throw new EOTException(ErrorConstants.NON_NUMERIC_VALUE);
				}
				// Adding the SC % for each txnType.
				Float sc = scTxnTypemap.get(txnTypeStr) == null ? 0.0F : scTxnTypemap.get(txnTypeStr) ;
				scTxnTypemap.put(txnTypeStr, serviceChargePct + sc ) ;
				
				ServiceChargeSplit scSplit = stakeHolderDao.getServiceChargeSplit(accNum, new Integer(txnTypeStr),bankId);
				
				if(scSplit==null){
					scSplit = new ServiceChargeSplit();
				}
				
//				scSplit.setAccountNumber(accNum);
				scSplit.setServiceChargePct(serviceChargePct);
				TransactionType txnType = new TransactionType();
				txnType.setTransactionType(Integer.parseInt(txnTypeStr));
				scSplit.setTransactionType(txnType);
				scSplit.setBank(bank);
				transactionRulesDao.saveOrUpdate(scSplit);
	
			}
			
		}	
			// Ensuring the total SC % for a txnType is not greater than 100%
			for (String txnType : scTxnTypemap.keySet()){
				
				Float sc = scTxnTypemap.get(txnType) ;
				
				if( sc!=null && sc.floatValue() != 100.0F ){
					throw new EOTException(ErrorConstants.INVALID_STAKEHOLDER_SC_PERC);
				}
				
			}	
	}

	@Override
	public Map<String, Float> getServiceCharges(Integer bankId) throws EOTException {

		Bank bank = bankDao.getBank(bankId);
		if(bank == null){
			throw new EOTException(ErrorConstants.INVALID_BANK);
		}

		Map<String, Float> map = new HashMap<String, Float>();

		
		List<ServiceChargeSplit> list =  bankDao.getServiceChargeSplits(bankId);
		if(list != null){
			for(ServiceChargeSplit scp : list ){
//				map.put(scp.getAccountNumber()+"-"+scp.getTransactionType().getTransactionType(), scp.getServiceChargePct());
			}
		}

		return map;
	}

	@Override
	public Bank getBank(Integer bankId) throws EOTException{

		Bank bank = bankDao.getBank(bankId);
		if(bank == null){
			throw new EOTException(ErrorConstants.INVALID_BANK);
		}
		return bank;
	}
	
	@Override
	public List<StakeHolder> getstakeHolders() {
		return stakeHolderDao.getStakeHolders();
	}

}

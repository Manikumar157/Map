package com.eot.banking.service;

import java.io.EOFException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eot.banking.dao.CommissionDao;
import com.eot.banking.dto.CommisionSplitsDTO;
import com.eot.banking.dto.CommissionDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.utils.Page;
import com.eot.entity.CommisionSplits;
import com.eot.entity.Commission;
import com.eot.entity.CustomerProfiles;
import com.eot.entity.TransactionType;

@Service("commissionService")
public class CommissionServiceImpl implements CommissionService{
	
	@Autowired
	private CommissionDao commissionDao;
	
	/**************Added by bidyut*******************/
	public List<CustomerProfiles> loadCustomerListForCommission(CommissionDTO commissionDTO)
	{
		List<CustomerProfiles> customerProfileList=null;
		try
		{
			 customerProfileList= loadCustomerProfiles(commissionDTO.getBankId());
			System.out.println("before "+customerProfileList);
			for(int i=0;i<customerProfileList.size();i++)
			{
				CustomerProfiles customerProfile = customerProfileList.get(i);
				int profileId= (customerProfile.getProfileId()).intValue();
				if(profileId==1)
				{
					customerProfileList.remove(customerProfile);
				}
				
			}
			System.out.println("after "+customerProfileList);
		}catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return customerProfileList;
	}
	
	@Override
	public Page loadCommisionSlabDetails(Integer pageNumber , CommissionDTO commissionDTO) throws EOTException {
		Page page = commissionDao.loadCommissionSlabDetails(pageNumber , commissionDTO);
		return page;
	}
	
	@Override
	public List<CustomerProfiles> loadCustomerProfiles(Integer bankId) throws EOFException {
		
		return commissionDao.loadCustomerProfiles(bankId);
	}
	
	@Override
	public CommissionDTO getCommisionDetails(Long commissionId) throws EOFException {

		Commission commission  = commissionDao.loadCommissionDetailsByCommissionId(commissionId);
		CommissionDTO  commissionDTO = new CommissionDTO();
	if(commission !=null){
		commissionDTO.setCommisionId(commission.getCommissionId());
		commissionDTO.setBankId(commission.getBankId());
		commissionDTO.setCommission(commission.getCommissionPer());
		commissionDTO.setProfileId(commission.getProfileId());
		Integer [] transactionType = {commission.getTransactionTypeId()};
		commissionDTO.setTransactions(transactionType);
		commissionDTO.setTransactionTypeId(commission.getTransactionTypeId());
	}
		return commissionDTO;
	}
	
	
	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=Throwable.class )
	public CommissionDTO updateCommisionSlabs(CommissionDTO commissionDTO) throws EOFException {
		//delete selected slab 
		commissionDao.deleteCommissionSlab(commissionDTO.getCommisionId());
		
		//check for duplicate entry
		 List<Commission> commissionList = null;
			
				commissionList = commissionDao.loadCommisionSlab(commissionDTO.getTransactionTypeId(),commissionDTO.getBankId(),commissionDTO.getProfileId());
					 if(commissionList!=null ){
						   	throw new EOFException(ErrorConstants.COMMISSION_ALREDY_EXISTS);
					 }
			
		Commission commission = new Commission();
		commission.setBankId(commissionDTO.getBankId());
		commission.setProfileId(commissionDTO.getProfileId());
		commission.setCommissionPer(commissionDTO.getCommission());
		commission.setTransactionTypeId(commissionDTO.getTransactionTypeId());
		commissionDao.save(commission);
		return commissionDTO;
	}

	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=Throwable.class )
	public CommissionDTO saveCommisionSlabs(CommissionDTO commissionDTO) throws EOTException {
		 List<Commission> commissionList = null;
		for(Integer c : commissionDTO.getTransactions()){
			commissionList = commissionDao.loadCommisionSlab(c.intValue(),commissionDTO.getBankId(),commissionDTO.getProfileId());
				 if(commissionList!=null ){
					 CommissionDTO commissionDTO2 = new CommissionDTO();
					
					 Commission commission = commissionList.get(0);
					 Integer [] transactionTypes = {commission.getTransactionTypeId()};
					 commissionDTO2.setBankId(commission.getBankId());
					 commissionDTO2.setProfileId(commission.getProfileId());
					 commissionDTO2.setTransactions(transactionTypes);
					   	throw new EOTException(ErrorConstants.COMMISSION_ALREDY_EXISTS);
				 }
		}
		for(Integer c : commissionDTO.getTransactions()){
			Commission commission = new Commission();
			commission.setBankId(commissionDTO.getBankId());
			commission.setProfileId(commissionDTO.getProfileId());
			commission.setTransactionTypeId(c.intValue());
			commission.setCommissionPer(commissionDTO.getCommission());
			commissionDao.save(commission);
		}
		return commissionDTO;
	}

	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=Throwable.class )
	public void createCommissionSplits(CommisionSplitsDTO commisionSplitsDTO) throws EOTException {
		if(commisionSplitsDTO != null && CollectionUtils.isNotEmpty(commisionSplitsDTO.getCommissionSplitList())){
			for (CommisionSplitsDTO commission : commisionSplitsDTO.getCommissionSplitList()) {
				CommisionSplits commisionSplits = new CommisionSplits();
				commisionSplits.setBusinessPartner(commission.getBusinessPartner());
				TransactionType transactionType = new TransactionType();
				transactionType.setTransactionType(Integer.parseInt(commission.getTransactionType()));
				commisionSplits.setTransactionType(transactionType);
				commisionSplits.setCommisionPct(commission.getCommisionPct());
				commissionDao.save(commisionSplits);
			}
		}
		
	}

	@Override
	public List<CommisionSplitsDTO> loadCommissionSplits() throws EOTException {
		List<CommisionSplits> commissinSplits =  commissionDao.loadCommissionSplits();
		List<CommisionSplitsDTO> commissionSplitsList= new ArrayList<>();
		for (CommisionSplits commissinSplit : commissinSplits) {
			CommisionSplitsDTO commisionSplitsDTO = new CommisionSplitsDTO();
			commisionSplitsDTO.setBusinessPartner(commissinSplit.getBusinessPartner());
			commisionSplitsDTO.setTransactionType(null != commissinSplit.getTransactionType() && null != commissinSplit.getTransactionType().getTransactionType() ? commissinSplit.getTransactionType().getTransactionType().toString():null);
			commisionSplitsDTO.setCommisionPct(commissinSplit.getCommisionPct());
			commisionSplitsDTO.setCommisionSplitID(commissinSplit.getCommisionSplitID());
			commissionSplitsList.add(commisionSplitsDTO);
		}
		return commissionSplitsList;
	}

	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=Throwable.class )
	public void deleteOldCommission() throws EOTException {
		List<CommisionSplits> commissinSplits =  commissionDao.loadCommissionSplits();
		for (CommisionSplits commisionSplit : commissinSplits) {
			commissionDao.delete(commisionSplit);
		}
		
	}
	
}

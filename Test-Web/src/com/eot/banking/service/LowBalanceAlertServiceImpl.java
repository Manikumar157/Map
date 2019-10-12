package com.eot.banking.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eot.banking.dao.BusinessPartnerDao;
import com.eot.banking.dao.CustomerDao;
import com.eot.entity.SmsLog;

@Service("LowBalanceAlert")
public class LowBalanceAlertServiceImpl implements LowBalanceAlertService {
	
	@Autowired
	CustomerDao customerDAO;
	
	@Autowired
	BusinessPartnerDao businessPartnerDAO;

	@Override
	@Transactional
	public void lowBalanceAlert() {
		
		List<Object[]> agentBalanceDetails = customerDAO.lowBlanceCheckForAgent();
		for (Object[] objects : agentBalanceDetails) {
			if(Integer.parseInt(objects[5].toString())==1){
				System.out.println("Agent: "+objects[5]);
				logSmsAlert(objects[0].toString(),objects[3].toString());
				
			}
			
		}
		
		List<Object[]> bpBalanceDetails = businessPartnerDAO.lowBlanceCheckForBusinessPartner();
		for (Object[] objects : bpBalanceDetails) {
			if(objects[6].equals(1)){
				System.out.println("SuperAgent: "+objects[6]);
				logSmsAlert(objects[0].toString(),objects[4].toString());
			}
			
		}
	}
	
	private void logSmsAlert(String MobileNumber, String balance){
		SmsLog smsLog = new SmsLog();
		smsLog.setMobileNumber("211" + MobileNumber);
		smsLog.setMessageType(101);
		smsLog.setEncoding(1);
		smsLog.setCreatedDate(new Date());
		smsLog.setMessage("Your float balance is low ! Please do the needful to increase it. Float Balance is : "+balance);
		smsLog.setScheduledDate(new Date());
		smsLog.setStatus(0);
		customerDAO.save(smsLog);
	}

}

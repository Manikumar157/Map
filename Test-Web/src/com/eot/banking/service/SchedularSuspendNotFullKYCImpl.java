package com.eot.banking.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eot.banking.common.EOTConstants;
import com.eot.banking.dao.CustomerDao;
import com.eot.entity.Customer;

@Service("SuspendNotFullKYCService")
public class SchedularSuspendNotFullKYCImpl implements SchedularSuspendNotFullKYC {
	@Autowired
	CustomerDao customerDAO;

	@Override
	@Transactional
	public void suspendNotFullKYC() {
		List<Object[]> customers = customerDAO.getCustomerEnrolledUnder90DaysByKYCStatus(EOTConstants.KYC_STATUS_APPROVED);
		//System.out.println("### SuspendNotFullKYCService ### "+customers);
		if(CollectionUtils.isNotEmpty(customers)){
			for (Object [] object : customers) {
				customerDAO.updateCustomer(Long.valueOf(object[0].toString()), EOTConstants.CUSTOMER_STATUS_SUSPENDED);
			}
		}

	}

}

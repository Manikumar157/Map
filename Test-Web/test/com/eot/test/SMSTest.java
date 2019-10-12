package com.eot.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.eot.banking.service.CustomerService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
						{"file:WebContent/WEB-INF/config/*.xml"
						}
					  )
public class SMSTest {
	
	@Autowired
	private CustomerService customerService;
	
	@Test
	public void createCustomers() {
		Long customerId=0L;
		try {
			customerService.resetTxnPin(customerId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

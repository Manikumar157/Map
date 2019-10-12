package com.eot.test;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.eot.banking.exceptions.EOTException;
import com.eot.banking.service.TransactionService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
						{"file:WebContent/WEB-INF/config/*.xml"
						}
						)
public class DaoTest {
	
	@Autowired
	TransactionService txService;
	
	@Test
	public void test() {
		try {
			List<Map> result = txService.getAccountHeadByBankId("1");
			System.out.println(result);
		} catch (EOTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

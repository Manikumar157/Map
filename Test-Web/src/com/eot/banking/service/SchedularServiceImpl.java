package com.eot.banking.service;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eot.banking.dao.SchedularDAO;
import com.eot.entity.PendingTransaction;

@Service("schedularService")
public class SchedularServiceImpl implements SchedularService {

	@Autowired
	private SchedularDAO schedularDao;
	
	@Override
	public void pendingTxnCheck() {
		//System.out.println("withdrawal and sale pending transaction checkin.....");
		Calendar cal = Calendar.getInstance();
		Date currentDateTime = cal.getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		List<PendingTransaction> pendingTxnList=schedularDao.getUnprocessedPendingTxn();
		String pendingTransactionIds="";
		if(!pendingTxnList.isEmpty())
		{
			for(PendingTransaction pendingTxn:pendingTxnList)
			{
				Date date= pendingTxn.getTransactionDate();
				//pendingTransactionIds=pendingTxn.getTransactionId()+"";
				System.out.println("Date time 1:: "+format.format(date));
				System.out.println("Date time 2:: "+format.format(currentDateTime));
				LocalDateTime dateTime1= LocalDateTime.parse(format.format(date), formatter);
				LocalDateTime dateTime2= LocalDateTime.parse(format.format(currentDateTime), formatter);
				long diffInMinutes = java.time.Duration.between(dateTime1, dateTime2).toMinutes();
				System.out.println("Time Difference:::"+diffInMinutes);
				if(diffInMinutes>=15)
				{
					pendingTxn.setStatus(104);//status 102 is for expire
					//schedularDao.update(pendingTxn);
					//schedularDao.saveOrUpdate(pendingTxn);
					pendingTransactionIds=pendingTransactionIds+pendingTxn.getTransactionId()+",";
					//pendingTransactionIds=pendingTransactionIds+pendingTxn.getTransactionId()+"";
					//schedularDao.updatePendingTxnByIds(pendingTransactionIds);
				}
			}
			if(!pendingTransactionIds.equals("")) {
				schedularDao.updatePendingTxnByIds(pendingTransactionIds.substring(0, pendingTransactionIds.length()-1));
			}
		}
		

	}


}

package com.eot.banking.utils;

import java.util.ArrayList;
import java.util.List;

import com.eot.dtos.basic.Transaction;

public class TableUtilPdf {

	public String[] initializeHeader() {
		String[] headers = new String[5];
		headers[0] = "SLNO";
		headers[1] = "AMOUNT";
		headers[2] = "TRANSACTION TYPE";
		headers[3]="TRANSACTION DATE";
		headers[4]=" DESCRIPTION";
		
		return headers;
	}

	public List<Object[]> createObjectListFromList(List<Transaction> txnlist){
		Object[] row = null;  
		List<Object[]> obList=new ArrayList<Object[]>();
		try {

			int slno = 0;
			for(int i=0;i<txnlist.size();i++){
				row=new Object[5];
				Transaction transaction=(Transaction) txnlist.get(i);
				row[0]=++slno;
				row[1]=transaction.getAmount();
				row[2]=transaction.getTransType();
				row[3]=DateUtil.formatDateAndTime(transaction.getTransDate().getTime());
				row[4]=transaction.getTransDesc();
				obList.add(row);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obList;
	}	

}

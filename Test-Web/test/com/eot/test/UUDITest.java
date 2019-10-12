package com.eot.test;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOUtil;

import com.eot.banking.utils.EOTUtil;


public class UUDITest {

	public static void main(String[] args) {


		/*for(int i = 0 ; i < 1000000 ; i++){
			
			System.out.println(EOTUtil.generateLoginPin()+"-"+EOTUtil.generateTransactionPin()+"-"+
					EOTUtil.generateAppID()+"-"+EOTUtil.generateUUID());
		}*/
		
		/*try {
			System.out.println(ISOUtil.zeropad("1", 9));
		} catch (ISOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		Integer integer =new Integer(1);
		Integer inti =new Integer(1);
		System.out.println(integer.equals(inti));
		
		String label = Label(30);
		System.out.println(label);

	}
	
	public static String Label(int x){
		String label = null;
		switch (x) {
		case 30:
			label = "Balance Enquiry";
			break;
		case 126:
			label = "mGurush Pay";
			break;

		default:
			label="NA";
			break;
		}
		return label;
	}

}

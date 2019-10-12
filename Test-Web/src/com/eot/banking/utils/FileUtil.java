package com.eot.banking.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.eot.banking.dto.BranchDTO;
import com.eot.banking.dto.OperatorDTO;
import com.eot.banking.dto.ReversalDTO;
import com.eot.banking.dto.TransactionParamDTO;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.exceptions.EOTException;

public class FileUtil {
	
	

	public static List<BranchDTO> parseBranchFile(CommonsMultipartFile fileName) throws EOTException{

		List<BranchDTO> branchList = new ArrayList<BranchDTO>();

		if(fileName.getSize()==0){
			return branchList;
		}


		byte dataBytes[] = null;
		try {
			InputStream in = fileName.getInputStream();
			Long size = fileName.getSize();
			int formDataLength = Integer.valueOf(size.intValue());
			dataBytes = new byte[formDataLength];
			int byteRead = 0;
			int totalBytesRead = 0;
			while (totalBytesRead < formDataLength){
				byteRead = in.read(dataBytes, totalBytesRead, formDataLength);
				totalBytesRead += byteRead;
			}
			String[] rowArray = new String(dataBytes).split("\n") ;

			for( String tmp : rowArray ){

				String[] cols = tmp.split("\\,");

				BranchDTO dto = new BranchDTO();

				dto.setSerialNum(cols[0]);
				dto.setCity(cols[1]);
				dto.setQuarter(cols[2]);
				dto.setLocation(cols[3]);
				dto.setAddress(cols[4]);
				dto.setDescription(cols[5]);
				dto.setBranchCode(cols[6].trim());
				branchList.add(dto);

			}

			branchList.remove(0);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EOTException(ErrorConstants.INVALID_BRANCH_FILE);
		}

		return branchList;

	}
	public static List<OperatorDTO> parseDenominationFile(CommonsMultipartFile fileName) throws EOTException{

		List<OperatorDTO> denominationList = new ArrayList<OperatorDTO>();

		if(fileName.getSize()==0){
			return denominationList;
		}


		byte dataBytes[] = null;
		try {
			InputStream in = fileName.getInputStream();
			Long size = fileName.getSize();
			int formDataLength = Integer.valueOf(size.intValue());
			dataBytes = new byte[formDataLength];
			int byteRead = 0;
			int totalBytesRead = 0;
			while (totalBytesRead < formDataLength){
				byteRead = in.read(dataBytes, totalBytesRead, formDataLength);
				totalBytesRead += byteRead;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		String[] rowArray = new String(dataBytes).split("\n") ;

		for( String tmp : rowArray ){
			String[] cols = tmp.split("\\,");
			if(cols.length !=4){
				throw new EOTException(ErrorConstants.INVALID_VOUCHER_FILE);
			}
			OperatorDTO dto = new OperatorDTO();

			dto.setSerialNum(cols[0]);
			dto.setDenomination(cols[1].trim());
			dto.setVouchurSlNum(cols[2].trim());
			dto.setVoucherNum(cols[3].trim());

			denominationList.add(dto);

		}

		denominationList.remove(0);

		return denominationList;

	}
	public static List<ReversalDTO> parseReversalFile(CommonsMultipartFile reversalFile) {

		List<ReversalDTO> revList=new ArrayList<ReversalDTO>();


		if(reversalFile.getSize()==0){			
			return revList;			
		}

		byte dataBytes[] = null;
		try {
			InputStream in = reversalFile.getInputStream();
			Long size = reversalFile.getSize();
			int formDataLength = Integer.valueOf(size.intValue());
			dataBytes = new byte[formDataLength];
			int byteRead = 0;
			int totalBytesRead = 0;
			while (totalBytesRead < formDataLength){
				byteRead = in.read(dataBytes, totalBytesRead, formDataLength);
				totalBytesRead += byteRead;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		String[] rowArray = new String(dataBytes).split("\n") ;
		for( String tmp : rowArray ){
			String[] cols = tmp.split("\\,");

			ReversalDTO reversalDTO = new ReversalDTO();

			reversalDTO.setTransactionId(cols[0]);
			reversalDTO.setAdjustedAmount(cols[1]);
			reversalDTO.setAdjustedFee(cols[2]);
			reversalDTO.setReversedTxnType(cols[3]);
			reversalDTO.setComment(cols[4]);	

			revList.add(reversalDTO);


		}
		revList.remove(0);
		return revList;
	}
	
	public static List<TransactionParamDTO> parseTransactionFile(CommonsMultipartFile txnFile) {

		List<TransactionParamDTO> txnList=new ArrayList<TransactionParamDTO>();


		if(txnFile.getSize()==0){			
			return txnList;			
		}

		byte dataBytes[] = null;
		try {
			InputStream in = txnFile.getInputStream();
			Long size = txnFile.getSize();
			int formDataLength = Integer.valueOf(size.intValue());
			dataBytes = new byte[formDataLength];
			int byteRead = 0;
			int totalBytesRead = 0;
			while (totalBytesRead < formDataLength){
				byteRead = in.read(dataBytes, totalBytesRead, formDataLength);
				totalBytesRead += byteRead;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		String[] tempArray = new String(dataBytes).split("\n") ;
		String[] rowArray = Arrays.copyOfRange(tempArray, 1, tempArray.length);
		for( String tmp : rowArray ){
			String[] cols = tmp.split("\\,");

			TransactionParamDTO transactionParamDTO = new TransactionParamDTO();
			String mobileNumber=null;
			String isdCode=null;
			if (cols[1] == null || cols[1].equals("")) {
				mobileNumber="";
			}else if(cols[1].length()<9){
				mobileNumber="";
			}else{
				isdCode = cols[1].trim().substring(1, 4);
				mobileNumber = cols[1].trim().substring(cols[1].length()-9, cols[1].length());
			}
			
			transactionParamDTO.setMobileNumber(isdCode+mobileNumber);
			//transactionParamDTO.setAccountAlias(cols[2]);
			transactionParamDTO.setCustomerName(cols[2]);
			String amount = cols[3].trim();
			transactionParamDTO.setAmount(amount == null || amount.equals("") || !Pattern.matches("[0-9]*", amount) ? 0L :Long.parseLong(amount));
			transactionParamDTO.setDescription(cols[4]);

			txnList.add(transactionParamDTO);


		}
		return txnList;
	}
	
	public static void validateUploadTrnsactionFile(CommonsMultipartFile fileName) throws EOTException{

		BufferedReader buffer = null;
	    String line = "";
	    String cvsSplitBy = ",";

	    try {
		 buffer = new BufferedReader(new InputStreamReader(fileName.getInputStream()));
		 line = buffer.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 String[] cols = line.split(cvsSplitBy); 
		if(cols.length !=5){
			throw new EOTException(ErrorConstants.INVALID_VOUCHER_FILE);
		}else if(!cols[0].equalsIgnoreCase("Sl No")) {
			throw new EOTException(ErrorConstants.INVALID_COLUMN_Sl_No);
		}else if(!cols[1].equalsIgnoreCase("Mobile Number")) {
			throw new EOTException(ErrorConstants.INVALID_COLUMN_Mobile_Number);
		}/*else if(!cols[2].equalsIgnoreCase("Account Alias")) {
			throw new EOTException(ErrorConstants.INVALID_COLUMN_Account_Alias);
		}*/else if(!cols[2].equalsIgnoreCase("Name")) {
			throw new EOTException(ErrorConstants.INVALID_COLUMN_Name);
		}else if(!cols[3].equalsIgnoreCase("Amount")) {
			throw new EOTException(ErrorConstants.INVALID_COLUMN_Amount);
		}else if(!cols[4].equalsIgnoreCase("Description")) {
			throw new EOTException(ErrorConstants.INVALID_COLUMN_Description);
		}
		return ;
	}
}

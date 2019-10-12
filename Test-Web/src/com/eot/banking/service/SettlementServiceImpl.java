package com.eot.banking.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.jpos.iso.ISOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eot.banking.common.AppConfigurations;
import com.eot.banking.common.EOTConstants;
import com.eot.banking.dao.AccountHeadMappingDao;
import com.eot.banking.dao.BankDao;
import com.eot.banking.dao.SettlementDao;
import com.eot.banking.dto.ClearingHouseDTO;
import com.eot.banking.dto.SettlementDto;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.utils.DateUtil;
import com.eot.entity.Bank;
import com.eot.entity.BankTellers;
import com.eot.entity.ClearingHousePool;
import com.eot.entity.SettlementJournal;
import com.eot.entity.SettlementLog;

@Service("settleMentService")
public class SettlementServiceImpl implements SettlementService{

	@Autowired
	private AppConfigurations appConfig;
	@Autowired
	private SettlementDao settlementDao;
	@Autowired
	private AccountHeadMappingDao accountHeadMappingDao;
	@Autowired
	private BankDao bankDao;
	@Autowired
	public MessageSource messageSource ;

	@Override
	public ClearingHouseDTO viewClearingHouseDetails(int poolId) throws EOTException{
		ClearingHousePool chp = bankDao.getClearingHouseDetails(poolId);

		if(chp == null){
			throw new EOTException(ErrorConstants.INVALID_CLEARING_HOUSE);
		}

		ClearingHouseDTO dto = new ClearingHouseDTO();
		dto.setClearingPoolId(chp.getClearingPoolId());
		dto.setClearingHouseName(chp.getClearingPoolName());
		dto.setCurrency(chp.getCurrency().getCurrencyId());
		dto.setDescription(chp.getDescription());
		dto.setFileFormat(chp.getOutputFileNameFormat());
		dto.setStatus(chp.getStatus());
		dto.setSettlementAccount(chp.getSettlementAccount());
		dto.setEmailID(chp.getEmailID());
		dto.setGuaranteeAccount(chp.getGuaranteeAccount());
		dto.setEOTSwiftCode(chp.getEOTSwiftCode());
		dto.setMobileNumber(chp.getMobileNumber());
		dto.setContactPerson(chp.getContactPerson());
		dto.setCentralBankAccount(chp.getCentralBankAccount());
		dto.setMessageType(chp.getMessageType());
		return dto;
	}
	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=Throwable.class )
	public String createsettlementFileForPool(
			Integer  clearingHousePoolId,String settlementDate,ClearingHouseDTO clearingHouseDTO,String userName,HttpServletResponse response) throws EOTException{

		List<SettlementJournal> settlementJournals=new ArrayList<SettlementJournal>();
		String fileData=new String();
		try {
			ClearingHousePool clearingHousePool=null;
			Date sDate=DateUtil.stringToDate(settlementDate);
			//Date tDate=DateUtil.stringToDate(toDate);
			/*SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	        Date dateStr = formatter.parse(fDate);*/

			settlementJournals = settlementDao.getUnSettledEntries( clearingHousePoolId,sDate );

			Hashtable<String, SettlementDto> accountsAndSettlementDataList = new Hashtable<String, SettlementDto>();
			Hashtable<String, Double> partyAndAmountList = new Hashtable<String, Double>();

			if(settlementJournals.size()==0){
				throw new EOTException( ErrorConstants.SETTLEMENT_JOURNAL_NOT_EXIST);
			}else{
				for (int i=0;i<settlementJournals.size();i++) {
					clearingHousePool=accountHeadMappingDao.getguaranteeAccountNumber(clearingHousePoolId);
					SettlementDto settlementData = getSettlmentData( settlementJournals.get(i).getDebitAccount().getAccountNumber(),
							settlementJournals.get(i).getCreditAccount().getAccountNumber(),clearingHousePool.getSettlementAccount(), accountsAndSettlementDataList,clearingHousePoolId);

					if(clearingHouseDTO.getBanks().length>0 && i<clearingHouseDTO.getBanks().length){
						Bank bank = bankDao.getBank(clearingHouseDTO.getBanks()[i]);

						if(bank != null){

							if(settlementData.getSettlementParty().equalsIgnoreCase(bank.getSwiftCode().trim())){

								settlementData.setSettlementParty(clearingHousePool.getGuaranteeAccount());
							}

						}
					}
					Double amount = partyAndAmountList.get( settlementData.getSettlementParty() );
					amount = amount == null ? 0 : amount;
					if( settlementData.isCredit() ) {

						amount += settlementJournals.get(i).getAmount();
					}
					else {

						amount -= settlementJournals.get(i).getAmount();
					}
					partyAndAmountList.put( settlementData.getSettlementParty(), amount );
				}
				for (String party : partyAndAmountList.keySet()) {

					System.out.println("Party: " + party + "; Amount: " + partyAndAmountList.get( party ));
				}

				fileData=generateSettlementFile(clearingHousePool, new Date(), partyAndAmountList,response);
				String fileName= "MT"+clearingHousePool.getMessageType()+"_"+ clearingHousePool.getClearingPoolName() + "_" + longDate.format(new Date()) + "_" +  ".xml";
				SettlementLog settlementLog=new SettlementLog();
				settlementLog.setEntryTime(new Date());
				settlementLog.setFileName(fileName);
				settlementLog.setPoolId(clearingHousePool.getClearingPoolId());
				settlementLog.setUser(userName);
				settlementLog.setStatus(1);
				settlementDao.save(settlementLog);
			}
		}
		catch (EOTException e) {

			throw e;
		}
		catch (Exception e) {
			e.printStackTrace();
			//throw new EOTException( "");
		}
		return fileData;
	}

	@Override
	public List<Bank> getBanksByPoolId(int poolId) {

		List<Bank> banks=settlementDao.getBanksByPoolId(poolId);
		return banks;
	}

	private SettlementDto getSettlmentData( String fromAccountNo, String toAccountNo,String settlementAccount, Hashtable<String, SettlementDto>accountsAndSettlementDataList,Integer clearingHousePoolId) {

		String key = fromAccountNo + "-" + toAccountNo;
		SettlementDto settlementData = accountsAndSettlementDataList.get( key );

		if( settlementData != null ) {

			return settlementData;
		}
		SettlementDto oppSettlementData = accountsAndSettlementDataList.get( toAccountNo + "-" + fromAccountNo );
		if( oppSettlementData != null ) {

			settlementData = new SettlementDto();
			settlementData.setFromAccountNo( fromAccountNo );
			settlementData.setToAccountNo(toAccountNo);
			settlementData.setFromAccountHeadId(oppSettlementData.getToAccountHeadId());
			settlementData.setToAccountHeadId(oppSettlementData.getFromAccountHeadId());
			settlementData.setSettlementParty(oppSettlementData.getSettlementParty());
			settlementData.setCredit(!oppSettlementData.isCredit());

			accountsAndSettlementDataList.put( key, settlementData );
			return settlementData;
		}
		settlementData = accountHeadMappingDao.getAccountHeadIDsForAccounts( fromAccountNo, toAccountNo );

		settlementData.setSettlementParty( settlementData.getSettlementParty() == null ? settlementAccount : settlementData.getSettlementParty() );

		settlementData.setCredit( settlementData.getToAccountHeadId() == 58 || 
				settlementData.getToAccountHeadId() == 104);
		accountsAndSettlementDataList.put(key, settlementData);

		return settlementData;

	}

	private String generateSettlementFile( ClearingHousePool clearingHousePool, 
			Date settlementDate, Hashtable<String, Double> partyAndAmountList ,HttpServletResponse response) throws Exception {
		String fileName=null;
		StringBuffer buffer=null;
		try {

			int sequence = getSequence(clearingHousePool, settlementDate);
			String referenceNo = "971" + clearingHousePool.getEOTSwiftCode()+ shortDate.format(settlementDate)+ padSequence(sequence, 3);
			fileName = getFileName( clearingHousePool, settlementDate, sequence );
			buffer = new StringBuffer("<?xml version=\"1.0\" encoding=\"latin1\" ?>");
			appendLineToBuffer(buffer, "<SWIFT_msg_fields>");
			appendLineToBuffer(buffer, "<msg_format>S</msg_format>");
			appendLineToBuffer(buffer, "<msg_sub_format>I</msg_sub_format>");
			appendLineToBuffer(buffer, "<msg_sender>"+clearingHousePool.getMsgSender()+"</msg_sender>");
			appendLineToBuffer(buffer, "<msg_receiver>"+clearingHousePool.getCentralBankAccount()+"</msg_receiver>");
			appendLineToBuffer(buffer, "<msg_type>971</msg_type>");
			appendLineToBuffer(buffer, "<msg_priority />");
			appendLineToBuffer(buffer, "<msg_del_notif_rq />");
			appendLineToBuffer(buffer, "<msg_user_priority>0030</msg_user_priority>");
			appendLineToBuffer(buffer, "<msg_user_reference>" + referenceNo + "</msg_user_reference>");
			appendLineToBuffer(buffer, "<msg_copy_srv_id />");
			appendLineToBuffer(buffer, "<msg_fin_validation />");
			appendLineToBuffer(buffer, "<msg_pde>N</msg_pde>");
			appendLineToBuffer(buffer, "<block4>:20:" + referenceNo);
			for (String party : partyAndAmountList.keySet()) {

				Double amt = partyAndAmountList.get( party );
				if( amt == 0 ) {

					continue;
				}
				appendLineToBuffer(buffer, ":25:" + party);
				String amtEntry = (amt < 0 ? "D" : "C") + shortDate.format( settlementDate ) + "XOF" + Math.round(Math.abs( amt )) + ",";
				appendLineToBuffer(buffer, ":62F:" + amtEntry);
			}
			buffer.append("</block4>");
			appendLineToBuffer(buffer, "</SWIFT_msg_fields>");
			System.out.println(buffer.toString());
			/*File file = new File(fileName );

			boolean fileCreated = file.createNewFile();
			if( !fileCreated ) {

				throw new Exception( );
			}

			response.setContentType("application/xml");
			response.setHeader("Content-Disposition", "attachment; filename="+file);
			//OutputStream os = response.getOutputStream();

			FileOutputStream outputStream = new FileOutputStream( file );
			outputStream.write(buffer.toString().getBytes());
			outputStream.close();*/

		}
		catch (Exception e) {

			throw e;
		}
		return buffer.toString();

	}
	private int getSequence(ClearingHousePool clearingHousePool, 
			Date settlementDate) throws Exception {

		for( int i = 1; i < 100; i++ ) {

			String fileName =  appConfig.getSettlementFileLocation() + getFileName(clearingHousePool, settlementDate, i)  ;
			File file =  new File( fileName );
			if( !file.exists() ) {

				return i;
			}
		}
		throw new Exception( );
	}
	private SimpleDateFormat shortDate = new SimpleDateFormat("yyMMdd");
	private SimpleDateFormat longDate = new SimpleDateFormat("yyyyMMdd");
	private String getFileName(ClearingHousePool clearingHousePool, 
			Date settlementDate, int sequence ) {

		return "MT971_"+ clearingHousePool.getClearingPoolName() + "_" + longDate.format(settlementDate) + "_" + padSequence(sequence, 2) + ".xml";
	}

	private void appendLineToBuffer( StringBuffer buffer, String line ) {

		System.setProperty( "line.separator", "" + "\r\n" );

		buffer.append("\r\n" + line);
	}

	private String padSequence( int sequence, int noOfDigits ) {

		String ret = sequence+1 + "";
		while( ret.length() < noOfDigits ) {

			ret = "0" + ret;
		}
		return ret;
	}

	@Override
	public List<ClearingHousePool> getBankIdByUserName(String userName) throws EOTException{

		BankTellers teller = bankDao.getTellerByUsername(userName);	
		if(teller == null){
			throw new EOTException(ErrorConstants.INVALID_TELLER);
		}
		List<ClearingHousePool> clearingHousePools=settlementDao.getClearingHousesByBankId(teller.getBank().getBankId());

		if(clearingHousePools==null){
			throw new EOTException(null);
		}

		return clearingHousePools;
	}

	@Override
	public Bank getBankName(String userName) throws EOTException {


		return settlementDao.getBankName(userName);
	}

	@Override
	public String generateCSVFile(Integer poolId,String userName,Locale locale,ClearingHouseDTO clearingHouseDTO)throws EOTException{

		BankTellers teller = bankDao.getTellerByUsername(userName);				
		if(teller == null){
			throw new EOTException(ErrorConstants.INVALID_TELLER);
		}
		List list=settlementDao.generateCSVFile(poolId, teller.getBank().getBankId(),clearingHouseDTO);
		if(list.size()==0){
			throw new EOTException(ErrorConstants.SETTLEMENT_DATA_NOT_AVAILABLE);
		}
		StringBuffer writer =  new StringBuffer();
		/*Murari, 10/07/2018, Adding header to the CSV file --- start---*/
		writer.append("GIM");
		writer.append(System.getProperty("line.separator"));
		writer.append("SETTLEMENT_FILE");
		writer.append(System.getProperty("line.separator"));
		/*---end---*/
		// Vineeth, 25/06/2018, CSV file column is separated with comma(,) but not with semi-colon(;) 
		writer.append(messageSource.getMessage("LABEL_CURRENT_VERSION",null,locale));
		//writer.append(';');
		writer.append(',');
		writer.append(messageSource.getMessage("LABEL_UNIQUE_REF",null,locale));
	//	writer.append(';');
		writer.append(',');
		writer.append(messageSource.getMessage("LABEL_TXN_REF",null,locale));
	//	writer.append(';');
		writer.append(',');
		writer.append(messageSource.getMessage("LABEL_NETWORK",null,locale));
	//	writer.append(';');
		writer.append(',');
		/*Murari, 12/07/2018, As discussed with Sudhanshu Adding CUSTOMER NAME--- start---*/
		writer.append(messageSource.getMessage("LABEL_CUSTOMER_ID",null,locale));
	//	writer.append(';');
		writer.append(',');
		writer.append(messageSource.getMessage("LABEL_CUSTOMER_NAME",null,locale));
		//	writer.append(';');
			writer.append(',');
			/*---end---*/
		writer.append(messageSource.getMessage("LABEL_ACC_TYPE",null,locale));
	//	writer.append(';');
		writer.append(',');
		writer.append(messageSource.getMessage("LABEL_STAN",null,locale));
	//	writer.append(';');
		writer.append(',');
		writer.append(messageSource.getMessage("LABEL_COUNTRY_CODE",null,locale));
	//	writer.append(';');
		writer.append(',');
		writer.append(messageSource.getMessage("LABEL_MOBILE_NUM",null,locale));
	//	writer.append(';');
		writer.append(',');
		writer.append( messageSource.getMessage("LABEL_ISSUER_BANKCODE",null,locale) );
	//	writer.append(';');
		writer.append(',');
		writer.append(messageSource.getMessage("LABEL_ISSURE_BRANCHCODE",null,locale));
	//	writer.append(';');
		writer.append(',');
		writer.append(messageSource.getMessage("LABEL_BENF_COUNTRY_CODE",null,locale));
	//	writer.append(';');
		writer.append(',');
		writer.append(messageSource.getMessage("LABEL_BENF_MOBILE_NUM",null,locale));
	//	writer.append(';');
		writer.append(',');
		writer.append( messageSource.getMessage("LABEL_BENF_BANKCODE",null,locale) );
	//	writer.append(';');
		writer.append(',');
		writer.append(messageSource.getMessage("LABEL_BENF_BRANCHCODE",null,locale));
	//	writer.append(';');
		writer.append(',');
		writer.append(messageSource.getMessage("LABEL_ACQ_BANKCODE",null,locale));
	//	writer.append(';');
		writer.append(',');
		writer.append(messageSource.getMessage("LABEL_ACQ_BRANCHCODE",null,locale));
	//	writer.append(';');
		writer.append(',');
		writer.append(messageSource.getMessage("LABEL_POS_COUNTRY_CODE",null,locale));
	//	writer.append(';');
		writer.append(',');
		writer.append(messageSource.getMessage("LABEL_POS_TYPE",null,locale));
	//	writer.append(';');
		writer.append(',');
		writer.append( messageSource.getMessage("LABEL_POS_DESC",null,locale) );
	//	writer.append(';');
		writer.append(',');
		writer.append(messageSource.getMessage("LABEL_POS_ID",null,locale));
	//	writer.append(';');
		writer.append(',');
		writer.append(messageSource.getMessage("LABEL_POS_DESGN",null,locale));
	//	writer.append(';');
		writer.append(',');
		writer.append(messageSource.getMessage("LABEL_TRANSACTION_TYPE",null,locale) );
	//	writer.append(';');
		writer.append(',');
		/*Murari, 10/07/2018, As discussed with Sudhanshu only TRANSACTION_TYPE is required so commenting  LABEL_TRANSACTION_TYPE_DESG--- start---*/
	/*	writer.append(messageSource.getMessage("LABEL_TRANSACTION_TYPE_DESG",null,locale) );
	//	writer.append(';');
		writer.append(',');
		----End-----*/
		writer.append(messageSource.getMessage("LABEL_TRANSACTION_ISO_CUR_CODE",null,locale) );
	//	writer.append(';');	
		writer.append(',');
		writer.append(messageSource.getMessage("LABEL_AMOUNT",null,locale));
	//	writer.append(';');
		writer.append(',');
		/*writer.append(messageSource.getMessage("LABEL_TXN_SIGN",null,locale));
		writer.append(';');		*/
		writer.append( messageSource.getMessage("LABEL_TXN_DATE",null,locale));
	//	writer.append(';');	
		writer.append(',');
		writer.append(messageSource.getMessage("LABEL_SERVICE_CHARGE",null,locale));
	//	writer.append(';');
		writer.append(',');
		writer.append(messageSource.getMessage("LABEL_TAX",null,locale));
	//	writer.append(';');
		writer.append(',');
		writer.append(messageSource.getMessage("LABEL_STAMP_FEE",null,locale));
	//	writer.append(';');	
		writer.append(',');
		writer.append(messageSource.getMessage("LABEL_BANK_SHARE",null,locale) );
	//	writer.append(';');
		writer.append(',');
		writer.append(messageSource.getMessage("LABEL_GIM_SHARE",null,locale));
	//	writer.append(';');
		writer.append(',');
		writer.append(messageSource.getMessage("LABEL_GIM_THIRD_PARTY_ID",null,locale) );	
		writer.append('\n');

		String fileRef=UUID.randomUUID().toString().replaceAll("-", "").substring(0,20);
		for(int i=0;i<list.size();i++){
			Object[] obj=(Object[])list.get(i);
			/*Murari, 13/07/2018, As discussed with Sudhanshu Adding CUSTOMER NAME at index 2, so shifting all the index value after 2 to next index--- start---*/
			//FOR CURRENT VERSION
			writer.append("010");
	//		writer.append(';');
			writer.append(',');

			//FOR FILE REFERENCE
			writer.append(fileRef);			
	//		writer.append(';');
			writer.append(',');

			//FOR TXN REF
			if(obj[0] !=null){
				writer.append(obj[0]);
			//	writer.append(';');
				writer.append(',');
			}else{
			//	writer.append(';');
				writer.append(',');
			}

			// FOR NETWORK
			if(obj[20] !=null){

				if(obj[20].equals(obj[6])){
					writer.append("00");
		//			writer.append(';');
					writer.append(',');
				}else{
					writer.append("01");
		//			writer.append(';');
					writer.append(',');
				}


			}else{
				writer.append("00");
		//		writer.append(';');
				writer.append(',');
			}
			
			
			//FOR CUSTOMER ID
			if(obj[1] !=null){
				try{
					writer.append(obj[1]);
			//		writer.append(';');
					writer.append(',');
				}catch (Exception e) {

				}
			}else{
		//		writer.append(';');
				writer.append(',');
			}			
			
			/*Murari, 12/07/2018, As discussed with Sudhanshu Adding CUSTOMER NAME--- start---*/
			
			if(obj[2] !=null){
				try{
					writer.append(obj[2]);
			//		writer.append(';');
					writer.append(',');
				}catch (Exception e) {

				}
			}else{
		//		writer.append(';');
				writer.append(',');
			}
			/*----end-----12/07/2018-----*/
			//FOR CUSTOMER NAME
			/*Murari, 13/07/2018, As discussed with Sudhanshu removing integer value from  CUSTOMER A/C TYPE--- start---*/
			//FOR CUSTOMER A/C TYPE
			if(obj[3] !=null){
				if(obj[3].equals(1)){
					writer.append(EOTConstants.ACC_TYPE_PERSONAL);
				}
				if(obj[3].equals(2)){
					writer.append(EOTConstants.ACC_TYPE_NOMINAL);
				}
				if(obj[3].equals(3)){
					writer.append(EOTConstants.ACC_TYPE_REAL);
				}
		//		writer.append(';');
				writer.append(',');
			}else{
		//		writer.append(';');
				writer.append(',');
			}
			/*----end-----13/07/2018-----*/
			//FOR STAN
		//	writer.append(';');
			writer.append(',');

			//FOR COUNTRY ISD CODE-CUSTOMER
			if(obj[4] !=null){
				writer.append(obj[4]);
		//		writer.append(';');
				writer.append(',');
			}else{
		//		writer.append(';');
				writer.append(',');
			}

			//FOR MOB NUM
			if(obj[5] !=null){				
				writer.append(obj[5]);
	//			writer.append(';');
				writer.append(',');
			}else{
	//			writer.append(';');
				writer.append(',');
			}

			//FOR BANK CODE			
			if(obj[6] !=null){
				writer.append(obj[6]);
	//			writer.append(';');
				writer.append(',');
			}else{
	//			writer.append(';');
				writer.append(',');
			}

			//FOR BRANCH CODE
			if(obj[7] !=null){
				writer.append(obj[7]);
	//			writer.append(';');
				writer.append(',');
			}else{
	//			writer.append(';');
				writer.append(',');
			}

			//FOR COUNTRY ISD CODE-BENIFICIARY
			if(obj[19] !=null){
				writer.append(obj[19]);
	//			writer.append(';');
				writer.append(',');
			}else{
	//			writer.append(';');
				writer.append(',');
			}

			//FOR MOBILE NUMBER			
			if(obj[18] !=null){
				writer.append(obj[18]);
	//			writer.append(';');
				writer.append(',');
			}else if(obj[22] !=null){
				writer.append(obj[22]);
	//			writer.append(';');
				writer.append(',');
			}else{
	//			writer.append(';');
				writer.append(',');
			}

			//FOR BANK CODE
			if(obj[16] !=null){
				writer.append(obj[16]);
	//			writer.append(';');
				writer.append(',');
			}else{
	//			writer.append(';');
				writer.append(',');

			}

			//FOR BRANCH CODE
			if(obj[17] !=null){
				writer.append(obj[17]);
	//			writer.append(';');
				writer.append(',');
			}else{
	//			writer.append(';');
				writer.append(',');
			}	

			//FOR ACQUIRER BANK CODE		
			if(obj[20] !=null){
				writer.append(obj[20]);
	//			writer.append(';');
				writer.append(',');
			}else{
				writer.append(obj[6]);
	//			writer.append(';');
				writer.append(',');
			}	

			//FOR AQCUIRER BRANCH CODE
			if(obj[21] !=null){
				writer.append(obj[21]);
	//			writer.append(';');
				writer.append(',');
			}else{
				writer.append(obj[7]);
	//			writer.append(';');
				writer.append(',');
			}	

			//FOR POS ISO CODE
	//		writer.append(';');
			writer.append(',');

			//FOR POS TYPE
	//		writer.append(';');
			writer.append(',');

			//FOR POS DESC
	//		writer.append(';');
			writer.append(',');

			//FOR POS ID
	//		writer.append(';');
			writer.append(',');

			//FOR POS DESGN
	//		writer.append(';');
			writer.append(',');

			/*Murari, 10/07/2018, As discussed with Sudhanshu only TRANSACTION_TYPE is required so commenting  LABEL_TRANSACTION_TYPE_DESG--- start---*/
			/*//FOR TXN TYPE ID
			if(obj[8] !=null){
				writer.append(obj[8]);
	//			writer.append(';');
				writer.append(',');
			}else{
	//			writer.append(';');	
				writer.append(',');
			}  ------End---------*/

			//FOR TXN TYPE 			
			if(obj[9] !=null){
				if(obj[9].equals(EOTConstants.TXN_ID_DEPOSIT)){
					writer.append(messageSource.getMessage("Deposit",null,locale));
				}
				else if(obj[9].equals(EOTConstants.TXN_ID_WITHDRAWAL)){
					writer.append(messageSource.getMessage("Withdrawl",null,locale));
				}
				else if(obj[9].equals(EOTConstants.TXN_ID_BALANCE_ENQUIRY)){
					writer.append(messageSource.getMessage("Balance_Enquiry",null,locale));
				}
				else if(obj[9].equals(EOTConstants.TXN_ID_MINISTATEMENT)){
					writer.append(messageSource.getMessage("Mini_Statement",null,locale));
				}
				else if(obj[9].equals(EOTConstants.TXN_ID_TXNSTATEMENT)){
					writer.append(messageSource.getMessage("Txn_Statement",null,locale));
				}
				else if(obj[9].equals(EOTConstants.TXN_ID_ACTIVATION)){
					writer.append(messageSource.getMessage("Activation",null,locale));
				}
				/*Murari, 10/07/2018, Adding transaction type ADD_PAYEE--- start---*/
				else if(obj[9].equals(EOTConstants.TXN_ID_ADD_PAYEE)){
					writer.append(messageSource.getMessage("Add_Payee",null,locale));
				}
				/*end*/
				else if(obj[9].equals(EOTConstants.TXN_ID_TXNPINCHANGE)){
					writer.append(messageSource.getMessage("Txn_Pin_Change",null,locale));
				}
				else if(obj[9].equals(EOTConstants.TXN_ID_TRFDIRECT)){
					writer.append(messageSource.getMessage("TRF_DIRECT",null,locale));
				}
				else if(obj[9].equals(EOTConstants.TXN_ID_RESET_PIN)){
					writer.append(messageSource.getMessage("RESET_PIN",null,locale));
				}
				else if(obj[9].equals(EOTConstants.TXN_ID_SALE)){
					writer.append(messageSource.getMessage("SALE",null,locale));
				}
				else if(obj[9].equals(EOTConstants.TXN_ID_TOPUP)){
					writer.append(messageSource.getMessage("TOP_UP",null,locale));
				}
				else if(obj[9].equals(EOTConstants.TXN_ID_RESET_TXNPIN)){
					writer.append(messageSource.getMessage("RESET_TXN_PIN",null,locale));
				}
				else if(obj[9].equals(EOTConstants.TXN_ID_REACTIVATION)){
					writer.append(messageSource.getMessage("REACTIVATION",null,locale));
				}
				else if(obj[9].equals(EOTConstants.TXN_ID_BILLPAYMENT)){
					writer.append(messageSource.getMessage("BILL_PAYMENT",null,locale));
				}
				else if(obj[9].equals(EOTConstants.TXN_ID_SMSCASH)){
					writer.append(messageSource.getMessage("SMS_CASH",null,locale));
				}
				else if(obj[9].equals(EOTConstants.TXN_ID_ADDCARD)){
					writer.append(messageSource.getMessage("ADD_CARD",null,locale));
				}
				else if(obj[9].equals(EOTConstants.TXN_ID_CONFIRMCARD)){
					writer.append(messageSource.getMessage("CONFIRM_CARD",null,locale));
				}
				else if(obj[9].equals(EOTConstants.TXN_ID_DELETECARD)){
					writer.append(messageSource.getMessage("DELETE_CARD",null,locale));
				}
				else if(obj[9].equals(EOTConstants.TXN_ID_PAYMENTHISTORY)){
					writer.append(messageSource.getMessage("PAYMENT_HOSTORY",null,locale));
				}
				else if(obj[9].equals(EOTConstants.TXN_ID_PINCHANGE)){
					writer.append(messageSource.getMessage("PIN_CHANGE",null,locale));
				}
				else if(obj[9].equals(EOTConstants.TXN_ID_REVERSAL)){
					writer.append(messageSource.getMessage("Adjustment",null,locale));
				}
				else if(obj[9].equals(EOTConstants.TXN_ID_CANCEL)){
					writer.append(messageSource.getMessage("Void",null,locale));
				}
				else if(obj[9].equals(EOTConstants.TXN_ID_ADD_BANK_ACCOUNT)){
					writer.append(messageSource.getMessage("Add_Bank_Acc",null,locale));
				}
				else if(obj[9].equals(EOTConstants.TXN_ID_CHEQUE_STATUS)){
					writer.append(messageSource.getMessage("CHEQUE_STATUS",null,locale));
				}

	//			writer.append(';');
				writer.append(',');
			}else{
	//			writer.append(';');	
				writer.append(',');
			}
			/*--@ Author name <vinod joshi>, Date<7/19/2018>, purpose of change <Currency not coming > ,*/
			/*--@ Start*/
			//FOR TXN ISO CURRENCY CODE
			writer.append("SSP");
			/*--@ End*/
	//		writer.append(';');
			writer.append(',');

			//FOR AMOUNT
			if(obj[8] !=null){
				writer.append(obj[8]);
	//			writer.append(';');
				writer.append(',');
			}else{
	//			writer.append(';');	
				writer.append(',');
			}
			/*
			//FOR TXN SIGN
			if(obj[8].equals(EOTConstants.TXN_ID_DEPOSIT)){
				writer.append('C');
			}else{
				writer.append('D');	
			}

			writer.append(';');*/

			//FOR TXN DATE
			if(obj[10] !=null){
				writer.append(DateUtil.dateAndTime((Date)obj[10]));
				writer.append(';');
				writer.append(',');
			}else{
	//			writer.append(';');	
				writer.append(',');
			}

			//FOR SC
			if(obj[11] !=null){
				writer.append(obj[11]);
	//			writer.append(';');
				writer.append(',');
			}else{
	//			writer.append(';');	
				writer.append(',');
			}

			//FOR TAX
			if(obj[13] !=null){
				writer.append(obj[13]);
	//			writer.append(';');
				writer.append(',');
			}else{
	//			writer.append(';');	
				writer.append(',');
			}

			//FOR STAMP FEE
			if(obj[12] !=null){
				writer.append(obj[12]);
	//			writer.append(';');
				writer.append(',');
			}else{
	//			writer.append(';');	
				writer.append(',');
			}

			//FOR BANK SHARE
			if(obj[14] !=null){
				Double bankShare= (Double) obj[14];
				Double gimShare= (Double) obj[15];
				Double totalBankShare=bankShare-gimShare;	
				writer.append(totalBankShare);
	//			writer.append(';');
				writer.append(',');
			}else{
	//			writer.append(';');
				writer.append(',');
			}

			//FOR GIM SHARE
			if(obj[15] !=null){
				writer.append(obj[15]);
	//			writer.append(';');
				writer.append(',');
			}else{
	//			writer.append(';');
				writer.append(',');
			}
			
			/*----end-----13/07/2018---shifting index to next index--*/
			
			//FOR GIM THIRD PARTY ID
	//		writer.append(';');
			writer.append(',');
			writer.append('\n');

		}
		return writer.toString();
	}

	@Override
	public String generateSettlementSummaryCSVFile(Integer poolId, String userName,
			Locale locale, ClearingHouseDTO clearingHouseDTO)
					throws EOTException {
		BankTellers teller = bankDao.getTellerByUsername(userName);				
		if(teller == null){
			throw new EOTException(ErrorConstants.INVALID_TELLER);
		}
		List list=settlementDao.generateSettlementSummaryCSVFile(poolId, teller.getBank().getBankId(),clearingHouseDTO);
		if(list.size()==0){
			throw new EOTException(ErrorConstants.SETTLEMENT_DATA_NOT_AVAILABLE);
		}
		StringBuffer writer =  new StringBuffer();
		/*Murari, 10/07/2018, Adding header to the CSV file --- start---*/
		writer.append("GIM");
		writer.append(System.getProperty("line.separator"));
		writer.append("SETTLEMENT_SUMMARY_DETAILS");
		writer.append(System.getProperty("line.separator"));
		/*---end---*/
		writer.append(messageSource.getMessage("LABEL_CURRENT_VERSION",null,locale));
	// Vineeth, 25/06/2018, CSV file column is separated with comma(,) but not with semi-colon(;) 
		//	writer.append(';');
		writer.append(',');
		writer.append(messageSource.getMessage("LABEL_UNIQUE_REF",null,locale));
	//	writer.append(';');
		writer.append(',');
		writer.append(messageSource.getMessage("LABEL_NETWORK",null,locale));
	//	writer.append(';');	
		writer.append(',');
		writer.append(messageSource.getMessage("LABEL_ACC_TYPE",null,locale));
	//	writer.append(';');
		writer.append(',');
		writer.append(messageSource.getMessage("LABEL_TRANSACTION_TYPE",null,locale));
	//	writer.append(';');	
		writer.append(',');
		/*Murari, 10/07/2018, As discussed with Sudhanshu only TRANSACTION_TYPE is required so commenting  LABEL_TRANSACTION_TYPE_DESG--- start---*/
	/*	writer.append(messageSource.getMessage("LABEL_TRANSACTION_TYPE_DESG",null,locale));
	//	writer.append(';');
		writer.append(',');
		end*/
		writer.append(messageSource.getMessage("LABEL_TRANSACTION_ISO_CUR_CODE",null,locale));
	//	writer.append(';');
		writer.append(',');
		/*writer.append(messageSource.getMessage("LABEL_TXN_SIGN",null,locale));
		writer.append(';');*/
		writer.append(messageSource.getMessage("LABEL_CUM_TXN_AMOUNT",null,locale));	
	//	writer.append(';');
		writer.append(',');
		writer.append(messageSource.getMessage("LABEL_CUM_SERVICE_CHARGE",null,locale));
	//	writer.append(';');
		writer.append(',');
		writer.append(messageSource.getMessage("LABEL_CUM_TAX",null,locale));
	//	writer.append(';');
		writer.append(',');
		writer.append(messageSource.getMessage("LABEL_CUM_STAMP_FEE",null,locale));
	//	writer.append(';');	
		writer.append(',');
		writer.append(messageSource.getMessage("LABEL_CUM_BANK_SHARE",null,locale) );
	//	writer.append(';');
		writer.append(',');
		writer.append( messageSource.getMessage("LABEL_TXN_DATE",null,locale));
		
	//	writer.append(';');
		writer.append(',');
		writer.append(messageSource.getMessage("LABEL_CUM_GIM_SHARE",null,locale));
		writer.append(',');
		writer.append(',');
		writer.append('\n');	

		String fileRef=UUID.randomUUID().toString().replaceAll("-", "").substring(0,20);
		for(int i=0;i<list.size();i++){
			Object[] obj=(Object[])list.get(i);

			//FOR CURRENT VERSION
			writer.append("010");
	//		writer.append(';');
			writer.append(',');

			//FOR FILE REFERENCE
			writer.append(fileRef);			
	//		writer.append(';');
			writer.append(',');
		
			// FOR NETWORK
	//		writer.append(';');
			writer.append(',');

			//FOR CUSTOMER A/C TYPE

	//		writer.append(';');
			writer.append(',');

			/*Murari, 10/07/2018, As discussed with Sudhanshu only TRANSACTION_TYPE is required so commenting  LABEL_TRANSACTION_TYPE_DESG--- start---*/
			//FOR TXN TYPE ID
			/*if(obj[2] !=null){
				writer.append(obj[2]);
	//			writer.append(';');
				writer.append(',');
			}else{
	//			writer.append(';');
				writer.append(',');
			}
			End*/

			//FOR TXN TYPE 			
			if(obj[2] !=null){
				if(obj[2].equals(EOTConstants.TXN_ID_DEPOSIT)){
					writer.append(messageSource.getMessage("Deposit",null,locale));
				}
				else if(obj[2].equals(EOTConstants.TXN_ID_WITHDRAWAL)){
					writer.append(messageSource.getMessage("Withdrawl",null,locale));
				}
				else if(obj[2].equals(EOTConstants.TXN_ID_BALANCE_ENQUIRY)){
					writer.append(messageSource.getMessage("Balance_Enquiry",null,locale));
				}
				else if(obj[2].equals(EOTConstants.TXN_ID_MINISTATEMENT)){
					writer.append(messageSource.getMessage("Mini_Statement",null,locale));
				}
				else if(obj[2].equals(EOTConstants.TXN_ID_TXNSTATEMENT)){
					writer.append(messageSource.getMessage("Txn_Statement",null,locale));
				}
				else if(obj[2].equals(EOTConstants.TXN_ID_ACTIVATION)){
					writer.append(messageSource.getMessage("Activation",null,locale));
				}
				/*Murari, 10/07/2018, Adding transaction type ADD_PAYEE--- start---*/
				else if(obj[2].equals(EOTConstants.TXN_ID_ADD_PAYEE)){
					writer.append(messageSource.getMessage("Add_Payee",null,locale));
				}
				/*end*/
				else if(obj[2].equals(EOTConstants.TXN_ID_TXNPINCHANGE)){
					writer.append(messageSource.getMessage("Txn_Pin_Change",null,locale));
				}
				else if(obj[2].equals(EOTConstants.TXN_ID_TRFDIRECT)){
					writer.append(messageSource.getMessage("TRF_DIRECT",null,locale));
				}
				else if(obj[2].equals(EOTConstants.TXN_ID_RESET_PIN)){
					writer.append(messageSource.getMessage("RESET_PIN",null,locale));
				}
				else if(obj[2].equals(EOTConstants.TXN_ID_SALE)){
					writer.append(messageSource.getMessage("SALE",null,locale));
				}
				else if(obj[2].equals(EOTConstants.TXN_ID_TOPUP)){
					writer.append(messageSource.getMessage("TOP_UP",null,locale));
				}
				else if(obj[2].equals(EOTConstants.TXN_ID_RESET_TXNPIN)){
					writer.append(messageSource.getMessage("RESET_TXN_PIN",null,locale));
				}
				else if(obj[2].equals(EOTConstants.TXN_ID_REACTIVATION)){
					writer.append(messageSource.getMessage("REACTIVATION",null,locale));
				}
				else if(obj[2].equals(EOTConstants.TXN_ID_BILLPAYMENT)){
					writer.append(messageSource.getMessage("BILL_PAYMENT",null,locale));
				}
				else if(obj[2].equals(EOTConstants.TXN_ID_SMSCASH)){
					writer.append(messageSource.getMessage("SMS_CASH",null,locale));
				}
				else if(obj[2].equals(EOTConstants.TXN_ID_ADDCARD)){
					writer.append(messageSource.getMessage("ADD_CARD",null,locale));
				}
				else if(obj[2].equals(EOTConstants.TXN_ID_CONFIRMCARD)){
					writer.append(messageSource.getMessage("CONFIRM_CARD",null,locale));
				}
				else if(obj[2].equals(EOTConstants.TXN_ID_DELETECARD)){
					writer.append(messageSource.getMessage("DELETE_CARD",null,locale));
				}
				else if(obj[2].equals(EOTConstants.TXN_ID_PAYMENTHISTORY)){
					writer.append(messageSource.getMessage("PAYMENT_HOSTORY",null,locale));
				}
				else if(obj[2].equals(EOTConstants.TXN_ID_PINCHANGE)){
					writer.append(messageSource.getMessage("PIN_CHANGE",null,locale));
				}
				else if(obj[2].equals(EOTConstants.TXN_ID_REVERSAL)){
					writer.append(messageSource.getMessage("Adjustment",null,locale));
				}
				else if(obj[2].equals(EOTConstants.TXN_ID_CANCEL)){
					writer.append(messageSource.getMessage("Void",null,locale));
				}
				else if(obj[2].equals(EOTConstants.TXN_ID_ADD_BANK_ACCOUNT)){
					writer.append(messageSource.getMessage("Add_Bank_Acc",null,locale));
				}
				else if(obj[2].equals(EOTConstants.TXN_ID_CHEQUE_STATUS)){
					writer.append(messageSource.getMessage("CHEQUE_STATUS",null,locale));
				}

	//			writer.append(';');
				writer.append(',');
			}else{
	//			writer.append(';');	
				writer.append(',');
			}
			
			/*--@ Author name <vinod joshi>, Date<7/19/2018>, purpose of change <Currency not coming > ,*/
			/*--@ Start*/
			//FOR TXN ISO CURRENCY CODE
			writer.append("SSP");
			/*--@ End*/
	//		writer.append(';');
			writer.append(',');

			/*//FOR TXN SIGN
			writer.append(';');
			 */
			//FOR CUM TXN AMOUNT
			if(obj[1] !=null){
				writer.append(obj[1]);
	//			writer.append(';');
				writer.append(',');
			}else{
	//			writer.append(';');	
				writer.append(',');
			}

			//FOR SC
			if(obj[4] !=null){
				writer.append(obj[4]);
	//			writer.append(';');
				writer.append(',');
			}else{
	//			writer.append(';');	
				writer.append(',');
			}

			//FOR TAX
			if(obj[6] !=null){
				writer.append(obj[6]);
	//			writer.append(';');
				writer.append(',');
			}else{
	//			writer.append(';');		
				writer.append(',');
			}

			//FOR STAMP FEE
			if(obj[5] !=null){
				writer.append(obj[5]);
	//			writer.append(';');
				writer.append(',');
			}else{
	//			writer.append(';');	
				writer.append(',');
			}

			//FOR BANK SHARE
			if(obj[7] !=null){
				Double bankShare= (Double) obj[7];
				Double gimShare= (Double) obj[8];
				Double totalBankShare=bankShare-gimShare;	
				writer.append(totalBankShare);
	//			writer.append(';');
				writer.append(',');
			}else{
	//			writer.append(';');
				writer.append(',');
			}

			//FOR TXN DATE
			if(obj[9] !=null){
				writer.append(DateUtil.formatDateToStr((Date)obj[9]));
				writer.append(';');
				writer.append(',');
			}else{
	//			writer.append(';');	
				writer.append(',');
			}
			
			//FOR GIM SHARE
			if(obj[8] !=null){
				writer.append(obj[8]);
	//			writer.append(';');
				writer.append(',');
			}else{
	//			writer.append(';');
				writer.append(',');
			}

		

			writer.append('\n');

		}
		return writer.toString();
	}
	@Override
	public List getSettlementNetBalance(ClearingHouseDTO clearingHouseDTO,String userName)throws EOTException {
		BankTellers teller = bankDao.getTellerByUsername(userName);				
		if(teller == null){
			throw new EOTException(ErrorConstants.INVALID_TELLER);
		}
		List<Map<String,String>> list= settlementDao.getSettlementNetBalance(clearingHouseDTO,teller.getBank().getBankId());
		if(list.get(0).containsValue(null)){
			throw new EOTException(ErrorConstants.SETTLEMENT_DATA_NOT_AVAILABLE);
		}
		return list;
	}

	@Override
	public List getSettlementNetBalance1(ClearingHouseDTO clearingHouseDTO,String userName)throws EOTException {

		BankTellers teller = bankDao.getTellerByUsername(userName);				
		if(teller == null){
			throw new EOTException(ErrorConstants.INVALID_TELLER);
		}

		List list= settlementDao.getSettlementNetBalance1(clearingHouseDTO,teller.getBank().getBankId());

		Object[] obj=(Object[])list.get(0);

		if(list.size()==0 || list==null || obj[2]==null){
			throw new EOTException(ErrorConstants.SETTLEMENT_DATA_NOT_AVAILABLE);
		}
		return list;
	}
}

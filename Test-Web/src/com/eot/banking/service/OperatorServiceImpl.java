package com.eot.banking.service;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.GroupedStackedBarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.TextAnchor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eot.banking.common.EOTConstants;
import com.eot.banking.dao.BankDao;
import com.eot.banking.dao.LocationDao;
import com.eot.banking.dao.OperatorDao;
import com.eot.banking.dao.WebUserDao;
import com.eot.banking.dto.CardDto;
import com.eot.banking.dto.OperatorDTO;
import com.eot.banking.dto.OperatorDenominationDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.utils.EOTUtil;
import com.eot.banking.utils.FileUtil;
import com.eot.banking.utils.Page;
import com.eot.entity.Account;
import com.eot.entity.Bank;
import com.eot.entity.BankTellers;
import com.eot.entity.Country;
import com.eot.entity.CustomerCard;
import com.eot.entity.Operator;
import com.eot.entity.OperatorDenomination;
import com.eot.entity.OperatorVoucher;
import com.eot.entity.WebUser;
import com.keypoint.PngEncoder;
import com.thinkways.kms.KMS;
import com.thinkways.kms.security.KMSSecurityException;
import com.thinkways.util.HexString;

@Service("operatorService")
@Transactional(readOnly=true)
public class OperatorServiceImpl implements OperatorService{

	private KMS kmsHandle;
	
	@Autowired
	private OperatorDao operatorDao;
	@Autowired
	private BankDao bankDao;
	@Autowired
	private WebUserDao webUserDao;
	@Autowired
	private LocationDao locationDao;

	public void setKmsHandle(KMS kmsHandle) {

		this.kmsHandle = kmsHandle;
	}
	
	@Override
	public Page getOperatorList(Integer pageNumber) throws EOTException{	
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		WebUser webUser=webUserDao.getUser(auth.getName());
		if(webUser==null){
			throw new EOTException(ErrorConstants.INVALID_USER);
		}
		Page page = null;
		if(webUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_EOT_ADMIN){
			page = operatorDao.getOperatorListByPageNumber(pageNumber);
		}else{
		
		BankTellers teller = bankDao.getTellerByUsername(webUser.getUserName());
		page =operatorDao.getOperatorList(pageNumber,teller.getBank().getBankId());
		
		}
		return page;
	}

	@Override
	public Page getDenominations(Long operatorId,Integer pageNumber) {
		Page page=operatorDao.getDenominations(operatorId,pageNumber);		
		return page;
	}

	@Override
	public Page getVouchers( Long operatorId,Integer pageNumber) {
		Page page=operatorDao.getVouchers(operatorId,pageNumber);
		return page;
	}

	@Override
	public List<Country> getCountryList(String language) {

		return operatorDao.getCountries(language);
	}

	@Override
	public List<Bank> getBankList() {
		return bankDao.getActiveBanks();
	}


	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=Throwable.class )
	public void addOperator(OperatorDTO operatorDTO)throws EOTException {

		Operator operator=operatorDao.getOperatorNameByName(operatorDTO.getOperatorName(),operatorDTO.getCountryId());
		if(operator!=null){
			throw new EOTException(ErrorConstants.OPERATOR_NAME_EXIST);
		}		
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		WebUser webUser=webUserDao.getUser(auth.getName());
		if(webUser==null){
			throw new EOTException(ErrorConstants.INVALID_USER);
		}
		BankTellers teller = bankDao.getTellerByUsername(webUser.getUserName());
		
		
		operator =new Operator();
		//operator.setOperatorId(operatorDTO.getOperatorId());
		operator.setOperatorName(operatorDTO.getOperatorName());
		operator.setCommission(operatorDTO.getComission());
		operator.setActive(operatorDTO.getActive());
		operator.setCreatedDate(new Date());
		
		Country country = locationDao.getCountry(operatorDTO.getCountryId());
		/*Country country=new Country();
		country.setCountryId(operatorDTO.getCountryId());*/
		operator.setCountry(country);

		Account acc = new Account();

		acc.setAccountNumber(EOTUtil.generateAccountNumber(bankDao.getNextAccountNumberSequence()));
		acc.setAccountType(EOTConstants.ACCOUNT_TYPE_PERSONAL);
		acc.setActive(EOTConstants.ACCOUNT_STATUS_ACTIVE);
		acc.setAlias(EOTConstants.ACCOUNT_ALIAS_CUSTOMER+"-"+operator.getOperatorName());
		acc.setCurrentBalance(0.0);
		acc.setCurrentBalanceType(EOTConstants.ACCOUNT_BALANCE_TYPE_CREDIT);
		acc.setReferenceId(" ");
		acc.setReferenceType(EOTConstants.REFERENCE_TYPE_MERCHANT);
		bankDao.save(acc);

		operator.setAccount(acc);

		/*
		 * bellow code is commented by bidyut becouse now bank id loaded from login 
		 * Bank bank = new Bank();
		bank.setBankId(operatorDTO.getBankId());*/
		operator.setBank(teller.getBank());

		operatorDao.save(operator);

		acc.setReferenceId(operator.getOperatorId().toString());
		operatorDao.update(acc);
		//Naqui Fix for 6453
		operatorDao.evict(operator);
		
	}

	@Override
	public OperatorDTO getOperator(Long operatorId) throws EOTException{
		Operator operator=operatorDao.getOperator(operatorId);
		if(operator == null){
			throw new EOTException(ErrorConstants.INVALID_OPERATOR);
		}else{
			OperatorDTO operatorDTO =new OperatorDTO();
			operatorDTO.setOperatorId(operator.getOperatorId());
			operatorDTO.setOperatorName(operator.getOperatorName());
			operatorDTO.setActive(operator.getActive());
			operatorDTO.setCountryId(operator.getCountry().getCountryId());
			operatorDTO.setComission(operator.getCommission());
			operatorDTO.setBankId(operator.getBank().getBankId());
			return operatorDTO;
		}
	}
	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=Throwable.class )
	public void updateOperator(OperatorDTO operatorDTO) throws EOTException {

		Operator operator=operatorDao.getOperatorNameByName(operatorDTO.getOperatorName(),operatorDTO.getCountryId(),operatorDTO.getOperatorId());
		if(operator!=null){
			throw new EOTException(ErrorConstants.OPERATOR_NAME_EXIST);
		}		

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		WebUser webUser=webUserDao.getUser(auth.getName());
		if(webUser==null){
			throw new EOTException(ErrorConstants.INVALID_USER);
		}

		BankTellers teller = bankDao.getTellerByUsername(webUser.getUserName());		
		
		operator=operatorDao.getOperator(operatorDTO.getOperatorId());
		if(operator == null){
			throw new EOTException(ErrorConstants.INVALID_OPERATOR);
		}else{
			operator.setOperatorId(operatorDTO.getOperatorId());
			operator.setOperatorName(operatorDTO.getOperatorName());
			operator.setActive(operatorDTO.getActive());
			operator.setCommission(operatorDTO.getComission());
			/*Country country=new Country();
			country.setCountryId(operatorDTO.getCountryId());
			operator.setCountry(country);*/
			Country country = locationDao.getCountry(operatorDTO.getCountryId());
			operator.setCountry(country);

			if(operator.getBank()==null || operator.getAccount()== null){
				Account acc = new Account();

				acc.setAccountNumber(EOTUtil.generateAccountNumber(bankDao.getNextAccountNumberSequence()));
				acc.setAccountType(EOTConstants.ACCOUNT_TYPE_PERSONAL);
				acc.setActive(EOTConstants.ACCOUNT_STATUS_ACTIVE);
				acc.setAlias(EOTConstants.ACCOUNT_ALIAS_CUSTOMER+"-"+operator.getOperatorName());
				acc.setCurrentBalance(0.0);
				acc.setCurrentBalanceType(EOTConstants.ACCOUNT_BALANCE_TYPE_CREDIT);
				acc.setReferenceId(operator.getOperatorId().toString());
				acc.setReferenceType(EOTConstants.REFERENCE_TYPE_MERCHANT);
				bankDao.save(acc);

				operator.setAccount(acc);
				/*Bank bank = new Bank();
				bank.setBankId(operatorDTO.getBankId());*/
				operator.setBank(teller.getBank());

			}

			operatorDao.update(operator);
		}

	}
	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=Throwable.class )
	public void addDenomination(OperatorDenominationDTO operatorDenominationDTO) throws EOTException{

		Operator operatorStatus=operatorDao.getOperatorStatus(operatorDenominationDTO.getOperatorId());
		if(operatorStatus==null){
			throw new EOTException(ErrorConstants.OPERATOR_INACTIVE);
		}

		OperatorDenomination operatorDenomination=new OperatorDenomination();		

		operatorDenomination.setDenomination(operatorDenominationDTO.getDenomination());
		operatorDenomination.setActive(operatorDenominationDTO.getActive());

		Operator operator=new Operator();
		operator.setOperatorId(operatorDenominationDTO.getOperatorId());
		operatorDenomination.setOperator(operator);

		operatorDao.save(operatorDenomination);

	}

	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=Throwable.class )
	public void updateDenomination( OperatorDenominationDTO operatorDenominationDTO) throws EOTException {

		Operator operatorStatus=operatorDao.getOperatorStatus(operatorDenominationDTO.getOperatorId());
		if(operatorStatus==null){
			throw new EOTException(ErrorConstants.OPERATOR_INACTIVE);
		}

		OperatorDenomination operatorDenomination=operatorDao.getDenomination(operatorDenominationDTO.getDenominationId());	

		if(operatorDenomination == null){
			throw new EOTException(ErrorConstants.INVALID_DENOMINATION);
		}else{

			operatorDenomination.setDenomination(operatorDenominationDTO.getDenomination());
			operatorDenomination.setActive(operatorDenominationDTO.getActive());

			Operator operator=new Operator();
			operator.setOperatorId(operatorDenominationDTO.getOperatorId());
			operatorDenomination.setOperator(operator);

			operatorDao.save(operatorDenomination);

		}

	}

	@Override
	public OperatorDenominationDTO getDenomination(Long denominationId) throws EOTException {

		OperatorDenomination operatorDenomination=operatorDao.getDenomination(denominationId);		

		if(operatorDenomination == null){
			throw new EOTException(ErrorConstants.INVALID_DENOMINATION);
		}else{

			OperatorDenominationDTO dto = new OperatorDenominationDTO();

			dto.setDenominationId(operatorDenomination.getDenominationId());
			dto.setDenomination(operatorDenomination.getDenomination());
			dto.setActive(operatorDenomination.getActive());
			dto.setOperatorId(operatorDenomination.getOperator().getOperatorId());

			return dto ;

		}

	}

	@Transactional(readOnly = false,propagation=Propagation.REQUIRES_NEW)
	public void  uploadVoucherDetails(OperatorDenominationDTO operatorDenominationDTO) throws EOTException{
		try{
			List<Object> voucherList = new ArrayList<Object>();
			List<String> errors = new ArrayList<String>();
			List<String> voucherSlErrors = new ArrayList<String>();
			List<String> voucherNumErrors = new ArrayList<String>();
			List<String> voucherInvalidValues = new ArrayList<String>();
			List<String> invalidVoucherNumber = new ArrayList<String>();
			List<OperatorDTO> vouchersList = FileUtil.parseDenominationFile(operatorDenominationDTO.getVoucherFile());
			operatorDenominationDTO.setVoucherInvalidSlNum(voucherInvalidValues);
			operatorDenominationDTO.setInvalidVoucherNumbers(invalidVoucherNumber);
			for(int k=0;k<vouchersList.size();k++){

				if(Pattern.matches("[0-9]*", vouchersList.get(k).getVouchurSlNum())){
					continue;
				}else{
					voucherInvalidValues.add(vouchersList.get(k).getSerialNum()+"-"+vouchersList.get(k).getVouchurSlNum());
					continue;
				}

			}  
			if(voucherInvalidValues.size()>0){

				throw new EOTException(ErrorConstants.INVALID_VOUCHER_FILE);

			}
			for(int k=0;k<vouchersList.size();k++){

				if(Pattern.matches("[0-9]*", vouchersList.get(k).getVoucherNum())){

					continue;
				}else{
					invalidVoucherNumber.add(vouchersList.get(k).getSerialNum()+"-"+vouchersList.get(k).getVoucherNum());

					continue;
				}

			}  
			if(invalidVoucherNumber.size()>0){
				throw new EOTException(ErrorConstants.INVALID_VOUCHER_FILE);
			}

			Operator operatorStatus=operatorDao.getOperatorStatus(operatorDenominationDTO.getOperatorId());
			if(operatorStatus==null){
				throw new EOTException(ErrorConstants.OPERATOR_INACTIVE);
			}
			operatorDenominationDTO.setVoucherErrorList(errors);
			Map<String,String> voucherSlNummap=new HashMap<String,String>();

			for(int j=0;j<vouchersList.size();j++){
				for(int k=j+1;k<vouchersList.size();k++){

					if(vouchersList.get(j).getVouchurSlNum().equals(vouchersList.get(k).getVouchurSlNum())){
						voucherSlErrors.add(vouchersList.get(k).getSerialNum());
						voucherSlNummap.put(vouchersList.get(k).getSerialNum(),vouchersList.get(k).getVouchurSlNum());

						continue;
					}
				}  
			}
			HashSet<String> slnumbers=new HashSet<String>(voucherSlErrors);
			ArrayList<String> csvvoucherSlnumDuplicates=new ArrayList<String>(slnumbers);
			ArrayList<Integer> csvvoucherSlnum=new ArrayList<Integer>();

			for(int i=0;i<csvvoucherSlnumDuplicates.size();i++){
				csvvoucherSlnum.add(Integer.parseInt(csvvoucherSlnumDuplicates.get(i)));

			}
			Collections.sort(csvvoucherSlnum);

			List<String> cssVoucherSllist=new ArrayList<String>();

			for(int i=0;i<csvvoucherSlnum.size();i++){
				cssVoucherSllist.add(csvvoucherSlnum.get(i)+"-"+voucherSlNummap.get(String.valueOf(csvvoucherSlnumDuplicates.get(i))));
			}
			//Collections.sort(csvvoucherSlnumDuplicates);
			operatorDenominationDTO.setCsvvoucherSlNum(cssVoucherSllist);
			if(voucherSlErrors.size()>0){

				throw new EOTException(ErrorConstants.INVALID_VOUCHER_FILE);

			}
			Map<String,String> numberMap=new HashMap<String,String>();
			for(int j=0;j<vouchersList.size();j++){
				for(int k=j+1;k<vouchersList.size();k++){
					if(vouchersList.get(j).getVoucherNum().equals(vouchersList.get(k).getVoucherNum())){
						voucherNumErrors.add(vouchersList.get(k).getSerialNum());
						numberMap.put(vouchersList.get(k).getSerialNum(), vouchersList.get(k).getVoucherNum());
						continue;
					}
				}  
			}

			HashSet<String> hs=new HashSet<String>(voucherNumErrors);
			ArrayList<String> csvvoucherNumDuplicates=new ArrayList<String>(hs);
			ArrayList<Integer> csvvoucherNum=new ArrayList<Integer>();
			for(int i=0;i<csvvoucherNumDuplicates.size();i++){
				csvvoucherNum.add(Integer.parseInt(csvvoucherNumDuplicates.get(i)));
			}
			Collections.sort(csvvoucherNum);
			List<String> voucherNumList=new ArrayList<String>();
			for(int i=0;i<csvvoucherNum.size();i++){

				voucherNumList.add(csvvoucherNum.get(i)+"-"+numberMap.get(String.valueOf(csvvoucherNum.get(i))));
			}
			operatorDenominationDTO.setCsvvoucherNum(voucherNumList);
			if(voucherNumErrors.size()>0){
				throw new EOTException(ErrorConstants.INVALID_VOUCHER_FILE);
			}
			for(OperatorDTO operator2:vouchersList){
				OperatorVoucher voucherSlNum=operatorDao.verifyVoucherSlNumber(operator2.getVouchurSlNum(),operatorDenominationDTO.getOperatorId());
				if(voucherSlNum!=null){
					voucherSlErrors.add(operator2.getSerialNum() +" - " +operator2.getVouchurSlNum());
					continue;
				}
			}
			if(voucherSlErrors.size()>0){
				throw new EOTException(ErrorConstants.INVALID_VOUCHER_FILE);
			}
			for(OperatorDTO operator3:vouchersList){
				OperatorVoucher voucherNum=operatorDao.verifyVoucherNum(operator3.getVoucherNum(),operatorDenominationDTO.getOperatorId());
				if(voucherNum!=null){
					voucherNumErrors.add(operator3.getSerialNum() +" - " +operator3.getVoucherNum());
					continue;
				}
			}
			if(voucherNumErrors.size()>0){
				throw new EOTException(ErrorConstants.INVALID_VOUCHER_FILE);
			}
			for(OperatorDTO operator2:vouchersList){
				Operator operator=new Operator();
				OperatorVoucher operatorVoucher =new OperatorVoucher();
				OperatorDenomination denomination=new OperatorDenomination();

				OperatorDenomination denomination2=operatorDao.verifyDenomination(Long.parseLong(operator2.getDenomination()),operatorDenominationDTO.getOperatorId());
				if(denomination2==null){
					errors.add( operator2.getSerialNum() +" - " +operator2.getDenomination());
					continue;
				}

				denomination.setDenominationId(denomination2.getDenominationId());
				operator.setOperatorId(operatorDenominationDTO.getOperatorId());
				operatorVoucher.setActive(1);
				operatorVoucher.setVoucherNumber(operator2.getVoucherNum());
				operatorVoucher.setVoucherSerialNumber(operator2.getVouchurSlNum());
				operatorVoucher.setOperator(operator);
				operatorVoucher.setOperatorDenomination(denomination);
				voucherList.add(operatorVoucher);

			} 

			if(errors.size()>0 ){

				throw new EOTException(ErrorConstants.INVALID_VOUCHER_FILE);

			}else{
				operatorDao.saveList(voucherList);
			}
		}catch(EOTException e){
			throw e;
		}catch(NumberFormatException e){
			throw new EOTException(ErrorConstants.INVALID_VOUCHER_VALUES);
		}


	}

	@Override
	public Operator getOperatorName(Long operatorId) {		
		Operator operator=operatorDao.getOperatorName(operatorId);		
		return operator;
	}

	@Override
	public void checkDenomination(OperatorDenominationDTO operatorDenominationDTO) throws EOTException{
		if(operatorDenominationDTO.getDenominationId() == null){
			OperatorDenomination operatorDenomination=operatorDao.checkDenomination(operatorDenominationDTO.getDenomination(),operatorDenominationDTO.getOperatorId());
			if(operatorDenomination !=null){
				throw new EOTException(ErrorConstants.DENOMINATION_EXIST);
			}
		}else{
			OperatorDenomination operatorDenomination=operatorDao.checkDenomination(operatorDenominationDTO.getDenomination(),operatorDenominationDTO.getOperatorId(),operatorDenominationDTO.getDenominationId());
			if(operatorDenomination !=null){
				throw new EOTException(ErrorConstants.DENOMINATION_EXIST);
			}
		}

	}

	@Override
	public List<Operator> getOperatorListByCountry(Integer countryId) {	
		return operatorDao.getOperatorListByCountry(countryId);
	}

	@Override
	public List<OperatorDenomination> getDenominationList(Long operatorId) {		
		return operatorDao.getDenominationList(operatorId);
	}

	@Override
	public byte[] getChartImageBytes(OperatorDTO operatorDTO)throws EOTException {

		String catAxis = "";
		DefaultCategoryDataset dataset=new DefaultCategoryDataset();
		List list=operatorDao.getChartList(operatorDTO);
		for(int i=0;i<list.size();i++){
			Object[] obj=(Object[])list.get(i);
			dataset.setValue(Double.parseDouble(obj[1].toString()), "", obj[0] != null ? obj[0].toString() : "");
		} 
		String topupDetails="";	
		if(operatorDTO.getImgType()!=null && operatorDTO.getImgType().equals("imgCount")){
			topupDetails="No of Topups";
		}else if(operatorDTO.getImgType()!=null && operatorDTO.getImgType().equals("imgValue")){
			topupDetails="Sum of Topups";
		}	
		JFreeChart chart = ChartFactory.createBarChart("","GIM",topupDetails, dataset, PlotOrientation.VERTICAL, false,true, false);
		CategoryAxis categoryAxis = new CategoryAxis(catAxis);
		chart.setBackgroundPaint(Color.WHITE);
		chart.getTitle().setPaint(Color.BLACK); 
		CategoryPlot p = chart.getCategoryPlot(); 
		p.setRangeGridlinePaint(Color.red);
		GroupedStackedBarRenderer renderer = new GroupedStackedBarRenderer();
		renderer.setMaximumBarWidth(0.07);

		p.setRenderer(renderer);
		categoryAxis.setLowerMargin(0.05);
		categoryAxis.setCategoryMargin(0.7);
		categoryAxis.setUpperMargin(0.05);      
		categoryAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);	    
		// TO KEEP Y AXIS SIZE SHOULD BE FIXED
		//NumberAxis axis = (NumberAxis) p.getRangeAxis(); 
		//axis.setRange(90, 100); 
		//axis.setAutoRangeIncludesZero(false);		
		BarRenderer rendere = (BarRenderer) p.getRenderer();  
		rendere.setBaseItemLabelsVisible(true);  
		rendere.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());  
		rendere.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,TextAnchor.TOP_CENTER));	        
		p.setDomainAxis(categoryAxis);
		p.setRenderer(rendere);
		ChartRenderingInfo info = null;
		info = new ChartRenderingInfo(new StandardEntityCollection());
		BufferedImage chartImage = chart.createBufferedImage(680,370,info);
		PngEncoder encoder = new PngEncoder(chartImage, false, 0, 3);
		return encoder.pngEncode();		
	}

	@Override
	public List getChartList(OperatorDTO operatorDTO)throws EOTException {
		List list=operatorDao.getChartList(operatorDTO);
		if(list==null || list.isEmpty() || list.get(0).equals(0)){
			throw new EOTException(ErrorConstants.NO_TOPUP_FOUND);
		}
		return list;
	}

	@Override
	public byte[] getPieChartImageBytes(OperatorDTO operatorDTO) {
		String catAxis = "";
		DefaultPieDataset pieDataset = new DefaultPieDataset();
		List list=operatorDao.getChartList(operatorDTO);
		for(int i=0;i<list.size();i++){
			Object[] obj=(Object[])list.get(i);
			pieDataset.setValue(obj[0] != null ? obj[0].toString()+"  : "+obj[1].toString()+"  : "+obj[2].toString() : "",Double.parseDouble(obj[1].toString()) );
		} 

		JFreeChart chart = ChartFactory.createPieChart
				("", pieDataset, false,true,true);		

		chart.setBackgroundPaint(new Color(222, 222, 255));
		final PiePlot plot = (PiePlot) chart.getPlot();
		plot.setBackgroundPaint(Color.white);
		plot.setCircular(true);
		plot.setNoDataMessage("No data available");

		// add the chart to a panel...
		ChartRenderingInfo info = null;
		info = new ChartRenderingInfo(new StandardEntityCollection());          
		BufferedImage chartImage = chart.createBufferedImage(680, 370, info);
		PngEncoder encoder = new PngEncoder(chartImage, false, 0, 3);
		return encoder.pngEncode();
	}

	@Override
	public Page searchOperator(OperatorDTO operatorDTO,int pageNumber, String language) throws EOTException {		
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		WebUser webUser=webUserDao.getUser(auth.getName());
		if(webUser==null){
			throw new EOTException(ErrorConstants.INVALID_USER);
		}

		BankTellers teller = bankDao.getTellerByUsername(webUser.getUserName());
		Integer bankId = teller == null ? null : teller.getBank().getBankId();
        Page page = operatorDao.searchOperator(operatorDTO,pageNumber,bankId);	
		
		if(page.getResults().size()==0){
			throw new EOTException(ErrorConstants.OPERATOR_NOT_EXIST); 
		}
		
		
		return page;
	}

	@Override
	public CardDto getCardDetailsByOperatorId(Integer operatorId) {
		CustomerCard card=operatorDao.getCardDetailsByOperatorId(operatorId);

		CardDto cardDto=new CardDto();
		cardDto.setOperatorId(operatorId);

		if( card != null){	
			cardDto.setCardId(card.getCardId());
			try {
				cardDto.setCardNo(new String( kmsHandle.externalDbDesOperation( HexString.hexToBuffer(card.getCardNumber()), false)));
				cardDto.setCvv(new String( kmsHandle.externalDbDesOperation( HexString.hexToBuffer(card.getCvv()), false)));
				cardDto.setCardExpiry(new String( kmsHandle.externalDbDesOperation( HexString.hexToBuffer(card.getCardExpiry()), false)));
			} catch (KMSSecurityException e) {
				e.printStackTrace();

			}
			cardDto.setStatus(card.getStatus());
		}

		return cardDto;
	}

	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=Throwable.class )
	public void saveOrUpdateCard(CardDto cardDto, Map<String, Object> model) throws EOTException {
		CustomerCard card=null;

		if( cardDto.getCardId()==null ){
			try {
				card=operatorDao.getOperatorVertualCard(HexString.bufferToHex(kmsHandle.externalDbDesOperation( cardDto.getCardNo().getBytes(), true) ));
			} catch (KMSSecurityException e) {
				e.printStackTrace();
			}
			if(card!=null)
				throw new EOTException(ErrorConstants.CUSTOMER_CARD_EXIST);
		}else{
			card=operatorDao.getCardDetailsByOperatorId(cardDto.getOperatorId(),cardDto.getCardNo());
			if(card!=null)
				throw new EOTException(ErrorConstants.CUSTOMER_CARD_EXIST);
		}
		
		Operator operator = operatorDao.getOperator(cardDto.getOperatorId().longValue());

		card=new CustomerCard();

		Bank bank=new Bank();
		bank.setBankId(operator.getBank().getBankId());
		card.setBank(bank);

		card.setReferenceId(cardDto.getOperatorId()+"");
		try {
			card.setCardNumber(HexString.bufferToHex(kmsHandle.externalDbDesOperation( cardDto.getCardNo().getBytes(), true) ));
			card.setCvv(HexString.bufferToHex(kmsHandle.externalDbDesOperation( cardDto.getCvv().getBytes(), true) ));
			card.setCardExpiry(HexString.bufferToHex(kmsHandle.externalDbDesOperation( cardDto.getCardExpiry().getBytes(), true) ));
		} catch (KMSSecurityException e) {
			e.printStackTrace();
		}

		card.setStatus(cardDto.getStatus());
		card.setReferenceType(EOTConstants.REFERENCE_TYPE_OPERATOR);
		card.setAlias(EOTConstants.CUSTOMER_VIRTUAL_CARD);

		if(cardDto.getCardId()==null){			
			operatorDao.save(card);
			cardDto.setCardId(card.getCardId());
			model.put("message","ADD_CARD_SUCCESS");
		}else{
			card.setCardId(cardDto.getCardId());
			operatorDao.update(card);
			model.put("message","UPDATE_CARD_SUCCESS");
		}
		model.put("cardDto",cardDto);
	}
}

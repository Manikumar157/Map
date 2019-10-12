package com.eot.banking.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.eot.banking.common.EOTConstants;

public class OperatorDenominationDTO {

	private Long denominationId;
	@NotNull(message="NotNull.operatorDenominationDTO.denomination")
	private Long denomination;	  
	private Integer active = 1;
	private Long operatorId;
    private String operatorName;
    private Integer templateId = EOTConstants.UPLOAD_VOUCHERS_TEMPLETE_ID;
	private List<String> voucherErrorList ;
	private CommonsMultipartFile voucherFile;
	private List<String> voucherSlNumErrorList ;
	private List<String> voucherNumErrorList ;
	private List<String> voucherInvalidSlNum;
	private List<String> invalidVoucherNumbers;
	private List<String> csvvoucherNum;
	private List<String> csvvoucherSlNum;
	
	public List<String> getCsvvoucherNum() {
		return csvvoucherNum;
	}
	public void setCsvvoucherNum(List<String> csvvoucherNum) {
		this.csvvoucherNum = csvvoucherNum;
	}
	public List<String> getCsvvoucherSlNum() {
		return csvvoucherSlNum;
	}
	public void setCsvvoucherSlNum(List<String> csvvoucherSlNum) {
		this.csvvoucherSlNum = csvvoucherSlNum;
	}
	public List<String> getInvalidVoucherNumbers() {
		return invalidVoucherNumbers;
	}
	public void setInvalidVoucherNumbers(List<String> invalidVoucherNumbers) {
		this.invalidVoucherNumbers = invalidVoucherNumbers;
	}
	
	public List<String> getVoucherInvalidSlNum() {
		return voucherInvalidSlNum;
	}
	public void setVoucherInvalidSlNum(List<String> voucherInvalidSlNum) {
		this.voucherInvalidSlNum = voucherInvalidSlNum;
	}
	public Integer getActive() {
		return active;
	}
	public void setActive(Integer active) {
		this.active = active;
	}
	public Long getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}
	public Long getDenominationId() {
		return denominationId;
	}
	public void setDenominationId(Long denominationId) {
		this.denominationId = denominationId;
	}
	public Long getDenomination() {
		return denomination;
	}
	public void setDenomination(Long denomination) {
		this.denomination = denomination;
	}
	public List<String> getVoucherErrorList() {
		
		return voucherErrorList;
	}
	public void setVoucherErrorList(List<String> voucherErrorList) {
		this.voucherErrorList = voucherErrorList;
	}
	public CommonsMultipartFile getVoucherFile() {
		return voucherFile;
	}
	public void setVoucherFile(CommonsMultipartFile voucherFile) {
		this.voucherFile = voucherFile;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public List<String> getVoucherSlNumErrorList() {
		return voucherSlNumErrorList;
	}
	public void setVoucherSlNumErrorList(List<String> voucherSlNumErrorList) {
		this.voucherSlNumErrorList = voucherSlNumErrorList;
	}
	public List<String> getVoucherNumErrorList() {
		return voucherNumErrorList;
	}
	public void setVoucherNumErrorList(List<String> voucherNumErrorList) {
		this.voucherNumErrorList = voucherNumErrorList;
	}
	public Integer getTemplateId() {
		return templateId;
	}
	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}
}

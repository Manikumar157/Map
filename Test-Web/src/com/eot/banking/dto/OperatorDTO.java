package com.eot.banking.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
/**
 * 
 */

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * @author swadhin
 *
 */
public class OperatorDTO implements Serializable{
	
	private String serialNum;
	private Long operatorId;
	@NotEmpty(message="NotEmpty.operatorDTO.operatorName")
	private String operatorName;   
	private Date createdDate; 
	private Integer active ;
	
	@NotNull(message="NotNull.operatorDTO.countryId")
	private Integer countryId;

	private String Denomination;
	private String vouchurSlNum;
	private String voucherNum;
	private String comission;
	@DateTimeFormat(pattern="dd/MM/yyyy") 
	private Date fromDate;	
	@DateTimeFormat(pattern="dd/MM/yyyy") 
	private Date toDate;	
	private Long denominationId;
	
	private String chartType;
	private String imgType;
	//@NotNull(message="NotNull.operatorDTO.bankId")
	private Integer bankId;
	
	
	public Long getDenominationId() {
		return denominationId;
	}
	public void setDenominationId(Long denominationId) {
		this.denominationId = denominationId;
	}
	public String getComission() {
		return comission;
	}
	public void setComission(String comission) {
		this.comission = comission;
	}
	public Long getOperatorId() {
		return operatorId;
	}
	/**
	 * @return the operatorName
	 */
	public String getOperatorName() {
		return operatorName;
	}
	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}
	/**
	 * @return the active
	 */
	public Integer getActive() {
		return active;
	}
	/**
	 * @return the countryId
	 */
	public Integer getCountryId() {
		return countryId;
	}
	/**
	 * @param operatorId the operatorId to set
	 */
	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}
	/**
	 * @param operatorName the operatorName to set
	 */
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	/**
	 * @param active the active to set
	 */
	public void setActive(Integer active) {
		this.active = active;
	}
	/**
	 * @param countryId the countryId to set
	 */
	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	public String getSerialNum() {
		return serialNum;
	}
	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}
	public String getDenomination() {
		return Denomination;
	}
	public void setDenomination(String denomination) {
		Denomination = denomination;
	}
	public String getVouchurSlNum() {
		return vouchurSlNum;
	}
	public void setVouchurSlNum(String vouchurSlNum) {
		this.vouchurSlNum = vouchurSlNum;
	}
	public String getVoucherNum() {
		return voucherNum;
	}
	public void setVoucherNum(String voucherNum) {
		this.voucherNum = voucherNum;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public String getChartType() {
		return chartType;
	}
	public void setChartType(String chartType) {
		this.chartType = chartType;
	}
	public String getImgType() {
		return imgType;
	}
	public void setImgType(String imgType) {
		this.imgType = imgType;
	}
	public Integer getBankId() {
		return bankId;
	}
	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}
}

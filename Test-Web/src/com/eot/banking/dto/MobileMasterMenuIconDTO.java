package com.eot.banking.dto;

import java.sql.Blob;

import org.springframework.web.multipart.MultipartFile;

public class MobileMasterMenuIconDTO {
	
	private String iconCode;
	private Integer iconId;
	private String functionalCode;
	private String iconImage;
	
	
	
	
	public String getFunctionalCode() {
		return functionalCode;
	}
	public void setFunctionalCode(String functionalCode) {
		this.functionalCode = functionalCode;
	}
	public String getIconCode() {
		return iconCode;
	}
	public void setIconCode(String iconCode) {
		this.iconCode = iconCode;
	}
	public Integer getIconId() {
		return iconId;
	}
	public void setIconId(Integer iconId) {
		this.iconId = iconId;
	}
	public String getIconImage() {
		return iconImage;
	}
	public void setIconImage(String iconImage) {
		this.iconImage = iconImage;
	}
	
	
	

}

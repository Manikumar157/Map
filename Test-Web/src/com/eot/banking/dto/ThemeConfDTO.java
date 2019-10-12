package com.eot.banking.dto;

import org.springframework.web.multipart.MultipartFile;

public class ThemeConfDTO {
	
	//Mobile color configuration parameters
	private Integer bankId;
	private String toolBarColor;
	private String tabColorSelected;
	private String tabColorUnselected;
	private String gridColorSelected;
	private String gridColorUnselected;
	private String screenBackgroundColor;
	private String listHeaderColor;
	private String textColor;
	private Integer appType;
	private String bankName;
	//Web color configuration parameters
	private String bankTheme;
	private MultipartFile logoImg;
	private String btnColorName;
	private String logo;
	private String menuColorName;
	private String subMenuColorName;
	
	public Integer getBankId() {
		return bankId;
	}
	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}
	public String getToolBarColor() {
		return toolBarColor;
	}
	public void setToolBarColor(String toolBarColor) {
		this.toolBarColor = toolBarColor;
	}
	public String getTabColorSelected() {
		return tabColorSelected;
	}
	public void setTabColorSelected(String tabColorSelected) {
		this.tabColorSelected = tabColorSelected;
	}
	public String getTabColorUnselected() {
		return tabColorUnselected;
	}
	public void setTabColorUnselected(String tabColorUnselected) {
		this.tabColorUnselected = tabColorUnselected;
	}
	public String getGridColorSelected() {
		return gridColorSelected;
	}
	public void setGridColorSelected(String gridColorSelected) {
		this.gridColorSelected = gridColorSelected;
	}
	public String getGridColorUnselected() {
		return gridColorUnselected;
	}
	public void setGridColorUnselected(String gridColorUnselected) {
		this.gridColorUnselected = gridColorUnselected;
	}
	public String getScreenBackgroundColor() {
		return screenBackgroundColor;
	}
	public void setScreenBackgroundColor(String screenBackgroundColor) {
		this.screenBackgroundColor = screenBackgroundColor;
	}
	public String getListHeaderColor() {
		return listHeaderColor;
	}
	public void setListHeaderColor(String listHeaderColor) {
		this.listHeaderColor = listHeaderColor;
	}
	public String getTextColor() {
		return textColor;
	}
	public void setTextColor(String textColor) {
		this.textColor = textColor;
	}
	public Integer getAppType() {
		return appType;
	}
	public void setAppType(Integer appType) {
		this.appType = appType;
	}
	public MultipartFile getLogoImg() {
		return logoImg;
	}
	public void setLogoImg(MultipartFile logoImg) {
		this.logoImg = logoImg;
	}
	public String getBtnColorName() {
		return btnColorName;
	}
	public void setBtnColorName(String btnColorName) {
		this.btnColorName = btnColorName;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getMenuColorName() {
		return menuColorName;
	}
	public void setMenuColorName(String menuColorName) {
		this.menuColorName = menuColorName;
	}
	public String getSubMenuColorName() {
		return subMenuColorName;
	}
	public void setSubMenuColorName(String subMenuColorName) {
		this.subMenuColorName = subMenuColorName;
	}
	public String getBankTheme() {
		return bankTheme;
	}
	public void setBankTheme(String bankTheme) {
		this.bankTheme = bankTheme;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
}

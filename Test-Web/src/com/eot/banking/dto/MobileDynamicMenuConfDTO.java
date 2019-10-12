package com.eot.banking.dto;

import java.util.List;
import java.util.Map;

public class MobileDynamicMenuConfDTO {
	
	private Integer bankId;
	private Integer profileId;
	private Integer mobileMenuIconId;
	private Integer tabId;
	private String tabs;
	private List<MobileMenuDTO> menuList;
	private Integer appId;
	private String selectedMenuIcon;
	private List<Integer> editMenuList;
	private List<Map<Object, Object>> editMenuIconList;
	
	
	public Integer getAppId() {
		return appId;
	}
	public void setAppId(Integer appId) {
		this.appId = appId;
	}
	public Integer getBankId() {
		return bankId;
	}
	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}
	public Integer getProfileId() {
		return profileId;
	}
	public void setProfileId(Integer profileId) {
		this.profileId = profileId;
	}
	public Integer getMobileMenuIconId() {
		return mobileMenuIconId;
	}
	public void setMobileMenuIconId(Integer mobileMenuIconId) {
		this.mobileMenuIconId = mobileMenuIconId;
	}
	public Integer getTabId() {
		return tabId;
	}
	public void setTabId(Integer tabId) {
		this.tabId = tabId;
	}
	public List<MobileMenuDTO> getMenuList() {
		return menuList;
	}
	public void setMenuList(List<MobileMenuDTO> menuList) {
		this.menuList = menuList;
	}
	public String getTabs() {
		return tabs;
	}
	public void setTabs(String tabs) {
		this.tabs = tabs;
	}
	public String getSelectedMenuIcon() {
		return selectedMenuIcon;
	}
	public void setSelectedMenuIcon(String selectedMenuIcon) {
		this.selectedMenuIcon = selectedMenuIcon;
	}
	public List<Integer> getEditMenuList() {
		return editMenuList;
	}
	public void setEditMenuList(List<Integer> editMenuList) {
		this.editMenuList = editMenuList;
	}
	public List<Map<Object, Object>> getEditMenuIconList() {
		return editMenuIconList;
	}
	public void setEditMenuIconList(List<Map<Object, Object>> editMenuIconList) {
		this.editMenuIconList = editMenuIconList;
	}
	
	
	

}

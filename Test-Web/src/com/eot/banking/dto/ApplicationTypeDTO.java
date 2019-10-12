package com.eot.banking.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * The Class ApplicationTypeDTO.
 */
public class ApplicationTypeDTO implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2722669993345553473L;

	/** The app type id. */
	private int appTypeId;
	
	/** The application type name. */
	private String applicationTypeName;
	
	/** The top menu list. */
	private List<TopMenuDTO> topMenuList = new ArrayList<TopMenuDTO>();

	/**
	 * Gets the app type id.
	 *
	 * @return the app type id
	 */
	public int getAppTypeId() {
		return appTypeId;
	}

	/**
	 * Sets the app type id.
	 *
	 * @param appTypeId the new app type id
	 */
	public void setAppTypeId(int appTypeId) {
		this.appTypeId = appTypeId;
	}

	/**
	 * Gets the application type name.
	 *
	 * @return the application type name
	 */
	public String getApplicationTypeName() {
		return applicationTypeName;
	}

	/**
	 * Sets the application type name.
	 *
	 * @param applicationTypeName the new application type name
	 */
	public void setApplicationTypeName(String applicationTypeName) {
		this.applicationTypeName = applicationTypeName;
	}

	/**
	 * Gets the top menu list.
	 *
	 * @return the top menu list
	 */
	public List<TopMenuDTO> getTopMenuList() {
		return topMenuList;
	}

	/**
	 * Sets the top menu list.
	 *
	 * @param topMenuList the new top menu list
	 */
	public void setTopMenuList(List<TopMenuDTO> topMenuList) {
		this.topMenuList = topMenuList;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ApplicationTypeDTO [appTypeId=" + appTypeId
				+ ", applicationTypeName=" + applicationTypeName
				+ ", topMenuList=" + topMenuList + "]";
	}
	
	
}

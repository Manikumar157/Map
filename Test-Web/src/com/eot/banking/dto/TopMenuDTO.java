package com.eot.banking.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class TopMenuDTO.
 */
public class TopMenuDTO implements Serializable {


	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6289473454620750373L;

	/** The top menu id. */
	private int  topMenuId;
	
	/** The top menu name. */
	private String topMenuName;
	
	/** The access type. */
	private Integer accessType;
	
	/** The left menu dto list. */
	private List<LeftMenuDTO> leftMenuList = new ArrayList<LeftMenuDTO>();

	/**
	 * Gets the top menu id.
	 *
	 * @return the top menu id
	 */
	public int getTopMenuId() {
		return topMenuId;
	}

	/**
	 * Sets the top menu id.
	 *
	 * @param topMenuId the new top menu id
	 */
	public void setTopMenuId(int topMenuId) {
		this.topMenuId = topMenuId;
	}

	/**
	 * Gets the top menu name.
	 *
	 * @return the top menu name
	 */
	public String getTopMenuName() {
		return topMenuName;
	}

	/**
	 * Sets the top menu name.
	 *
	 * @param topMenuName the new top menu name
	 */
	public void setTopMenuName(String topMenuName) {
		this.topMenuName = topMenuName;
	}

	/**
	 * Gets the access type.
	 *
	 * @return the access type
	 */
	public Integer getAccessType() {
		return accessType;
	}

	/**
	 * Sets the access type.
	 *
	 * @param accessType the new access type
	 */
	public void setAccessType(Integer accessType) {
		this.accessType = accessType;
	}

	/**
	 * Gets the left menu list.
	 *
	 * @return the left menu list
	 */
	public List<LeftMenuDTO> getLeftMenuList() {
		return leftMenuList;
	}

	/**
	 * Sets the left menu list.
	 *
	 * @param leftMenuList the new left menu list
	 */
	public void setLeftMenuList(List<LeftMenuDTO> leftMenuList) {
		this.leftMenuList = leftMenuList;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TopMenuDTO [topMenuId=" + topMenuId + ", topMenuName="
				+ topMenuName + ", leftMenuList=" + leftMenuList + "]";
	}
	
}

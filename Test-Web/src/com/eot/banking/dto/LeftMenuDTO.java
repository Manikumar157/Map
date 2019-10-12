package com.eot.banking.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class LeftMenuDTO.
 */
public class LeftMenuDTO implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5992605302902783816L;

	/** The left menu id. */
	private int leftMenuId;
	
	/** The left menu name. */
	private String leftMenuName;
	
	/** The submenu list. */
	private List<SubMenuDTO> subMenuList = new ArrayList<SubMenuDTO>();

	/**
	 * Gets the left menu id.
	 *
	 * @return the left menu id
	 */
	public int getLeftMenuId() {
		return leftMenuId;
	}

	/**
	 * Sets the left menu id.
	 *
	 * @param leftMenuId the new left menu id
	 */
	public void setLeftMenuId(int leftMenuId) {
		this.leftMenuId = leftMenuId;
	}

	/**
	 * Gets the left menu name.
	 *
	 * @return the left menu name
	 */
	public String getLeftMenuName() {
		return leftMenuName;
	}

	/**
	 * Sets the left menu name.
	 *
	 * @param leftMenuName the new left menu name
	 */
	public void setLeftMenuName(String leftMenuName) {
		this.leftMenuName = leftMenuName;
	}

	/**
	 * Gets the sub menu list.
	 *
	 * @return the sub menu list
	 */
	public List<SubMenuDTO> getSubMenuList() {
		return subMenuList;
	}

	/**
	 * Sets the sub menu list.
	 *
	 * @param subMenuList the new sub menu list
	 */
	public void setSubMenuList(List<SubMenuDTO> subMenuList) {
		this.subMenuList = subMenuList;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LeftMenuDTO [leftMenuId=" + leftMenuId + ", leftMenuName="
				+ leftMenuName + ", subMenuList=" + subMenuList + "]";
	}
	
	
}

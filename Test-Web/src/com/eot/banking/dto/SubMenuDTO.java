package com.eot.banking.dto;

import java.io.Serializable;

/**
 * The Class SubMenuDTO.
 */
public class SubMenuDTO implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5597790215237778763L;

	/** The id. */
	private int subMenuId;
	
	/** The sub menu name. */
	private String subMenuName;
	
	/** The applicable. */
	private boolean applicable;
	
	/** The selected value. */
	private String selectedValue;

	/**
	 * Gets the sub menu id.
	 *
	 * @return the sub menu id
	 */
	public int getSubMenuId() {
		return subMenuId;
	}

	/**
	 * Sets the sub menu id.
	 *
	 * @param subMenuId the new sub menu id
	 */
	public void setSubMenuId(int subMenuId) {
		this.subMenuId = subMenuId;
	}

	/**
	 * Gets the sub menu name.
	 *
	 * @return the sub menu name
	 */
	public String getSubMenuName() {
		return subMenuName;
	}

	/**
	 * Sets the sub menu name.
	 *
	 * @param subMenuName the new sub menu name
	 */
	public void setSubMenuName(String subMenuName) {
		this.subMenuName = subMenuName;
	}

	/**
	 * Checks if is applicable.
	 *
	 * @return true, if is applicable
	 */
	public boolean isApplicable() {
		return applicable;
	}

	/**
	 * Sets the applicable.
	 *
	 * @param applicable the new applicable
	 */
	public void setApplicable(boolean applicable) {
		this.applicable = applicable;
	}

	/**
	 * Gets the selected value.
	 *
	 * @return the selected value
	 */
	public String getSelectedValue() {
		return selectedValue;
	}

	/**
	 * Sets the selected value.
	 *
	 * @param selectedValue the new selected value
	 */
	public void setSelectedValue(String selectedValue) {
		this.selectedValue = selectedValue;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SubMenuDTO [subMenuId=" + subMenuId + ", subMenuName="
				+ subMenuName + ", applicable=" + applicable
				+ ", selectedValue=" + selectedValue + "]";
	}
	
}

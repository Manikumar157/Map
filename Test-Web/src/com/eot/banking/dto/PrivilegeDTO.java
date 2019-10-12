package com.eot.banking.dto;

import java.io.Serializable;

/**
 * The Class PrivilegeDTO.
 */
public class PrivilegeDTO implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8306729700947361273L;
	
	/** The app type id. */
	private Integer appTypeId;
	
	/** The top menu id. */
	private Integer topMenuId;
	
	/** The top menu name. */
	private String topMenuName;
	
	/** The left menu id. */
	private Integer leftMenuId;
	
	/** The left menu name. */
	private String leftMenuName;
	
	/** The sub menu id. */
	private Integer subMenuId;
	
	/** The sub menu name. */
	private String subMenuName;
	
	/** The entity code. */
	private String entityCode;
	
	/** The add removal flag. */
	private Integer addRemovalFlag;
	
	/** The reference id. */
	private String referenceId;
	
	/** The reference type. */
	private Integer referenceType;
	
	/** The visibility. */
	private Integer visibility;
	
	/** The selected. */
	private boolean selected;
	
	/** The access type. */
	private Integer accessType;

	/**
	 * Gets the app type id.
	 *
	 * @return the app type id
	 */
	public Integer getAppTypeId() {
		return appTypeId;
	}

	/**
	 * Sets the app type id.
	 *
	 * @param appTypeId the new app type id
	 */
	public void setAppTypeId(Integer appTypeId) {
		this.appTypeId = appTypeId;
	}

	/**
	 * Gets the top menu id.
	 *
	 * @return the top menu id
	 */
	public Integer getTopMenuId() {
		return topMenuId;
	}

	/**
	 * Sets the top menu id.
	 *
	 * @param topMenuId the new top menu id
	 */
	public void setTopMenuId(Integer topMenuId) {
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
	 * Gets the left menu id.
	 *
	 * @return the left menu id
	 */
	public Integer getLeftMenuId() {
		return leftMenuId;
	}

	/**
	 * Sets the left menu id.
	 *
	 * @param leftMenuId the new left menu id
	 */
	public void setLeftMenuId(Integer leftMenuId) {
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
	 * Gets the sub menu id.
	 *
	 * @return the sub menu id
	 */
	public Integer getSubMenuId() {
		return subMenuId;
	}

	/**
	 * Sets the sub menu id.
	 *
	 * @param subMenuId the new sub menu id
	 */
	public void setSubMenuId(Integer subMenuId) {
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
	 * Gets the entity code.
	 *
	 * @return the entity code
	 */
	public String getEntityCode() {
		return entityCode;
	}

	/**
	 * Sets the entity code.
	 *
	 * @param entityCode the new entity code
	 */
	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}

	/**
	 * Gets the adds the removal flag.
	 *
	 * @return the adds the removal flag
	 */
	public Integer getAddRemovalFlag() {
		return addRemovalFlag;
	}

	/**
	 * Sets the adds the removal flag.
	 *
	 * @param addRemovalFlag the new adds the removal flag
	 */
	public void setAddRemovalFlag(Integer addRemovalFlag) {
		this.addRemovalFlag = addRemovalFlag;
	}

	/**
	 * Gets the reference id.
	 *
	 * @return the reference id
	 */
	public String getReferenceId() {
		return referenceId;
	}

	/**
	 * Sets the reference id.
	 *
	 * @param referenceId the new reference id
	 */
	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	/**
	 * Gets the reference type.
	 *
	 * @return the reference type
	 */
	public Integer getReferenceType() {
		return referenceType;
	}

	/**
	 * Sets the reference type.
	 *
	 * @param referenceType the new reference type
	 */
	public void setReferenceType(Integer referenceType) {
		this.referenceType = referenceType;
	}

	/**
	 * Checks if is visibility.
	 *
	 * @return true, if is visibility
	 */
	public Integer getVisibility() {
		return visibility;
	}

	/**
	 * Sets the visibility.
	 *
	 * @param visibility the new visibility
	 */
	public void setVisibility(Integer visibility) {
		this.visibility = visibility;
	}

	/**
	 * Checks if is selected.
	 *
	 * @return true, if is selected
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * Sets the selected.
	 *
	 * @param selected the new selected
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
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

}

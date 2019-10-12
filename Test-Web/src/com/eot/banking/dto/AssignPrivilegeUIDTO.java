package com.eot.banking.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class AssignPrivilegeUIDTO.
 */
public class AssignPrivilegeUIDTO  implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1175665997967823175L;
	
	/** The ref id. */
	private String userId;
	
	/** The role id. */
	private String roleId;
	
	private String roleName;
	
	/** The ref type. */
	private Integer refType;
	
	/** The is form submit. */
	private Integer isFormSubmit;
	
	/** The application type dto list. */
	private List<ApplicationTypeDTO> applicationTypeDTOList = new ArrayList<ApplicationTypeDTO>();
	
	/** The privilege names. */
	private List<String> privilegeNames;


	/**
	 * Gets the user id.
	 *
	 * @return the user id
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets the user id.
	 *
	 * @param userId the new user id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Gets the role id.
	 *
	 * @return the role id
	 */
	public String getRoleId() {
		return roleId;
	}

	/**
	 * Sets the role id.
	 *
	 * @param roleId the new role id
	 */
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
	 * Gets the ref type.
	 *
	 * @return the ref type
	 */
	public Integer getRefType() {
		return refType;
	}

	/**
	 * Sets the ref type.
	 *
	 * @param refType the new ref type
	 */
	public void setRefType(Integer refType) {
		this.refType = refType;
	}

	/**
	 * Gets the checks if is form submit.
	 *
	 * @return the checks if is form submit
	 */
	public Integer getIsFormSubmit() {
		return isFormSubmit;
	}

	/**
	 * Sets the checks if is form submit.
	 *
	 * @param isFormSubmit the new checks if is form submit
	 */
	public void setIsFormSubmit(Integer isFormSubmit) {
		this.isFormSubmit = isFormSubmit;
	}

	/**
	 * Gets the application type dto list.
	 *
	 * @return the application type dto list
	 */
	public List<ApplicationTypeDTO> getApplicationTypeDTOList() {
		return applicationTypeDTOList;
	}

	/**
	 * Sets the application type dto list.
	 *
	 * @param applicationTypeDTOList the new application type dto list
	 */
	public void setApplicationTypeDTOList(
			List<ApplicationTypeDTO> applicationTypeDTOList) {
		this.applicationTypeDTOList = applicationTypeDTOList;
	}

	/**
	 * Gets the privilege names.
	 *
	 * @return the privilege names
	 */
	public List<String> getPrivilegeNames() {
		return privilegeNames;
	}

	/**
	 * Sets the privilege names.
	 *
	 * @param privilegeNames the new privilege names
	 */
	public void setPrivilegeNames(List<String> privilegeNames) {
		this.privilegeNames = privilegeNames;
	}

	

}

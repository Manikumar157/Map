package com.eot.banking.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class PrivilegeAssignmentDTO.
 */
public class PrivilegeAssignmentDTO implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2731003677319253160L;

	/** The reference id. */
	private String referenceId;

	/** The reference type. */
	private Integer referenceType;
	
	/** The application type dto list. */
	private List<ApplicationTypeDTO> applicationTypeDTOList = new ArrayList<ApplicationTypeDTO>();
	
	/** The application type dto list save. */
	private List<ApplicationTypeDTO> applicationTypeDTOListSave = new ArrayList<ApplicationTypeDTO>();
	
	/** The privilege names. */
	private List<String> privilegeNames;

	/** The entity code. */
	private String entityCode;

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
	 * Gets the application type dto list save.
	 *
	 * @return the application type dto list save
	 */
	public List<ApplicationTypeDTO> getApplicationTypeDTOListSave() {
		return applicationTypeDTOListSave;
	}

	/**
	 * Sets the application type dto list save.
	 *
	 * @param applicationTypeDTOListSave the new application type dto list save
	 */
	public void setApplicationTypeDTOListSave(
			List<ApplicationTypeDTO> applicationTypeDTOListSave) {
		this.applicationTypeDTOListSave = applicationTypeDTOListSave;
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
	 * The Interface PrivilegeReferenceCheck.
	 */
	public interface PrivilegeReferenceCheck{}
	
}

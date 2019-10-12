package com.eot.banking.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * The Class RoleDto.
 */
public class RoleDto implements Serializable {
	
    /** The role name. */
    private String roleName;

    /** The role description. */
    private String roleDescription;

    /** The status. */
    private Integer status;

    /** The created date. */
    private Date createdDate;

    /** The updated date. */
    private Date updatedDate;
    
    private Integer roleId;

	/**
	 * Gets the role name.
	 *
	 * @return the role name
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * Sets the role name.
	 *
	 * @param roleName the new role name
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
	 * Gets the role description.
	 *
	 * @return the role description
	 */
	public String getRoleDescription() {
		return roleDescription;
	}

	/**
	 * Sets the role description.
	 *
	 * @param roleDescription the new role description
	 */
	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * Gets the created date.
	 *
	 * @return the created date
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * Sets the created date.
	 *
	 * @param createdDate the new created date
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * Gets the updated date.
	 *
	 * @return the updated date
	 */
	public Date getUpdatedDate() {
		return updatedDate;
	}

	/**
	 * Sets the updated date.
	 *
	 * @param updatedDate the new updated date
	 */
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RoleDto [roleName=" + roleName + ", roleDescription="
				+ roleDescription + ", status=" + status + ", createdDate="
				+ createdDate + ", updatedDate=" + updatedDate + "]";
	}
	
}

package com.eot.banking.service;

import java.util.List;

import com.eot.banking.dto.PrivilegeAssignmentDTO;
import com.eot.banking.dto.RoleDto;
import com.eot.banking.exceptions.EOTException;
import com.eot.entity.WebUser;
import com.eot.entity.WebUserRole;

/**
 * The Interface RoleAndPrivilegeService.
 */
public interface RoleAndPrivilegeService {

	/**
	 * Show privilege assignment details.
	 *
	 * @param privilegeAssignmentDTO the privilege assignment dto
	 * @return the privilege assignment dto
	 * @throws NhanceApplicationException the corum application exception
	 */
	public PrivilegeAssignmentDTO showPrivilegeAssignmentDetails(PrivilegeAssignmentDTO privilegeAssignmentDTO) throws EOTException;

	/**
	 * Save privilege details.
	 *
	 * @param privilegeAssignmentDTO the privilege assignment dto
	 * @return the privilege assignment dto
	 * @throws NhanceApplicationException the corum application exception
	 */
	public PrivilegeAssignmentDTO savePrivilegeDetails(PrivilegeAssignmentDTO privilegeAssignmentDTO) throws EOTException;

	/**
	 * List role details.
	 *
	 * @return the list
	 */
	public List<RoleDto> listRoleDetails();

	/**
	 * Gets the login role list.
	 *
	 * @return the login role list
	 */
	public List<WebUserRole> getLoginRoleList();

	/**
	 * Gets the users by role id.
	 *
	 * @param roleId the role id
	 * @return the users by role id
	 */
	List<WebUser> getUsersByRoleId(Integer roleId);

	

}

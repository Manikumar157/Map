package com.eot.banking.dao;

import java.util.HashMap;
import java.util.List;

import com.eot.banking.dto.PrivilegeDTO;
import com.eot.entity.LoginLeftMenu;
import com.eot.entity.LoginPrivilegeMapping;
import com.eot.entity.LoginSubMenu;
import com.eot.entity.WebUser;
import com.eot.entity.WebUserRole;

/**
 * The Interface RoleAndPrivilegeDao.
 */
public interface RoleAndPrivilegeDao extends BaseDao {
	
	/**
	 * Load all possible privileges.
	 *
	 * @param privilegeDTO the privilege dto
	 * @return the list
	 */
	List<HashMap<String, Object>> loadAllPossiblePrivileges(PrivilegeDTO privilegeDTO);

	/**
	 * Search sub menus.
	 *
	 * @return the list
	 */
	List<LoginSubMenu> searchSubMenus();

	/**
	 * Delete privilege details.
	 *
	 * @param privilegeDTO the privilege dto
	 */
	void deletePrivilegeDetails(PrivilegeDTO privilegeDTO);

	/**
	 * Load add and removed privileges.
	 *
	 * @param privilegeDTO the privilege dto
	 * @return the list
	 */
	List<LoginPrivilegeMapping> loadAddAndRemovedPrivileges(PrivilegeDTO privilegeDTO);

	/**
	 * Load left menu.
	 *
	 * @param leftMenuId the left menu id
	 * @return the login left menu
	 */
	LoginLeftMenu loadLeftMenu(Integer leftMenuId);

	/**
	 * Load sub menu.
	 *
	 * @param subMenuId the sub menu id
	 * @return the login sub menu
	 */
	LoginSubMenu loadSubMenu(Integer subMenuId);

	/**
	 * List role.
	 *
	 * @return lsit of role
	 */
	List<WebUserRole> listRole();

	/**
	 * Gets the login role list.
	 *
	 * @return the login role list
	 */
	List<WebUserRole> getLoginRoleList();

	/**
	 * Check role name exist.
	 *
	 * @param roleName the role name
	 * @return the login role
	 */
	WebUserRole checkRoleNameExist(String roleName);

	List<WebUser> getUsersByRoleId(Integer roleId);

	List<WebUserRole> listRole(WebUser user);

}

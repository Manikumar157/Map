package com.eot.banking.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.eot.banking.common.EOTConstants;
import com.eot.banking.dao.RoleAndPrivilegeDao;
import com.eot.banking.dto.PrivilegeDTO;
import com.eot.entity.LoginLeftMenu;
import com.eot.entity.LoginPrivilegeMapping;
import com.eot.entity.LoginSubMenu;
import com.eot.entity.WebUser;
import com.eot.entity.WebUserRole;

/**
 * The Class RoleAndPrivilegeDaoImpl.
 */
public class RoleAndPrivilegeDaoImpl extends BaseDaoImpl implements RoleAndPrivilegeDao {
	
	/* (non-Javadoc)
	 * @see com.corum.dao.LoginDao#loadAllPossiblePrivileges(com.corum.dto.PrivilegeDTO)
	 */
	
	/** The logger. */
	private Logger logger = Logger.getLogger(RoleAndPrivilegeDaoImpl.class);
	
	/* (non-Javadoc)
	 * @see com.nhance.dao.RoleAndPrivilegeDao#loadAllPossiblePrivileges(com.nhance.dto.PrivilegeDTO)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<HashMap<String, Object>> loadAllPossiblePrivileges(
			PrivilegeDTO privilegeDTO) {
		
		List<HashMap<String, Object>> privileges = null;
		
		String queryString = "SELECT "+
				"tm.application_type AS appTypeId, "+
				"tm.top_menu_id AS topMenuId, tm.top_menu_name AS topMenuName, "+
				"lm.left_menu_id AS leftMenuId,lm.left_menu_name AS leftMenuName, "+ 
				"sm.sub_menu_id AS subMenuId, sm.sub_menu_name AS subMenuName, "+
				"vis.sub_menu_id AS visibility, tm.access_type as accessType " +
			 "FROM "+ 
				 "login_top_menu tm " +
				 "JOIN login_left_menu lm ON lm.top_menu_id = tm.top_menu_id "+
				 "JOIN login_sub_menu sm "+
				 "LEFT JOIN login_menu_visibility vis ON vis.left_menu_id=lm.left_menu_id AND vis.sub_menu_id=sm.sub_menu_id "+
				 "ORDER BY left_menu_name";


		Query query = getHibernateTemplate().getSessionFactory().getCurrentSession()
			.createSQLQuery(queryString)
			.addScalar("appTypeId", Hibernate.INTEGER)
			.addScalar("topMenuId", Hibernate.INTEGER)
			.addScalar("topMenuName", Hibernate.STRING)
			.addScalar("leftMenuId", Hibernate.INTEGER)
			.addScalar("leftMenuName", Hibernate.STRING)
			.addScalar("subMenuId", Hibernate.INTEGER)
			.addScalar("subMenuName", Hibernate.STRING)
			.addScalar("visibility", Hibernate.INTEGER)
			.addScalar("accessType", Hibernate.INTEGER)
		.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		
		privileges = query.list();
		return CollectionUtils.isNotEmpty(privileges) ? privileges: null;
	}
	
	/* (non-Javadoc)
	 * @see com.corum.dao.LoginDao#searchSubMenus()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<LoginSubMenu> searchSubMenus() {
		List<LoginSubMenu> authRoleprivSubMenus = (List<LoginSubMenu>) getHibernateTemplate().find("from LoginSubMenu");
		return CollectionUtils.isNotEmpty(authRoleprivSubMenus) ? authRoleprivSubMenus: null;
	}
	
	/* (non-Javadoc)
	 * @see com.corum.dao.LoginDao#deletePrivilegeDetails(com.corum.dto.PrivilegeDTO)
	 */
	@Override
	public void deletePrivilegeDetails(PrivilegeDTO privilegeDTO) {
		Query query = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(
						"DELETE from LoginPrivilegeMapping prvMapping " +
						"where prvMapping.referenceType=:referenceType and prvMapping.referenceId=:referenceId ");
		query.setParameter("referenceType", privilegeDTO.getReferenceType());
		query.setParameter("referenceId", privilegeDTO.getReferenceId());
		query.executeUpdate();
		
	}
	
	/* (non-Javadoc)
	 * @see com.corum.dao.LoginDao#loadAddAndRemovedPrivileges(com.corum.dto.PrivilegeDTO)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<LoginPrivilegeMapping> loadAddAndRemovedPrivileges(
			PrivilegeDTO privilegeDTO) {
		
		List<LoginPrivilegeMapping> authRoleprivPrivilegeMappings = null;
		
		DetachedCriteria criteria = DetachedCriteria.forClass(LoginPrivilegeMapping.class, "mappings");
		
		criteria.createAlias("mappings.loginLeftMenu", "leftMenu");
		criteria.setFetchMode("leftMenu", FetchMode.JOIN);
		
		criteria.createAlias("leftMenu.loginTopMenu", "topMenu");
		criteria.setFetchMode("topMenu", FetchMode.JOIN);
		
		criteria.createAlias("mappings.loginSubMenu", "subMenu");
		criteria.setFetchMode("subMenu", FetchMode.JOIN);
		
		criteria.add(Restrictions.eq("mappings.referenceId", privilegeDTO.getReferenceId()));
		criteria.add(Restrictions.eq("mappings.referenceType", privilegeDTO.getReferenceType()));
		
		authRoleprivPrivilegeMappings = (List<LoginPrivilegeMapping>) getHibernateTemplate().findByCriteria(criteria);
		return CollectionUtils.isNotEmpty(authRoleprivPrivilegeMappings) ? authRoleprivPrivilegeMappings: null;
	}
	
	/* (non-Javadoc)
	 * @see com.corum.dao.LoginDao#loadLeftMenu(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public LoginLeftMenu loadLeftMenu(Integer leftMenuId) {
		List<LoginLeftMenu> authRoleprivLeftMenus = (List<LoginLeftMenu>) getHibernateTemplate().find("from LoginLeftMenu as lmenu where lmenu.leftMenuId = ?", leftMenuId);
		return CollectionUtils.isNotEmpty(authRoleprivLeftMenus) ? authRoleprivLeftMenus.get(0) : null;
	}
	
	/* (non-Javadoc)
	 * @see com.corum.dao.LoginDao#loadSubMenu(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public LoginSubMenu loadSubMenu(Integer subMenuId) {
		List<LoginSubMenu> authRoleprivSubMenus = (List<LoginSubMenu>) getHibernateTemplate().find("from LoginSubMenu as smenu where smenu.subMenuId = ?", subMenuId);
		return CollectionUtils.isNotEmpty(authRoleprivSubMenus) ? authRoleprivSubMenus.get(0) : null;
	}

	/*@Override
	public List<LoginUser> listUser() {
		DetachedCriteria criteria = DetachedCriteria.forClass(LoginUser.class, "login");

		criteria.setFetchMode("role", FetchMode.JOIN);
		criteria.createAlias("login.loginRole", "role");

		List<LoginUser> loginUsers = getHibernateTemplate().findByCriteria(criteria);

		return CollectionUtils.isNotEmpty(loginUsers) ? loginUsers: null;
	}*/
	
	
	
	/* (non-Javadoc)
	 * @see com.corum.dao.RoleAndPrivilegeDao#listRole()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<WebUserRole> listRole() {
		DetachedCriteria criteria = DetachedCriteria.forClass(WebUserRole.class,"role");
		List<WebUserRole> roleDetails = (List<WebUserRole>) getHibernateTemplate().findByCriteria(criteria);
		return roleDetails;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<WebUserRole> listRole(WebUser user) {
		DetachedCriteria criteria = DetachedCriteria.forClass(WebUserRole.class,"role");
		criteria.add(Restrictions.gt("roleId", EOTConstants.ROLEID_BANK_ADMIN));
		List<WebUserRole> roleDetails = (List<WebUserRole>) getHibernateTemplate().findByCriteria(criteria);
		return roleDetails;
	}
	/* (non-Javadoc)
	 * @see com.corum.dao.RoleAndPrivilegeDao#getLoginRoleList()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<WebUserRole> getLoginRoleList() {
		DetachedCriteria criteria = DetachedCriteria.forClass(WebUserRole.class, "role");
		List<WebUserRole> loginRoles = (List<WebUserRole>) getHibernateTemplate().findByCriteria(criteria);
		return CollectionUtils.isNotEmpty(loginRoles) ? loginRoles: null;
	}
	
	/* (non-Javadoc)
	 * @see com.nhance.dao.RoleAndPrivilegeDao#checkRoleNameExist(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public WebUserRole checkRoleNameExist(String roleName) {
		logger.info("inside checkRoleNameExist method..... ");
		DetachedCriteria criteria=DetachedCriteria.forClass(WebUserRole.class, "role");
		criteria.add(Restrictions.eq("role.roleName", roleName));
		List<WebUserRole> roleNames=(List<WebUserRole>) getHibernateTemplate().findByCriteria(criteria);
		return CollectionUtils.isNotEmpty(roleNames) ? roleNames.get(0): null;
		
	}
	
	/* (non-Javadoc)
	 * @see com.eot.banking.dao.RoleAndPrivilegeDao#getUsersByRoleId(java.lang.Integer)
	 */
	@Override
	public List<WebUser> getUsersByRoleId(Integer roleId) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from WebUser as user where user.webUserRole.roleId=:roleId").setParameter("roleId", roleId);
		return query.list();
	}

}

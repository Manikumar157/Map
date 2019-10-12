package com.eot.banking.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eot.banking.common.EOTConstants;
import com.eot.banking.dao.RoleAndPrivilegeDao;
import com.eot.banking.dao.WebUserDao;
import com.eot.banking.dto.ApplicationTypeDTO;
import com.eot.banking.dto.LeftMenuDTO;
import com.eot.banking.dto.PrivilegeAssignmentDTO;
import com.eot.banking.dto.PrivilegeDTO;
import com.eot.banking.dto.RoleDto;
import com.eot.banking.dto.SubMenuDTO;
import com.eot.banking.dto.TopMenuDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.utils.AccessTypeEnum;
import com.eot.banking.utils.AddRemovePrivilegeFlagEnum;
import com.eot.banking.utils.ApplicationTypeEEnum;
import com.eot.banking.utils.DevAppTypeEnum;
import com.eot.banking.utils.Page;
import com.eot.banking.utils.ReferenceTypeEEnum;
import com.eot.entity.LoginLeftMenu;
import com.eot.entity.LoginPrivilegeMapping;
import com.eot.entity.LoginSubMenu;
import com.eot.entity.WebUser;
import com.eot.entity.WebUserRole;

/**
 * The Class RoleAndPrivilegeServiceImpl.
 */
@Service("roleAndPrivilegeService")
public class RoleAndPrivilegeServiceImpl implements RoleAndPrivilegeService {
	
	/** The and privilege dao. */
	@Autowired
	private RoleAndPrivilegeDao roleAndPrivilegeDao;
	
	/** The web user dao. */
	@Autowired
	private WebUserDao webUserDao;
	
	
	/* (non-Javadoc)
	 * @see com.corum.service.LoginService#showPrivilegeAssignmentDetails(com.corum.dto.PrivilegeAssignmentDTO)
	 */
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public PrivilegeAssignmentDTO showPrivilegeAssignmentDetails(PrivilegeAssignmentDTO privilegeAssignmentDTO) throws EOTException {
		
		return preparePrvList(privilegeAssignmentDTO);
		
	}
	
	/* (non-Javadoc)
	 * @see com.corum.service.LoginService#savePrivilegeDetails(com.corum.dto.PrivilegeAssignmentDTO)
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public PrivilegeAssignmentDTO savePrivilegeDetails(PrivilegeAssignmentDTO privilegeAssignmentDTO) throws EOTException {
		
		String entityCode = null;
		PrivilegeDTO privilegeDTO = new PrivilegeDTO();
		List<PrivilegeDTO> privilegeDTOs = null;
		String[] menuIds  = null;
		String referenceId = privilegeAssignmentDTO.getReferenceId();
		List<String> postTypePrivileges = null;
		
		Integer referenceType = privilegeAssignmentDTO.getReferenceType();
		
		privilegeDTO.setReferenceId(privilegeAssignmentDTO.getReferenceId());
		privilegeDTO.setReferenceType(privilegeAssignmentDTO.getReferenceType());
		
		List<ApplicationTypeDTO>  applicationTypeDTOs = privilegeAssignmentDTO.getApplicationTypeDTOListSave();
		List<String> selectedPrivilegeList = preparePrivilegeDetails(applicationTypeDTOs);
		
		// Delete all the existing privileges for the referenceId
		privilegeDTO.setEntityCode(entityCode);
		roleAndPrivilegeDao.deletePrivilegeDetails(privilegeDTO);
		
		if(referenceType.equals(ReferenceTypeEEnum.USER.getCode())) {
			
			WebUser user=webUserDao.getUser(privilegeAssignmentDTO.getReferenceId());
			
			// Load all selected privileges for post type
			PrivilegeDTO dto = new PrivilegeDTO();
			dto.setReferenceId(user.getWebUserRole().getRoleId().toString());
			dto.setReferenceType(ReferenceTypeEEnum.ROLE.getCode());
			List<LoginPrivilegeMapping> privilegeMappings = roleAndPrivilegeDao.loadAddAndRemovedPrivileges(dto);
			if(privilegeMappings != null) {
				privilegeDTOs = new ArrayList<PrivilegeDTO>();
				for (LoginPrivilegeMapping privilegeMapping : privilegeMappings) {
					privilegeDTO = new PrivilegeDTO();
					privilegeDTO.setAppTypeId(privilegeMapping.getLoginLeftMenu().getLoginTopMenu().getApplicationType());
					privilegeDTO.setAccessType(privilegeMapping.getLoginLeftMenu().getLoginTopMenu().getAccessType());
					privilegeDTO.setTopMenuId(privilegeMapping.getLoginLeftMenu().getLoginTopMenu().getTopMenuId());
					privilegeDTO.setTopMenuName(privilegeMapping.getLoginLeftMenu().getLoginTopMenu().getTopMenuName());
					privilegeDTO.setLeftMenuId(privilegeMapping.getLoginLeftMenu().getLeftMenuId());
					privilegeDTO.setLeftMenuName(privilegeMapping.getLoginLeftMenu().getLeftMenuName());
					privilegeDTO.setSubMenuId(privilegeMapping.getLoginSubMenu().getSubMenuId());
					privilegeDTO.setSubMenuName(privilegeMapping.getLoginSubMenu().getSubMenuName());
					privilegeDTO.setAddRemovalFlag(privilegeMapping.getAddRemovalFlag());
					privilegeDTOs.add(privilegeDTO);
				}
				
				postTypePrivileges = new ArrayList<String>();
				for (PrivilegeDTO postTypeDto : privilegeDTOs) {
					if(postTypeDto.getAddRemovalFlag().equals(AddRemovePrivilegeFlagEnum.ADDED.getCode())) {
						postTypePrivileges.add(postTypeDto.getLeftMenuId() + "_" + postTypeDto.getSubMenuId());
					}
				}
				// Remove selected privileges from postTypePrivileges
				postTypePrivileges.removeAll(selectedPrivilegeList);
				selectedPrivilegeList = postTypePrivileges;
				
			}
			
		}
		
		// Save All the selected privileges from ui
		if(CollectionUtils.isNotEmpty(selectedPrivilegeList)) {
			privilegeDTOs = new ArrayList<PrivilegeDTO>();
			for (String selectedPrivlege : selectedPrivilegeList) {
				privilegeDTO = new PrivilegeDTO();
				menuIds  = selectedPrivlege.split("_");
				privilegeDTO.setReferenceId(referenceId);
				privilegeDTO.setReferenceType(referenceType);
				privilegeDTO.setLeftMenuId(Integer.parseInt(menuIds[0]));
				privilegeDTO.setSubMenuId(Integer.parseInt(menuIds[1]));
				privilegeDTO.setEntityCode(entityCode);
				if(referenceType.equals(ReferenceTypeEEnum.USER.getCode())) {
					privilegeDTO.setAddRemovalFlag(AddRemovePrivilegeFlagEnum.REMOVED.getCode());
				} else if(referenceType.equals(ReferenceTypeEEnum.ROLE.getCode())) {
					privilegeDTO.setAddRemovalFlag(AddRemovePrivilegeFlagEnum.ADDED.getCode());
				}
				privilegeDTOs.add(privilegeDTO);
			}
			
			
			List<Object> authRoleprivPrivilegeMappings = new ArrayList<Object>();
			
			LoginPrivilegeMapping authRoleprivPrivilegeMapping = null;
			LoginLeftMenu authRoleprivLeftMenu = null;
			LoginSubMenu authRoleprivSubMenu = null;
			for (PrivilegeDTO privilDTO : privilegeDTOs) {
				authRoleprivPrivilegeMapping = new LoginPrivilegeMapping();
				authRoleprivLeftMenu = roleAndPrivilegeDao.loadLeftMenu(privilDTO.getLeftMenuId());
				authRoleprivSubMenu = roleAndPrivilegeDao.loadSubMenu(privilDTO.getSubMenuId());
				authRoleprivPrivilegeMapping.setAddRemovalFlag(privilDTO.getAddRemovalFlag());
				authRoleprivLeftMenu.setLeftMenuId(privilDTO.getLeftMenuId());
				authRoleprivPrivilegeMapping.setLoginLeftMenu(authRoleprivLeftMenu);
				authRoleprivSubMenu.setSubMenuId(privilDTO.getSubMenuId());
				authRoleprivPrivilegeMapping.setLoginSubMenu(authRoleprivSubMenu);
				authRoleprivPrivilegeMapping.setCreatedDate(new Date());
				authRoleprivPrivilegeMapping.setReferenceId(privilDTO.getReferenceId());
				authRoleprivPrivilegeMapping.setReferenceType(privilDTO.getReferenceType());
				authRoleprivPrivilegeMapping.setUpdatedDate(new Date());
				authRoleprivPrivilegeMappings.add(authRoleprivPrivilegeMapping);
			}
			
			roleAndPrivilegeDao.saveList(authRoleprivPrivilegeMappings);
			
		}
		
		// fetch and set other details like search details
		privilegeAssignmentDTO = preparePrvList(privilegeAssignmentDTO);

		//TODO load user/role list
		
		return privilegeAssignmentDTO;
		
	}
	
	/**
	 * Prepare prv list.
	 *
	 * @param privilegeAssignmentDTO the privilege assignment dto
	 * @return the privilege assignment dto
	 * @throws NhanceApplicationException the corum application exception
	 */
	public PrivilegeAssignmentDTO preparePrvList(PrivilegeAssignmentDTO privilegeAssignmentDTO) throws EOTException {
		
		PrivilegeDTO privilegeDTO = new PrivilegeDTO();
		Integer referenceType = privilegeAssignmentDTO.getReferenceType();
		
		privilegeDTO.setReferenceId(privilegeAssignmentDTO.getReferenceId());
		privilegeDTO.setReferenceType(privilegeAssignmentDTO.getReferenceType());
		
		Map<String, Integer> selectedPrivilegeMap = new HashMap<String, Integer>();
		Map<String, Integer> selectedPrivilegeForPostTypeMap = new HashMap<String, Integer>();
		List<String> subMenuNames = new ArrayList<String>();
		Map<String, Object> privilegeMap = new HashMap<String, Object>();
		SubMenuDTO subMenuDTO = null;
		List<PrivilegeDTO> selectedPrivilegeList = null;
		
		if(referenceType.equals(ReferenceTypeEEnum.USER.getCode())) {
			
			WebUser user=webUserDao.getUser(privilegeAssignmentDTO.getReferenceId());
			
			// Load all selected privileges for post type
			PrivilegeDTO dto = new PrivilegeDTO();
			dto.setReferenceId(user.getWebUserRole().getRoleId().toString()); //TODO this has to come from DB
			dto.setReferenceType(ReferenceTypeEEnum.ROLE.getCode());
			List<LoginPrivilegeMapping> privilegeMappings = roleAndPrivilegeDao.loadAddAndRemovedPrivileges(dto);
			if(privilegeMappings != null) {
				List<PrivilegeDTO> privilegeDTOs = new ArrayList<PrivilegeDTO>();
				for (LoginPrivilegeMapping privilegeMapping : privilegeMappings) {
					PrivilegeDTO prvDTO = new PrivilegeDTO();
					prvDTO.setAppTypeId(privilegeMapping.getLoginLeftMenu().getLoginTopMenu().getApplicationType());
					prvDTO.setAccessType(privilegeMapping.getLoginLeftMenu().getLoginTopMenu().getAccessType());
					prvDTO.setTopMenuId(privilegeMapping.getLoginLeftMenu().getLoginTopMenu().getTopMenuId());
					prvDTO.setTopMenuName(privilegeMapping.getLoginLeftMenu().getLoginTopMenu().getTopMenuName());
					prvDTO.setLeftMenuId(privilegeMapping.getLoginLeftMenu().getLeftMenuId());
					prvDTO.setLeftMenuName(privilegeMapping.getLoginLeftMenu().getLeftMenuName());
					prvDTO.setSubMenuId(privilegeMapping.getLoginSubMenu().getSubMenuId());
					prvDTO.setSubMenuName(privilegeMapping.getLoginSubMenu().getSubMenuName());
					prvDTO.setAddRemovalFlag(privilegeMapping.getAddRemovalFlag());
					privilegeDTOs.add(prvDTO);
				}
				
				if(CollectionUtils.isNotEmpty(privilegeDTOs)) {
					for (PrivilegeDTO postTypeDto : privilegeDTOs) {
						selectedPrivilegeForPostTypeMap.put(postTypeDto.getLeftMenuId() + "_" + postTypeDto.getSubMenuId(), postTypeDto.getAddRemovalFlag());
					}
				}
			}
			
			
		}
		
		// Load all selected privileges
		List<LoginPrivilegeMapping> privilegeMappings = roleAndPrivilegeDao.loadAddAndRemovedPrivileges(privilegeDTO);
		
		if(privilegeMappings != null) {
			List<PrivilegeDTO> privilegeDTOs = new ArrayList<PrivilegeDTO>();
			for (LoginPrivilegeMapping privilegeMapping : privilegeMappings) {
				PrivilegeDTO prvDTO = new PrivilegeDTO();
				prvDTO.setAppTypeId(privilegeMapping.getLoginLeftMenu().getLoginTopMenu().getApplicationType());
				prvDTO.setAccessType(privilegeMapping.getLoginLeftMenu().getLoginTopMenu().getAccessType());
				prvDTO.setTopMenuId(privilegeMapping.getLoginLeftMenu().getLoginTopMenu().getTopMenuId());
				prvDTO.setTopMenuName(privilegeMapping.getLoginLeftMenu().getLoginTopMenu().getTopMenuName());
				prvDTO.setLeftMenuId(privilegeMapping.getLoginLeftMenu().getLeftMenuId());
				prvDTO.setLeftMenuName(privilegeMapping.getLoginLeftMenu().getLeftMenuName());
				prvDTO.setSubMenuId(privilegeMapping.getLoginSubMenu().getSubMenuId());
				prvDTO.setSubMenuName(privilegeMapping.getLoginSubMenu().getSubMenuName());
				prvDTO.setAddRemovalFlag(privilegeMapping.getAddRemovalFlag());
				privilegeDTOs.add(prvDTO);
			}
			
			if(CollectionUtils.isNotEmpty(privilegeDTOs)) {
				selectedPrivilegeList = privilegeDTOs;
				
				for (PrivilegeDTO dto : selectedPrivilegeList) {
					selectedPrivilegeMap.put(dto.getLeftMenuId() + "_" + dto.getSubMenuId(), dto.getAddRemovalFlag());
				}
			}
		}
		
				
		// Load all possible privileges
		List<HashMap<String, Object>> privilegs = roleAndPrivilegeDao.loadAllPossiblePrivileges(privilegeDTO);
		
		if(privilegs != null) {
			List<PrivilegeDTO> privilegeDTOs = new ArrayList<PrivilegeDTO>();
			for (HashMap<String, Object> map : privilegs) {
				privilegeDTO = new PrivilegeDTO();
				privilegeDTO.setAppTypeId((Integer)map.get("appTypeId"));
				privilegeDTO.setTopMenuId((Integer)map.get("topMenuId"));
				privilegeDTO.setTopMenuName((String)map.get("topMenuName"));
				privilegeDTO.setLeftMenuId((Integer)map.get("leftMenuId"));
				privilegeDTO.setLeftMenuName((String)map.get("leftMenuName"));
				privilegeDTO.setSubMenuId((Integer)map.get("subMenuId"));
				privilegeDTO.setSubMenuName((String)map.get("subMenuName"));
				privilegeDTO.setVisibility((Integer)map.get("visibility"));
				privilegeDTO.setAccessType((Integer)map.get("accessType"));
				privilegeDTOs.add(privilegeDTO);
			}
			
			for ( PrivilegeDTO privilege : privilegeDTOs) {
				
				Integer appTypeId = privilege.getAppTypeId();
				Integer topMenuId = privilege.getTopMenuId();
				Integer leftMenuId = privilege.getLeftMenuId();
				Integer accessType = privilege.getAccessType();
				String key = accessType + "_" + appTypeId;
				//checking whether value is available in privilegeMap with key - appTypeId
				//If not available create the map with key as appTypeId and value as applicationTypeDTO
				ApplicationTypeDTO applicationTypeDTO = (ApplicationTypeDTO)privilegeMap.get(key);
				if ( applicationTypeDTO == null ) {
					applicationTypeDTO = new ApplicationTypeDTO();
					applicationTypeDTO.setAppTypeId(appTypeId);
					if(accessType.equals(AccessTypeEnum.WEB_ACCESS.getCode())) {
						applicationTypeDTO.setApplicationTypeName( ApplicationTypeEEnum.getApplicationType(appTypeId) );
					} 
					if (accessType.equals(AccessTypeEnum.DEVICE_ACCESS.getCode())) {
						applicationTypeDTO.setApplicationTypeName(DevAppTypeEnum.getApplicationType(appTypeId.toString()));
					}
					privilegeMap.put(key, applicationTypeDTO);
					privilegeAssignmentDTO.getApplicationTypeDTOList().add(applicationTypeDTO);
				}
				
				//checking whether value is available in privilegeMap with key - appTypeId+"_"+topMenuId
				//If not available create the map with key as appTypeId and value as topMenuDTO
				String topMenuKey = key + "_" + topMenuId;
				TopMenuDTO topMenuDTO = (TopMenuDTO)privilegeMap.get(topMenuKey);
				if ( topMenuDTO == null ) {
					topMenuDTO = new TopMenuDTO();
					topMenuDTO.setTopMenuId(topMenuId);
					topMenuDTO.setTopMenuName(privilege.getTopMenuName());
					topMenuDTO.setAccessType(accessType);
					applicationTypeDTO.getTopMenuList().add(topMenuDTO);
					
					privilegeMap.put(topMenuKey, topMenuDTO);
				}
				
				//checking whether value is available in privilegeMap with key - appTypeId+"_"+topMenuId+"_"+leftMenuId
				//If not available create the map with key as appTypeId and value as leftMenuDTO
				String leftMenuKey = key+"_"+topMenuId+"_"+leftMenuId;
				LeftMenuDTO leftMenuDTO = (LeftMenuDTO)privilegeMap.get(leftMenuKey);
				if ( leftMenuDTO == null ) {
					
					leftMenuDTO = new LeftMenuDTO();
					leftMenuDTO.setLeftMenuId(leftMenuId);
					leftMenuDTO.setLeftMenuName(privilege.getLeftMenuName());
					topMenuDTO.getLeftMenuList().add(leftMenuDTO);
					
					privilegeMap.put(leftMenuKey, leftMenuDTO);
				}
				
				//creating SubMenuDTO
				subMenuDTO = new SubMenuDTO();
				subMenuDTO.setSubMenuId(privilege.getSubMenuId());
				String subMenuName = privilege.getSubMenuName();
				subMenuDTO.setSubMenuName(subMenuName);
				
				//subMenuNames.add(subMenuName);
				//setting is applicable field- this field is used for checking whether submenu is applicable or not
				//based on this field we can enable/disable the checkboxes
				if(referenceType.equals(ReferenceTypeEEnum.USER.getCode())  
						&& selectedPrivilegeForPostTypeMap.get(leftMenuDTO.getLeftMenuId() + "_" + subMenuDTO.getSubMenuId()) != null) {
					subMenuDTO.setApplicable(true);
					if (selectedPrivilegeMap.get(leftMenuDTO.getLeftMenuId() + "_" + subMenuDTO.getSubMenuId()) == null 
							|| (selectedPrivilegeMap.get(leftMenuDTO.getLeftMenuId() + "_" + subMenuDTO.getSubMenuId()) != null 
							&& selectedPrivilegeMap.get(leftMenuDTO.getLeftMenuId() + "_" + subMenuDTO.getSubMenuId()).equals(AddRemovePrivilegeFlagEnum.ADDED.getCode()))) {
						subMenuDTO.setSelectedValue(leftMenuDTO.getLeftMenuId()+"_"+subMenuDTO.getSubMenuId());
					}
					
				} else if (referenceType.equals(ReferenceTypeEEnum.ROLE.getCode()) 
						&& privilege.getVisibility() != null) {
					subMenuDTO.setApplicable(true);
					if (selectedPrivilegeMap.get(leftMenuDTO.getLeftMenuId() + "_" + subMenuDTO.getSubMenuId()) != null 
							&& selectedPrivilegeMap.get(leftMenuDTO.getLeftMenuId() + "_" + subMenuDTO.getSubMenuId()).equals(AddRemovePrivilegeFlagEnum.ADDED.getCode())) {
						subMenuDTO.setSelectedValue(leftMenuDTO.getLeftMenuId()+"_"+subMenuDTO.getSubMenuId());
					}
				}
				
				//adding the subMenuDTO to the existing leftMenuDTO
				leftMenuDTO.getSubMenuList().add(subMenuDTO);
			
				}
			
			subMenuDTO = new SubMenuDTO();
			List<LoginSubMenu> subMenus = roleAndPrivilegeDao.searchSubMenus();
			
			if(subMenus != null) {
				List<SubMenuDTO> subMenuDTOs = new ArrayList<SubMenuDTO>();
				for (LoginSubMenu subMenu : subMenus) {
					subMenuDTO = new SubMenuDTO();
					subMenuDTO.setSubMenuId(subMenu.getSubMenuId());
					subMenuDTO.setSubMenuName(subMenu.getSubMenuName());
					subMenuDTOs.add(subMenuDTO);
				}
					for (SubMenuDTO dto : subMenuDTOs) {
						subMenuNames.add(dto.getSubMenuName());
					}
				}
			}
			
			privilegeAssignmentDTO.setPrivilegeNames(subMenuNames);
				
		return privilegeAssignmentDTO;
	}
	
	/**
	 * Prepare privilege details.
	 *
	 * @param appTypeList the app type list
	 * @return the list
	 */
	private List<String> preparePrivilegeDetails(List<ApplicationTypeDTO> appTypeList) {
		
		List<String> userPrivilegeList = new ArrayList<String>();
		
		for ( int appIndex = 0; appTypeList != null && appIndex < appTypeList.size(); appIndex++ ) {
			List<TopMenuDTO> topMenuList = appTypeList.get(appIndex).getTopMenuList();
			for ( int topIndex = 0; topMenuList!= null && topIndex < topMenuList.size(); topIndex++ ) {
				List<LeftMenuDTO> leftMenuList = topMenuList.get(topIndex).getLeftMenuList();
				for ( int leftIndex = 0; leftMenuList != null && leftIndex < leftMenuList.size(); leftMenuList.get(leftIndex++) ) {
					LeftMenuDTO leftMenuDTO = leftMenuList.get(leftIndex);
					List<SubMenuDTO> subMenuList = leftMenuDTO.getSubMenuList();
					for( int subMenuIndex = 0; subMenuList != null && subMenuIndex < subMenuList.size(); subMenuIndex++ ) {
						SubMenuDTO subMenuDTO = subMenuList.get(subMenuIndex);
						if ( null != subMenuDTO.getSelectedValue() && !"".equals(subMenuDTO.getSelectedValue()) ) {
							userPrivilegeList.add(subMenuDTO.getSelectedValue());
						}
					}
				}
			}
		}
		return userPrivilegeList;
	}


	/* (non-Javadoc)
	 * @see com.corum.service.RoleAndPrivilegeService#listRoleDetails()
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public List<RoleDto> listRoleDetails() {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
		WebUser user=webUserDao.getUser(userName);
		List<RoleDto> roleDTOList = new ArrayList<RoleDto>();
		if( user.getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_EOT_ADMIN)) {
			List<WebUserRole> roleList=roleAndPrivilegeDao.listRole();
			for(WebUserRole role: roleList){
				roleDTOList.add(convertLoginRoleTORoleDTO(role));
			}
		} else if (user.getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BANK_ADMIN)) {

			List<WebUserRole> roleList = roleAndPrivilegeDao.listRole(user);
			for (WebUserRole role : roleList) {
				roleDTOList.add(convertLoginRoleTORoleDTO(role));
			}
		
				}
		
		return roleDTOList;
	}

	/**
	 * Convert login role to role dto.
	 *
	 * @param role the role
	 * @return the role dto
	 */
	private RoleDto convertLoginRoleTORoleDTO(WebUserRole role) {
		RoleDto roleDTO=new RoleDto();
		roleDTO.setRoleName(role.getRoleName());
		roleDTO.setRoleDescription(role.getDescription());
		roleDTO.setRoleId(role.getRoleId());
		return roleDTO;
	}

	/* (non-Javadoc)
	 * @see com.corum.service.RoleAndPrivilegeService#getLoginRoleList()
	 */
	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public List<WebUserRole> getLoginRoleList() {
		return roleAndPrivilegeDao.getLoginRoleList();
	}
	
	/* (non-Javadoc)
	 * @see com.eot.banking.service.RoleAndPrivilegeService#getUsersByRoleId(java.lang.Integer)
	 */
	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public List<WebUser> getUsersByRoleId(Integer roleId) {
		return roleAndPrivilegeDao.getUsersByRoleId(roleId);
	}

}

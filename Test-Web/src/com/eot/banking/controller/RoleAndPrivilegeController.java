package com.eot.banking.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eot.banking.dto.AssignPrivilegeUIDTO;
import com.eot.banking.dto.PrivilegeAssignmentDTO;
import com.eot.banking.dto.RoleDto;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.service.RoleAndPrivilegeService;
import com.eot.banking.utils.ReferenceTypeEEnum;

/**
 * The Class RoleController.
 */
@Controller
public class RoleAndPrivilegeController extends PageViewController {
	
	/** The role and privilege service. */
	@Autowired
	private RoleAndPrivilegeService roleAndPrivilegeService;
	
	/**
	 * Show privilege assignment details.
	 *
	 * @param assignPrivilegeUIDTO the assign privilege uidto
	 * @param map the map
	 * @param request the request
	 * @param response the response
	 * @return the string
	 */
	@RequestMapping("/showPrivilegeDetails.htm")
	public String showPrivilegeAssignmentDetails(AssignPrivilegeUIDTO assignPrivilegeUIDTO, Map<String, Object> map, 
			HttpServletRequest request, HttpServletResponse response) {
		map.put("roleList",roleAndPrivilegeService.listRoleDetails());
		map.put("assignPrivilegeUIDTO", assignPrivilegeUIDTO);
		
		return "assignPrivilege";
	}
	
	/**
	 * List role details.
	 *
	 * @param model the model
	 * @param request the request
	 * @param response the response
	 * @return the string
	 * @throws NhanceApplicationException the corum application exception
	 */
	@RequestMapping("/viewRole.htm")
	public String listRoleDetails(Map<String,Object> model, 
			HttpServletRequest request, HttpServletResponse response) {
		try {
			List<RoleDto> roledtoList=roleAndPrivilegeService.listRoleDetails();
			model.put("roledtoList", roledtoList);
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			pageLogger(request,response,"ViewRole");
		}
		return "viewRole";
	}

	
	
	
	
	/**
	 * Load privilege assignment details.
	 *
	 * @param assignPrivilegeUIDTO the assign privilege uidto
	 * @param map the map
	 * @param request the request
	 * @param response the response
	 * @return the string
	 */
	@RequestMapping("/loadPrivilegeDetails.htm")
	public String loadPrivilegeAssignmentDetails(AssignPrivilegeUIDTO assignPrivilegeUIDTO, Map<String, Object> map, 
			HttpServletRequest request, HttpServletResponse response) {
		try {
			PrivilegeAssignmentDTO privilegeAssignmentDTO = new PrivilegeAssignmentDTO();
			if( StringUtils.isNotBlank(assignPrivilegeUIDTO.getUserId())  && !"select".equals(assignPrivilegeUIDTO.getUserId()) ) {
				privilegeAssignmentDTO.setReferenceType(ReferenceTypeEEnum.USER.getCode());
				privilegeAssignmentDTO.setReferenceId(assignPrivilegeUIDTO.getUserId());
			} else {
				privilegeAssignmentDTO.setReferenceType(ReferenceTypeEEnum.ROLE.getCode());
				privilegeAssignmentDTO.setReferenceId(assignPrivilegeUIDTO.getRoleId());
			}
			privilegeAssignmentDTO = roleAndPrivilegeService.showPrivilegeAssignmentDetails(privilegeAssignmentDTO);
			assignPrivilegeUIDTO.setApplicationTypeDTOList(privilegeAssignmentDTO.getApplicationTypeDTOList());
			assignPrivilegeUIDTO.setPrivilegeNames(privilegeAssignmentDTO.getPrivilegeNames());
			
			map.put("roleList",roleAndPrivilegeService.listRoleDetails());
			map.put("userList", roleAndPrivilegeService.getUsersByRoleId(Integer.parseInt(assignPrivilegeUIDTO.getRoleId())));
			map.put("assignPrivilegeUIDTO", assignPrivilegeUIDTO);
			
		} catch (EOTException e) {
			e.printStackTrace();
		} catch (Exception e) {
		}finally{
			pageLogger(request,response,"LoadPrivilegeDetails");
		}

		
		return "assignPrivilege";
	}
	
	/**
	 * Assign privilege details.
	 *
	 * @param assignPrivilegeUIDTO the assign privilege uidto
	 * @param map the map
	 * @param request the request
	 * @param response the response
	 * @return the string
	 */
	@RequestMapping("/assignPrivilegeDetails.htm")
	public String assignPrivilegeDetails(AssignPrivilegeUIDTO assignPrivilegeUIDTO,
			Map<String, Object> map, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			PrivilegeAssignmentDTO assignmentDTO = new PrivilegeAssignmentDTO();
			if( StringUtils.isNotBlank(assignPrivilegeUIDTO.getUserId())  && !"select".equals(assignPrivilegeUIDTO.getUserId()) ) {
				assignmentDTO.setReferenceType(ReferenceTypeEEnum.USER.getCode());
				assignmentDTO.setReferenceId(assignPrivilegeUIDTO.getUserId());
			} else {
				assignmentDTO.setReferenceType(ReferenceTypeEEnum.ROLE.getCode());
				assignmentDTO.setReferenceId(assignPrivilegeUIDTO.getRoleId());
			}
			assignmentDTO.setApplicationTypeDTOListSave(assignPrivilegeUIDTO.getApplicationTypeDTOList());
			roleAndPrivilegeService.savePrivilegeDetails(assignmentDTO);
			
			assignPrivilegeUIDTO.setApplicationTypeDTOList(assignmentDTO.getApplicationTypeDTOList());
			assignPrivilegeUIDTO.setPrivilegeNames(assignmentDTO.getPrivilegeNames());
			
			map.put("roleList",roleAndPrivilegeService.listRoleDetails());
			map.put("userList", roleAndPrivilegeService.getUsersByRoleId(Integer.parseInt(assignPrivilegeUIDTO.getRoleId())));
			map.put("message","ASSIGN_PRIVILEGE_SUCCESS");
			map.put("assignPrivilegeUIDTO", assignPrivilegeUIDTO);
		} catch (EOTException e) {
			e.printStackTrace();
		} catch (Exception e) {
		}finally{
			pageLogger(request,response,"AssignPrivilegeDetails");
		}
		
		return "assignPrivilege";
	}
	
	/**
	 * Gets the users by role id.
	 *
	 * @param roleId the role id
	 * @param model the model
	 * @param request the request
	 * @param response the response
	 * @return the users by role id
	 */
	@RequestMapping("/getUsersByRoleId.htm")
	public String getUsersByRoleId(@RequestParam Integer roleId, Map<String, Object> model,HttpServletRequest request,HttpServletResponse response){
		try {

			model.put("entity", "userId");
			model.put("id", "userName");
			model.put("value", "userName");
			model.put("list", roleAndPrivilegeService.getUsersByRoleId(roleId));
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
		  pageLogger(request,response,"getUsersByRole");
		}	
		return "combo";
	}

}

package com.eot.banking.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;

import com.eot.banking.dto.MobileDynamicMenuConfDTO;
import com.eot.banking.dto.MobileMasterMenuDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.service.MobileDynamicMenuService;
import com.eot.banking.service.OperatorService;
import com.eot.banking.service.TransactionRulesService;
import com.eot.entity.MobileMenuConfiguration;

@Controller
public class DynamicMenuController {
	@Autowired
	private MobileDynamicMenuService mobileDynamicMenuService;
	@Autowired
	private LocaleResolver localeResolver;
	@Autowired
	private OperatorService operatorService;
	@Autowired
	private TransactionRulesService transactionRulesService;

	@RequestMapping("/dynamicMenuConfigForm.htm")
	public String dynamicMenuConfForm(@RequestParam Integer appType, Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response, MobileDynamicMenuConfDTO dynamicMenuConfDTO) {
		String viewPage = null;
		try {
			if (appType.equals(0))
				viewPage = "customerMenuConfigForm";
			if (appType.equals(1))
				viewPage = "merchantMenuConfigForm";
			if (appType.equals(2))
				viewPage = "soleMerchantMenuConfigForm";

			model.put("dynamicMenuConfDTO", new MobileDynamicMenuConfDTO());
			model.put("appId", appType);
			model.put("tabList", mobileDynamicMenuService.getTabList(appType));
			model.put("bankList", operatorService.getBankList());
			model.put("menuList", new ArrayList<MobileMasterMenuDTO>());
			List<Map<String, Object>> list = mobileDynamicMenuService.getAllDynamicMenuConfigurationByBank(appType);
			model.put("menuConfigList", list);
			model.put("success_message", request.getParameter("message"));
			// model.put("customerProfileList",transactionRulesService.getCustomerProfilesByBankId(dynamicMenuConfDTO.getBankId()));
			int pageNumber = request.getParameter("pageNumber") == null ? 1
					: new Integer(request.getParameter("pageNumber"));
			// Page page = locationService.getCountriesList(pageNumber) ;
			// page.requestPage = "showCountries.htm";
			// model.put("page",page);
			model.put("lang", localeResolver.resolveLocale(request).toString());
		} catch (EOTException e) {
			e.printStackTrace();
			model.put("dynamicMenuConfDTO", new MobileDynamicMenuConfDTO());
			model.put("message", e.getErrorCode());
			return "";
		}

		catch (Exception ex) {
			ex.printStackTrace();
			model.put("message", ErrorConstants.SERVICE_ERROR);
		} finally {
			// pageLogger(request,response,"Country");
		}
		return viewPage;
	}
	
	@RequestMapping("/addConfiguration.htm")
	public String addConfiguration(@RequestParam Integer appType, Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response, MobileDynamicMenuConfDTO dynamicMenuConfDTO) {
		String viewPage = null;
		try {
			if (appType.equals(0))
				viewPage = "editCustomerMenuConfigForm";
			if (appType.equals(1))
				viewPage = "editMerchantMenuConfigForm";
			if (appType.equals(2))
				viewPage = "editSoleMerchantMenuConfigForm";

			model.put("dynamicMenuConfDTO", new MobileDynamicMenuConfDTO());
			model.put("appId", appType);
			model.put("tabList", mobileDynamicMenuService.getTabList(appType));
			model.put("bankList", operatorService.getBankList());
			model.put("menuList", new ArrayList<MobileMasterMenuDTO>());
			List<Map<String, Object>> list = mobileDynamicMenuService.getAllDynamicMenuConfigurationByBank(appType);
			model.put("menuConfigList", list);
			model.put("success_message", request.getParameter("message"));
			// model.put("customerProfileList",transactionRulesService.getCustomerProfilesByBankId(dynamicMenuConfDTO.getBankId()));
			int pageNumber = request.getParameter("pageNumber") == null ? 1
					: new Integer(request.getParameter("pageNumber"));
			// Page page = locationService.getCountriesList(pageNumber) ;
			// page.requestPage = "showCountries.htm";
			// model.put("page",page);
			model.put("lang", localeResolver.resolveLocale(request).toString());
		} catch (EOTException e) {
			e.printStackTrace();
			model.put("dynamicMenuConfDTO", new MobileDynamicMenuConfDTO());
			model.put("message", e.getErrorCode());
			return "";
		}

		catch (Exception ex) {
			ex.printStackTrace();
			model.put("message", ErrorConstants.SERVICE_ERROR);
		} finally {
			// pageLogger(request,response,"Country");
		}
		return viewPage;
	}

	@RequestMapping("/loadGridMenu.htm")
	public String loadGridMenu(@RequestParam Integer tabId, @RequestParam Integer appId, Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response) {

		try {

			model.put("id", "menuId");
			model.put("value", "menuName");
			model.put("entity", "tab-menu");
			model.put("functionalCode", "functionalCode");
			model.put("emptyMessage", "No menu list for this tab.");
			List list = mobileDynamicMenuService.getMenuList(tabId, appId);
			model.put("list", list);
			model.put("lang", localeResolver.resolveLocale(request).toString());
		} catch (Exception ex) {
			ex.printStackTrace();
			model.put("message", ErrorConstants.SERVICE_ERROR);
		} finally {
		}
		return "tabMenuList";
	}

	@RequestMapping("/saveDynamicMenuConfiguration.htm")
	public String saveDynamicMenuConfiguration(@RequestParam Integer appId, Map<String, Object> model, MobileDynamicMenuConfDTO dynamicMenuConfDTO, HttpServletRequest request, HttpServletResponse response) {
		//Integer appId = 2;
		String returnMessage = null;
		try {
			mobileDynamicMenuService.saveMobileMenuConfiguration(dynamicMenuConfDTO);
			appId = dynamicMenuConfDTO.getAppId();
			model.put("dynamicMenuConfDTO", new MobileDynamicMenuConfDTO());
			

			if (appId.equals(0))
				returnMessage = "&message=CUSTOMER_MENU_ADDED_SUCCESSFULLY";
			if (appId.equals(1))
				returnMessage = "&message=MERCHANT_MENU_ADDED_SUCCESSFULLY";
			if (appId.equals(2))
				returnMessage = "&message=SOLE_MENU_ADDED_SUCCESSFULLY";

		} catch (EOTException e) {
			returnMessage = "&message=" + e.getErrorCode();
			e.printStackTrace();
			model.put("dynamicMenuConfDTO", new MobileDynamicMenuConfDTO());
			model.put("message", e.getErrorCode());
			return "";
		} catch (Exception e) {
			returnMessage = "&message=" + ErrorConstants.SERVICE_ERROR;
			e.printStackTrace();
			model.put("message", ErrorConstants.SERVICE_ERROR);
		}

		return "redirect:/dynamicMenuConfigForm.htm?appType=" + appId+"&csrfToken="+request.getSession().getAttribute("csrfToken") + returnMessage;
		// return "";
	}
	
	@RequestMapping("/editDynamicMenuConfiguration.htm")
	public String editDynamicMenuConfiguration(@RequestParam Integer appId, Map<String, Object> model, MobileDynamicMenuConfDTO dynamicMenuConfDTO, HttpServletRequest request, HttpServletResponse response) {
		//Integer appId = 2;
		String returnMessage = null;
		try {
			mobileDynamicMenuService.saveMobileMenuConfiguration(dynamicMenuConfDTO);
			appId = dynamicMenuConfDTO.getAppId();
			model.put("dynamicMenuConfDTO", new MobileDynamicMenuConfDTO());
			if (appId.equals(0))
				returnMessage = "&message=CUSTOMER_MENU_UPDATED_SUCCESSFULLY";
			if (appId.equals(1))
				returnMessage = "&message=MERCHANT_MENU_UPDATED_SUCCESSFULLY";
			if (appId.equals(2))
				returnMessage = "&message=SOLE_MENU_UPDATED_SUCCESSFULLY";
		} catch (EOTException e) {
			returnMessage = "&message=" + e.getErrorCode();
			e.printStackTrace();
			model.put("dynamicMenuConfDTO", new MobileDynamicMenuConfDTO());
			model.put("message", e.getErrorCode());
			return "";
		} catch (Exception e) {
			returnMessage = "&message=" + ErrorConstants.SERVICE_ERROR;
			e.printStackTrace();
			model.put("message", ErrorConstants.SERVICE_ERROR);
		}
		

		return "redirect:/dynamicMenuConfigForm.htm?appType=" + appId+"&csrfToken="+request.getSession().getAttribute("csrfToken") + returnMessage;
		// return "";
	}

	@RequestMapping("/getCustomerProfilesByBank.htm")
	public String getCustomerProfiles(@RequestParam Integer bankId, Map<String, Object> model) {

		try {
			model.put("entity", "profileId");
			model.put("id", "profileId");
			model.put("value", "profileName");
			model.put("list", transactionRulesService.getCustomerProfilesByBankId(bankId));
		} catch (EOTException e) {
			e.printStackTrace();
			model.put("dynamicMenuConfDTO", new MobileDynamicMenuConfDTO());
			model.put("message", e.getErrorCode());
			return "";
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message", ErrorConstants.SERVICE_ERROR);
		}

		return "combo";
	}
	
	
	@RequestMapping("/loadMenuIcons.htm")
	public String loadMenuIcons(@RequestParam String functionalCode,Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response) {

		try {

			model.put("id", "iconId");
			model.put("value", "iconCode");
			model.put("blob", "iconImage");
			model.put("entity", "tab-menu-icon");
			model.put("emptyMessage", "No icon list for this tab.");
			List list = mobileDynamicMenuService.getMenuIconList(functionalCode);
			model.put("list", list);
			model.put("lang", localeResolver.resolveLocale(request).toString());
		} catch (Exception ex) {
			model.put("message", ErrorConstants.SERVICE_ERROR);
		} finally {
		}
		return "tabMenuIconList";
	}
	/*-- //@start Murari  Date:30/07/2018 purpose:cross site Scripting --*/
	@SuppressWarnings("unchecked")
	@RequestMapping("/loadConfigurationBybank.htm")
	public String loadConfigurationBybank(MobileDynamicMenuConfDTO dynamicMenuConfDTO, Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response) {
		String viewPage = null;
			if (dynamicMenuConfDTO.getAppId().equals(0))
				viewPage = "editCustomerMenuConfigForm";
			if (dynamicMenuConfDTO.getAppId().equals(1))
				viewPage = "editMerchantMenuConfigForm";
			if (dynamicMenuConfDTO.getAppId().equals(2))
				viewPage = "editSoleMerchantMenuConfigForm";
		try {
			
			model.put("appId", dynamicMenuConfDTO.getAppId());
			model.put("tabList", mobileDynamicMenuService.getTabList(dynamicMenuConfDTO.getAppId()));
			/******************* BANK DROPDOWN LIST ***********************/
			model.put("bankList", operatorService.getBankList());
			/******************* PROFILE DROPDOWN LIST ***********************/
			model.put("bankProfileList", transactionRulesService.getCustomerProfilesByBankId(dynamicMenuConfDTO.getBankId()));
			List<Map<String, Object>> allConfigList = mobileDynamicMenuService.getAllDynamicMenuConfigurationByBank(dynamicMenuConfDTO.getAppId());
			model.put("menuConfigList", allConfigList);
			
			Map<String, Object> editMenuConfiglist = mobileDynamicMenuService.getDynamicMenuConfigurationByBank(dynamicMenuConfDTO.getBankId(), dynamicMenuConfDTO.getProfileId(), dynamicMenuConfDTO.getAppId());
			model.put("editMenuConfiglist", editMenuConfiglist);
			int tabTypeId = 0;
			if (editMenuConfiglist.containsKey("tabList") == true) {
				List tabListSelected = (List) editMenuConfiglist.get("tabList");
				tabTypeId = (Integer) tabListSelected.get(0);
			}
			List menuList = mobileDynamicMenuService.getMenuList(tabTypeId, dynamicMenuConfDTO.getAppId());
			model.put("id", "menuId");
			model.put("value1", "menuName");
			model.put("entity", "tab-menu");
			model.put("imageId", "imageId");
			model.put("functionalCode", "functionalCode");
			model.put("emptyMessage", "No menu list for this tab.");
			model.put("menuList", menuList);
			model.put("lang", localeResolver.resolveLocale(request).toString());
			
			MobileDynamicMenuConfDTO mobileDynamicMenuConfig = new MobileDynamicMenuConfDTO();
			mobileDynamicMenuConfig.setBankId((Integer) editMenuConfiglist.get("bankId"));
			mobileDynamicMenuConfig.setProfileId((Integer) editMenuConfiglist.get("profileId"));
			mobileDynamicMenuConfig.setTabId(tabTypeId);
			mobileDynamicMenuConfig.setSelectedMenuIcon(String.valueOf(editMenuConfiglist.get("selectedDetails")));
			model.put("dynamicMenuConfDTO", mobileDynamicMenuConfig);
		} catch (Exception ex) {
			model.put("message", ErrorConstants.SERVICE_ERROR);
		} finally {
		}
		return viewPage;
	}
	/*--  @End--*/
	@SuppressWarnings("unchecked")
	@RequestMapping("/getConfigurationBybank.htm")
	public String getConfigurationBybank(@RequestParam Integer bankId,@RequestParam Integer profileId,@RequestParam Integer appId,Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response) {
		
try {
			
			model.put("appId", appId);
			model.put("tabList", mobileDynamicMenuService.getTabList(appId));
			/******************* BANK DROPDOWN LIST ***********************/
			model.put("bankList", operatorService.getBankList());
			/******************* PROFILE DROPDOWN LIST ***********************/
			model.put("bankProfileList", transactionRulesService.getCustomerProfilesByBankId(bankId));
			List<Map<String, Object>> allConfigList = mobileDynamicMenuService.getAllDynamicMenuConfigurationByBank(appId);
			model.put("menuConfigList", allConfigList);
			
			Map<String, Object> editMenuConfiglist = mobileDynamicMenuService.getDynamicMenuConfigurationByBank(bankId, profileId, appId);
			model.put("editMenuConfiglist", editMenuConfiglist);
			int tabTypeId = 0;
			if (editMenuConfiglist.containsKey("tabList") == true) {
				List tabListSelected = (List) editMenuConfiglist.get("tabList");
				model.put("selectedTabList", editMenuConfiglist.get("tabList"));
				tabTypeId = (Integer) tabListSelected.get(0);
			}
			System.out.println("==========" + tabTypeId);
			List menuList = mobileDynamicMenuService.getMenuList(tabTypeId, appId);
			model.put("id", "menuId");
			model.put("value1", "menuName");
			model.put("entity", "tab-menu");
			model.put("imageId", "imageId");
			model.put("functionalCode", "functionalCode");
			model.put("emptyMessage", "No menu list for this tab.");
			model.put("menuList", menuList);
			model.put("lang", localeResolver.resolveLocale(request).toString());
			
			MobileDynamicMenuConfDTO mobileDynamicMenuConfig = new MobileDynamicMenuConfDTO();
			mobileDynamicMenuConfig.setBankId((Integer) editMenuConfiglist.get("bankId"));
			if (editMenuConfiglist.get("profileId") != null) {
				mobileDynamicMenuConfig.setProfileId((Integer) editMenuConfiglist.get("profileId"));
			}
			mobileDynamicMenuConfig.setTabId(tabTypeId);
			mobileDynamicMenuConfig.setSelectedMenuIcon(String.valueOf(editMenuConfiglist.get("selectedDetails")));
			model.put("dynamicMenuConfDTO", mobileDynamicMenuConfig);
		} catch (Exception ex) {
			model.put("message", ErrorConstants.SERVICE_ERROR);
		} finally {
		}
		return "configurationPage";
	}
	
	

	@RequestMapping("/checkBankProfileExistance.htm")
	@ResponseBody
	public String checkBankProfileExistance(@RequestParam Integer bankId, @RequestParam Integer profileId, @RequestParam Integer appId, @RequestParam Integer tabId, Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response) {

		try {
			String list = mobileDynamicMenuService.checkBankProfileExistance(bankId, profileId, appId, tabId);
			return "Success";
		} catch (EOTException error) {
			return error.getErrorCode();
		} finally {
		}
	}


}

package com.eot.banking.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import com.eot.banking.dto.MobileDynamicMenuConfDTO;
import com.eot.banking.dto.ThemeConfDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.service.OperatorService;
import com.eot.banking.service.ThemeService;
import com.eot.banking.utils.Page;

@Controller
public class ThemeController extends PageViewController{
	
	@Autowired
	private OperatorService operatorService;
	
	@Autowired
	private ThemeService themeService;
	
	@Autowired
	private LocaleResolver localeResolver;
	
	@RequestMapping("/themeConfigForm.htm")
	public String getThemeConfig(@RequestParam Integer appType,Map<String, Object> model, 
			HttpServletRequest request, HttpServletResponse response, ThemeConfDTO themeConfDTO) {
		
		String viewPage = "configurations";
		themeConfDTO.setAppType(appType);
		//Setting default text color
		themeConfDTO.setTextColor("000000");
		Page page;
		try {
			page = themeService.getBanksWithThemes(1, themeConfDTO);
			model.put("page",page);	
			model.put("bankList", operatorService.getBankList());
			model.put("lang", localeResolver.resolveLocale(request).toString());
			model.put("appId", appType);
		} catch (EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
			return "";
		}catch (Exception ex) {
			ex.printStackTrace();
			model.put("message", ErrorConstants.SERVICE_ERROR);
		}

		return viewPage;
	}
	/*@Murari Date:02/08/2018 changed addthemeConfigForm.htm to addThemeConfigForm.htm purpose - bug 5788*/
	@RequestMapping("/addThemeConfigForm.htm")
	public String addThemeConfig(@RequestParam Integer appType,Map<String, Object> model, 
			HttpServletRequest request, HttpServletResponse response, ThemeConfDTO themeConfDTO) {
		
		String viewPage = "themeConfigForm";
		themeConfDTO.setAppType(appType);
		//Setting default text color
		themeConfDTO.setTextColor("000000");
		themeConfDTO.setAppType(appType);
		Page page;
		try {
			page = themeService.getBanksWithThemes(1, themeConfDTO);
			model.put("page",page);	
			model.put("bankList", operatorService.getBankList());
			model.put("lang", localeResolver.resolveLocale(request).toString());
			model.put("appId", appType);
		} catch (EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
			return "";
		}catch (Exception ex) {
			ex.printStackTrace();
			model.put("message", ErrorConstants.SERVICE_ERROR);
		}

		return viewPage;
	}

	
	@RequestMapping("/saveThemeConfig.htm")
	public String saveThemeConfig( ThemeConfDTO themeConfDTO,  BindingResult result,
			Map<String, Object> model, HttpServletRequest request, HttpServletResponse response
			) {
		String viewPage = "configurations";
		try {
			themeService.saveThemeConfig(themeConfDTO);
			themeConfDTO.setBankId(null);
			Page page=themeService.getBanksWithThemes(1, themeConfDTO);
			model.put("page",page);	
			model.put("message","ADD_THEME_SUCCESS");
			model.put("lang", localeResolver.resolveLocale(request).toString());
			// vineeth changes, on 008-08-2018, bug no:5863
			model.put("appId", themeConfDTO.getAppType());
			// changes over
		} catch (EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
			return "";
		}catch (Exception ex) {
			ex.printStackTrace();
			model.put("message", ErrorConstants.SERVICE_ERROR);
		}
		finally {
			model.put("bankList", operatorService.getBankList());
		}
		return viewPage;
	}
	
	@RequestMapping("/updateThemeConfig.htm")
	public String updateThemeConfig( ThemeConfDTO themeConfDTO,  BindingResult result,
			Map<String, Object> model, HttpServletRequest request, HttpServletResponse response
			) {
		//String viewPage = "themeConfigForm";
		  String viewPage = "configurations";
		
		try {
			themeService.saveThemeConfig(themeConfDTO);
			themeConfDTO.setBankId(null);
			Page page=themeService.getBanksWithThemes(1, themeConfDTO);
			model.put("page",page);	
			model.put("message","UPDATE_THEME_SUCCESS");
			model.put("lang", localeResolver.resolveLocale(request).toString());
			// vineeth changes, on 008-08-2018, bug no:5863
			model.put("appId", themeConfDTO.getAppType());
			// changes over
		} catch (EOTException e) {
			e.printStackTrace();
		}finally {
			model.put("bankList", operatorService.getBankList());
		}
		return viewPage;
	}
	/*@start Murari  Date:30/07/2018 purpose:cross site Scripting*/
	@RequestMapping("/viewThemeConfigForm.htm")
	public String viewAppliedTheme(Map<String, Object> model, 
			HttpServletRequest request, HttpServletResponse response, ThemeConfDTO themeConfDTO, BindingResult result) {
		
		String viewPage = "viewThemeConfigForm";
		try {
			ThemeConfDTO theme = themeService.getTheme(themeConfDTO.getBankId(), themeConfDTO.getAppType().toString());
			model.put("themeConfDTO",theme);	
			model.put("bankList", operatorService.getBankList());
			model.put("lang", localeResolver.resolveLocale(request).toString());
		} catch (EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
			return "";
		}catch (Exception ex) {
			ex.printStackTrace();
			model.put("message", ErrorConstants.SERVICE_ERROR);
		}

		return viewPage;
	}
	
	@RequestMapping("/editThemeConfigForm.htm")
	public String editAppliedTheme(Map<String, Object> model, 
			HttpServletRequest request, HttpServletResponse response, ThemeConfDTO themeConfDTO, BindingResult result) {
		
		String viewPage = "editThemeConfigForm";
		try {
			ThemeConfDTO theme = themeService.getTheme(themeConfDTO.getBankId(), themeConfDTO.getAppType().toString());
			theme.setBankId(themeConfDTO.getBankId());
			model.put("themeConfDTO",theme);	
			model.put("bankList", operatorService.getBankList());
			model.put("lang", localeResolver.resolveLocale(request).toString());
		} catch (EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
			return "";
		}catch (Exception ex) {
			ex.printStackTrace();
			model.put("message", ErrorConstants.SERVICE_ERROR);
		}

		return viewPage;
	}
	/*--End...*/
	@RequestMapping("/webColorConfigForm.htm")
	public String getWebColorConfig(Map<String, Object> model, 
			HttpServletRequest request, HttpServletResponse response, ThemeConfDTO themeConfDTO) {
		
		String viewPage = "webColorConfigForm";
		//Setting default text color
		themeConfDTO.setTextColor("000000");
		Page page;
		try {
			page = themeService.getBanksWithWebThemes(1, themeConfDTO);
			model.put("page",page);	
			model.put("bankList", operatorService.getBankList());
			model.put("lang", localeResolver.resolveLocale(request).toString());
		} catch (EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
			return "";
		}catch (Exception ex) {
			ex.printStackTrace();
			model.put("message", ErrorConstants.SERVICE_ERROR);
		}

		return viewPage;
	}
	
	@RequestMapping("/addWebColorConfigForm.htm")
	public String addtWebColorConfig(Map<String, Object> model, 
			HttpServletRequest request, HttpServletResponse response, ThemeConfDTO themeConfDTO) {
		
		String viewPage = "addWebColorConfigForm";
		//Setting default text color
		themeConfDTO.setTextColor("000000");
		Page page;
		try {
			page = themeService.getBanksWithWebThemes(1, themeConfDTO);
			model.put("page",page);	
			model.put("bankList", operatorService.getBankList());
			model.put("lang", localeResolver.resolveLocale(request).toString());
		} catch (EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
			return "";
		}catch (Exception ex) {
			ex.printStackTrace();
			model.put("message", ErrorConstants.SERVICE_ERROR);
		}

		return viewPage;
	}
	
	@RequestMapping("/saveWebColorConfig.htm")
	public String saveWebColorConfig( ThemeConfDTO themeConfDTO,  BindingResult result,
			Map<String, Object> model, HttpServletRequest request, HttpServletResponse response
			) {
		String viewPage = "webColorConfigForm";
		try {
			themeService.saveOrUpdateWebTheme(themeConfDTO);
			themeConfDTO.setBankId(null);
			Page page=themeService.getBanksWithWebThemes(1, themeConfDTO);
			model.put("page",page);	
			model.put("message","ADD_WEB_COLOR_SUCCESS");
			model.put("lang", localeResolver.resolveLocale(request).toString());
		} catch (EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
			return "";
		}catch (Exception ex) {
			ex.printStackTrace();
			model.put("message", ErrorConstants.SERVICE_ERROR);
		}
		finally {
			model.put("bankList", operatorService.getBankList());
		}
		return viewPage;
	}
	
	@RequestMapping("/updateWebColorConfig.htm")
	public String updateWebColorConfig( ThemeConfDTO themeConfDTO,  BindingResult result,
			Map<String, Object> model, HttpServletRequest request, HttpServletResponse response
			) {
		String viewPage = "webColorConfigForm";
		try {
			themeService.saveOrUpdateWebTheme(themeConfDTO);
			themeConfDTO.setBankId(null);
			Page page=themeService.getBanksWithWebThemes(1, themeConfDTO);
			model.put("page",page);	
			model.put("message","UPDATE_THEME_SUCCESS");
			model.put("lang", localeResolver.resolveLocale(request).toString());
		} catch (EOTException e) {
			e.printStackTrace();
		}finally {
			model.put("bankList", operatorService.getBankList());
		}
		return viewPage;
	}
	/*--@start Murari  Date:30/07/2018 purpose:cross site Scripting */
	@RequestMapping("/viewWebColorConfigForm.htm")
	public String viewAppliedWebTheme(Map<String, Object> model, 
			HttpServletRequest request, HttpServletResponse response, ThemeConfDTO themeConfDTO, BindingResult result) {
		
		String viewPage = "viewWebColorConfigForm";
		try {
			ThemeConfDTO theme = themeService.getWebTheme(themeConfDTO.getBankId());
			model.put("themeConfDTO",theme);	
			model.put("bankList", operatorService.getBankList());
			model.put("lang", localeResolver.resolveLocale(request).toString());
		} catch (EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
			return "";
		}catch (Exception ex) {
			ex.printStackTrace();
			model.put("message", ErrorConstants.SERVICE_ERROR);
		}

		return viewPage;
	}
	
	@RequestMapping("/editWebColorConfigForm.htm")
	public String editAppliedWebTheme(Map<String, Object> model, 
			HttpServletRequest request, HttpServletResponse response, ThemeConfDTO themeConfDTO, BindingResult result) {
		
		String viewPage = "editWebColorConfigForm";
		try {
			ThemeConfDTO theme = themeService.getWebTheme(themeConfDTO.getBankId());
			theme.setBankId(themeConfDTO.getBankId());
			model.put("themeConfDTO",theme);	
			model.put("bankList", operatorService.getBankList());
			model.put("lang", localeResolver.resolveLocale(request).toString());
		} catch (EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
			return "";
		}catch (Exception ex) {
			ex.printStackTrace();
			model.put("message", ErrorConstants.SERVICE_ERROR);
		}

		return viewPage;
	}
	/*End*/
}

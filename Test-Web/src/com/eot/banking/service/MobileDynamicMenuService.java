package com.eot.banking.service;

import java.io.EOFException;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestParam;

import com.eot.banking.dto.MobileDynamicMenuConfDTO;
import com.eot.banking.dto.MobileMasterMenuDTO;
import com.eot.banking.dto.MobileMasterMenuIconDTO;
import com.eot.banking.dto.MobileMenuDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.entity.MobileMenuConfiguration;

public interface MobileDynamicMenuService {
	
	MobileDynamicMenuConfDTO saveMobileMenuConfiguration(MobileDynamicMenuConfDTO dynamicMenuConfDTO) throws EOTException;
	List<MobileMasterMenuDTO> getTabList(Integer tabType) throws EOTException;
	List<MobileMasterMenuDTO> getMenuList(Integer tabId, Integer appId) throws EOTException;
	List<MobileMasterMenuIconDTO> getMenuIconList(String functionalCode) throws EOTException;
	Map<String, Object> getDynamicMenuConfigurationByBank(Integer bankId, Integer profileId, Integer appId)
			throws EOTException;
	List<Map<String, Object>> getAllDynamicMenuConfigurationByBank(Integer appId);
	String checkBankProfileExistance(Integer bankId, Integer profileId, Integer appId, Integer tabId) throws EOTException;
	
	
	

}

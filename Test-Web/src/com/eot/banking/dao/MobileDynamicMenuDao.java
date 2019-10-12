package com.eot.banking.dao;

import java.util.List;
import java.util.Map;

import com.eot.banking.dto.MobileDynamicMenuConfDTO;
import com.eot.entity.MobileMasterIcon;
import com.eot.entity.MobileMasterMenu;
import com.eot.entity.MobileMenuAppTypeMapping;
import com.eot.entity.MobileMenuConfiguration;

public interface MobileDynamicMenuDao extends BaseDao  {
	
	List<MobileMenuAppTypeMapping> getTabByAppType(int apptype);

	List<MobileMenuAppTypeMapping> getMenuByTabType(int apptype, int tabType);

	List<MobileMasterIcon> getMobileMenuIconsByFCNCode(String functionalCode);

	List<Object[]> getDynamicMenuConfiguration(Integer bankId, Integer profileId, Integer appId);

	List<Object[]> getAllDynamicMenuConfiguration(int appId);

	List<Object[]> getTabConfig(int tabId, MobileDynamicMenuConfDTO dynamicMenuConfDTO);

	List<Object[]> checkBankProfileExistance(Integer bankId, Integer profileId, Integer appId, Integer tabId);

}

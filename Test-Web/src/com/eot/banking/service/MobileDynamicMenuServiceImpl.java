package com.eot.banking.service;

import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eot.banking.dao.MobileDynamicMenuDao;
import com.eot.banking.dto.MobileDynamicMenuConfDTO;
import com.eot.banking.dto.MobileMasterMenuDTO;
import com.eot.banking.dto.MobileMasterMenuIconDTO;
import com.eot.banking.dto.MobileMenuDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.entity.MobileMasterIcon;
import com.eot.entity.MobileMasterMenu;
import com.eot.entity.MobileMenuAppType;
import com.eot.entity.MobileMenuAppTypeMapping;
import com.eot.entity.MobileMenuConfiguration;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@Service("mobileDynanicMenu")
@Transactional(readOnly = false)
public class MobileDynamicMenuServiceImpl implements MobileDynamicMenuService {
	@Autowired
	private MobileDynamicMenuDao mobileDynamicMenuDao;

	@Override
	public MobileDynamicMenuConfDTO saveMobileMenuConfiguration(MobileDynamicMenuConfDTO dynamicMenuConfDTO)
			throws EOTException {

		List<MobileMenuDTO> mobileMenus = dynamicMenuConfDTO.getMenuList();
		String selectedMenuIcon = dynamicMenuConfDTO.getSelectedMenuIcon();
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(selectedMenuIcon);
			JSONArray jsonArray = (JSONArray) obj;
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonSingleObject = (JSONObject) jsonArray.get(i);

				int tabId = Integer.parseInt(jsonSingleObject.get("tabId").toString());
				try {
					List list = mobileDynamicMenuDao.getTabConfig(tabId, dynamicMenuConfDTO);

					if (list.size() == 0) {
						MobileMenuConfiguration dynamicMenuTabConfiguration = new MobileMenuConfiguration();
						dynamicMenuTabConfiguration.setBankId(dynamicMenuConfDTO.getBankId());
						dynamicMenuTabConfiguration.setProfileId(dynamicMenuConfDTO.getProfileId());
						dynamicMenuTabConfiguration.setTabTypeId(tabId);

						MobileMenuAppType appType = new MobileMenuAppType();
						appType.setAppTypeId(dynamicMenuConfDTO.getAppId());
						dynamicMenuTabConfiguration.setMobilemenuapptype(appType);

						MobileMasterMenu mobileMasterMenu = new MobileMasterMenu();
						mobileMasterMenu.setMmId(tabId);
						dynamicMenuTabConfiguration.setMobileMasterMenu(mobileMasterMenu);
						/*Added by Pavan 03-07-2018 Status for newly added Menu list*/
						dynamicMenuTabConfiguration.setStatus(10);

						mobileDynamicMenuDao.save(dynamicMenuTabConfiguration);
					}

				} catch (Exception e) {
					e.printStackTrace();

				}

				List<Map<String, Object>> jsonSelectedMenuArray = (List<Map<String, Object>>) jsonSingleObject
						.get("tabMenuIconList");
				for (int j = 0; j < jsonSelectedMenuArray.size(); j++) {
					JSONObject jsonMenuListObject = (JSONObject) jsonSelectedMenuArray.get(j);

					MobileMenuConfiguration dynamicMenuConfiguration = new MobileMenuConfiguration();

					dynamicMenuConfiguration.setBankId(dynamicMenuConfDTO.getBankId());
					dynamicMenuConfiguration.setProfileId(dynamicMenuConfDTO.getProfileId());
					dynamicMenuConfiguration.setTabTypeId(tabId);
					dynamicMenuConfiguration.setStatus(Integer.parseInt(jsonMenuListObject.get("status").toString()));
					MobileMasterMenu mobileMasterMenu = new MobileMasterMenu();
					MobileMasterIcon mobileMenuIcon = new MobileMasterIcon();

					mobileMasterMenu.setMmId(Integer.parseInt(jsonMenuListObject.get("menuId").toString()));
					mobileMenuIcon.setMmiId(Integer.parseInt(jsonMenuListObject.get("iconId").toString()));

					MobileMenuAppType appType = new MobileMenuAppType();
					appType.setAppTypeId(dynamicMenuConfDTO.getAppId());
					dynamicMenuConfiguration.setMobilemenuapptype(appType);

					dynamicMenuConfiguration.setMobileMasterMenu(mobileMasterMenu);
					dynamicMenuConfiguration.setMobileMasterIcon(mobileMenuIcon);
					dynamicMenuConfiguration.setMobileIndex(i + 1);
					// System.out.println(menuIcon[2]);
					if (jsonMenuListObject.get("dbId") != null && jsonMenuListObject.get("dbId") != "") {
						dynamicMenuConfiguration.setMmcId(Integer.parseInt(jsonMenuListObject.get("dbId").toString()));
						mobileDynamicMenuDao.merge(dynamicMenuConfiguration);
					} else {
						mobileDynamicMenuDao.save(dynamicMenuConfiguration);
					}
				}
			}
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return dynamicMenuConfDTO;
	}

	@Override
	public List<MobileMasterMenuDTO> getTabList(Integer apptype) throws EOTException {

		List<MobileMenuAppTypeMapping> masterMenu = mobileDynamicMenuDao.getTabByAppType(apptype);

		// if(masterMenu.size()==0)
		// throw new EOTException("");//
		List<MobileMasterMenuDTO> tabList = new ArrayList<MobileMasterMenuDTO>();
		MobileMasterMenuDTO menuDTO = null;

		if (CollectionUtils.isNotEmpty(masterMenu)) {
			for (MobileMenuAppTypeMapping menu : masterMenu) {
				menuDTO = new MobileMasterMenuDTO();
				menuDTO.setMenuId(menu.getMobilemastermenu().getMmId());
				menuDTO.setMenuName(menu.getMobilemastermenu().getMmName());
				tabList.add(menuDTO);
			}
		}

		return tabList;
	}

	@Override
	public List<MobileMasterMenuDTO> getMenuList(Integer tabId, Integer appId) throws EOTException {

		List<MobileMenuAppTypeMapping> mobileAppTypeMapping = mobileDynamicMenuDao.getMenuByTabType(appId, tabId);

		// if(masterMenu.size()==0)
		// throw new EOTException("");//
		List<MobileMasterMenuDTO> tabList = new ArrayList<MobileMasterMenuDTO>();
		MobileMasterMenuDTO menuDTO = null;

		if (CollectionUtils.isNotEmpty(mobileAppTypeMapping)) {
			for (MobileMenuAppTypeMapping menu : mobileAppTypeMapping) {
				menuDTO = new MobileMasterMenuDTO();
				menuDTO.setMenuId(menu.getMobilemastermenu().getMmId());
				menuDTO.setMenuName(menu.getMobilemastermenu().getMmName());
				menuDTO.setFunctionalCode(menu.getMobilemastermenu().getFunctionalCode());
				tabList.add(menuDTO);
			}
		}

		return tabList;

	}

	@Override
	public List<MobileMasterMenuIconDTO> getMenuIconList(String functionalCode) throws EOTException {

		List<MobileMasterIcon> masterMenu = mobileDynamicMenuDao.getMobileMenuIconsByFCNCode(functionalCode);

		// if(masterMenu.size()==0)
		// throw new EOTException("");//
		List<MobileMasterMenuIconDTO> menuIconList = new ArrayList<MobileMasterMenuIconDTO>();
		MobileMasterMenuIconDTO menuDTO = null;

		if (CollectionUtils.isNotEmpty(masterMenu)) {
			for (MobileMasterIcon menu : masterMenu) {
				menuDTO = new MobileMasterMenuIconDTO();
				menuDTO.setIconId(menu.getMmiId());
				menuDTO.setFunctionalCode(menu.getFunctionalCode());
				menuDTO.setIconCode(menu.getIconCode());

				try {
					if (null != menu.getIconImage()) {
						byte[] entityLogo = menu.getIconImage().getBytes(1, (int) menu.getIconImage().length());
						byte[] encodeBase64 = Base64.encodeBase64(entityLogo);
						String base64Encoded = new String(encodeBase64, "UTF-8");
						menuDTO.setIconImage(base64Encoded);
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				menuIconList.add(menuDTO);
			}
		}

		return menuIconList;

	}

	@Override
	public Map<String, Object> getDynamicMenuConfigurationByBank(Integer bankId, Integer profileId, Integer appId)
			throws EOTException {

		List<Object[]> masterMenu = mobileDynamicMenuDao.getDynamicMenuConfiguration(bankId, profileId, appId);
		// List<Integer> menuList = new ArrayList<Integer>();
		// List<Map<Object, Object>> menuIconList = new ArrayList<Map<Object,
		// Object>>();
		// StringBuilder selectedMenuIconList = new StringBuilder();

		List<Map<String, Object>> selectedDetails = new ArrayList<Map<String, Object>>();
		// List<Map<String, Object>> tabMenuList = new ArrayList<Map<String, Object>>();
		List<Integer> tabList = new ArrayList<Integer>();

		/************* INDEX IN QUERY ***************/
		// mmcId = 0
		// mmId = 1
		// bankId = 2
		// profileId = 3
		// tabTypeId = 4
		// mmiId = 5
		// iconImage = 6
		// status = 7
		// menuName = 8
		Map<String, Object> otherDetail = new HashMap<String, Object>();
	//	Object[] firstObject = masterMenu.get(0);
	// vineeth change, 20-07-2018, 	
		Object[] firstObject;
		if(masterMenu.size()>0) {
			 firstObject = masterMenu.get(0);
			otherDetail.put("bankId", firstObject[2]);
			otherDetail.put("profileId", firstObject[3]);
			
			for (int i = 0; i < masterMenu.size(); i++) {
				Object[] objects = masterMenu.get(i);
				if (objects[1] != objects[4]) {
					int tabId = (Integer) objects[4];
					List<Map<String, Object>> tabMenuList = new ArrayList<Map<String, Object>>();
					for (int j = 0; j < masterMenu.size(); j++) {
						Object[] multiTabValueLoop = masterMenu.get(j);
						if (multiTabValueLoop[1] != multiTabValueLoop[4]) {
							Map<String, Object> menuIconListMap = new HashMap<String, Object>();
							int checkTab = (Integer) multiTabValueLoop[4];
							if (tabId == checkTab) {
								menuIconListMap.put("menuId", multiTabValueLoop[1]);
								menuIconListMap.put("iconId", multiTabValueLoop[5]);
								menuIconListMap.put("menuName", multiTabValueLoop[8]);
								menuIconListMap.put("iconImage", "");
								menuIconListMap.put("dbId", multiTabValueLoop[0]);
								menuIconListMap.put("status", multiTabValueLoop[7]);

								try {
									if (null != multiTabValueLoop[6]) {
										Blob image = (Blob) multiTabValueLoop[6];
										byte[] entityLogo = image.getBytes(1, (int) image.length());
										byte[] encodeBase64 = Base64.encodeBase64(entityLogo);
										String base64Encoded = new String(encodeBase64, "UTF-8");
										menuIconListMap.put("iconImage", "data:image/png;base64," + base64Encoded);
									}
								} catch (UnsupportedEncodingException e) {
									e.printStackTrace();
								} catch (SQLException e) {
									e.printStackTrace();
								}
								tabMenuList.add(menuIconListMap);
							}
						}
					}
					if (!tabList.contains(tabId)) {
						tabList.add(tabId);
						Map<String, Object> selectedDetailsMap = new HashMap<String, Object>();
						selectedDetailsMap.put("tabId", tabId);
						selectedDetailsMap.put("tabMenuIconList", tabMenuList);
						selectedDetails.add(selectedDetailsMap);
					}
				}
			}
			otherDetail.put("tabList", tabList);			
		}else {
			otherDetail.put("bankId", bankId);
			otherDetail.put("profileId", profileId);
		}
		
		/*otherDetail.put("bankId", firstObject[2]);
		otherDetail.put("profileId", firstObject[3]);*/


  /*		for (int i = 0; i < masterMenu.size(); i++) {
			Object[] objects = masterMenu.get(i);
			if (objects[1] != objects[4]) {
				int tabId = (Integer) objects[4];
				List<Map<String, Object>> tabMenuList = new ArrayList<Map<String, Object>>();
				for (int j = 0; j < masterMenu.size(); j++) {
					Object[] multiTabValueLoop = masterMenu.get(j);
					if (multiTabValueLoop[1] != multiTabValueLoop[4]) {
						Map<String, Object> menuIconListMap = new HashMap<String, Object>();
						int checkTab = (Integer) multiTabValueLoop[4];
						if (tabId == checkTab) {
							menuIconListMap.put("menuId", multiTabValueLoop[1]);
							menuIconListMap.put("iconId", multiTabValueLoop[5]);
							menuIconListMap.put("menuName", multiTabValueLoop[8]);
							menuIconListMap.put("iconImage", "");
							menuIconListMap.put("dbId", multiTabValueLoop[0]);
							menuIconListMap.put("status", multiTabValueLoop[7]);

							try {
								if (null != multiTabValueLoop[6]) {
									Blob image = (Blob) multiTabValueLoop[6];
									byte[] entityLogo = image.getBytes(1, (int) image.length());
									byte[] encodeBase64 = Base64.encodeBase64(entityLogo);
									String base64Encoded = new String(encodeBase64, "UTF-8");
									menuIconListMap.put("iconImage", "data:image/png;base64," + base64Encoded);
								}
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							} catch (SQLException e) {
								e.printStackTrace();
							}
							tabMenuList.add(menuIconListMap);
						}
					}
				}
				if (!tabList.contains(tabId)) {
					tabList.add(tabId);
					Map<String, Object> selectedDetailsMap = new HashMap<String, Object>();
					selectedDetailsMap.put("tabId", tabId);
					selectedDetailsMap.put("tabMenuIconList", tabMenuList);
					selectedDetails.add(selectedDetailsMap);
				}
			}
		//	otherDetail.put("tabList", tabList);
		}

		otherDetail.put("tabList", tabList);*/
		// vineeth change over 
		otherDetail.put("selectedDetails", JSONValue.toJSONString(selectedDetails));
		return otherDetail;


	}

	@Override
	public List<Map<String, Object>> getAllDynamicMenuConfigurationByBank(Integer appId) {
		List<Object[]> masterMenu = mobileDynamicMenuDao.getAllDynamicMenuConfiguration(appId);

		List<Map<String, Object>> responseList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < masterMenu.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			Object[] objects = masterMenu.get(i);
			map.put("bankName", objects[0]);
			map.put("bankId", objects[1]);
			map.put("mmcId", objects[2]);
			map.put("profileName", objects[3]);
			map.put("profileId", objects[4]);
			responseList.add(map);
		}
		return responseList;
	}

	@Override
	public String checkBankProfileExistance(Integer bankId, Integer profileId, Integer appId, Integer tabId) throws EOTException {
		List<Object[]> masterMenu = mobileDynamicMenuDao.checkBankProfileExistance(bankId, profileId, appId, tabId);
		
		Object count = masterMenu.get(0);
		
		if (Integer.parseInt(count.toString()) > 0) {
			if (profileId == null) {
				throw new EOTException(ErrorConstants.BANK_CONFIG_ALREADY_EXISTS);
			} else {
				throw new EOTException(ErrorConstants.BANK_PROFILE_CONFIG_ALREADY_EXISTS);
			}
			
		} else {
			return masterMenu.toString();
		}
		
	}

}

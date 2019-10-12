package com.eot.banking.dao.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.eot.banking.common.EOTConstants;
import com.eot.banking.dao.MobileDynamicMenuDao;
import com.eot.banking.dto.MobileDynamicMenuConfDTO;
import com.eot.banking.dto.MobileMenuDTO;
import com.eot.entity.Merchant;
import com.eot.entity.MobileMasterIcon;
import com.eot.entity.MobileMasterMenu;
import com.eot.entity.MobileMenuAppTypeMapping;
import com.eot.entity.MobileMenuConfiguration;

@Repository("mobileDynamicMenuDao")
public class MobileDynamicMenuDaoImpl extends BankDaoImpl implements MobileDynamicMenuDao {
	
	@Override
	public List<MobileMenuAppTypeMapping> getTabByAppType(int apptype) {
		//@Start, Optimized query, by Murari, dated : 23-07-2018
				Query query = getSessionFactory().getCurrentSession().createQuery("from MobileMenuAppTypeMapping  where  mobilemastermenu.mmTypeId=:mmTypeId and  mobilemenuapptype.appTypeId=:appTypeId")
						.setParameter("mmTypeId", EOTConstants.MOBILE_MENU_TAB)
						.setParameter("appTypeId", apptype);
				//@End
		/*Query query = getSessionFactory().getCurrentSession().createQuery("from MobileMenuAppTypeMapping  where  mobilemastermenu.mmTypeId="
				+ EOTConstants.MOBILE_MENU_TAB + " and  mobilemenuapptype.appTypeId=" + apptype);*/
		List<MobileMenuAppTypeMapping> list = query.list();
		return CollectionUtils.isNotEmpty(list) ? list : new ArrayList<MobileMenuAppTypeMapping>();
	}
	@Override
	public List<MobileMasterIcon> getMobileMenuIconsByFCNCode(String functionalCode) {
		//@Start, Optimized query, by Murari, dated : 23-07-2018
		Query query=getSessionFactory().getCurrentSession().createQuery("from MobileMasterIcon  where  functionalCode=:functionalCode").setParameter("functionalCode", functionalCode);
		//@End
		/*Query query=getSessionFactory().getCurrentSession().createQuery("from MobileMasterIcon  where  functionalCode='"+functionalCode+"'");*/
		List<MobileMasterIcon> list = query.list();
		return CollectionUtils.isNotEmpty(list) ? list : new ArrayList<MobileMasterIcon>();
	}
	
	@Override
	public List<MobileMenuAppTypeMapping> getMenuByTabType(int apptype,int tabType) {
		//@Start, Optimized query, by Murari, dated : 23-07-2018
		Query query=getSessionFactory().getCurrentSession().createQuery("from MobileMenuAppTypeMapping  where  mobilemastermenu.mmTypeId=:mmTypeId and  mobilemenuapptype.appTypeId=:apptype and mobilemastermenu.mmTabType=:tabType")
				.setParameter("mmTypeId", EOTConstants.MOBILE_MENU_GRID)
				.setParameter("apptype", apptype)
				.setParameter("tabType", tabType);
		//@End
		/*Query query=getSessionFactory().getCurrentSession().createQuery("from MobileMenuAppTypeMapping  where  mobilemastermenu.mmTypeId="+EOTConstants.MOBILE_MENU_GRID+" and  mobilemenuapptype.appTypeId="+apptype+" and mobilemastermenu.mmTabType="+tabType);*/
		List<MobileMenuAppTypeMapping> list = query.list();
		return CollectionUtils.isNotEmpty(list) ? list : new ArrayList<MobileMenuAppTypeMapping>();
	}
	@Override
	public List<Object[]> getDynamicMenuConfiguration(Integer bankId,Integer profileId,Integer appId) {	
		/*Query query=getSessionFactory().getCurrentSession().createQuery("Select mmc.mmcId, mmc.mobileMasterMenu.mmId, mmc.bankId, mmc.profileId, mmc.tabTypeId, mmi.mmiId, mmi.iconImage, mmc.status, mmm.mmName from MobileMenuConfiguration mmc, MobileMasterIcon mmi, MobileMasterMenu mmm  where mmi.mmiId = mmc.mobileMasterIcon.mmiId and mmm.mmId = mmc.mobileMasterMenu.mmId and mmc.bankId="+bankId+" and  mmc.profileId="+profileId+" and mmc.mobilemenuapptype.appTypeId="+appId+" and mmc.status = 10");*/
		Query query=getSessionFactory().getCurrentSession().createQuery("Select mmc.mmcId, mmc.mobileMasterMenu.mmId, mmc.bankId, mmc.profileId, mmc.tabTypeId, mmi.mmiId, mmi.iconImage, mmc.status, mmm.mmName from MobileMenuConfiguration mmc, MobileMasterIcon mmi, MobileMasterMenu mmm  where mmi.mmiId = mmc.mobileMasterIcon.mmiId and mmm.mmId = mmc.mobileMasterMenu.mmId and mmc.bankId="+bankId+" and  mmc.profileId="+profileId+" and mmc.mobilemenuapptype.appTypeId="+appId+" and mmc.status =:status").setParameter("status", 10);
		List list = query.list();
		return CollectionUtils.isNotEmpty(list) ? list : new ArrayList();
	}
	@Override
	public List<Object[]> getAllDynamicMenuConfiguration(int appId) {
//		Query query=getSessionFactory().getCurrentSession().createQuery("Select b.bankName, mmc.bankId, mmc.mmcId, cp.profileName, mmc.profileId from MobileMenuConfiguration mmc, Bank b, CustomerProfiles cp where b.bankId = mmc.bankId and cp.profileId=mmc.profileId and mmc.mobilemenuapptype.appTypeId="+appId + " group by mmc.bankId, mmc.profileId");
		Query query = getSessionFactory().getCurrentSession().createSQLQuery("select b.BankName as bankName, MMC.BANK_ID as bankId, MMC.MMC_ID as mmcId, cp.ProfileName as profileName, MMC.PROFILE_ID as profileId " + 
				"from MobileMenuConfiguration as MMC INNER JOIN Bank as b ON b.BankID = MMC.BANK_ID LEFT JOIN CustomerProfiles as cp ON cp.ProfileID = MMC.PROFILE_ID " + 
				"where MMC.APP_TYPE= " + appId + " OR MMC.PROFILE_ID is null " +
				"group by MMC.BANK_ID , MMC.PROFILE_ID ");
		List list = query.list();
		return CollectionUtils.isNotEmpty(list) ? list : new ArrayList();
	}

	@Override
	public List<Object[]> getTabConfig(int tabId, MobileDynamicMenuConfDTO dynamicMenuConfDTO) {

		Query query= getSessionFactory().getCurrentSession().createQuery("from MobileMenuConfiguration  where  bankId="+dynamicMenuConfDTO.getBankId()+" and  profileId="+dynamicMenuConfDTO.getProfileId()+" and tabTypeId="+ tabId +" and mobilemenuapptype="+dynamicMenuConfDTO.getAppId());
		List list = query.list();
		return CollectionUtils.isNotEmpty(list) ? list : new ArrayList();
	}

	@Override
	public List<Object[]> checkBankProfileExistance(Integer bankId, Integer profileId, Integer appId, Integer tabId) {
		Query query= getSessionFactory().getCurrentSession().createQuery("SELECT count(*) as count FROM MobileMenuConfiguration  where  bankId="+bankId+" and  profileId="+profileId +" and mobilemenuapptype.appTypeId="+ appId +" and tabTypeId=" + tabId);
		List list = query.list();
		return CollectionUtils.isNotEmpty(list) ? list : new ArrayList();
	}
}

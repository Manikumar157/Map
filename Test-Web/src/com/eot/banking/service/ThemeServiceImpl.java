package com.eot.banking.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eot.banking.dao.BankDao;
import com.eot.banking.dao.BrandingDao;
import com.eot.banking.dao.ThemeDao;
import com.eot.banking.dto.ThemeConfDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.utils.Page;
import com.eot.entity.Branding;
import com.eot.entity.Mobiletheamcolorconfig;

@Service("themeService")
@Transactional(readOnly=false)
public class ThemeServiceImpl implements ThemeService {
	
	@Autowired 
	ThemeDao themeDao;
	
	@Autowired 
	BrandingDao brandingDao;
	
	@Autowired 
	BankDao bankDao;
	
	public void setThemeDao(ThemeDao themeDao) {
		this.themeDao = themeDao;
	}
	
	public void setBrandingDao(BrandingDao brandingDao) {
		this.brandingDao = brandingDao;
	}

	public void setBankDao(BankDao bankDao) {
		this.bankDao = bankDao;
	}

	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=Throwable.class )
	public void saveThemeConfig(ThemeConfDTO themeConfDTO) throws EOTException {
		saveOrUpdateMobileTheme(themeConfDTO);
		//saveOrUpdateWebTheme(themeConfDTO);
		
	}

	@Override
	public void saveOrUpdateWebTheme(ThemeConfDTO themeConfDTO) throws EOTException {
		Branding webThemeEntity = themeDao.getUniqueWebTheme(themeConfDTO.getBankId());
		
		if (null == webThemeEntity) {
			Branding branding = new Branding();
			branding.setCreatedDate(new Date());
			setBranding(themeConfDTO, branding);
			themeDao.save(branding);
		} else {
			setBranding(themeConfDTO, webThemeEntity);
			themeDao.update(webThemeEntity);
		}
	}

	private void saveOrUpdateMobileTheme(ThemeConfDTO themeConfDTO) {
		Mobiletheamcolorconfig mobileThemeEntity = themeDao.getUniqueMobileTheme(themeConfDTO.getBankId(), themeConfDTO.getAppType().toString());
		if (null==mobileThemeEntity) {
			Mobiletheamcolorconfig theme= new Mobiletheamcolorconfig();
			theme.setCreatedDate(new Date());
			setMobileThemeColorConfig(themeConfDTO, theme);
			themeDao.save(theme);
		}else{
			setMobileThemeColorConfig(themeConfDTO, mobileThemeEntity);
			themeDao.update(mobileThemeEntity);
		}
	}

	private void setMobileThemeColorConfig(ThemeConfDTO themeConfDTO, Mobiletheamcolorconfig theme) {
		
		theme.setBank(bankDao.getBank(themeConfDTO.getBankId()));
		theme.setToolBarColor(themeConfDTO.getToolBarColor());
		theme.setScreenBgColor(themeConfDTO.getScreenBackgroundColor());
		theme.setTabColorSelected(themeConfDTO.getTabColorSelected());
		theme.setTabColorUnselected(themeConfDTO.getTabColorUnselected());
		theme.setGridColorSelected(themeConfDTO.getGridColorSelected());
		theme.setGridColorUnselected(themeConfDTO.getGridColorUnselected());
		theme.setListHeaderColor(themeConfDTO.getListHeaderColor());
		theme.setAppTypeId(themeConfDTO.getAppType().toString());
		theme.setTextColor(themeConfDTO.getTextColor());
		theme.setUpdatedDate(new Date());
	}
	
	private void setBranding(ThemeConfDTO themeConfDTO, Branding branding) {
	
		branding.setBank(bankDao.getBank(themeConfDTO.getBankId()));
		branding.setBankThemeColor(themeConfDTO.getBankTheme());
		branding.setButtonColor(themeConfDTO.getBtnColorName());
		branding.setMenuColor(themeConfDTO.getMenuColorName());
		branding.setSubMenuColor(themeConfDTO.getSubMenuColorName());
		try {	
			if(null != themeConfDTO.getLogoImg() && themeConfDTO.getLogoImg().getSize() > 0){
				branding.setLogo(Hibernate.createBlob(themeConfDTO.getLogoImg().getBytes()));
			}
		}
		 catch (IOException e) {
			e.printStackTrace();
		}
		branding.setUpdatedDate(new Date());
	}

	@Override
	public Page getBanksWithThemes(int pageNumber, ThemeConfDTO themeDTO) throws EOTException {

		Page page = themeDao.getBanksWithThemes(pageNumber, themeDTO);
		
		/*if(page.results.size() == 0){
			throw new EOTException(ErrorConstants.THEME_NOT_EXISTS);
		}
*/
		return page;
	}
	
	@Override
	public Page getBanksWithWebThemes(int pageNumber, ThemeConfDTO themeDTO) throws EOTException {

		Page page = themeDao.getBanksWithWebThemes(pageNumber, themeDTO);
		
		/*if(page.results.size() == 0){
			throw new EOTException(ErrorConstants.THEME_NOT_EXISTS);
		}
*/
		return page;
	}

	@Override
	public ThemeConfDTO getTheme(Integer bankId, String appType) throws EOTException {
		
		ThemeConfDTO themeDTO=new ThemeConfDTO();
		Mobiletheamcolorconfig mobileTheme = themeDao.getUniqueMobileTheme(bankId, appType);
		//Branding webTheme = themeDao.getUniqueWebTheme(bankId);
		setThemeDTOForMobile(themeDTO, mobileTheme);
		//setThemeDTOForWeb(themeDTO, webTheme);
		return themeDTO;
	}
	
	@Override
	public ThemeConfDTO getWebTheme(Integer bankId) throws EOTException {
		
		ThemeConfDTO themeDTO=new ThemeConfDTO();
		Branding webTheme = themeDao.getUniqueWebTheme(bankId);
		setThemeDTOForWeb(themeDTO, webTheme);
		return themeDTO;
	}
	
	private void setThemeDTOForMobile(ThemeConfDTO themeConfDTO, Mobiletheamcolorconfig mobileTheme){
		
		if(mobileTheme!=null){
			
			themeConfDTO.setAppType(Integer.valueOf(mobileTheme.getAppTypeId()));
			themeConfDTO.setBankId(mobileTheme.getBank().getBankId());
			themeConfDTO.setBankName(mobileTheme.getBank().getBankName());
			themeConfDTO.setToolBarColor(mobileTheme.getToolBarColor());
			themeConfDTO.setScreenBackgroundColor(mobileTheme.getScreenBgColor());
			themeConfDTO.setTabColorSelected(mobileTheme.getTabColorSelected());
			themeConfDTO.setTabColorUnselected(mobileTheme.getTabColorUnselected());
			themeConfDTO.setGridColorSelected(mobileTheme.getGridColorSelected());
			themeConfDTO.setGridColorUnselected(mobileTheme.getGridColorUnselected());
			themeConfDTO.setListHeaderColor(mobileTheme.getListHeaderColor());
			themeConfDTO.setTextColor(mobileTheme.getTextColor());
		}
	}
	
	private void setThemeDTOForWeb(ThemeConfDTO themeConfDTO, Branding webTheme){
		
		if(webTheme!=null){
			themeConfDTO.setBankId(webTheme.getBank().getBankId());
			themeConfDTO.setBankName(webTheme.getBank().getBankName());
			themeConfDTO.setBankTheme(webTheme.getBankThemeColor());;
			themeConfDTO.setBtnColorName(webTheme.getButtonColor());;
			themeConfDTO.setSubMenuColorName(webTheme.getSubMenuColor());
			themeConfDTO.setMenuColorName(webTheme.getMenuColor());
			try {
				if(null !=webTheme.getLogo()){
					byte[]entityLogo=webTheme.getLogo().getBytes(1, (int)webTheme.getLogo().length());
					byte[] encodeBase64 = Base64.encodeBase64(entityLogo);
					String base64Encoded = new String(encodeBase64, "UTF-8");
					themeConfDTO.setLogo(base64Encoded);
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}

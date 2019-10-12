package com.eot.banking.service;

import com.eot.banking.dto.ThemeConfDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.utils.Page;
import com.eot.entity.Mobiletheamcolorconfig;

public interface ThemeService {
	
	public void saveThemeConfig(ThemeConfDTO themeConfDTO) throws EOTException;
	public void saveOrUpdateWebTheme(ThemeConfDTO themeConfDTO) throws EOTException;
	Page getBanksWithThemes(int pageNumber, ThemeConfDTO themeDTO)throws EOTException;
	ThemeConfDTO getTheme (Integer bankId, String appType)throws EOTException;
	Page getBanksWithWebThemes(int pageNumber, ThemeConfDTO themeDTO) throws EOTException;
	ThemeConfDTO getWebTheme(Integer bankId) throws EOTException;
}

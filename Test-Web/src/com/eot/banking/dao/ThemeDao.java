package com.eot.banking.dao;

import com.eot.banking.dto.ThemeConfDTO;
import com.eot.banking.utils.Page;
import com.eot.entity.Branding;
import com.eot.entity.Mobiletheamcolorconfig;

public interface ThemeDao extends BaseDao {
	
	Page getBanksWithThemes(int pageNumber, ThemeConfDTO themeDTO);
	Mobiletheamcolorconfig getUniqueMobileTheme (Integer bankId, String appType);
	Branding getUniqueWebTheme (Integer bankId);
	Page getBanksWithWebThemes(int pageNumber, ThemeConfDTO themeDTO);
}

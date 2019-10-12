package com.eot.banking.service;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eot.banking.dao.WebUserDao;
import com.eot.banking.exceptions.EOTException;
import com.eot.entity.PageLog;
import com.eot.entity.SessionLog;

@Service("pageService")
public class PageServiceImpl implements PageService{

	@Autowired
	private WebUserDao webUserDao;

	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=EOTException.class)
	public void savePageDetails(HttpServletRequest request,
			HttpServletResponse response,String page) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if(null!= authentication){
			String userName= authentication.getName();
			SessionLog sessionLog  = (SessionLog) request.getSession().getAttribute("sessionLog");

			System.out.println(userName);

			if(!userName.equalsIgnoreCase("anonymousUser")){
				Date currentTime = new Date();

				if( sessionLog == null ){

					sessionLog = new SessionLog();
					sessionLog.setLoginTime( currentTime );
					sessionLog.setUserName(userName);
					sessionLog.setIpAddress(request.getRemoteAddr());
					sessionLog.setSessionId(request.getRequestedSessionId());

					webUserDao.save(sessionLog);

					request.getSession().setAttribute("sessionLog", sessionLog ) ;

				}

				PageLog pageLog = new PageLog();

				pageLog.setPageVisited(page);
				pageLog.setTimeVisit(currentTime);
				pageLog.setSessionLog(sessionLog);
				webUserDao.save(pageLog);
			}

		}
	}
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=EOTException.class)
	public void closeSession(HttpServletRequest request,HttpServletResponse response,String page) {

		SessionLog sessionLog = (SessionLog) request.getSession().getAttribute("sessionLog");

		if(sessionLog !=null){
			Date currentTime = new Date();

			PageLog pageLog = new PageLog();

			pageLog.setPageVisited(page);
			pageLog.setTimeVisit(currentTime);
			pageLog.setSessionLog(sessionLog);

			webUserDao.save(pageLog);
			sessionLog.setLogoutTime(currentTime);
			webUserDao.update(sessionLog);
			request.getSession().invalidate();
			Cookie terminate = new Cookie(TokenBasedRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY, null);
			terminate.setMaxAge(0);
			response.addCookie(terminate);
		}

	}
}

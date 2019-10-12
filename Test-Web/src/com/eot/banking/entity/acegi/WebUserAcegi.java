package com.eot.banking.entity.acegi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;

import com.eot.banking.common.EOTConstants;
import com.eot.entity.WebUser;

public class WebUserAcegi extends WebUser implements UserDetails {
	
	public WebUserAcegi(WebUser webUser){
		super(
					webUser.getUserName(), 
					webUser.getPassword(), 
					webUser.getFirstName(), 
					webUser.getMiddleName(), 
					webUser.getLastName(), 
					webUser.getEmail(), 
					webUser.getMobileNumber(), 
					webUser.getAlternateNumber(), 
					webUser.getCredentialsExpired(), 
					webUser.getAccountLocked(), 
					webUser.getCreatedDate(), 
					webUser.getWebUserRole()
				);
	}
	
	private List<String> privileges;
	
	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		List<GrantedAuthority > list = new ArrayList<GrantedAuthority>();
		for (String privilege : privileges) {
			list.add(new GrantedAuthorityImpl("ROLE_" + privilege));
		}
		return list;
	}

//	@Override
//	public Collection<GrantedAuthority> getAuthorities() {
//		List<GrantedAuthority > list = new ArrayList<GrantedAuthority>();
//		list.add(new GrantedAuthorityImpl("ROLE_"+getWebUserRole().getRoleName()));
//		return list;
//	}

	@Override
	public String getUsername() {
		return getUserName();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return EOTConstants.WEB_USER_UNLOCKED.equals(getAccountLocked());
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return EOTConstants.WEB_USER_CREDENTIALS_NON_EXPIRED.equals(getCredentialsExpired());
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public List<String> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(List<String> privileges) {
		this.privileges = privileges;
	}

}

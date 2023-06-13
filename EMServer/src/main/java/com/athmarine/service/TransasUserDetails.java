package com.athmarine.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.athmarine.entity.User;

public class TransasUserDetails implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User user;

	public TransasUserDetails(User user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// List<Role> roles = (List<Role>) user.getRole();
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		// for (Role role : roles) {
		authorities.add(new SimpleGrantedAuthority("Admin"));
		// }
		return authorities;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	public String getEmail() {
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		if (user.getStatus() == 0) {
			return true;
		}
		return false;
	}

	@Override
	public String getUsername() {

		return user.getEmail();
	}
	
	

}

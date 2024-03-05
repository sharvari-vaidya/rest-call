package com.example.main.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.main.entity.UserEntity; 
  
public class UserInfoModel implements UserDetails { 
  
    private String name; 
    private String password; 
  
    public UserInfoModel(UserEntity userInfo) { 
        name = userInfo.getUserId(); 
        password = userInfo.getUserPassword(); 
        
    } 
  
    @Override
    public String getPassword() { 
        return password; 
    } 
  
    @Override
    public String getUsername() { 
        return name; 
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
        return true; 
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	} 
} 
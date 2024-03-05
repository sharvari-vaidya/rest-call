package com.example.main.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails; 
import org.springframework.security.core.userdetails.UserDetailsService; 
import org.springframework.security.core.userdetails.UsernameNotFoundException; 
import org.springframework.security.crypto.password.PasswordEncoder; 
import org.springframework.stereotype.Service;

import com.example.main.entity.UserEntity;
import com.example.main.model.UserInfoModel;
import com.example.main.repo.UserRepository;

import java.util.Optional;

@Service
public class UserInfoService implements UserDetailsService {


    @Autowired
    private UserRepository repository; 
  
    @Autowired
    private PasswordEncoder encoder;
    
	@Override
	public UserDetails loadUserByUsername(String userID) throws UsernameNotFoundException {
		Optional<UserEntity> userDetail = repository.findById(userID);
		return userDetail.map(UserInfoModel::new)
				.orElseThrow(() -> new UsernameNotFoundException("User not found " + userID));
	}

}

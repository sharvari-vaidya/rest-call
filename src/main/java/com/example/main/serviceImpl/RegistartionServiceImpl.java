package com.example.main.serviceImpl;

import java.time.Instant;
import java.util.Optional;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.main.entity.UserEntity;
import com.example.main.model.ResponseModel;
import com.example.main.repo.UserRepository;
import com.example.main.service.RegistrationService;
import com.example.main.service.Util;
import com.example.main.util.Constants;

@Service
public class RegistartionServiceImpl implements RegistrationService{

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
    private Util util;
	
	@Override
	public ResponseModel registerUser(UserEntity request, Logger log) {
		ResponseModel response = new ResponseModel();
		try {
		Optional<UserEntity> user = userRepo.findById(request.getUserId());
		if(user.isPresent()) {
			response.setErrorCode(Constants.ErrorCode.USER_ALREADY_EXITS);
			response.setErrorMessage("USER ALREADY EXITS");
		}else {
			request.setCreatedDate(util.longToTimestamp(System.currentTimeMillis()));
			request.setUserPassword(util.encryptPassword(request.getUserPassword()));
			userRepo.save(request);
			response.setErrorCode(Constants.ErrorCode.USER_CREATED_SUCCESSFULLY);
			response.setErrorMessage("USER CREATED SUCCESSFULLY");
		}
		}catch (Exception e) {
			log.info("Exception occured in registerUser service :: " +e.getMessage());
			response.setErrorCode(Constants.ErrorCode.FAILURE);
			response.setErrorMessage("FAILURE");
			e.printStackTrace();
		}
		return response;
	}
}

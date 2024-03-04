package com.example.main.serviceImpl;

import java.util.Optional;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.main.entity.UserEntity;
import com.example.main.model.ResponseModel;
import com.example.main.repo.UserRepository;
import com.example.main.service.LoginService;
import com.example.main.util.Constants;

@Service
public class LoginServiceImpl implements LoginService{

	@Autowired
	private UserRepository userRepo; 
	
	@Override
	public ResponseModel makeLogin(UserEntity request, Logger log) {
		ResponseModel response= new ResponseModel();
		try {
			Optional<UserEntity> user = userRepo.findById(request.getUserId());
			if(user.isPresent()) {
				if(request.getUserPassword().equals(user.get().getUserPassword())) {
					response.setErrorCode(Constants.ErrorCode.USER_LOGGED_IN_SUCCESSFULLY);
					response.setErrorMessage("USER LOGGED IN SUCCESSFULLY");
				}else {
					response.setErrorCode(Constants.ErrorCode.PASSWORD_INCORRECT);
					response.setErrorMessage("PASSWORD TYPED IS INCORRECT");
				}
			}else {
				response.setErrorCode(Constants.ErrorCode.USER_DOES_NOT_EXISTS);
				response.setErrorMessage("USER DOES NOT EXISTS");
			}
		}catch(Exception e){
			log.info("Exception occured in makeLogin service :: " +e.getMessage());
			response.setErrorCode(Constants.ErrorCode.FAILURE);
			response.setErrorMessage("FAILURE");
			e.printStackTrace();
		}
		return response;
	}
}

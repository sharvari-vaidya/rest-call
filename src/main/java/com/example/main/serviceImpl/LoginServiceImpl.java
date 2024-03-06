package com.example.main.serviceImpl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.main.entity.UserEntity;
import com.example.main.model.ResponseModel;
import com.example.main.model.UserInfoModel;
import com.example.main.repo.UserRepository;
import com.example.main.service.LoginService;
import com.example.main.service.RedisService;
import com.example.main.service.Util;
import com.example.main.util.Constants;
import com.google.gson.Gson;

import redis.clients.jedis.JedisCluster;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private Util util;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private RedisService redisService;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private BCryptPasswordEncoder encoder;

	@Override
	public ResponseModel makeLogin(UserEntity request, Logger log) {
		ResponseModel response = new ResponseModel();
		try {
			Optional<UserEntity> user = userRepo.findById(request.getUserId());
			if (user.isPresent()) {
				if (encoder.matches(request.getUserPassword(), user.get().getUserPassword())) {
					user.map(UserInfoModel::new);
					Authentication authentication = authenticationManager.authenticate(
							new UsernamePasswordAuthenticationToken(request.getUserId(), request.getUserPassword()));

					if (authentication.isAuthenticated()) {

						String token = jwtService.generateToken(request.getUserId());
						log.info("For the user ::  " + request.getUserId() + " auth token is :: " + token);

						redisService.setValue(Constants.RedisKeys.REDIS_SESSION_KEY+request.getUserId(), new Gson().toJson(user.get()));
						redisService.setExpiry(Constants.RedisKeys.REDIS_SESSION_KEY+request.getUserId());

						redisService.setValue(Constants.RedisKeys.REDIS_JWT_TOKEN+request.getUserId(), token);
						redisService.setExpiry(Constants.RedisKeys.REDIS_JWT_TOKEN+request.getUserId());
						
						
						
						
						response.setErrorCode(Constants.ErrorCode.USER_LOGGED_IN_SUCCESSFULLY);
						response.setErrorMessage("USER LOGGED IN SUCCESSFULLY");
					} else {
						response.setErrorCode(Constants.ErrorCode.INVALID_CREDENTIALS);
						response.setErrorMessage("INVALID CREDENTIALS");
					}
				} else {
					response.setErrorCode(Constants.ErrorCode.PASSWORD_INCORRECT);
					response.setErrorMessage("PASSWORD TYPED IS INCORRECT");
				}
			} else {
				response.setErrorCode(Constants.ErrorCode.USER_DOES_NOT_EXISTS);
				response.setErrorMessage("USER DOES NOT EXISTS");
			}
		} catch (Exception e) {
			log.info("Exception occured in makeLogin service :: " + e.getMessage());
			response.setErrorCode(Constants.ErrorCode.FAILURE);
			response.setErrorMessage("FAILURE");
			e.printStackTrace();
		}
		return response;
	}

	@Override
	public ResponseModel makeLogout(UserEntity request, Logger log) {
		ResponseModel response = new ResponseModel();
		try {
			redisService.removeUser(Constants.RedisKeys.REDIS_SESSION_KEY+request.getUserId());
			redisService.removeUser(Constants.RedisKeys.REDIS_JWT_TOKEN+request.getUserId());
			response.setErrorCode(Constants.ErrorCode.USER_LOGGED_OUT_SUCCESSFULLY);
			response.setErrorMessage("USER LOGGED OUT SUCCESSFULLY");

		} catch (Exception e) {
			log.info("Exception occured in makeLogout service :: " + e.getMessage());
			response.setErrorCode(Constants.ErrorCode.FAILURE);
			response.setErrorMessage("FAILURE");
			e.printStackTrace();
		}
		return response;
	}

}

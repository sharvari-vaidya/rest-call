package com.example.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.main.entity.UserEntity;
import com.example.main.model.ResponseModel;
import com.example.main.service.LoginService;
import com.example.main.service.RedisService;
import com.example.main.util.Constants;

import lombok.extern.log4j.Log4j2;
import redis.clients.jedis.JedisCluster;

@RestController
@Log4j2
public class LoginController {

	@Autowired
	private LoginService loginService;

	@Autowired
	private RedisService redisService;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<ResponseModel> login(@RequestBody UserEntity request) {
		log.info("Entering in login controller with request :: " + request.toString());
		HttpHeaders headers = new HttpHeaders();
		ResponseModel response = loginService.makeLogin(request, log);
		if (response.getErrorCode() == Constants.ErrorCode.USER_LOGGED_IN_SUCCESSFULLY) {
			String token = redisService.getValue(Constants.RedisKeys.REDIS_JWT_TOKEN+request.getUserId());
			headers.add("jwtToken", token);
		}
		log.info("Exiting with response :: " + response.toString());
		return new ResponseEntity<ResponseModel>(response, headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public ResponseEntity<ResponseModel> logout(@RequestBody UserEntity request) {
		log.info("Entering in logout controller with request :: " + request.toString());
		ResponseModel response = loginService.makeLogout(request, log);
		log.info("Exiting with response :: " + response.toString());
		return new ResponseEntity<ResponseModel>(response, HttpStatus.OK);
	}

}

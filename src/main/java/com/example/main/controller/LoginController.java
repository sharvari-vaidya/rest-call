package com.example.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.main.entity.UserEntity;
import com.example.main.model.ResponseModel;
import com.example.main.service.LoginService;

import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
public class LoginController {
	
	@Autowired
	private LoginService loginService;

	@RequestMapping(value="/login",method=RequestMethod.POST)
	public ResponseEntity<ResponseModel> login(@RequestBody UserEntity request){
		
		log.info("Entering in login controller with request :: "+request.toString());
		ResponseModel response = loginService.makeLogin(request,log);
		log.info("Exiting with response :: "+response.toString());
		return new ResponseEntity<ResponseModel>(response, HttpStatus.OK); 

	}

}

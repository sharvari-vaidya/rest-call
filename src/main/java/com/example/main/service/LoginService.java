package com.example.main.service;

import org.apache.logging.log4j.Logger;

import com.example.main.entity.UserEntity;
import com.example.main.model.ResponseModel;

public interface LoginService {

	ResponseModel makeLogin(UserEntity request, Logger log);

}

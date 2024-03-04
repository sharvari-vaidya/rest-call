package com.example.main.service;

import org.apache.logging.log4j.Logger;

import com.example.main.entity.UserEntity;
import com.example.main.model.ResponseModel;

public interface RegistrationService {

	ResponseModel registerUser(UserEntity request, Logger log);

}

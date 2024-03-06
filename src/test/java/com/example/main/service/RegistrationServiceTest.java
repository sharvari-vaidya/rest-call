package com.example.main.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Timestamp;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.main.entity.UserEntity;
import com.example.main.model.ResponseModel;
import com.example.main.repo.UserRepository;
import com.example.main.serviceImpl.RegistartionServiceImpl;
import com.example.main.util.Constants;

@SpringBootTest
public class RegistrationServiceTest {

	@Mock
	private UserRepository userRepo;
	
	@Mock
    private Util util;
	
	@Mock
    Logger log;
	
	@InjectMocks
	RegistrationService registrationService = new RegistartionServiceImpl();
	
	
	@Test
	@DisplayName("RegistrationSuccessTest")
	public void testWhenRegistrationSuccess() {
		UserEntity user= null;

        Mockito.doNothing().when(log).info(Mockito.anyString());
		Mockito.when(userRepo.findById("USER1")).thenReturn(Optional.ofNullable(null));
		Mockito.when(userRepo.save(user)).thenReturn(null);
		Mockito.when(util.encryptPassword("123")).thenReturn("123");
		Mockito.when(util.longToTimestamp(ArgumentMatchers.anyLong())).thenReturn(new Timestamp(System.currentTimeMillis()));
		user=getUser();
		ResponseModel response = registrationService.registerUser(user, log);
		assertEquals(response.getErrorCode(), Constants.ErrorCode.USER_CREATED_SUCCESSFULLY);
	}
	
	
	@Test
	@DisplayName("RegistrationFailureTest")
	public void testWhenUserAlreadyExistsFails() {
		UserEntity user= getUser();
		Mockito.when(userRepo.findById("USER1")).thenReturn(Optional.of(user));
		Mockito.when(util.encryptPassword("123")).thenReturn("123");
		Mockito.when(util.longToTimestamp(ArgumentMatchers.anyLong())).thenReturn(new Timestamp(System.currentTimeMillis()));
		Mockito.when(userRepo.save(user)).thenReturn(null);
		ResponseModel response = registrationService.registerUser(user, log);
		assertEquals(response.getErrorCode(), Constants.ErrorCode.USER_ALREADY_EXITS);
	}
	
	
	private UserEntity getUser() {
		return UserEntity.builder()
				.userId("USER1")
				.userName("ABC")
				.userPassword("123")
				.build();
	}

	
}

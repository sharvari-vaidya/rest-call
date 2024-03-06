package com.example.main.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Timestamp;
import java.util.Optional;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.main.entity.UserEntity;
import com.example.main.model.ResponseModel;
import com.example.main.repo.UserRepository;
import com.example.main.serviceImpl.JwtService;
import com.example.main.serviceImpl.LoginServiceImpl;
import com.example.main.util.Constants;

@SpringBootTest
public class LoginServiceTest {

	@Mock
	private UserRepository userRepo;

	@Mock
	private Util util;

	@Mock
	private JwtService jwtService;

	@Mock
	private RedisService redisService;

	@Mock
	Logger log;

	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private BCryptPasswordEncoder encoder;

	@InjectMocks
	LoginService loginService = new LoginServiceImpl();
	
	String randomToken = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJtbW0iLCJleHAiOjE3MDk2NjQxOTN9.irJ-RUZNtF2588cQoEJCf1ZiI8V-VN_SVKBxm750Pb_iMmfmd6h9OVOd4U_U6l_g";


	@Test
	@DisplayName("LoginSuccessTest")
	public void testWhenLoginSuccess() {
        UsernamePasswordAuthenticationToken authentication = Mockito.mock(UsernamePasswordAuthenticationToken.class);
		UserEntity user = getUser();
		Mockito.when(userRepo.findById("USER1")).thenReturn(Optional.of(user));
		Mockito.when(encoder.matches("123", user.getUserPassword())).thenReturn(true);
		Mockito.when(authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getUserId(), user.getUserPassword())))
				.thenReturn(authentication);
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);

		Mockito.when(jwtService.generateToken(Constants.RedisKeys.REDIS_SESSION_KEY + "USER1")).thenReturn(randomToken);
		Mockito.doNothing().when(redisService).setValue(Constants.RedisKeys.REDIS_SESSION_KEY + "USER1", randomToken);
		Mockito.doNothing().when(redisService).setExpiry(Constants.RedisKeys.REDIS_SESSION_KEY + "USER1");
		Mockito.doNothing().when(redisService).setValue(Constants.RedisKeys.REDIS_JWT_TOKEN + "USER1", randomToken);
		Mockito.doNothing().when(redisService).setExpiry(Constants.RedisKeys.REDIS_JWT_TOKEN + "USER1");
		ResponseModel response = loginService.makeLogin(user, log);
		assertEquals(response.getErrorCode(), Constants.ErrorCode.USER_LOGGED_IN_SUCCESSFULLY);
	}

	@Test
	@DisplayName("PasswordMismatchTest")
	public void testWhenPasswordIncorrectFailure() {
        UsernamePasswordAuthenticationToken authentication = Mockito.mock(UsernamePasswordAuthenticationToken.class);
		UserEntity user = getUser();
		Mockito.when(userRepo.findById("USER1")).thenReturn(Optional.of(user));
		Mockito.when(encoder.matches("123", user.getUserPassword())).thenReturn(false);
		ResponseModel response = loginService.makeLogin(user, log);
		assertEquals(response.getErrorCode(), Constants.ErrorCode.PASSWORD_INCORRECT);
	}
	
	@Test
	@DisplayName("UserNotRegisteredTest")
	public void testWhenUserNotRegisteredFailure() {
        UsernamePasswordAuthenticationToken authentication = Mockito.mock(UsernamePasswordAuthenticationToken.class);
		UserEntity user = null;
		Mockito.when(userRepo.findById("USER1")).thenReturn(Optional.ofNullable(user));
		user= getUser();
		ResponseModel response = loginService.makeLogin(user, log);
		assertEquals(response.getErrorCode(), Constants.ErrorCode.USER_DOES_NOT_EXISTS);
	}
	
	@Test
	@DisplayName("AuthenticationFailsTest")
	public void testWhenAuthenticationFailsFailure() {
        UsernamePasswordAuthenticationToken authentication = Mockito.mock(UsernamePasswordAuthenticationToken.class);
		UserEntity user = getUser();
		Mockito.when(userRepo.findById("USER1")).thenReturn(Optional.of(user));
		Mockito.when(encoder.matches("123", user.getUserPassword())).thenReturn(true);
		Mockito.when(authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getUserId(), user.getUserPassword())))
				.thenReturn(authentication);
		ResponseModel response = loginService.makeLogin(user, log);
		assertEquals(response.getErrorCode(), Constants.ErrorCode.INVALID_CREDENTIALS);
	}
	
	
	
	@Test
	@DisplayName("LogoutSuccessTest")
	public void testWhenLogoutSuccessful() {
		UserEntity user = getUser();
		Mockito.doNothing().when(redisService).removeUser(Constants.RedisKeys.REDIS_SESSION_KEY + "USER1");
		Mockito.doNothing().when(redisService).removeUser(Constants.RedisKeys.REDIS_JWT_TOKEN + "USER1");
		ResponseModel response = loginService.makeLogout(user, log);
		assertEquals(response.getErrorCode(), Constants.ErrorCode.USER_LOGGED_OUT_SUCCESSFULLY);
	}
	
	private UserEntity getUser() {
		return UserEntity.builder().userId("USER1").userName("ABC").userPassword("123").build();
	}

}

package com.example.main.service;

public interface RedisService {

	void setValue(String userID, String token);

	String getValue(String userID);
	
	void setExpiry(String userID);
	
	void removeUser(String userID);

}

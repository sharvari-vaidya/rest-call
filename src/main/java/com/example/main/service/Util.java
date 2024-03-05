package com.example.main.service;

import java.sql.Timestamp;

public interface Util {

	boolean isNeitherNullNorEmpty(Object obj);

	String encryptPassword(String password);

	Timestamp longToTimestamp(long longtime);

}

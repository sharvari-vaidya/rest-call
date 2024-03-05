package com.example.main.util;

public interface Constants {

	public interface ErrorCode {
		int FAILURE = 101;
		int USER_CREATED_SUCCESSFULLY = 102;
		int USER_ALREADY_EXITS = 103;
		int USER_DOES_NOT_EXISTS = 104;
		int USER_LOGGED_IN_SUCCESSFULLY = 105;
		int PASSWORD_INCORRECT = 106;
		int PRODUCT_ALREADY_PRESENT = 107;
		int PRODUCT_CREATED = 108;
		int NO_ANY_PRODUCT_PRESENT = 109;
		int PRODUCTS_FOUND = 110;
		int USER_NOT_LOGGEDIN = 111;
		int USER_LOGGED_OUT_SUCCESSFULLY = 112;
		int INVALID_CREDENTIALS = 113;
	}

	public interface RedisKeys {
		String REDIS_SESSION_KEY = "SESSION";
		String REDIS_JWT_TOKEN = "JWT";
	}

}

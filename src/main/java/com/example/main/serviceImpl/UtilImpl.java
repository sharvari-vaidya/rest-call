package com.example.main.serviceImpl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.text.ParseException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.main.service.Util;


@Service
public class UtilImpl implements Util{

	@Override
	public boolean isNeitherNullNorEmpty(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof String && ((String) obj).trim().equals("")) {
			return false;
		}
		if (obj instanceof List<?> && ((List) obj).isEmpty()) {
			return false;
		}
		return true;
	}
	
	@Override
	public String encryptPassword(String password) {
        try {
        	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        	return encoder.encode(password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	
	@Override
	public Timestamp longToTimestamp(long longtime) {
		
		Date date = new Date();
		Timestamp ts = new Timestamp(longtime);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        String time = formatter.format(ts);  
        try {
        	 date  = formatter.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        Timestamp timestamp = new Timestamp(date.getTime());
        return timestamp;
	}

	
}

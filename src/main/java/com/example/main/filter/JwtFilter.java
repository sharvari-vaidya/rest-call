package com.example.main.filter;

import java.io.IOException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.main.entity.UserEntity;
import com.example.main.service.RedisService;
import com.example.main.service.Util;
import com.example.main.serviceImpl.JwtService;
import com.example.main.serviceImpl.RegistartionServiceImpl;
import com.example.main.serviceImpl.UserInfoService;
import com.example.main.util.Constants;
import com.google.gson.Gson;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private UserInfoService userDetailsService;

	@Autowired
	private RedisService redisService;

	@Autowired
	private Util util;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
	    logger.info("Request URI: " + request.getRequestURI());
		String authHeader = request.getHeader("Authorization");
		String token = null;
		String userId = null;
		String jwtToken = null;
		if (util.isNeitherNullNorEmpty(authHeader)) {
			jwtToken = authHeader.substring(authHeader.indexOf(' ') + 1);
		}
		if (util.isNeitherNullNorEmpty(authHeader) && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7);
			userId = jwtService.extractUsername(token);
		}

		if (util.isNeitherNullNorEmpty(userId) && SecurityContextHolder.getContext().getAuthentication() == null) {

			String userDetailsJson = redisService.getValue(Constants.RedisKeys.REDIS_SESSION_KEY + userId);
			UserEntity userEntity = Objects.nonNull(userDetailsJson)
					? new Gson().fromJson(userDetailsJson, UserEntity.class)
					: null;
			String actualToken = redisService.getValue(Constants.RedisKeys.REDIS_JWT_TOKEN + userId);

			if (util.isNeitherNullNorEmpty(actualToken) && actualToken.equals(jwtToken)) {
				redisService.setExpiry(Constants.RedisKeys.REDIS_JWT_TOKEN + userId);
				if (util.isNeitherNullNorEmpty(userEntity) && userId.equalsIgnoreCase(userEntity.getUserId())) {
					redisService.setExpiry(Constants.RedisKeys.REDIS_SESSION_KEY + userId);

					UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
					if (jwtService.validateToken(token, userDetails)) {
						UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
								userDetails, null, userDetails.getAuthorities());
						authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						SecurityContextHolder.getContext().setAuthentication(authToken);
					}
				}
			}
		}
		filterChain.doFilter(request, response);
	}

}

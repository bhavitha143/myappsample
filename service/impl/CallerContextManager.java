package com.bh.realtrack.service.impl;

import java.util.Base64;
import java.util.Collections;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

import com.bh.realtrack.dto.User;
import com.bh.realtrack.exception.UnknowAuthenticationException;
import com.bh.realtrack.model.CallerContext;
import com.bh.realtrack.service.ICallerContextManager;

/**
 * @author Anand Kumar
 */
@Service
public class CallerContextManager implements ICallerContextManager {

    protected static final String APPLICATION_KEY_HEADER = "rt-id-token";
    private static Logger logger = LoggerFactory.getLogger(CallerContextManager.class.getName());

    /*@Override
    public CallerContext getCallerContext(final HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof OAuth2Authentication) {
            OAuth2Authentication oauth = (OAuth2Authentication) authentication;
            return new CallerContext(oauth.getName(), request.getHeader(APPLICATION_KEY_HEADER),
                    oauth.getAuthorities());
        }
        throw new UnknowAuthenticationException(
                "Unsupported authentication object: " + " " + authentication.getClass().getName());
    }*/
    
   /* @Override
	public CallerContext getCallerContext(final HttpServletRequest request) throws UnknowAuthenticationException{
		String userSSO = null;

		String jwtToken = request.getHeader("rt-id-token");

		if(jwtToken==null || jwtToken.isEmpty())
		{
			logger.error("Token header cannot be empty.User authentication failed", jwtToken);
			throw new UnknowAuthenticationException("Token header cannot be empty.User authentication failed");
		}
		else
		{
			String[] tokens = StringUtils.split(jwtToken, ".");
			
			if(tokens.length<3)
			{
				logger.error("Invalid JWT token passed. User cannot be authenticated");
				throw new UnknowAuthenticationException("Invalid JWT token passed. User cannot be authenticated");
			}
			
			try {
								
					byte[] payload  = Base64.getDecoder().decode(tokens[1]);
					String payLoadToString = new String(payload);
					JSONParser parser = new JSONParser();
					JSONObject claimsJson;
					try {
						claimsJson = (JSONObject) parser.parse(payLoadToString);
						if(claimsJson.containsKey("sub"))
						{
							userSSO = (String) claimsJson.get("sub");
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
				
					if(userSSO !=null && !userSSO.isEmpty())
					{
						return new CallerContext(userSSO, request.getHeader(APPLICATION_KEY_HEADER),
								Collections.EMPTY_LIST);
					}
					else
					{
						logger.error("Cannot Parse User Claims from token. User authentication failed");
						throw new UnknowAuthenticationException("Cannot Parse User Claims from token. User authentication failed");
					}

				
			}
			catch(IllegalArgumentException e)
			{
				logger.error("Exception while reading token. User authentication failed", e.getMessage());
				throw new UnknowAuthenticationException("Exception while reading token. User authentication failed");
			}
		}
		   	

	}*/
    
public CallerContext getCallerContext(final HttpServletRequest request) {
		
		logger.info("Inside context manager");
		
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
        	UsernamePasswordAuthenticationToken oauth = (UsernamePasswordAuthenticationToken) authentication;        	
        	User user = (User) oauth.getPrincipal();
            return new CallerContext(user.getUserSSO(), request.getHeader(APPLICATION_KEY_HEADER),
                    oauth.getAuthorities());
        }
        throw new UnknowAuthenticationException(
                "Unsupported authentication object: " + " " + authentication.getClass().getName());
    }
}

package com.bh.realtrack.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bh.realtrack.dao.ICommonDAO;
import com.bh.realtrack.util.CommonConstants;


@Repository
public class CommonDAO implements ICommonDAO {
	
private static final Logger LOGGER = LoggerFactory.getLogger(CommonDAO.class);
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	
	@Override
	public Map<String,String> fetchFavProjectsDetails(String sso) {
		
		Map<String,String> favProjectDetailMap = new HashMap<String,String>();
		ResultSet rs = null;
		
		try(Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(CommonConstants.GET_ANALYSIS_FAV_PROJECT_DETAILS);)
		{
		
			ps.setString(1, sso);
			rs = ps.executeQuery();
			
			while(rs.next())
			{
				favProjectDetailMap.put(rs.getString(1), rs.getString(2));
			}
			
		}catch (Exception exception)
		{
			
			LOGGER.error("Error occured when fecthing favourite project details " + exception.getMessage());
		}
		
		return favProjectDetailMap;
		
	}

}

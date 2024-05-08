package com.bh.realtrack.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.realtrack.dao.ICommonDAO;
import com.bh.realtrack.model.CallerContext;
import com.bh.realtrack.service.ICommonService;

@Service
public class CommonService implements ICommonService {
	

	@Autowired
	private ICommonDAO commonDAO;
	
	@Autowired
	private CallerContext callerContext;


	@Override
	public String fetchFavProjects()
	{
		
		String sso = callerContext.getName();
	
		Map<String,String> favProjectDetailsMap = null;
		String favProjectFlag = null;
		String favProjectId = null;
		
		favProjectDetailsMap = commonDAO.fetchFavProjectsDetails(sso);
		favProjectFlag = favProjectDetailsMap.get("PORTFOLIO_FAVORITE_FLAG");
		

		
		if(null != favProjectFlag && "Y".equalsIgnoreCase(favProjectFlag))
		{
			favProjectId = favProjectDetailsMap.get("PORTFOLIO_FAVORITE_PROJECTS");
			
			if(null == favProjectId || "".equalsIgnoreCase(favProjectId))
			{
				favProjectId =  null;
			}
		}
		
		return favProjectId;
	}



}

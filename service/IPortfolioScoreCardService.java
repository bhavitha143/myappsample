package com.bh.realtrack.service;

import java.util.List;
import java.util.Map;

import com.bh.realtrack.dto.PortfolioScoreCardDetailsDTO;
import com.bh.realtrack.dto.PortfolioScoreCardFilterDTO;
import com.bh.realtrack.dto.PortfolioScoreCardManageScorecardDTO;

public interface IPortfolioScoreCardService {

	Map<String, Object> getPortfolioScoreCardFilters(PortfolioScoreCardFilterDTO portfolioScoreCardFilterDTO);

	Map<String, Object> getPortfolioScoreCardPublishedDetails(PortfolioScoreCardFilterDTO portfolioScoreCardFilterDTO);

	Map<String, Object> getPortfolioScoreCardLiveDetails(PortfolioScoreCardFilterDTO portfolioScoreCardFilterDTO);

	Map<String, Object> getPortfolioScoreCardManageScorecardDetails(
			PortfolioScoreCardFilterDTO portfolioScoreCardFilterDTO);

	Map<String, Object> savePortfolioScoreCardManageScorecardDetails(String selectedSegment,
			List<PortfolioScoreCardManageScorecardDTO> portfolioScoreCardManageScorecardDTOList);

	Map<String, Object> savePortfolioScoreCardLiveDetails(String selectedSegment,
			PortfolioScoreCardDetailsDTO portfolioScoreCardDetailsDTO);

	Map<String, Object> publishPortfolioScoreCardLiveDetails(String selectedSegment,
			PortfolioScoreCardDetailsDTO portfolioScoreCardDetailsDTO);

}

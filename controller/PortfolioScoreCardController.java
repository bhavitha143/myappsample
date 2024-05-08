package com.bh.realtrack.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bh.realtrack.dto.PortfolioScoreCardDetailsDTO;
import com.bh.realtrack.dto.PortfolioScoreCardFilterDTO;
import com.bh.realtrack.dto.PortfolioScoreCardManageScorecardDTO;
import com.bh.realtrack.service.IPortfolioScoreCardService;

@RestController
@CrossOrigin
public class PortfolioScoreCardController {

	@Autowired
	IPortfolioScoreCardService iPortfolioScoreCardService;

	// private static final Logger log =
	// LoggerFactory.getLogger(PortfolioScoreCardController.class);

	@RequestMapping(value = "/getPortfolioScoreCardFilters", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public Map<String, Object> getPortfolioScoreCardFilters(
			@RequestBody PortfolioScoreCardFilterDTO portfolioScoreCardFilterDTO) {

		return iPortfolioScoreCardService.getPortfolioScoreCardFilters(portfolioScoreCardFilterDTO);
	}

	@RequestMapping(value = "/getPortfolioScoreCardPublishedDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public Map<String, Object> getPortfolioScoreCardPublishedDetails(
			@RequestBody PortfolioScoreCardFilterDTO portfolioScoreCardFilterDTO) {

		return iPortfolioScoreCardService.getPortfolioScoreCardPublishedDetails(portfolioScoreCardFilterDTO);
	}

	@RequestMapping(value = "/getPortfolioScoreCardLiveDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public Map<String, Object> getPortfolioScoreCardLiveDetails(
			@RequestBody PortfolioScoreCardFilterDTO portfolioScoreCardFilterDTO) {

		return iPortfolioScoreCardService.getPortfolioScoreCardLiveDetails(portfolioScoreCardFilterDTO);
	}

	@RequestMapping(value = "/getPortfolioScoreCardManageScorecardDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public Map<String, Object> getPortfolioScoreCardManageScorecardDetails(
			@RequestBody PortfolioScoreCardFilterDTO portfolioScoreCardFilterDTO) {

		return iPortfolioScoreCardService.getPortfolioScoreCardManageScorecardDetails(portfolioScoreCardFilterDTO);
	}

	@RequestMapping(value = "/savePortfolioScoreCardManageScorecardDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public Map<String, Object> savePortfolioScoreCardManageScorecardDetails(@RequestParam String selectedSegment,
			@RequestBody List<PortfolioScoreCardManageScorecardDTO> portfolioScoreCardManageScorecardDTOList) {

		return iPortfolioScoreCardService.savePortfolioScoreCardManageScorecardDetails(selectedSegment,
				portfolioScoreCardManageScorecardDTOList);
	}

	@RequestMapping(value = "/savePortfolioScoreCardLiveDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public Map<String, Object> savePortfolioScoreCardLiveDetails(@RequestParam String selectedSegment,
			@RequestBody PortfolioScoreCardDetailsDTO portfolioScoreCardDetailsDTO) {

		return iPortfolioScoreCardService.savePortfolioScoreCardLiveDetails(selectedSegment,
				portfolioScoreCardDetailsDTO);
	}

	@RequestMapping(value = "/publishPortfolioScoreCardLiveDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public Map<String, Object> publishPortfolioScoreCardLiveDetails(@RequestParam String selectedSegment,
			@RequestBody PortfolioScoreCardDetailsDTO portfolioScoreCardDetailsDTO) {

		return iPortfolioScoreCardService.publishPortfolioScoreCardLiveDetails(selectedSegment,
				portfolioScoreCardDetailsDTO);
	}

}

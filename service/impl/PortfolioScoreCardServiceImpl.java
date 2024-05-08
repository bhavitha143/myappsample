package com.bh.realtrack.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.realtrack.dao.IPortfolioScoreCardDAO;
import com.bh.realtrack.dto.BookedProjectsDTO;
import com.bh.realtrack.dto.LastUpdatedNameDTO;
import com.bh.realtrack.dto.PortfolioScoreCardAreaDTO;
import com.bh.realtrack.dto.PortfolioScoreCardDetailsDTO;
import com.bh.realtrack.dto.PortfolioScoreCardFilterDTO;
import com.bh.realtrack.dto.PortfolioScoreCardHighlightDTO;
import com.bh.realtrack.dto.PortfolioScoreCardManageScorecardDTO;
import com.bh.realtrack.dto.PortfolioScoreCardSegmentDTO;
import com.bh.realtrack.model.CallerContext;
import com.bh.realtrack.service.ICommonService;
import com.bh.realtrack.service.IPortfolioScoreCardService;
import com.bh.realtrack.util.PortfolioScoreCardConstants;

@Service
public class PortfolioScoreCardServiceImpl implements IPortfolioScoreCardService {

	@Autowired
	private CallerContext callerContext;

	@Autowired
	IPortfolioScoreCardDAO iPortfolioScoreCardDAO;

	@Autowired
	private ICommonService commonService;

	private static final Logger log = LoggerFactory.getLogger(PortfolioScoreCardServiceImpl.class);

	@Override
	public Map<String, Object> getPortfolioScoreCardFilters(PortfolioScoreCardFilterDTO portfolioScoreCardFilterDTO) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<String> showBy = new ArrayList<String>();
		String date = "", defaultShowBy = "", segment = "0", title = "", message = "";
		try {
			if (null != portfolioScoreCardFilterDTO.getSegment()
					&& !portfolioScoreCardFilterDTO.getSegment().equalsIgnoreCase("")
					&& !portfolioScoreCardFilterDTO.getSegment().contains(",")) {
				showBy = iPortfolioScoreCardDAO.getPortfolioScoreCardFilters(portfolioScoreCardFilterDTO);
				if (showBy.size() == 0 || !showBy.contains("Current View")) {
					showBy.add("Current View");
				}
				segment = portfolioScoreCardFilterDTO.getSegment();
				defaultShowBy = null != portfolioScoreCardFilterDTO.getShowBy()
						&& !portfolioScoreCardFilterDTO.getShowBy().isEmpty()
						&& (portfolioScoreCardFilterDTO.getShowBy().contains(segment)
								|| portfolioScoreCardFilterDTO.getShowBy().equalsIgnoreCase("Save Draft"))
										? portfolioScoreCardFilterDTO.getShowBy()
										: "Current View";
				date = iPortfolioScoreCardDAO.getPortfolioScoreCardFiltersDate(portfolioScoreCardFilterDTO);
				if (segment.equalsIgnoreCase("0")) {
					if (null != date && !date.equalsIgnoreCase("")) {
						title = "GTE Portfolio Scorecard @ " + date;
					} else {
						title = "GTE Portfolio Scorecard";
					}
				} else {
					if (null != date && !date.equalsIgnoreCase("")) {
						title = segment + " Portfolio Scorecard @ " + date;
					} else {
						title = segment + " Portfolio Scorecard";
					}
				}
			} else {
				message = "No data available";
			}
			responseMap.put("showBy", showBy);
			responseMap.put("defaultShowBy", defaultShowBy);
			responseMap.put("date", date);
			responseMap.put("title", title);
			responseMap.put("message", message);
		} catch (Exception e) {
			log.error("Exception in PortfolioScoreCardServiceImpl :: getPortfolioScoreCardFilters() :: "
					+ e.getMessage());
		}

		return responseMap;
	}

	@Override
	public Map<String, Object> getPortfolioScoreCardPublishedDetails(
			PortfolioScoreCardFilterDTO portfolioScoreCardFilterDTO) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<PortfolioScoreCardSegmentDTO> prodSegmentList = new ArrayList<PortfolioScoreCardSegmentDTO>();
		List<PortfolioScoreCardAreaDTO> prodAreaList = new ArrayList<PortfolioScoreCardAreaDTO>();
		List<PortfolioScoreCardHighlightDTO> prodHighlightsList = new ArrayList<PortfolioScoreCardHighlightDTO>();
		List<PortfolioScoreCardSegmentDTO> installSegmentList = new ArrayList<PortfolioScoreCardSegmentDTO>();
		List<PortfolioScoreCardAreaDTO> installAreaList = new ArrayList<PortfolioScoreCardAreaDTO>();
		List<PortfolioScoreCardHighlightDTO> installHighlightsList = new ArrayList<PortfolioScoreCardHighlightDTO>();
		LastUpdatedNameDTO lastUpdatedDetails = new LastUpdatedNameDTO();
		String favProjects = "0", segment = "0", showBookProjects = "Y";
		try {
			favProjects = commonService.fetchFavProjects();
			if (null == favProjects) {
				favProjects = "0";
			}
			portfolioScoreCardFilterDTO.setFavProjects(favProjects);
			if (null != portfolioScoreCardFilterDTO.getSegment()
					&& !portfolioScoreCardFilterDTO.getSegment().equalsIgnoreCase("")
					&& !portfolioScoreCardFilterDTO.getSegment().contains(",")) {
				segment = portfolioScoreCardFilterDTO.getSegment();
				prodSegmentList = iPortfolioScoreCardDAO.getScoreCardProdSegmentForPublish(portfolioScoreCardFilterDTO);
				prodAreaList = iPortfolioScoreCardDAO.getScoreCardProdAreaForPublish("Production",
						portfolioScoreCardFilterDTO);
				prodHighlightsList = iPortfolioScoreCardDAO
						.getScoreCardProdHighlightsForPublish(portfolioScoreCardFilterDTO);
				installSegmentList = iPortfolioScoreCardDAO
						.getScoreCardInstallSegmentForPublish(portfolioScoreCardFilterDTO);
				installAreaList = iPortfolioScoreCardDAO.getScoreCardProdAreaForPublish("Installation",
						portfolioScoreCardFilterDTO);
				installHighlightsList = iPortfolioScoreCardDAO
						.getScoreCardInstallHighlightsForPublish(portfolioScoreCardFilterDTO);
				lastUpdatedDetails = iPortfolioScoreCardDAO.getLastUpdatedDetails("Publish",
						portfolioScoreCardFilterDTO);
				if (!segment.equalsIgnoreCase("0")) {
					showBookProjects = "N";
				}
			}
			responseMap.put("prodSegmentList", prodSegmentList);
			responseMap.put("prodAreaList", prodAreaList);
			responseMap.put("prodHighlightsList", prodHighlightsList);
			responseMap.put("installSegmentList", installSegmentList);
			responseMap.put("installAreaList", installAreaList);
			responseMap.put("installHighlightsList", installHighlightsList);
			responseMap.put("lastUpdatedDetails", lastUpdatedDetails);
			responseMap.put("showBookProjects", showBookProjects);
		} catch (Exception e) {
			log.error("Exception in PortfolioScoreCardServiceImpl :: getPortfolioScoreCardPublishedDetails() :: "
					+ e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getPortfolioScoreCardLiveDetails(
			PortfolioScoreCardFilterDTO portfolioScoreCardFilterDTO) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<PortfolioScoreCardSegmentDTO> prodSegmentList = new ArrayList<PortfolioScoreCardSegmentDTO>();
		List<PortfolioScoreCardAreaDTO> prodAreaList = new ArrayList<PortfolioScoreCardAreaDTO>();
		List<PortfolioScoreCardHighlightDTO> prodHighlightsList = new ArrayList<PortfolioScoreCardHighlightDTO>();
		List<PortfolioScoreCardSegmentDTO> installSegmentList = new ArrayList<PortfolioScoreCardSegmentDTO>();
		List<PortfolioScoreCardAreaDTO> installAreaList = new ArrayList<PortfolioScoreCardAreaDTO>();
		List<PortfolioScoreCardHighlightDTO> installHighlightsList = new ArrayList<PortfolioScoreCardHighlightDTO>();
		List<BookedProjectsDTO> bookedProjectsList = new ArrayList<BookedProjectsDTO>();
		LastUpdatedNameDTO lastUpdatedDetails = new LastUpdatedNameDTO();
		String favProjects = "0", segment = "0", showBookProjects = "Y";
		try {
			favProjects = commonService.fetchFavProjects();
			if (null == favProjects) {
				favProjects = "0";
			}
			portfolioScoreCardFilterDTO.setFavProjects(favProjects);
			if (null != portfolioScoreCardFilterDTO.getSegment()
					&& !portfolioScoreCardFilterDTO.getSegment().equalsIgnoreCase("")
					&& !portfolioScoreCardFilterDTO.getSegment().contains(",")) {
				segment = portfolioScoreCardFilterDTO.getSegment();
				prodSegmentList = iPortfolioScoreCardDAO
						.getScoreCardProdSegmentForLiveData(portfolioScoreCardFilterDTO);
				prodAreaList = iPortfolioScoreCardDAO.getScoreCardProdAreaForLiveData(portfolioScoreCardFilterDTO,
						"Production");
				prodHighlightsList = iPortfolioScoreCardDAO
						.getScoreCardProdHighlightsForLiveData(portfolioScoreCardFilterDTO);
				installSegmentList = iPortfolioScoreCardDAO
						.getScoreCardInstallSegmentForLiveData(portfolioScoreCardFilterDTO);
				installAreaList = iPortfolioScoreCardDAO.getScoreCardProdAreaForLiveData(portfolioScoreCardFilterDTO,
						"Installation");
				installHighlightsList = iPortfolioScoreCardDAO
						.getScoreCardInstallHighlightsForLiveData(portfolioScoreCardFilterDTO);
				bookedProjectsList = iPortfolioScoreCardDAO.getBookedProjectsDetails();
				lastUpdatedDetails = iPortfolioScoreCardDAO.getLastUpdatedDetails("Live", portfolioScoreCardFilterDTO);
				if (!segment.equalsIgnoreCase("0")) {
					showBookProjects = "N";
				}
			}
			responseMap.put("prodSegmentList", prodSegmentList);
			responseMap.put("prodAreaList", prodAreaList);
			responseMap.put("prodHighlightsList", prodHighlightsList);
			responseMap.put("installSegmentList", installSegmentList);
			responseMap.put("installAreaList", installAreaList);
			responseMap.put("installHighlightsList", installHighlightsList);
			responseMap.put("bookedProjectsList", bookedProjectsList);
			responseMap.put("lastUpdatedDetails", lastUpdatedDetails);
			responseMap.put("showBookProjects", showBookProjects);
		} catch (Exception e) {
			log.error("Exception in PortfolioScoreCardServiceImpl :: getScoreCardProdSegmentForLiveData() :: "
					+ e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getPortfolioScoreCardManageScorecardDetails(
			PortfolioScoreCardFilterDTO portfolioScoreCardFilterDTO) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<PortfolioScoreCardManageScorecardDTO> manageScorecardDetails = new ArrayList<PortfolioScoreCardManageScorecardDTO>();
		LastUpdatedNameDTO lastUpdatedDetails = new LastUpdatedNameDTO();
		try {
			if (null != portfolioScoreCardFilterDTO.getSegment()
					&& !portfolioScoreCardFilterDTO.getSegment().equalsIgnoreCase("")
					&& !portfolioScoreCardFilterDTO.getSegment().contains(",")) {
				String favProjects = commonService.fetchFavProjects();
				if (null == favProjects) {
					favProjects = "0";
				}
				portfolioScoreCardFilterDTO.setFavProjects(favProjects);
				manageScorecardDetails = iPortfolioScoreCardDAO
						.getPortfolioScoreCardManageScorecardDetails(portfolioScoreCardFilterDTO);
				lastUpdatedDetails = iPortfolioScoreCardDAO.getLastUpdatedDetails("ManageSC",
						portfolioScoreCardFilterDTO);
			}
			responseMap.put("manageScorecardDetails", manageScorecardDetails);
			responseMap.put("lastUpdatedDetails", lastUpdatedDetails);

		} catch (Exception e) {
			log.error("Exception in PortfolioScoreCardServiceImpl :: getPortfolioScoreCardManageScorecardDetails() :: "
					+ e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> savePortfolioScoreCardManageScorecardDetails(String selectedSegment,
			List<PortfolioScoreCardManageScorecardDTO> portfolioScoreCardManageScorecardDTOList) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		boolean updateFlag = true;
		try {
			if (null != selectedSegment && !selectedSegment.equalsIgnoreCase("") && !selectedSegment.contains(",")) {
				String sso = callerContext.getName();
				updateFlag = iPortfolioScoreCardDAO.savePortfolioScoreCardManageScorecardDetails(selectedSegment,
						portfolioScoreCardManageScorecardDTOList, sso);
				if (updateFlag) {
					responseMap.put("status", PortfolioScoreCardConstants.STATUS_SUCCESS);
					responseMap.put("msg", "Data saved successfully");
				} else {
					responseMap.put("status", PortfolioScoreCardConstants.STATUS_ERROR);
					responseMap.put("msg", "Error while saving data");
				}
			} else {
				responseMap.put("status", PortfolioScoreCardConstants.STATUS_ERROR);
				responseMap.put("msg", "Error while saving data");
			}
		} catch (Exception e) {
			responseMap.put("status", PortfolioScoreCardConstants.STATUS_ERROR);
			responseMap.put("msg", "Error while saving data");
			log.error("Exception in PortfolioScoreCardServiceImpl :: savePortfolioScoreCardManageScorecardDetails() :: "
					+ e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> savePortfolioScoreCardLiveDetails(String selectedSegment,
			PortfolioScoreCardDetailsDTO portfolioScoreCardDetailsDTO) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<PortfolioScoreCardSegmentDTO> prodSegmentList = new ArrayList<PortfolioScoreCardSegmentDTO>();
		List<PortfolioScoreCardAreaDTO> prodAreaList = new ArrayList<PortfolioScoreCardAreaDTO>();
		List<PortfolioScoreCardHighlightDTO> prodHighlightsList = new ArrayList<PortfolioScoreCardHighlightDTO>();
		List<PortfolioScoreCardSegmentDTO> installSegmentList = new ArrayList<PortfolioScoreCardSegmentDTO>();
		List<PortfolioScoreCardAreaDTO> installAreaList = new ArrayList<PortfolioScoreCardAreaDTO>();
		List<PortfolioScoreCardHighlightDTO> installHighlightsList = new ArrayList<PortfolioScoreCardHighlightDTO>();
		boolean saveFlag = true;
		String sso = callerContext.getName();
		try {
			if (null != selectedSegment && !selectedSegment.equalsIgnoreCase("") && !selectedSegment.contains(",")) {
				prodSegmentList = portfolioScoreCardDetailsDTO.getProdSegmentList();
				prodAreaList = portfolioScoreCardDetailsDTO.getProdAreaList();
				prodHighlightsList = portfolioScoreCardDetailsDTO.getProdHighlightsList();
				installSegmentList = portfolioScoreCardDetailsDTO.getInstallSegmentList();
				installAreaList = portfolioScoreCardDetailsDTO.getInstallAreaList();
				installHighlightsList = portfolioScoreCardDetailsDTO.getInstallHighlightsList();

				if (saveFlag) {
					saveFlag = iPortfolioScoreCardDAO.savePortfolioScoreCardProdSegmentDetails(selectedSegment,
							prodSegmentList, sso);
				}
				if (saveFlag) {
					saveFlag = iPortfolioScoreCardDAO.savePortfolioScoreCardProdAreaDetails(selectedSegment,
							prodAreaList, sso);
				}
				if (saveFlag) {
					saveFlag = iPortfolioScoreCardDAO.savePortfolioScoreCardProdHightlightDetails(selectedSegment,
							prodHighlightsList, sso);
				}
				if (saveFlag) {
					saveFlag = iPortfolioScoreCardDAO.savePortfolioScoreCardInstSegmentDetails(selectedSegment,
							installSegmentList, sso);
				}
				if (saveFlag) {
					saveFlag = iPortfolioScoreCardDAO.savePortfolioScoreCardInstAreaDetails(selectedSegment,
							installAreaList, sso);
				}
				if (saveFlag) {
					saveFlag = iPortfolioScoreCardDAO.savePortfolioScoreCardInstHightlightDetails(selectedSegment,
							installHighlightsList, sso);
				}

				if (saveFlag) {
					responseMap.put("status", PortfolioScoreCardConstants.STATUS_SUCCESS);
					responseMap.put("msg", "Data saved successfully");
				} else {
					responseMap.put("status", PortfolioScoreCardConstants.STATUS_ERROR);
					responseMap.put("msg", "Error while saving data");
				}
			} else {
				responseMap.put("status", PortfolioScoreCardConstants.STATUS_ERROR);
				responseMap.put("msg", "Error while saving data");
			}

		} catch (Exception e) {
			log.error("Exception in PortfolioScoreCardServiceImpl::savePortfolioScoreCardLiveDetails() : "
					+ e.getMessage());
			responseMap.put("status", PortfolioScoreCardConstants.STATUS_ERROR);
			responseMap.put("msg", "Error while saving data");
		}

		return responseMap;
	}

	@Override
	public Map<String, Object> publishPortfolioScoreCardLiveDetails(String selectedSegment,
			PortfolioScoreCardDetailsDTO portfolioScoreCardDetailsDTO) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<PortfolioScoreCardSegmentDTO> prodSegmentList = new ArrayList<PortfolioScoreCardSegmentDTO>();
		List<PortfolioScoreCardAreaDTO> prodAreaList = new ArrayList<PortfolioScoreCardAreaDTO>();
		List<PortfolioScoreCardHighlightDTO> prodHighlightsList = new ArrayList<PortfolioScoreCardHighlightDTO>();
		List<PortfolioScoreCardSegmentDTO> installSegmentList = new ArrayList<PortfolioScoreCardSegmentDTO>();
		List<PortfolioScoreCardAreaDTO> installAreaList = new ArrayList<PortfolioScoreCardAreaDTO>();
		List<PortfolioScoreCardHighlightDTO> installHighlightsList = new ArrayList<PortfolioScoreCardHighlightDTO>();
		boolean publishFlag = true, saveFlag = true;

		try {
			if (null != selectedSegment && !selectedSegment.equalsIgnoreCase("") && !selectedSegment.contains(",")) {
				String sso = callerContext.getName();
				prodSegmentList = portfolioScoreCardDetailsDTO.getProdSegmentList();
				prodAreaList = portfolioScoreCardDetailsDTO.getProdAreaList();
				prodHighlightsList = portfolioScoreCardDetailsDTO.getProdHighlightsList();
				installSegmentList = portfolioScoreCardDetailsDTO.getInstallSegmentList();
				installAreaList = portfolioScoreCardDetailsDTO.getInstallAreaList();
				installHighlightsList = portfolioScoreCardDetailsDTO.getInstallHighlightsList();

				if (saveFlag) {
					saveFlag = iPortfolioScoreCardDAO.savePortfolioScoreCardProdSegmentDetails(selectedSegment,
							prodSegmentList, sso);
				}
				if (saveFlag) {
					saveFlag = iPortfolioScoreCardDAO.savePortfolioScoreCardProdAreaDetails(selectedSegment,
							prodAreaList, sso);
				}
				if (saveFlag) {
					saveFlag = iPortfolioScoreCardDAO.savePortfolioScoreCardProdHightlightDetails(selectedSegment,
							prodHighlightsList, sso);
				}
				if (saveFlag) {
					saveFlag = iPortfolioScoreCardDAO.savePortfolioScoreCardInstSegmentDetails(selectedSegment,
							installSegmentList, sso);
				}
				if (saveFlag) {
					saveFlag = iPortfolioScoreCardDAO.savePortfolioScoreCardInstAreaDetails(selectedSegment,
							installAreaList, sso);
				}
				if (saveFlag) {
					saveFlag = iPortfolioScoreCardDAO.savePortfolioScoreCardInstHightlightDetails(selectedSegment,
							installHighlightsList, sso);
				}

				if (saveFlag && publishFlag) {
					publishFlag = iPortfolioScoreCardDAO.publishPortfolioScoreCardProdSegmentDetails(selectedSegment,
							prodSegmentList, sso);
				}
				if (saveFlag && publishFlag) {
					publishFlag = iPortfolioScoreCardDAO.publishPortfolioScoreCardProdAreaDetails(selectedSegment,
							prodAreaList, sso);
				}
				if (saveFlag && publishFlag) {
					publishFlag = iPortfolioScoreCardDAO.publishPortfolioScoreCardProdHightlightDetails(selectedSegment,
							prodHighlightsList, sso);
				}
				if (saveFlag && publishFlag) {
					publishFlag = iPortfolioScoreCardDAO.publishPortfolioScoreCardInstSegmentDetails(selectedSegment,
							installSegmentList, sso);
				}
				if (saveFlag && publishFlag) {
					publishFlag = iPortfolioScoreCardDAO.publishPortfolioScoreCardInstAreaDetails(selectedSegment,
							installAreaList, sso);
				}
				if (saveFlag && publishFlag) {
					publishFlag = iPortfolioScoreCardDAO.publishPortfolioScoreCardInstHighlightDetails(selectedSegment,
							installHighlightsList, sso);
				}

				if (publishFlag && saveFlag) {
					responseMap.put("status", PortfolioScoreCardConstants.STATUS_SUCCESS);
					responseMap.put("msg", "Data publish successfully");
				} else {
					responseMap.put("status", PortfolioScoreCardConstants.STATUS_ERROR);
					responseMap.put("msg", "Error while publishing data");
				}
			} else {
				responseMap.put("status", PortfolioScoreCardConstants.STATUS_ERROR);
				responseMap.put("msg", "Error while publishing data");
			}

		} catch (Exception e) {
			log.error("Exception in PortfolioScoreCardServiceImpl :: publishPortfolioScoreCardLiveDetails() :: "
					+ e.getMessage());
			responseMap.put("status", PortfolioScoreCardConstants.STATUS_ERROR);
			responseMap.put("msg", "Error while publishing data");
		}
		return responseMap;
	}

}

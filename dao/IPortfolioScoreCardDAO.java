package com.bh.realtrack.dao;

import java.util.List;

import com.bh.realtrack.dto.BookedProjectsDTO;
import com.bh.realtrack.dto.LastUpdatedNameDTO;
import com.bh.realtrack.dto.PortfolioScoreCardAreaDTO;
import com.bh.realtrack.dto.PortfolioScoreCardFilterDTO;
import com.bh.realtrack.dto.PortfolioScoreCardHighlightDTO;
import com.bh.realtrack.dto.PortfolioScoreCardManageScorecardDTO;
import com.bh.realtrack.dto.PortfolioScoreCardSegmentDTO;

public interface IPortfolioScoreCardDAO {

	List<String> getPortfolioScoreCardFilters(PortfolioScoreCardFilterDTO portfolioScoreCardFilterDTO);

	String getPortfolioScoreCardFiltersDate(PortfolioScoreCardFilterDTO portfolioScoreCardFilterDTO);

	List<PortfolioScoreCardSegmentDTO> getScoreCardProdSegmentForPublish(
			PortfolioScoreCardFilterDTO portfolioScoreCardFilterDTO);

	List<PortfolioScoreCardAreaDTO> getScoreCardProdAreaForPublish(String section,
			PortfolioScoreCardFilterDTO portfolioScoreCardFilterDTO);

	List<PortfolioScoreCardHighlightDTO> getScoreCardProdHighlightsForPublish(
			PortfolioScoreCardFilterDTO portfolioScoreCardFilterDTO);

	List<PortfolioScoreCardSegmentDTO> getScoreCardInstallSegmentForPublish(
			PortfolioScoreCardFilterDTO portfolioScoreCardFilterDTO);

	List<PortfolioScoreCardHighlightDTO> getScoreCardInstallHighlightsForPublish(
			PortfolioScoreCardFilterDTO portfolioScoreCardFilterDTO);

	List<BookedProjectsDTO> getBookedProjectsDetails();

	List<PortfolioScoreCardSegmentDTO> getScoreCardProdSegmentForLiveData(
			PortfolioScoreCardFilterDTO portfolioScoreCardFilterDTO);

	List<PortfolioScoreCardAreaDTO> getScoreCardProdAreaForLiveData(
			PortfolioScoreCardFilterDTO portfolioScoreCardFilterDTO, String section);

	List<PortfolioScoreCardHighlightDTO> getScoreCardProdHighlightsForLiveData(
			PortfolioScoreCardFilterDTO portfolioScoreCardFilterDTO);

	List<PortfolioScoreCardSegmentDTO> getScoreCardInstallSegmentForLiveData(
			PortfolioScoreCardFilterDTO portfolioScoreCardFilterDTO);

	List<PortfolioScoreCardHighlightDTO> getScoreCardInstallHighlightsForLiveData(
			PortfolioScoreCardFilterDTO portfolioScoreCardFilterDTO);

	List<PortfolioScoreCardManageScorecardDTO> getPortfolioScoreCardManageScorecardDetails(
			PortfolioScoreCardFilterDTO portfolioScoreCardFilterDTO);

	boolean savePortfolioScoreCardManageScorecardDetails(String selectedSegment,
			List<PortfolioScoreCardManageScorecardDTO> portfolioScoreCardManageScorecardDTOList, String sso);

	boolean savePortfolioScoreCardProdSegmentDetails(String selectedSegment,
			List<PortfolioScoreCardSegmentDTO> prodSegmentList, String sso);

	boolean savePortfolioScoreCardProdAreaDetails(String selectedSegment, List<PortfolioScoreCardAreaDTO> prodAreaList,
			String sso);

	boolean savePortfolioScoreCardProdHightlightDetails(String selectedSegment,
			List<PortfolioScoreCardHighlightDTO> prodHighlightsList, String sso);

	boolean savePortfolioScoreCardInstSegmentDetails(String selectedSegment,
			List<PortfolioScoreCardSegmentDTO> installSegmentList, String sso);

	boolean savePortfolioScoreCardInstAreaDetails(String selectedSegment, List<PortfolioScoreCardAreaDTO> prodAreaList,
			String sso);

	boolean savePortfolioScoreCardInstHightlightDetails(String selectedSegment,
			List<PortfolioScoreCardHighlightDTO> installHighlightsList, String sso);

	boolean publishPortfolioScoreCardProdSegmentDetails(String selectedSegment,
			List<PortfolioScoreCardSegmentDTO> prodSegmentList, String sso);

	boolean publishPortfolioScoreCardProdAreaDetails(String selectedSegment,
			List<PortfolioScoreCardAreaDTO> prodAreaList, String sso);

	boolean publishPortfolioScoreCardProdHightlightDetails(String selectedSegment,
			List<PortfolioScoreCardHighlightDTO> prodHighlightsList, String sso);

	boolean publishPortfolioScoreCardInstSegmentDetails(String selectedSegment,
			List<PortfolioScoreCardSegmentDTO> installSegmentList, String sso);

	boolean publishPortfolioScoreCardInstAreaDetails(String selectedSegment,
			List<PortfolioScoreCardAreaDTO> installAreaList, String sso);

	boolean publishPortfolioScoreCardInstHighlightDetails(String selectedSegment,
			List<PortfolioScoreCardHighlightDTO> installHighlightsList, String sso);

	LastUpdatedNameDTO getLastUpdatedDetails(String module, PortfolioScoreCardFilterDTO portfolioScoreCardFilterDTO);

}

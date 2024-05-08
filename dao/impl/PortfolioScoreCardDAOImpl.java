package com.bh.realtrack.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bh.realtrack.dao.IPortfolioScoreCardDAO;
import com.bh.realtrack.dto.BookedProjectsDTO;
import com.bh.realtrack.dto.LastUpdatedNameDTO;
import com.bh.realtrack.dto.PortfolioScoreCardAreaDTO;
import com.bh.realtrack.dto.PortfolioScoreCardFilterDTO;
import com.bh.realtrack.dto.PortfolioScoreCardHighlightDTO;
import com.bh.realtrack.dto.PortfolioScoreCardManageScorecardDTO;
import com.bh.realtrack.dto.PortfolioScoreCardSegmentDTO;
import com.bh.realtrack.util.PortfolioScoreCardConstants;

@Repository
public class PortfolioScoreCardDAOImpl implements IPortfolioScoreCardDAO {

	private static final Logger log = LoggerFactory.getLogger(PortfolioScoreCardDAOImpl.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public List<String> getPortfolioScoreCardFilters(PortfolioScoreCardFilterDTO portfolioScoreCardFilterDTO) {
		List<String> list = new ArrayList<String>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(PortfolioScoreCardConstants.GET_PORTFOLIO_SCORE_CARD_FILTER_DETAILS)) {
			pstm.setString(1, portfolioScoreCardFilterDTO.getSegment());
			pstm.setString(2, portfolioScoreCardFilterDTO.getSegment());
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				list.add(rs.getString("showby"));
			}
		} catch (SQLException e) {
			log.error("Exception in PortfolioScoreCardDAOImpl :: getPortfolioScoreCardFilters() :: " + e.getMessage());
		}
		return list;
	}

	@Override
	public String getPortfolioScoreCardFiltersDate(PortfolioScoreCardFilterDTO portfolioScoreCardFilterDTO) {
		String date = "";
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(
						PortfolioScoreCardConstants.GET_DATE_FOR_PORTFOLIO_SCORE_CARD_FILTER_DETAILS)) {
			pstm.setString(1, portfolioScoreCardFilterDTO.getSegment());
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				date = (rs.getString("published_dt"));
			}
		} catch (SQLException e) {
			log.error("Exception in PortfolioScoreCardDAOImpl :: getPortfolioScoreCardFiltersDate() :: "
					+ e.getMessage());
		}
		return date;
	}

	@Override
	public List<PortfolioScoreCardSegmentDTO> getScoreCardProdSegmentForPublish(
			PortfolioScoreCardFilterDTO portfolioScoreCardFilterDTO) {
		List<PortfolioScoreCardSegmentDTO> list = new ArrayList<PortfolioScoreCardSegmentDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(
						PortfolioScoreCardConstants.GET_PORTFOLIO_SCORE_CARD_PRODUCTION_SEGMENT_DETAILS_FOR_PUBLISH)) {
			pstm.setString(1, portfolioScoreCardFilterDTO.getSegment());
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				PortfolioScoreCardSegmentDTO dto = new PortfolioScoreCardSegmentDTO();
				dto.setSegment(null != rs.getString("o_segment") ? rs.getString("o_segment") : "");
				dto.setProjectCnt(rs.getDouble("o_proj_count"));
				dto.setContractVal(rs.getDouble("o_contract_val"));
				dto.setTrend(null != rs.getString("o_trend") ? rs.getString("o_trend") : "");
				dto.setCmAS(rs.getDouble("o_cm_as_per"));
				dto.setCmAD(rs.getDouble("o_cm_ad_per"));
				if (null != dto.getSegment() && dto.getSegment().equalsIgnoreCase("TOTAL")
						&& dto.getProjectCnt() != 0) {
					list.add(dto);
				} else if (null != dto.getSegment() && !dto.getSegment().equalsIgnoreCase("TOTAL")) {
					list.add(dto);
				}
			}
		} catch (SQLException e) {
			log.error("Exception in PortfolioScoreCardDAOImpl :: getScoreCardProdSegmentForPublish() :: "
					+ e.getMessage());
		}
		return list;
	}

	@Override
	public List<PortfolioScoreCardAreaDTO> getScoreCardProdAreaForPublish(String section,
			PortfolioScoreCardFilterDTO portfolioScoreCardFilterDTO) {
		List<PortfolioScoreCardAreaDTO> list = new ArrayList<PortfolioScoreCardAreaDTO>();
		String query = "";
		switch (section) {
		case "Production":
			query = PortfolioScoreCardConstants.GET_PORTFOLIO_SCORE_CARD_PRODUCTION_AREA_DETAILS_FOR_PUBLISH;
			break;
		case "Installation":
			query = PortfolioScoreCardConstants.GET_PORTFOLIO_SCORE_CARD_INSTALLATION_AREA_DETAILS_FOR_PUBLISH;
			break;
		}
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(query)) {
			pstm.setString(1, portfolioScoreCardFilterDTO.getSegment());
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				PortfolioScoreCardAreaDTO dto = new PortfolioScoreCardAreaDTO();
				dto.setArea(null != rs.getString("o_area") ? rs.getString("o_area") : "");
				dto.setTrend(null != rs.getString("o_trend") ? rs.getString("o_trend") : "");
				dto.setTrendColor(null != rs.getString("o_trend_color") ? rs.getString("o_trend_color") : "");
				dto.setGreenCnt(rs.getInt("o_green_cnt"));
				dto.setGreenPer(rs.getFloat("o_green_per"));
				dto.setYellowCnt(rs.getInt("o_yellow_cnt"));
				dto.setYellowPer(rs.getFloat("o_yellow_per"));
				dto.setRedCnt(rs.getInt("o_quality_red_cnt"));
				dto.setRedPer(rs.getFloat("o_red_per"));
				dto.setHighlights(null != rs.getString("o_highlights") ? rs.getString("o_highlights") : "");
				list.add(dto);
			}
		} catch (Exception e) {
			log.error(
					"Exception in PortfolioScoreCardDAOImpl :: getScoreCardProdAreaForPublish() :: " + e.getMessage());
		}
		return list;
	}

	@Override
	public List<PortfolioScoreCardHighlightDTO> getScoreCardProdHighlightsForPublish(
			PortfolioScoreCardFilterDTO portfolioScoreCardFilterDTO) {
		List<PortfolioScoreCardHighlightDTO> list = new ArrayList<PortfolioScoreCardHighlightDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(
						PortfolioScoreCardConstants.GET_PORTFOLIO_SCORE_CARD_PRODUCTION_HIGHLIGHTS_DETAILS_FOR_PUBLISH)) {
			pstm.setString(1, portfolioScoreCardFilterDTO.getSegment());
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				PortfolioScoreCardHighlightDTO dto = new PortfolioScoreCardHighlightDTO();
				dto.setHighlights(null != rs.getString("o_highlights") ? rs.getString("o_highlights") : "");
				dto.setHighlightImage(
						null != rs.getString("o_highlights_images") ? rs.getString("o_highlights_images") : "");
				dto.setNewProjectAwarded(
						null != rs.getString("o_new_projects_awarded") ? rs.getString("o_new_projects_awarded") : "");
				list.add(dto);
			}
		} catch (Exception e) {
			log.error("Exception in PortfolioScoreCardDAOImpl :: getScoreCardProdHighlightsForPublish() :: "
					+ e.getMessage());
		}
		return list;
	}

	@Override
	public List<PortfolioScoreCardSegmentDTO> getScoreCardInstallSegmentForPublish(
			PortfolioScoreCardFilterDTO portfolioScoreCardFilterDTO) {
		List<PortfolioScoreCardSegmentDTO> list = new ArrayList<PortfolioScoreCardSegmentDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(
						PortfolioScoreCardConstants.GET_PORTFOLIO_SCORE_CARD_INSTALLATION_SEGMENT_DETAILS_FOR_PUBLISH)) {
			pstm.setString(1, portfolioScoreCardFilterDTO.getSegment());
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				PortfolioScoreCardSegmentDTO dto = new PortfolioScoreCardSegmentDTO();
				dto.setSegment(null != rs.getString("o_segment") ? rs.getString("o_segment") : "");
				dto.setProjectCnt(rs.getDouble("o_proj_count"));
				dto.setContractVal(rs.getDouble("o_contract_val"));
				dto.setTrend(null != rs.getString("o_trend") ? rs.getString("o_trend") : "");
				if (null != dto.getSegment() && dto.getSegment().equalsIgnoreCase("TOTAL")
						&& dto.getProjectCnt() != 0) {
					list.add(dto);
				} else if (null != dto.getSegment() && !dto.getSegment().equalsIgnoreCase("TOTAL")) {
					list.add(dto);
				}
			}
		} catch (SQLException e) {
			log.error("Exception in PortfolioScoreCardDAOImpl :: getScoreCardInstallSegmentForPublish() :: "
					+ e.getMessage());
		}
		return list;
	}

	@Override
	public List<PortfolioScoreCardHighlightDTO> getScoreCardInstallHighlightsForPublish(
			PortfolioScoreCardFilterDTO portfolioScoreCardFilterDTO) {
		List<PortfolioScoreCardHighlightDTO> list = new ArrayList<PortfolioScoreCardHighlightDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(
						PortfolioScoreCardConstants.GET_PORTFOLIO_SCORE_CARD_INSTALLATION_HIGHLIGHTS_DETAILS_FOR_PUBLISH)) {
			pstm.setString(1, portfolioScoreCardFilterDTO.getSegment());
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				PortfolioScoreCardHighlightDTO dto = new PortfolioScoreCardHighlightDTO();
				dto.setHighlights(null != rs.getString("o_highlights") ? rs.getString("o_highlights") : "");
				dto.setHighlightImage(
						null != rs.getString("o_highlights_images") ? rs.getString("o_highlights_images") : "");
				list.add(dto);
			}
		} catch (Exception e) {
			log.error("Exception in PortfolioScoreCardDAOImpl :: getScoreCardInstallHighlightsForPublish() :: "
					+ e.getMessage());
		}
		return list;
	}

	@Override
	public List<BookedProjectsDTO> getBookedProjectsDetails() {
		List<BookedProjectsDTO> list = new ArrayList<BookedProjectsDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(PortfolioScoreCardConstants.GET_PORTFOLIO_SCORE_CARD_BOOKED_PROJECTS)) {
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				BookedProjectsDTO dto = new BookedProjectsDTO();
				dto.setProjectId(null != rs.getString("project_id") ? rs.getString("project_id") : "");
				dto.setProjectName(null != rs.getString("project_name") ? rs.getString("project_name") : "");
				dto.setSegment(null != rs.getString("segment") ? rs.getString("segment") : "");
				list.add(dto);
			}
		} catch (Exception e) {
			log.error("Exception in PortfolioScoreCardDAOImpl :: getBookedProjectsDetails() :: " + e.getMessage());
		}
		return list;
	}

	@Override
	public LastUpdatedNameDTO getLastUpdatedDetails(String module,
			PortfolioScoreCardFilterDTO portfolioScoreCardFilterDTO) {
		LastUpdatedNameDTO dto = new LastUpdatedNameDTO();
		String query = "";
		switch (module) {
		case "Publish":
			query = PortfolioScoreCardConstants.GET_PORTFOLIO_SCORE_CARD_LAST_UPDATED_DETAILS_FOR_PUBLISH;
			break;
		case "Live":
			query = PortfolioScoreCardConstants.GET_PORTFOLIO_SCORE_CARD_LAST_UPDATED_DETAILS_FOR_LIVE;
			break;
		case "ManageSC":
			query = PortfolioScoreCardConstants.GET_PORTFOLIO_SCORE_CARD_LAST_UPDATED_DETAILS_FOR_MANAGE_SCORECARD;
			break;
		}
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(query);) {
			if (null != module && !module.equalsIgnoreCase("")
					&& (module.equalsIgnoreCase("Live") || module.equalsIgnoreCase("Publish"))) {
				pstm.setString(1, portfolioScoreCardFilterDTO.getSegment());
			}
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				dto.setLastUpdatedBy(null != rs.getString("last_update_by") ? rs.getString("last_update_by") : "");
				dto.setLastUpdatedDate(null != rs.getString("last_update_dt") ? rs.getString("last_update_dt") : "");
				dto.setLastUpdatedName(null != rs.getString("name") ? rs.getString("name") : "");
			}
		} catch (Exception e) {
			log.error("Exception in PortfolioScoreCardDAOImpl :: getLastUpdatedDetails() :: " + e.getMessage());
		}
		return dto;
	}

	@Override
	public List<PortfolioScoreCardSegmentDTO> getScoreCardProdSegmentForLiveData(
			PortfolioScoreCardFilterDTO portfolioScoreCardFilterDTO) {
		List<PortfolioScoreCardSegmentDTO> list = new ArrayList<PortfolioScoreCardSegmentDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(
						PortfolioScoreCardConstants.GET_PORTFOLIO_SCORE_CARD_PRODUCTION_SEGMENT_DETAILS_FOR_LIVE)) {
			pstm.setString(1, portfolioScoreCardFilterDTO.getShowBy());
			pstm.setString(2, portfolioScoreCardFilterDTO.getSegment());
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				PortfolioScoreCardSegmentDTO dto = new PortfolioScoreCardSegmentDTO();
				dto.setSegment(null != rs.getString("o_segment") ? rs.getString("o_segment") : "");
				dto.setProjectCnt(rs.getInt("o_proj_count"));
				dto.setContractVal(rs.getDouble("o_contract_val"));
				dto.setTrend(null != rs.getString("o_trend") ? rs.getString("o_trend") : "");
				dto.setCmAS(rs.getDouble("o_cm_as_per"));
				dto.setCmAD(rs.getDouble("o_cm_ad_per"));
				if (null != dto.getSegment() && dto.getSegment().equalsIgnoreCase("TOTAL")
						&& dto.getProjectCnt() != 0) {
					list.add(dto);
				} else if (null != dto.getSegment() && !dto.getSegment().equalsIgnoreCase("TOTAL")) {
					list.add(dto);
				}
			}
		} catch (SQLException e) {
			log.error("Exception in PortfolioScoreCardDAOImpl :: getScoreCardProdSegmentForLiveData() :: "
					+ e.getMessage());
		}
		return list;
	}

	@Override
	public List<PortfolioScoreCardAreaDTO> getScoreCardProdAreaForLiveData(
			PortfolioScoreCardFilterDTO portfolioScoreCardFilterDTO, String section) {
		List<PortfolioScoreCardAreaDTO> list = new ArrayList<PortfolioScoreCardAreaDTO>();
		String query = "";
		switch (section) {
		case "Production":
			query = PortfolioScoreCardConstants.GET_PORTFOLIO_SCORE_CARD_PRODUCTION_AREA_DETAILS_FOR_LIVE;
			break;
		case "Installation":
			query = PortfolioScoreCardConstants.GET_PORTFOLIO_SCORE_CARD_INSTALLATION_AREA_DETAILS_FOR_LIVE;
			break;
		}
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(query)) {
			pstm.setString(1, portfolioScoreCardFilterDTO.getShowBy());
			pstm.setString(2, portfolioScoreCardFilterDTO.getSegment());
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				PortfolioScoreCardAreaDTO dto = new PortfolioScoreCardAreaDTO();
				dto.setArea(null != rs.getString("o_area") ? rs.getString("o_area") : "");
				dto.setTrend(null != rs.getString("o_trend") ? rs.getString("o_trend") : "");
				dto.setTrendColor(null != rs.getString("o_trend_color") ? rs.getString("o_trend_color") : "");
				dto.setGreenCnt(rs.getInt("o_green_cnt"));
				dto.setGreenPer(rs.getFloat("o_green_per"));
				dto.setYellowCnt(rs.getInt("o_yellow_cnt"));
				dto.setYellowPer(rs.getFloat("o_yellow_per"));
				dto.setRedCnt(rs.getInt("o_red_cnt"));
				dto.setRedPer(rs.getFloat("o_red_per"));
				dto.setHighlights(null != rs.getString("o_highlights") ? rs.getString("o_highlights") : "");
				list.add(dto);
			}
		} catch (Exception e) {
			log.error(
					"Exception in PortfolioScoreCardDAOImpl :: getScoreCardProdAreaForLiveData() :: " + e.getMessage());
		}
		return list;
	}

	@Override
	public List<PortfolioScoreCardHighlightDTO> getScoreCardProdHighlightsForLiveData(
			PortfolioScoreCardFilterDTO portfolioScoreCardFilterDTO) {
		List<PortfolioScoreCardHighlightDTO> list = new ArrayList<PortfolioScoreCardHighlightDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(
						PortfolioScoreCardConstants.GET_PORTFOLIO_SCORE_CARD_PRODUCTION_HIGHLIGHTS_DETAILS_FOR_LIVE)) {
			pstm.setString(1, portfolioScoreCardFilterDTO.getShowBy());
			pstm.setString(2, portfolioScoreCardFilterDTO.getSegment());
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				PortfolioScoreCardHighlightDTO dto = new PortfolioScoreCardHighlightDTO();
				dto.setHighlights(null != rs.getString("o_highlights") ? rs.getString("o_highlights") : "");
				dto.setHighlightImage(
						null != rs.getString("o_highlights_images") ? rs.getString("o_highlights_images") : "");
				dto.setNewProjectAwarded(
						null != rs.getString("o_new_projects_awarded") ? rs.getString("o_new_projects_awarded") : "");
				list.add(dto);
			}
		} catch (Exception e) {
			log.error("Exception in PortfolioScoreCardDAOImpl :: getScoreCardProdHighlightsForLiveData() :: "
					+ e.getMessage());
		}
		return list;
	}

	@Override
	public List<PortfolioScoreCardSegmentDTO> getScoreCardInstallSegmentForLiveData(
			PortfolioScoreCardFilterDTO portfolioScoreCardFilterDTO) {
		List<PortfolioScoreCardSegmentDTO> list = new ArrayList<PortfolioScoreCardSegmentDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(
						PortfolioScoreCardConstants.GET_PORTFOLIO_SCORE_CARD_INSTALLATION_SEGMENT_DETAILS_FOR_LIVE)) {
			pstm.setString(1, portfolioScoreCardFilterDTO.getShowBy());
			pstm.setString(2, portfolioScoreCardFilterDTO.getSegment());
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				PortfolioScoreCardSegmentDTO dto = new PortfolioScoreCardSegmentDTO();
				dto.setSegment(null != rs.getString("o_segment") ? rs.getString("o_segment") : "");
				dto.setProjectCnt(rs.getInt("o_proj_count"));
				dto.setContractVal(rs.getDouble("o_contract_val"));
				dto.setTrend(null != rs.getString("o_trend") ? rs.getString("o_trend") : "");
				if (null != dto.getSegment() && dto.getSegment().equalsIgnoreCase("TOTAL")
						&& dto.getProjectCnt() != 0) {
					list.add(dto);
				} else if (null != dto.getSegment() && !dto.getSegment().equalsIgnoreCase("TOTAL")) {
					list.add(dto);
				}
			}
		} catch (SQLException e) {
			log.error("Exception in PortfolioScoreCardDAOImpl :: getScoreCardInstallSegmentForLiveData() :: "
					+ e.getMessage());
		}
		return list;
	}

	@Override
	public List<PortfolioScoreCardHighlightDTO> getScoreCardInstallHighlightsForLiveData(
			PortfolioScoreCardFilterDTO portfolioScoreCardFilterDTO) {
		List<PortfolioScoreCardHighlightDTO> list = new ArrayList<PortfolioScoreCardHighlightDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(
						PortfolioScoreCardConstants.GET_PORTFOLIO_SCORE_CARD_INSTALLATION_HIGHLIGHTS_DETAILS_FOR_LIVE)) {
			pstm.setString(1, portfolioScoreCardFilterDTO.getShowBy());
			pstm.setString(2, portfolioScoreCardFilterDTO.getSegment());
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				PortfolioScoreCardHighlightDTO dto = new PortfolioScoreCardHighlightDTO();
				dto.setHighlights(null != rs.getString("o_highlights") ? rs.getString("o_highlights") : "");
				dto.setHighlightImage(
						null != rs.getString("o_highlights_images") ? rs.getString("o_highlights_images") : "");
				list.add(dto);
			}
		} catch (Exception e) {
			log.error("Exception in PortfolioScoreCardDAOImpl :: getScoreCardInstallHighlightsForLiveData() :: "
					+ e.getMessage());
		}
		return list;
	}

	@Override
	public List<PortfolioScoreCardManageScorecardDTO> getPortfolioScoreCardManageScorecardDetails(
			PortfolioScoreCardFilterDTO portfolioScoreCardFilterDTO) {
		List<PortfolioScoreCardManageScorecardDTO> list = new ArrayList<PortfolioScoreCardManageScorecardDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(
						PortfolioScoreCardConstants.GET_PORTFOLIO_SCORE_CARD_MANAGE_SCORE_CARD_DETAILS)) {
			pstm.setString(1, portfolioScoreCardFilterDTO.getSegment());
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				PortfolioScoreCardManageScorecardDTO dto = new PortfolioScoreCardManageScorecardDTO();
				dto.setProductionFlag(null != rs.getString("o_prod_chk_flag") ? rs.getString("o_prod_chk_flag") : "");
				dto.setInstallationFlag(
						null != rs.getString("o_install_chk_flag") ? rs.getString("o_install_chk_flag") : "");
				dto.setProjectName(null != rs.getString("o_project_name") ? rs.getString("o_project_name") : "");
				dto.setProjectId(null != rs.getString("o_project_id") ? rs.getString("o_project_id") : "");
				dto.setSegment(null != rs.getString("o_segment") ? rs.getString("o_segment") : "");
				dto.setPhase(null != rs.getString("o_phase") ? rs.getString("o_phase") : "");
				dto.setInstallJob(null != rs.getString("o_inst_job") ? rs.getString("o_inst_job") : "");
				dto.setInstallStatus(null != rs.getString("o_instl_status") ? rs.getString("o_instl_status") : "");
				dto.setUpdatedBy(null != rs.getString("o_updated_by") ? rs.getString("o_updated_by") : "");
				dto.setUpdatedOn(null != rs.getString("o_updated_dt") ? rs.getString("o_updated_dt") : "");
				list.add(dto);
			}
		} catch (Exception e) {
			log.error("Exception in PortfolioScoreCardDAOImpl :: getPortfolioScoreCardManageScorecardDetails() :: "
					+ e.getMessage());
		}
		return list;
	}

	@Override
	public boolean savePortfolioScoreCardManageScorecardDetails(String selectedSegment,
			List<PortfolioScoreCardManageScorecardDTO> portfolioScoreCardManageScorecardDTOList, String sso) {
		boolean updateFlag = true;
		updateFlag = deletePortfolioScoreCardManageScorecard(selectedSegment);
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(
						PortfolioScoreCardConstants.SAVE_PORTFOLIO_SCORE_CARD_MANAGE_SCORE_CARD_DETAILS)) {
			for (PortfolioScoreCardManageScorecardDTO dto : portfolioScoreCardManageScorecardDTOList) {
				pstm.setString(1, dto.getProductionFlag());
				pstm.setString(2, dto.getInstallationFlag());
				pstm.setString(3, dto.getProjectName());
				pstm.setString(4, dto.getProjectId());
				pstm.setString(5, dto.getSegment());
				pstm.setString(6, dto.getPhase());
				pstm.setString(7, dto.getInstallJob());
				pstm.setString(8, dto.getInstallStatus());
				pstm.setString(9, sso);
				pstm.setString(10, sso);
				if (pstm.executeUpdate() > 0) {
					updateFlag = true;
				}
			}
		} catch (Exception e) {
			updateFlag = false;
			log.error("Exception in PortfolioScoreCardDAOImpl :: savePortfolioScoreCardManageScorecardDetails() :: "
					+ e.getMessage());
		}
		return updateFlag;
	}

	private boolean deletePortfolioScoreCardManageScorecard(String selectedSegment) {
		boolean deleteFlag = true;
		StringBuilder deleteQuery = new StringBuilder(
				PortfolioScoreCardConstants.DELETE_PORTFOLIO_SCORE_CARD_MANAGE_SCORE_CARD_DETAILS);
		if (null != selectedSegment && !selectedSegment.equalsIgnoreCase("")
				&& !selectedSegment.equalsIgnoreCase("0")) {
			deleteQuery.append(" where segment = ?");
		}
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(deleteQuery.toString());) {
			if (null != selectedSegment && !selectedSegment.equalsIgnoreCase("")
					&& !selectedSegment.equalsIgnoreCase("0")) {
				pstm.setString(1, selectedSegment);
			}
			if (pstm.executeUpdate() > 0) {
				deleteFlag = true;
			}
		} catch (SQLException e) {
			deleteFlag = false;
			log.error("Exception in PortfolioScoreCardDAOImpl :: deletePortfolioScoreCard() :: " + e.getMessage());
		}
		return deleteFlag;
	}

	private boolean deletePortfolioScoreCard(String module, String selectedSegment) {
		boolean deleteFlag = true;
		StringBuilder query = new StringBuilder();
		switch (module) {
		case "ProdSegment":
			query.append(PortfolioScoreCardConstants.DELETE_PORTFOLIO_SCORE_CARD_PROD_SEGMENT_DETAILS);
			break;
		case "ProdArea":
			query.append(PortfolioScoreCardConstants.DELETE_PORTFOLIO_SCORE_CARD_PROD_AREA_DETAILS);
			break;
		case "ProdHighlight":
			query.append(PortfolioScoreCardConstants.DELETE_PORTFOLIO_SCORE_CARD_PROD_HIGHLIGHTS_DETAILS);
			break;
		case "InstSegment":
			query.append(PortfolioScoreCardConstants.DELETE_PORTFOLIO_SCORE_CARD_INST_SEGMENT_DETAILS);
			break;
		case "InstArea":
			query.append(PortfolioScoreCardConstants.DELETE_PORTFOLIO_SCORE_CARD_INST_AREA_DETAILS);
			break;
		case "InstHighlight":
			query.append(PortfolioScoreCardConstants.DELETE_PORTFOLIO_SCORE_CARD_INST_HIGHLIGHTS_DETAILS);
			break;
		case "ProdSegment_Pub":
			query.append(PortfolioScoreCardConstants.DELETE_PORTFOLIO_SCORE_CARD_PROD_SEGMENT_DETAILS_PUB);
			break;
		case "ProdArea_Pub":
			query.append(PortfolioScoreCardConstants.DELETE_PORTFOLIO_SCORE_CARD_PROD_AREA_DETAILS_PUB);
			break;
		case "ProdHighlight_Pub":
			query.append(PortfolioScoreCardConstants.DELETE_PORTFOLIO_SCORE_CARD_PROD_HIGHLIGHTS_DETAILS_PUB);
			break;
		case "InstSegment_Pub":
			query.append(PortfolioScoreCardConstants.DELETE_PORTFOLIO_SCORE_CARD_INST_SEGMENT_DETAILS_PUB);
			break;
		case "InstArea_Pub":
			query.append(PortfolioScoreCardConstants.DELETE_PORTFOLIO_SCORE_CARD_INST_AREA_DETAILS_PUB);
			break;
		case "InstHighlight_Pub":
			query.append(PortfolioScoreCardConstants.DELETE_PORTFOLIO_SCORE_CARD_INST_HIGHLIGHTS_DETAILS_PUB);
			break;
		}
		if (null != selectedSegment && !selectedSegment.equalsIgnoreCase("")) {
			query.append(" and selected_segment = ?");
		}
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(query.toString());) {
			if (null != selectedSegment && !selectedSegment.equalsIgnoreCase("")) {
				pstm.setString(1, selectedSegment);
			}
			if (pstm.executeUpdate() > 0) {
				deleteFlag = true;
			}
		} catch (SQLException e) {
			deleteFlag = false;
			log.error("Exception in PortfolioScoreCardDAOImpl :: deletePortfolioScoreCard() :: " + e.getMessage());
		}
		return deleteFlag;
	}

	@Override
	public boolean savePortfolioScoreCardProdSegmentDetails(String selectedSegment,
			List<PortfolioScoreCardSegmentDTO> prodSegmentList, String sso) {
		boolean saveFlag = true;
		saveFlag = deletePortfolioScoreCard("ProdSegment", selectedSegment);
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(PortfolioScoreCardConstants.SAVE_PORTFOLIO_SCORE_CARD_PROD_SEGMENT_DETAILS)) {
			for (PortfolioScoreCardSegmentDTO dto : prodSegmentList) {
				if (null != dto.getSegment() && !dto.getSegment().equalsIgnoreCase("TOTAL")) {
					pstm.setString(1, dto.getSegment());
					pstm.setDouble(2, dto.getProjectCnt());
					pstm.setDouble(3, dto.getContractVal());
					pstm.setString(4, dto.getTrend());
					pstm.setDouble(5, dto.getCmAS());
					pstm.setDouble(6, dto.getCmAD());
					pstm.setString(7, sso);
					pstm.setString(8, selectedSegment);
					if (pstm.executeUpdate() > 0) {
						saveFlag = true;
					}
				}
			}
		} catch (Exception e) {
			saveFlag = false;
			log.error("Exception in PortfolioScoreCardDAOImpl :: savePortfolioScoreCardProdSegmentDetails() :: "
					+ e.getMessage());
		}
		return saveFlag;
	}

	@Override
	public boolean savePortfolioScoreCardProdAreaDetails(String selectedSegment,
			List<PortfolioScoreCardAreaDTO> prodAreaList, String sso) {
		boolean saveFlag = true;
		saveFlag = deletePortfolioScoreCard("ProdArea", selectedSegment);
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(PortfolioScoreCardConstants.SAVE_PORTFOLIO_SCORE_CARD_PROD_AREA_DETAILS)) {
			for (PortfolioScoreCardAreaDTO dto : prodAreaList) {
				pstm.setString(1, null);
				pstm.setString(2, null);
				pstm.setString(3, null);
				pstm.setString(4, null);
				pstm.setString(5, dto.getArea());
				pstm.setInt(6, dto.getGreenCnt());
				pstm.setInt(7, dto.getYellowCnt());
				pstm.setInt(8, dto.getRedCnt());
				pstm.setFloat(9, dto.getGreenPer());
				pstm.setFloat(10, dto.getYellowPer());
				pstm.setFloat(11, dto.getRedPer());
				pstm.setString(12, dto.getTrend());
				pstm.setString(13, dto.getTrendColor());
				pstm.setString(14, dto.getHighlights());
				pstm.setString(15, sso);
				pstm.setString(16, selectedSegment);
				if (pstm.executeUpdate() > 0) {
					saveFlag = true;
				}
			}
		} catch (Exception e) {
			saveFlag = false;
			log.error("Exception in PortfolioScoreCardDAOImpl :: savePortfolioScoreCardProdAreaDetails() :: "
					+ e.getMessage());
		}
		return saveFlag;
	}

	@Override
	public boolean savePortfolioScoreCardProdHightlightDetails(String selectedSegment,
			List<PortfolioScoreCardHighlightDTO> prodHighlightsList, String sso) {
		boolean saveFlag = true;
		saveFlag = deletePortfolioScoreCard("ProdHighlight", selectedSegment);
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(
						PortfolioScoreCardConstants.SAVE_PORTFOLIO_SCORE_CARD_PROD_HIGHLIGHTS_DETAILS);) {
			for (PortfolioScoreCardHighlightDTO dto : prodHighlightsList) {
				pstm.setString(1, null);
				pstm.setString(2, dto.getHighlights());
				pstm.setString(3, dto.getHighlightImage());
				pstm.setString(4, dto.getNewProjectAwarded());
				pstm.setString(5, sso);
				pstm.setString(6, selectedSegment);
				if (pstm.executeUpdate() > 0) {
					saveFlag = true;
				}
			}
		} catch (Exception e) {
			saveFlag = false;
			log.error("Exception in PortfolioScoreCardDAOImpl :: savePortfolioScoreCardProdHightlightDetails() :: "
					+ e.getMessage());
		}
		return saveFlag;
	}

	@Override
	public boolean savePortfolioScoreCardInstSegmentDetails(String selectedSegment,
			List<PortfolioScoreCardSegmentDTO> installSegmentList, String sso) {
		boolean saveFlag = true;
		saveFlag = deletePortfolioScoreCard("InstSegment", selectedSegment);
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(PortfolioScoreCardConstants.SAVE_PORTFOLIO_SCORE_CARD_INST_SEGMENT_DETAILS)) {
			for (PortfolioScoreCardSegmentDTO dto : installSegmentList) {
				if (null != dto.getSegment() && !dto.getSegment().equalsIgnoreCase("TOTAL")) {
					pstm.setString(1, dto.getSegment());
					pstm.setDouble(2, dto.getProjectCnt());
					pstm.setDouble(3, dto.getContractVal());
					pstm.setString(4, dto.getTrend());
					pstm.setString(5, sso);
					pstm.setString(6, selectedSegment);
					if (pstm.executeUpdate() > 0) {
						saveFlag = true;
					}
				}
			}
		} catch (Exception e) {
			saveFlag = false;
			log.error("Exception in PortfolioScoreCardDAOImpl :: savePortfolioScoreCardInstSegmentDetails() :: "
					+ e.getMessage());
		}
		return saveFlag;
	}

	@Override
	public boolean savePortfolioScoreCardInstAreaDetails(String selectedSegment,
			List<PortfolioScoreCardAreaDTO> instAreaList, String sso) {
		boolean saveFlag = true;
		saveFlag = deletePortfolioScoreCard("InstArea", selectedSegment);
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(PortfolioScoreCardConstants.SAVE_PORTFOLIO_SCORE_CARD_INST_AREA_DETAILS)) {
			for (PortfolioScoreCardAreaDTO dto : instAreaList) {
				pstm.setString(1, null);
				pstm.setString(2, null);
				pstm.setString(3, null);
				pstm.setString(4, null);
				pstm.setString(5, null);
				pstm.setString(6, dto.getArea());
				pstm.setInt(7, dto.getGreenCnt());
				pstm.setInt(8, dto.getYellowCnt());
				pstm.setInt(9, dto.getRedCnt());
				pstm.setFloat(10, dto.getGreenPer());
				pstm.setFloat(11, dto.getYellowPer());
				pstm.setFloat(12, dto.getRedPer());
				pstm.setString(13, dto.getTrend());
				pstm.setString(14, dto.getTrendColor());
				pstm.setString(15, dto.getHighlights());
				pstm.setString(16, sso);
				pstm.setString(17, selectedSegment);
				if (pstm.executeUpdate() > 0) {
					saveFlag = true;
				}
			}
		} catch (Exception e) {
			saveFlag = false;
			log.error("Exception in PortfolioScoreCardDAOImpl :: savePortfolioScoreCardInstAreaDetails() :: "
					+ e.getMessage());
		}
		return saveFlag;
	}

	@Override
	public boolean savePortfolioScoreCardInstHightlightDetails(String selectedSegment,
			List<PortfolioScoreCardHighlightDTO> installHighlightsList, String sso) {
		boolean saveFlag = true;
		saveFlag = deletePortfolioScoreCard("InstHighlight", selectedSegment);
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(
						PortfolioScoreCardConstants.SAVE_PORTFOLIO_SCORE_CARD_INST_HIGHLIGHTS_DETAILS);) {
			for (PortfolioScoreCardHighlightDTO dto : installHighlightsList) {
				pstm.setString(1, null);
				pstm.setString(2, dto.getHighlights());
				pstm.setString(3, dto.getHighlightImage());
				pstm.setString(4, sso);
				pstm.setString(5, selectedSegment);
				if (pstm.executeUpdate() > 0) {
					saveFlag = true;
				}
			}
		} catch (Exception e) {
			saveFlag = false;
			log.error("Exception in PortfolioScoreCardDAOImpl :: savePortfolioScoreCardInstHightlightDetails() :: "
					+ e.getMessage());
		}
		return saveFlag;
	}

	@Override
	public boolean publishPortfolioScoreCardProdSegmentDetails(String selectedSegment,
			List<PortfolioScoreCardSegmentDTO> prodSegmentList, String sso) {
		boolean updateFlag = true;
		updateFlag = deletePortfolioScoreCard("ProdSegment_Pub", selectedSegment);
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(
						PortfolioScoreCardConstants.PUBLISH_PORTFOLIO_SCORE_CARD_PROD_SEGMENT_DETAILS)) {
			for (PortfolioScoreCardSegmentDTO dto : prodSegmentList) {
				if (null != dto.getSegment() && !dto.getSegment().equalsIgnoreCase("TOTAL")) {
					pstm.setString(1, dto.getSegment());
					pstm.setDouble(2, dto.getProjectCnt());
					pstm.setDouble(3, dto.getContractVal());
					pstm.setString(4, dto.getTrend());
					pstm.setDouble(5, dto.getCmAS());
					pstm.setDouble(6, dto.getCmAD());
					pstm.setString(7, getPublishShowByString(selectedSegment));
					pstm.setString(8, sso);
					pstm.setString(9, selectedSegment);
					if (pstm.executeUpdate() > 0) {
						updateFlag = true;
					}
				}
			}
		} catch (Exception e) {
			updateFlag = false;
			log.error("Exception in PortfolioScoreCardDAOImpl :: publishPortfolioScoreCardProdSegmentDetails() :: "
					+ e.getMessage());
		}
		return updateFlag;
	}

	@Override
	public boolean publishPortfolioScoreCardProdAreaDetails(String selectedSegment,
			List<PortfolioScoreCardAreaDTO> prodAreaList, String sso) {
		boolean updateFlag = true;
		updateFlag = deletePortfolioScoreCard("ProdArea_Pub", selectedSegment);
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(PortfolioScoreCardConstants.PUBLISH_PORTFOLIO_SCORE_CARD_PROD_AREA_DETAILS)) {
			for (PortfolioScoreCardAreaDTO dto : prodAreaList) {
				pstm.setString(1, null);
				pstm.setString(2, null);
				pstm.setString(3, null);
				pstm.setString(4, null);
				pstm.setString(5, dto.getArea());
				pstm.setInt(6, dto.getGreenCnt());
				pstm.setInt(7, dto.getYellowCnt());
				pstm.setInt(8, dto.getRedCnt());
				pstm.setFloat(9, dto.getGreenPer());
				pstm.setFloat(10, dto.getYellowPer());
				pstm.setFloat(11, dto.getRedPer());
				pstm.setString(12, dto.getTrend());
				pstm.setString(13, dto.getTrendColor());
				pstm.setString(14, dto.getHighlights());
				pstm.setString(15, getPublishShowByString(selectedSegment));
				pstm.setString(16, sso);
				pstm.setString(17, selectedSegment);
				if (pstm.executeUpdate() > 0) {
					updateFlag = true;
				}
			}
		} catch (Exception e) {
			updateFlag = false;
			log.error("Exception in PortfolioScoreCardDAOImpl :: publishPortfolioScoreCardProdAreaDetails() :: "
					+ e.getMessage());
		}
		return updateFlag;
	}

	@Override
	public boolean publishPortfolioScoreCardProdHightlightDetails(String selectedSegment,
			List<PortfolioScoreCardHighlightDTO> prodHighlightsList, String sso) {
		boolean updateFlag = true;
		updateFlag = deletePortfolioScoreCard("ProdHighlight_Pub", selectedSegment);
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(
						PortfolioScoreCardConstants.PUBLISH_PORTFOLIO_SCORE_CARD_PROD_HIGHLIGHTS_DETAILS);) {
			for (PortfolioScoreCardHighlightDTO dto : prodHighlightsList) {
				pstm.setString(1, null);
				pstm.setString(2, dto.getHighlights());
				pstm.setString(3, dto.getHighlightImage());
				pstm.setString(4, dto.getNewProjectAwarded());
				pstm.setString(5, getPublishShowByString(selectedSegment));
				pstm.setString(6, sso);
				pstm.setString(7, selectedSegment);
				if (pstm.executeUpdate() > 0) {
					updateFlag = true;
				}
			}
		} catch (Exception e) {
			updateFlag = false;
			log.error("Exception in PortfolioScoreCardDAOImpl :: publishPortfolioScoreCardProdHightlightDetails() :: "
					+ e.getMessage());
		}
		return updateFlag;
	}

	@Override
	public boolean publishPortfolioScoreCardInstSegmentDetails(String selectedSegment,
			List<PortfolioScoreCardSegmentDTO> installSegmentList, String sso) {
		boolean updateFlag = true;
		updateFlag = deletePortfolioScoreCard("InstSegment_Pub", selectedSegment);
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(
						PortfolioScoreCardConstants.PUBLISH_PORTFOLIO_SCORE_CARD_INST_SEGMENT_DETAILS)) {
			for (PortfolioScoreCardSegmentDTO dto : installSegmentList) {
				if (null != dto.getSegment() && !dto.getSegment().equalsIgnoreCase("TOTAL")) {
					pstm.setString(1, dto.getSegment());
					pstm.setDouble(2, dto.getProjectCnt());
					pstm.setDouble(3, dto.getContractVal());
					pstm.setString(4, dto.getTrend());
					pstm.setString(5, getPublishShowByString(selectedSegment));
					pstm.setString(6, sso);
					pstm.setString(7, selectedSegment);
					if (pstm.executeUpdate() > 0) {
						updateFlag = true;
					}
				}
			}
		} catch (Exception e) {
			updateFlag = false;
			log.error("Exception in PortfolioScoreCardDAOImpl :: publishPortfolioScoreCardInstSegmentDetails() :: "
					+ e.getMessage());
		}
		return updateFlag;
	}

	@Override
	public boolean publishPortfolioScoreCardInstAreaDetails(String selectedSegment,
			List<PortfolioScoreCardAreaDTO> installAreaList, String sso) {
		boolean updateFlag = true;
		updateFlag = deletePortfolioScoreCard("InstArea_Pub", selectedSegment);
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(PortfolioScoreCardConstants.PUBLISH_PORTFOLIO_SCORE_CARD_INST_AREA_DETAILS)) {
			for (PortfolioScoreCardAreaDTO dto : installAreaList) {
				pstm.setString(1, null);
				pstm.setString(2, null);
				pstm.setString(3, null);
				pstm.setString(4, null);
				pstm.setString(5, null);
				pstm.setString(6, dto.getArea());
				pstm.setInt(7, dto.getGreenCnt());
				pstm.setInt(8, dto.getYellowCnt());
				pstm.setInt(9, dto.getRedCnt());
				pstm.setFloat(10, dto.getGreenPer());
				pstm.setFloat(11, dto.getYellowPer());
				pstm.setFloat(12, dto.getRedPer());
				pstm.setString(13, dto.getTrend());
				pstm.setString(14, dto.getTrendColor());
				pstm.setString(15, dto.getHighlights());
				pstm.setString(16, getPublishShowByString(selectedSegment));
				pstm.setString(17, sso);
				pstm.setString(18, selectedSegment);
				if (pstm.executeUpdate() > 0) {
					updateFlag = true;
				}
			}
		} catch (Exception e) {
			updateFlag = false;
			log.error("Exception in PortfolioScoreCardDAOImpl :: publishPortfolioScoreCardInstAreaDetails() :: "
					+ e.getMessage());
		}
		return updateFlag;
	}

	@Override
	public boolean publishPortfolioScoreCardInstHighlightDetails(String selectedSegment,
			List<PortfolioScoreCardHighlightDTO> installHighlightsList, String sso) {
		boolean updateFlag = true;
		updateFlag = deletePortfolioScoreCard("InstHighlight_Pub", selectedSegment);
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(
						PortfolioScoreCardConstants.PUBLISH_PORTFOLIO_SCORE_CARD_INST_HIGHLIGHTS_DETAILS);) {
			for (PortfolioScoreCardHighlightDTO dto : installHighlightsList) {
				pstm.setString(1, null);
				pstm.setString(2, dto.getHighlights());
				pstm.setString(3, dto.getHighlightImage());
				pstm.setString(4, getPublishShowByString(selectedSegment));
				pstm.setString(5, sso);
				pstm.setString(6, selectedSegment);
				if (pstm.executeUpdate() > 0) {
					updateFlag = true;
				}
			}
		} catch (Exception e) {
			updateFlag = false;
			log.error("Exception in PortfolioScoreCardDAOImpl :: publishPortfolioScoreCardInstHighlightDetails() :: "
					+ e.getMessage());
		}
		return updateFlag;
	}

	public String getPublishShowByString(String selectedSegment) {
		String showBy = "";
		try {
			LocalDateTime ldt = LocalDateTime.now();
			String date = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH).format(ldt);
			if (null != selectedSegment && !selectedSegment.equalsIgnoreCase("")
					&& !selectedSegment.equalsIgnoreCase("0")) {
				showBy = selectedSegment + "-" + "Published@" + date;
			} else {
				showBy = "Published@" + date;
			}
		} catch (Exception e) {
			log.error("Exception in PortfolioScoreCardDAOImpl :: getPublishShowByString() :: " + e.getMessage());
		}
		return showBy;
	}

}

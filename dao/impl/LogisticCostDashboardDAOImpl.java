package com.bh.realtrack.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bh.realtrack.dao.LogisticCostDashboardDAO;
import com.bh.realtrack.dto.DropDownDTO;
import com.bh.realtrack.dto.LCDashboardBPSDTO;
import com.bh.realtrack.dto.LCDashboardBPSiteAnalysisDTO;
import com.bh.realtrack.dto.LCDashboardBPSiteAnalysisDetailsDTO;
import com.bh.realtrack.dto.LCDashboardBPSiteAnalysisFilterDTO;
import com.bh.realtrack.dto.LCDashboardDTO;
import com.bh.realtrack.dto.LCDashboardDetailedWorkloadDTO;
import com.bh.realtrack.dto.LCDashboardTransportationAnalysisDTO;
import com.bh.realtrack.dto.LCDashboardTransportationAnalysisDetailsDTO;
import com.bh.realtrack.dto.LCDashboardTransportationAnalysisFilterDTO;
import com.bh.realtrack.dto.LCDashboardWorkloadDTO;
import com.bh.realtrack.dto.LCSummaryDTO;
import com.bh.realtrack.dto.LCVolumeAnalysisPiechart;
import com.bh.realtrack.dto.LegendColorDTO;
import com.bh.realtrack.util.LogisticCostDashboardConstants;

@Repository
public class LogisticCostDashboardDAOImpl implements LogisticCostDashboardDAO {

	private static Logger log = LoggerFactory.getLogger(LogisticCostDashboardDAOImpl.class.getName());

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public List<DropDownDTO> getLCPmLeaderDropDown(LCDashboardDTO paramList) {
		List<DropDownDTO> pmLeaderList = new ArrayList<DropDownDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(LogisticCostDashboardConstants.GET_PM_LEADER_LIST)) {
			pstm.setInt(1, paramList.getCustomerId());
			pstm.setInt(2, paramList.getCustomerId());
			pstm.setInt(3, paramList.getCompanyId());
			pstm.setInt(4, paramList.getCompanyId());
			pstm.setString(5, paramList.getBusinessUnit());
			pstm.setString(6, paramList.getBusinessUnit());
			pstm.setString(7, paramList.getSegment());
			pstm.setString(8, paramList.getSegment());
			pstm.setString(9, paramList.getRegion());
			pstm.setString(10, paramList.getRegion());
			pstm.setString(11, paramList.getFavProjects());
			pstm.setString(12, paramList.getFavProjects());
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				if (null != rs.getString(1)) {
					DropDownDTO dto = new DropDownDTO();
					dto.setKey(null != rs.getString(1) ? rs.getString(1) : "");
					dto.setVal(null != rs.getString(1) ? rs.getString(1) : "");
					pmLeaderList.add(dto);
				}
			}
		} catch (SQLException e) {
			log.error("Exception while getting PM leader :: " + e.getMessage());
		}
		return pmLeaderList;
	}

	@Override
	public List<DropDownDTO> getLCSpmDropDown(LCDashboardDTO paramList) {
		List<DropDownDTO> spmList = new ArrayList<DropDownDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(LogisticCostDashboardConstants.GET_SPM_LIST)) {
			pstm.setInt(1, paramList.getCustomerId());
			pstm.setInt(2, paramList.getCustomerId());
			pstm.setInt(3, paramList.getCompanyId());
			pstm.setInt(4, paramList.getCompanyId());
			pstm.setString(5, paramList.getBusinessUnit());
			pstm.setString(6, paramList.getBusinessUnit());
			pstm.setString(7, paramList.getSegment());
			pstm.setString(8, paramList.getSegment());
			pstm.setString(9, paramList.getRegion());
			pstm.setString(10, paramList.getRegion());
			pstm.setString(11, paramList.getFavProjects());
			pstm.setString(12, paramList.getFavProjects());
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				if (null != rs.getString(1)) {
					DropDownDTO dto = new DropDownDTO();
					dto.setKey(null != rs.getString(1) ? rs.getString(1) : "");
					dto.setVal(null != rs.getString(1) ? rs.getString(1) : "");
					spmList.add(dto);
				}
			}
		} catch (SQLException e) {
			log.error("Exception while getting SPM leader :: " + e.getMessage());
		}
		return spmList;
	}

	@Override
	public List<DropDownDTO> getLCFinanceSegmentDropDown(LCDashboardDTO paramList, String projectList) {
		List<DropDownDTO> financialSegmentList = new ArrayList<DropDownDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(LogisticCostDashboardConstants.GET_FINANCIAL_SEGMENT_LIST)) {
			pstm.setString(1, projectList);
			pstm.setString(2, projectList);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				if (null != rs.getString(1)) {
					DropDownDTO dto = new DropDownDTO();
					dto.setKey(null != rs.getString(1) ? rs.getString(1) : "");
					dto.setVal(null != rs.getString(1) ? rs.getString(1) : "");
					financialSegmentList.add(dto);
				}
			}
		} catch (SQLException e) {
			log.error("Exception while getting Financial Segment :: " + e.getMessage());
		}
		return financialSegmentList;
	}

	@Override
	public List<DropDownDTO> getLCShippingManagerDropDown(LCDashboardDTO paramList, String projectList) {
		List<DropDownDTO> shippingManagerList = new ArrayList<DropDownDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(LogisticCostDashboardConstants.GET_SHIPPING_MANAGER_LIST)) {
			pstm.setString(1, projectList);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				if (null != rs.getString(1)) {
					DropDownDTO dto = new DropDownDTO();
					dto.setKey(null != rs.getString(1) ? rs.getString(1) : "");
					dto.setVal(null != rs.getString(1) ? rs.getString(1) : "");
					shippingManagerList.add(dto);
				}
			}
		} catch (SQLException e) {
			log.error("Exception while getting Shipping Manager :: " + e.getMessage());
		}
		return shippingManagerList;
	}

	@Override
	public List<DropDownDTO> getLCRacDateHorizonDropDowns() {
		List<DropDownDTO> racDateHorizonList = new ArrayList<DropDownDTO>();
		List<String> list = new ArrayList<String>();
		LocalDate lDate = LocalDate.now();
		String quarter1 = "", quarter2 = "", quarter3 = "", quarter4 = "", currYear = "", prevYear = "",
				quarterStr = "", yearStr = "";

		quarterStr = String.valueOf(lDate.minusMonths(9).get(IsoFields.QUARTER_OF_YEAR));
		yearStr = String.valueOf(lDate.minusMonths(9).getYear()).substring(2, 4);
		quarter1 = yearStr + "-" + quarterStr + "Q";
		list.add(quarter1);

		quarterStr = String.valueOf(lDate.minusMonths(6).get(IsoFields.QUARTER_OF_YEAR));
		yearStr = String.valueOf(lDate.minusMonths(6).getYear()).substring(2, 4);
		quarter2 = yearStr + "-" + quarterStr + "Q";
		list.add(quarter2);

		quarterStr = String.valueOf(lDate.minusMonths(3).get(IsoFields.QUARTER_OF_YEAR));
		yearStr = String.valueOf(lDate.minusMonths(3).getYear()).substring(2, 4);
		quarter3 = yearStr + "-" + quarterStr + "Q";
		list.add(quarter3);

		quarterStr = String.valueOf(lDate.get(IsoFields.QUARTER_OF_YEAR));
		yearStr = String.valueOf(lDate.getYear()).substring(2, 4);
		quarter4 = yearStr + "-" + quarterStr + "Q";
		list.add(quarter4);

		prevYear = String.valueOf(lDate.minusYears(1).getYear());
		list.add(prevYear);

		currYear = String.valueOf(lDate.getYear());
		list.add(currYear);

		list.add("Last 3 Years");

		for (String obj : list) {
			DropDownDTO dto = new DropDownDTO();
			dto.setKey(obj);
			dto.setVal(obj);
			racDateHorizonList.add(dto);
		}
		return racDateHorizonList;
	}

	@Override
	public List<DropDownDTO> getLCExWorksHorizonDropDowns() {
		List<DropDownDTO> racDateHorizonList = new ArrayList<DropDownDTO>();
		List<String> list = new ArrayList<String>();
		LocalDate lDate = LocalDate.now();
		String quarter1 = "", quarter2 = "", quarter3 = "", quarter4 = "", currYear = "", prevYear = "",
				quarterStr = "", yearStr = "";

		quarterStr = String.valueOf(lDate.minusMonths(9).get(IsoFields.QUARTER_OF_YEAR));
		yearStr = String.valueOf(lDate.minusMonths(9).getYear()).substring(2, 4);
		quarter1 = yearStr + "-" + quarterStr + "Q";
		list.add(quarter1);

		quarterStr = String.valueOf(lDate.minusMonths(6).get(IsoFields.QUARTER_OF_YEAR));
		yearStr = String.valueOf(lDate.minusMonths(6).getYear()).substring(2, 4);
		quarter2 = yearStr + "-" + quarterStr + "Q";
		list.add(quarter2);

		quarterStr = String.valueOf(lDate.minusMonths(3).get(IsoFields.QUARTER_OF_YEAR));
		yearStr = String.valueOf(lDate.minusMonths(3).getYear()).substring(2, 4);
		quarter3 = yearStr + "-" + quarterStr + "Q";
		list.add(quarter3);

		quarterStr = String.valueOf(lDate.get(IsoFields.QUARTER_OF_YEAR));
		yearStr = String.valueOf(lDate.getYear()).substring(2, 4);
		quarter4 = yearStr + "-" + quarterStr + "Q";
		list.add(quarter4);

		prevYear = String.valueOf(lDate.minusYears(1).getYear());
		list.add(prevYear);

		currYear = String.valueOf(lDate.getYear());
		list.add(currYear);

		list.add("Last 3 Years");

		for (String obj : list) {
			DropDownDTO dto = new DropDownDTO();
			dto.setKey(obj);
			dto.setVal(obj);
			racDateHorizonList.add(dto);
		}
		return racDateHorizonList;
	}

	@Override
	public String getDefaultProjectList(LCDashboardDTO paramList) {
		String defaultProjects = "0";
		ResultSet rs = null;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(LogisticCostDashboardConstants.GET_LC_PROJECT_LIST);) {
			pstm.setInt(1, paramList.getCustomerId());
			pstm.setInt(2, paramList.getCustomerId());
			pstm.setInt(3, paramList.getCompanyId());
			pstm.setInt(4, paramList.getCompanyId());
			pstm.setString(5, paramList.getBusinessUnit());
			pstm.setString(6, paramList.getBusinessUnit());
			pstm.setString(7, paramList.getSegment());
			pstm.setString(8, paramList.getSegment());
			pstm.setString(9, paramList.getRegion());
			pstm.setString(10, paramList.getRegion());
			pstm.setString(11, paramList.getFavProjects());
			pstm.setString(12, paramList.getFavProjects());
			rs = pstm.executeQuery();
			while (rs.next()) {
				String project = rs.getString(1);
				if (project != null && !project.equalsIgnoreCase("") && !defaultProjects.equalsIgnoreCase("")) {
					defaultProjects = defaultProjects + ";" + project;
				} else {
					defaultProjects = project;
				}
			}
			log.info("defaultProjects :: " + defaultProjects);
			return defaultProjects;
		} catch (Exception exception) {
			log.error("Exception while getting default project list :: " + exception.getMessage());
		}
		return defaultProjects;
	}

	@Override
	public String getBoxPackingProjectList(LCDashboardBPSDTO paramList, String startDate, String endDate) {
		String defaultProjects = "0";
		ResultSet rs = null;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(LogisticCostDashboardConstants.GET_LC_BOX_AND_PACKING_PROJECT_LIST);) {
			pstm.setInt(1, paramList.getCustomerId());
			pstm.setInt(2, paramList.getCustomerId());
			pstm.setInt(3, paramList.getCompanyId());
			pstm.setInt(4, paramList.getCompanyId());
			pstm.setString(5, paramList.getBusinessUnit());
			pstm.setString(6, paramList.getBusinessUnit());
			pstm.setString(7, paramList.getSegment());
			pstm.setString(8, paramList.getSegment());
			pstm.setString(9, paramList.getRegion());
			pstm.setString(10, paramList.getRegion());
			pstm.setString(11, paramList.getFavProjects());
			pstm.setString(12, paramList.getFavProjects());
			pstm.setString(13, paramList.getPmLeader());
			pstm.setString(14, paramList.getPmLeader());
			pstm.setString(15, paramList.getSpm());
			pstm.setString(16, paramList.getSpm());
			pstm.setString(17, paramList.getFinancialSegment());
			pstm.setString(18, paramList.getFinancialSegment());
			pstm.setString(19, paramList.getShippingManager());
			pstm.setString(20, paramList.getShippingManager());
			pstm.setString(21, "0");
			pstm.setString(22, startDate);
			pstm.setString(23, endDate);
			rs = pstm.executeQuery();
			while (rs.next()) {
				String project = rs.getString(1);
				if (project != null && !project.equalsIgnoreCase("") && !defaultProjects.equalsIgnoreCase("")) {
					defaultProjects = defaultProjects + ";" + project;
				} else {
					defaultProjects = project;
				}
			}
			log.info("B&P defaultProjects :: " + defaultProjects);
			return defaultProjects;
		} catch (Exception exception) {
			log.error("Exception while getting B&P project list :: " + exception.getMessage());
		}
		return defaultProjects;
	}

	@Override
	public LCSummaryDTO getLCDashboardBoxPackingSummary(LCDashboardBPSDTO paramList, String projectList) {
		LCSummaryDTO dto = new LCSummaryDTO();
		String actualCost = "", budgetAsSold = "", lastBudget = "", volumePacked = "";
		actualCost = getBoxPackingActualCost(projectList);
		budgetAsSold = getBoxPackingBudgetAsSold(projectList);
		lastBudget = getBoxPackingLastBudget(projectList);
		volumePacked = getBoxPackingVolumePacked(projectList);
		dto.setBudgetAsSold(budgetAsSold);
		dto.setActualCost(actualCost);
		dto.setLastBudget(lastBudget);
		dto.setVolumePacked(volumePacked);
		return dto;
	}

	@Override
	public Map<String, Object> getLCDashboardVolumePackedPieChart(LCDashboardBPSDTO paramList, String projectList) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		LCVolumeAnalysisPiechart thermodto = new LCVolumeAnalysisPiechart();
		LCVolumeAnalysisPiechart wooddto = new LCVolumeAnalysisPiechart();
		resultMap.put("thermo", thermodto);
		resultMap.put("wood", wooddto);
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(LogisticCostDashboardConstants.GET_BOX_PACKING_VOLUME_PACKED_PIE_CHART);) {
			pstm.setString(1, projectList);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				thermodto.setPer(null != rs.getString("volume_thermo_per") ? rs.getString("volume_thermo_per") : "");
				thermodto.setVol(null != rs.getString("volume_thermo") ? rs.getString("volume_thermo") : "");
				wooddto.setPer(null != rs.getString("volume_wood_per") ? rs.getString("volume_wood_per") : "");
				wooddto.setVol(null != rs.getString("volume_wood") ? rs.getString("volume_wood") : "");
				resultMap.put("thermo", thermodto);
				resultMap.put("wood", wooddto);
			}
		} catch (SQLException e) {
			log.error("Exception while getting B&P Volume Packed Pie Chart :: " + e.getMessage());
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> getLCDashboardVolumePackedBySitePieChart(LCDashboardBPSDTO paramList,
			String projectList) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap = getLCDashboardVolumePackedBySiteDefaultValues();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(LogisticCostDashboardConstants.GET_BOX_VOLUME_PACKED_BY_SITE_PIE_CHART);) {
			pstm.setString(1, projectList);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				LCVolumeAnalysisPiechart dto = new LCVolumeAnalysisPiechart();

				String volumeSiteBy = "";
				volumeSiteBy = rs.getString("site_out");
				dto.setVol(null != rs.getString("volume_out") ? rs.getString("volume_out") : "");
				dto.setPer(null != rs.getString("vol_per_out") ? rs.getString("vol_per_out") : "");
				if (null != volumeSiteBy && !"".equalsIgnoreCase(volumeSiteBy)
						&& responseMap.containsKey(volumeSiteBy)) {
					responseMap.put(volumeSiteBy, dto);
				}

			}
		} catch (SQLException e) {
			log.error("Exception while getting B&P Volume Packed Pie Chart :: " + e.getMessage());
		}
		return responseMap;
	}

	public Map<String, Object> getLCDashboardVolumePackedBySiteDefaultValues() {
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		String[] list = { "Firenze", "Massa", "Avenza", "Vibo", "Bari", "Livorno", "Fot", "Material Shipped direct",
				"Packing By Supplier(ext)", "Packing By Supplier(int)", "Not Updated" };
		for (String obj : list) {
			LCVolumeAnalysisPiechart dto = new LCVolumeAnalysisPiechart();
			dto.setVol("");
			dto.setPer("");
			resultMap.put(obj, dto);
		}
		return resultMap;
	}

	public String getBoxPackingBudgetAsSold(String projectList) {
		String bdAsSold = "";
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(LogisticCostDashboardConstants.GET_BOX_PACKING_BUDGET_AS_SOLD)) {
			pstm.setString(1, projectList);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				bdAsSold = rs.getString(1);
			}
			return bdAsSold;
		} catch (SQLException e) {
			log.error("Exception while getting B&P Budget As Sold :: " + e.getMessage());
		}
		return bdAsSold;
	}

	public String getBoxPackingActualCost(String projectList) {
		String actualCost = "";
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(LogisticCostDashboardConstants.GET_BOX_PACKING_ACTUAL_COST)) {
			pstm.setString(1, projectList);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				actualCost = rs.getString(1);
			}
			return actualCost;
		} catch (SQLException e) {
			log.error("Exception while getting B&P Actual Cost :: " + e.getMessage());
		}
		return actualCost;
	}

	public String getBoxPackingLastBudget(String projectList) {
		String lastbgd = "";
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(LogisticCostDashboardConstants.GET_BOX_PACKING_LAST_BUDGET)) {
			pstm.setString(1, projectList);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				lastbgd = rs.getString(1);
			}
			return lastbgd;
		} catch (SQLException e) {
			log.error("Exception while getting B&P Last Budget :: " + e.getMessage());
		}
		return lastbgd;

	}

	public String getBoxPackingVolumePacked(String projectList) {
		String volPacked = "";
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(LogisticCostDashboardConstants.GET_BOX_PACKING_VOLUME_PACKED)) {
			pstm.setString(1, projectList);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				volPacked = rs.getString(1);
			}
			return volPacked;
		} catch (SQLException e) {
			log.error("Exception while getting B&P Volume Packed :: " + e.getMessage());
		}
		return volPacked;
	}

	@Override
	public String getTransportationProjectList(LCDashboardBPSDTO paramList, String startDate, String endDate) {
		String defaultProjects = "0";
		ResultSet rs = null;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(LogisticCostDashboardConstants.GET_LC_TRANSPORTATION_PROJECT_LIST);) {
			pstm.setInt(1, paramList.getCustomerId());
			pstm.setInt(2, paramList.getCustomerId());
			pstm.setInt(3, paramList.getCompanyId());
			pstm.setInt(4, paramList.getCompanyId());
			pstm.setString(5, paramList.getBusinessUnit());
			pstm.setString(6, paramList.getBusinessUnit());
			pstm.setString(7, paramList.getSegment());
			pstm.setString(8, paramList.getSegment());
			pstm.setString(9, paramList.getRegion());
			pstm.setString(10, paramList.getRegion());
			pstm.setString(11, paramList.getFavProjects());
			pstm.setString(12, paramList.getFavProjects());
			pstm.setString(13, paramList.getPmLeader());
			pstm.setString(14, paramList.getPmLeader());
			pstm.setString(15, paramList.getSpm());
			pstm.setString(16, paramList.getSpm());
			pstm.setString(17, paramList.getFinancialSegment());
			pstm.setString(18, paramList.getFinancialSegment());
			pstm.setString(19, paramList.getShippingManager());
			pstm.setString(20, paramList.getShippingManager());
			pstm.setString(21, "0");
			pstm.setString(22, startDate);
			pstm.setString(23, endDate);
			rs = pstm.executeQuery();
			while (rs.next()) {
				String project = rs.getString(1);
				if (project != null && !project.equalsIgnoreCase("") && !defaultProjects.equalsIgnoreCase("")) {
					defaultProjects = defaultProjects + ";" + project;
				} else {
					defaultProjects = project;
				}
			}
			log.info("Transportation defaultProjects :: " + defaultProjects);
			return defaultProjects;
		} catch (Exception exception) {
			log.error("Exception while getting transportation project list :: " + exception.getMessage());
		}
		return defaultProjects;
	}

	@Override
	public LCSummaryDTO getLCDashboardTransportationSummary(LCDashboardBPSDTO paramList, String projectList) {
		LCSummaryDTO dto = new LCSummaryDTO();
		String actualCost = "", budgetAsSold = "", lastBudget = "", volumeShipped = "";
		actualCost = getTransportationActualCost(projectList);
		budgetAsSold = getTransportationBudgetAsSold(projectList);
		lastBudget = getTransportationLastBudget(projectList);
		volumeShipped = getTransportationVolumeShipped(projectList);
		dto.setBudgetAsSold(budgetAsSold);
		dto.setActualCost(actualCost);
		dto.setLastBudget(lastBudget);
		dto.setVolumePacked(volumeShipped);
		return dto;
	}

	@Override
	public Map<String, Object> getLCDashboardVolumeShippedPieChart(LCDashboardBPSDTO paramList, String projectList) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		LCVolumeAnalysisPiechart packedDTO = new LCVolumeAnalysisPiechart();
		LCVolumeAnalysisPiechart unPackedDTO = new LCVolumeAnalysisPiechart();
		LCVolumeAnalysisPiechart msdPackedDTO = new LCVolumeAnalysisPiechart();
		LCVolumeAnalysisPiechart msdUnpackedDTO = new LCVolumeAnalysisPiechart();
		resultMap.put("Packed", packedDTO);
		resultMap.put("Unpacked", unPackedDTO);
		resultMap.put("MSD-Packed", msdPackedDTO);
		resultMap.put("MSD-Unpacked", msdUnpackedDTO);
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(
						LogisticCostDashboardConstants.GET_TRANSPORTATION_VOLUME_SHIPPED_PIE_CHART);) {
			pstm.setString(1, projectList);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				packedDTO.setPer(
						null != rs.getString("volume_packed_per_out") ? rs.getString("volume_packed_per_out") : "");
				packedDTO.setVol(null != rs.getString("volume_packed_out") ? rs.getString("volume_packed_out") : "");
				unPackedDTO.setPer(
						null != rs.getString("volume_unpacked_per_out") ? rs.getString("volume_unpacked_per_out") : "");
				unPackedDTO
						.setVol(null != rs.getString("volume_unpacked_out") ? rs.getString("volume_unpacked_out") : "");
				msdPackedDTO.setVol(
						null != rs.getString("volume_packed_msd_out") ? rs.getString("volume_packed_msd_out") : "");
				msdPackedDTO.setPer(
						null != rs.getString("volume_packed_per_msd_out") ? rs.getString("volume_packed_per_msd_out")
								: "");
				msdUnpackedDTO.setVol(
						null != rs.getString("volume_unpacked_msd_out") ? rs.getString("volume_unpacked_msd_out") : "");
				msdUnpackedDTO.setPer(null != rs.getString("volume_unpacked_per_msd_out")
						? rs.getString("volume_unpacked_per_msd_out")
						: "");

			}
			resultMap.put("Packed", packedDTO);
			resultMap.put("Unpacked", unPackedDTO);
			resultMap.put("MSD-Packed", msdPackedDTO);
			resultMap.put("MSD-Unpacked", msdUnpackedDTO);
		} catch (SQLException e) {
			log.error("Exception while getting Volume Shipped Pie Chart :: " + e.getMessage());
		}
		return resultMap;
	}

	public String getTransportationBudgetAsSold(String projectList) {
		String bdAsSold = "";
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(LogisticCostDashboardConstants.GET_TRANSPORTATION_BUDGET_AS_SOLD)) {
			pstm.setString(1, projectList);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				bdAsSold = null != rs.getString(1) ? rs.getString(1) : "";
			}
			return bdAsSold;
		} catch (SQLException e) {
			log.error("Exception while getting Transportation Budget As Sold :: " + e.getMessage());
		}
		return bdAsSold;
	}

	public String getTransportationActualCost(String projectList) {
		String actualCost = "";
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(LogisticCostDashboardConstants.GET_TRANSPORTATION_ACTUAL_COST)) {
			pstm.setString(1, projectList);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				actualCost = null != rs.getString(1) ? rs.getString(1) : "";
			}
			return actualCost;
		} catch (SQLException e) {
			log.error("Exception while getting Transportation Actual Cost :: " + e.getMessage());
		}
		return actualCost;
	}

	public String getTransportationLastBudget(String projectList) {
		String lastbgd = "";
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(LogisticCostDashboardConstants.GET_TRANSPORTATION_LAST_BUDGET)) {
			pstm.setString(1, projectList);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				lastbgd = null != rs.getString(1) ? rs.getString(1) : "";
			}
			return lastbgd;
		} catch (SQLException e) {
			log.error("Exception while getting Transportation Last Budget :: " + e.getMessage());
		}
		return lastbgd;

	}

	public String getTransportationVolumeShipped(String projectList) {
		String volPacked = "";
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(LogisticCostDashboardConstants.GET_TRANSPORTATION_VOLUME_SHIPPED)) {
			pstm.setString(1, projectList);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				volPacked = null != rs.getString(1) ? rs.getString(1) : "";
			}
			return volPacked;
		} catch (SQLException e) {
			log.error("Exception while getting Transportation Volume Packed :: " + e.getMessage());
		}
		return volPacked;
	}

	@Override
	public Map<String, Object> getLCDashboardShipmentStatusPieChart(String projectList) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		LCVolumeAnalysisPiechart shippedDTO = new LCVolumeAnalysisPiechart();
		LCVolumeAnalysisPiechart awaitingShippingDTO = new LCVolumeAnalysisPiechart();
		resultMap.put("shipped", shippedDTO);
		resultMap.put("awaitingShipping", awaitingShippingDTO);
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(
						LogisticCostDashboardConstants.GET_TRANSPORTATION_SHIPMENT_STATUS_PIE_CHART)) {
			pstm.setString(1, projectList);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				awaitingShippingDTO.setPer(
						null != rs.getString("awaiting_shipping_vol_per") ? rs.getString("awaiting_shipping_vol_per")
								: "");
				awaitingShippingDTO.setVol(
						null != rs.getString("awaiting_shipping_vol") ? rs.getString("awaiting_shipping_vol") : "");
				shippedDTO.setPer(null != rs.getString("shipped_vol_per") ? rs.getString("shipped_vol_per") : "");
				shippedDTO.setVol(null != rs.getString("shipped_vol") ? rs.getString("shipped_vol") : "");
			}
			resultMap.put("shipped", shippedDTO);
			resultMap.put("awaitingShipping", awaitingShippingDTO);
		} catch (SQLException e) {
			log.error("Exception while getting LC Shipment Status Pie Chart :: " + e.getMessage());
		}
		return resultMap;
	}

	@Override
	public String getWorkloadProjectList(LCDashboardBPSDTO paramList, String startDate, String endDate) {
		String defaultProjects = "0";
		ResultSet rs = null;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(LogisticCostDashboardConstants.GET_LC_WORKLOAD_PROJECT_LIST);) {
			pstm.setInt(1, paramList.getCustomerId());
			pstm.setInt(2, paramList.getCustomerId());
			pstm.setInt(3, paramList.getCompanyId());
			pstm.setInt(4, paramList.getCompanyId());
			pstm.setString(5, paramList.getBusinessUnit());
			pstm.setString(6, paramList.getBusinessUnit());
			pstm.setString(7, paramList.getSegment());
			pstm.setString(8, paramList.getSegment());
			pstm.setString(9, paramList.getRegion());
			pstm.setString(10, paramList.getRegion());
			pstm.setString(11, paramList.getFavProjects());
			pstm.setString(12, paramList.getFavProjects());
			pstm.setString(13, paramList.getPmLeader());
			pstm.setString(14, paramList.getPmLeader());
			pstm.setString(15, paramList.getSpm());
			pstm.setString(16, paramList.getSpm());
			pstm.setString(17, paramList.getFinancialSegment());
			pstm.setString(18, paramList.getFinancialSegment());
			pstm.setString(19, paramList.getShippingManager());
			pstm.setString(20, paramList.getShippingManager());
			rs = pstm.executeQuery();
			while (rs.next()) {
				String project = rs.getString(1);
				if (project != null && !project.equalsIgnoreCase("") && !defaultProjects.equalsIgnoreCase("")) {
					defaultProjects = defaultProjects + ";" + project;
				} else {
					defaultProjects = project;
				}
			}
			log.info("workload defaultProjects :: " + defaultProjects);
			return defaultProjects;
		} catch (Exception exception) {

			log.error("Exception while getting logistics workload project list :: " + exception.getMessage());
		}
		return defaultProjects;
	}

	@Override
	public List<LCDashboardWorkloadDTO> getLCDashboardWorkloadDetails(LCDashboardBPSDTO paramList, String projectList) {
		List<LCDashboardWorkloadDTO> list = new ArrayList<LCDashboardWorkloadDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(LogisticCostDashboardConstants.GET_WORKLOAD_DETAILS)) {
			pstm.setString(1, projectList);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				LCDashboardWorkloadDTO dto = new LCDashboardWorkloadDTO();
				dto.setSegment(null != rs.getString("segment_out") ? rs.getString("segment_out") : "");
				dto.setRegion(null != rs.getString("region_out") ? rs.getString("region_out") : "");
				dto.setInstallationCountry(
						null != rs.getString("installation_country_out") ? rs.getString("installation_country_out")
								: "");
				dto.setProjectId(null != rs.getString("project_id_out") ? rs.getString("project_id_out") : "");
				dto.setProjectName(
						null != rs.getString("master_project_name_out") ? rs.getString("master_project_name_out") : "");
				dto.setCustomerName(
						null != rs.getString("contract_customer_out") ? rs.getString("contract_customer_out") : "");
				dto.setCurrentStatus(null != rs.getString("master_project_phase_map_out")
						? rs.getString("master_project_phase_map_out")
						: "");
				dto.setPm(null != rs.getString("pm_name_out") ? rs.getString("pm_name_out") : "");
				dto.setSm(null != rs.getString("shipping_manager_out") ? rs.getString("shipping_manager_out") : "");
				dto.setFreightTermsTitleTransfer(
						null != rs.getString("freight_term_out") ? rs.getString("freight_term_out") : "");
				dto.setIncotermsCategory(
						null != rs.getString("incoterms_category_out") ? rs.getString("incoterms_category_out") : "");
				dto.setRacDate(null != rs.getString("rac_out") ? rs.getString("rac_out") : "");
				dto.setBpBudgetAsSold(null != rs.getString("bp_bdg_out") ? rs.getString("bp_bdg_out") : "");
				dto.setBpLastBudget(null != rs.getString("bp_last_bdg_out") ? rs.getString("bp_last_bdg_out") : "");
				dto.setBpActualCost(null != rs.getString("bp_act_out") ? rs.getString("bp_act_out") : "");
				dto.setBpRate(null != rs.getString("product_rate_out") ? rs.getString("product_rate_out") : "");
				dto.setBpVolumeOTR(null != rs.getString("volume_otr_out") ? rs.getString("volume_otr_out") : "");
				dto.setVolumePacked(null != rs.getString("volume_packed_out") ? rs.getString("volume_packed_out") : "");
				dto.setVolumePackedLastMonth(null != rs.getString("volume_packed_last_month_out")
						? rs.getString("volume_packed_last_month_out")
						: "");
				dto.setWood(null != rs.getString("wood_out") ? rs.getString("wood_out") : "");
				dto.setThermo(null != rs.getString("thermo_out") ? rs.getString("thermo_out") : "");
				dto.setUnpacked(null != rs.getString("bp_not_needed_out") ? rs.getString("bp_not_needed_out") : "");
				dto.setFirstExpirationDate(
						null != rs.getString("expiration_date_out") ? rs.getString("expiration_date_out") : "");
				dto.setReadyForShipment(
						null != rs.getString("ready_for_shipment_out") ? rs.getString("ready_for_shipment_out") : "");
				dto.setFirstExWorksDate(
						null != rs.getString("exworks_date_out") ? rs.getString("exworks_date_out") : "");
				dto.setTransportationBudgetAsSold(
						null != rs.getString("transportation_bdg_out") ? rs.getString("transportation_bdg_out") : "");
				dto.setTransportationLastBudget(null != rs.getString("transportation_last_bdg_out")
						? rs.getString("transportation_last_bdg_out")
						: "");
				dto.setTransportationActualCost(
						null != rs.getString("transportation_act_out") ? rs.getString("transportation_act_out") : "");
				dto.setTransportationRate(
						null != rs.getString("transportation_rate_out") ? rs.getString("transportation_rate_out") : "");
				dto.setVolumeShipped(
						null != rs.getString("volume_shipped_out") ? rs.getString("volume_shipped_out") : "");
				dto.setVolumeMSD(null != rs.getString("volume_msd_out") ? rs.getString("volume_msd_out") : "");
				dto.setShippedWood(null != rs.getString("shipped_wood_out") ? rs.getString("shipped_wood_out") : "");
				dto.setShippedThermo(
						null != rs.getString("shipped_thermo_out") ? rs.getString("shipped_thermo_out") : "");
				dto.setShippedUnpacked(
						null != rs.getString("shipped_bp_not_needed_out") ? rs.getString("shipped_bp_not_needed_out")
								: "");
				dto.setOpptyId(null != rs.getString("opportunity_id_out") ? rs.getString("opportunity_id_out") : "");
				dto.setSow(null != rs.getString("scope_of_work_out") ? rs.getString("scope_of_work_out") : "");
				dto.setPlpCodeInternalDoc(
						null != rs.getString("internal_doc_out") ? rs.getString("internal_doc_out") : "");
				dto.setPlpCodeVdr(null != rs.getString("vdr_out") ? rs.getString("vdr_out") : "");
				dto.setBusiness(null != rs.getString("business_unit_out") ? rs.getString("business_unit_out") : "");
				list.add(dto);
			}
			return list;
		} catch (SQLException e) {
			log.error("Exception while getting LC Workload Details :: " + e.getMessage());
		}
		return list;
	}

	@Override
	public List<LCDashboardDetailedWorkloadDTO> getLCDashboardDetailedWorkloadDetails(LCDashboardBPSDTO paramList,
			String projectList) {
		List<LCDashboardDetailedWorkloadDTO> list = new ArrayList<LCDashboardDetailedWorkloadDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(LogisticCostDashboardConstants.GET_DETAILED_WORKLOAD_DETAILS)) {
			pstm.setString(1, projectList);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				LCDashboardDetailedWorkloadDTO dto = new LCDashboardDetailedWorkloadDTO();
				dto.setSegment(null != rs.getString("segment_out") ? rs.getString("segment_out") : "");
				dto.setRegion(null != rs.getString("region_out") ? rs.getString("region_out") : "");
				dto.setInstallationCountry(
						null != rs.getString("installation_country_out") ? rs.getString("installation_country_out")
								: "");
				dto.setOpportunityId(
						null != rs.getString("opportunity_id_out") ? rs.getString("opportunity_id_out") : "");
				dto.setProject(null != rs.getString("project_id_out") ? rs.getString("project_id_out") : "");
				dto.setProjectName(
						null != rs.getString("master_project_name_out") ? rs.getString("master_project_name_out") : "");
				dto.setCustomerName(
						null != rs.getString("contract_customer_out") ? rs.getString("contract_customer_out") : "");
				dto.setUnitId(null != rs.getString("unit_id_out") ? rs.getString("unit_id_out") : "");
				dto.setMachineType(null != rs.getString("machine_type_out") ? rs.getString("machine_type_out") : "");
				dto.setCurrentStatus(null != rs.getString("master_project_phase_map_out")
						? rs.getString("master_project_phase_map_out")
						: "");
				dto.setPm(null != rs.getString("pm_name_out") ? rs.getString("pm_name_out") : "");
				dto.setSm(null != rs.getString("shipping_manager_out") ? rs.getString("shipping_manager_out") : "");
				dto.setFreightTermsTitleTransfer(
						null != rs.getString("freight_term_out") ? rs.getString("freight_term_out") : "");
				dto.setIncotermsCategory(
						null != rs.getString("incoterms_category_out") ? rs.getString("incoterms_category_out") : "");
				dto.setRacDate(null != rs.getString("rac_out") ? rs.getString("rac_out") : "");
				dto.setBpBudgetAsSold(null != rs.getString("bp_bdg_out") ? rs.getString("bp_bdg_out") : "");
				dto.setBpLastBudget(null != rs.getString("bp_last_bdg_out") ? rs.getString("bp_last_bdg_out") : "");
				dto.setBpActualCost(null != rs.getString("bp_act_out") ? rs.getString("bp_act_out") : "");
				dto.setBpRate(null != rs.getString("product_rate_out") ? rs.getString("product_rate_out") : "");
				dto.setBpVolumeOTR(null != rs.getString("volume_otr_out") ? rs.getString("volume_otr_out") : "");
				dto.setVolumePacked(null != rs.getString("volume_packed_out") ? rs.getString("volume_packed_out") : "");
				dto.setVolumePackedLastMonth(null != rs.getString("volume_packed_last_month_out")
						? rs.getString("volume_packed_last_month_out")
						: "");
				dto.setWood(null != rs.getString("wood_out") ? rs.getString("wood_out") : "");
				dto.setThermo(null != rs.getString("thermo_out") ? rs.getString("thermo_out") : "");
				dto.setUnpacked(null != rs.getString("bp_not_needed_out") ? rs.getString("bp_not_needed_out") : "");
				dto.setFirstBoxExpirationDate(
						null != rs.getString("expiration_date_out") ? rs.getString("expiration_date_out") : "");
				dto.setReadyForShipment(
						null != rs.getString("ready_for_shipment_out") ? rs.getString("ready_for_shipment_out") : "");
				dto.setFirstExWorksDate(
						null != rs.getString("exworks_date_out") ? rs.getString("exworks_date_out") : "");
				dto.setTransportationBudgetAsSold(
						null != rs.getString("transportation_bdg_out") ? rs.getString("transportation_bdg_out") : "");
				dto.setTransportationLastBudget(null != rs.getString("transportation_last_bdg_out")
						? rs.getString("transportation_last_bdg_out")
						: "");
				dto.setTransportationActualCost(
						null != rs.getString("transportation_act_out") ? rs.getString("transportation_act_out") : "");
				dto.setTransportationRate(
						null != rs.getString("transportation_rate_out") ? rs.getString("transportation_rate_out") : "");
				dto.setVolumeShipped(
						null != rs.getString("volume_shipped_out") ? rs.getString("volume_shipped_out") : "");
				dto.setVolumeMSD(null != rs.getString("volume_msd_out") ? rs.getString("volume_msd_out") : "");
				dto.setShippedWood(null != rs.getString("shipped_wood_out") ? rs.getString("shipped_wood_out") : "");
				dto.setShippedThermo(
						null != rs.getString("shipped_thermo_out") ? rs.getString("shipped_thermo_out") : "");
				dto.setShippedUnpacked(
						null != rs.getString("shipped_bp_not_needed_out") ? rs.getString("shipped_bp_not_needed_out")
								: "");
				dto.setInternalDoc(null != rs.getString("internal_doc_out") ? rs.getString("internal_doc_out") : "");
				dto.setVdr(null != rs.getString("vdr_out") ? rs.getString("vdr_out") : "");
				list.add(dto);
			}

		} catch (SQLException e) {
			log.error("Exception while getting LC Detailed Workload Details :: " + e.getMessage());
		}
		return list;
	}

	@Override
	public List<DropDownDTO> getBPSiteAnalysisSiteDropDown(String projectList) {
		List<DropDownDTO> siteList = new ArrayList<DropDownDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(LogisticCostDashboardConstants.GET_LC_BP_SITE_ANALYSIS_SITE_LIST)) {
			pstm.setString(1, projectList);
			pstm.setString(2, projectList);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				if (null != rs.getString(1)) {
					DropDownDTO dto = new DropDownDTO();
					dto.setKey(null != rs.getString("site_name") ? rs.getString("site_name") : "");
					dto.setVal(null != rs.getString("site_name") ? rs.getString("site_name") : "");
					siteList.add(dto);
				}
			}
		} catch (Exception e) {
			log.error("Exception in LogisticCostDashboardDAOImpl :: getBPSiteAnalysisSiteDropDown() :: "
					+ e.getMessage());
		}
		return siteList;
	}

	@Override
	public List<DropDownDTO> getBPSiteAnalysisPackTypeDropDown(String projectList) {
		List<DropDownDTO> packTypeList = new ArrayList<DropDownDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(LogisticCostDashboardConstants.GET_LC_BP_SITE_ANALYSIS_PACK_TYPE_LIST)) {
			pstm.setString(1, projectList);
			pstm.setString(2, projectList);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				if (null != rs.getString(1)) {
					DropDownDTO dto = new DropDownDTO();
					dto.setKey(null != rs.getString("category") ? rs.getString("category") : "");
					dto.setVal(null != rs.getString("category") ? rs.getString("category") : "");
					packTypeList.add(dto);
				}
			}
		} catch (Exception e) {
			log.error("Exception in LogisticCostDashboardDAOImpl :: getBPSiteAnalysisPackTypeDropDown() :: "
					+ e.getMessage());
		}
		return packTypeList;
	}

	@Override
	public List<DropDownDTO> getBPSiteAnalysisColorByDropDown() {
		String[] list = { "SITE", "PACKING TYPE" };
		List<DropDownDTO> colorByList = new ArrayList<DropDownDTO>();
		for (String obj : list) {
			DropDownDTO dto = new DropDownDTO();
			dto.setKey(obj);
			dto.setVal(obj);
			colorByList.add(dto);
		}
		return colorByList;
	}

	@Override
	public DropDownDTO getBPSiteAnalysisDefaultDates() {
		DropDownDTO defaultDatesDTO = new DropDownDTO();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(LogisticCostDashboardConstants.GET_LC_BP_SITE_ANALYSIS_DEFAULT_DATES)) {
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				if (null != rs.getString(1)) {
					defaultDatesDTO.setKey(null != rs.getString("start_date") ? rs.getString("start_date") : "");
					defaultDatesDTO.setVal(null != rs.getString("end_date") ? rs.getString("end_date") : "");
				}
			}
		} catch (Exception e) {
			log.error("Exception in LogisticCostDashboardDAOImpl :: getBPSiteAnalysisDefaultDates() :: "
					+ e.getMessage());
		}
		return defaultDatesDTO;
	}

	@Override
	public String getBPSiteAnalysisSummaryProjectList(LCDashboardBPSiteAnalysisFilterDTO paramList, String startDate,
			String endDate) {
		String defaultProjects = "0";
		ResultSet rs = null;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(LogisticCostDashboardConstants.GET_LC_BP_SITE_ANALYSIS_PROJECT_LIST);) {
			pstm.setInt(1, paramList.getCustomerId());
			pstm.setInt(2, paramList.getCustomerId());
			pstm.setInt(3, paramList.getCompanyId());
			pstm.setInt(4, paramList.getCompanyId());
			pstm.setString(5, paramList.getBusinessUnit());
			pstm.setString(6, paramList.getBusinessUnit());
			pstm.setString(7, paramList.getSegment());
			pstm.setString(8, paramList.getSegment());
			pstm.setString(9, paramList.getRegion());
			pstm.setString(10, paramList.getRegion());
			pstm.setString(11, paramList.getFavProjects());
			pstm.setString(12, paramList.getFavProjects());
			pstm.setString(13, paramList.getPmLeader());
			pstm.setString(14, paramList.getPmLeader());
			pstm.setString(15, paramList.getSpm());
			pstm.setString(16, paramList.getSpm());
			pstm.setString(17, paramList.getFinancialSegment());
			pstm.setString(18, paramList.getFinancialSegment());
			pstm.setString(19, paramList.getShippingManager());
			pstm.setString(20, paramList.getShippingManager());
			pstm.setString(21, paramList.getSite());
			pstm.setString(22, paramList.getSite());
			pstm.setString(23, paramList.getPackType());
			pstm.setString(24, paramList.getPackType());
			pstm.setString(25, startDate);
			pstm.setString(26, endDate);
			rs = pstm.executeQuery();
			while (rs.next()) {
				String project = rs.getString(1);
				if (project != null && !project.equalsIgnoreCase("") && !defaultProjects.equalsIgnoreCase("")) {
					defaultProjects = defaultProjects + ";" + project;
				} else {
					defaultProjects = project;
				}
			}
			log.info("getBPSiteAnalysisSummaryProjectList :: defaultProjects :: " + defaultProjects);
			return defaultProjects;
		} catch (Exception e) {
			log.error("Exception in LogisticCostDashboardDAOImpl :: getBPSiteAnalysisSummaryProjectList() :: "
					+ e.getMessage());
			return defaultProjects;
		}
	}

	@Override
	public List<LCDashboardBPSiteAnalysisDTO> getBPSiteAnalysisChartSummary(String projectList,
			LCDashboardBPSiteAnalysisFilterDTO paramList, String startDate, String endDate, String chartType,
			String colorBy) {
		List<LCDashboardBPSiteAnalysisDTO> list = new ArrayList<LCDashboardBPSiteAnalysisDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(LogisticCostDashboardConstants.GET_LC_BP_SITE_ANALYSIS_CHART_SUMMARY)) {
			pstm.setString(1, projectList);
			pstm.setString(2, chartType);
			pstm.setString(3, colorBy);
			pstm.setString(4, paramList.getSite());
			pstm.setString(5, paramList.getPackType());
			pstm.setString(6, startDate);
			pstm.setString(7, endDate);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				LCDashboardBPSiteAnalysisDTO dto = new LCDashboardBPSiteAnalysisDTO();
				dto.setYearQuarter(null != rs.getString("period_out") ? rs.getString("period_out") : "");
				dto.setyAxis(null != rs.getString("name_out") ? rs.getString("name_out") : "");
				dto.setVolumePacked(null != rs.getString("volum_count") ? rs.getString("volum_count") : "");
				list.add(dto);
			}
		} catch (Exception e) {
			log.error("Exception in LogisticCostDashboardDAOImpl :: getBPSiteAnalysisChartSummary() :: "
					+ e.getMessage());
		}
		return list;
	}

	@Override
	public List<LCDashboardBPSiteAnalysisDetailsDTO> getBPSiteAnalysisDetails(String projectList, String startDate,
			String endDate) {
		List<LCDashboardBPSiteAnalysisDetailsDTO> list = new ArrayList<LCDashboardBPSiteAnalysisDetailsDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(LogisticCostDashboardConstants.GET_LC_BP_SITE_ANALYSIS_EXCEL_DOWNLOAD)) {
			pstm.setString(1, projectList);
			pstm.setString(2, startDate);
			pstm.setString(3, endDate);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				LCDashboardBPSiteAnalysisDetailsDTO dto = new LCDashboardBPSiteAnalysisDetailsDTO();
				dto.setMonthYear(null != rs.getString("period_out") ? rs.getString("period_out") : "");
				dto.setSite(null != rs.getString("site_name_out") ? rs.getString("site_name_out") : "");
				dto.setIncoterms(null != rs.getString("incoterms") ? rs.getString("incoterms") : "");
				dto.setWood(null != rs.getString("wood_count") ? rs.getString("wood_count") : "");
				dto.setThermo(null != rs.getString("thermo_count") ? rs.getString("thermo_count") : "");
				dto.setUnPacked(null != rs.getString("unpacked_count") ? rs.getString("unpacked_count") : "");
				list.add(dto);
			}
		} catch (Exception e) {
			log.error("Exception in LogisticCostDashboardDAOImpl :: getBPSiteAnalysisDetails() :: " + e.getMessage());
		}
		return list;
	}

	@Override
	public List<DropDownDTO> getTransportationAnalysisRegionDropDown(String projectList) {
		List<DropDownDTO> regionList = new ArrayList<DropDownDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(LogisticCostDashboardConstants.GET_LC_TRANSPORTATION_ANALYSIS_REGION_LIST)) {
			pstm.setString(1, projectList);
			pstm.setString(2, projectList);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				if (null != rs.getString(1)) {
					DropDownDTO dto = new DropDownDTO();
					dto.setKey(null != rs.getString("region") ? rs.getString("region") : "");
					dto.setVal(null != rs.getString("region") ? rs.getString("region") : "");
					regionList.add(dto);
				}
			}
		} catch (Exception e) {
			log.error("Exception in LogisticCostDashboardDAOImpl :: getTransportationAnalysisRegionDropDown() :: "
					+ e.getMessage());
		}

		return regionList;
	}

	@Override
	public List<DropDownDTO> getTransportationAnalysisIncotermsDropDown(String projectList) {
		List<DropDownDTO> incotermsList = new ArrayList<DropDownDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(
						LogisticCostDashboardConstants.GET_LC_TRANSPORTATION_ANALYSIS_INCOTERMS_LIST)) {
			pstm.setString(1, projectList);
			pstm.setString(2, projectList);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				if (null != rs.getString(1)) {
					DropDownDTO dto = new DropDownDTO();
					dto.setKey(null != rs.getString("freight_term") ? rs.getString("freight_term") : "");
					dto.setVal(null != rs.getString("freight_term") ? rs.getString("freight_term") : "");
					incotermsList.add(dto);
				}
			}
		} catch (Exception e) {
			log.error("Exception in LogisticCostDashboardDAOImpl :: getTransportationAnalysisIncotermsDropDown() :: "
					+ e.getMessage());
		}
		return incotermsList;
	}

	@Override
	public List<DropDownDTO> getTransportationAnalysisColorByDropDown() {
		String[] list = { "REGION", "INCOTERMS", "PACKING TYPE" };
		List<DropDownDTO> colorByList = new ArrayList<DropDownDTO>();
		for (String obj : list) {
			DropDownDTO dto = new DropDownDTO();
			dto.setKey(obj);
			dto.setVal(obj);
			colorByList.add(dto);
		}
		return colorByList;
	}

	@Override
	public DropDownDTO getTransportationAnalysisDefaultDates() {
		DropDownDTO defaultDatesDTO = new DropDownDTO();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(
						LogisticCostDashboardConstants.GET_LC_TRANSPORTATION_ANALYSIS_DEFAULT_DATES)) {
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				if (null != rs.getString(1)) {
					defaultDatesDTO.setKey(null != rs.getString("start_date") ? rs.getString("start_date") : "");
					defaultDatesDTO.setVal(null != rs.getString("end_date") ? rs.getString("end_date") : "");
				}
			}
		} catch (Exception e) {
			log.error("Exception in LogisticCostDashboardDAOImpl :: getTransportationAnalysisDefaultDates() :: "
					+ e.getMessage());
		}
		return defaultDatesDTO;
	}

	@Override
	public String getLCDashboardTransportationAnalysisSummaryProjectList(
			LCDashboardTransportationAnalysisFilterDTO paramList, String startDate, String endDate) {
		String defaultProjects = "0";
		ResultSet rs = null;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(
						LogisticCostDashboardConstants.GET_LC_TRANSPORTATION_ANALYSIS_PROJECT_LIST);) {
			pstm.setInt(1, paramList.getCustomerId());
			pstm.setInt(2, paramList.getCustomerId());
			pstm.setInt(3, paramList.getCompanyId());
			pstm.setInt(4, paramList.getCompanyId());
			pstm.setString(5, paramList.getBusinessUnit());
			pstm.setString(6, paramList.getBusinessUnit());
			pstm.setString(7, paramList.getSegment());
			pstm.setString(8, paramList.getSegment());
			pstm.setString(9, paramList.getRegion());
			pstm.setString(10, paramList.getRegion());
			pstm.setString(11, paramList.getFavProjects());
			pstm.setString(12, paramList.getFavProjects());
			pstm.setString(13, paramList.getPmLeader());
			pstm.setString(14, paramList.getPmLeader());
			pstm.setString(15, paramList.getSpm());
			pstm.setString(16, paramList.getSpm());
			pstm.setString(17, paramList.getFinancialSegment());
			pstm.setString(18, paramList.getFinancialSegment());
			pstm.setString(19, paramList.getShippingManager());
			pstm.setString(20, paramList.getShippingManager());
			pstm.setString(21, paramList.getTransportationRegion());
			pstm.setString(22, paramList.getTransportationRegion());
			pstm.setString(23, paramList.getIncoterms());
			pstm.setString(24, paramList.getIncoterms());
			pstm.setString(25, startDate);
			pstm.setString(26, endDate);
			rs = pstm.executeQuery();
			while (rs.next()) {
				String project = rs.getString(1);
				if (project != null && !project.equalsIgnoreCase("") && !defaultProjects.equalsIgnoreCase("")) {
					defaultProjects = defaultProjects + ";" + project;
				} else {
					defaultProjects = project;
				}
			}
			log.info("defaultProjects :: " + defaultProjects);
			return defaultProjects;
		} catch (Exception e) {
			log.error("Exception in LogisticCostDashboardDAOImpl :: getTransportationAnalysisSummaryProjectList() :: "
					+ e.getMessage());
			return defaultProjects;
		}
	}

	@Override
	public List<LCDashboardTransportationAnalysisDTO> getTransportationAnalysisChartSummary(String projectList,
			LCDashboardTransportationAnalysisFilterDTO paramList, String startDate, String endDate, String chartType,
			String colorBy) {
		List<LCDashboardTransportationAnalysisDTO> list = new ArrayList<LCDashboardTransportationAnalysisDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(
						LogisticCostDashboardConstants.GET_LC_TRANSPORTATION_ANALYSIS_CHART_SUMMARY)) {
			pstm.setString(1, projectList);
			pstm.setString(2, chartType);
			pstm.setString(3, colorBy);
			pstm.setString(4, paramList.getTransportationRegion());
			pstm.setString(5, paramList.getIncoterms());
			pstm.setString(6, startDate);
			pstm.setString(7, endDate);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				LCDashboardTransportationAnalysisDTO dto = new LCDashboardTransportationAnalysisDTO();
				dto.setYearQuarter(null != rs.getString("period_out") ? rs.getString("period_out") : "");
				dto.setyAxis(null != rs.getString("name_out") ? rs.getString("name_out") : "");
				dto.setVolShipped(null != rs.getString("volum_count") ? rs.getString("volum_count") : "");
				dto.setTotSpend(null != rs.getString("po_count") ? rs.getString("po_count") : "");
				list.add(dto);
			}
		} catch (Exception e) {
			log.error("Exception in LogisticCostDashboardDAOImpl :: getTransportationAnalysisChartSummary() :: "
					+ e.getMessage());
		}
		return list;
	}

	@Override
	public List<LCDashboardTransportationAnalysisDetailsDTO> getTransportationAnalysisDetails(String projectList,
			String startDate, String endDate) {
		List<LCDashboardTransportationAnalysisDetailsDTO> list = new ArrayList<LCDashboardTransportationAnalysisDetailsDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(
						LogisticCostDashboardConstants.GET_LC_TRANSPORTATION_ANALYSIS_EXCEL_DOWNLOAD)) {
			pstm.setString(1, projectList);
			pstm.setString(2, startDate);
			pstm.setString(3, endDate);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				LCDashboardTransportationAnalysisDetailsDTO dto = new LCDashboardTransportationAnalysisDetailsDTO();
				dto.setMotherJob(null != rs.getString("project_id_out") ? rs.getString("project_id_out") : "");
				dto.setSubProject(null != rs.getString("job_out") ? rs.getString("job_out") : "");
				dto.setRegion(null != rs.getString("region_name_out") ? rs.getString("region_name_out") : "");
				dto.setIncoterms(null != rs.getString("incoterms") ? rs.getString("incoterms") : "");
				dto.setVolShipped(null != rs.getString("sum_volume") ? rs.getString("sum_volume") : "");
				dto.setWeight(null != rs.getString("weight") ? rs.getString("weight") : "");
				dto.setTotalSpend(null != rs.getString("total_spend") ? rs.getString("total_spend") : "");
				list.add(dto);
			}
		} catch (Exception e) {
			log.error("Exception in LogisticCostDashboardDAOImpl :: getTransportationAnalysisDetails() :: "
					+ e.getMessage());
		}
		return list;
	}

	@Override
	public List<LegendColorDTO> getLCDashboardChartConfiguration(String colorBy) {
		List<LegendColorDTO> list = new ArrayList<LegendColorDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(LogisticCostDashboardConstants.GET_LC_CHART_CONFIGURATION);) {
			pstm.setString(1, colorBy);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				LegendColorDTO dto = new LegendColorDTO();
				dto.setLegendName(null != rs.getString("legend_name") ? rs.getString("legend_name") : "");
				dto.setKey(null != rs.getString("key_cc") ? rs.getString("key_cc") : "");
				dto.setColor(null != rs.getString("color") ? rs.getString("color") : "");
				list.add(dto);
			}
		} catch (SQLException e) {
			log.error("Error in getOpenBarConfig :: " + e.getMessage());
		}
		return list;
	}

}

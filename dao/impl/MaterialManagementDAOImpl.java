package com.bh.realtrack.dao.impl;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ws.rs.ServerErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.bh.realtrack.dao.IMaterialManagementDAO;
import com.bh.realtrack.dao.helper.MaterialManagementDAOHelper;
import com.bh.realtrack.dto.DropDownDTO;
import com.bh.realtrack.dto.MMMoveOrderPopupDetailsDTO;
import com.bh.realtrack.dto.MMPackingFDChartDetailsDTO;
import com.bh.realtrack.dto.MMPackingFDPopupDetailsDTO;
import com.bh.realtrack.dto.MMPackingFDSummaryDetailsDTO;
import com.bh.realtrack.dto.MMPickingChartDetailsDTO;
import com.bh.realtrack.dto.MMPickingPopupDetailsDTO;
import com.bh.realtrack.dto.MMPickingSummaryDetailsDTO;
import com.bh.realtrack.dto.MMReceivingChartDetailsDTO;
import com.bh.realtrack.dto.MMReceivingPopupDetailsDTO;
import com.bh.realtrack.dto.MMReceivingSummaryDetailsDTO;
import com.bh.realtrack.dto.MMWfAnomalyPopupDetailsDTO;
import com.bh.realtrack.dto.MMWfAnomalySummaryDetailsDTO;
import com.bh.realtrack.enumerators.ErrorCode;
import com.bh.realtrack.util.MaterialManagementConstants;

/**
 * @author Shweta D. Sawant
 */
@Repository
public class MaterialManagementDAOImpl implements IMaterialManagementDAO {
	private static final Logger log = LoggerFactory.getLogger(MaterialManagementDAOImpl.class.getName());
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public List<DropDownDTO> getMMReceivingSubProjectFilter(String projectId) {
		return jdbcTemplate.query(MaterialManagementConstants.GET_MM_RECEIVING_SUB_PROJECT_FILTER,
				new Object[] { projectId }, new ResultSetExtractor<List<DropDownDTO>>() {
					@Override
					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
						List<DropDownDTO> subProjectFilterList = new ArrayList<DropDownDTO>();
						try {
							String subProject = null;
							while (rs.next()) {
								DropDownDTO dropDownDTO = new DropDownDTO();
								subProject = rs.getString("sub_project");
								if (null != subProject) {
									dropDownDTO.setKey(subProject);
									dropDownDTO.setVal(subProject);
									subProjectFilterList.add(dropDownDTO);
								}
							}
						} catch (Exception e) {
							log.error("Error in getting Material Management : Receiving - Sub Project Filter :: "
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return subProjectFilterList;
					}
				});
	}

	@Override
	public List<DropDownDTO> getMMReceivingOrganizationFilter(String projectId) {
		return jdbcTemplate.query(MaterialManagementConstants.GET_MM_RECEIVING_ORGANIZATION_FILTER,
				new Object[] { projectId }, new ResultSetExtractor<List<DropDownDTO>>() {
					@Override
					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
						List<DropDownDTO> ownershipFilterList = new ArrayList<DropDownDTO>();
						try {
							String organization = null;
							while (rs.next()) {
								DropDownDTO dropDownDTO = new DropDownDTO();
								organization = rs.getString("organization");
								if (null != organization) {
									dropDownDTO.setKey(organization);
									dropDownDTO.setVal(organization);
									ownershipFilterList.add(dropDownDTO);
								}
							}
						} catch (Exception e) {
							log.error("Error in getting Material Management : Receiving - Organization Filter :: "
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return ownershipFilterList;
					}
				});
	}

	@Override
	public List<DropDownDTO> getMMReceivingLocationFilter(String projectId) {
		return jdbcTemplate.query(MaterialManagementConstants.GET_MM_RECEIVING_LOCATION_FILTER,
				new Object[] { projectId }, new ResultSetExtractor<List<DropDownDTO>>() {
					@Override
					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
						List<DropDownDTO> locationFilterList = new ArrayList<DropDownDTO>();
						try {
							String location = null;
							while (rs.next()) {
								DropDownDTO dropDownDTO = new DropDownDTO();
								location = rs.getString("location");
								if (null != location) {
									dropDownDTO.setKey(location);
									dropDownDTO.setVal(location);
									locationFilterList.add(dropDownDTO);
								}
							}
						} catch (Exception e) {
							log.error("Error in getting Material Management : Receiving - Location Filter :: "
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return locationFilterList;
					}
				});
	}

	@Override
	public MMReceivingSummaryDetailsDTO getMMReceivingCountDetails(String projectId, String subProject,
			String organization, String location) {
		Connection con = null;
		MMReceivingSummaryDetailsDTO summaryDTO = new MMReceivingSummaryDetailsDTO();
		try {
			String[] subProjectStr = subProject.split(";");
			String[] organizationStr = organization.split(";");
			String[] locationStr = location.split(";");
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con.prepareStatement(MaterialManagementConstants.GET_MM_RECEIVING_SUMMARY_CNT);
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			Array organizationStrArr = con.createArrayOf("varchar", organizationStr);
			Array locationStrArr = con.createArrayOf("varchar", locationStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setArray(3, subProjectStrArr);
			pstm.setArray(4, organizationStrArr);
			pstm.setArray(5, organizationStrArr);
			pstm.setArray(6, locationStrArr);
			pstm.setArray(7, locationStrArr);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				summaryDTO.setTotal(null != rs.getString("total") ? rs.getString("total") : "");
				summaryDTO.setMaterialArrived(
						null != rs.getString("material_arrived") ? rs.getString("material_arrived") : "");
				summaryDTO.setOnInventory(null != rs.getString("on_inventory") ? rs.getString("on_inventory") : "");
				summaryDTO.setOnReceivingArea(
						null != rs.getString("on_receiving_area") ? rs.getString("on_receiving_area") : "");
			}
		} catch (SQLException e) {
			log.error("Error in getting Material Management : Receiving - Summary Counts :: " + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("Error in getting Material Management : Receiving - Summary Counts :: " + e.getMessage());
				}
			}
		}
		return summaryDTO;
	}

	@Override
	public Map<String, Object> getMMReceivingAgingPieChart(String projectId, String subProject, String organization,
			String location) {
		Connection con = null;
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("critical", "0");
		resultMap.put("nonCritical", "0");
		try {
			String[] subProjectStr = subProject.split(";");
			String[] organizationStr = organization.split(";");
			String[] locationStr = location.split(";");
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con.prepareStatement(MaterialManagementConstants.GET_MM_RECEIVING_AGING_PIE_CHART);
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			Array organizationStrArr = con.createArrayOf("varchar", organizationStr);
			Array locationStrArr = con.createArrayOf("varchar", locationStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setArray(3, subProjectStrArr);
			pstm.setArray(4, organizationStrArr);
			pstm.setArray(5, organizationStrArr);
			pstm.setArray(6, locationStrArr);
			pstm.setArray(7, locationStrArr);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				String agingNoteVal = "", count = "";
				agingNoteVal = rs.getString("aging_note");
				count = rs.getString("count");
				if (null != agingNoteVal && !"".equalsIgnoreCase(agingNoteVal) && resultMap.containsKey(agingNoteVal)) {
					resultMap.put(agingNoteVal, count);
				}
			}
		} catch (SQLException e) {
			log.error("Error in getting Material Management : Receiving - Aging Pie Chart :: " + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error(
							"Error in getting Material Management : Receiving - Aging Pie Chart :: " + e.getMessage());
				}
			}
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> getMMReceivingWfAnomalyPieChart(String projectId, String subProject, String organization,
			String location) {
		Connection con = null;
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("anomalyStillOpen", "0");
		resultMap.put("anomalyClosed", "0");
		resultMap.put("withoutAnomaly", "0");
		try {
			String[] subProjectStr = subProject.split(";");
			String[] organizationStr = organization.split(";");
			String[] locationStr = location.split(";");
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con
					.prepareStatement(MaterialManagementConstants.GET_MM_RECEIVING_WF_ANOMALY_PIE_CHART);
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			Array organizationStrArr = con.createArrayOf("varchar", organizationStr);
			Array locationStrArr = con.createArrayOf("varchar", locationStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setArray(3, subProjectStrArr);
			pstm.setArray(4, organizationStrArr);
			pstm.setArray(5, organizationStrArr);
			pstm.setArray(6, locationStrArr);
			pstm.setArray(7, locationStrArr);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				String anomalyNoteVal = "", count = "";
				anomalyNoteVal = rs.getString("anomaly_note");
				count = rs.getString("count");
				if (null != anomalyNoteVal && !"".equalsIgnoreCase(anomalyNoteVal)
						&& resultMap.containsKey(anomalyNoteVal)) {
					resultMap.put(anomalyNoteVal, count);
				}
			}
		} catch (SQLException e) {
			log.error("Error in getting Material Management : Receiving - WF Anomaly Pie Chart :: " + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("Error in getting Material Management : Receiving - WF Anomaly Pie Chart :: "
							+ e.getMessage());
				}
			}
		}
		return resultMap;
	}

	@Override
	public List<MMReceivingPopupDetailsDTO> getMMReceivingPopupDetails(String projectId, String subProject,
			String organization, String location, String type, String startDate, String endDate, String fiscalWeek) {
		Connection con = null;
		List<MMReceivingPopupDetailsDTO> receivingPopupDetails = new ArrayList<MMReceivingPopupDetailsDTO>();
		try {
			String[] subProjectStr = subProject.split(";");
			String[] organizationStr = organization.split(";");
			String[] locationStr = location.split(";");
			con = jdbcTemplate.getDataSource().getConnection();
			String queryString = MaterialManagementDAOHelper.getMMReceivingDetailsQuery(type);
			PreparedStatement pstm = con.prepareStatement(queryString);
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			Array organizationStrArr = con.createArrayOf("varchar", organizationStr);
			Array locationStrArr = con.createArrayOf("varchar", locationStr);
			if (type.equalsIgnoreCase("CYCLE_TIME_CHART_WEEKLY")) {
				pstm.setString(1, startDate);
				pstm.setString(2, endDate);
				pstm.setString(3, projectId);
				pstm.setArray(4, subProjectStrArr);
				pstm.setArray(5, subProjectStrArr);
				pstm.setArray(6, organizationStrArr);
				pstm.setArray(7, organizationStrArr);
				pstm.setArray(8, locationStrArr);
				pstm.setArray(9, locationStrArr);
				pstm.setString(10, fiscalWeek);
			} else {
				pstm.setString(1, projectId);
				pstm.setArray(2, subProjectStrArr);
				pstm.setArray(3, subProjectStrArr);
				pstm.setArray(4, organizationStrArr);
				pstm.setArray(5, organizationStrArr);
				pstm.setArray(6, locationStrArr);
				pstm.setArray(7, locationStrArr);
			}
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				MMReceivingPopupDetailsDTO receivingPopupDTO = new MMReceivingPopupDetailsDTO();
				receivingPopupDTO.setCostingProject(
						null != rs.getString("costing_project") ? rs.getString("costing_project") : "");
				receivingPopupDTO.setOm(null != rs.getString("om") ? rs.getString("om") : "");
				receivingPopupDTO.setPei(null != rs.getString("pei") ? rs.getString("pei") : "");
				receivingPopupDTO.setPeiDescription(
						null != rs.getString("pei_description") ? rs.getString("pei_description") : "");
				receivingPopupDTO
						.setOrderedItem(null != rs.getString("ordered_item") ? rs.getString("ordered_item") : "");
				receivingPopupDTO
						.setDescription(null != rs.getString("description") ? rs.getString("description") : "");
				receivingPopupDTO.setQty(null != rs.getString("quantity") ? rs.getString("quantity") : "");
				receivingPopupDTO.setFatherCode(null != rs.getString("father_code") ? rs.getString("father_code") : "");
				receivingPopupDTO.setStatus(null != rs.getString("status") ? rs.getString("status") : "");
				receivingPopupDTO.setOwner(null != rs.getString("owner") ? rs.getString("owner") : "");
				receivingPopupDTO.setNotes(null != rs.getString("notes") ? rs.getString("notes") : "");
				receivingPopupDTO
						.setSupplyStatus(null != rs.getString("supply_status") ? rs.getString("supply_status") : "");
				receivingPopupDTO.setAscpOrderNumber(
						null != rs.getString("ascp_order_number") ? rs.getString("ascp_order_number") : "");
				receivingPopupDTO.setVendor(null != rs.getString("vendor") ? rs.getString("vendor") : "");
				receivingPopupDTO
						.setPromisedDate(null != rs.getString("promised_date") ? rs.getString("promised_date") : "");
				receivingPopupDTO.setPoChild(null != rs.getString("po_child") ? rs.getString("po_child") : "");
				receivingPopupDTO.setSpa(null != rs.getString("spa") ? rs.getString("spa") : "");
				receivingPopupDTO.setBuyer(null != rs.getString("buyer") ? rs.getString("buyer") : "");
				receivingPopupDTO.setFulfillmentResponsibility(
						null != rs.getString("fulfillment_responsibility") ? rs.getString("fulfillment_responsibility")
								: "");
				receivingPopupDTO.setInvOrg(null != rs.getString("inv_org") ? rs.getString("inv_org") : "");
				receivingPopupDTO.setPlant(null != rs.getString("location") ? rs.getString("location") : "");
				receivingPopupDTO.setPoFather(null != rs.getString("po_father") ? rs.getString("po_father") : "");
				receivingPopupDTO.setContract(null != rs.getString("project_id") ? rs.getString("project_id") : "");
				receivingPopupDTO.setPoHeaderNumber(
						null != rs.getString("po_header_number") ? rs.getString("po_header_number") : "");
				receivingPopupDTO
						.setPoLineNumber(null != rs.getString("po_line_number") ? rs.getString("po_line_number") : "");
				receivingPopupDTO.setMaxArrivalDate(
						null != rs.getString("max_arrival_date") ? rs.getString("max_arrival_date") : "");
				receivingPopupDTO.setReceiptDate(
						null != rs.getString("transaction_receipt_date") ? rs.getString("transaction_receipt_date")
								: "");
				receivingPopupDTO.setDeliverDate(
						null != rs.getString("transaction_delivery_date") ? rs.getString("transaction_delivery_date")
								: "");
				receivingPopupDTO.setWfAnomalyNumber(
						null != rs.getString("wf_anomaly_number") ? rs.getString("wf_anomaly_number") : "");
				receivingPopupDTO
						.setAnomalyType(null != rs.getString("anomaly_type") ? rs.getString("anomaly_type") : "");
				receivingPopupDTO.setAnomalyCategory(
						null != rs.getString("anomaly_category") ? rs.getString("anomaly_category") : "");
				receivingPopupDTO
						.setAnomalyCode(null != rs.getString("anomaly_code") ? rs.getString("anomaly_code") : "");
				receivingPopupDTO.setAnomalyOpenDate(
						null != rs.getString("anomaly_open_date") ? rs.getString("anomaly_open_date") : "");
				receivingPopupDTO.setAnomalyCloseDate(
						null != rs.getString("anomaly_close_date") ? rs.getString("anomaly_close_date") : "");
				receivingPopupDTO.setOwnership(null != rs.getString("ownership") ? rs.getString("ownership") : "");
				receivingPopupDetails.add(receivingPopupDTO);
			}
		} catch (SQLException e) {
			log.error("Error in getting Material Management : Receiving - " + type + " Popup :: " + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("Error in getting Material Management : Receiving - " + type + " Popup :: "
							+ e.getMessage());
				}
			}
		}
		return receivingPopupDetails;
	}

	private String getLastDateOfFiscalWeek(String fiscalWeek) {
		Calendar cal = Calendar.getInstance();
		DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss z uuuu").withLocale(Locale.US);
		DateTimeFormatter outputformat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		String formatedDate = "";
		try {
			if (fiscalWeek != null && !fiscalWeek.isEmpty()) {
				String[] fiscalWeekStr = fiscalWeek.split("-");
				cal.set(Calendar.WEEK_OF_YEAR, Integer.parseInt(fiscalWeekStr[1]));
				cal.set(Calendar.YEAR, Integer.parseInt(fiscalWeekStr[2]));
				cal.set(Calendar.DAY_OF_WEEK, cal.getActualMaximum(Calendar.DAY_OF_WEEK));
				ZonedDateTime zoneDt = ZonedDateTime.parse(cal.getTime().toString(), inputFormat);
				LocalDate localDt = zoneDt.toLocalDate();
				formatedDate = localDt.format(outputformat);
			} else {
				throw new Exception("No Fiscal Week selected: ");
			}
		} catch (Exception e) {
			log.error("getLastDateOfFiscalWeek(): Exception occurred : " + e.getMessage());
		}
		return formatedDate;
	}

	@Override
	public List<MMReceivingChartDetailsDTO> getMMReceivingChartDetails(String projectId, String subProject,
			String organization, String location, String startDate, String endDate, String weekOrMonth) {
		Connection con = null;
		List<MMReceivingChartDetailsDTO> chartDetails = new ArrayList<MMReceivingChartDetailsDTO>();
		try {
			String[] subProjectStr = subProject.split(";");
			String[] organizationStr = organization.split(";");
			String[] locationStr = location.split(";");
			con = jdbcTemplate.getDataSource().getConnection();
			String lastUpdateDate = getLastUpdatedDate(projectId);
			PreparedStatement pstm = con
					.prepareStatement(MaterialManagementConstants.GET_MM_RECEIVING_WEEKLY_CHART_DETAILS);
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			Array organizationStrArr = con.createArrayOf("varchar", organizationStr);
			Array locationStrArr = con.createArrayOf("varchar", locationStr);
			pstm.setString(1, startDate);
			pstm.setString(2, endDate);
			pstm.setString(3, projectId);
			pstm.setArray(4, subProjectStrArr);
			pstm.setArray(5, subProjectStrArr);
			pstm.setArray(6, organizationStrArr);
			pstm.setArray(7, organizationStrArr);
			pstm.setArray(8, locationStrArr);
			pstm.setArray(9, locationStrArr);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				MMReceivingChartDetailsDTO chartDetailsDTO = new MMReceivingChartDetailsDTO();
				chartDetailsDTO.setFiscalWeek(null != rs.getString("fiscal_week") ? rs.getString("fiscal_week") : "");
				chartDetailsDTO.setTotalLines(null != rs.getString("cnt") ? rs.getString("cnt") : "");
				chartDetailsDTO.setP50(null != rs.getString("p50") ? rs.getString("p50") : "");
				chartDetailsDTO.setP75(null != rs.getString("p75") ? rs.getString("p75") : "");
				chartDetailsDTO.setP95(null != rs.getString("p95") ? rs.getString("p95") : "");
				boolean checkFlag = checkFiscalWeek(rs.getString("fiscal_week"), lastUpdateDate);
				if (checkFlag) {
					chartDetails.add(chartDetailsDTO);
				} else {
					chartDetailsDTO
							.setFiscalWeek(null != rs.getString("fiscal_week") ? rs.getString("fiscal_week") : "");
					chartDetailsDTO.setTotalLines(null);
					chartDetailsDTO.setCycleTime(null);
					chartDetailsDTO.setP50(null);
					chartDetailsDTO.setP75(null);
					chartDetailsDTO.setP95(null);
					chartDetails.add(chartDetailsDTO);
				}
			}
		} catch (SQLException e) {
			log.error("Error in getting Material Management : Receiving - Weekly Chart Details :: " + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("Error in getting Material Management : Receiving - Weekly Chart Details :: "
							+ e.getMessage());
				}
			}
		}
		return chartDetails;
	}

	public boolean checkFiscalWeek(String input, String lastUpdatedDate) {
		boolean checkFlag = false;
		DateTimeFormatter formatter = new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("dd-MMM-yyyy")
				.toFormatter();
		LocalDate localDate = LocalDate.parse(lastUpdatedDate, formatter);
		int currYear = Calendar.getInstance().get(Calendar.YEAR);
		int currFiscalWeek = localDate.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
		if (!input.isEmpty() && input != null && !input.equalsIgnoreCase("")) {
			String[] weekParts = input.split("-");
			if (Integer.parseInt(weekParts[2]) < currYear) {
				checkFlag = true;
			} else if (Integer.parseInt(weekParts[2]) == currYear && Integer.parseInt(weekParts[1]) <= currFiscalWeek) {
				checkFlag = true;
			}
		}
		return checkFlag;
	}

	@Override
	public List<MMReceivingPopupDetailsDTO> getMMReceivingDetailsExcel(String projectId, String subProject,
			String organization, String location) {
		Connection con = null;
		List<MMReceivingPopupDetailsDTO> excelDetails = new ArrayList<MMReceivingPopupDetailsDTO>();
		try {
			String[] subProjectStr = subProject.split(";");
			String[] organizationStr = organization.split(";");
			String[] locationStr = location.split(";");
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con.prepareStatement(MaterialManagementConstants.GET_MM_RECEIVING_DETAILS);
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			Array organizationStrArr = con.createArrayOf("varchar", organizationStr);
			Array locationStrArr = con.createArrayOf("varchar", locationStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setArray(3, subProjectStrArr);
			pstm.setArray(4, organizationStrArr);
			pstm.setArray(5, organizationStrArr);
			pstm.setArray(6, locationStrArr);
			pstm.setArray(7, locationStrArr);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				MMReceivingPopupDetailsDTO excelDetailsDTO = new MMReceivingPopupDetailsDTO();
				excelDetailsDTO.setCostingProject(
						null != rs.getString("costing_project") ? rs.getString("costing_project") : "");
				excelDetailsDTO.setOm(null != rs.getString("om") ? rs.getString("om") : "");
				excelDetailsDTO.setPei(null != rs.getString("pei") ? rs.getString("pei") : "");
				excelDetailsDTO.setPeiDescription(
						null != rs.getString("pei_description") ? rs.getString("pei_description") : "");
				excelDetailsDTO
						.setOrderedItem(null != rs.getString("ordered_item") ? rs.getString("ordered_item") : "");
				excelDetailsDTO.setDescription(null != rs.getString("description") ? rs.getString("description") : "");
				excelDetailsDTO.setQty(null != rs.getString("quantity") ? rs.getString("quantity") : "");
				excelDetailsDTO.setFatherCode(null != rs.getString("father_code") ? rs.getString("father_code") : "");
				excelDetailsDTO.setStatus(null != rs.getString("status") ? rs.getString("status") : "");
				excelDetailsDTO.setOwner(null != rs.getString("owner") ? rs.getString("owner") : "");
				excelDetailsDTO.setNotes(null != rs.getString("notes") ? rs.getString("notes") : "");
				excelDetailsDTO
						.setSupplyStatus(null != rs.getString("supply_status") ? rs.getString("supply_status") : "");
				excelDetailsDTO.setAscpOrderNumber(
						null != rs.getString("ascp_order_number") ? rs.getString("ascp_order_number") : "");
				excelDetailsDTO.setVendor(null != rs.getString("vendor") ? rs.getString("vendor") : "");
				excelDetailsDTO
						.setPromisedDate(null != rs.getString("promised_date") ? rs.getString("promised_date") : "");
				excelDetailsDTO.setPoChild(null != rs.getString("po_child") ? rs.getString("po_child") : "");
				excelDetailsDTO.setSpa(null != rs.getString("spa") ? rs.getString("spa") : "");
				excelDetailsDTO.setBuyer(null != rs.getString("buyer") ? rs.getString("buyer") : "");
				excelDetailsDTO.setFulfillmentResponsibility(
						null != rs.getString("fulfillment_responsibility") ? rs.getString("fulfillment_responsibility")
								: "");
				excelDetailsDTO.setInvOrg(null != rs.getString("inv_org") ? rs.getString("inv_org") : "");
				excelDetailsDTO.setPlant(null != rs.getString("location") ? rs.getString("location") : "");
				excelDetailsDTO.setPoFather(null != rs.getString("po_father") ? rs.getString("po_father") : "");
				excelDetailsDTO.setContract(null != rs.getString("project_id") ? rs.getString("project_id") : "");
				excelDetailsDTO.setPoHeaderNumber(
						null != rs.getString("po_header_number") ? rs.getString("po_header_number") : "");
				excelDetailsDTO
						.setPoLineNumber(null != rs.getString("po_line_number") ? rs.getString("po_line_number") : "");
				excelDetailsDTO.setMaxArrivalDate(
						null != rs.getString("max_arrival_date") ? rs.getString("max_arrival_date") : "");
				excelDetailsDTO.setReceiptDate(
						null != rs.getString("transaction_receipt_date") ? rs.getString("transaction_receipt_date")
								: "");
				excelDetailsDTO.setDeliverDate(
						null != rs.getString("transaction_delivery_date") ? rs.getString("transaction_delivery_date")
								: "");
				excelDetailsDTO.setWfAnomalyNumber(
						null != rs.getString("wf_anomaly_number") ? rs.getString("wf_anomaly_number") : "");
				excelDetailsDTO
						.setAnomalyType(null != rs.getString("anomaly_type") ? rs.getString("anomaly_type") : "");
				excelDetailsDTO.setAnomalyCategory(
						null != rs.getString("anomaly_category") ? rs.getString("anomaly_category") : "");
				excelDetailsDTO
						.setAnomalyCode(null != rs.getString("anomaly_code") ? rs.getString("anomaly_code") : "");
				excelDetailsDTO.setAnomalyOpenDate(
						null != rs.getString("anomaly_open_date") ? rs.getString("anomaly_open_date") : "");
				excelDetailsDTO.setAnomalyCloseDate(
						null != rs.getString("anomaly_close_date") ? rs.getString("anomaly_close_date") : "");
				excelDetailsDTO.setOwnership(null != rs.getString("ownership") ? rs.getString("ownership") : "");
				excelDetails.add(excelDetailsDTO);
			}
		} catch (SQLException e) {
			log.error("Error occured while getting data for Material Management : Receiving - Excel file :: "
					+ e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("Error occured while getting data for Material Management : Receiving - Excel file :: "
							+ e.getMessage());
				}
			}
		}
		return excelDetails;
	}

	@Override
	public String getLastUpdatedDate(String projectId) {
		return jdbcTemplate.query(MaterialManagementConstants.GET_MM_RECEIVING_LAST_REFRESHED_DATE,
				new Object[] { projectId }, new ResultSetExtractor<String>() {
					public String extractData(ResultSet rs) throws SQLException {
						String date = "";
						while (rs.next()) {
							date = rs.getString(1);
						}
						return date;
					}
				});
	}

	@Override
	public String checkIfProjectIsModule(String projectId) {
		return jdbcTemplate.query(MaterialManagementConstants.CHECK_IF_PROJECT_IS_MODULE, new Object[] { projectId },
				new ResultSetExtractor<String>() {
					public String extractData(ResultSet rs) throws SQLException {
						String flag = "";
						while (rs.next()) {
							flag = rs.getString(1);
						}
						return flag;
					}
				});
	}

	@Override
	public String getLastUpdatedDate(String projectId, String moveOrderType) {
		return jdbcTemplate.query(MaterialManagementConstants.GET_MM_PICKING_LAST_REFRESHED_DATE,
				new Object[] { projectId, moveOrderType }, new ResultSetExtractor<String>() {
					public String extractData(ResultSet rs) throws SQLException {
						String date = "";
						while (rs.next()) {
							date = rs.getString(1);
						}
						return date;
					}
				});
	}

	@Override
	public List<DropDownDTO> getMMPickingSubProjectFilter(String projectId, String moveOrderType) {
		return jdbcTemplate.query(MaterialManagementConstants.GET_MM_PICKING_SUB_PROJECT_FILTER,
				new Object[] { projectId, moveOrderType }, new ResultSetExtractor<List<DropDownDTO>>() {
					@Override
					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
						List<DropDownDTO> subProjectFilterList = new ArrayList<DropDownDTO>();
						try {
							String subProject = null;
							while (rs.next()) {
								DropDownDTO dropDownDTO = new DropDownDTO();
								subProject = rs.getString("sub_project");
								if (null != subProject) {
									dropDownDTO.setKey(subProject);
									dropDownDTO.setVal(subProject);
									subProjectFilterList.add(dropDownDTO);
								}
							}
						} catch (Exception e) {
							log.error("Error in getting Material Management : " + moveOrderType
									+ " Sub Project Filter :: " + e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return subProjectFilterList;
					}
				});
	}

	@Override
	public List<DropDownDTO> getMMPickingSubInvFromFilter(String projectId, String moveOrderType) {
		return jdbcTemplate.query(MaterialManagementConstants.GET_MM_PICKING_SUB_INVENTORY_FROM_FILTER,
				new Object[] { projectId, moveOrderType }, new ResultSetExtractor<List<DropDownDTO>>() {
					@Override
					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
						List<DropDownDTO> subInvFromFilterList = new ArrayList<DropDownDTO>();
						try {
							String subInvFrom = null;
							while (rs.next()) {
								DropDownDTO dropDownDTO = new DropDownDTO();
								subInvFrom = rs.getString("subInvFrom");
								if (null != subInvFrom) {
									dropDownDTO.setKey(subInvFrom);
									dropDownDTO.setVal(subInvFrom);
									subInvFromFilterList.add(dropDownDTO);
								}
							}
						} catch (Exception e) {
							log.error("Error in getting Material Management : " + moveOrderType
									+ " - Sub Inventory From Filter :: " + e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return subInvFromFilterList;
					}
				});

	}

	@Override
	public List<DropDownDTO> getMMPickingSubInvToFilter(String projectId, String moveOrderType) {
		return jdbcTemplate.query(MaterialManagementConstants.GET_MM_PICKING_SUB_INVENTORY_TO_FILTER,
				new Object[] { projectId, moveOrderType }, new ResultSetExtractor<List<DropDownDTO>>() {
					@Override
					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
						List<DropDownDTO> subInvToFilterList = new ArrayList<DropDownDTO>();
						try {
							String subInvTo = null;
							while (rs.next()) {
								DropDownDTO dropDownDTO = new DropDownDTO();
								subInvTo = rs.getString("subInvTo");
								if (null != subInvTo) {
									dropDownDTO.setKey(subInvTo);
									dropDownDTO.setVal(subInvTo);
									subInvToFilterList.add(dropDownDTO);
								}
							}
						} catch (Exception e) {
							log.error("Error in getting Material Management : " + moveOrderType
									+ " - Sub Inventory To Filter :: " + e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return subInvToFilterList;
					}
				});

	}

	@Override
	public MMPickingSummaryDetailsDTO getMMPickingSummaryDetails(String projectId, String subProject, String subInvFrom,
			String subInvTo, String moveOrderType) {
		Connection con = null;
		MMPickingSummaryDetailsDTO summaryDTO = new MMPickingSummaryDetailsDTO();
		try {
			String[] subProjectStr = subProject.split(";");
			String[] subInvFromStr = subInvFrom.split(";");
			String[] subInvToStr = subInvTo.split(";");
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con.prepareStatement(MaterialManagementConstants.GET_MM_PICKING_SUMMARY_CNT);
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			Array subInvFromStrArr = con.createArrayOf("varchar", subInvFromStr);
			Array subInvToStrArr = con.createArrayOf("varchar", subInvToStr);
			pstm.setString(1, projectId);
			pstm.setString(2, moveOrderType);
			pstm.setArray(3, subProjectStrArr);
			pstm.setArray(4, subProjectStrArr);
			pstm.setArray(5, subInvFromStrArr);
			pstm.setArray(6, subInvFromStrArr);
			pstm.setArray(7, subInvToStrArr);
			pstm.setArray(8, subInvToStrArr);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				summaryDTO.setTotalMoveOrder(null != rs.getString("total") ? rs.getString("total") : "");
				summaryDTO.setIssued(null != rs.getString("issued") ? rs.getString("issued") : "");
				summaryDTO.setLoaded(null != rs.getString("loaded") ? rs.getString("loaded") : "");
				summaryDTO.setDropped(null != rs.getString("dropped") ? rs.getString("dropped") : "");
			}
		} catch (SQLException e) {
			log.error("Error in getting Material Management : " + moveOrderType + " - Summary Counts :: "
					+ e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("Error in getting Material Management : " + moveOrderType + " - Summary Counts :: "
							+ e.getMessage());
				}
			}
		}
		return summaryDTO;
	}

	@Override
	public List<MMPickingChartDetailsDTO> getMMPickingChartDetails(String projectId, String subProject,
			String subInvFrom, String subInvTo, String startDate, String endDate, String moveOrderType) {
		Connection con = null;
		StringBuilder queryString = new StringBuilder();
		List<MMPickingChartDetailsDTO> chartDetails = new ArrayList<MMPickingChartDetailsDTO>();
		Map<String, MMPickingChartDetailsDTO> compareMap = new LinkedHashMap<String, MMPickingChartDetailsDTO>();
		MMPickingChartDetailsDTO compareDTO = new MMPickingChartDetailsDTO();
		try {
			String[] subProjectStr = subProject.split(";");
			String[] subInvFromStr = subInvFrom.split(";");
			String[] subInvToStr = subInvTo.split(";");
			con = jdbcTemplate.getDataSource().getConnection();
			String lastUpdateDate = getLastUpdatedDate(projectId, moveOrderType);
			if (moveOrderType.equalsIgnoreCase(MaterialManagementConstants.MOVE_ORDER_TYPE_PICKING)) {
				queryString.append(MaterialManagementConstants.GET_MM_PICKING_CHART_DETAILS);
			} else if (moveOrderType
					.equalsIgnoreCase(MaterialManagementConstants.MOVE_ORDER_TYPE_MOVE_ORDER_TRANSFER)) {
				queryString.append(MaterialManagementConstants.GET_MM_MOVE_ORDER_CHART_DETAILS);
			}
			PreparedStatement pstm = con.prepareStatement(queryString.toString());
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			Array subInvFromStrArr = con.createArrayOf("varchar", subInvFromStr);
			Array subInvToStrArr = con.createArrayOf("varchar", subInvToStr);
			pstm.setString(1, startDate);
			pstm.setString(2, endDate);
			pstm.setString(3, projectId);
			pstm.setArray(4, subProjectStrArr);
			pstm.setArray(5, subProjectStrArr);
			pstm.setArray(6, subInvFromStrArr);
			pstm.setArray(7, subInvFromStrArr);
			pstm.setArray(8, subInvToStrArr);
			pstm.setArray(9, subInvToStrArr);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				MMPickingChartDetailsDTO chartDetailsDTO = new MMPickingChartDetailsDTO();
				String color = "", fw;
				chartDetailsDTO.setFiscalWeek(null != rs.getString("fiscal_week") ? rs.getString("fiscal_week") : "");
				fw = chartDetailsDTO.getFiscalWeek();
				color = rs.getString("color_bucket");
				compareDTO = compareMap.get(fw);
				boolean checkFlag = checkFiscalWeek(rs.getString("fiscal_week"), lastUpdateDate);
				if (null == compareDTO) {
					if (checkFlag) {
						if (color.equalsIgnoreCase("Red")) {
							chartDetailsDTO.setRed(null != rs.getString("count") ? rs.getString("count") : "");
						} else if (color.equalsIgnoreCase("Green")) {
							chartDetailsDTO.setGreen(null != rs.getString("count") ? rs.getString("count") : "");
						}
						compareMap.put(fw, chartDetailsDTO);
					} else {
						chartDetailsDTO
								.setFiscalWeek(null != rs.getString("fiscal_week") ? rs.getString("fiscal_week") : "");
						chartDetailsDTO.setRed(null);
						chartDetailsDTO.setGreen(null);
						compareMap.put(fw, chartDetailsDTO);
					}
				} else if (checkFlag) {
					if (color.equalsIgnoreCase("Red")) {
						compareDTO.setRed(null != rs.getString("count") ? rs.getString("count") : "");
					} else if (color.equalsIgnoreCase("Green")) {
						compareDTO.setGreen(null != rs.getString("count") ? rs.getString("count") : "");
					}
				}
			}
			chartDetails.addAll(compareMap.values());
		} catch (SQLException e) {
			log.error("Error in getting Material Management : Picking - Chart Details :: " + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("Error in getting Material Management : Picking - Chart Details :: " + e.getMessage());
				}
			}
		}
		return chartDetails;
	}

	@Override
	public List<MMPickingPopupDetailsDTO> getMMPickingSummaryPopupDetails(String projectId, String subProject,
			String subInvFrom, String subInvTo, String type) {
		Connection con = null;
		List<MMPickingPopupDetailsDTO> pickingPopupDetails = new ArrayList<MMPickingPopupDetailsDTO>();
		try {
			String[] subProjectStr = subProject.split(";");
			String[] subInvFromStr = subInvFrom.split(";");
			String[] subInvToStr = subInvTo.split(";");
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con
					.prepareStatement(MaterialManagementConstants.GET_MM_PICKING_SUMMARY_POPUP_DETAILS);
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			Array subInvFromStrArr = con.createArrayOf("varchar", subInvFromStr);
			Array subInvToStrArr = con.createArrayOf("varchar", subInvToStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setArray(3, subProjectStrArr);
			pstm.setArray(4, subInvFromStrArr);
			pstm.setArray(5, subInvFromStrArr);
			pstm.setArray(6, subInvToStrArr);
			pstm.setArray(7, subInvToStrArr);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				MMPickingPopupDetailsDTO pickingPopupDTO = new MMPickingPopupDetailsDTO();
				pickingPopupDTO.setJob(null != rs.getString("job_number") ? rs.getString("job_number") : "");
				pickingPopupDTO.setItem(null != rs.getString("item") ? rs.getString("item") : "");
				pickingPopupDTO.setMoveOrderNumber(
						null != rs.getString("move_order_number") ? rs.getString("move_order_number") : "");
				pickingPopupDTO.setMoveOrderLine(
						null != rs.getString("move_order_line") ? rs.getString("move_order_line") : "");
				pickingPopupDTO.setMoveOrderType(
						null != rs.getString("move_order_type") ? rs.getString("move_order_type") : "");
				pickingPopupDTO.setMoveOrderDate(
						null != rs.getString("move_order_date") ? rs.getString("move_order_date") : "");
				pickingPopupDTO.setLoadDate(null != rs.getString("load_date") ? rs.getString("load_date") : "");
				pickingPopupDTO.setDropDate(null != rs.getString("drop_date") ? rs.getString("drop_date") : "");
				pickingPopupDTO.setAgingDropDtVsMoIssueDt(null != rs.getString("aging") ? rs.getString("aging") : "");
				pickingPopupDTO.setQuantity(null != rs.getString("quantity") ? rs.getString("quantity") : "");
				pickingPopupDTO.setOrganizationCode(
						null != rs.getString("organization_code") ? rs.getString("organization_code") : "");
				pickingPopupDTO.setSubInventoryFrom(
						null != rs.getString("subinventory_from") ? rs.getString("subinventory_from") : "");
				pickingPopupDTO.setSubInventoryTo(
						null != rs.getString("subinventory_to") ? rs.getString("subinventory_to") : "");
				pickingPopupDTO.setSsoLoader(null != rs.getString("sso_loader") ? rs.getString("sso_loader") : "");
				pickingPopupDTO.setLoaderName(null != rs.getString("loader_name") ? rs.getString("loader_name") : "");
				pickingPopupDTO.setSsoDropper(null != rs.getString("sso_dropper") ? rs.getString("sso_dropper") : "");
				pickingPopupDTO
						.setDropperName(null != rs.getString("dropper_name") ? rs.getString("dropper_name") : "");
				pickingPopupDTO.setLpn(null != rs.getString("lpn") ? rs.getString("lpn") : "");
				pickingPopupDTO.setWip(null != rs.getString("wip_entity_id") ? rs.getString("wip_entity_id") : "");
				pickingPopupDTO
						.setSummaryStatus(null != rs.getString("summary_status") ? rs.getString("summary_status") : "");

				if (type != null && !type.isEmpty() && type.equalsIgnoreCase("TOT_MOVE_ORDER")) {
					pickingPopupDetails.add(pickingPopupDTO);
				} else if (type != null && !type.isEmpty() && !type.equalsIgnoreCase("TOT_MOVE_ORDER")
						&& type.equalsIgnoreCase(pickingPopupDTO.getSummaryStatus())) {
					pickingPopupDetails.add(pickingPopupDTO);
				}

			}
		} catch (SQLException e) {
			log.error("Error in getting Material Management : Picking Tab - " + type + " Popup :: " + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("Error in getting Material Management : Picking Tab - " + type + " Popup :: "
							+ e.getMessage());
				}
			}
		}
		return pickingPopupDetails;
	}

	@Override
	public List<MMPickingPopupDetailsDTO> getMMPickingChartPopupDetails(String projectId, String subProject,
			String subInvFrom, String subInvTo, String type, String startDate, String endDate, String fiscalWeek) {
		Connection con = null;
		List<MMPickingPopupDetailsDTO> pickingPopupDetails = new ArrayList<MMPickingPopupDetailsDTO>();
		try {
			String[] subProjectStr = subProject.split(";");
			String[] subInvFromStr = subInvFrom.split(";");
			String[] subInvToStr = subInvTo.split(";");
			String color = "";
			if (type.equalsIgnoreCase("BAR_CHART_RED")) {
				color = "Red";
			} else if (type.equalsIgnoreCase("BAR_CHART_GREEN")) {
				color = "Green";
			}
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con
					.prepareStatement(MaterialManagementConstants.GET_MM_PICKING_CHART_POPUP_DETAILS);
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			Array subInvFromStrArr = con.createArrayOf("varchar", subInvFromStr);
			Array subInvToStrArr = con.createArrayOf("varchar", subInvToStr);
			pstm.setString(1, startDate);
			pstm.setString(2, endDate);
			pstm.setString(3, projectId);
			pstm.setArray(4, subProjectStrArr);
			pstm.setArray(5, subProjectStrArr);
			pstm.setArray(6, subInvFromStrArr);
			pstm.setArray(7, subInvFromStrArr);
			pstm.setArray(8, subInvToStrArr);
			pstm.setArray(9, subInvToStrArr);
			pstm.setString(10, fiscalWeek);
			pstm.setString(11, color);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				MMPickingPopupDetailsDTO pickingPopupDTO = new MMPickingPopupDetailsDTO();
				pickingPopupDTO.setJob(null != rs.getString("job_number") ? rs.getString("job_number") : "");
				pickingPopupDTO.setItem(null != rs.getString("item") ? rs.getString("item") : "");
				pickingPopupDTO.setMoveOrderNumber(
						null != rs.getString("move_order_number") ? rs.getString("move_order_number") : "");
				pickingPopupDTO.setMoveOrderLine(
						null != rs.getString("move_order_line") ? rs.getString("move_order_line") : "");
				pickingPopupDTO.setMoveOrderType(
						null != rs.getString("move_order_type") ? rs.getString("move_order_type") : "");
				pickingPopupDTO.setMoveOrderDate(
						null != rs.getString("move_order_date") ? rs.getString("move_order_date") : "");
				pickingPopupDTO.setLoadDate(null != rs.getString("load_date") ? rs.getString("load_date") : "");
				pickingPopupDTO.setDropDate(null != rs.getString("drop_date") ? rs.getString("drop_date") : "");
				pickingPopupDTO.setAgingDropDtVsMoIssueDt(null != rs.getString("aging") ? rs.getString("aging") : "");
				pickingPopupDTO.setQuantity(null != rs.getString("quantity") ? rs.getString("quantity") : "");
				pickingPopupDTO.setOrganizationCode(
						null != rs.getString("organization_code") ? rs.getString("organization_code") : "");
				pickingPopupDTO.setSubInventoryFrom(
						null != rs.getString("subinventory_from") ? rs.getString("subinventory_from") : "");
				pickingPopupDTO.setSubInventoryTo(
						null != rs.getString("subinventory_to") ? rs.getString("subinventory_to") : "");
				pickingPopupDTO.setSsoLoader(null != rs.getString("sso_loader") ? rs.getString("sso_loader") : "");
				pickingPopupDTO.setLoaderName(null != rs.getString("loader_name") ? rs.getString("loader_name") : "");
				pickingPopupDTO.setSsoDropper(null != rs.getString("sso_dropper") ? rs.getString("sso_dropper") : "");
				pickingPopupDTO
						.setDropperName(null != rs.getString("dropper_name") ? rs.getString("dropper_name") : "");
				pickingPopupDTO.setLpn(null != rs.getString("lpn") ? rs.getString("lpn") : "");
				pickingPopupDTO.setWip(null != rs.getString("wip_entity_id") ? rs.getString("wip_entity_id") : "");
				pickingPopupDetails.add(pickingPopupDTO);
			}
		} catch (SQLException e) {
			log.error("Error in getting Material Management : Picking Tab - " + type + " Popup :: " + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("Error in getting Material Management : Picking Tab - " + type + " Popup :: "
							+ e.getMessage());
				}
			}
		}
		return pickingPopupDetails;
	}

	@Override
	public List<MMPickingPopupDetailsDTO> getMMPickingDetailsExcel(String projectId, String subProject,
			String subInvFrom, String subInvTo) {
		Connection con = null;
		List<MMPickingPopupDetailsDTO> excelDetails = new ArrayList<MMPickingPopupDetailsDTO>();
		try {
			String[] subProjectStr = subProject.split(";");
			String[] subInvFromStr = subInvFrom.split(";");
			String[] subInvToStr = subInvTo.split(";");
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con
					.prepareStatement(MaterialManagementConstants.GET_MM_PICKING_SUMMARY_POPUP_DETAILS);
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			Array subInvFromStrArr = con.createArrayOf("varchar", subInvFromStr);
			Array subInvToStrArr = con.createArrayOf("varchar", subInvToStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setArray(3, subProjectStrArr);
			pstm.setArray(4, subInvFromStrArr);
			pstm.setArray(5, subInvFromStrArr);
			pstm.setArray(6, subInvToStrArr);
			pstm.setArray(7, subInvToStrArr);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				MMPickingPopupDetailsDTO excelDetailsDTO = new MMPickingPopupDetailsDTO();
				excelDetailsDTO.setJob(null != rs.getString("job_number") ? rs.getString("job_number") : "");
				excelDetailsDTO.setItem(null != rs.getString("item") ? rs.getString("item") : "");
				excelDetailsDTO.setMoveOrderNumber(
						null != rs.getString("move_order_number") ? rs.getString("move_order_number") : "");
				excelDetailsDTO.setMoveOrderLine(
						null != rs.getString("move_order_line") ? rs.getString("move_order_line") : "");
				excelDetailsDTO.setMoveOrderType(
						null != rs.getString("move_order_type") ? rs.getString("move_order_type") : "");
				excelDetailsDTO.setMoveOrderDate(
						null != rs.getString("move_order_date") ? rs.getString("move_order_date") : "");
				excelDetailsDTO.setLoadDate(null != rs.getString("load_date") ? rs.getString("load_date") : "");
				excelDetailsDTO.setDropDate(null != rs.getString("drop_date") ? rs.getString("drop_date") : "");
				excelDetailsDTO.setAgingDropDtVsMoIssueDt(null != rs.getString("aging") ? rs.getString("aging") : "");
				excelDetailsDTO.setQuantity(null != rs.getString("quantity") ? rs.getString("quantity") : "");
				excelDetailsDTO.setOrganizationCode(
						null != rs.getString("organization_code") ? rs.getString("organization_code") : "");
				excelDetailsDTO.setSubInventoryFrom(
						null != rs.getString("subinventory_from") ? rs.getString("subinventory_from") : "");
				excelDetailsDTO.setSubInventoryTo(
						null != rs.getString("subinventory_to") ? rs.getString("subinventory_to") : "");
				excelDetailsDTO.setSsoLoader(null != rs.getString("sso_loader") ? rs.getString("sso_loader") : "");
				excelDetailsDTO.setLoaderName(null != rs.getString("loader_name") ? rs.getString("loader_name") : "");
				excelDetailsDTO.setSsoDropper(null != rs.getString("sso_dropper") ? rs.getString("sso_dropper") : "");
				excelDetailsDTO
						.setDropperName(null != rs.getString("dropper_name") ? rs.getString("dropper_name") : "");
				excelDetailsDTO.setLpn(null != rs.getString("lpn") ? rs.getString("lpn") : "");
				excelDetailsDTO.setWip(null != rs.getString("wip_entity_id") ? rs.getString("wip_entity_id") : "");
				excelDetails.add(excelDetailsDTO);
			}
		} catch (SQLException e) {
			log.error("Error occured while getting data for Material Management : Picking Excel file :: "
					+ e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("Error occured while getting data for Material Management : Picking Excel file :: "
							+ e.getMessage());
				}
			}
		}
		return excelDetails;
	}

	@Override
	public List<MMMoveOrderPopupDetailsDTO> getMMMoveOrderSummaryPopupDetails(String projectId, String subProject,
			String subInvFrom, String subInvTo, String type) {
		Connection con = null;
		List<MMMoveOrderPopupDetailsDTO> moveOrderPopupDetails = new ArrayList<MMMoveOrderPopupDetailsDTO>();
		try {
			String[] subProjectStr = subProject.split(";");
			String[] subInvFromStr = subInvFrom.split(";");
			String[] subInvToStr = subInvTo.split(";");
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con
					.prepareStatement(MaterialManagementConstants.GET_MM_MOVE_ORDER_SUMMARY_POPUP_DETAILS);
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			Array subInvFromStrArr = con.createArrayOf("varchar", subInvFromStr);
			Array subInvToStrArr = con.createArrayOf("varchar", subInvToStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setArray(3, subProjectStrArr);
			pstm.setArray(4, subInvFromStrArr);
			pstm.setArray(5, subInvFromStrArr);
			pstm.setArray(6, subInvToStrArr);
			pstm.setArray(7, subInvToStrArr);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				MMMoveOrderPopupDetailsDTO moveOrderDetailsDTO = new MMMoveOrderPopupDetailsDTO();
				moveOrderDetailsDTO.setJob(null != rs.getString("job_number") ? rs.getString("job_number") : "");
				moveOrderDetailsDTO.setItem(null != rs.getString("item") ? rs.getString("item") : "");
				moveOrderDetailsDTO.setMoveOrderNumber(
						null != rs.getString("move_order_number") ? rs.getString("move_order_number") : "");
				moveOrderDetailsDTO.setMoveOrderLine(
						null != rs.getString("move_order_line") ? rs.getString("move_order_line") : "");
				moveOrderDetailsDTO.setMoveOrderType(
						null != rs.getString("move_order_type") ? rs.getString("move_order_type") : "");
				moveOrderDetailsDTO.setMoveOrderDate(
						null != rs.getString("move_order_date") ? rs.getString("move_order_date") : "");
				moveOrderDetailsDTO.setLoadDate(null != rs.getString("load_date") ? rs.getString("load_date") : "");
				moveOrderDetailsDTO.setDropDate(null != rs.getString("drop_date") ? rs.getString("drop_date") : "");
				moveOrderDetailsDTO.setAgingLoadDtVsMoIssueDt(
						null != rs.getString("aging_load_mo") ? rs.getString("aging_load_mo") : "");
				moveOrderDetailsDTO.setAgingDropDtVsLoadDt(
						null != rs.getString("aging_load_drop") ? rs.getString("aging_load_drop") : "");
				moveOrderDetailsDTO.setQuantity(null != rs.getString("quantity") ? rs.getString("quantity") : "");
				moveOrderDetailsDTO.setOrganizationCode(
						null != rs.getString("organization_code") ? rs.getString("organization_code") : "");
				moveOrderDetailsDTO.setSubInventoryFrom(
						null != rs.getString("subinventory_from") ? rs.getString("subinventory_from") : "");
				moveOrderDetailsDTO.setSubInventoryTo(
						null != rs.getString("subinventory_to") ? rs.getString("subinventory_to") : "");
				moveOrderDetailsDTO.setSsoLoader(null != rs.getString("sso_loader") ? rs.getString("sso_loader") : "");
				moveOrderDetailsDTO
						.setLoaderName(null != rs.getString("loader_name") ? rs.getString("loader_name") : "");
				moveOrderDetailsDTO
						.setSsoDropper(null != rs.getString("sso_dropper") ? rs.getString("sso_dropper") : "");
				moveOrderDetailsDTO
						.setDropperName(null != rs.getString("dropper_name") ? rs.getString("dropper_name") : "");
				moveOrderDetailsDTO.setLpn(null != rs.getString("lpn") ? rs.getString("lpn") : "");
				moveOrderDetailsDTO.setWip(null != rs.getString("wip_entity_id") ? rs.getString("wip_entity_id") : "");
				moveOrderDetailsDTO
						.setSsoNumber(null != rs.getString("ss_order_number") ? rs.getString("ss_order_number") : "");
				moveOrderDetailsDTO
						.setSsoLine(null != rs.getString("ss_order_line") ? rs.getString("ss_order_line") : "");
				moveOrderDetailsDTO
						.setSummaryStatus(null != rs.getString("summary_status") ? rs.getString("summary_status") : "");

				if (type != null && !type.isEmpty() && type.equalsIgnoreCase("TOT_MOVE_ORDER")) {
					moveOrderPopupDetails.add(moveOrderDetailsDTO);
				} else if (type != null && !type.isEmpty() && !type.equalsIgnoreCase("TOT_MOVE_ORDER")
						&& type.equalsIgnoreCase(moveOrderDetailsDTO.getSummaryStatus())) {
					moveOrderPopupDetails.add(moveOrderDetailsDTO);
				}

			}
		} catch (SQLException e) {
			log.error("Error in getting Material Management : Move Order Transfer Tab - " + type + " Popup :: "
					+ e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("Error in getting Material Management : Move Order Transfer Tab - " + type + " Popup :: "
							+ e.getMessage());
				}
			}
		}

		return moveOrderPopupDetails;
	}

	@Override
	public List<MMMoveOrderPopupDetailsDTO> getMMMoveOrderChartPopupDetails(String projectId, String subProject,
			String subInvFrom, String subInvTo, String type, String startDate, String endDate, String fiscalWeek) {
		Connection con = null;
		List<MMMoveOrderPopupDetailsDTO> moveOrderPopupDetails = new ArrayList<MMMoveOrderPopupDetailsDTO>();
		try {
			String[] subProjectStr = subProject.split(";");
			String[] subInvFromStr = subInvFrom.split(";");
			String[] subInvToStr = subInvTo.split(";");
			String color = "";
			if (type.equalsIgnoreCase("BAR_CHART_RED")) {
				color = "Red";
			} else if (type.equalsIgnoreCase("BAR_CHART_GREEN")) {
				color = "Green";
			}
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con
					.prepareStatement(MaterialManagementConstants.GET_MM_MOVE_ORDER_CHART_POPUP_DETAILS);
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			Array subInvFromStrArr = con.createArrayOf("varchar", subInvFromStr);
			Array subInvToStrArr = con.createArrayOf("varchar", subInvToStr);
			pstm.setString(1, startDate);
			pstm.setString(2, endDate);
			pstm.setString(3, projectId);
			pstm.setArray(4, subProjectStrArr);
			pstm.setArray(5, subProjectStrArr);
			pstm.setArray(6, subInvFromStrArr);
			pstm.setArray(7, subInvFromStrArr);
			pstm.setArray(8, subInvToStrArr);
			pstm.setArray(9, subInvToStrArr);
			pstm.setString(10, fiscalWeek);
			pstm.setString(11, color);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				MMMoveOrderPopupDetailsDTO moveOrderDetailsDTO = new MMMoveOrderPopupDetailsDTO();
				moveOrderDetailsDTO.setJob(null != rs.getString("job_number") ? rs.getString("job_number") : "");
				moveOrderDetailsDTO.setItem(null != rs.getString("item") ? rs.getString("item") : "");
				moveOrderDetailsDTO.setMoveOrderNumber(
						null != rs.getString("move_order_number") ? rs.getString("move_order_number") : "");
				moveOrderDetailsDTO.setMoveOrderLine(
						null != rs.getString("move_order_line") ? rs.getString("move_order_line") : "");
				moveOrderDetailsDTO.setMoveOrderType(
						null != rs.getString("move_order_type") ? rs.getString("move_order_type") : "");
				moveOrderDetailsDTO.setMoveOrderDate(
						null != rs.getString("move_order_date") ? rs.getString("move_order_date") : "");
				moveOrderDetailsDTO.setLoadDate(null != rs.getString("load_date") ? rs.getString("load_date") : "");
				moveOrderDetailsDTO.setDropDate(null != rs.getString("drop_date") ? rs.getString("drop_date") : "");
				moveOrderDetailsDTO.setAgingLoadDtVsMoIssueDt(
						null != rs.getString("aging_load_mo") ? rs.getString("aging_load_mo") : "");
				moveOrderDetailsDTO.setAgingDropDtVsLoadDt(
						null != rs.getString("aging_load_drop") ? rs.getString("aging_load_drop") : "");
				moveOrderDetailsDTO.setQuantity(null != rs.getString("quantity") ? rs.getString("quantity") : "");
				moveOrderDetailsDTO.setOrganizationCode(
						null != rs.getString("organization_code") ? rs.getString("organization_code") : "");
				moveOrderDetailsDTO.setSubInventoryFrom(
						null != rs.getString("subinventory_from") ? rs.getString("subinventory_from") : "");
				moveOrderDetailsDTO.setSubInventoryTo(
						null != rs.getString("subinventory_to") ? rs.getString("subinventory_to") : "");
				moveOrderDetailsDTO.setSsoLoader(null != rs.getString("sso_loader") ? rs.getString("sso_loader") : "");
				moveOrderDetailsDTO
						.setLoaderName(null != rs.getString("loader_name") ? rs.getString("loader_name") : "");
				moveOrderDetailsDTO
						.setSsoDropper(null != rs.getString("sso_dropper") ? rs.getString("sso_dropper") : "");
				moveOrderDetailsDTO
						.setDropperName(null != rs.getString("dropper_name") ? rs.getString("dropper_name") : "");
				moveOrderDetailsDTO.setLpn(null != rs.getString("lpn") ? rs.getString("lpn") : "");
				moveOrderDetailsDTO.setWip(null != rs.getString("wip_entity_id") ? rs.getString("wip_entity_id") : "");
				moveOrderDetailsDTO
						.setSsoNumber(null != rs.getString("ss_order_number") ? rs.getString("ss_order_number") : "");
				moveOrderDetailsDTO
						.setSsoLine(null != rs.getString("ss_order_line") ? rs.getString("ss_order_line") : "");
				moveOrderPopupDetails.add(moveOrderDetailsDTO);
			}
		} catch (SQLException e) {
			log.error("Error in getting Material Management : Move Order Transfer Tab - " + type + " Popup :: "
					+ e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("Error in getting Material Management : Move Order Transfer Tab - " + type + " Popup :: "
							+ e.getMessage());
				}
			}
		}

		return moveOrderPopupDetails;
	}

	@Override
	public List<MMMoveOrderPopupDetailsDTO> getMMMoveOrderDetailsExcel(String projectId, String subProject,
			String subInvFrom, String subInvTo) {
		Connection con = null;
		List<MMMoveOrderPopupDetailsDTO> excelDetails = new ArrayList<MMMoveOrderPopupDetailsDTO>();
		try {
			String[] subProjectStr = subProject.split(";");
			String[] subInvFromStr = subInvFrom.split(";");
			String[] subInvToStr = subInvTo.split(";");
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con
					.prepareStatement(MaterialManagementConstants.GET_MM_MOVE_ORDER_SUMMARY_POPUP_DETAILS);
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			Array subInvFromStrArr = con.createArrayOf("varchar", subInvFromStr);
			Array subInvToStrArr = con.createArrayOf("varchar", subInvToStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setArray(3, subProjectStrArr);
			pstm.setArray(4, subInvFromStrArr);
			pstm.setArray(5, subInvFromStrArr);
			pstm.setArray(6, subInvToStrArr);
			pstm.setArray(7, subInvToStrArr);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				MMMoveOrderPopupDetailsDTO excelDetailsDTO = new MMMoveOrderPopupDetailsDTO();
				excelDetailsDTO.setJob(null != rs.getString("job_number") ? rs.getString("job_number") : "");
				excelDetailsDTO.setItem(null != rs.getString("item") ? rs.getString("item") : "");
				excelDetailsDTO.setMoveOrderNumber(
						null != rs.getString("move_order_number") ? rs.getString("move_order_number") : "");
				excelDetailsDTO.setMoveOrderLine(
						null != rs.getString("move_order_line") ? rs.getString("move_order_line") : "");
				excelDetailsDTO.setMoveOrderType(
						null != rs.getString("move_order_type") ? rs.getString("move_order_type") : "");
				excelDetailsDTO.setMoveOrderDate(
						null != rs.getString("move_order_date") ? rs.getString("move_order_date") : "");
				excelDetailsDTO.setLoadDate(null != rs.getString("load_date") ? rs.getString("load_date") : "");
				excelDetailsDTO.setDropDate(null != rs.getString("drop_date") ? rs.getString("drop_date") : "");
				excelDetailsDTO.setAgingLoadDtVsMoIssueDt(
						null != rs.getString("aging_load_mo") ? rs.getString("aging_load_mo") : "");
				excelDetailsDTO.setAgingDropDtVsLoadDt(
						null != rs.getString("aging_load_drop") ? rs.getString("aging_load_drop") : "");
				excelDetailsDTO.setQuantity(null != rs.getString("quantity") ? rs.getString("quantity") : "");
				excelDetailsDTO.setOrganizationCode(
						null != rs.getString("organization_code") ? rs.getString("organization_code") : "");
				excelDetailsDTO.setSubInventoryFrom(
						null != rs.getString("subinventory_from") ? rs.getString("subinventory_from") : "");
				excelDetailsDTO.setSubInventoryTo(
						null != rs.getString("subinventory_to") ? rs.getString("subinventory_to") : "");
				excelDetailsDTO.setSsoLoader(null != rs.getString("sso_loader") ? rs.getString("sso_loader") : "");
				excelDetailsDTO.setLoaderName(null != rs.getString("loader_name") ? rs.getString("loader_name") : "");
				excelDetailsDTO.setSsoDropper(null != rs.getString("sso_dropper") ? rs.getString("sso_dropper") : "");
				excelDetailsDTO
						.setDropperName(null != rs.getString("dropper_name") ? rs.getString("dropper_name") : "");
				excelDetailsDTO.setLpn(null != rs.getString("lpn") ? rs.getString("lpn") : "");
				excelDetailsDTO.setWip(null != rs.getString("wip_entity_id") ? rs.getString("wip_entity_id") : "");
				excelDetailsDTO
						.setSsoNumber(null != rs.getString("ss_order_number") ? rs.getString("ss_order_number") : "");
				excelDetailsDTO.setSsoLine(null != rs.getString("ss_order_line") ? rs.getString("ss_order_line") : "");
				excelDetails.add(excelDetailsDTO);
			}
		} catch (SQLException e) {
			log.error("Error occured while getting data for Material Management : Move Order Transfer Excel file :: "
					+ e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error(
							"Error occured while getting data for Material Management : Move Order Transfer Excel file :: "
									+ e.getMessage());
				}
			}
		}
		return excelDetails;
	}

	@Override
	public String getWfAnoLastUpdatedDate(String projectId) {
		String date = "";
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(MaterialManagementConstants.GET_MM_WF_ANOMALY_LAST_REFRESHED_DATE)) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				date = rs.getString(1);
			}
			return date;
		} catch (SQLException e) {
			log.error("Error in getting Material Management : WF Anomaly - Last Updated Date :: " + e.getMessage());
		}
		return date;
	}

	@Override
	public List<DropDownDTO> getWFAnoOwnershipFilter(String projectId) {
		List<DropDownDTO> ownershipList = new ArrayList<DropDownDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(MaterialManagementConstants.GET_MM_WF_ANOMALY_OWNERSHIP_FILTER)) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				if (null != rs.getString(1)) {
					DropDownDTO dto = new DropDownDTO();
					dto.setKey(null != rs.getString(1) ? rs.getString(1) : "");
					dto.setVal(null != rs.getString(1) ? rs.getString(1) : "");
					ownershipList.add(dto);
				}
			}
		} catch (SQLException e) {
			log.error("Error in getting Material Management : WF Anomaly - Ownership Filter :: " + e.getMessage());
		}
		return ownershipList;
	}

	@Override
	public MMWfAnomalySummaryDetailsDTO getWFAnoCountDetails(String projectId, String ownership) {
		MMWfAnomalySummaryDetailsDTO summaryDTO = new MMWfAnomalySummaryDetailsDTO();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(MaterialManagementConstants.GET_MM_WF_ANOMALY_SUMMARY_CNT);) {
			String[] ownershipStr = ownership.split(";");
			Array ownershipStrArr = con.createArrayOf("varchar", ownershipStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, ownershipStrArr);
			pstm.setArray(3, ownershipStrArr);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				summaryDTO.setTotal(null != rs.getString("total") ? rs.getString("total") : "");
				summaryDTO.setOpen(null != rs.getString("wf_open") ? rs.getString("wf_open") : "");
				summaryDTO.setClosed(null != rs.getString("wf_closed") ? rs.getString("wf_closed") : "");
			}
		} catch (SQLException e) {
			log.error("Error in getting Material Management : WF Anomaly - Summary Counts :: " + e.getMessage());
		}
		return summaryDTO;
	}

	@Override
	public Map<String, Object> getWFAnoCycleChartDetails(String projectId, String ownership) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("days_less_than_5", "0");
		resultMap.put("days_between_5_and_22", "0");
		resultMap.put("days_more_than_22", "0");
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(MaterialManagementConstants.GET_MM_WF_ANOMALY_CYCLE_CHART);) {
			String[] ownershipStr = ownership.split(";");
			Array ownershipStrArr = con.createArrayOf("varchar", ownershipStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, ownershipStrArr);
			pstm.setArray(3, ownershipStrArr);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				String closedGreen = "", closedYellow = "", closedRed = "";
				closedGreen = rs.getString("closed_green");
				closedYellow = rs.getString("closed_yellow");
				closedRed = rs.getString("closed_red");
				if (null != closedGreen && !"".equalsIgnoreCase(closedGreen)
						&& resultMap.containsKey("days_less_than_5")) {
					resultMap.put("days_less_than_5", closedGreen);
				}
				if (null != closedYellow && !"".equalsIgnoreCase(closedYellow)
						&& resultMap.containsKey("days_between_5_and_22")) {
					resultMap.put("days_between_5_and_22", closedYellow);
				}
				if (null != closedRed && !"".equalsIgnoreCase(closedRed)
						&& resultMap.containsKey("days_more_than_22")) {
					resultMap.put("days_more_than_22", closedRed);
				}
			}
		} catch (SQLException e) {
			log.error("Error in getting Material Management : WF Anomaly - Cycle Chart :: " + e.getMessage());
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> getWFAnoAgingChartDetails(String projectId, String ownership) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("days_less_than_5", "0");
		resultMap.put("days_between_5_and_22", "0");
		resultMap.put("days_more_than_22", "0");
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(MaterialManagementConstants.GET_MM_WF_ANOMALY_AGING_CHART);) {
			String[] ownershipStr = ownership.split(";");
			Array ownershipStrArr = con.createArrayOf("varchar", ownershipStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, ownershipStrArr);
			pstm.setArray(3, ownershipStrArr);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				String openGreen = "", openYellow = "", openRed = "";
				openGreen = rs.getString("open_green");
				openYellow = rs.getString("open_yellow");
				openRed = rs.getString("open_red");
				if (null != openGreen && !"".equalsIgnoreCase(openGreen) && resultMap.containsKey("days_less_than_5")) {
					resultMap.put("days_less_than_5", openGreen);
				}
				if (null != openYellow && !"".equalsIgnoreCase(openYellow)
						&& resultMap.containsKey("days_between_5_and_22")) {
					resultMap.put("days_between_5_and_22", openYellow);
				}
				if (null != openRed && !"".equalsIgnoreCase(openRed) && resultMap.containsKey("days_more_than_22")) {
					resultMap.put("days_more_than_22", openRed);
				}
			}
		} catch (SQLException e) {
			log.error("Error in getting Material Management : WF Anomaly - Aging Chart :: " + e.getMessage());
		}
		return resultMap;
	}

	@Override
	public List<MMWfAnomalyPopupDetailsDTO> getWFAnoPopupDetails(String projectId, String ownership, String chartType,
			String status) {
		List<MMWfAnomalyPopupDetailsDTO> wfAnoPopupDetails = new ArrayList<MMWfAnomalyPopupDetailsDTO>();
		String queryString = MaterialManagementDAOHelper.getMMWFAnomolyDetailsQuery(chartType, status);
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(queryString);) {
			String[] ownershipStr = ownership.split(";");
			Array ownershipStrArr = con.createArrayOf("varchar", ownershipStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, ownershipStrArr);
			pstm.setArray(3, ownershipStrArr);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				MMWfAnomalyPopupDetailsDTO wfAnoPopupDTO = new MMWfAnomalyPopupDetailsDTO();
				wfAnoPopupDTO.setWfAnomalyNumber(
						null != rs.getString("wf_anomaly_number") ? rs.getString("wf_anomaly_number") : "");
				wfAnoPopupDTO.setStatus(null != rs.getString("status") ? rs.getString("status") : "");
				wfAnoPopupDTO.setCategory(null != rs.getString("category") ? rs.getString("category") : "");
				wfAnoPopupDTO.setAnomalyCode(null != rs.getString("anomaly_code") ? rs.getString("anomaly_code") : "");
				wfAnoPopupDTO
						.setWfImputation(null != rs.getString("wf_imputation") ? rs.getString("wf_imputation") : "");
				wfAnoPopupDTO
						.setCreationDate(null != rs.getString("creation_date") ? rs.getString("creation_date") : "");
				wfAnoPopupDTO.setClosureDate(null != rs.getString("closure_date") ? rs.getString("closure_date") : "");
				wfAnoPopupDTO.setOwnership(null != rs.getString("ownership") ? rs.getString("ownership") : "");
				wfAnoPopupDTO.setPlant(null != rs.getString("plant") ? rs.getString("plant") : "");
				wfAnoPopupDTO.setInvOrg(null != rs.getString("inv_org") ? rs.getString("inv_org") : "");
				wfAnoPopupDTO.setDepartment(null != rs.getString("department") ? rs.getString("department") : "");
				wfAnoPopupDTO.setProcessStep(null != rs.getString("process_step") ? rs.getString("process_step") : "");
				wfAnoPopupDTO.setMaterialOrigin(
						null != rs.getString("material_origin") ? rs.getString("material_origin") : "");
				wfAnoPopupDTO.setWarehouseOrigin(
						null != rs.getString("warehouse_origin") ? rs.getString("warehouse_origin") : "");
				wfAnoPopupDTO.setPo(null != rs.getString("po") ? rs.getString("po") : "");
				wfAnoPopupDTO.setPoLine(null != rs.getString("po_line") ? rs.getString("po_line") : "");
				wfAnoPopupDTO.setPoReleaseShipping(
						null != rs.getString("po_release_shipping") ? rs.getString("po_release_shipping") : "");
				wfAnoPopupDTO
						.setSupplierCode(null != rs.getString("supplier_code") ? rs.getString("supplier_code") : "");
				wfAnoPopupDTO
						.setSupplierName(null != rs.getString("supplier_name") ? rs.getString("supplier_name") : "");
				wfAnoPopupDTO.setItem(null != rs.getString("item") ? rs.getString("item") : "");
				wfAnoPopupDTO.setMaterialLocation(
						null != rs.getString("material_location") ? rs.getString("material_location") : "");
				wfAnoPopupDTO.setSalesOrder(null != rs.getString("sales_order") ? rs.getString("sales_order") : "");
				wfAnoPopupDTO.setMoveOrder(null != rs.getString("move_order") ? rs.getString("move_order") : "");
				wfAnoPopupDTO.setOwnerAnomalyType(
						null != rs.getString("owner_anomaly_type") ? rs.getString("owner_anomaly_type") : "");
				wfAnoPopupDTO.setJob(null != rs.getString("job") ? rs.getString("job") : "");
				wfAnoPopupDetails.add(wfAnoPopupDTO);
			}
		} catch (SQLException e) {
			log.error("Error in getting Material Management : WF Anomaly  - " + chartType + " Popup :: "
					+ e.getMessage());
		}
		return wfAnoPopupDetails;
	}

	@Override
	public List<MMWfAnomalyPopupDetailsDTO> getWfAnoDetailsExcel(String projectId, String ownership, String chartType,
			String status) {
		List<MMWfAnomalyPopupDetailsDTO> excelDetails = new ArrayList<MMWfAnomalyPopupDetailsDTO>();
		String queryString = MaterialManagementDAOHelper.getMMWFAnomolyDetailsQuery(chartType, status);
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(queryString);) {
			String[] ownershipStr = ownership.split(";");
			Array ownershipStrArr = con.createArrayOf("varchar", ownershipStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, ownershipStrArr);
			pstm.setArray(3, ownershipStrArr);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				MMWfAnomalyPopupDetailsDTO excelDetailsDTO = new MMWfAnomalyPopupDetailsDTO();
				excelDetailsDTO.setWfAnomalyNumber(
						null != rs.getString("wf_anomaly_number") ? rs.getString("wf_anomaly_number") : "");
				excelDetailsDTO.setStatus(null != rs.getString("status") ? rs.getString("status") : "");
				excelDetailsDTO.setCategory(null != rs.getString("category") ? rs.getString("category") : "");
				excelDetailsDTO
						.setAnomalyCode(null != rs.getString("anomaly_code") ? rs.getString("anomaly_code") : "");
				excelDetailsDTO
						.setWfImputation(null != rs.getString("wf_imputation") ? rs.getString("wf_imputation") : "");
				excelDetailsDTO
						.setCreationDate(null != rs.getString("creation_date") ? rs.getString("creation_date") : "");
				excelDetailsDTO
						.setClosureDate(null != rs.getString("closure_date") ? rs.getString("closure_date") : "");
				excelDetailsDTO.setOwnership(null != rs.getString("ownership") ? rs.getString("ownership") : "");
				excelDetailsDTO.setPlant(null != rs.getString("plant") ? rs.getString("plant") : "");
				excelDetailsDTO.setInvOrg(null != rs.getString("inv_org") ? rs.getString("inv_org") : "");
				excelDetailsDTO.setDepartment(null != rs.getString("department") ? rs.getString("department") : "");
				excelDetailsDTO
						.setProcessStep(null != rs.getString("process_step") ? rs.getString("process_step") : "");
				excelDetailsDTO.setMaterialOrigin(
						null != rs.getString("material_origin") ? rs.getString("material_origin") : "");
				excelDetailsDTO.setWarehouseOrigin(
						null != rs.getString("warehouse_origin") ? rs.getString("warehouse_origin") : "");
				excelDetailsDTO.setPo(null != rs.getString("po") ? rs.getString("po") : "");
				excelDetailsDTO.setPoLine(null != rs.getString("po_line") ? rs.getString("po_line") : "");
				excelDetailsDTO.setPoReleaseShipping(
						null != rs.getString("po_release_shipping") ? rs.getString("po_release_shipping") : "");
				excelDetailsDTO
						.setSupplierCode(null != rs.getString("supplier_code") ? rs.getString("supplier_code") : "");
				excelDetailsDTO
						.setSupplierName(null != rs.getString("supplier_name") ? rs.getString("supplier_name") : "");
				excelDetailsDTO.setItem(null != rs.getString("item") ? rs.getString("item") : "");
				excelDetailsDTO.setMaterialLocation(
						null != rs.getString("material_location") ? rs.getString("material_location") : "");
				excelDetailsDTO.setSalesOrder(null != rs.getString("sales_order") ? rs.getString("sales_order") : "");
				excelDetailsDTO.setMoveOrder(null != rs.getString("move_order") ? rs.getString("move_order") : "");
				excelDetailsDTO.setOwnerAnomalyType(
						null != rs.getString("owner_anomaly_type") ? rs.getString("owner_anomaly_type") : "");
				excelDetailsDTO.setJob(null != rs.getString("job") ? rs.getString("job") : "");
				excelDetails.add(excelDetailsDTO);
			}
		} catch (SQLException e) {
			log.error("Error in getting Material Management : WF Anomaly Excel file :: " + e.getMessage());
		}
		return excelDetails;
	}

	@Override
	public List<DropDownDTO> getPackingFDSubProjectFilter(String projectId) {
		return jdbcTemplate.query(MaterialManagementConstants.GET_MM_PACKING_SUB_PROJECT_FILTER,
				new Object[] { projectId }, new ResultSetExtractor<List<DropDownDTO>>() {
					@Override
					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
						List<DropDownDTO> subProjectFilterList = new ArrayList<DropDownDTO>();
						try {
							String subProject = null;
							while (rs.next()) {
								DropDownDTO dropDownDTO = new DropDownDTO();
								subProject = rs.getString("sub_project");
								if (null != subProject) {
									dropDownDTO.setKey(subProject);
									dropDownDTO.setVal(subProject);
									subProjectFilterList.add(dropDownDTO);
								}
							}
						} catch (Exception e) {
							log.error("Error in getting Material Management : Packing - Sub Project Filter :: "
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return subProjectFilterList;
					}
				});
	}

	@Override
	public List<DropDownDTO> getPackingFDSubInvFromFilter(String projectId) {
		return jdbcTemplate.query(MaterialManagementConstants.GET_MM_PACKING_SUB_INVENTORY_FROM_FILTER,
				new Object[] { projectId }, new ResultSetExtractor<List<DropDownDTO>>() {
					@Override
					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
						List<DropDownDTO> subInvFromFilterList = new ArrayList<DropDownDTO>();
						try {
							String subInvFrom = null;
							while (rs.next()) {
								DropDownDTO dropDownDTO = new DropDownDTO();
								subInvFrom = rs.getString("subInvFrom");
								if (null != subInvFrom) {
									dropDownDTO.setKey(subInvFrom);
									dropDownDTO.setVal(subInvFrom);
									subInvFromFilterList.add(dropDownDTO);
								}
							}
						} catch (Exception e) {
							log.error("Error in getting Material Management : Packing - Sub Inventory From Filter :: "
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return subInvFromFilterList;
					}
				});

	}

	@Override
	public List<DropDownDTO> getPackingFDSubInvToFilter(String projectId) {
		return jdbcTemplate.query(MaterialManagementConstants.GET_MM_PACKING_SUB_INVENTORY_TO_FILTER,
				new Object[] { projectId }, new ResultSetExtractor<List<DropDownDTO>>() {
					@Override
					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
						List<DropDownDTO> subInvToFilterList = new ArrayList<DropDownDTO>();
						try {
							String subInvTo = null;
							while (rs.next()) {
								DropDownDTO dropDownDTO = new DropDownDTO();
								subInvTo = rs.getString("subInvTo");
								if (null != subInvTo) {
									dropDownDTO.setKey(subInvTo);
									dropDownDTO.setVal(subInvTo);
									subInvToFilterList.add(dropDownDTO);
								}
							}
						} catch (Exception e) {
							log.error("Error in getting Material Management : Packing - Sub Inventory To Filter :: "
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return subInvToFilterList;
					}
				});

	}

	@Override
	public List<DropDownDTO> getPackingFDOwnershipFilter(String projectId) {
		return jdbcTemplate.query(MaterialManagementConstants.GET_MM_PACKING_OWNERSHIP_FILTER,
				new Object[] { projectId }, new ResultSetExtractor<List<DropDownDTO>>() {
					@Override
					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
						List<DropDownDTO> ownershipList = new ArrayList<DropDownDTO>();
						try {
							String ownership = null;
							while (rs.next()) {
								DropDownDTO dropDownDTO = new DropDownDTO();
								ownership = rs.getString("ownership");
								if (null != ownership && !ownership.equalsIgnoreCase("")) {
									dropDownDTO.setKey(ownership);
									dropDownDTO.setVal(ownership);
									ownershipList.add(dropDownDTO);
								}
							}
						} catch (Exception e) {
							log.error("Error in getting Material Management : Packing - Ownership Filter :: "
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return ownershipList;
					}
				});
	}

	@Override
	public MMPackingFDSummaryDetailsDTO getPackingFDCountDetails(String projectId, String subProject, String subInvFrom,
			String subInvTo, String ownership) {
		Connection con = null;
		MMPackingFDSummaryDetailsDTO summaryDTO = new MMPackingFDSummaryDetailsDTO();
		try {
			String[] subProjectStr = subProject.split(";");
			String[] subInvFromStr = subInvFrom.split(";");
			String[] subInvToStr = subInvTo.split(";");
			String[] ownershipStr = ownership.split(";");
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con.prepareStatement(MaterialManagementConstants.GET_MM_PACKING_SUMMARY_CNT);
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			Array subInvFromStrArr = con.createArrayOf("varchar", subInvFromStr);
			Array subInvToStrArr = con.createArrayOf("varchar", subInvToStr);
			Array ownershipStrArr = con.createArrayOf("varchar", ownershipStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setArray(3, subProjectStrArr);
			pstm.setArray(4, subInvFromStrArr);
			pstm.setArray(5, subInvFromStrArr);
			pstm.setArray(6, subInvToStrArr);
			pstm.setArray(7, subInvToStrArr);
			pstm.setArray(8, ownershipStrArr);
			pstm.setArray(9, ownershipStrArr);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				summaryDTO.setTotalMoveOrder(null != rs.getString("total") ? rs.getString("total") : "");
				summaryDTO.setIssued(null != rs.getString("issued") ? rs.getString("issued") : "");
				summaryDTO.setLoaded(null != rs.getString("loaded") ? rs.getString("loaded") : "");
				summaryDTO.setDropped(null != rs.getString("dropped") ? rs.getString("dropped") : "");
				summaryDTO.setPacked(null != rs.getString("packed") ? rs.getString("packed") : "");
			}
		} catch (SQLException e) {
			log.error("Error in getting Material Management : Packing - Summary Counts :: " + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("Error in getting Material Management : Packing - Summary Counts :: " + e.getMessage());
				}
			}
		}
		return summaryDTO;
	}

	@Override
	public String getPackingFDLastUpdatedDate(String projectId) {
		return jdbcTemplate.query(MaterialManagementConstants.GET_MM_PACKING_LAST_REFRESHED_DATE,
				new Object[] { projectId }, new ResultSetExtractor<String>() {
					public String extractData(ResultSet rs) throws SQLException {
						String date = "";
						while (rs.next()) {
							date = rs.getString(1);
						}
						return date;
					}
				});
	}

	@Override
	public List<MMPackingFDChartDetailsDTO> getMMPackingFDChartDetails(String projectId, String subProject,
			String subInvFrom, String subInvTo, String ownership, String startDate, String endDate) {
		Connection con = null;
		List<MMPackingFDChartDetailsDTO> list = new ArrayList<MMPackingFDChartDetailsDTO>();
		try {
			String[] subProjectStr = subProject.split(";");
			String[] subInvFromStr = subInvFrom.split(";");
			String[] subInvToStr = subInvTo.split(";");
			String[] ownershipStr = ownership.split(";");
			con = jdbcTemplate.getDataSource().getConnection();
			String lastUpdateDate = getPackingFDLastUpdatedDate(projectId);
			PreparedStatement pstm = con.prepareStatement(MaterialManagementConstants.GET_MM_PACKING_CHART_DETAILS);
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			Array subInvFromStrArr = con.createArrayOf("varchar", subInvFromStr);
			Array subInvToStrArr = con.createArrayOf("varchar", subInvToStr);
			Array ownershipStrArr = con.createArrayOf("varchar", ownershipStr);
			pstm.setString(1, startDate);
			pstm.setString(2, endDate);
			pstm.setString(3, projectId);
			pstm.setArray(4, subProjectStrArr);
			pstm.setArray(5, subProjectStrArr);
			pstm.setArray(6, subInvFromStrArr);
			pstm.setArray(7, subInvFromStrArr);
			pstm.setArray(8, subInvToStrArr);
			pstm.setArray(9, subInvToStrArr);
			pstm.setArray(10, ownershipStrArr);
			pstm.setArray(11, ownershipStrArr);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				NumberFormat df = new DecimalFormat("#.##");
				MMPackingFDChartDetailsDTO chartDTO = new MMPackingFDChartDetailsDTO();
				chartDTO.setFiscalWeek(null != rs.getString("fiscal_week") ? rs.getString("fiscal_week") : "");
				chartDTO.setGreenSection(null != rs.getString("green_cnt") ? rs.getString("green_cnt") : "");
				chartDTO.setRedSection(null != rs.getString("red_cnt") ? rs.getString("red_cnt") : "");
				chartDTO.setP50(null != rs.getString("p50") ? String.valueOf(df.format(rs.getFloat("p50"))) : "");
				chartDTO.setP75(null != rs.getString("p75") ? String.valueOf(df.format(rs.getFloat("p75"))) : "");
				chartDTO.setP95(null != rs.getString("p95") ? String.valueOf(df.format(rs.getFloat("p95"))) : "");
				boolean checkFlag = checkFiscalWeek(rs.getString("fiscal_week"), lastUpdateDate);
				if (checkFlag) {
					list.add(chartDTO);
				} else {
					chartDTO.setFiscalWeek(null != rs.getString("fiscal_week") ? rs.getString("fiscal_week") : "");
					chartDTO.setGreenSection(null);
					chartDTO.setRedSection(null);
					chartDTO.setP50(null);
					chartDTO.setP75(null);
					chartDTO.setP95(null);
					list.add(chartDTO);
				}
			}
		} catch (SQLException e) {
			log.error("Error in getting Material Management : Packing - Summary Counts :: " + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("Error in getting Material Management : Packing - Summary Counts :: " + e.getMessage());
				}
			}
		}
		return list;
	}

	@Override
	public List<MMPackingFDPopupDetailsDTO> getMMPackingFDSummaryPopupDetails(String projectId, String subProject,
			String subInvFrom, String subInvTo, String ownership, String chartType, String status) {
		Connection con = null;
		List<MMPackingFDPopupDetailsDTO> packingPopupDetails = new ArrayList<MMPackingFDPopupDetailsDTO>();
		StringBuilder queryString = new StringBuilder();
		try {
			String[] subProjectStr = subProject.split(";");
			String[] subInvFromStr = subInvFrom.split(";");
			String[] subInvToStr = subInvTo.split(";");
			String[] ownershipStr = ownership.split(";");
			con = jdbcTemplate.getDataSource().getConnection();
			queryString.append(MaterialManagementConstants.GET_MM_PACKING_SUMMARY_POPUP_DETAILS);
			if (status != null && !status.isEmpty() && !status.equalsIgnoreCase("TOTAL_MOVE_ORDER")) {
				queryString.append(" where pfd_status = ?");
			}
			PreparedStatement pstm = con.prepareStatement(queryString.toString());
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			Array subInvFromStrArr = con.createArrayOf("varchar", subInvFromStr);
			Array subInvToStrArr = con.createArrayOf("varchar", subInvToStr);
			Array ownershipStrArr = con.createArrayOf("varchar", ownershipStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setArray(3, subProjectStrArr);
			pstm.setArray(4, subInvFromStrArr);
			pstm.setArray(5, subInvFromStrArr);
			pstm.setArray(6, subInvToStrArr);
			pstm.setArray(7, subInvToStrArr);
			pstm.setArray(8, ownershipStrArr);
			pstm.setArray(9, ownershipStrArr);
			if (status != null && !status.isEmpty() && !status.equalsIgnoreCase("TOTAL_MOVE_ORDER")) {
				pstm.setString(10, status);
			}
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				MMPackingFDPopupDetailsDTO packingPopupDTO = new MMPackingFDPopupDetailsDTO();
				packingPopupDTO.setCostingProject(
						null != rs.getString("costing_project") ? rs.getString("costing_project") : "");
				packingPopupDTO.setOm(null != rs.getString("om") ? rs.getString("om") : "");
				packingPopupDTO.setPei(null != rs.getString("pei") ? rs.getString("pei") : "");
				packingPopupDTO.setPeiDescription(
						null != rs.getString("pei_description") ? rs.getString("pei_description") : "");
				packingPopupDTO
						.setOrderedItem(null != rs.getString("ordered_item") ? rs.getString("ordered_item") : "");
				packingPopupDTO.setDescription(null != rs.getString("description") ? rs.getString("description") : "");
				packingPopupDTO.setQty(null != rs.getString("quantity") ? rs.getString("quantity") : "");
				packingPopupDTO.setFatherCode(null != rs.getString("father_code") ? rs.getString("father_code") : "");
				packingPopupDTO.setStatus(null != rs.getString("status") ? rs.getString("status") : "");
				packingPopupDTO.setOwner(null != rs.getString("owner") ? rs.getString("owner") : "");
				packingPopupDTO.setNotes(null != rs.getString("notes") ? rs.getString("notes") : "");
				packingPopupDTO
						.setSupplyStatus(null != rs.getString("supply_status") ? rs.getString("supply_status") : "");
				packingPopupDTO.setAscpOrderNumber(
						null != rs.getString("ascp_order_number") ? rs.getString("ascp_order_number") : "");
				packingPopupDTO.setVendor(null != rs.getString("vendor") ? rs.getString("vendor") : "");
				packingPopupDTO
						.setPromisedDate(null != rs.getString("promised_date") ? rs.getString("promised_date") : "");
				packingPopupDTO.setPoChild(null != rs.getString("po_child") ? rs.getString("po_child") : "");
				packingPopupDTO.setSpa(null != rs.getString("spa") ? rs.getString("spa") : "");
				packingPopupDTO.setBuyer(null != rs.getString("buyer") ? rs.getString("buyer") : "");
				packingPopupDTO.setFulfillmentResponsibility(
						null != rs.getString("fulfillment_responsibility") ? rs.getString("fulfillment_responsibility")
								: "");
				packingPopupDTO.setInvOrg(null != rs.getString("inv_org") ? rs.getString("inv_org") : "");
				packingPopupDTO.setPlant(null != rs.getString("plant") ? rs.getString("plant") : "");
				packingPopupDTO.setPoFather(null != rs.getString("po_father") ? rs.getString("po_father") : "");
				packingPopupDTO.setContract(null != rs.getString("project_id") ? rs.getString("project_id") : "");
				packingPopupDTO.setPoHeaderNumber(
						null != rs.getString("po_header_number") ? rs.getString("po_header_number") : "");
				packingPopupDTO
						.setPoLineNumber(null != rs.getString("po_line_number") ? rs.getString("po_line_number") : "");
				packingPopupDTO.setMaxArrivalDate(
						null != rs.getString("max_arrival_date") ? rs.getString("max_arrival_date") : "");
				packingPopupDTO.setMoveOrder(null != rs.getString("move_order") ? rs.getString("move_order") : "");
				packingPopupDTO.setMoveOrderDate(
						null != rs.getString("move_order_date") ? rs.getString("move_order_date") : "");
				packingPopupDTO
						.setOutermostBox(null != rs.getString("outermost_box") ? rs.getString("outermost_box") : "");
				packingPopupDTO.setBoxClosureDate(
						null != rs.getString("box_closure_date") ? rs.getString("box_closure_date") : "");
				packingPopupDTO.setExpirationDate(
						null != rs.getString("expiration_date") ? rs.getString("expiration_date") : "");
				packingPopupDTO.setBoxPlace(null != rs.getString("box_place") ? rs.getString("box_place") : "");
				packingPopupDTO.setLoadedTime(null != rs.getString("loaded_time") ? rs.getString("loaded_time") : "");
				packingPopupDTO.setSsoLoader(null != rs.getString("sso_loader") ? rs.getString("sso_loader") : "");
				packingPopupDTO.setLoaderName(null != rs.getString("loader_name") ? rs.getString("loader_name") : "");
				packingPopupDTO
						.setDropOffTime(null != rs.getString("drop_off_time") ? rs.getString("drop_off_time") : "");
				packingPopupDTO.setSsoDropper(null != rs.getString("sso_dropper") ? rs.getString("sso_dropper") : "");
				packingPopupDTO
						.setDropperName(null != rs.getString("dropper_name") ? rs.getString("dropper_name") : "");
				packingPopupDTO.setFromSubInventory(
						null != rs.getString("from_subinventory") ? rs.getString("from_subinventory") : "");
				packingPopupDTO
						.setFromLocator(null != rs.getString("from_locator") ? rs.getString("from_locator") : "");
				packingPopupDTO.setToLocater(null != rs.getString("to_locator") ? rs.getString("to_locator") : "");
				packingPopupDTO.setSummaryStatus(null != rs.getString("pfd_status") ? rs.getString("pfd_status") : "");
				packingPopupDetails.add(packingPopupDTO);
			}
		} catch (SQLException e) {
			log.error("Error in getting Material Management : Packing Summary Popup :: " + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("Error in getting Material Management : Packing Summary Popup :: " + e.getMessage());
				}
			}
		}
		return packingPopupDetails;
	}

	@Override
	public List<MMPackingFDPopupDetailsDTO> getMMPackingFDChartPopupDetails(String projectId, String subProject,
			String subInvFrom, String subInvTo, String ownership, String chartType, String status, String startDate,
			String endDate, String fiscalWeek) {
		Connection con = null;
		List<MMPackingFDPopupDetailsDTO> packingPopupDetails = new ArrayList<MMPackingFDPopupDetailsDTO>();
		StringBuilder queryString = new StringBuilder();
		try {
			String[] subProjectStr = subProject.split(";");
			String[] subInvFromStr = subInvFrom.split(";");
			String[] subInvToStr = subInvTo.split(";");
			String[] ownershipStr = ownership.split(";");
			String color = "";
			if (status.equalsIgnoreCase("RED")) {
				color = "RED";
			} else if (status.equalsIgnoreCase("GREEN")) {
				color = "GREEN";
			}
			con = jdbcTemplate.getDataSource().getConnection();
			queryString.append(MaterialManagementConstants.GET_MM_PACKING_CHART_POPUP_DETAILS);
			PreparedStatement pstm = con.prepareStatement(queryString.toString());
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			Array subInvFromStrArr = con.createArrayOf("varchar", subInvFromStr);
			Array subInvToStrArr = con.createArrayOf("varchar", subInvToStr);
			Array ownershipStrArr = con.createArrayOf("varchar", ownershipStr);
			pstm.setString(1, startDate);
			pstm.setString(2, endDate);
			pstm.setString(3, projectId);
			pstm.setArray(4, subProjectStrArr);
			pstm.setArray(5, subProjectStrArr);
			pstm.setArray(6, subInvFromStrArr);
			pstm.setArray(7, subInvFromStrArr);
			pstm.setArray(8, subInvToStrArr);
			pstm.setArray(9, subInvToStrArr);
			pstm.setArray(10, ownershipStrArr);
			pstm.setArray(11, ownershipStrArr);
			pstm.setString(12, color);
			pstm.setString(13, fiscalWeek);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				MMPackingFDPopupDetailsDTO packingPopupDTO = new MMPackingFDPopupDetailsDTO();
				packingPopupDTO.setCostingProject(
						null != rs.getString("costing_project") ? rs.getString("costing_project") : "");
				packingPopupDTO.setOm(null != rs.getString("om") ? rs.getString("om") : "");
				packingPopupDTO.setPei(null != rs.getString("pei") ? rs.getString("pei") : "");
				packingPopupDTO.setPeiDescription(
						null != rs.getString("pei_description") ? rs.getString("pei_description") : "");
				packingPopupDTO
						.setOrderedItem(null != rs.getString("ordered_item") ? rs.getString("ordered_item") : "");
				packingPopupDTO.setDescription(null != rs.getString("description") ? rs.getString("description") : "");
				packingPopupDTO.setQty(null != rs.getString("quantity") ? rs.getString("quantity") : "");
				packingPopupDTO.setFatherCode(null != rs.getString("father_code") ? rs.getString("father_code") : "");
				packingPopupDTO.setStatus(null != rs.getString("status") ? rs.getString("status") : "");
				packingPopupDTO.setOwner(null != rs.getString("owner") ? rs.getString("owner") : "");
				packingPopupDTO.setNotes(null != rs.getString("notes") ? rs.getString("notes") : "");
				packingPopupDTO
						.setSupplyStatus(null != rs.getString("supply_status") ? rs.getString("supply_status") : "");
				packingPopupDTO.setAscpOrderNumber(
						null != rs.getString("ascp_order_number") ? rs.getString("ascp_order_number") : "");
				packingPopupDTO.setVendor(null != rs.getString("vendor") ? rs.getString("vendor") : "");
				packingPopupDTO
						.setPromisedDate(null != rs.getString("promised_date") ? rs.getString("promised_date") : "");
				packingPopupDTO.setPoChild(null != rs.getString("po_child") ? rs.getString("po_child") : "");
				packingPopupDTO.setSpa(null != rs.getString("spa") ? rs.getString("spa") : "");
				packingPopupDTO.setBuyer(null != rs.getString("buyer") ? rs.getString("buyer") : "");
				packingPopupDTO.setFulfillmentResponsibility(
						null != rs.getString("fulfillment_responsibility") ? rs.getString("fulfillment_responsibility")
								: "");
				packingPopupDTO.setInvOrg(null != rs.getString("inv_org") ? rs.getString("inv_org") : "");
				packingPopupDTO.setPlant(null != rs.getString("plant") ? rs.getString("plant") : "");
				packingPopupDTO.setPoFather(null != rs.getString("po_father") ? rs.getString("po_father") : "");
				packingPopupDTO.setContract(null != rs.getString("project_id") ? rs.getString("project_id") : "");
				packingPopupDTO.setPoHeaderNumber(
						null != rs.getString("po_header_number") ? rs.getString("po_header_number") : "");
				packingPopupDTO
						.setPoLineNumber(null != rs.getString("po_line_number") ? rs.getString("po_line_number") : "");
				packingPopupDTO.setMaxArrivalDate(
						null != rs.getString("max_arrival_date") ? rs.getString("max_arrival_date") : "");
				packingPopupDTO.setMoveOrder(null != rs.getString("move_order") ? rs.getString("move_order") : "");
				packingPopupDTO.setMoveOrderDate(
						null != rs.getString("move_order_date") ? rs.getString("move_order_date") : "");
				packingPopupDTO
						.setOutermostBox(null != rs.getString("outermost_box") ? rs.getString("outermost_box") : "");
				packingPopupDTO.setBoxClosureDate(
						null != rs.getString("box_closure_date") ? rs.getString("box_closure_date") : "");
				packingPopupDTO.setExpirationDate(
						null != rs.getString("expiration_date") ? rs.getString("expiration_date") : "");
				packingPopupDTO.setBoxPlace(null != rs.getString("box_place") ? rs.getString("box_place") : "");
				packingPopupDTO.setLoadedTime(null != rs.getString("loaded_time") ? rs.getString("loaded_time") : "");
				packingPopupDTO.setSsoLoader(null != rs.getString("sso_loader") ? rs.getString("sso_loader") : "");
				packingPopupDTO.setLoaderName(null != rs.getString("loader_name") ? rs.getString("loader_name") : "");
				packingPopupDTO
						.setDropOffTime(null != rs.getString("drop_off_time") ? rs.getString("drop_off_time") : "");
				packingPopupDTO.setSsoDropper(null != rs.getString("sso_dropper") ? rs.getString("sso_dropper") : "");
				packingPopupDTO
						.setDropperName(null != rs.getString("dropper_name") ? rs.getString("dropper_name") : "");
				packingPopupDTO.setFromSubInventory(
						null != rs.getString("from_subinventory") ? rs.getString("from_subinventory") : "");
				packingPopupDTO
						.setFromLocator(null != rs.getString("from_locator") ? rs.getString("from_locator") : "");
				packingPopupDTO.setToLocater(null != rs.getString("to_locator") ? rs.getString("to_locator") : "");
				packingPopupDetails.add(packingPopupDTO);
			}
		} catch (SQLException e) {
			log.error("Error in getting Material Management : Packing Chart Popup :: " + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("Error in getting Material Management : Packing Chart Popup :: " + e.getMessage());
				}
			}
		}
		return packingPopupDetails;
	}

	@Override
	public List<MMPackingFDPopupDetailsDTO> getMMPackingFDDetailsExcel(String projectId, String subProject,
			String subInvFrom, String subInvTo, String ownership) {
		Connection con = null;
		List<MMPackingFDPopupDetailsDTO> excelDetails = new ArrayList<MMPackingFDPopupDetailsDTO>();
		try {
			String[] subProjectStr = subProject.split(";");
			String[] subInvFromStr = subInvFrom.split(";");
			String[] subInvToStr = subInvTo.split(";");
			String[] ownershipStr = ownership.split(";");
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con
					.prepareStatement(MaterialManagementConstants.GET_MM_PACKING_SUMMARY_POPUP_DETAILS);
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			Array subInvFromStrArr = con.createArrayOf("varchar", subInvFromStr);
			Array subInvToStrArr = con.createArrayOf("varchar", subInvToStr);
			Array ownershipStrArr = con.createArrayOf("varchar", ownershipStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setArray(3, subProjectStrArr);
			pstm.setArray(4, subInvFromStrArr);
			pstm.setArray(5, subInvFromStrArr);
			pstm.setArray(6, subInvToStrArr);
			pstm.setArray(7, subInvToStrArr);
			pstm.setArray(8, ownershipStrArr);
			pstm.setArray(9, ownershipStrArr);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				MMPackingFDPopupDetailsDTO excelDetailsDTO = new MMPackingFDPopupDetailsDTO();
				excelDetailsDTO.setCostingProject(
						null != rs.getString("costing_project") ? rs.getString("costing_project") : "");
				excelDetailsDTO.setOm(null != rs.getString("om") ? rs.getString("om") : "");
				excelDetailsDTO.setPei(null != rs.getString("pei") ? rs.getString("pei") : "");
				excelDetailsDTO.setPeiDescription(
						null != rs.getString("pei_description") ? rs.getString("pei_description") : "");
				excelDetailsDTO
						.setOrderedItem(null != rs.getString("ordered_item") ? rs.getString("ordered_item") : "");
				excelDetailsDTO.setDescription(null != rs.getString("description") ? rs.getString("description") : "");
				excelDetailsDTO.setQty(null != rs.getString("quantity") ? rs.getString("quantity") : "");
				excelDetailsDTO.setFatherCode(null != rs.getString("father_code") ? rs.getString("father_code") : "");
				excelDetailsDTO.setStatus(null != rs.getString("status") ? rs.getString("status") : "");
				excelDetailsDTO.setOwner(null != rs.getString("owner") ? rs.getString("owner") : "");
				excelDetailsDTO.setNotes(null != rs.getString("notes") ? rs.getString("notes") : "");
				excelDetailsDTO
						.setSupplyStatus(null != rs.getString("supply_status") ? rs.getString("supply_status") : "");
				excelDetailsDTO.setAscpOrderNumber(
						null != rs.getString("ascp_order_number") ? rs.getString("ascp_order_number") : "");
				excelDetailsDTO.setVendor(null != rs.getString("vendor") ? rs.getString("vendor") : "");
				excelDetailsDTO
						.setPromisedDate(null != rs.getString("promised_date") ? rs.getString("promised_date") : "");
				excelDetailsDTO.setPoChild(null != rs.getString("po_child") ? rs.getString("po_child") : "");
				excelDetailsDTO.setSpa(null != rs.getString("spa") ? rs.getString("spa") : "");
				excelDetailsDTO.setBuyer(null != rs.getString("buyer") ? rs.getString("buyer") : "");
				excelDetailsDTO.setFulfillmentResponsibility(
						null != rs.getString("fulfillment_responsibility") ? rs.getString("fulfillment_responsibility")
								: "");
				excelDetailsDTO.setInvOrg(null != rs.getString("inv_org") ? rs.getString("inv_org") : "");
				excelDetailsDTO.setPlant(null != rs.getString("plant") ? rs.getString("plant") : "");
				excelDetailsDTO.setPoFather(null != rs.getString("po_father") ? rs.getString("po_father") : "");
				excelDetailsDTO.setContract(null != rs.getString("project_id") ? rs.getString("project_id") : "");
				excelDetailsDTO.setPoHeaderNumber(
						null != rs.getString("po_header_number") ? rs.getString("po_header_number") : "");
				excelDetailsDTO
						.setPoLineNumber(null != rs.getString("po_line_number") ? rs.getString("po_line_number") : "");
				excelDetailsDTO.setMaxArrivalDate(
						null != rs.getString("max_arrival_date") ? rs.getString("max_arrival_date") : "");
				excelDetailsDTO.setMoveOrder(null != rs.getString("move_order") ? rs.getString("move_order") : "");
				excelDetailsDTO.setMoveOrderDate(
						null != rs.getString("move_order_date") ? rs.getString("move_order_date") : "");
				excelDetailsDTO
						.setOutermostBox(null != rs.getString("outermost_box") ? rs.getString("outermost_box") : "");
				excelDetailsDTO.setBoxClosureDate(
						null != rs.getString("box_closure_date") ? rs.getString("box_closure_date") : "");
				excelDetailsDTO.setExpirationDate(
						null != rs.getString("expiration_date") ? rs.getString("expiration_date") : "");
				excelDetailsDTO.setBoxPlace(null != rs.getString("box_place") ? rs.getString("box_place") : "");
				excelDetailsDTO.setLoadedTime(null != rs.getString("loaded_time") ? rs.getString("loaded_time") : "");
				excelDetailsDTO.setSsoLoader(null != rs.getString("sso_loader") ? rs.getString("sso_loader") : "");
				excelDetailsDTO.setLoaderName(null != rs.getString("loader_name") ? rs.getString("loader_name") : "");
				excelDetailsDTO
						.setDropOffTime(null != rs.getString("drop_off_time") ? rs.getString("drop_off_time") : "");
				excelDetailsDTO.setSsoDropper(null != rs.getString("sso_dropper") ? rs.getString("sso_dropper") : "");
				excelDetailsDTO
						.setDropperName(null != rs.getString("dropper_name") ? rs.getString("dropper_name") : "");
				excelDetailsDTO.setFromSubInventory(
						null != rs.getString("from_subinventory") ? rs.getString("from_subinventory") : "");
				excelDetailsDTO
						.setFromLocator(null != rs.getString("from_locator") ? rs.getString("from_locator") : "");
				excelDetailsDTO.setToLocater(null != rs.getString("to_locator") ? rs.getString("to_locator") : "");
				excelDetails.add(excelDetailsDTO);
			}
		} catch (SQLException e) {
			log.error("Error in getting Material Management : Packing Excel file :: " + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("Error in getting Material Management : Packing Excel file :: " + e.getMessage());
				}
			}
		}
		return excelDetails;
	}
}
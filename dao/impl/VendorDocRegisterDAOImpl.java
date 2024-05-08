package com.bh.realtrack.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bh.realtrack.dao.IVendorDocRegisterDAO;
import com.bh.realtrack.dao.helper.VendorDocRegistryDAOHelper;
import com.bh.realtrack.dto.VDRStatisticsOTDDetailsDTO;
import com.bh.realtrack.util.VendorDocRegisterConstants;

@Repository
public class VendorDocRegisterDAOImpl implements IVendorDocRegisterDAO {

	private static final Logger log = LoggerFactory.getLogger(VendorDocRegisterDAOImpl.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public Map<String, Integer> getStatisticsOTDDetails(String projectId, String subProjectNo, String docOwner,
			String docType) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("Completed Late", 0);
		map.put("Completed On Time", 0);
		map.put("Overdue", 0);
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(VendorDocRegistryDAOHelper.getStatisticsOTDDetailsQuery(docType));) {
			pstm.setString(1, projectId);
			pstm.setString(2, subProjectNo);
			pstm.setString(3, subProjectNo);
			pstm.setString(4, docOwner);
			pstm.setString(5, docOwner);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				String key = "";
				int val = 0;
				key = null != rs.getString("early_status") ? rs.getString("early_status") : "";
				val = null != rs.getString("count") ? rs.getInt("count") : 0;
				if (null != key && !key.isEmpty()) {
					if (key.equalsIgnoreCase("Completed Late")) {
						map.put("Completed Late", val);
					} else if (key.equalsIgnoreCase("Completed On Time")) {
						map.put("Completed On Time", val);
					} else if (key.equalsIgnoreCase("Backlog")) {
						map.put("Overdue", val);
					}
				}
			}
		} catch (SQLException e) {
			log.error("Exception in getStatisticsOTDDetails :: " + e.getMessage());
		}
		return map;
	}

	@Override
	public String getStatisticsOTDLastUpdateDate(String projectId) {
		String lastUpdateDate = "";
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(VendorDocRegisterConstants.GET_VDR_STATISTICS_LAST_UPDATE_DATE);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				lastUpdateDate = null != rs.getString("last_update_date") ? rs.getString("last_update_date") : "";
			}
		} catch (SQLException e) {
			log.error("Exception in getStatisticsOTDLastUpdateDate :: " + e.getMessage());
		}
		return lastUpdateDate;
	}

	@Override
	public List<VDRStatisticsOTDDetailsDTO> getStatisticsOTDChartPopupDetails(String projectId, String subProjectNo,
			String docOwner, String docType, String milestoneStatus) {
		List<VDRStatisticsOTDDetailsDTO> list = new ArrayList<VDRStatisticsOTDDetailsDTO>();
		String status = "";
		if (milestoneStatus.equalsIgnoreCase("Completed Late")) {
			status = "Completed Late";
		} else if (milestoneStatus.equalsIgnoreCase("Completed On Time")) {
			status = "Completed On Time";
		} else if (milestoneStatus.equalsIgnoreCase("Overdue")) {
			status = "Backlog";
		}
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(
						VendorDocRegistryDAOHelper.getStatisticsOTDChartPopupDetailsQuery(docType));) {
			pstm.setString(1, projectId);
			pstm.setString(2, status);
			pstm.setString(3, subProjectNo);
			pstm.setString(4, subProjectNo);
			pstm.setString(5, docOwner);
			pstm.setString(6, docOwner);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				VDRStatisticsOTDDetailsDTO dto = new VDRStatisticsOTDDetailsDTO();
				dto.setJob(null != rs.getString("job_id") ? rs.getString("job_id") : "");
				dto.setCustomerDocId(null != rs.getString("customer_doc") ? rs.getString("customer_doc") : "");
				dto.setDocNumber(null != rs.getString("doc_number") ? rs.getString("doc_number") : "");
				dto.setPertCode(null != rs.getString("pert_code") ? rs.getString("pert_code") : "");
				dto.setDocTitle(null != rs.getString("title_doc") ? rs.getString("title_doc") : "");
				dto.setStatus(null != rs.getString("aging") ? rs.getString("aging") : "");
				dto.setMilestone(null != rs.getString("type1") ? rs.getString("type1") : "");
				dto.setOtd(null != rs.getString("early_status") ? rs.getString("early_status") : "");
				dto.setDaysLate(null != rs.getString("days_late") ? rs.getString("days_late") : "");
				dto.setOwner(null != rs.getString("role_id") ? rs.getString("role_id") : "");
				dto.setBaselineDate(null != rs.getString("vdr_early_date") ? rs.getString("vdr_early_date") : "");
				dto.setActualDate(null != rs.getString("vdr_finish") ? rs.getString("vdr_finish") : "");
				dto.setDocType(null != rs.getString("function1") ? rs.getString("function1") : "");
				list.add(dto);
			}
		} catch (SQLException e) {
			log.error("Exception in getStatisticsOTDChartPopupDetails :: " + e.getMessage());
		}
		return list;
	}

	@Override
	public List<VDRStatisticsOTDDetailsDTO> downloadStatisticsOTDExcelDetails(String projectId, String subProjectNo,
			String docOwner, String docType) {
		List<VDRStatisticsOTDDetailsDTO> list = new ArrayList<VDRStatisticsOTDDetailsDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(VendorDocRegistryDAOHelper.getStatisticsOTDExcelDetailsQuery(docType));) {
			pstm.setString(1, projectId);
			pstm.setString(2, subProjectNo);
			pstm.setString(3, subProjectNo);
			pstm.setString(4, docOwner);
			pstm.setString(5, docOwner);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				VDRStatisticsOTDDetailsDTO dto = new VDRStatisticsOTDDetailsDTO();
				dto.setJob(null != rs.getString("job_id") ? rs.getString("job_id") : "");
				dto.setCustomerDocId(null != rs.getString("customer_doc") ? rs.getString("customer_doc") : "");
				dto.setDocNumber(null != rs.getString("doc_number") ? rs.getString("doc_number") : "");
				dto.setPertCode(null != rs.getString("pert_code") ? rs.getString("pert_code") : "");
				dto.setDocTitle(null != rs.getString("title_doc") ? rs.getString("title_doc") : "");
				dto.setStatus(null != rs.getString("aging") ? rs.getString("aging") : "");
				dto.setMilestone(null != rs.getString("type1") ? rs.getString("type1") : "");
				dto.setOtd(null != rs.getString("early_status") ? rs.getString("early_status") : "");
				dto.setDaysLate(null != rs.getString("days_late") ? rs.getString("days_late") : "");
				dto.setOwner(null != rs.getString("role_id") ? rs.getString("role_id") : "");
				dto.setBaselineDate(null != rs.getString("vdr_early_date") ? rs.getString("vdr_early_date") : "");
				dto.setActualDate(null != rs.getString("vdr_finish") ? rs.getString("vdr_finish") : "");
				dto.setDocType(null != rs.getString("function1") ? rs.getString("function1") : "");
				list.add(dto);
			}
		} catch (Exception e) {
			log.error("Exception in downloadStatisticsOTDExcelDetails :: " + e.getMessage());
		}
		return list;
	}
}

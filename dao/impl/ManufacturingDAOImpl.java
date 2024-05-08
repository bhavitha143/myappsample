package com.bh.realtrack.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.ServerErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.bh.realtrack.dao.IManufacturingDAO;
import com.bh.realtrack.dto.DropDownDTO;
import com.bh.realtrack.dto.ManufacturingPopupDetailsDTO;
import com.bh.realtrack.dto.ManufacturingSummaryPopUpDetailsDTO;
import com.bh.realtrack.enumerators.ErrorCode;
import com.bh.realtrack.util.ManufacturingConstants;

/**
 * @author Shweta D. Sawant
 */
@Repository
public class ManufacturingDAOImpl implements IManufacturingDAO {

	private static final Logger log = LoggerFactory.getLogger(ManufacturingDAOImpl.class.getName());

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public String getRole(String sso) {
		return jdbcTemplate.query(ManufacturingConstants.GET_ROLE, new Object[] { sso },
				new ResultSetExtractor<String>() {
					@Override
					public String extractData(final ResultSet rs) {
						String role = new String();
						try {
							while (rs.next()) {
								role = rs.getString(1);
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting role:" + e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return role;
					}
				});
	}

	@Override
	public List<ManufacturingPopupDetailsDTO> getManufacturingAgingStatus(String projectIds, String companyId,
			String subProject, String ncrtype, String criticality, String organizationName, String role) {
		return jdbcTemplate.query(ManufacturingConstants.GET_AGING_QUERY,
				new Object[] { projectIds, projectIds, Integer.parseInt(companyId), role, role, subProject, subProject,
						ncrtype, ncrtype, criticality, criticality, organizationName, organizationName },
				new ResultSetExtractor<List<ManufacturingPopupDetailsDTO>>() {
					public List<ManufacturingPopupDetailsDTO> extractData(ResultSet rs) throws SQLException {
						List<ManufacturingPopupDetailsDTO> list = new ArrayList<ManufacturingPopupDetailsDTO>();
						try {
							while (rs.next()) {
								ManufacturingPopupDetailsDTO dto = new ManufacturingPopupDetailsDTO();
								dto.setProject(null != rs.getString("project") ? rs.getString("project") : "");
								dto.setSubProject(null != rs.getString("subproject") ? rs.getString("subproject") : "");
								dto.setCriticality(
										null != rs.getString("criticality") ? rs.getString("criticality") : "");
								dto.setOrigin(null != rs.getString("origin") ? rs.getString("origin") : "");
								dto.setNcrNumber(null != rs.getString("ncrnumber") ? rs.getString("ncrnumber") : "");
								dto.setCreationDate(
										null != rs.getString("creationdate") ? rs.getString("creationdate") : "");
								dto.setNcrType(null != rs.getString("ncrtype") ? rs.getString("ncrtype") : "");
								dto.setLastRevision(
										null != rs.getString("lastrevision") ? rs.getString("lastrevision") : "");
								dto.setStatus(null != rs.getString("status") ? rs.getString("status") : "");
								dto.setClosureDate(
										null != rs.getString("closuredate") ? rs.getString("closuredate") : "");
								dto.setAging(null != rs.getString("aging") ? rs.getString("aging") : "");
								dto.setCustomerFeedback(
										null != rs.getString("customerfeedback") ? rs.getString("customerfeedback")
												: "");
								dto.setPartNumber(null != rs.getString("partnumber") ? rs.getString("partnumber") : "");
								dto.setOmId(null != rs.getString("om_id") ? rs.getString("om_id") : "");
								dto.setOmDescription(
										null != rs.getString("om_description") ? rs.getString("om_description") : "");
								dto.setPeiCode(null != rs.getString("pei_code") ? rs.getString("pei_code") : "");
								dto.setPartid(null != rs.getString("partid") ? rs.getString("partid") : "");
								dto.setDiscrepancy(
										null != rs.getString("discrepancy") ? rs.getString("discrepancy") : "");
								dto.setDefect(null != rs.getString("defect") ? rs.getString("defect") : "");
								dto.setDevText(null != rs.getString("devtext") ? rs.getString("devtext") : "");
								dto.setDispostion(
										null != rs.getString("disposition") ? rs.getString("disposition") : "");
								dto.setDispText(null != rs.getString("disptext") ? rs.getString("disptext") : "");
								dto.setSpecialJobId(
										null != rs.getString("specialjobid") ? rs.getString("specialjobid") : "");
								dto.setSpecialJobDate(
										null != rs.getString("specialjobdate") ? rs.getString("specialjobdate") : "");
								dto.setSrcRefreshDate(
										null != rs.getString("srcrefreshdate") ? rs.getString("srcrefreshdate") : "");
								dto.setImputationCode(
										null != rs.getString("imputationcode") ? rs.getString("imputationcode") : "");
								dto.setCauseCode(null != rs.getString("causecode") ? rs.getString("causecode") : "");
								dto.setDevFname(null != rs.getString("devfname") ? rs.getString("devfname") : "");
								dto.setDevLname(null != rs.getString("devlname") ? rs.getString("devlname") : "");
								dto.setDevDate(null != rs.getString("devdate") ? rs.getString("devdate") : "");
								dto.setDispFname(null != rs.getString("dispfname") ? rs.getString("dispfname") : "");
								dto.setDispLname(null != rs.getString("displname") ? rs.getString("displname") : "");
								dto.setDispDate(null != rs.getString("dispdate") ? rs.getString("dispdate") : "");
								dto.setPlantLocation(
										null != rs.getString("plantlocation") ? rs.getString("plantlocation") : "");
								dto.setPlantSublocation(
										null != rs.getString("plantsublocation") ? rs.getString("plantsublocation")
												: "");
								dto.setSpecificResponsibility(null != rs.getString("specificresponsibility")
										? rs.getString("specificresponsibility")
										: "");
								dto.setApprovalFlag(
										null != rs.getString("approvalflag") ? rs.getString("approvalflag") : "");
								dto.setDiscrepancyComments(null != rs.getString("discrepancycomments")
										? rs.getString("discrepancycomments")
										: "");
								dto.setOrganizationName(
										null != rs.getString("organizationname") ? rs.getString("organizationname")
												: "");
								list.add(dto);
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting manufacturing aging status details:"
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return list;
					}
				});
	}

	@Override
	public List<ManufacturingPopupDetailsDTO> getManufacturingAgingStatusForTPS(String projectIds, String companyId,
			String subProject, String ncrtype, String criticality, String role) {
		return jdbcTemplate.query(ManufacturingConstants.GET_AGING_QUERY_FOR_TPS,
				new Object[] { projectIds, projectIds, Integer.parseInt(companyId), role, role, subProject, subProject,
						ncrtype, ncrtype, criticality, criticality },
				new ResultSetExtractor<List<ManufacturingPopupDetailsDTO>>() {
					public List<ManufacturingPopupDetailsDTO> extractData(ResultSet rs) throws SQLException {
						List<ManufacturingPopupDetailsDTO> list = new ArrayList<ManufacturingPopupDetailsDTO>();
						try {
							while (rs.next()) {
								ManufacturingPopupDetailsDTO dto = new ManufacturingPopupDetailsDTO();
								dto.setProject(null != rs.getString("project") ? rs.getString("project") : "");
								dto.setSubProject(null != rs.getString("subproject") ? rs.getString("subproject") : "");
								dto.setCriticality(
										null != rs.getString("criticality") ? rs.getString("criticality") : "");
								dto.setOrigin(null != rs.getString("origin") ? rs.getString("origin") : "");
								dto.setNcrNumber(null != rs.getString("ncrnumber") ? rs.getString("ncrnumber") : "");
								dto.setCreationDate(
										null != rs.getString("creationdate") ? rs.getString("creationdate") : "");
								dto.setNcrType(null != rs.getString("ncrtype") ? rs.getString("ncrtype") : "");
								dto.setLastRevision(
										null != rs.getString("lastrevision") ? rs.getString("lastrevision") : "");
								dto.setStatus(null != rs.getString("status") ? rs.getString("status") : "");
								dto.setClosureDate(
										null != rs.getString("closuredate") ? rs.getString("closuredate") : "");
								dto.setAging(null != rs.getString("aging") ? rs.getString("aging") : "");
								dto.setCustomerFeedback(
										null != rs.getString("customerfeedback") ? rs.getString("customerfeedback")
												: "");
								dto.setPartNumber(null != rs.getString("partnumber") ? rs.getString("partnumber") : "");
								dto.setOmId(null != rs.getString("om_id") ? rs.getString("om_id") : "");
								dto.setOmDescription(
										null != rs.getString("om_description") ? rs.getString("om_description") : "");
								dto.setPeiCode(null != rs.getString("pei_code") ? rs.getString("pei_code") : "");
								dto.setPartid(null != rs.getString("partid") ? rs.getString("partid") : "");
								dto.setDiscrepancy(
										null != rs.getString("discrepancy") ? rs.getString("discrepancy") : "");
								dto.setDefect(null != rs.getString("defect") ? rs.getString("defect") : "");
								dto.setDevText(null != rs.getString("devtext") ? rs.getString("devtext") : "");
								dto.setDispostion(
										null != rs.getString("disposition") ? rs.getString("disposition") : "");
								dto.setDispText(null != rs.getString("disptext") ? rs.getString("disptext") : "");
								dto.setSpecialJobId(
										null != rs.getString("specialjobid") ? rs.getString("specialjobid") : "");
								dto.setSpecialJobDate(
										null != rs.getString("specialjobdate") ? rs.getString("specialjobdate") : "");
								dto.setSrcRefreshDate(
										null != rs.getString("srcrefreshdate") ? rs.getString("srcrefreshdate") : "");
								dto.setImputationCode(
										null != rs.getString("imputationcode") ? rs.getString("imputationcode") : "");
								dto.setCauseCode(null != rs.getString("causecode") ? rs.getString("causecode") : "");
								dto.setDevFname(null != rs.getString("devfname") ? rs.getString("devfname") : "");
								dto.setDevLname(null != rs.getString("devlname") ? rs.getString("devlname") : "");
								dto.setDevDate(null != rs.getString("devdate") ? rs.getString("devdate") : "");
								dto.setDispFname(null != rs.getString("dispfname") ? rs.getString("dispfname") : "");
								dto.setDispLname(null != rs.getString("displname") ? rs.getString("displname") : "");
								dto.setDispDate(null != rs.getString("dispdate") ? rs.getString("dispdate") : "");
								dto.setPlantLocation(
										null != rs.getString("plantlocation") ? rs.getString("plantlocation") : "");
								dto.setPlantSublocation(
										null != rs.getString("plantsublocation") ? rs.getString("plantsublocation")
												: "");
								dto.setSpecificResponsibility(null != rs.getString("specificresponsibility")
										? rs.getString("specificresponsibility")
										: "");
								dto.setApprovalFlag(
										null != rs.getString("approvalflag") ? rs.getString("approvalflag") : "");
								dto.setDiscrepancyComments(null != rs.getString("discrepancycomments")
										? rs.getString("discrepancycomments")
										: "");
								dto.setOrganizationName(
										null != rs.getString("organizationname") ? rs.getString("organizationname")
												: "");
								list.add(dto);
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting manufacturing aging status details:"
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return list;
					}
				});
	}

	@Override
	public String getLastUpdatedDate(String projectIds) {
		return jdbcTemplate.query(ManufacturingConstants.GET_MANUFACTURING_LAST_REFRESHED_DATE,
				new Object[] { projectIds, projectIds }, new ResultSetExtractor<String>() {
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
	public List<ManufacturingPopupDetailsDTO> getManufacturingCreationCurrentDetails(String projectIds,
			String companyId, String subProject, String ncrtype, String criticality, String organizationName,
			String fromdate, String todate, String role) {
		return jdbcTemplate.query(ManufacturingConstants.GET_CREATION_CURRENT_QUERY,
				new Object[] { projectIds, projectIds, Integer.parseInt(companyId), fromdate, todate, role, role,
						subProject, subProject, ncrtype, ncrtype, criticality, criticality, organizationName,
						organizationName },
				new ResultSetExtractor<List<ManufacturingPopupDetailsDTO>>() {
					public List<ManufacturingPopupDetailsDTO> extractData(ResultSet rs) throws SQLException {
						List<ManufacturingPopupDetailsDTO> list = new ArrayList<ManufacturingPopupDetailsDTO>();
						try {
							while (rs.next()) {
								ManufacturingPopupDetailsDTO dto = new ManufacturingPopupDetailsDTO();
								dto.setProject(null != rs.getString("project") ? rs.getString("project") : "");
								dto.setSubProject(null != rs.getString("subproject") ? rs.getString("subproject") : "");
								dto.setCriticality(
										null != rs.getString("criticality") ? rs.getString("criticality") : "");
								dto.setOrigin(null != rs.getString("origin") ? rs.getString("origin") : "");
								dto.setNcrNumber(null != rs.getString("ncrnumber") ? rs.getString("ncrnumber") : "");
								dto.setCreationDate(
										null != rs.getString("creationdate") ? rs.getString("creationdate") : "");
								dto.setNcrType(null != rs.getString("ncrtype") ? rs.getString("ncrtype") : "");
								dto.setLastRevision(
										null != rs.getString("lastrevision") ? rs.getString("lastrevision") : "");
								dto.setStatus(null != rs.getString("status") ? rs.getString("status") : "");
								dto.setClosureDate(
										null != rs.getString("closuredate") ? rs.getString("closuredate") : "");
								dto.setAging(null != rs.getString("aging") ? rs.getString("aging") : "");
								dto.setCustomerFeedback(
										null != rs.getString("customerfeedback") ? rs.getString("customerfeedback")
												: "");
								dto.setPartNumber(null != rs.getString("partnumber") ? rs.getString("partnumber") : "");
								dto.setOmId(null != rs.getString("om_id") ? rs.getString("om_id") : "");
								dto.setOmDescription(
										null != rs.getString("om_description") ? rs.getString("om_description") : "");
								dto.setPeiCode(null != rs.getString("pei_code") ? rs.getString("pei_code") : "");
								dto.setPartid(null != rs.getString("partid") ? rs.getString("partid") : "");
								dto.setDiscrepancy(
										null != rs.getString("discrepancy") ? rs.getString("discrepancy") : "");
								dto.setDefect(null != rs.getString("defect") ? rs.getString("defect") : "");
								dto.setDevText(null != rs.getString("devtext") ? rs.getString("devtext") : "");
								dto.setDispostion(
										null != rs.getString("disposition") ? rs.getString("disposition") : "");
								dto.setDispText(null != rs.getString("disptext") ? rs.getString("disptext") : "");
								dto.setSpecialJobId(
										null != rs.getString("specialjobid") ? rs.getString("specialjobid") : "");
								dto.setSpecialJobDate(
										null != rs.getString("specialjobdate") ? rs.getString("specialjobdate") : "");
								dto.setSrcRefreshDate(
										null != rs.getString("srcrefreshdate") ? rs.getString("srcrefreshdate") : "");
								dto.setImputationCode(
										null != rs.getString("imputationcode") ? rs.getString("imputationcode") : "");
								dto.setCauseCode(null != rs.getString("causecode") ? rs.getString("causecode") : "");
								dto.setDevFname(null != rs.getString("devfname") ? rs.getString("devfname") : "");
								dto.setDevLname(null != rs.getString("devlname") ? rs.getString("devlname") : "");
								dto.setDevDate(null != rs.getString("devdate") ? rs.getString("devdate") : "");
								dto.setDispFname(null != rs.getString("dispfname") ? rs.getString("dispfname") : "");
								dto.setDispLname(null != rs.getString("displname") ? rs.getString("displname") : "");
								dto.setDispDate(null != rs.getString("dispdate") ? rs.getString("dispdate") : "");
								dto.setPlantLocation(
										null != rs.getString("plantlocation") ? rs.getString("plantlocation") : "");
								dto.setPlantSublocation(
										null != rs.getString("plantsublocation") ? rs.getString("plantsublocation")
												: "");
								dto.setSpecificResponsibility(null != rs.getString("specificresponsibility")
										? rs.getString("specificresponsibility")
										: "");
								dto.setApprovalFlag(
										null != rs.getString("approvalflag") ? rs.getString("approvalflag") : "");
								dto.setDiscrepancyComments(null != rs.getString("discrepancycomments")
										? rs.getString("discrepancycomments")
										: "");
								dto.setOrganizationName(
										null != rs.getString("organizationname") ? rs.getString("organizationname")
												: "");
								list.add(dto);
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting manufacturing aging status details:"
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return list;
					}
				});
	}

	@Override
	public List<ManufacturingPopupDetailsDTO> getManufacturingCreationCurrentDetailsForTPS(String projectIds,
			String companyId, String subProject, String ncrtype, String criticality, String fromdate, String todate,
			String role) {
		return jdbcTemplate.query(ManufacturingConstants.GET_CREATION_CURRENT_QUERY_FOR_TPS,
				new Object[] { projectIds, projectIds, Integer.parseInt(companyId), fromdate, todate, role, role,
						subProject, subProject, ncrtype, ncrtype, criticality, criticality },
				new ResultSetExtractor<List<ManufacturingPopupDetailsDTO>>() {
					public List<ManufacturingPopupDetailsDTO> extractData(ResultSet rs) throws SQLException {
						List<ManufacturingPopupDetailsDTO> list = new ArrayList<ManufacturingPopupDetailsDTO>();
						try {
							while (rs.next()) {
								ManufacturingPopupDetailsDTO dto = new ManufacturingPopupDetailsDTO();
								dto.setProject(null != rs.getString("project") ? rs.getString("project") : "");
								dto.setSubProject(null != rs.getString("subproject") ? rs.getString("subproject") : "");
								dto.setCriticality(
										null != rs.getString("criticality") ? rs.getString("criticality") : "");
								dto.setOrigin(null != rs.getString("origin") ? rs.getString("origin") : "");
								dto.setNcrNumber(null != rs.getString("ncrnumber") ? rs.getString("ncrnumber") : "");
								dto.setCreationDate(
										null != rs.getString("creationdate") ? rs.getString("creationdate") : "");
								dto.setNcrType(null != rs.getString("ncrtype") ? rs.getString("ncrtype") : "");
								dto.setLastRevision(
										null != rs.getString("lastrevision") ? rs.getString("lastrevision") : "");
								dto.setStatus(null != rs.getString("status") ? rs.getString("status") : "");
								dto.setClosureDate(
										null != rs.getString("closuredate") ? rs.getString("closuredate") : "");
								dto.setAging(null != rs.getString("aging") ? rs.getString("aging") : "");
								dto.setCustomerFeedback(
										null != rs.getString("customerfeedback") ? rs.getString("customerfeedback")
												: "");
								dto.setPartNumber(null != rs.getString("partnumber") ? rs.getString("partnumber") : "");
								dto.setOmId(null != rs.getString("om_id") ? rs.getString("om_id") : "");
								dto.setOmDescription(
										null != rs.getString("om_description") ? rs.getString("om_description") : "");
								dto.setPeiCode(null != rs.getString("pei_code") ? rs.getString("pei_code") : "");
								dto.setPartid(null != rs.getString("partid") ? rs.getString("partid") : "");
								dto.setDiscrepancy(
										null != rs.getString("discrepancy") ? rs.getString("discrepancy") : "");
								dto.setDefect(null != rs.getString("defect") ? rs.getString("defect") : "");
								dto.setDevText(null != rs.getString("devtext") ? rs.getString("devtext") : "");
								dto.setDispostion(
										null != rs.getString("disposition") ? rs.getString("disposition") : "");
								dto.setDispText(null != rs.getString("disptext") ? rs.getString("disptext") : "");
								dto.setSpecialJobId(
										null != rs.getString("specialjobid") ? rs.getString("specialjobid") : "");
								dto.setSpecialJobDate(
										null != rs.getString("specialjobdate") ? rs.getString("specialjobdate") : "");
								dto.setSrcRefreshDate(
										null != rs.getString("srcrefreshdate") ? rs.getString("srcrefreshdate") : "");
								dto.setImputationCode(
										null != rs.getString("imputationcode") ? rs.getString("imputationcode") : "");
								dto.setCauseCode(null != rs.getString("causecode") ? rs.getString("causecode") : "");
								dto.setDevFname(null != rs.getString("devfname") ? rs.getString("devfname") : "");
								dto.setDevLname(null != rs.getString("devlname") ? rs.getString("devlname") : "");
								dto.setDevDate(null != rs.getString("devdate") ? rs.getString("devdate") : "");
								dto.setDispFname(null != rs.getString("dispfname") ? rs.getString("dispfname") : "");
								dto.setDispLname(null != rs.getString("displname") ? rs.getString("displname") : "");
								dto.setDispDate(null != rs.getString("dispdate") ? rs.getString("dispdate") : "");
								dto.setPlantLocation(
										null != rs.getString("plantlocation") ? rs.getString("plantlocation") : "");
								dto.setPlantSublocation(
										null != rs.getString("plantsublocation") ? rs.getString("plantsublocation")
												: "");
								dto.setSpecificResponsibility(null != rs.getString("specificresponsibility")
										? rs.getString("specificresponsibility")
										: "");
								dto.setApprovalFlag(
										null != rs.getString("approvalflag") ? rs.getString("approvalflag") : "");
								dto.setDiscrepancyComments(null != rs.getString("discrepancycomments")
										? rs.getString("discrepancycomments")
										: "");
								dto.setOrganizationName(
										null != rs.getString("organizationname") ? rs.getString("organizationname")
												: "");
								list.add(dto);
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting manufacturing aging status details:"
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return list;
					}
				});
	}

	@Override
	public List<ManufacturingPopupDetailsDTO> getManufacturingCreationClosureDetails(String projectIds,
			String companyId, String subProject, String ncrtype, String criticality, String organizationName,
			String fromdate, String todate, String role) {
		return jdbcTemplate.query(ManufacturingConstants.GET_CREATION_CLOSURE_QUERY,
				new Object[] { projectIds, projectIds, Integer.parseInt(companyId), fromdate, todate, role, role,
						subProject, subProject, ncrtype, ncrtype, criticality, criticality, organizationName,
						organizationName },
				new ResultSetExtractor<List<ManufacturingPopupDetailsDTO>>() {
					public List<ManufacturingPopupDetailsDTO> extractData(ResultSet rs) throws SQLException {
						List<ManufacturingPopupDetailsDTO> list = new ArrayList<ManufacturingPopupDetailsDTO>();
						try {
							while (rs.next()) {
								ManufacturingPopupDetailsDTO dto = new ManufacturingPopupDetailsDTO();
								dto.setProject(null != rs.getString("project") ? rs.getString("project") : "");
								dto.setSubProject(null != rs.getString("subproject") ? rs.getString("subproject") : "");
								dto.setCriticality(
										null != rs.getString("criticality") ? rs.getString("criticality") : "");
								dto.setOrigin(null != rs.getString("origin") ? rs.getString("origin") : "");
								dto.setNcrNumber(null != rs.getString("ncrnumber") ? rs.getString("ncrnumber") : "");
								dto.setCreationDate(
										null != rs.getString("creationdate") ? rs.getString("creationdate") : "");
								dto.setNcrType(null != rs.getString("ncrtype") ? rs.getString("ncrtype") : "");
								dto.setLastRevision(
										null != rs.getString("lastrevision") ? rs.getString("lastrevision") : "");
								dto.setStatus(null != rs.getString("status") ? rs.getString("status") : "");
								dto.setClosureDate(
										null != rs.getString("closuredate") ? rs.getString("closuredate") : "");
								dto.setAging(null != rs.getString("aging") ? rs.getString("aging") : "");
								dto.setCustomerFeedback(
										null != rs.getString("customerfeedback") ? rs.getString("customerfeedback")
												: "");
								dto.setPartNumber(null != rs.getString("partnumber") ? rs.getString("partnumber") : "");
								dto.setOmId(null != rs.getString("om_id") ? rs.getString("om_id") : "");
								dto.setOmDescription(
										null != rs.getString("om_description") ? rs.getString("om_description") : "");
								dto.setPeiCode(null != rs.getString("pei_code") ? rs.getString("pei_code") : "");
								dto.setPartid(null != rs.getString("partid") ? rs.getString("partid") : "");
								dto.setDiscrepancy(
										null != rs.getString("discrepancy") ? rs.getString("discrepancy") : "");
								dto.setDefect(null != rs.getString("defect") ? rs.getString("defect") : "");
								dto.setDevText(null != rs.getString("devtext") ? rs.getString("devtext") : "");
								dto.setDispostion(
										null != rs.getString("disposition") ? rs.getString("disposition") : "");
								dto.setDispText(null != rs.getString("disptext") ? rs.getString("disptext") : "");
								dto.setSpecialJobId(
										null != rs.getString("specialjobid") ? rs.getString("specialjobid") : "");
								dto.setSpecialJobDate(
										null != rs.getString("specialjobdate") ? rs.getString("specialjobdate") : "");
								dto.setSrcRefreshDate(
										null != rs.getString("srcrefreshdate") ? rs.getString("srcrefreshdate") : "");
								dto.setImputationCode(
										null != rs.getString("imputationcode") ? rs.getString("imputationcode") : "");
								dto.setCauseCode(null != rs.getString("causecode") ? rs.getString("causecode") : "");
								dto.setDevFname(null != rs.getString("devfname") ? rs.getString("devfname") : "");
								dto.setDevLname(null != rs.getString("devlname") ? rs.getString("devlname") : "");
								dto.setDevDate(null != rs.getString("devdate") ? rs.getString("devdate") : "");
								dto.setDispFname(null != rs.getString("dispfname") ? rs.getString("dispfname") : "");
								dto.setDispLname(null != rs.getString("displname") ? rs.getString("displname") : "");
								dto.setDispDate(null != rs.getString("dispdate") ? rs.getString("dispdate") : "");
								dto.setPlantLocation(
										null != rs.getString("plantlocation") ? rs.getString("plantlocation") : "");
								dto.setPlantSublocation(
										null != rs.getString("plantsublocation") ? rs.getString("plantsublocation")
												: "");
								dto.setSpecificResponsibility(null != rs.getString("specificresponsibility")
										? rs.getString("specificresponsibility")
										: "");
								dto.setApprovalFlag(
										null != rs.getString("approvalflag") ? rs.getString("approvalflag") : "");
								dto.setDiscrepancyComments(null != rs.getString("discrepancycomments")
										? rs.getString("discrepancycomments")
										: "");
								dto.setOrganizationName(
										null != rs.getString("organizationname") ? rs.getString("organizationname")
												: "");
								list.add(dto);
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting manufacturing aging status details:"
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return list;
					}
				});
	}

	@Override
	public List<ManufacturingPopupDetailsDTO> getManufacturingCreationClosureDetailsForTPS(String projectIds,
			String companyId, String subProject, String ncrtype, String criticality, String fromdate, String todate,
			String role) {
		return jdbcTemplate.query(ManufacturingConstants.GET_CREATION_CLOSURE_QUERY_FOR_TPS,
				new Object[] { projectIds, projectIds, Integer.parseInt(companyId), fromdate, todate, role, role,
						subProject, subProject, ncrtype, ncrtype, criticality, criticality },
				new ResultSetExtractor<List<ManufacturingPopupDetailsDTO>>() {
					public List<ManufacturingPopupDetailsDTO> extractData(ResultSet rs) throws SQLException {
						List<ManufacturingPopupDetailsDTO> list = new ArrayList<ManufacturingPopupDetailsDTO>();
						try {
							while (rs.next()) {
								ManufacturingPopupDetailsDTO dto = new ManufacturingPopupDetailsDTO();
								dto.setProject(null != rs.getString("project") ? rs.getString("project") : "");
								dto.setSubProject(null != rs.getString("subproject") ? rs.getString("subproject") : "");
								dto.setCriticality(
										null != rs.getString("criticality") ? rs.getString("criticality") : "");
								dto.setOrigin(null != rs.getString("origin") ? rs.getString("origin") : "");
								dto.setNcrNumber(null != rs.getString("ncrnumber") ? rs.getString("ncrnumber") : "");
								dto.setCreationDate(
										null != rs.getString("creationdate") ? rs.getString("creationdate") : "");
								dto.setNcrType(null != rs.getString("ncrtype") ? rs.getString("ncrtype") : "");
								dto.setLastRevision(
										null != rs.getString("lastrevision") ? rs.getString("lastrevision") : "");
								dto.setStatus(null != rs.getString("status") ? rs.getString("status") : "");
								dto.setClosureDate(
										null != rs.getString("closuredate") ? rs.getString("closuredate") : "");
								dto.setAging(null != rs.getString("aging") ? rs.getString("aging") : "");
								dto.setCustomerFeedback(
										null != rs.getString("customerfeedback") ? rs.getString("customerfeedback")
												: "");
								dto.setPartNumber(null != rs.getString("partnumber") ? rs.getString("partnumber") : "");
								dto.setOmId(null != rs.getString("om_id") ? rs.getString("om_id") : "");
								dto.setOmDescription(
										null != rs.getString("om_description") ? rs.getString("om_description") : "");
								dto.setPeiCode(null != rs.getString("pei_code") ? rs.getString("pei_code") : "");
								dto.setPartid(null != rs.getString("partid") ? rs.getString("partid") : "");
								dto.setDiscrepancy(
										null != rs.getString("discrepancy") ? rs.getString("discrepancy") : "");
								dto.setDefect(null != rs.getString("defect") ? rs.getString("defect") : "");
								dto.setDevText(null != rs.getString("devtext") ? rs.getString("devtext") : "");
								dto.setDispostion(
										null != rs.getString("disposition") ? rs.getString("disposition") : "");
								dto.setDispText(null != rs.getString("disptext") ? rs.getString("disptext") : "");
								dto.setSpecialJobId(
										null != rs.getString("specialjobid") ? rs.getString("specialjobid") : "");
								dto.setSpecialJobDate(
										null != rs.getString("specialjobdate") ? rs.getString("specialjobdate") : "");
								dto.setSrcRefreshDate(
										null != rs.getString("srcrefreshdate") ? rs.getString("srcrefreshdate") : "");
								dto.setImputationCode(
										null != rs.getString("imputationcode") ? rs.getString("imputationcode") : "");
								dto.setCauseCode(null != rs.getString("causecode") ? rs.getString("causecode") : "");
								dto.setDevFname(null != rs.getString("devfname") ? rs.getString("devfname") : "");
								dto.setDevLname(null != rs.getString("devlname") ? rs.getString("devlname") : "");
								dto.setDevDate(null != rs.getString("devdate") ? rs.getString("devdate") : "");
								dto.setDispFname(null != rs.getString("dispfname") ? rs.getString("dispfname") : "");
								dto.setDispLname(null != rs.getString("displname") ? rs.getString("displname") : "");
								dto.setDispDate(null != rs.getString("dispdate") ? rs.getString("dispdate") : "");
								dto.setPlantLocation(
										null != rs.getString("plantlocation") ? rs.getString("plantlocation") : "");
								dto.setPlantSublocation(
										null != rs.getString("plantsublocation") ? rs.getString("plantsublocation")
												: "");
								dto.setSpecificResponsibility(null != rs.getString("specificresponsibility")
										? rs.getString("specificresponsibility")
										: "");
								dto.setApprovalFlag(
										null != rs.getString("approvalflag") ? rs.getString("approvalflag") : "");
								dto.setDiscrepancyComments(null != rs.getString("discrepancycomments")
										? rs.getString("discrepancycomments")
										: "");
								dto.setOrganizationName(
										null != rs.getString("organizationname") ? rs.getString("organizationname")
												: "");
								list.add(dto);
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting manufacturing aging status details:"
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return list;
					}
				});
	}

	@Override
	public List<String> getSubProject(String projectId, int companyId, String role) {
		return jdbcTemplate.query(ManufacturingConstants.GET_SUB_PROJECT_FILTER,
				new Object[] { projectId, projectId, companyId, role, role }, new ResultSetExtractor<List<String>>() {
					@Override
					public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
						List<String> list = new ArrayList<>();
						while (rs.next()) {
							String subProject = rs.getString("subproject");
							list.add(subProject);
						}
						return list;
					}
				});
	}

	@Override
	public List<String> getNcrType(String projectId, int companyId, String role) {
		return jdbcTemplate.query(ManufacturingConstants.GET_NCR_TYPE_FILTER,
				new Object[] { projectId, projectId, companyId, role, role }, new ResultSetExtractor<List<String>>() {
					@Override
					public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
						List<String> list = new ArrayList<>();
						while (rs.next()) {
							String ncrType = rs.getString("nonconformancetype");
							list.add(ncrType);
						}
						return list;
					}
				});
	}

	@Override
	public List<DropDownDTO> getCriticality(String projectId, int companyId, String role) {
		return jdbcTemplate.query(ManufacturingConstants.GET_CRITICALITY_FILTER,
				new Object[] { projectId, projectId, companyId, role, role },
				new ResultSetExtractor<List<DropDownDTO>>() {
					@Override
					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
						List<DropDownDTO> list = new ArrayList<>();
						while (rs.next()) {
							DropDownDTO dto = new DropDownDTO();
							dto.setKey(rs.getString("key"));
							dto.setVal(rs.getString("value"));
							list.add(dto);
						}
						return list;
					}
				});
	}

	@Override
	public List<String> getOrganizationName(String projectId, int companyId, String role) {
		return jdbcTemplate.query(ManufacturingConstants.GET_ORGANIZATION_NAME_FILTER,
				new Object[] { projectId, projectId, companyId, role, role }, new ResultSetExtractor<List<String>>() {
					@Override
					public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
						List<String> list = new ArrayList<>();
						while (rs.next()) {
							String organizationName = rs.getString("organizationname");
							list.add(organizationName);
						}
						return list;
					}
				});
	}

	@Override
	public List<ManufacturingPopupDetailsDTO> downloadExcelForNCAgingStatusPopup(String projectId, String companyId,
			String subProject, String ncrType, String criticality, String organizationName, int barStartRange,
			int barEndRange, String barStatus, String role) {

		return jdbcTemplate.query(ManufacturingConstants.NC_AGING_STATUS_POPUP_EXCEL_DOWNLOAD,
				new Object[] { projectId, projectId, Integer.parseInt(companyId), role, role, subProject, subProject,
						ncrType, ncrType, criticality, criticality, organizationName, organizationName, barStartRange,
						barStartRange, barStartRange, barEndRange, barStatus },
				new ResultSetExtractor<List<ManufacturingPopupDetailsDTO>>() {
					public List<ManufacturingPopupDetailsDTO> extractData(ResultSet rs) throws SQLException {
						List<ManufacturingPopupDetailsDTO> list = new ArrayList<ManufacturingPopupDetailsDTO>();
						try {
							while (rs.next()) {
								ManufacturingPopupDetailsDTO dto = new ManufacturingPopupDetailsDTO();
								dto.setProject(null != rs.getString("project") ? rs.getString("project") : "");
								dto.setSubProject(null != rs.getString("subproject") ? rs.getString("subproject") : "");
								dto.setCriticality(
										null != rs.getString("criticality") ? rs.getString("criticality") : "");
								dto.setOrigin(null != rs.getString("origin") ? rs.getString("origin") : "");
								dto.setNcrNumber(null != rs.getString("ncrnumber") ? rs.getString("ncrnumber") : "");
								dto.setCreationDate(
										null != rs.getString("creationdate") ? rs.getString("creationdate") : "");
								dto.setNcrType(null != rs.getString("ncrtype") ? rs.getString("ncrtype") : "");
								dto.setLastRevision(
										null != rs.getString("lastrevision") ? rs.getString("lastrevision") : "");
								dto.setStatus(null != rs.getString("status") ? rs.getString("status") : "");
								dto.setClosureDate(
										null != rs.getString("closuredate") ? rs.getString("closuredate") : "");
								dto.setAging(null != rs.getString("aging") ? rs.getString("aging") : "");
								dto.setCustomerFeedback(
										null != rs.getString("customerfeedback") ? rs.getString("customerfeedback")
												: "");
								dto.setPartNumber(null != rs.getString("partnumber") ? rs.getString("partnumber") : "");
								dto.setOmId(null != rs.getString("om_id") ? rs.getString("om_id") : "");
								dto.setOmDescription(
										null != rs.getString("om_description") ? rs.getString("om_description") : "");
								dto.setPeiCode(null != rs.getString("pei_code") ? rs.getString("pei_code") : "");
								dto.setPartid(null != rs.getString("partid") ? rs.getString("partid") : "");
								dto.setDiscrepancy(
										null != rs.getString("discrepancy") ? rs.getString("discrepancy") : "");
								dto.setDefect(null != rs.getString("defect") ? rs.getString("defect") : "");
								dto.setDevText(null != rs.getString("devtext") ? rs.getString("devtext") : "");
								dto.setDispostion(
										null != rs.getString("disposition") ? rs.getString("disposition") : "");
								dto.setDispText(null != rs.getString("disptext") ? rs.getString("disptext") : "");
								dto.setSpecialJobId(
										null != rs.getString("specialjobid") ? rs.getString("specialjobid") : "");
								dto.setSpecialJobDate(
										null != rs.getString("specialjobdate") ? rs.getString("specialjobdate") : "");
								dto.setSrcRefreshDate(
										null != rs.getString("srcrefreshdate") ? rs.getString("srcrefreshdate") : "");
								dto.setImputationCode(
										null != rs.getString("imputationcode") ? rs.getString("imputationcode") : "");
								dto.setCauseCode(null != rs.getString("causecode") ? rs.getString("causecode") : "");
								dto.setDevFname(null != rs.getString("devfname") ? rs.getString("devfname") : "");
								dto.setDevLname(null != rs.getString("devlname") ? rs.getString("devlname") : "");
								dto.setDevDate(null != rs.getString("devdate") ? rs.getString("devdate") : "");
								dto.setDispFname(null != rs.getString("dispfname") ? rs.getString("dispfname") : "");
								dto.setDispLname(null != rs.getString("displname") ? rs.getString("displname") : "");
								dto.setDispDate(null != rs.getString("dispdate") ? rs.getString("dispdate") : "");
								dto.setPlantLocation(
										null != rs.getString("plantlocation") ? rs.getString("plantlocation") : "");
								dto.setPlantSublocation(
										null != rs.getString("plantsublocation") ? rs.getString("plantsublocation")
												: "");
								dto.setSpecificResponsibility(null != rs.getString("specificresponsibility")
										? rs.getString("specificresponsibility")
										: "");
								dto.setApprovalFlag(
										null != rs.getString("approvalflag") ? rs.getString("approvalflag") : "");
								dto.setDiscrepancyComments(null != rs.getString("discrepancycomments")
										? rs.getString("discrepancycomments")
										: "");
								dto.setOrganizationName(
										null != rs.getString("organizationname") ? rs.getString("organizationname")
												: "");
								list.add(dto);
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting manufacturing aging status details:"
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return list;
					}
				});
	}

	@Override
	public List<ManufacturingPopupDetailsDTO> downloadExcelForNCCreationByCurrentStatusPopup(String projectId,
			String companyId, String subProject, String ncrType, String criticality, String fromDate, String toDate,
			String organizationName, String barStartDate, String barEndDate, String barStatus, String role) {
		return jdbcTemplate.query(ManufacturingConstants.NC_CREATION_CURRENT_POPUP_EXCEL_DOWNLOAD,
				new Object[] { projectId, projectId, Integer.parseInt(companyId), fromDate, toDate, role, role,
						subProject, subProject, ncrType, ncrType, criticality, criticality, organizationName,
						organizationName, barStartDate, barEndDate, barStatus },
				new ResultSetExtractor<List<ManufacturingPopupDetailsDTO>>() {
					public List<ManufacturingPopupDetailsDTO> extractData(ResultSet rs) throws SQLException {
						List<ManufacturingPopupDetailsDTO> list = new ArrayList<ManufacturingPopupDetailsDTO>();
						try {
							while (rs.next()) {
								ManufacturingPopupDetailsDTO dto = new ManufacturingPopupDetailsDTO();
								dto.setProject(null != rs.getString("project") ? rs.getString("project") : "");
								dto.setSubProject(null != rs.getString("subproject") ? rs.getString("subproject") : "");
								dto.setCriticality(
										null != rs.getString("criticality") ? rs.getString("criticality") : "");
								dto.setOrigin(null != rs.getString("origin") ? rs.getString("origin") : "");
								dto.setNcrNumber(null != rs.getString("ncrnumber") ? rs.getString("ncrnumber") : "");
								dto.setCreationDate(
										null != rs.getString("creationdate") ? rs.getString("creationdate") : "");
								dto.setNcrType(null != rs.getString("ncrtype") ? rs.getString("ncrtype") : "");
								dto.setLastRevision(
										null != rs.getString("lastrevision") ? rs.getString("lastrevision") : "");
								dto.setStatus(null != rs.getString("status") ? rs.getString("status") : "");
								dto.setClosureDate(
										null != rs.getString("closuredate") ? rs.getString("closuredate") : "");
								dto.setAging(null != rs.getString("aging") ? rs.getString("aging") : "");
								dto.setCustomerFeedback(
										null != rs.getString("customerfeedback") ? rs.getString("customerfeedback")
												: "");
								dto.setPartNumber(null != rs.getString("partnumber") ? rs.getString("partnumber") : "");
								dto.setOmId(null != rs.getString("om_id") ? rs.getString("om_id") : "");
								dto.setOmDescription(
										null != rs.getString("om_description") ? rs.getString("om_description") : "");
								dto.setPeiCode(null != rs.getString("pei_code") ? rs.getString("pei_code") : "");
								dto.setPartid(null != rs.getString("partid") ? rs.getString("partid") : "");
								dto.setDiscrepancy(
										null != rs.getString("discrepancy") ? rs.getString("discrepancy") : "");
								dto.setDefect(null != rs.getString("defect") ? rs.getString("defect") : "");
								dto.setDevText(null != rs.getString("devtext") ? rs.getString("devtext") : "");
								dto.setDispostion(
										null != rs.getString("disposition") ? rs.getString("disposition") : "");
								dto.setDispText(null != rs.getString("disptext") ? rs.getString("disptext") : "");
								dto.setSpecialJobId(
										null != rs.getString("specialjobid") ? rs.getString("specialjobid") : "");
								dto.setSpecialJobDate(
										null != rs.getString("specialjobdate") ? rs.getString("specialjobdate") : "");
								dto.setSrcRefreshDate(
										null != rs.getString("srcrefreshdate") ? rs.getString("srcrefreshdate") : "");
								dto.setImputationCode(
										null != rs.getString("imputationcode") ? rs.getString("imputationcode") : "");
								dto.setCauseCode(null != rs.getString("causecode") ? rs.getString("causecode") : "");
								dto.setDevFname(null != rs.getString("devfname") ? rs.getString("devfname") : "");
								dto.setDevLname(null != rs.getString("devlname") ? rs.getString("devlname") : "");
								dto.setDevDate(null != rs.getString("devdate") ? rs.getString("devdate") : "");
								dto.setDispFname(null != rs.getString("dispfname") ? rs.getString("dispfname") : "");
								dto.setDispLname(null != rs.getString("displname") ? rs.getString("displname") : "");
								dto.setDispDate(null != rs.getString("dispdate") ? rs.getString("dispdate") : "");
								dto.setPlantLocation(
										null != rs.getString("plantlocation") ? rs.getString("plantlocation") : "");
								dto.setPlantSublocation(
										null != rs.getString("plantsublocation") ? rs.getString("plantsublocation")
												: "");
								dto.setSpecificResponsibility(null != rs.getString("specificresponsibility")
										? rs.getString("specificresponsibility")
										: "");
								dto.setApprovalFlag(
										null != rs.getString("approvalflag") ? rs.getString("approvalflag") : "");
								dto.setDiscrepancyComments(null != rs.getString("discrepancycomments")
										? rs.getString("discrepancycomments")
										: "");
								dto.setOrganizationName(
										null != rs.getString("organizationname") ? rs.getString("organizationname")
												: "");
								list.add(dto);
							}
						} catch (SQLException e) {
							log.error(
									"something went wrong while getting manufacturing creation current status details:"
											+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return list;
					}
				});
	}

	@Override
	public List<ManufacturingPopupDetailsDTO> downloadExcelForNCRsClosedPopup(String projectId, String companyId,
			String subProject, String ncrType, String criticality, String fromDate, String toDate,
			String organizationName, String barStartDate, String barEndDate, String role) {
		return jdbcTemplate.query(ManufacturingConstants.NC_CLOSURE_POPUP_EXCEL_DOWNLOAD,
				new Object[] { projectId, projectId, Integer.parseInt(companyId), fromDate, toDate, role, role,
						subProject, subProject, ncrType, ncrType, criticality, criticality, organizationName,
						organizationName, barStartDate, barEndDate },
				new ResultSetExtractor<List<ManufacturingPopupDetailsDTO>>() {
					public List<ManufacturingPopupDetailsDTO> extractData(ResultSet rs) throws SQLException {
						List<ManufacturingPopupDetailsDTO> list = new ArrayList<ManufacturingPopupDetailsDTO>();
						try {
							while (rs.next()) {
								ManufacturingPopupDetailsDTO dto = new ManufacturingPopupDetailsDTO();
								dto.setProject(null != rs.getString("project") ? rs.getString("project") : "");
								dto.setSubProject(null != rs.getString("subproject") ? rs.getString("subproject") : "");
								dto.setCriticality(
										null != rs.getString("criticality") ? rs.getString("criticality") : "");
								dto.setOrigin(null != rs.getString("origin") ? rs.getString("origin") : "");
								dto.setNcrNumber(null != rs.getString("ncrnumber") ? rs.getString("ncrnumber") : "");
								dto.setCreationDate(
										null != rs.getString("creationdate") ? rs.getString("creationdate") : "");
								dto.setNcrType(null != rs.getString("ncrtype") ? rs.getString("ncrtype") : "");
								dto.setLastRevision(
										null != rs.getString("lastrevision") ? rs.getString("lastrevision") : "");
								dto.setStatus(null != rs.getString("status") ? rs.getString("status") : "");
								dto.setClosureDate(
										null != rs.getString("closuredate") ? rs.getString("closuredate") : "");
								dto.setAging(null != rs.getString("aging") ? rs.getString("aging") : "");
								dto.setCustomerFeedback(
										null != rs.getString("customerfeedback") ? rs.getString("customerfeedback")
												: "");
								dto.setPartNumber(null != rs.getString("partnumber") ? rs.getString("partnumber") : "");
								dto.setOmId(null != rs.getString("om_id") ? rs.getString("om_id") : "");
								dto.setOmDescription(
										null != rs.getString("om_description") ? rs.getString("om_description") : "");
								dto.setPeiCode(null != rs.getString("pei_code") ? rs.getString("pei_code") : "");
								dto.setPartid(null != rs.getString("partid") ? rs.getString("partid") : "");
								dto.setDiscrepancy(
										null != rs.getString("discrepancy") ? rs.getString("discrepancy") : "");
								dto.setDefect(null != rs.getString("defect") ? rs.getString("defect") : "");
								dto.setDevText(null != rs.getString("devtext") ? rs.getString("devtext") : "");
								dto.setDispostion(
										null != rs.getString("disposition") ? rs.getString("disposition") : "");
								dto.setDispText(null != rs.getString("disptext") ? rs.getString("disptext") : "");
								dto.setSpecialJobId(
										null != rs.getString("specialjobid") ? rs.getString("specialjobid") : "");
								dto.setSpecialJobDate(
										null != rs.getString("specialjobdate") ? rs.getString("specialjobdate") : "");
								dto.setSrcRefreshDate(
										null != rs.getString("srcrefreshdate") ? rs.getString("srcrefreshdate") : "");
								dto.setImputationCode(
										null != rs.getString("imputationcode") ? rs.getString("imputationcode") : "");
								dto.setCauseCode(null != rs.getString("causecode") ? rs.getString("causecode") : "");
								dto.setDevFname(null != rs.getString("devfname") ? rs.getString("devfname") : "");
								dto.setDevLname(null != rs.getString("devlname") ? rs.getString("devlname") : "");
								dto.setDevDate(null != rs.getString("devdate") ? rs.getString("devdate") : "");
								dto.setDispFname(null != rs.getString("dispfname") ? rs.getString("dispfname") : "");
								dto.setDispLname(null != rs.getString("displname") ? rs.getString("displname") : "");
								dto.setDispDate(null != rs.getString("dispdate") ? rs.getString("dispdate") : "");
								dto.setPlantLocation(
										null != rs.getString("plantlocation") ? rs.getString("plantlocation") : "");
								dto.setPlantSublocation(
										null != rs.getString("plantsublocation") ? rs.getString("plantsublocation")
												: "");
								dto.setSpecificResponsibility(null != rs.getString("specificresponsibility")
										? rs.getString("specificresponsibility")
										: "");
								dto.setApprovalFlag(
										null != rs.getString("approvalflag") ? rs.getString("approvalflag") : "");
								dto.setDiscrepancyComments(null != rs.getString("discrepancycomments")
										? rs.getString("discrepancycomments")
										: "");
								dto.setOrganizationName(
										null != rs.getString("organizationname") ? rs.getString("organizationname")
												: "");
								list.add(dto);
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting manufacturing closure status details:"
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return list;
					}
				});
	}

	@Override
	public List<ManufacturingPopupDetailsDTO> downloadExcelForNCRsCreatedPopup(String projectId, String companyId,
			String subProject, String ncrType, String criticality, String fromDate, String toDate,
			String organizationName, String barStartDate, String barEndDate, String role) {
		return jdbcTemplate.query(ManufacturingConstants.NC_CREATION_POPUP_EXCEL_DOWNLOAD,
				new Object[] { projectId, projectId, Integer.parseInt(companyId), fromDate, toDate, role, role,
						subProject, subProject, ncrType, ncrType, criticality, criticality, organizationName,
						organizationName, barStartDate, barEndDate },
				new ResultSetExtractor<List<ManufacturingPopupDetailsDTO>>() {
					public List<ManufacturingPopupDetailsDTO> extractData(ResultSet rs) throws SQLException {
						List<ManufacturingPopupDetailsDTO> list = new ArrayList<ManufacturingPopupDetailsDTO>();
						try {
							while (rs.next()) {
								ManufacturingPopupDetailsDTO dto = new ManufacturingPopupDetailsDTO();
								dto.setProject(null != rs.getString("project") ? rs.getString("project") : "");
								dto.setSubProject(null != rs.getString("subproject") ? rs.getString("subproject") : "");
								dto.setCriticality(
										null != rs.getString("criticality") ? rs.getString("criticality") : "");
								dto.setOrigin(null != rs.getString("origin") ? rs.getString("origin") : "");
								dto.setNcrNumber(null != rs.getString("ncrnumber") ? rs.getString("ncrnumber") : "");
								dto.setCreationDate(
										null != rs.getString("creationdate") ? rs.getString("creationdate") : "");
								dto.setNcrType(null != rs.getString("ncrtype") ? rs.getString("ncrtype") : "");
								dto.setLastRevision(
										null != rs.getString("lastrevision") ? rs.getString("lastrevision") : "");
								dto.setStatus(null != rs.getString("status") ? rs.getString("status") : "");
								dto.setClosureDate(
										null != rs.getString("closuredate") ? rs.getString("closuredate") : "");
								dto.setAging(null != rs.getString("aging") ? rs.getString("aging") : "");
								dto.setCustomerFeedback(
										null != rs.getString("customerfeedback") ? rs.getString("customerfeedback")
												: "");
								dto.setPartNumber(null != rs.getString("partnumber") ? rs.getString("partnumber") : "");
								dto.setOmId(null != rs.getString("om_id") ? rs.getString("om_id") : "");
								dto.setOmDescription(
										null != rs.getString("om_description") ? rs.getString("om_description") : "");
								dto.setPeiCode(null != rs.getString("pei_code") ? rs.getString("pei_code") : "");
								dto.setPartid(null != rs.getString("partid") ? rs.getString("partid") : "");
								dto.setDiscrepancy(
										null != rs.getString("discrepancy") ? rs.getString("discrepancy") : "");
								dto.setDefect(null != rs.getString("defect") ? rs.getString("defect") : "");
								dto.setDevText(null != rs.getString("devtext") ? rs.getString("devtext") : "");
								dto.setDispostion(
										null != rs.getString("disposition") ? rs.getString("disposition") : "");
								dto.setDispText(null != rs.getString("disptext") ? rs.getString("disptext") : "");
								dto.setSpecialJobId(
										null != rs.getString("specialjobid") ? rs.getString("specialjobid") : "");
								dto.setSpecialJobDate(
										null != rs.getString("specialjobdate") ? rs.getString("specialjobdate") : "");
								dto.setSrcRefreshDate(
										null != rs.getString("srcrefreshdate") ? rs.getString("srcrefreshdate") : "");
								dto.setImputationCode(
										null != rs.getString("imputationcode") ? rs.getString("imputationcode") : "");
								dto.setCauseCode(null != rs.getString("causecode") ? rs.getString("causecode") : "");
								dto.setDevFname(null != rs.getString("devfname") ? rs.getString("devfname") : "");
								dto.setDevLname(null != rs.getString("devlname") ? rs.getString("devlname") : "");
								dto.setDevDate(null != rs.getString("devdate") ? rs.getString("devdate") : "");
								dto.setDispFname(null != rs.getString("dispfname") ? rs.getString("dispfname") : "");
								dto.setDispLname(null != rs.getString("displname") ? rs.getString("displname") : "");
								dto.setDispDate(null != rs.getString("dispdate") ? rs.getString("dispdate") : "");
								dto.setPlantLocation(
										null != rs.getString("plantlocation") ? rs.getString("plantlocation") : "");
								dto.setPlantSublocation(
										null != rs.getString("plantsublocation") ? rs.getString("plantsublocation")
												: "");
								dto.setSpecificResponsibility(null != rs.getString("specificresponsibility")
										? rs.getString("specificresponsibility")
										: "");
								dto.setApprovalFlag(
										null != rs.getString("approvalflag") ? rs.getString("approvalflag") : "");
								dto.setDiscrepancyComments(null != rs.getString("discrepancycomments")
										? rs.getString("discrepancycomments")
										: "");
								dto.setOrganizationName(
										null != rs.getString("organizationname") ? rs.getString("organizationname")
												: "");
								list.add(dto);
							}
						} catch (SQLException e) {
							log.error("something went wrong while getting manufacturing creation status details:"
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return list;
					}
				});
	}

	@Override
	public List<ManufacturingSummaryPopUpDetailsDTO> getManufacturingSummaryPopUpDetails(String projectId,
			String companyId, String subProject, String ncrType, String criticality, String status) {
		String query = "";
		switch (status) {
		case "Total":
			query = ManufacturingConstants.GET_MANUFACTURING_SUMMARY_POPUP_DETAILS_TOTAL;
			break;

		case "Open":
			query = ManufacturingConstants.GET_MANUFACTURING_SUMMARY_POPUP_DETAILS_OPEN;
			break;
		}

		List<ManufacturingSummaryPopUpDetailsDTO> manufacturingPopUpDetailsList = new ArrayList<>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(query);) {
			pstm.setString(1, projectId);
			pstm.setString(2, projectId);
			pstm.setInt(3, Integer.parseInt(companyId));
			pstm.setString(4, ncrType);
			pstm.setString(5, criticality);
			pstm.setString(6, subProject);
			pstm.setString(7, subProject);
			pstm.setString(8, ncrType);
			pstm.setString(9, ncrType);
			pstm.setString(10, criticality);
			pstm.setString(11, criticality);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				ManufacturingSummaryPopUpDetailsDTO responseDTO = new ManufacturingSummaryPopUpDetailsDTO();
				responseDTO.setProject(null != rs.getString("project") ? rs.getString("project") : "");
				responseDTO.setSubProject(null != rs.getString("subproject") ? rs.getString("subproject") : "");
				responseDTO.setCriticality(null != rs.getString("criticality") ? rs.getString("criticality") : "");
				responseDTO.setOrigin(null != rs.getString("origin") ? rs.getString("origin") : "");
				responseDTO.setNcrNumber(null != rs.getString("ncrnumber") ? rs.getString("ncrnumber") : "");
				responseDTO.setCreationDate(null != rs.getString("creationdate") ? rs.getString("creationdate") : "");
				responseDTO.setNcrType(null != rs.getString("ncrtype") ? rs.getString("ncrtype") : "");
				responseDTO.setLastRevision(null != rs.getString("lastrevision") ? rs.getString("lastrevision") : "");
				responseDTO.setStatus(null != rs.getString("status") ? rs.getString("status") : "");
				responseDTO.setClosureDate(null != rs.getString("closuredate") ? rs.getString("closuredate") : "");
				responseDTO.setAging(null != rs.getString("aging") ? rs.getString("aging") : "");
				responseDTO.setCustomerFeedback(
						null != rs.getString("customerfeedback") ? rs.getString("customerfeedback") : "");
				responseDTO.setPartNumber(null != rs.getString("partnumber") ? rs.getString("partnumber") : "");
				responseDTO.setPartid(null != rs.getString("partid") ? rs.getString("partid") : "");
				responseDTO.setPeiCode(null != rs.getString("pei_code") ? rs.getString("pei_code") : "");
				responseDTO.setOmId(null != rs.getString("om_id") ? rs.getString("om_id") : "");
				responseDTO
						.setOmDescription(null != rs.getString("om_description") ? rs.getString("om_description") : "");
				responseDTO.setDiscrepancy(null != rs.getString("discrepancy") ? rs.getString("discrepancy") : "");
				responseDTO.setDefect(null != rs.getString("defect") ? rs.getString("defect") : "");
				responseDTO.setDevLname(null != rs.getString("devlname") ? rs.getString("devlname") : "");
				responseDTO.setDevFname(null != rs.getString("devfname") ? rs.getString("devfname") : "");
				responseDTO.setDevDate(null != rs.getString("devdate") ? rs.getString("devdate") : "");
				responseDTO.setDevText(null != rs.getString("devtext") ? rs.getString("devtext") : "");
				responseDTO.setDispostion(null != rs.getString("disposition") ? rs.getString("disposition") : "");
				responseDTO.setDispLname(null != rs.getString("displname") ? rs.getString("displname") : "");
				responseDTO.setDispFname(null != rs.getString("dispfname") ? rs.getString("dispfname") : "");
				responseDTO.setDispDate(null != rs.getString("dispdate") ? rs.getString("dispdate") : "");
				responseDTO.setDispText(null != rs.getString("disptext") ? rs.getString("disptext") : "");
				responseDTO.setSpecialJobId(null != rs.getString("specialjobid") ? rs.getString("specialjobid") : "");
				responseDTO.setSpecialJobDate(
						null != rs.getString("specialjobdate") ? rs.getString("specialjobdate") : "");
				responseDTO.setSrcRefreshDate(
						null != rs.getString("srcrefreshdate") ? rs.getString("srcrefreshdate") : "");
				// not required for TPS project
				responseDTO.setImputationCode(
						null != rs.getString("imputationcode") ? rs.getString("imputationcode") : "");
				responseDTO.setCauseCode(null != rs.getString("causecode") ? rs.getString("causecode") : "");
				responseDTO
						.setPlantLocation(null != rs.getString("plantlocation") ? rs.getString("plantlocation") : "");
				responseDTO.setPlantSublocation(
						null != rs.getString("plantsublocation") ? rs.getString("plantsublocation") : "");
				responseDTO.setSpecificResponsibility(
						null != rs.getString("specificresponsibility") ? rs.getString("specificresponsibility") : "");
				responseDTO.setApprovalFlag(null != rs.getString("approvalflag") ? rs.getString("approvalflag") : "");
				responseDTO.setDiscrepancyComments(
						null != rs.getString("discrepancycomments") ? rs.getString("discrepancycomments") : "");
				responseDTO.setOrganizationName(
						null != rs.getString("organizationname") ? rs.getString("organizationname") : "");
				manufacturingPopUpDetailsList.add(responseDTO);
			}

		} catch (SQLException e) {
			log.error("Exception while getting Manufacturing PopUp Details :: " + e.getMessage());
		}
		return manufacturingPopUpDetailsList;
	}

}
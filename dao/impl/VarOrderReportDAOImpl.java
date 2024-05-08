package com.bh.realtrack.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.ServerErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.bh.realtrack.dao.VarOrderReportDAO;
import com.bh.realtrack.dto.VarOrderReport1DTO;
import com.bh.realtrack.dto.VarOrderReportDTO;
import com.bh.realtrack.enumerators.ErrorCode;
import com.bh.realtrack.util.VarOrderReportConstants;

/**
 * 
 * @author tchavda
 * @since 2019-03-01
 * @version 1.0
 */
@Repository
public class VarOrderReportDAOImpl implements VarOrderReportDAO {
	Logger logger = LoggerFactory.getLogger(VarOrderReportDAOImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<VarOrderReportDTO> getVarOrderReportDetails(String projectID, int roleId, int companyId) {
		return jdbcTemplate.query(VarOrderReportConstants.GET_EXCEL_DATA, new Object[] { roleId, companyId, projectID },
				new ResultSetExtractor<List<VarOrderReportDTO>>() {
					@Override
					public List<VarOrderReportDTO> extractData(final ResultSet rs) {
						
						List<VarOrderReportDTO> list = new ArrayList<VarOrderReportDTO>();
						try {
							while (rs.next()) {
								VarOrderReportDTO varOrderReportDTO = new VarOrderReportDTO();
								varOrderReportDTO.setProjectId(rs.getString(1));
								varOrderReportDTO.setProjectName(rs.getString(2));
								varOrderReportDTO.setRecordNumber(rs.getString(3));
								varOrderReportDTO.setTitle(rs.getString(4));
								varOrderReportDTO.setChangeDescription(rs.getString(5));
								varOrderReportDTO.setStatus(rs.getString(6));
								varOrderReportDTO.setCustomerWantDt(rs.getString(7));
								varOrderReportDTO.setAging(rs.getString(8));
								varOrderReportDTO.setUpdateDt(rs.getString(9));
								varOrderReportDTO.setVorOrigAmount(rs.getDouble(10));
								varOrderReportDTO.setVorAmount(rs.getDouble(11));
								varOrderReportDTO.setCurrency(rs.getString(12));
								
								list.add(varOrderReportDTO);
							}
						} catch (SQLException e) {
							logger.error("error occured while iterating excel data:",e.getMessage());
						
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						
						return list;
					}
				});
	}

	@Override
	public List<VarOrderReport1DTO> getVarOrderReportDetails1(String projectId, int roleId, int companyId) {
		return jdbcTemplate.query(VarOrderReportConstants.GET_EXCEL_DATA, new Object[] { roleId, companyId, projectId },
				new ResultSetExtractor<List<VarOrderReport1DTO>>() {
					@Override
					public List<VarOrderReport1DTO> extractData(final ResultSet rs) {
						
						List<VarOrderReport1DTO> list = new ArrayList<VarOrderReport1DTO>();
						try {
							while (rs.next()) {
								VarOrderReport1DTO varOrderReportDTO = new VarOrderReport1DTO();
								varOrderReportDTO.setProjectId(rs.getInt(1));
								varOrderReportDTO.setProjectName(rs.getString(2));
								varOrderReportDTO.setRecordNumber(rs.getString(3));
								varOrderReportDTO.setTitle(rs.getString(4));
								varOrderReportDTO.setChangeDescription(rs.getString(5));
								varOrderReportDTO.setStatus(rs.getString(6));
								varOrderReportDTO.setCustomerWantDate(rs.getString(7));
								varOrderReportDTO.setAging(rs.getInt(8));
								varOrderReportDTO.setUpdateDate(rs.getString(9));
								varOrderReportDTO.setVorOrigAmount(rs.getString(10));
								varOrderReportDTO.setVorAmount(rs.getString(11));
								varOrderReportDTO.setCurrency(rs.getString(12));
								varOrderReportDTO.setSubPl(rs.getString(13));
								varOrderReportDTO.setCustomer(rs.getString(14));
								varOrderReportDTO.setRegion(rs.getString(15));
								varOrderReportDTO.setProjectNumber(rs.getString(16));
								varOrderReportDTO.setProject_name(rs.getString(17));
								varOrderReportDTO.setVorCustomerRefNo(rs.getString(18));
								varOrderReportDTO.setVorBasisForCo(rs.getString(19));
								varOrderReportDTO.setVorType(rs.getString(20));
								varOrderReportDTO.setVorOrigin(rs.getString(21));
								varOrderReportDTO.setVorPhase(rs.getString(22));
								varOrderReportDTO.setCustomerView(rs.getString(23));
								varOrderReportDTO.setCustReviewDate(rs.getString(24));
								varOrderReportDTO.setCustApprovalByDate(rs.getString(25));
								varOrderReportDTO.setValidity(rs.getString(26));
								varOrderReportDTO.setDaysToExpiry(rs.getString(27));
								varOrderReportDTO.setInitialCost(rs.getString(28));
								varOrderReportDTO.setInitialCostUsd(rs.getString(29));
								varOrderReportDTO.setInitialPrice(rs.getString(30));
								varOrderReportDTO.setInitialPriceUsd(rs.getString(31));
								varOrderReportDTO.setInitialCm(rs.getString(32));
								varOrderReportDTO.setFinalCost(rs.getString(33));
								varOrderReportDTO.setFinalCostUsd(rs.getString(34));
								varOrderReportDTO.setFinalPrice(rs.getString(35));
								varOrderReportDTO.setFinalPriceUsd(rs.getString(36));
								varOrderReportDTO.setFinalCm(rs.getString(37));
								varOrderReportDTO.setProbAmountUsd(rs.getString(38));
								varOrderReportDTO.setVorYear(rs.getString(39));
								varOrderReportDTO.setVorYearQtr(rs.getString(40));
								varOrderReportDTO.setVorLikelihood(rs.getString(41));
								varOrderReportDTO.setDateChangeIdentified(rs.getString(42));
								varOrderReportDTO.setCreator(rs.getString(43));
								varOrderReportDTO.setCreationDate(rs.getString(44));
								varOrderReportDTO.setQualityDocumentationImpact(rs.getString(45));
								varOrderReportDTO.setFdsTcpImpact(rs.getString(46));
								varOrderReportDTO.setContractDeliveryImpact(rs.getString(47));
								varOrderReportDTO.setInternalScheduleImpact(rs.getString(48));
								
								list.add(varOrderReportDTO);
							}
						} catch (SQLException e) {
							logger.error("error occured while iterating excel data:",e.getMessage());
						
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						
						return list;
					}
				});
	}
}

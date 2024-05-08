package com.bh.realtrack.dao;

import java.util.List;

import com.bh.realtrack.dto.VarOrderReport1DTO;
import com.bh.realtrack.dto.VarOrderReportDTO;

/**
 * 
 * @author tchavda
 * @since 2019-03-01
 * @version 1.0
 */
public interface VarOrderReportDAO {
	List<VarOrderReportDTO> getVarOrderReportDetails(String projectID, int roleId, int companyId);

	List<VarOrderReport1DTO> getVarOrderReportDetails1(String projectId, int roleId, int companyId);
}

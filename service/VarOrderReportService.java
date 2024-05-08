package com.bh.realtrack.service;

import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

import com.bh.realtrack.dto.VarOrderReportDTO;

/**
 * 
 * @author tchavda
 * @since 2019-03-01
 * @version 1.0
 */
public interface VarOrderReportService {

	List<VarOrderReportDTO> generateOrderList(String projectID, int roleId, int companyId);

	ResponseEntity<InputStreamResource> createExcelBytes(String projectId, int roleId, int companyId);
}

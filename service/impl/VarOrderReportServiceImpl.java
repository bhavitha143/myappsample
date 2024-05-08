package com.bh.realtrack.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.ServerErrorException;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bh.realtrack.dao.IExecutionDao;
import com.bh.realtrack.dao.VarOrderReportDAO;
import com.bh.realtrack.dto.VarOrderReport1DTO;
import com.bh.realtrack.dto.VarOrderReportDTO;
import com.bh.realtrack.enumerators.ErrorCode;
import com.bh.realtrack.excel.ExportExecutionToExcel;
import com.bh.realtrack.model.CallerContext;
import com.bh.realtrack.service.VarOrderReportService;
import com.bh.realtrack.util.VarOrderReportConstants;

/**
 * 
 * @author tchavda
 * @since 2019-03-01
 * @version 1.0
 */
@Service
public class VarOrderReportServiceImpl implements VarOrderReportService {

	@Autowired
	private VarOrderReportDAO varOrderReportDAO;
	
	@Autowired
	private IExecutionDao iExecutionDAO;
	
	@Autowired
	private CallerContext callerContext;

	Logger logger = LoggerFactory.getLogger(VarOrderReportServiceImpl.class);

	@Override
	public List<VarOrderReportDTO> generateOrderList(String projectID, int roleId, int companyId) {

		List<VarOrderReportDTO> varOrderExcelDataList = new ArrayList<VarOrderReportDTO>();

		varOrderExcelDataList = varOrderReportDAO.getVarOrderReportDetails(projectID, roleId, companyId);
		return varOrderExcelDataList;
	}

	@SuppressWarnings("resource")
	@Override
	public ResponseEntity<InputStreamResource> createExcelBytes(String projectId, int roleId, int companyId) {
		
		List<VarOrderReportDTO> varOrderExcelDataList = new ArrayList<VarOrderReportDTO>();
		List<VarOrderReport1DTO> varOrderExcelDataList1 = new ArrayList<VarOrderReport1DTO>();
		
		String sso  = callerContext.getName();
		String role = iExecutionDAO.getRole(sso);
		
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		ExportExecutionToExcel excelObj = new ExportExecutionToExcel();
		varOrderExcelDataList = generateOrderList(projectId, roleId, companyId);

		varOrderExcelDataList1 = varOrderReportDAO.getVarOrderReportDetails1(projectId, roleId, companyId);

		ResponseEntity<InputStreamResource> responseEntity = null;
		String fileName = VarOrderReportConstants.FILE_NAME;

		FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream(fileName);

			if (companyId == 2) {
				workbook = excelObj.exportVarOrderExcel(workbook, varOrderExcelDataList);
			}else {
				if (companyId == 4) {
					workbook = excelObj.exportVarOrderExcel1(workbook, varOrderExcelDataList1, role);
				}
				
			}
			workbook.write(fileOut);

			fileOut.flush();

			File file = new File(fileName);
			InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
			HttpHeaders headers = new HttpHeaders();
			headers.add("content-disposition", String.format("attachment; filename=\"%s\"", file.getName()));
			responseEntity = ResponseEntity.ok().headers(headers).contentLength(file.length())
					.contentType(MediaType.parseMediaType("application/text")).body(resource);
		} catch (IOException e) {
			logger.error("something went wrong while downloading execution excel:" + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} catch (Exception e) {
			logger.error("something went wrong while downloading execution excel:" + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			try {
				fileOut.close();
				workbook.close();
			} catch (IOException e) {
				logger.error("something went wrong while downloading execution excel:" + e.getMessage());
			} catch (Exception e) {
				logger.error("something went wrong while downloading execution excel:" + e.getMessage());
			}
		}
		return responseEntity;

	}
}
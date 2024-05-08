/**
 * 
 */
package com.bh.realtrack.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.ServerErrorException;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bh.realtrack.dao.IContractMilestoneDAO;
import com.bh.realtrack.dto.ContractMilestoneDTO;
import com.bh.realtrack.enumerators.ErrorCode;
import com.bh.realtrack.excel.ExportContractMilestoneToExcel;
import com.bh.realtrack.service.IContractMilestoneService;

/**
 * @author Anand Kumar
 *
 */
@Service
public class ContractMilestoneServiceImpl implements IContractMilestoneService {
	private static Logger log = LoggerFactory
			.getLogger(ProjectComplexityServiceImpl.class.getName());
	@Autowired
	private IContractMilestoneDAO iContractMilestoneDAO;

	@SuppressWarnings("resource")
	@Override
	public ResponseEntity<InputStreamResource> exportContractMilestoneExcel(
			final String projectId) {
		List<ContractMilestoneDTO> contractMilestoneDTOList = new ArrayList<ContractMilestoneDTO>();
		XSSFWorkbook workbook = new XSSFWorkbook();
		ExportContractMilestoneToExcel excelObj = new ExportContractMilestoneToExcel();
		contractMilestoneDTOList = iContractMilestoneDAO
				.getContractMilestoneDetails(projectId);
		ResponseEntity<InputStreamResource> responseEntity = null;
		String fileName = "Contract_Milestone_Details_" + projectId + ".xlsx";
		FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream(fileName);
			workbook = excelObj.exportContractMilestoneExcel(workbook,
					contractMilestoneDTOList);
			workbook.write(fileOut);
			fileOut.flush();
			File file = new File(fileName);
			InputStreamResource resource = new InputStreamResource(
					new FileInputStream(file));
			HttpHeaders headers = new HttpHeaders();
			headers.add("content-disposition", String.format(
					"attachment; filename=\"%s\"", file.getName()));
			responseEntity = ResponseEntity.ok().headers(headers)
					.contentLength(file.length())
					.contentType(MediaType.parseMediaType("application/text"))
					.body(resource);

		} catch (IOException e) {
			log.error("something went wrong while downloading contract milestone excel:"
					+ e.getMessage());
			throw new ServerErrorException(
					ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			try {
				fileOut.close();
				workbook.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		return responseEntity;
	}

}

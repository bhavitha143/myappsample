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
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bh.realtrack.config.HTTPConfig;
import com.bh.realtrack.dao.IKickOffDAO;
import com.bh.realtrack.dto.KickOffDTO;
import com.bh.realtrack.dto.KickOffLogDetailsDTO;
import com.bh.realtrack.dto.SaveKickOffDTO;
import com.bh.realtrack.dto.SaveKickOffResponseDTO;
import com.bh.realtrack.enumerators.ErrorCode;
import com.bh.realtrack.excel.ExportKickOffToExcel;
import com.bh.realtrack.model.CallerContext;
import com.bh.realtrack.pdf.ExportKickOffToPDF;
import com.bh.realtrack.service.IKickOffService;

import java.io.ByteArrayOutputStream;

/**
 *
 * @author Anand Kumar
 *
 */
@Service
public class KickOffServiceImpl extends HTTPConfig implements IKickOffService{
	private static Logger log = LoggerFactory
			.getLogger(KickOffServiceImpl.class.getName());
	private IKickOffDAO iKickOffDAO;
	private CallerContext callerContext;

	@Autowired
	public KickOffServiceImpl(final IKickOffDAO iKickOffDao,
			final CallerContext callerCxt) {
		this.iKickOffDAO = iKickOffDao;
		this.callerContext = callerCxt;
	}

	@Override
	public KickOffLogDetailsDTO getKickOffDetails(final String projectId) {
		List<KickOffDTO> kickOffDTOList = new ArrayList<KickOffDTO>();
		KickOffLogDetailsDTO logDetailsDTO = new KickOffLogDetailsDTO();
		kickOffDTOList = iKickOffDAO.getKickOffData(projectId);
		logDetailsDTO = iKickOffDAO.getKickOffLogDetails(projectId);
		logDetailsDTO.setKickOffList(kickOffDTOList);
		return logDetailsDTO;
	}

	@Override
	public SaveKickOffResponseDTO saveKickOffDetails(
			final SaveKickOffDTO saveKickOffDTO) {
		String sso = callerContext.getName();
		int result = 0;
		SaveKickOffResponseDTO saveKickOffResponseDTO = new SaveKickOffResponseDTO();

		if (iKickOffDAO.saveKickOffData(saveKickOffDTO.getProjectId(),
				saveKickOffDTO.getMatrix(), sso) > 0) {
			result = 1;
		}
		saveKickOffResponseDTO = iKickOffDAO
				.getKickOffSaveLogDetails(saveKickOffDTO.getProjectId());
		if (result == 1) {
			saveKickOffResponseDTO.setStatus("Success");
		} else {
			saveKickOffResponseDTO.setStatus("Error");
		}
		return saveKickOffResponseDTO;
	}

	@SuppressWarnings("resource")
	@Override
	public ResponseEntity<InputStreamResource> exportKickOffExcel(
			final String projectId) {
		XSSFWorkbook workbook = new XSSFWorkbook();
		ExportKickOffToExcel excelObj = new ExportKickOffToExcel();
		KickOffDTO kickOffDTO = iKickOffDAO.getMatrixDetails(projectId);
		ResponseEntity<InputStreamResource> responseEntity = null;
		String fileName = "KickOffMatrix_" + projectId + ".xlsx";
		FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream(fileName);
			workbook = excelObj.exportKickOffExcel(workbook, kickOffDTO);
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
			log.error("something went wrong while downloading kick off excel:"
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

	/**
	 * @author Gajendra Upadhyay
	 * @param projectId
	 * @return ResponseEntity<InputStreamResource>
	 */
	@Override
	public ResponseEntity<InputStreamResource> exportKickOffPDF(final String projectId) throws IOException {
		ByteArrayOutputStream out = null;
		HttpHeaders respHeaders = getHeaders();
		respHeaders.setContentType(MediaType.APPLICATION_JSON);
		KickOffDTO kickOffDTO = iKickOffDAO.getMatrixDetails(projectId);
		ExportKickOffToPDF exportKickOffToPDF = new ExportKickOffToPDF();
		ResponseEntity<InputStreamResource> responseEntity = null;

		try {
			out = exportKickOffToPDF.exportKickOffPDF(projectId, kickOffDTO);
			if (out == null) {
				log.error("Could not generate pdf");
			}

		} catch (Exception e) {
			log.error("Error while serving request {}", e.getMessage());
		}finally {
			out.close();
		}

		String filename = null;
		filename = "KickOffMatrix_" + projectId + ".pdf";

		Resource resource = new ByteArrayResource(out.toByteArray());
		respHeaders.setContentType(MediaType.APPLICATION_PDF);
		respHeaders.setContentDispositionFormData("Content-Disposition", filename);
		respHeaders.setContentLength(resource.contentLength());
		
		return new ResponseEntity<InputStreamResource>(new InputStreamResource(resource.getInputStream()), respHeaders, HttpStatus.OK);
	}
}

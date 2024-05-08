package com.bh.realtrack.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.realtrack.dao.IProcurementDAO;
import com.bh.realtrack.dto.ItemBuyControlDTO;
import com.bh.realtrack.dto.MaterialListDetailsDTO;
import com.bh.realtrack.dto.ProjectTeamDetailsDTO;
import com.bh.realtrack.excel.ExportMaterialListDetailsDTO;
import com.bh.realtrack.model.CallerContext;
import com.bh.realtrack.service.IProcurementService;

@Service
public class ProcurementServiceImpl implements IProcurementService {

	@Autowired
	IProcurementDAO iProcurementDAO;
	CallerContext callerContext;

	private static final Logger log = LoggerFactory.getLogger(ProcurementServiceImpl.class);

	@Autowired
	public ProcurementServiceImpl(final IProcurementDAO iProcurementDAO, final CallerContext callerCxt) {
		this.iProcurementDAO = iProcurementDAO;
		this.callerContext = callerCxt;
	}

	@Override
	public Map<String, Object> getItemBuyControlFlowDetails(String jobNumber, String dummyCode, String activityFilter) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<ItemBuyControlDTO> list = new ArrayList<ItemBuyControlDTO>();
		List<ProjectTeamDetailsDTO> chat = new ArrayList<ProjectTeamDetailsDTO>();
		try {
			list = iProcurementDAO.getItemBuyControlFlowDetails(jobNumber, dummyCode, activityFilter);
			chat = iProcurementDAO.getItemBuyControlFlowChatDetails(jobNumber, dummyCode, activityFilter);
			responseMap.put("data", list);
			responseMap.put("chat", chat);

		} catch (Exception e) {
			log.error("Exception in ProcurementServiceImpl :: getItemBuyControlFlowDetails :: " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public byte[] downloadMaterialListDetails(String projectId, String viewConsideration, String train,
			String subProject, String componentCode, String activityFilter, String viewName) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		SXSSFWorkbook workbook = null;
		ExportMaterialListDetailsDTO excelDTO = new ExportMaterialListDetailsDTO();
		List<MaterialListDetailsDTO> list = new ArrayList<MaterialListDetailsDTO>();
		byte[] excelData = null;
		try {
			workbook = new SXSSFWorkbook();
			list = iProcurementDAO.getMaterialListDetails(projectId, viewConsideration, train, subProject,
					componentCode, activityFilter);
			log.info("Creating Material List Details Sheet with " + list.size() + " rows.");
			if (null != viewConsideration && !viewConsideration.isEmpty()
					&& viewConsideration.equalsIgnoreCase("External")) {
				excelDTO.exportMaterialListCustomDetailsExcel(workbook, list, viewName);
			} else {
				excelDTO.exportMaterialListLiveDetailsExcel(workbook, list, viewName);
			}
			workbook.write(bos);
			excelData = bos.toByteArray();
		} catch (Exception e) {
			log.error("Exception in ProcurementServiceImpl :: downloadMaterialListDetails :: " + e.getMessage());
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				log.error("Exception in ProcurementServiceImpl :: downloadMaterialListDetails :: " + e.getMessage());
			}
		}
		return excelData;
	}

}

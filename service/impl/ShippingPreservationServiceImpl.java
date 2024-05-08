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

import com.bh.realtrack.dao.IShippingPreservationDAO;
import com.bh.realtrack.dto.KeyValueDTO;
import com.bh.realtrack.dto.LCSOSCellDTO;
import com.bh.realtrack.dto.LCVolumeAnalysisDetailsDTO;
import com.bh.realtrack.dto.LcAgainstVolumeActualCostDetailsDTO;
import com.bh.realtrack.dto.LcAgainstVolumeBoxPackingDetailsDTO;
import com.bh.realtrack.dto.LcAgainstVolumeDetailsTransportationDTO;
import com.bh.realtrack.dto.LcVolumeGaugeChartDTO;
import com.bh.realtrack.dto.LogisticCostDropdownDTO;
import com.bh.realtrack.dto.ShippingReportDTO;
import com.bh.realtrack.dto.ShowExpiryMessageDTO;
import com.bh.realtrack.excel.ExportLCVolumeDetailsExcel;
import com.bh.realtrack.model.CallerContext;
import com.bh.realtrack.service.IShippingPreservationService;

/**
 * @author Shweta D. Sawant
 */
@Service
public class ShippingPreservationServiceImpl implements IShippingPreservationService {

	@Autowired
	private IShippingPreservationDAO iShippingPreservationDAO;

	@Autowired
	private CallerContext callerContext;

	private static final Logger log = LoggerFactory.getLogger(ShippingPreservationServiceImpl.class.getName());

	// Shipping/Preservation Widget

	@Override
	public ShowExpiryMessageDTO showExpiryMessageFlag(String projectId, String status) {
		ShowExpiryMessageDTO dto = new ShowExpiryMessageDTO();
		try {
			if (null != status && !status.isEmpty() && !status.equalsIgnoreCase("")) {
				if (status.equalsIgnoreCase("EXPIRED")) {
					dto = iShippingPreservationDAO.showExpiryMessageExpiredFlag(projectId);
				} else {
					dto = iShippingPreservationDAO.showExpiryMessageFlag(projectId);
				}
			}
		} catch (Exception e) {
			log.error("showExpiryMessageFlag(): Exception occurred : " + e.getMessage());
		}
		return dto;
	}

	@Override
	public byte[] downloadShippingReportDetails(String projectId) {
		log.debug("INIT- downloadShippingReportDetails for projectId : {}", projectId);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		SXSSFWorkbook workbook = null;
		ExportLCVolumeDetailsExcel excelDTO = new ExportLCVolumeDetailsExcel();
		List<ShippingReportDTO> list = new ArrayList<ShippingReportDTO>();
		byte[] excelData = null;
		try {
			workbook = new SXSSFWorkbook();
			list = iShippingPreservationDAO.getShippingReportDetails(projectId);
			log.info("Creating Shippingreport Details Sheet with " + list.size() + " rows.");
			excelDTO.exportShippingReportDetailsExcel(workbook, list);
			workbook.write(bos);
			excelData = bos.toByteArray();
		} catch (Exception e) {
			log.error("Error occured while downloading Shipping Report Details:: " + e.getMessage());
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				log.error("Error occured while downloading Shipping Report Details:: " + e.getMessage());
			}
		}
		log.debug("END- downloadShippingReportDetails for projectId : {}", projectId);
		return excelData;
	}

	// Logistic Cost Widget

	@Override
	public LogisticCostDropdownDTO getLogisticCostDropdowns(String projectId) {
		LogisticCostDropdownDTO dropdownResponseDTO = new LogisticCostDropdownDTO();
		List<KeyValueDTO> subProjectList = new ArrayList<KeyValueDTO>();
		List<KeyValueDTO> showByList = new ArrayList<KeyValueDTO>();
		try {
			subProjectList = iShippingPreservationDAO.getLCSubProjectDropdown(projectId);
			showByList = iShippingPreservationDAO.getLCShowByDropdown(projectId);
			dropdownResponseDTO.setSubProject(subProjectList);
			dropdownResponseDTO.setShowBy(showByList);
			dropdownResponseDTO.setDefaultShowByValue("Box & Packing");
		} catch (Exception e) {
			log.error("getLogisticCostDropdowns(): Exception occurred : " + e.getMessage());
		}
		return dropdownResponseDTO;
	}

	@Override
	public Map<String, Object> getLCAgainstVolumeSummary(String projectId, String subProject, String showBy,
			String viewType) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		LcVolumeGaugeChartDTO gaugeChart = new LcVolumeGaugeChartDTO();
		String updatedOn = "", budgetAsSold = "";
		try {
			if (null != viewType && !viewType.isEmpty() && !viewType.equalsIgnoreCase("")) {
				if (viewType.equalsIgnoreCase("Y")) {
					responseMap = iShippingPreservationDAO.getLCAgainstVolumeSummaryMonthlyBarChart(projectId,
							subProject, showBy, viewType);
				} else {
					responseMap = iShippingPreservationDAO.getLCAgainstVolumeSummaryQuarterlyBarChart(projectId,
							subProject, showBy, viewType);
				}
			}
			budgetAsSold = iShippingPreservationDAO.getLCAgainstVolumeBudgetAsSold(projectId, subProject, showBy,
					viewType);
			gaugeChart = iShippingPreservationDAO.getLCAgainstVolumeSummaryGaugeChart(projectId, subProject, showBy,
					viewType);
			updatedOn = iShippingPreservationDAO.getLCAgainstVolumeSummaryUpdatedOn(projectId, subProject);
			responseMap.put("budgetAsSold", budgetAsSold);
			responseMap.put("lcVolumeGaugeChart", gaugeChart);
			responseMap.put("updatedOn", updatedOn);

		} catch (Exception e) {
			log.error("getLCAgainstVolumeSummary(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getLCVolumeAnalysisSummary(String projectId, String subProject, String viewType) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, Object> volAnalysisGraph = new HashMap<String, Object>();
		Map<String, Object> volumePackedPieChart = new HashMap<String, Object>();
		Map<String, Object> volumeShippedPieChart = new HashMap<String, Object>();
		String updatedOn = "";
		try {
			if (null != viewType && !viewType.isEmpty() && !viewType.equalsIgnoreCase("")) {
				if (viewType.equalsIgnoreCase("Y")) {
					volAnalysisGraph = iShippingPreservationDAO.getLCVolumeAnalysisMonthlyGraphSummary(projectId,
							subProject, viewType);
				} else {
					volAnalysisGraph = iShippingPreservationDAO.getLCVolumeAnalysisQuarterlyGraphSummary(projectId,
							subProject, viewType);
				}
			}
			volumePackedPieChart = iShippingPreservationDAO.getLCVolumeAnalysisVolumePackedPiechartSummary(projectId,
					subProject, viewType);
			volumeShippedPieChart = iShippingPreservationDAO.getLCVolumeAnalysisVolumeShippedPiechartSummary(projectId,
					subProject, viewType);
			updatedOn = iShippingPreservationDAO.getLCVolumeAnalysisSummaryUpdatedOn(projectId, subProject);
			responseMap.put("volAnalysisGraph", volAnalysisGraph);
			responseMap.put("volumePackedPieChart", volumePackedPieChart);
			responseMap.put("volumeShippedPieChart", volumeShippedPieChart);
			responseMap.put("updatedOn", updatedOn);

		} catch (Exception e) {
			log.error("getLCVolumeAnalysisSummary(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getLCAgainstVolumeDetailsPopup(String projectId, String subProject, String showBy,
			String viewType, String selectedPeriod) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<LcAgainstVolumeActualCostDetailsDTO> actualCostDetailsList = new ArrayList<LcAgainstVolumeActualCostDetailsDTO>();
		List<LcAgainstVolumeDetailsTransportationDTO> transportationDetailsList = new ArrayList<LcAgainstVolumeDetailsTransportationDTO>();
		List<LcAgainstVolumeBoxPackingDetailsDTO> boxPackingDetailsList = new ArrayList<LcAgainstVolumeBoxPackingDetailsDTO>();
		try {
			actualCostDetailsList = iShippingPreservationDAO.getLCAgainstVolumeActualCostDetails(projectId, subProject,
					viewType, showBy);
			if (null != showBy && !showBy.isEmpty() && !showBy.equalsIgnoreCase("")) {
				if (showBy.equalsIgnoreCase("Box & Packing")) {
					boxPackingDetailsList = iShippingPreservationDAO.getLCAgainstVolumeBoxPackingDetails(projectId,
							subProject);
				} else if (showBy.equalsIgnoreCase("Transportation")) {
					transportationDetailsList = iShippingPreservationDAO
							.getLCAgainstVolumeTransportationDetails(projectId, subProject);
				}
			}
			responseMap.put("actualCost", actualCostDetailsList);
			responseMap.put("transportation", transportationDetailsList);
			responseMap.put("boxAndPacking", boxPackingDetailsList);
		} catch (Exception e) {
			log.error("getLCAgainstVolumeDetailsPopup(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getLCVolumeAnalysisDetailsPopup(String projectId, String subProject, String viewType,
			String chartType, String category) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<LCVolumeAnalysisDetailsDTO> list = new ArrayList<LCVolumeAnalysisDetailsDTO>();
		try {
			if (null != chartType && !chartType.isEmpty() && !chartType.equalsIgnoreCase("")) {
				list = iShippingPreservationDAO.getLCVolumeAnalysisPopupDetails(projectId, subProject, viewType,
						chartType, category);
			}
			responseMap.put("popup", list);
		} catch (Exception e) {
			log.error("getLCVolumeAnalysisDetailsPopup(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public byte[] downloadLCAgainstVolumeDetails(String projectId, String subProject, String showBy, String viewType) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		SXSSFWorkbook workbook = null;
		ExportLCVolumeDetailsExcel excelDTO = new ExportLCVolumeDetailsExcel();
		List<LcAgainstVolumeActualCostDetailsDTO> actualCostDetailsList = new ArrayList<LcAgainstVolumeActualCostDetailsDTO>();
		List<LcAgainstVolumeDetailsTransportationDTO> transportationDetailsList = new ArrayList<LcAgainstVolumeDetailsTransportationDTO>();
		List<LcAgainstVolumeBoxPackingDetailsDTO> boxPackingDetailsList = new ArrayList<LcAgainstVolumeBoxPackingDetailsDTO>();
		byte[] excelData = null;
		try {
			workbook = new SXSSFWorkbook();
			if (null != showBy && !showBy.isEmpty() && !showBy.equalsIgnoreCase("")) {
				actualCostDetailsList = iShippingPreservationDAO.getLCAgainstVolumeActualCostDetails(projectId,
						subProject, viewType, showBy);
				log.info("Creating LC Against Volume Actual Cost Details Sheet with " + actualCostDetailsList.size()
						+ " rows.");
				excelDTO.exportLCAgainstVolumeActualCostDetailsExcel(workbook, actualCostDetailsList);
				if (showBy.equalsIgnoreCase("Box & Packing")) {
					boxPackingDetailsList = iShippingPreservationDAO.getLCAgainstVolumeBoxPackingDetails(projectId,
							subProject);
					log.info("Creating LC Against Volume Box and Packing Details Sheet with "
							+ boxPackingDetailsList.size() + " rows.");
					excelDTO.exportLCAgainstVolumeBoxPackingDetailsExcel(workbook, boxPackingDetailsList);
				} else if (showBy.equalsIgnoreCase("Transportation")) {
					transportationDetailsList = iShippingPreservationDAO
							.getLCAgainstVolumeTransportationDetails(projectId, subProject);
					log.info("Creating LC Against Volume Transportation Details Sheet with "
							+ transportationDetailsList.size() + " rows.");
					excelDTO.exportLCAgainstVolumeTransportationDetailsExcel(workbook, transportationDetailsList);
				}
			}
			workbook.write(bos);
			excelData = bos.toByteArray();
		} catch (Exception e) {
			log.error("Error occured while downloading LC Against Volume Details :: " + e.getMessage());
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				log.error("Error occured while downloading LC Against Volume Details :: " + e.getMessage());
			}
		}
		return excelData;
	}

	@Override
	public byte[] downloadLCVolumeAnalysisDetails(String projectId, String subProject, String viewType) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		SXSSFWorkbook workbook = null;
		ExportLCVolumeDetailsExcel excelDTO = new ExportLCVolumeDetailsExcel();
		List<LCVolumeAnalysisDetailsDTO> list = new ArrayList<LCVolumeAnalysisDetailsDTO>();
		byte[] excelData = null;
		try {
			workbook = new SXSSFWorkbook();
			list = iShippingPreservationDAO.getLCVolumeAnalysisExcelDetails(projectId, subProject);
			log.info("Creating LC Volume Analysis Details Sheet with " + list.size() + " rows.");
			excelDTO.exportLCVolumeAnalysisDetailsExcel(workbook, list);
			workbook.write(bos);
			excelData = bos.toByteArray();
		} catch (Exception e) {
			log.error("Error occured while downloading LC Volume Analysis Details:: " + e.getMessage());
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				log.error("Error occured while downloading LC Volume Analysis Details:: " + e.getMessage());
			}
		}
		return excelData;
	}

	@Override
	public Map<String, Object> getLCSOSCellData(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		String sosCheckFlag = "N";
		try {
			sosCheckFlag = iShippingPreservationDAO.getLCSOSCellData(projectId);
			responseMap.put("sosData", sosCheckFlag);
		} catch (Exception e) {
			log.error("getLCSOSCellData(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> saveLCSOSCellData(LCSOSCellDTO sosCellDTO) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		int result = 0;
		try {
			String sso = callerContext.getName();
			result = iShippingPreservationDAO.saveLCSOSCellData(sosCellDTO, sso);
			if (result == 1) {
				responseMap.put("status", "Success");
			} else {
				responseMap.put("status", "Error");
			}
		} catch (Exception e) {
			responseMap.put("status", "Error");
			log.error("saveLCSOSCellData(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

}

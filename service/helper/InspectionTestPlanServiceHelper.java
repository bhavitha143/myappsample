package com.bh.realtrack.service.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.springframework.stereotype.Component;

import com.bh.realtrack.dto.InspectionConfigurationDropDownDTO;
import com.bh.realtrack.dto.InspectionConfiguratorDTO;
import com.bh.realtrack.util.InspectionTestPlanConstants;

/**
 * @author Anand Kumar
 *
 */
@Component
public class InspectionTestPlanServiceHelper {

	DataFormatter dataFormatter = new DataFormatter();

	public InspectionConfiguratorDTO populateConfigurationDefaultValue(String projectId) {
		InspectionConfiguratorDTO inspectionConfiguratorDTO = new InspectionConfiguratorDTO();

		inspectionConfiguratorDTO.setProjectId(projectId);
		inspectionConfiguratorDTO.setCustomerDataFilter("O,W");
		inspectionConfiguratorDTO.setTpDataFilter("O,W");
		inspectionConfiguratorDTO.setPeriodFilter("90");

		return inspectionConfiguratorDTO;
	}

	public void fetchInspectionConfigurationMasterData(InspectionConfiguratorDTO inspectionConfiguratorDTO) {
		inspectionConfiguratorDTO.setCustomerIconMasterList(new ArrayList<InspectionConfigurationDropDownDTO>());
		inspectionConfiguratorDTO.setTpIconMasterList(new ArrayList<InspectionConfigurationDropDownDTO>());
		inspectionConfiguratorDTO.setEndUserIconMasterList(new ArrayList<InspectionConfigurationDropDownDTO>());
		inspectionConfiguratorDTO.setQcpPageNameMasterList(new ArrayList<InspectionConfigurationDropDownDTO>());
		List<InspectionConfigurationDropDownDTO> periodMasterList = new ArrayList<InspectionConfigurationDropDownDTO>();
		InspectionConfigurationDropDownDTO inspectionConfigurationDropDownDTO = null;

		inspectionConfigurationDropDownDTO = new InspectionConfigurationDropDownDTO();
		inspectionConfigurationDropDownDTO.setKey("R");
		inspectionConfigurationDropDownDTO.setValue(false);
		inspectionConfiguratorDTO.getCustomerIconMasterList().add(inspectionConfigurationDropDownDTO);

		inspectionConfigurationDropDownDTO = new InspectionConfigurationDropDownDTO();
		inspectionConfigurationDropDownDTO.setKey("W");
		inspectionConfigurationDropDownDTO.setValue(false);
		inspectionConfiguratorDTO.getCustomerIconMasterList().add(inspectionConfigurationDropDownDTO);

		inspectionConfigurationDropDownDTO = new InspectionConfigurationDropDownDTO();
		inspectionConfigurationDropDownDTO.setKey("M");
		inspectionConfigurationDropDownDTO.setValue(false);
		inspectionConfiguratorDTO.getCustomerIconMasterList().add(inspectionConfigurationDropDownDTO);

		inspectionConfigurationDropDownDTO = new InspectionConfigurationDropDownDTO();
		inspectionConfigurationDropDownDTO.setKey("O");
		inspectionConfigurationDropDownDTO.setValue(false);
		inspectionConfiguratorDTO.getCustomerIconMasterList().add(inspectionConfigurationDropDownDTO);

		inspectionConfigurationDropDownDTO = new InspectionConfigurationDropDownDTO();
		inspectionConfigurationDropDownDTO.setKey("I");
		inspectionConfigurationDropDownDTO.setValue(false);
		inspectionConfiguratorDTO.getCustomerIconMasterList().add(inspectionConfigurationDropDownDTO);

		inspectionConfigurationDropDownDTO = new InspectionConfigurationDropDownDTO();
		inspectionConfigurationDropDownDTO.setKey("Empty");
		inspectionConfigurationDropDownDTO.setValue(false);
		inspectionConfiguratorDTO.getCustomerIconMasterList().add(inspectionConfigurationDropDownDTO);

		inspectionConfigurationDropDownDTO = new InspectionConfigurationDropDownDTO();
		inspectionConfigurationDropDownDTO.setKey("R");
		inspectionConfigurationDropDownDTO.setValue(false);
		inspectionConfiguratorDTO.getTpIconMasterList().add(inspectionConfigurationDropDownDTO);

		inspectionConfigurationDropDownDTO = new InspectionConfigurationDropDownDTO();
		inspectionConfigurationDropDownDTO.setKey("W");
		inspectionConfigurationDropDownDTO.setValue(false);
		inspectionConfiguratorDTO.getTpIconMasterList().add(inspectionConfigurationDropDownDTO);

		inspectionConfigurationDropDownDTO = new InspectionConfigurationDropDownDTO();
		inspectionConfigurationDropDownDTO.setKey("O");
		inspectionConfigurationDropDownDTO.setValue(false);
		inspectionConfiguratorDTO.getTpIconMasterList().add(inspectionConfigurationDropDownDTO);

		inspectionConfigurationDropDownDTO = new InspectionConfigurationDropDownDTO();
		inspectionConfigurationDropDownDTO.setKey("Empty");
		inspectionConfigurationDropDownDTO.setValue(false);
		inspectionConfiguratorDTO.getTpIconMasterList().add(inspectionConfigurationDropDownDTO);

		inspectionConfigurationDropDownDTO = new InspectionConfigurationDropDownDTO();
		inspectionConfigurationDropDownDTO.setKey("R");
		inspectionConfigurationDropDownDTO.setValue(false);
		inspectionConfiguratorDTO.getEndUserIconMasterList().add(inspectionConfigurationDropDownDTO);

		inspectionConfigurationDropDownDTO = new InspectionConfigurationDropDownDTO();
		inspectionConfigurationDropDownDTO.setKey("W");
		inspectionConfigurationDropDownDTO.setValue(false);
		inspectionConfiguratorDTO.getEndUserIconMasterList().add(inspectionConfigurationDropDownDTO);

		inspectionConfigurationDropDownDTO = new InspectionConfigurationDropDownDTO();
		inspectionConfigurationDropDownDTO.setKey("O");
		inspectionConfigurationDropDownDTO.setValue(false);
		inspectionConfiguratorDTO.getEndUserIconMasterList().add(inspectionConfigurationDropDownDTO);

		inspectionConfigurationDropDownDTO = new InspectionConfigurationDropDownDTO();
		inspectionConfigurationDropDownDTO.setKey("I");
		inspectionConfigurationDropDownDTO.setValue(false);
		inspectionConfiguratorDTO.getEndUserIconMasterList().add(inspectionConfigurationDropDownDTO);

		inspectionConfigurationDropDownDTO = new InspectionConfigurationDropDownDTO();
		inspectionConfigurationDropDownDTO.setKey("Empty");
		inspectionConfigurationDropDownDTO.setValue(false);
		inspectionConfiguratorDTO.getEndUserIconMasterList().add(inspectionConfigurationDropDownDTO);

		inspectionConfiguratorDTO.getQcpPageNameMasterList().add(inspectionConfigurationDropDownDTO);

		inspectionConfigurationDropDownDTO = new InspectionConfigurationDropDownDTO("All", "All");
		periodMasterList.add(inspectionConfigurationDropDownDTO);

		inspectionConfigurationDropDownDTO = new InspectionConfigurationDropDownDTO("90", "90");
		periodMasterList.add(inspectionConfigurationDropDownDTO);

		inspectionConfigurationDropDownDTO = new InspectionConfigurationDropDownDTO("45", "45");
		periodMasterList.add(inspectionConfigurationDropDownDTO);

		inspectionConfigurationDropDownDTO = new InspectionConfigurationDropDownDTO("30", "30");
		periodMasterList.add(inspectionConfigurationDropDownDTO);

		inspectionConfiguratorDTO.setPeriodMasterList(periodMasterList);

	}

	public void populateSelectedConfigurationValue(InspectionConfiguratorDTO inspectionConfiguratorDTO) {

		String[] selectedCustomerValue = null;
		String[] selectedTpValue = null;
		String[] selectedEndUserValue = null;
		String[] selectedQCPPage = null;

		if (null != inspectionConfiguratorDTO.getCustomerIconMasterList()
				&& null != inspectionConfiguratorDTO.getCustomerDataFilter()
				&& !InspectionTestPlanConstants.EMPTY_STRING
						.equalsIgnoreCase(inspectionConfiguratorDTO.getCustomerDataFilter())) {
			selectedCustomerValue = inspectionConfiguratorDTO.getCustomerDataFilter().split(",");
			for (String customerValue : selectedCustomerValue) {
				for (InspectionConfigurationDropDownDTO inspectionConfigurationDropDownDTO : inspectionConfiguratorDTO
						.getCustomerIconMasterList()) {
					if (customerValue.equalsIgnoreCase(inspectionConfigurationDropDownDTO.getKey())) {
						inspectionConfigurationDropDownDTO.setValue(true);
					}
				}
			}
		}

		if (null != inspectionConfiguratorDTO.getTpDataFilter() && !InspectionTestPlanConstants.EMPTY_STRING
				.equalsIgnoreCase(inspectionConfiguratorDTO.getTpDataFilter())) {
			selectedTpValue = inspectionConfiguratorDTO.getCustomerDataFilter().split(",");
			for (String tpValue : selectedTpValue) {
				for (InspectionConfigurationDropDownDTO inspectionConfigurationDropDownDTO : inspectionConfiguratorDTO
						.getTpIconMasterList()) {
					if (tpValue.equalsIgnoreCase(inspectionConfigurationDropDownDTO.getKey())) {
						inspectionConfigurationDropDownDTO.setValue(true);
					}
				}
			}
		}

		if (null != inspectionConfiguratorDTO.getEndUserDataFilter() && !InspectionTestPlanConstants.EMPTY_STRING
				.equalsIgnoreCase(inspectionConfiguratorDTO.getEndUserDataFilter())) {
			selectedEndUserValue = inspectionConfiguratorDTO.getEndUserDataFilter().split(",");
			for (String endUserValue : selectedEndUserValue) {
				for (InspectionConfigurationDropDownDTO inspectionConfigurationDropDownDTO : inspectionConfiguratorDTO
						.getEndUserIconMasterList()) {
					if (endUserValue.equalsIgnoreCase(inspectionConfigurationDropDownDTO.getKey())) {
						inspectionConfigurationDropDownDTO.setValue(true);
					}
				}
			}
		}

		if (null != inspectionConfiguratorDTO.getQcpPageNameFilter()
				&& !inspectionConfiguratorDTO.getQcpPageNameFilter().isEmpty()) {
			inspectionConfiguratorDTO.setUserSelectedQCPPageName(new ArrayList<String>());
			selectedQCPPage = inspectionConfiguratorDTO.getQcpPageNameFilter().split(",");
			for (String qcpPage : selectedQCPPage) {
				inspectionConfiguratorDTO.getUserSelectedQCPPageName().add(qcpPage);
			}
		}

		if (null == inspectionConfiguratorDTO.getPeriodFilter()) {
			inspectionConfiguratorDTO.setPeriodFilter(InspectionTestPlanConstants.ALL_STRING);
		}

	}

	public boolean populateUserSelectedConfigurationValue(InspectionConfiguratorDTO inspectionConfiguratorDTO) {

		StringBuilder selectedCustomer = null;
		StringBuilder selectedTp = null;
		StringBuilder selectedEndUser = null;
		StringBuilder qcpPageFilter = null;
		boolean firstElement = true;
		boolean validCustomerConfiguration = false;
		String status = "Error";
		boolean valid = false;
		StringBuilder message = new StringBuilder();

		if (null != inspectionConfiguratorDTO.getCustomerIconMasterList()
				&& !inspectionConfiguratorDTO.getCustomerIconMasterList().isEmpty()) {
			selectedCustomer = new StringBuilder();
			for (InspectionConfigurationDropDownDTO inspectionConfigurationDropDownDTO : inspectionConfiguratorDTO
					.getCustomerIconMasterList()) {
				if (inspectionConfigurationDropDownDTO.isValue()) {
					if (!firstElement) {
						selectedCustomer.append(",");
					}
					selectedCustomer.append(inspectionConfigurationDropDownDTO.getKey());
					firstElement = false;
					validCustomerConfiguration = true;
				}
			}
			inspectionConfiguratorDTO.setCustomerDataFilter(selectedCustomer.toString());
		}

		firstElement = true;
		if (null != inspectionConfiguratorDTO.getTpIconMasterList()
				&& !inspectionConfiguratorDTO.getTpIconMasterList().isEmpty()) {

			selectedTp = new StringBuilder();
			for (InspectionConfigurationDropDownDTO inspectionConfigurationDropDownDTO : inspectionConfiguratorDTO
					.getTpIconMasterList()) {
				if (inspectionConfigurationDropDownDTO.isValue()) {
					if (!firstElement) {
						selectedTp.append(",");
					}
					selectedTp.append(inspectionConfigurationDropDownDTO.getKey());
					firstElement = false;
				}
			}
			inspectionConfiguratorDTO.setTpDataFilter(selectedTp.toString());
		}

		firstElement = true;
		if (null != inspectionConfiguratorDTO.getEndUserIconMasterList()
				&& !inspectionConfiguratorDTO.getEndUserIconMasterList().isEmpty()) {

			selectedEndUser = new StringBuilder();
			for (InspectionConfigurationDropDownDTO inspectionConfigurationDropDownDTO : inspectionConfiguratorDTO
					.getEndUserIconMasterList()) {
				if (inspectionConfigurationDropDownDTO.isValue()) {
					if (!firstElement) {
						selectedEndUser.append(",");
					}
					selectedEndUser.append(inspectionConfigurationDropDownDTO.getKey());
					firstElement = false;
				}
			}
			inspectionConfiguratorDTO.setEndUserDataFilter(selectedEndUser.toString());
		}

		firstElement = true;
		inspectionConfiguratorDTO.setQcpPageNameFilter(null);
		if (null != inspectionConfiguratorDTO.getUserSelectedQCPPageName()
				&& !inspectionConfiguratorDTO.getUserSelectedQCPPageName().isEmpty()) {
			qcpPageFilter = new StringBuilder();
			for (String qcpPage : inspectionConfiguratorDTO.getUserSelectedQCPPageName()) {
				if (!firstElement) {
					qcpPageFilter.append(",");
				}
				qcpPageFilter.append(qcpPage);
				firstElement = false;
			}
			inspectionConfiguratorDTO.setQcpPageNameFilter(qcpPageFilter.toString());
		}

		if (null != inspectionConfiguratorDTO.getPeriodFilter() && InspectionTestPlanConstants.ALL_STRING
				.equalsIgnoreCase(inspectionConfiguratorDTO.getPeriodFilter())) {
			inspectionConfiguratorDTO.setPeriodFilter(null);
		}

		if (!validCustomerConfiguration) {
			message.append("Please check atleast one Customer filter");
		}
		if (validCustomerConfiguration) {
			status = "Success";
			valid = true;
			message.append("Inspection Configurations are saved successfully");
		}

		inspectionConfiguratorDTO.setStatus(status);
		inspectionConfiguratorDTO.setMessage(message.toString());
		
		return valid;

	}

	public boolean validateExcelColumnDetails(Map<Long, String> columnNameMap, String excelHeader,
			StringBuilder validationMessage) {
		boolean validExcel = true;
		boolean columnError = false;
		boolean firstErrorColumn = true;
		StringBuilder errorColumns = new StringBuilder();
		Long columnKey = Long.valueOf(1);
		String[] excelHeaderData = excelHeader.split(",");

		if (excelHeaderData.length != columnNameMap.size()) {
			validExcel = false;
			validationMessage.append("Incorrect number of columns");
		} else {
			for (int i = 0; i < columnNameMap.size(); i++) {
				columnKey = Long.valueOf(i + 1);
				if (!columnNameMap.get(columnKey).equalsIgnoreCase(excelHeaderData[i].trim())) {
					validExcel = false;
					columnError = true;
					if (!firstErrorColumn) {
						errorColumns.append(", ");
					}
					errorColumns.append(columnNameMap.get(columnKey));
					firstErrorColumn = false;
				}
			}

			if (columnError) {
				validationMessage
						.append("Column(s) " + errorColumns.toString() + " are either misplaced or misspelled");
			}
		}

		return validExcel;
	}

}

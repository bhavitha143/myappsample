package com.bh.realtrack.dao.helper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.bh.realtrack.util.VendorDocRegisterConstants;

/**
 * @author Shweta D. Sawant
 */
@Component
public class VendorDocRegistryDAOHelper {

	public static String getStatisticsOTDDetailsQuery(String docType) {
		StringBuilder queryString = new StringBuilder();
		queryString.append(VendorDocRegisterConstants.GET_VDR_STATISTICS_OTD_DETAILS);
		if (null != docType && !docType.isEmpty() && !docType.equalsIgnoreCase("0")) {
			if (docType.equalsIgnoreCase("ENG")) {
				queryString.append(StringUtils.SPACE + "and upper(vdr.function1) = 'ENG'" + StringUtils.SPACE);
			} else if (docType.equalsIgnoreCase("Not Available")) {
				queryString.append(StringUtils.SPACE + "and vdr.function1 is null" + StringUtils.SPACE);
			} else {
				queryString.append(StringUtils.SPACE + "and (vdr.function1 is null or upper(vdr.function1) <> 'ENG')"
						+ StringUtils.SPACE);
			}
		}
		queryString.append(VendorDocRegisterConstants.GET_VDR_STATISTICS_OTD_DETAILS_GROUP_BY_CONDITION);
		return queryString.toString();
	}

	public static String getStatisticsOTDChartPopupDetailsQuery(String docType) {
		StringBuilder queryString = new StringBuilder();
		queryString.append(VendorDocRegisterConstants.GET_VDR_STATISTICS_OTD_POPUP_DETAILS);
		if (null != docType && !docType.isEmpty() && !docType.equalsIgnoreCase("0")) {
			if (docType.equalsIgnoreCase("ENG")) {
				queryString.append(StringUtils.SPACE + "and upper(vdr.function1) = 'ENG'");
			} else if (docType.equalsIgnoreCase("Not Available")) {
				queryString.append(StringUtils.SPACE + "and vdr.function1 is null");
			} else {
				queryString.append(StringUtils.SPACE + "and (vdr.function1 is null or upper(vdr.function1) <> 'ENG')");
			}
		}
		return queryString.toString();
	}

	public static String getStatisticsOTDExcelDetailsQuery(String docType) {
		StringBuilder queryString = new StringBuilder();
		queryString.append(VendorDocRegisterConstants.GET_STATISTICS_OTD_EXCEL_DETAILS);
		if (null != docType && !docType.isEmpty() && !docType.equalsIgnoreCase("0")) {
			if (docType.equalsIgnoreCase("ENG")) {
				queryString.append(StringUtils.SPACE + "and upper(vdr.function1) = 'ENG'");
			} else if (docType.equalsIgnoreCase("Not Available")) {
				queryString.append(StringUtils.SPACE + "and vdr.function1 is null");
			} else {
				queryString.append(StringUtils.SPACE + "and (vdr.function1 is null or upper(vdr.function1) <> 'ENG')");
			}
		}
		return queryString.toString();
	}
}

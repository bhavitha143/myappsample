package com.bh.realtrack.dao.helper;

import org.springframework.stereotype.Component;

import com.bh.realtrack.util.MaterialManagementConstants;

/**
 * @author Shweta D. Sawant
 */
@Component
public class MaterialManagementDAOHelper {

	public static String getMMReceivingDetailsQuery(String type) {
		StringBuilder queryString = new StringBuilder();
		if (type.equalsIgnoreCase("TOT_MAT_NEEDED")) {

			queryString.append(MaterialManagementConstants.GET_MM_RECEIVING_DETAILS);

		} else if (type.equalsIgnoreCase("MAT_ARR")) {

			queryString.append(MaterialManagementConstants.GET_MM_RECEIVING_DETAILS);
			queryString.append(MaterialManagementConstants.GET_MAT_ARR_DETAILS);

		} else if (type.equalsIgnoreCase("ON_INV")) {

			queryString.append(MaterialManagementConstants.GET_MM_RECEIVING_DETAILS);
			queryString.append(MaterialManagementConstants.GET_ON_INV_DETAILS);

		} else if (type.equalsIgnoreCase("ON_REC_AREA")) {

			queryString.append(MaterialManagementConstants.GET_MM_RECEIVING_DETAILS);
			queryString.append(MaterialManagementConstants.GET_ON_REC_AREA_DETAILS);

		} else if (type.equalsIgnoreCase("AGING_NON_CRC")) {

			queryString.append(MaterialManagementConstants.GET_MM_RECEIVING_AGING_PIE_CHART_DETAILS);
			queryString.append(MaterialManagementConstants.GET_MM_RECEIVING_AGING_NON_CRC_DETAILS);

		} else if (type.equalsIgnoreCase("AGING_CRC")) {

			queryString.append(MaterialManagementConstants.GET_MM_RECEIVING_AGING_PIE_CHART_DETAILS);
			queryString.append(MaterialManagementConstants.GET_MM_RECEIVING_AGING_CRC_DETAILS);

		} else if (type.equalsIgnoreCase("WF_ANO_OPEN")) {

			queryString.append(MaterialManagementConstants.GET_MM_RECEIVING_ANOMALY_PIE_CHART_DETAILS);
			queryString.append(MaterialManagementConstants.GET_MM_RECEIVING_WF_ANO_OPEN_DETAILS);

		} else if (type.equalsIgnoreCase("WF_ANO_CLOSED")) {

			queryString.append(MaterialManagementConstants.GET_MM_RECEIVING_ANOMALY_PIE_CHART_DETAILS);
			queryString.append(MaterialManagementConstants.GET_MM_RECEIVING_WF_ANO_CLOSED_DETAILS);

		} else if (type.equalsIgnoreCase("WF_NO_ANO")) {

			queryString.append(MaterialManagementConstants.GET_MM_RECEIVING_ANOMALY_PIE_CHART_DETAILS);
			queryString.append(MaterialManagementConstants.GET_MM_RECEIVING_WF_NO_ANO_DETAILS);

		} else if (type.equalsIgnoreCase("CYCLE_TIME_CHART_WEEKLY")) {

			queryString.append(MaterialManagementConstants.GET_MM_RECEIVING_CYCLE_TIME_CHART_WEEKLY_DETAILS);

		} else if (type.equalsIgnoreCase("CYCLE_TIME_CHART_MONTHLY")) {

		}
		return queryString.toString();
	}

	public static String getMMWFAnomolyDetailsQuery(String chartType, String status) {
		StringBuilder queryString = new StringBuilder();

		if (chartType.equalsIgnoreCase("SUMMARY") || chartType.equalsIgnoreCase("EXCEL")) {
			// queryString.append(MaterialManagementConstants.GET_MM_WF_ANOMALY_DETAILS);

			if (status.equalsIgnoreCase("OPEN")) {
				queryString.append(MaterialManagementConstants.GET_MM_WF_ANOMALY_OPEN_DETAILS);
			} else if (status.equalsIgnoreCase("CLOSED")) {
				queryString.append(MaterialManagementConstants.GET_MM_WF_ANOMALY_CLOSED_DETAILS);
			}

		} else if (chartType.equalsIgnoreCase("CYCLE_CHART")) {
			// queryString.append(MaterialManagementConstants.GET_MM_WF_ANOMALY_DETAILS);
			queryString.append(MaterialManagementConstants.GET_MM_WF_ANOMALY_CLOSED_DETAILS);

			if (status.equalsIgnoreCase("DAYS_LESS_THAN_5")) {
				queryString.append(" and color='Green'");
			} else if (status.equalsIgnoreCase("DAYS_BETWEEN_5_AND_22")) {
				queryString.append(" and color='Yellow'");
			} else if (status.equalsIgnoreCase("DAYS_MORE_THAN_22")) {
				queryString.append(" and color='Red'");
			}

		} else if (chartType.equalsIgnoreCase("AGING_CHART")) {
			// queryString.append(MaterialManagementConstants.GET_MM_WF_ANOMALY_DETAILS);
			queryString.append(MaterialManagementConstants.GET_MM_WF_ANOMALY_OPEN_DETAILS);

			if (status.equalsIgnoreCase("DAYS_LESS_THAN_5")) {
				queryString.append(" and color='Green'");
			} else if (status.equalsIgnoreCase("DAYS_BETWEEN_5_AND_22")) {
				queryString.append(" and color='Yellow'");
			} else if (status.equalsIgnoreCase("DAYS_MORE_THAN_22")) {
				queryString.append(" and color='Red'");
			}

		}
		return queryString.toString();
	}
}

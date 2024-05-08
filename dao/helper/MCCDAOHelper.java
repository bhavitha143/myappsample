package com.bh.realtrack.dao.helper;

import org.springframework.stereotype.Component;

import com.bh.realtrack.util.MCCConstants;

/**
 * @author Shweta D. Sawant
 */
@Component
public class MCCDAOHelper {

	public static String getMCCChartPopupDetailsQuery(String chartType, String status, String subStatus) {
		StringBuilder queryString = new StringBuilder();
		if (chartType.equalsIgnoreCase("SUMMARY")) {
			queryString.append(MCCConstants.GET_MCC_DETAILS);
			if (status.equalsIgnoreCase("EXECUTED")) {
				queryString.append(" and status='Executed'");
			}
		} else if (chartType.equalsIgnoreCase("CHART")) {
			queryString.append(MCCConstants.GET_MCC_DETAILS);
			queryString.append(" and sub_system = ?");
			if (subStatus.equalsIgnoreCase("OPEN")) {
				queryString.append(" and status='Open'");
			} else if (subStatus.equalsIgnoreCase("EXECUTED")) {
				queryString.append(" and status='Executed'");
			}
		}
		return queryString.toString();
	}
}

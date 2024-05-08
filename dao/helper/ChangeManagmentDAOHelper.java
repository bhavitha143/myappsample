package com.bh.realtrack.dao.helper;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bh.realtrack.dto.ChangeSummaryRequestDTO;

/**
 * @author Anand Kumar
 *
 */
@Component
public class ChangeManagmentDAOHelper {

	private static Logger log = LoggerFactory.getLogger(ChangeManagmentDAOHelper.class.getName());

	public StringBuilder frameDynamicQuery(ChangeSummaryRequestDTO changeSummaryRequestDTO, StringBuilder queryString) {

		queryString.append(
				"and project_id in ( select project_master_id from rt_app.rt_adm_project where project_id = ? ) ");

		if (null != changeSummaryRequestDTO.getJobNumber()
				&& !"".equalsIgnoreCase(changeSummaryRequestDTO.getJobNumber())
				&& !"OVERALL".equalsIgnoreCase(changeSummaryRequestDTO.getJobNumber())) {
			queryString.append("  and sub_project like ? ");
		}

		if (null != changeSummaryRequestDTO.getPhase() && !"".equalsIgnoreCase(changeSummaryRequestDTO.getPhase())
				&& !"OVERALL".equalsIgnoreCase(changeSummaryRequestDTO.getPhase())) {

			if (null != changeSummaryRequestDTO.getEndOfShipDate()) {
				if ("Site Phase".equalsIgnoreCase(changeSummaryRequestDTO.getPhase())) {
					queryString.append(" and ECR_CREATION_DATE > ? ");
				} else {
					queryString.append(" and ECR_CREATION_DATE <= ? ");
				}
			}
		}

		if ("SUMMARY".equals(changeSummaryRequestDTO.getLevel())) {

			if ("ASSESSED_IMPACT".equals(changeSummaryRequestDTO.getChartType())) {

				if ("NO_IMPACT".equals(changeSummaryRequestDTO.getSubSection())) {

					queryString.append(" and assessed_impact = 'NO_IMPACT' ");

				} else if ("DELIVERY_IMPACT".equals(changeSummaryRequestDTO.getSubSection())) {

					queryString.append(" and assessed_impact = 'DELIVERY_IMPACT' ");

				} else if ("COST_IMPACT".equals(changeSummaryRequestDTO.getSubSection())) {

					queryString.append(" and assessed_impact = 'COST_IMPACT' ");

				} else if ("COST_DELIVERY_IMPACT".equals(changeSummaryRequestDTO.getSubSection())) {

					queryString.append(" and assessed_impact = 'COST_DELIVERY_IMPACT' ");

				}
			} else if ("AGING_ECR".equals(changeSummaryRequestDTO.getChartType())) {

				if ("7DAYS".equals(changeSummaryRequestDTO.getSubSection())) {

					queryString.append(" and aging = '7_DAYS' ");

				} else if ("30DAYS".equals(changeSummaryRequestDTO.getSubSection())) {

					queryString.append(" and aging = '30_DAYS' ");

				} else if ("31DAYS".equals(changeSummaryRequestDTO.getSubSection())) {

					queryString.append(" and aging = '31_DAYS' ");
				}
				
				queryString.append(" and ecr_code not in ( select distinct ecr_code from rt_app.rt_cat_change_request_actions reqAction where cm.project_id = reqAction.project_id ) ");
			}
		} else if ("ACTION".equals(changeSummaryRequestDTO.getLevel())) {

			if ("ASSESSED_IMPACT".equals(changeSummaryRequestDTO.getChartType())) {
				
				queryString.append(" and status != 'Completed' ");
				
				if ("NO_IMPACT".equals(changeSummaryRequestDTO.getSubSection())) {

					queryString.append(" and assessed_impact = 'NO_IMPACT' ");
				} else if ("DELIVERY_IMPACT".equals(changeSummaryRequestDTO.getSubSection())) {

					queryString.append(" and assessed_impact = 'DELIVERY_IMPACT' ");
				} else if ("COST_IMPACT".equals(changeSummaryRequestDTO.getSubSection())) {

					queryString.append(" and assessed_impact = 'COST_IMPACT' ");
				} else if ("COST_DELIVERY_IMPACT".equals(changeSummaryRequestDTO.getSubSection())) {

					queryString.append(" and assessed_impact = 'COST_DELIVERY_IMPACT' ");
				}

				if ("ALREADY_DUE".equals(changeSummaryRequestDTO.getDateRange())) {

					queryString.append(" and due_in_weeks = 'ALREADY_DUE' ");
				} else if ("0_1_WEEKS".equals(changeSummaryRequestDTO.getDateRange())) {

					queryString.append(" and due_in_weeks = '0_1_WEEKS' ");
				} else if ("1_2_WEEKS".equals(changeSummaryRequestDTO.getDateRange())) {

					queryString.append(" and due_in_weeks = '1_2_WEEKS' ");
				} else if ("2_3_WEEKS".equals(changeSummaryRequestDTO.getDateRange())) {

					queryString.append(" and due_in_weeks = '2_3_WEEKS' ");
				} else if ("3_4_WEEKS".equals(changeSummaryRequestDTO.getDateRange())) {

					queryString.append(" and due_in_weeks = '3_4_WEEKS' ");
				} else if ("4_5_WEEKS".equals(changeSummaryRequestDTO.getDateRange())) {

					queryString.append(" and due_in_weeks = '4_5_WEEKS' ");
				}

			} else if ("SAY_DO".equals(changeSummaryRequestDTO.getChartType())) {

				if ("COMPLETED_ON_TIME".equals(changeSummaryRequestDTO.getSubSection())) {

					queryString.append(" and say_do = 'COMPLETED_ON_TIME' ");

				} else if ("COMPLETED_LATE".equals(changeSummaryRequestDTO.getSubSection())) {

					queryString.append(" and say_do = 'COMPLETED_LATE' ");

				} else if ("OVER_DUE".equals(changeSummaryRequestDTO.getSubSection())) {

					queryString.append(" and say_do = 'OVER_DUE' ");
				}
			}

		}

		return queryString;
	}

	public void setDynamicParam(ChangeSummaryRequestDTO changeSummaryRequestDTO, PreparedStatement ps, int paramIndex)
			throws SQLException {

		paramIndex = paramIndex + 1;
		ps.setString(paramIndex, changeSummaryRequestDTO.getProjectId());

		paramIndex = paramIndex + 1;
		if (null != changeSummaryRequestDTO.getJobNumber()
				&& !"".equalsIgnoreCase(changeSummaryRequestDTO.getJobNumber())
				&& !"OVERALL".equalsIgnoreCase(changeSummaryRequestDTO.getJobNumber())) {
			ps.setString(paramIndex, changeSummaryRequestDTO.getJobNumber());
		}

		paramIndex = paramIndex + 1;
		if (null != changeSummaryRequestDTO.getPhase() && !"".equalsIgnoreCase(changeSummaryRequestDTO.getPhase())
				&& !"OVERALL".equalsIgnoreCase(changeSummaryRequestDTO.getPhase())) {

			if (null != changeSummaryRequestDTO.getEndOfShipDate()) {

				ps.setDate(paramIndex, java.sql.Date.valueOf(changeSummaryRequestDTO.getEndOfShipDate().toString()));

			}
		}
	}
}

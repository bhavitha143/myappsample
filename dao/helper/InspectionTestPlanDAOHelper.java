package com.bh.realtrack.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bh.realtrack.dto.CustomerInspectionTestPlanDTO;
import com.bh.realtrack.dto.InspectionConfiguratorDTO;
import com.bh.realtrack.dto.InspectionTestPlanDTO;
import com.bh.realtrack.util.InspectionTestPlanConstants;

/**
 * @author Anand Kumar
 *
 */
@Component
public class InspectionTestPlanDAOHelper {

	private static Logger log = LoggerFactory.getLogger(InspectionTestPlanDAOHelper.class.getName());

	SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
	SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
	SimpleDateFormat realTrackFormat = new SimpleDateFormat("dd-MMM-yyyy");

	/***
	 * This helper method is used to apply the filter conditions defined in the
	 * configuration tab
	 * 
	 * applyPeriodFilter -- if this flag is set to true then the Period filter
	 * condition is applied
	 * 
	 * addAndCondition -- if this flag is set to true then 'add' is added to the
	 * query and then the condition is added
	 * 
	 * Customer, Tp and End User filter has Empty check. So when pushing the data
	 * from stage table to target table the Null or Empty value will be coded as
	 * Empty and if Empty filter is selected in the Configuration then Empty value
	 * will be fetched
	 * 
	 */
	public void applyConfiguratorFilterCondition(InspectionConfiguratorDTO inspectionConfiguratorDTO,
			ArrayList<Object> queryData, StringBuilder inspectionDetails, boolean applyPeriodFilter,
			boolean addAndCondition) {

		String[] customerFilter = null;
		String[] tpFilter = null;
		String[] endUserFilter = null;
		String[] qcpPageFilter = null;
		boolean firstElement = true;
		boolean addORCondition = false;

		/***
		 * If the customer filter has been selected in the configuration then split the
		 * value using InspectionTestPlanConstants.COMMA_SEPERATOR separator and add
		 * that in the query condition
		 */
		if (null != inspectionConfiguratorDTO) {

			if (validateFilterCondition(inspectionConfiguratorDTO.getCustomerDataFilter())
					|| validateFilterCondition(inspectionConfiguratorDTO.getEndUserDataFilter())
					|| validateFilterCondition(inspectionConfiguratorDTO.getTpDataFilter()))

			{
				/**
				 * Check if the and condition has to be added to the query or it is already
				 * added
				 */
				if (addAndCondition) {
					inspectionDetails.append(" and ( ");
				}
				/***
				 * For the next query condition 'and' needs to be added so set the value as true
				 * so 'and' will be added in the next query condition
				 * 
				 */
				else {
					inspectionDetails.append("  ( ");
					addAndCondition = true;
				}

				if (validateFilterCondition(inspectionConfiguratorDTO.getCustomerDataFilter())) {

					if (addORCondition) {
						inspectionDetails.append(" or ( ");
					} else {
						inspectionDetails.append("  ( ");
						addORCondition = true;
					}
					/***
					 * Split the customer filter value using
					 * InspectionTestPlanConstants.COMMA_SEPERATOR separator
					 */
					customerFilter = inspectionConfiguratorDTO.getCustomerDataFilter()
							.split(InspectionTestPlanConstants.COMMA_SEPERATOR);

					for (String customerValue : customerFilter) {

						/***
						 * For first element don't add 'or' condition
						 */
						if (firstElement) {
							inspectionDetails.append("customer = ? ");
						} else {
							inspectionDetails.append(" or customer = ? ");
						}
						firstElement = false;
						queryData.add(customerValue);
					}
					inspectionDetails.append(" ) ");
				}

				firstElement = true;

				/***
				 * If the TP filter has been selected in the configuration then split the value
				 * using InspectionTestPlanConstants.COMMA_SEPERATOR separator and add that in
				 * the query condition
				 */
				if (validateFilterCondition(inspectionConfiguratorDTO.getTpDataFilter())) {

					if (addORCondition) {
						inspectionDetails.append(" or ( ");
					} else {
						inspectionDetails.append("  ( ");
						addORCondition = true;
					}

					/***
					 * Split the TP filter value using InspectionTestPlanConstants.COMMA_SEPERATOR
					 * separator
					 */
					tpFilter = inspectionConfiguratorDTO.getTpDataFilter()
							.split(InspectionTestPlanConstants.COMMA_SEPERATOR);

					for (String tpValue : tpFilter) {

						/***
						 * For first element don't add 'or' condition
						 */
						if (firstElement) {
							inspectionDetails.append("tp = ?  ");
						} else {
							inspectionDetails.append(" or tp = ? ");
						}
						queryData.add(tpValue);
						firstElement = false;
					}
					inspectionDetails.append(" ) ");
				}

				/***
				 * If the End User filter has been selected in the configuration then split the
				 * value using InspectionTestPlanConstants.COMMA_SEPERATOR separator and add
				 * that in the query condition
				 */
				firstElement = true;
				if (validateFilterCondition(inspectionConfiguratorDTO.getEndUserDataFilter())) {

					if (addORCondition) {
						inspectionDetails.append(" or ( ");
					} else {
						inspectionDetails.append("  ( ");
						addORCondition = true;
					}

					/***
					 * Split the END USER filter value using
					 * InspectionTestPlanConstants.COMMA_SEPERATOR separator
					 */
					endUserFilter = inspectionConfiguratorDTO.getEndUserDataFilter()
							.split(InspectionTestPlanConstants.COMMA_SEPERATOR);

					for (String endUserValue : endUserFilter) {

						/***
						 * For first element don't add 'or' condition
						 */
						if (firstElement) {
							inspectionDetails.append("end_user = ?  ");
						} else {
							inspectionDetails.append(" or end_user = ? ");
						}

						queryData.add(endUserValue);
						firstElement = false;
					}
					inspectionDetails.append(" ) ");
				}

				inspectionDetails.append(" ) ");
			}

			firstElement = true;

			/***
			 * If the QCP page name filter has been selected in the configuration then split
			 * the value using InspectionTestPlanConstants.COMMA_SEPERATOR separator and add
			 * that in the query condition
			 * 
			 * As per the business, the selected QCP page name should not be fetched in
			 * External view so 'not in' filter is added
			 * 
			 */
			if (null != inspectionConfiguratorDTO.getQcpPageNameFilter() && !InspectionTestPlanConstants.EMPTY_STRING
					.equalsIgnoreCase(inspectionConfiguratorDTO.getQcpPageNameFilter())) {
				qcpPageFilter = inspectionConfiguratorDTO.getQcpPageNameFilter()
						.split(InspectionTestPlanConstants.COMMA_SEPERATOR);

				if (addAndCondition) {
					inspectionDetails.append(" and ( ");
				} else {
					inspectionDetails.append("  ( ");
					addAndCondition = true;
				}

				for (String qcpPageValue : qcpPageFilter) {
					if (firstElement) {
						inspectionDetails.append(" COALESCE (qcp_page_name , 'EMPTY' ) != ?   ");
					} else {
						inspectionDetails.append(" and COALESCE (qcp_page_name , 'EMPTY' ) != ? ");
					}

					queryData.add(qcpPageValue);
					firstElement = false;
				}
				inspectionDetails.append(" ) ");
			}

			/***
			 * If the period filter needs to be added then the calc_exp_inspection_date
			 * value is checked such that it is in the selected period range of insert date
			 * 
			 */
			if (applyPeriodFilter && null != inspectionConfiguratorDTO.getPeriodFilter()
					&& !InspectionTestPlanConstants.ALL_STRING
							.equalsIgnoreCase(inspectionConfiguratorDTO.getPeriodFilter())
					&& !InspectionTestPlanConstants.EMPTY_STRING
							.equalsIgnoreCase(inspectionConfiguratorDTO.getPeriodFilter())) {

				if (addAndCondition) {
					inspectionDetails.append(" and ( ");
				} else {
					inspectionDetails.append("  ( ");
				}
				inspectionDetails.append(" calc_exp_inspection_date_ext <=  now()  + interval '");
				inspectionDetails.append(Long.valueOf(inspectionConfiguratorDTO.getPeriodFilter()).intValue());
				inspectionDetails.append(" DAY'");
				inspectionDetails.append("  ) ");
			}
		}

	}

	public void applyExternalConfiguratorFilterCondition(InspectionConfiguratorDTO inspectionConfiguratorDTO,
			ArrayList<Object> queryData, StringBuilder inspectionDetails, boolean applyPeriodFilter,
			boolean addAndCondition) {

		String[] customerFilter = null;
		String[] tpFilter = null;
		String[] endUserFilter = null;
		String[] qcpPageFilter = null;
		boolean firstElement = true;
		boolean addORCondition = false;

		/***
		 * If the customer filter has been selected in the configuration then split the
		 * value using InspectionTestPlanConstants.COMMA_SEPERATOR separator and add
		 * that in the query condition
		 */
		if (null != inspectionConfiguratorDTO) {

			if (validateFilterCondition(inspectionConfiguratorDTO.getCustomerDataFilter())
					|| validateFilterCondition(inspectionConfiguratorDTO.getEndUserDataFilter())
					|| validateFilterCondition(inspectionConfiguratorDTO.getTpDataFilter()))

			{
				/**
				 * Check if the and condition has to be added to the query or it is already
				 * added
				 */
				if (addAndCondition) {
					inspectionDetails.append(" and ( ");
				}
				/***
				 * For the next query condition 'and' needs to be added so set the value as true
				 * so 'and' will be added in the next query condition
				 * 
				 */
				else {
					inspectionDetails.append("  ( ");
					addAndCondition = true;
				}

				if (validateFilterCondition(inspectionConfiguratorDTO.getCustomerDataFilter())) {

					if (addORCondition) {
						inspectionDetails.append(" or ( ");
					} else {
						inspectionDetails.append("  ( ");
						addORCondition = true;
					}
					/***
					 * Split the customer filter value using
					 * InspectionTestPlanConstants.COMMA_SEPERATOR separator
					 */
					customerFilter = inspectionConfiguratorDTO.getCustomerDataFilter()
							.split(InspectionTestPlanConstants.COMMA_SEPERATOR);

					for (String customerValue : customerFilter) {

						/***
						 * For first element don't add 'or' condition
						 */
						if (firstElement) {
							inspectionDetails.append("report_data.customer = ? ");
						} else {
							inspectionDetails.append(" or report_data.customer = ? ");
						}
						firstElement = false;
						queryData.add(customerValue);
					}
					inspectionDetails.append(" ) ");
				}

				firstElement = true;

				/***
				 * If the TP filter has been selected in the configuration then split the value
				 * using InspectionTestPlanConstants.COMMA_SEPERATOR separator and add that in
				 * the query condition
				 */
				if (validateFilterCondition(inspectionConfiguratorDTO.getTpDataFilter())) {

					if (addORCondition) {
						inspectionDetails.append(" or ( ");
					} else {
						inspectionDetails.append("  ( ");
						addORCondition = true;
					}

					/***
					 * Split the TP filter value using InspectionTestPlanConstants.COMMA_SEPERATOR
					 * separator
					 */
					tpFilter = inspectionConfiguratorDTO.getTpDataFilter()
							.split(InspectionTestPlanConstants.COMMA_SEPERATOR);

					for (String tpValue : tpFilter) {

						/***
						 * For first element don't add 'or' condition
						 */
						if (firstElement) {
							inspectionDetails.append("report_data.tp = ?  ");
						} else {
							inspectionDetails.append(" or report_data.tp = ? ");
						}
						queryData.add(tpValue);
						firstElement = false;
					}
					inspectionDetails.append(" ) ");
				}

				/***
				 * If the End User filter has been selected in the configuration then split the
				 * value using InspectionTestPlanConstants.COMMA_SEPERATOR separator and add
				 * that in the query condition
				 */
				firstElement = true;
				if (validateFilterCondition(inspectionConfiguratorDTO.getEndUserDataFilter())) {

					if (addORCondition) {
						inspectionDetails.append(" or ( ");
					} else {
						inspectionDetails.append("  ( ");
						addORCondition = true;
					}

					/***
					 * Split the END USER filter value using
					 * InspectionTestPlanConstants.COMMA_SEPERATOR separator
					 */
					endUserFilter = inspectionConfiguratorDTO.getEndUserDataFilter()
							.split(InspectionTestPlanConstants.COMMA_SEPERATOR);

					for (String endUserValue : endUserFilter) {

						/***
						 * For first element don't add 'or' condition
						 */
						if (firstElement) {
							inspectionDetails.append("report_data.end_user = ?  ");
						} else {
							inspectionDetails.append(" or report_data.end_user = ? ");
						}

						queryData.add(endUserValue);
						firstElement = false;
					}
					inspectionDetails.append(" ) ");
				}

				inspectionDetails.append(" ) ");
			}

			firstElement = true;

			/***
			 * If the QCP page name filter has been selected in the configuration then split
			 * the value using InspectionTestPlanConstants.COMMA_SEPERATOR separator and add
			 * that in the query condition
			 * 
			 * As per the business, the selected QCP page name should not be fetched in
			 * External view so 'not in' filter is added
			 * 
			 */
			if (null != inspectionConfiguratorDTO.getQcpPageNameFilter() && !InspectionTestPlanConstants.EMPTY_STRING
					.equalsIgnoreCase(inspectionConfiguratorDTO.getQcpPageNameFilter())) {
				qcpPageFilter = inspectionConfiguratorDTO.getQcpPageNameFilter()
						.split(InspectionTestPlanConstants.COMMA_SEPERATOR);

				if (addAndCondition) {
					inspectionDetails.append(" and ( ");
				} else {
					inspectionDetails.append("  ( ");
					addAndCondition = true;
				}

				for (String qcpPageValue : qcpPageFilter) {
					if (firstElement) {
						inspectionDetails.append(" COALESCE (report_data.qcp_page_name , 'EMPTY' ) != ?   ");
					} else {
						inspectionDetails.append(" and COALESCE (report_data.qcp_page_name , 'EMPTY' ) != ? ");
					}

					queryData.add(qcpPageValue);
					firstElement = false;
				}
				inspectionDetails.append(" ) ");
			}

			/***
			 * If the period filter needs to be added then the calc_exp_inspection_date
			 * value is checked such that it is in the selected period range of insert date
			 * 
			 */
			if (applyPeriodFilter && null != inspectionConfiguratorDTO.getPeriodFilter()
					&& !InspectionTestPlanConstants.ALL_STRING
							.equalsIgnoreCase(inspectionConfiguratorDTO.getPeriodFilter())
					&& !InspectionTestPlanConstants.EMPTY_STRING
							.equalsIgnoreCase(inspectionConfiguratorDTO.getPeriodFilter())) {

				if (addAndCondition) {
					inspectionDetails.append(" and ( ");
				} else {
					inspectionDetails.append("  ( ");
				}
				inspectionDetails.append(" report_data.calc_exp_inspection_date_ext <=  now()  + interval '");
				inspectionDetails.append(Long.valueOf(inspectionConfiguratorDTO.getPeriodFilter()).intValue());
				inspectionDetails.append(" DAY'");
				inspectionDetails.append("  ) ");
			}
		}

	}

	/***
	 * This helper method is used to populate the inspection test plan details
	 * fetched from DB to DTO
	 * 
	 */
	public void populateInspectionTestPlanDetails(final ResultSet rs, List<InspectionTestPlanDTO> list)
			throws SQLException {
		while (rs.next()) {

			InspectionTestPlanDTO inspectionTestPlanDTO = new InspectionTestPlanDTO();
			inspectionTestPlanDTO.setOmDescription(rs.getString("om_description"));
			inspectionTestPlanDTO.setQcpPageName(rs.getString("qcp_page_name"));
			inspectionTestPlanDTO.setCostingProject(rs.getString("costing_project"));
			inspectionTestPlanDTO.setFunctionalUnit(rs.getString("functional_unit"));
			inspectionTestPlanDTO.setFunctionalUnitDescription(rs.getString("fucntional_unit_description"));
			inspectionTestPlanDTO.setPeiCode(rs.getString("pei_code"));
			inspectionTestPlanDTO.setLongDummyCode(rs.getString("long_dummy_code"));
			inspectionTestPlanDTO.setItemCode(rs.getString("item_code"));
			inspectionTestPlanDTO.setItemDescription(rs.getString("item_description"));
			inspectionTestPlanDTO.setQcpDoc(rs.getString("qcp_doc"));
			inspectionTestPlanDTO.setRevQCP(rs.getString("qcp_rev"));
			inspectionTestPlanDTO.setRequirementId(rs.getString("requirement_id"));
			inspectionTestPlanDTO.setRequirementDescription(rs.getString("requirement_description"));
			inspectionTestPlanDTO.setStatus(rs.getString("status"));
			inspectionTestPlanDTO.setRefDocs(rs.getString("ref_docs"));
			inspectionTestPlanDTO.setProcedure(rs.getString("procedure_docs"));
			inspectionTestPlanDTO.setAcceptance(rs.getString("acceptance"));
			inspectionTestPlanDTO.setGe(rs.getString("ge"));
			inspectionTestPlanDTO.setCustomer(rs.getString("customer"));

			/**
			 * Customer, Tp and End User filter has Empty check. So when pushing the data
			 * from stage table to target table the Null or Empty value will be coded as
			 * Empty and if Empty filter is selected in the Configuration then Empty value
			 * will be fetched
			 * 
			 * So if the CUSTOMER, TP or END User value is EMPTY it will be set as NULL and
			 * displayed as NULL in the UI and download file
			 * 
			 */
			if (null != inspectionTestPlanDTO.getCustomer()
					&& InspectionTestPlanConstants.VALUE_EMPTY.equalsIgnoreCase(inspectionTestPlanDTO.getCustomer())) {
				inspectionTestPlanDTO.setCustomer(null);
			}
			inspectionTestPlanDTO.setEndUser(rs.getString("end_user"));
			if (null != inspectionTestPlanDTO.getEndUser()
					&& InspectionTestPlanConstants.VALUE_EMPTY.equalsIgnoreCase(inspectionTestPlanDTO.getEndUser())) {
				inspectionTestPlanDTO.setEndUser(null);
			}
			inspectionTestPlanDTO.setTp(rs.getString("tp"));
			if (null != inspectionTestPlanDTO.getTp()
					&& InspectionTestPlanConstants.VALUE_EMPTY.equalsIgnoreCase(inspectionTestPlanDTO.getTp())) {
				inspectionTestPlanDTO.setTp(null);
			}
			inspectionTestPlanDTO.setSupplier(rs.getString("supplier"));
			inspectionTestPlanDTO.setSupplierLocation(rs.getString("supplier_location"));
			inspectionTestPlanDTO.setSubSupplierLocation(rs.getString("sub_supplier_location"));
			inspectionTestPlanDTO.setPoWip(rs.getString("po_wip"));
			inspectionTestPlanDTO.setPoLine(rs.getString("po_line"));
			inspectionTestPlanDTO.setExpectedInspectionStartDate(rs.getString("expected_inspection_start_dt"));
			inspectionTestPlanDTO.setCustNotificationNumber(rs.getString("customer_notification_number"));
			inspectionTestPlanDTO.setNotificationNumber(rs.getString("notification_number"));
			inspectionTestPlanDTO.setNotificationStatus(rs.getString("notification_status"));
			inspectionTestPlanDTO.setNotificationRevision(rs.getString("notification_revision"));
			inspectionTestPlanDTO.setInspectionDate(rs.getString("inspection_date"));
			inspectionTestPlanDTO.setInspectionDuration(rs.getString("inspection_duration"));
			inspectionTestPlanDTO.setInspectionType(rs.getString("inspection_type"));
			inspectionTestPlanDTO.setNotificationToCustomer(formateTimeStamp(rs.getString("notification_to_customer")));
			inspectionTestPlanDTO.setRc1Reference(rs.getString("rc1_reference"));
			inspectionTestPlanDTO.setTestResult(rs.getString("test_results"));
			inspectionTestPlanDTO.setInspectionNotes(rs.getString("inspection_notes"));
			inspectionTestPlanDTO.setRtiStatus(rs.getString("rti_status"));
			inspectionTestPlanDTO.setRtiStatusColor(rs.getString("rti_status_color"));
			inspectionTestPlanDTO.setxAxisColor(rs.getString("x_axis_color"));
			inspectionTestPlanDTO.setyAxisColor(rs.getString("y_axis_color"));
			inspectionTestPlanDTO.setyAxisStatus(rs.getString("yaxis_status"));
			inspectionTestPlanDTO.setCustomerStatus(rs.getString("customer_status"));
			list.add(inspectionTestPlanDTO);
		}
	}

	/***
	 * This helper method is used to populate the inspection test plan details
	 * fetched from DB to DTO
	 * 
	 */
	public void populateExternalInspectionTestPlanDetails(final ResultSet rs, List<InspectionTestPlanDTO> list)
			throws SQLException {
		while (rs.next()) {

			InspectionTestPlanDTO inspectionTestPlanDTO = new InspectionTestPlanDTO();
			inspectionTestPlanDTO.setOmDescription(rs.getString("om_description"));
			inspectionTestPlanDTO.setQcpPageName(rs.getString("qcp_page_name"));
			inspectionTestPlanDTO.setCostingProject(rs.getString("costing_project"));
			inspectionTestPlanDTO.setFunctionalUnit(rs.getString("functional_unit"));
			inspectionTestPlanDTO.setFunctionalUnitDescription(rs.getString("fucntional_unit_description"));
			inspectionTestPlanDTO.setPeiCode(rs.getString("pei_code"));
			inspectionTestPlanDTO.setLongDummyCode(rs.getString("long_dummy_code"));
			inspectionTestPlanDTO.setItemCode(rs.getString("item_code"));
			inspectionTestPlanDTO.setItemDescription(rs.getString("item_description"));
			inspectionTestPlanDTO.setQcpDoc(rs.getString("qcp_doc"));
			inspectionTestPlanDTO.setRevQCP(rs.getString("qcp_rev"));
			inspectionTestPlanDTO.setRequirementId(rs.getString("requirement_id"));
			inspectionTestPlanDTO.setRequirementDescription(rs.getString("requirement_description"));
			inspectionTestPlanDTO.setStatus(rs.getString("status"));
			inspectionTestPlanDTO.setRefDocs(rs.getString("ref_docs"));
			inspectionTestPlanDTO.setProcedure(rs.getString("procedure_docs"));
			inspectionTestPlanDTO.setAcceptance(rs.getString("acceptance"));
			inspectionTestPlanDTO.setGe(rs.getString("ge"));
			inspectionTestPlanDTO.setCustomer(rs.getString("customer"));

			/**
			 * Customer, Tp and End User filter has Empty check. So when pushing the data
			 * from stage table to target table the Null or Empty value will be coded as
			 * Empty and if Empty filter is selected in the Configuration then Empty value
			 * will be fetched
			 * 
			 * So if the CUSTOMER, TP or END User value is EMPTY it will be set as NULL and
			 * displayed as NULL in the UI and download file
			 * 
			 */
			if (null != inspectionTestPlanDTO.getCustomer()
					&& InspectionTestPlanConstants.VALUE_EMPTY.equalsIgnoreCase(inspectionTestPlanDTO.getCustomer())) {
				inspectionTestPlanDTO.setCustomer(null);
			}
			inspectionTestPlanDTO.setEndUser(rs.getString("end_user"));
			if (null != inspectionTestPlanDTO.getEndUser()
					&& InspectionTestPlanConstants.VALUE_EMPTY.equalsIgnoreCase(inspectionTestPlanDTO.getEndUser())) {
				inspectionTestPlanDTO.setEndUser(null);
			}
			inspectionTestPlanDTO.setTp(rs.getString("tp"));
			if (null != inspectionTestPlanDTO.getTp()
					&& InspectionTestPlanConstants.VALUE_EMPTY.equalsIgnoreCase(inspectionTestPlanDTO.getTp())) {
				inspectionTestPlanDTO.setTp(null);
			}
			inspectionTestPlanDTO.setSupplier(rs.getString("supplier"));
			inspectionTestPlanDTO.setSupplierLocation(rs.getString("supplier_location"));
			inspectionTestPlanDTO.setSubSupplierLocation(rs.getString("sub_supplier_location"));
			inspectionTestPlanDTO.setPoWip(rs.getString("po_wip"));
			inspectionTestPlanDTO.setPoLine(rs.getString("po_line"));
			inspectionTestPlanDTO.setExpectedInspectionStartDate(rs.getString("expected_inspection_start_dt"));
			inspectionTestPlanDTO.setCustNotificationNumber(rs.getString("customer_notification_number_ext"));
			inspectionTestPlanDTO.setNotificationNumber(rs.getString("notification_number"));
			inspectionTestPlanDTO.setNotificationStatus(rs.getString("notification_status_ext"));
			inspectionTestPlanDTO.setNotificationRevision(rs.getString("notification_revision_ext"));
			inspectionTestPlanDTO.setInspectionDate(rs.getString("inspection_date"));
			inspectionTestPlanDTO.setInspectionDuration(rs.getString("inspection_duration"));
			inspectionTestPlanDTO.setInspectionType(rs.getString("inspection_type"));
			inspectionTestPlanDTO.setNotificationToCustomer(formateTimeStamp(rs.getString("notification_to_customer")));
			inspectionTestPlanDTO.setRc1Reference(rs.getString("rc1_reference"));
			inspectionTestPlanDTO.setTestResult(rs.getString("test_results"));
			inspectionTestPlanDTO.setInspectionNotes(rs.getString("inspection_notes"));
			inspectionTestPlanDTO.setRtiStatus(rs.getString("rti_status"));
			inspectionTestPlanDTO.setRtiStatusColor(rs.getString("rti_status_color"));
			inspectionTestPlanDTO.setxAxisColor(rs.getString("x_axis_color"));
			inspectionTestPlanDTO.setyAxisColor(rs.getString("y_axis_color"));
			inspectionTestPlanDTO.setyAxisStatus(rs.getString("y_axis_status_ext"));
			inspectionTestPlanDTO.setCustomerStatus(rs.getString("customer_status"));
			list.add(inspectionTestPlanDTO);
		}
	}

	/***
	 * This helper method is used to populate the customer inspection test plan
	 * details fetched from DB to DTO
	 * 
	 */
	public void populateCustomerInspectionTestPlanDetails(final ResultSet rs, List<CustomerInspectionTestPlanDTO> list)
			throws SQLException {
		while (rs.next()) {
			CustomerInspectionTestPlanDTO inspectionTestPlanDTO = new CustomerInspectionTestPlanDTO();

			inspectionTestPlanDTO.setQcpPageName(rs.getString("qcp_page_name"));
			inspectionTestPlanDTO.setCostingProject(rs.getString("costing_project"));
			inspectionTestPlanDTO.setQcpDoc(rs.getString("qcp_doc"));
			inspectionTestPlanDTO.setRevQCP(rs.getString("qcp_rev"));
			inspectionTestPlanDTO.setItemDescription(rs.getString("item_description"));
			inspectionTestPlanDTO.setRequirementId(rs.getString("requirement_id"));
			inspectionTestPlanDTO.setRequirementDescription(rs.getString("requirement_description"));
			inspectionTestPlanDTO.setRefDocs(rs.getString("ref_docs"));
			inspectionTestPlanDTO.setProcedure(rs.getString("procedure_docs"));
			inspectionTestPlanDTO.setAcceptance(rs.getString("acceptance"));
			inspectionTestPlanDTO.setGe(rs.getString("ge"));
			inspectionTestPlanDTO.setCustomer(rs.getString("customer"));

			/**
			 * Customer, Tp and End User filter has Empty check. So when pushing the data
			 * from stage table to target table the Null or Empty value will be coded as
			 * Empty and if Empty filter is selected in the Configuration then Empty value
			 * will be fetched
			 * 
			 * So if the CUSTOMER, TP or END User value is EMPTY it will be set as NULL and
			 * displayed as NULL in the UI and download file
			 * 
			 */
			if (null != inspectionTestPlanDTO.getCustomer()
					&& InspectionTestPlanConstants.VALUE_EMPTY.equalsIgnoreCase(inspectionTestPlanDTO.getCustomer())) {
				inspectionTestPlanDTO.setCustomer(null);
			}
			inspectionTestPlanDTO.setEndUser(rs.getString("end_user"));
			if (null != inspectionTestPlanDTO.getEndUser()
					&& InspectionTestPlanConstants.VALUE_EMPTY.equalsIgnoreCase(inspectionTestPlanDTO.getEndUser())) {
				inspectionTestPlanDTO.setEndUser(null);
			}
			inspectionTestPlanDTO.setTp(rs.getString("tp"));
			if (null != inspectionTestPlanDTO.getTp()
					&& InspectionTestPlanConstants.VALUE_EMPTY.equalsIgnoreCase(inspectionTestPlanDTO.getTp())) {
				inspectionTestPlanDTO.setTp(null);
			}
			inspectionTestPlanDTO.setSupplier(rs.getString("supplier"));
			inspectionTestPlanDTO.setSupplierLocation(rs.getString("supplier_location"));
			inspectionTestPlanDTO.setSubSupplierLocation(rs.getString("sub_supplier_location"));
			inspectionTestPlanDTO
					.setExpectedInspectionStartDate(formateData(rs.getString("expected_inspection_start_dt_ext")));
			inspectionTestPlanDTO.setCustNotificationNumber(rs.getString("customer_notification_number_ext"));
			inspectionTestPlanDTO.setNotificationStatus(rs.getString("notification_status_ext"));
			inspectionTestPlanDTO.setNotificationRevision(rs.getString("notification_revision_ext"));
			inspectionTestPlanDTO.setInspectionDate(formateData(rs.getString("inspection_date_ext")));
			inspectionTestPlanDTO.setRtiStatus(rs.getString("rti_status"));
			inspectionTestPlanDTO.setRtiStatusColor(rs.getString("rti_status_color"));
			inspectionTestPlanDTO.setxAxisColor(rs.getString("x_axis_color"));
			inspectionTestPlanDTO.setyAxisColor(rs.getString("y_axis_color"));
			inspectionTestPlanDTO.setyAxisStatus(rs.getString("yaxis_status"));
			inspectionTestPlanDTO.setCustomerStatus(rs.getString("customer_status"));
			list.add(inspectionTestPlanDTO);
		}
	}

	/***
	 * This helper method is used to populate the customer inspection test plan
	 * details fetched from DB to DTO
	 * 
	 */
	public String downloadCustomerInspectionTestPlanDetails(final ResultSet rs, StringBuilder resultData)
			throws SQLException {
		String customer = null;
		String endUser = null;
		String tp = null;
		String itemDescription = null;
		String requirementDescription = null;
		String acceptance = null;
		String refDoc = null;
		String procedureDoc = null;
		String qcpPageName = null;
		String costingProject = null;
		String qcpDoc = null;
		String qcpRev = null;
		String requirementId = null;
		String supplier = null;
		String supplierLocation = null;
		String subSupplierLocation = null;
		String customerNotificationNo = null;
		String notificationRevision = null;
		String notificationStatus = null;

		while (rs.next()) {

			qcpPageName = rs.getString("qcp_page_name");
			if (null != qcpPageName) {
				resultData.append(qcpPageName.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			costingProject = rs.getString("costing_project");
			if (null != costingProject) {
				resultData.append(costingProject.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			qcpDoc = rs.getString("qcp_doc");
			if (null != qcpDoc) {
				resultData.append(qcpDoc.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			qcpRev = rs.getString("qcp_rev");
			if (null != qcpRev) {
				resultData.append(qcpRev.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			itemDescription = rs.getString("item_description");
			if (null != itemDescription) {
				resultData.append(itemDescription.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			requirementId = rs.getString("requirement_id");
			if (null != requirementId) {
				requirementId = requirementId.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE);
				requirementId = InspectionTestPlanConstants.TAB_SPACE + requirementId;
				resultData.append(requirementId);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			requirementDescription = rs.getString("requirement_description");
			if (null != requirementDescription) {
				resultData.append(requirementDescription.replaceAll(
						InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER, InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			refDoc = rs.getString("ref_docs");
			if (null != refDoc) {
				resultData.append(refDoc.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			procedureDoc = rs.getString("procedure_docs");
			if (null != procedureDoc) {
				resultData.append(procedureDoc.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			acceptance = rs.getString("acceptance");
			if (null != acceptance) {
				resultData.append(acceptance.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			if (null != rs.getString("ge")) {
				resultData.append(rs.getString("ge"));
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			customer = rs.getString("customer");
			endUser = rs.getString("end_user");
			tp = rs.getString("tp");

			if (null != customer && !InspectionTestPlanConstants.VALUE_EMPTY.equalsIgnoreCase(customer)) {
				resultData.append(customer);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
			if (null != endUser && !InspectionTestPlanConstants.VALUE_EMPTY.equalsIgnoreCase(endUser)) {
				resultData.append(endUser);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
			if (null != tp && !InspectionTestPlanConstants.VALUE_EMPTY.equalsIgnoreCase(tp)) {
				resultData.append(tp);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			supplier = rs.getString("supplier");
			if (null != supplier) {
				resultData.append(supplier.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			supplierLocation = rs.getString("supplier_location");
			if (null != supplierLocation) {
				resultData.append(supplierLocation.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			subSupplierLocation = rs.getString("sub_supplier_location");
			if (null != subSupplierLocation) {
				resultData.append(subSupplierLocation.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			if (null != rs.getString("expected_inspection_start_dt_ext")) {
				resultData.append(rs.getString("expected_inspection_start_dt_ext"));
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			customerNotificationNo = rs.getString("customer_notification_number_ext");
			if (null != customerNotificationNo) {
				resultData.append(customerNotificationNo.replaceAll(
						InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER, InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			notificationStatus = rs.getString("notification_status_ext");
			if (null != notificationStatus) {
				resultData.append(notificationStatus.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			notificationRevision = rs.getString("notification_revision_ext");
			if (null != notificationRevision) {
				resultData.append(notificationRevision.replaceAll(
						InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER, InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			if (null != rs.getString("inspection_date_ext")) {
				resultData.append(rs.getString("inspection_date_ext"));
			}
			resultData.append(InspectionTestPlanConstants.END_OF_LINE_DELIMITER);

		}
		return resultData.toString();
	}

	/***
	 * This helper method is used to populate the customer inspection test plan
	 * details fetched from DB to DTO
	 * 
	 */
	public void frameCustomerDownloadFileHeader(StringBuilder resultData) {

		resultData.append("Qcp Page Name");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Costing Project");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("QCP Doc");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Rev QCP");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Item Description");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Requirement ID");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Requirement Description");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Ref Docs");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Procedure");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Acceptance");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("BH");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Customer");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("End User");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("TP");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Supplier");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Supplier Location");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Sub Supplier Location");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Expected Inspection Start Date");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Customer Notification Number");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Notification Status");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Notification Revision");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Inspection Date");
		resultData.append(InspectionTestPlanConstants.END_OF_LINE_DELIMITER);

	}

	/***
	 * This helper method is used to populate the customer inspection test plan
	 * details fetched from DB to DTO
	 * 
	 */
	public void frameIAPDownloadFileHeader(StringBuilder resultData) {

		resultData.append("OM Description");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Qcp Page Name");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Costing Project");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Functional Unit");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Functional Unit Description");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("PEI Code");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Long Dummy Code");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Item Code");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Item Description");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("QCP Doc");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Rev QCP");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Requirement ID");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Requirement Description");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Status");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Ref Docs");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Procedure");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Acceptance");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("BH");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Customer");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("End User");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("TP");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Supplier");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Supplier Location");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Sub Supplier Location");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("PO/WIP #");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("PO Line");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Expected Inspection Start Date");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Customer Notification Number");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Notification Number");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Notification Status");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Notification Revision");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Inspection Date");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Inspection Duration");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Inspection Type");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Notification To Customer");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Rc1 Reference");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Test Results");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Inspection Notes");

		resultData.append(InspectionTestPlanConstants.END_OF_LINE_DELIMITER);

	}

	/***
	 * This helper method is used to populate the customer inspection test plan
	 * details fetched from DB to DTO
	 * 
	 */
	public void frameExternalIAPDownloadFileHeader(StringBuilder resultData) {

		resultData.append("OM Description");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Qcp Page Name");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Costing Project");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Functional Unit");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Functional Unit Description");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("PEI Code");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Long Dummy Code");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Item Code");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Item Description");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("QCP Doc");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Rev QCP");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Requirement ID");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Requirement Description");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Status");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Ref Docs");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Procedure");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Acceptance");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("BH");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Customer");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("End User");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("TP");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Supplier");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Supplier Location");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Sub Supplier Location");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("PO/WIP #");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("PO Line");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Expected Inspection Start Date");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Customer Notification Number");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Notification Number");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Notification Status");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Notification Revision");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Inspection Date");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Inspection Duration");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Inspection Type");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Notification To Customer");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Rc1 Reference");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Test Results");
		resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);
		resultData.append("Inspection Notes");

		resultData.append(InspectionTestPlanConstants.END_OF_LINE_DELIMITER);

	}

	private boolean validateFilterCondition(String filterCondition) {
		boolean validFilter = true;

		if (null == filterCondition || InspectionTestPlanConstants.EMPTY_STRING.equalsIgnoreCase(filterCondition)) {
			validFilter = false;
		}

		return validFilter;
	}

	/***
	 * This helper method is used to populate the inspection test plan details
	 * fetched from DB to DTO
	 * 
	 */
	public void downloadInspectionTestPlanDetails(final ResultSet rs, StringBuilder resultData) throws SQLException {
		while (rs.next()) {

			String customer = null;
			String endUser = null;
			String tp = null;
			String omDescription = null;
			String itemDescription = null;
			String functionDescription = null;
			String requirementDescription = null;
			String inspectionNotes = null;
			String testResult = null;
			String rc1Reference = null;
			String customerNotification = null;
			String acceptance = null;
			String inspectionType = null;
			String refDoc = null;
			String procedureDoc = null;
			String qcpPageName = null;
			String costingProject = null;
			String functionalUnit = null;
			String peiCode = null;
			String longDummyCode = null;
			String itemCode = null;
			String qcpDoc = null;
			String qcpRev = null;
			String requirementId = null;
			String status = null;
			String supplier = null;
			String supplierLocation = null;
			String subSupplierLocation = null;
			String poWip = null;
			String poLine = null;
			String customerNotificationNo = null;
			String notificationNo = null;
			String notificationStatus = null;
			String notificationRevision = null;
			String inspectionDuration = null;

			omDescription = rs.getString("om_description");
			if (null != omDescription) {
				resultData.append(omDescription.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			qcpPageName = rs.getString("qcp_page_name");
			if (null != qcpPageName) {
				resultData.append(qcpPageName.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			costingProject = rs.getString("costing_project");
			if (null != costingProject) {
				resultData.append(costingProject.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			functionalUnit = rs.getString("functional_unit");
			if (null != functionalUnit) {
				resultData.append(functionalUnit.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			functionDescription = rs.getString("fucntional_unit_description");
			if (null != functionDescription) {
				resultData.append(functionDescription.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			peiCode = rs.getString("pei_code");
			if (null != peiCode) {
				resultData.append(peiCode.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			longDummyCode = rs.getString("long_dummy_code");
			if (null != longDummyCode) {
				resultData.append(longDummyCode.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			itemCode = rs.getString("item_code");
			if (null != itemCode) {
				resultData.append(itemCode.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			itemDescription = rs.getString("item_description");
			if (null != itemDescription) {
				resultData.append(itemDescription.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			qcpDoc = rs.getString("qcp_doc");
			if (null != qcpDoc) {
				resultData.append(qcpDoc.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			qcpRev = rs.getString("qcp_rev");
			if (null != qcpRev) {
				resultData.append(qcpRev.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			requirementId = rs.getString("requirement_id");
			if (null != requirementId) {
				requirementId = requirementId.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE);
				requirementId = InspectionTestPlanConstants.TAB_SPACE + requirementId;
				resultData.append(requirementId);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			requirementDescription = rs.getString("requirement_description");
			if (null != requirementDescription) {
				resultData.append(requirementDescription.replaceAll(
						InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER, InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			status = rs.getString("status");
			if (null != status) {
				resultData.append(status.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			refDoc = rs.getString("ref_docs");
			if (null != refDoc) {
				resultData.append(refDoc.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			procedureDoc = rs.getString("procedure_docs");
			if (null != procedureDoc) {
				resultData.append(procedureDoc.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			acceptance = rs.getString("acceptance");
			if (null != acceptance) {
				resultData.append(acceptance.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			if (null != rs.getString("ge")) {
				resultData.append(rs.getString("ge"));
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			customer = rs.getString("customer");
			endUser = rs.getString("end_user");
			tp = rs.getString("tp");

			if (null != customer && !InspectionTestPlanConstants.VALUE_EMPTY.equalsIgnoreCase(customer)) {
				resultData.append(customer);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			if (null != endUser && !InspectionTestPlanConstants.VALUE_EMPTY.equalsIgnoreCase(endUser)) {
				resultData.append(endUser);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			if (null != tp && !InspectionTestPlanConstants.VALUE_EMPTY.equalsIgnoreCase(tp)) {
				resultData.append(tp);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			supplier = rs.getString("supplier");
			if (null != supplier) {
				resultData.append(supplier.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			supplierLocation = rs.getString("supplier_location");
			if (null != supplierLocation) {
				resultData.append(supplierLocation.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			subSupplierLocation = rs.getString("sub_supplier_location");
			if (null != subSupplierLocation) {
				resultData.append(subSupplierLocation.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE));
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			poWip = rs.getString("po_wip");
			if (null != poWip) {
				resultData.append(poWip.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			poLine = rs.getString("po_line");
			if (null != poLine) {
				resultData.append(poLine.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			if (null != rs.getString("expected_inspection_start_dt")) {
				resultData.append(rs.getString("expected_inspection_start_dt"));
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			customerNotificationNo = rs.getString("customer_notification_number");
			if (null != customerNotificationNo) {
				resultData.append(customerNotificationNo.replaceAll(
						InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER, InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			notificationNo = rs.getString("notification_number");
			if (null != notificationNo) {
				resultData.append(notificationNo.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			notificationStatus = rs.getString("notification_status");
			if (null != notificationStatus) {
				resultData.append(notificationStatus.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			notificationRevision = rs.getString("notification_revision");
			if (null != notificationRevision) {
				resultData.append(notificationRevision.replaceAll(
						InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER, InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			if (null != rs.getString("inspection_date")) {
				resultData.append(rs.getString("inspection_date"));
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			inspectionDuration = rs.getString("inspection_duration");
			if (null != inspectionDuration) {
				resultData.append(inspectionDuration.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			inspectionType = rs.getString("inspection_type");
			if (null != inspectionType) {
				resultData.append(inspectionType.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			customerNotification = rs.getString("notification_to_customer");
			if (null != customerNotification) {
				SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
				SimpleDateFormat newFormat = new SimpleDateFormat("dd-MMM-yyyy");
				Date date;
				try {
					date = oldFormat.parse(customerNotification);
					resultData.append((newFormat.format(date).toUpperCase()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			rc1Reference = rs.getString("rc1_reference");
			if (null != rc1Reference) {
				resultData.append(rc1Reference.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			testResult = rs.getString("test_results");
			if (null != testResult) {
				resultData.append(testResult.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			inspectionNotes = rs.getString("inspection_notes");
			if (null != inspectionNotes) {

				resultData.append(inspectionNotes.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}

			resultData.append(InspectionTestPlanConstants.END_OF_LINE_DELIMITER);

		}
	}

	/***
	 * This helper method is used to populate the inspection test plan details
	 * fetched from DB to DTO
	 * 
	 */
	public void downloadExternalInspectionTestPlanDetails(final ResultSet rs, StringBuilder resultData)
			throws SQLException {
		while (rs.next()) {

			String customer = null;
			String endUser = null;
			String tp = null;
			String omDescription = null;
			String itemDescription = null;
			String functionDescription = null;
			String requirementDescription = null;
			String inspectionNotes = null;
			String testResult = null;
			String rc1Reference = null;
			String customerNotification = null;
			String acceptance = null;
			String inspectionType = null;
			String refDoc = null;
			String procedureDoc = null;
			String qcpPageName = null;
			String costingProject = null;
			String functionalUnit = null;
			String peiCode = null;
			String longDummyCode = null;
			String itemCode = null;
			String qcpDoc = null;
			String qcpRev = null;
			String requirementId = null;
			String status = null;
			String supplier = null;
			String supplierLocation = null;
			String subSupplierLocation = null;
			String poWip = null;
			String poLine = null;
			String customerNotificationNo = null;
			String notificationNo = null;
			String notificationStatus = null;
			String notificationRevision = null;
			String inspectionDuration = null;

			omDescription = rs.getString("om_description");
			if (null != omDescription) {
				resultData.append(omDescription.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			qcpPageName = rs.getString("qcp_page_name");
			if (null != qcpPageName) {
				resultData.append(qcpPageName.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			costingProject = rs.getString("costing_project");
			if (null != costingProject) {
				resultData.append(costingProject.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			functionalUnit = rs.getString("functional_unit");
			if (null != functionalUnit) {
				resultData.append(functionalUnit.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			functionDescription = rs.getString("fucntional_unit_description");
			if (null != functionDescription) {
				resultData.append(functionDescription.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			peiCode = rs.getString("pei_code");
			if (null != peiCode) {
				resultData.append(peiCode.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			longDummyCode = rs.getString("long_dummy_code");
			if (null != longDummyCode) {
				resultData.append(longDummyCode.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			itemCode = rs.getString("item_code");
			if (null != itemCode) {
				resultData.append(itemCode.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			itemDescription = rs.getString("item_description");
			if (null != itemDescription) {
				resultData.append(itemDescription.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			qcpDoc = rs.getString("qcp_doc");
			if (null != qcpDoc) {
				resultData.append(qcpDoc.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			qcpRev = rs.getString("qcp_rev");
			if (null != qcpRev) {
				resultData.append(qcpRev.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			requirementId = rs.getString("requirement_id");
			if (null != requirementId) {
				requirementId = requirementId.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE);
				requirementId = InspectionTestPlanConstants.TAB_SPACE + requirementId;
				resultData.append(requirementId);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			requirementDescription = rs.getString("requirement_description");
			if (null != requirementDescription) {
				resultData.append(requirementDescription.replaceAll(
						InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER, InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			status = rs.getString("status");
			if (null != status) {
				resultData.append(status.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			refDoc = rs.getString("ref_docs");
			if (null != refDoc) {
				resultData.append(refDoc.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			procedureDoc = rs.getString("procedure_docs");
			if (null != procedureDoc) {
				resultData.append(procedureDoc.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			acceptance = rs.getString("acceptance");
			if (null != acceptance) {
				resultData.append(acceptance.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			if (null != rs.getString("ge")) {
				resultData.append(rs.getString("ge"));
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			customer = rs.getString("customer");
			endUser = rs.getString("end_user");
			tp = rs.getString("tp");

			if (null != customer && !InspectionTestPlanConstants.VALUE_EMPTY.equalsIgnoreCase(customer)) {
				resultData.append(customer);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			if (null != endUser && !InspectionTestPlanConstants.VALUE_EMPTY.equalsIgnoreCase(endUser)) {
				resultData.append(endUser);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			if (null != tp && !InspectionTestPlanConstants.VALUE_EMPTY.equalsIgnoreCase(tp)) {
				resultData.append(tp);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			supplier = rs.getString("supplier");
			if (null != supplier) {
				resultData.append(supplier.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			supplierLocation = rs.getString("supplier_location");
			if (null != supplierLocation) {
				resultData.append(supplierLocation.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			subSupplierLocation = rs.getString("sub_supplier_location");
			if (null != subSupplierLocation) {
				resultData.append(subSupplierLocation.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			poWip = rs.getString("po_wip");
			if (null != poWip) {
				resultData.append(poWip.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			poLine = rs.getString("po_line");
			if (null != poLine) {
				resultData.append(poLine.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			if (null != rs.getString("expected_inspection_start_dt")) {
				resultData.append(rs.getString("expected_inspection_start_dt"));
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			customerNotificationNo = rs.getString("customer_notification_number_ext");
			if (null != customerNotificationNo) {
				resultData.append(customerNotificationNo.replaceAll(
						InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER, InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			notificationNo = rs.getString("notification_number");
			if (null != notificationNo) {
				resultData.append(notificationNo.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			notificationStatus = rs.getString("notification_status_ext");
			if (null != notificationStatus) {
				resultData.append(notificationStatus.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			notificationRevision = rs.getString("notification_revision_ext");
			if (null != notificationRevision) {
				resultData.append(notificationRevision.replaceAll(
						InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER, InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			if (null != rs.getString("inspection_date")) {
				resultData.append(rs.getString("inspection_date"));
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			inspectionDuration = rs.getString("inspection_duration");
			if (null != inspectionDuration) {
				resultData.append(inspectionDuration.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			inspectionType = rs.getString("inspection_type");
			if (null != inspectionType) {
				resultData.append(inspectionType.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			customerNotification = rs.getString("notification_to_customer");
			if (null != customerNotification) {
				SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
				SimpleDateFormat newFormat = new SimpleDateFormat("dd-MMM-yyyy");
				Date date;
				try {
					date = oldFormat.parse(customerNotification);
					resultData.append((newFormat.format(date).toUpperCase()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			rc1Reference = rs.getString("rc1_reference");
			if (null != rc1Reference) {
				resultData.append(rc1Reference.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			testResult = rs.getString("test_results");
			if (null != testResult) {
				resultData.append(testResult.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}
			resultData.append(InspectionTestPlanConstants.COMMA_SEPERATOR);

			inspectionNotes = rs.getString("inspection_notes");
			if (null != inspectionNotes) {

				resultData.append(inspectionNotes.replaceAll(InspectionTestPlanConstants.ALPHA_NUMERIC_REGIX_PATTER,
						InspectionTestPlanConstants.WHITE_SPACE)

				);
			}

			resultData.append(InspectionTestPlanConstants.END_OF_LINE_DELIMITER);

		}
	}

	public void frameInternalITPDetials(InspectionTestPlanDTO inspectionTestPlanDTO, StringBuilder query,
			ArrayList<Object> queryData) {

		if (null != inspectionTestPlanDTO.getRtiStatus()
				&& !InspectionTestPlanConstants.EMPTY_STRING.equalsIgnoreCase(inspectionTestPlanDTO.getRtiStatus())) {
			query.append(" and rti_status = ? ");
			queryData.add(inspectionTestPlanDTO.getRtiStatus());
		}

		if (null != inspectionTestPlanDTO.getCustomerStatus() && !InspectionTestPlanConstants.EMPTY_STRING
				.equalsIgnoreCase(inspectionTestPlanDTO.getCustomerStatus())) {
			query.append(" and customer_status = ? ");
			queryData.add(inspectionTestPlanDTO.getCustomerStatus());
		}

		if (null != inspectionTestPlanDTO.getyAxisStatus()
				&& !InspectionTestPlanConstants.EMPTY_STRING.equalsIgnoreCase(inspectionTestPlanDTO.getyAxisStatus())) {
			query.append(" and yaxis_Status = ? ");
			queryData.add(inspectionTestPlanDTO.getyAxisStatus());
		}

		if (null != inspectionTestPlanDTO.getxAxisColor()
				&& !InspectionTestPlanConstants.EMPTY_STRING.equalsIgnoreCase(inspectionTestPlanDTO.getxAxisColor())) {
			query.append(" and x_axis_color = ? ");
			queryData.add(inspectionTestPlanDTO.getxAxisColor());
		}

	}

	public void frameIAPExternalDetails(InspectionTestPlanDTO inspectionTestPlanDTO, StringBuilder query,
			ArrayList<Object> queryData) {

		if (null != inspectionTestPlanDTO.getRtiStatus()
				&& !InspectionTestPlanConstants.EMPTY_STRING.equalsIgnoreCase(inspectionTestPlanDTO.getRtiStatus())) {
			query.append(" and report_data.rti_status = ? ");
			queryData.add(inspectionTestPlanDTO.getRtiStatus());
		}

		if (null != inspectionTestPlanDTO.getCustomerStatus() && !InspectionTestPlanConstants.EMPTY_STRING
				.equalsIgnoreCase(inspectionTestPlanDTO.getCustomerStatus())) {
			query.append(" and report_data.customer_status = ? ");
			queryData.add(inspectionTestPlanDTO.getCustomerStatus());
		}

		if (null != inspectionTestPlanDTO.getyAxisStatus()
				&& !InspectionTestPlanConstants.EMPTY_STRING.equalsIgnoreCase(inspectionTestPlanDTO.getyAxisStatus())) {
			query.append(" and report_data.y_axis_status_ext = ? ");
			queryData.add(inspectionTestPlanDTO.getyAxisStatus());
		}

		if (null != inspectionTestPlanDTO.getxAxisColor()
				&& !InspectionTestPlanConstants.EMPTY_STRING.equalsIgnoreCase(inspectionTestPlanDTO.getxAxisColor())) {
			query.append(" and report_data.x_axis_ext = ? ");
			queryData.add(inspectionTestPlanDTO.getxAxisColor());
		}

	}

	public void frameExternalITPDetials(InspectionTestPlanDTO inspectionTestPlanDTO, StringBuilder query,
			ArrayList<Object> queryData) {

		if (null != inspectionTestPlanDTO.getRtiStatus()
				&& !InspectionTestPlanConstants.EMPTY_STRING.equalsIgnoreCase(inspectionTestPlanDTO.getRtiStatus())) {
			query.append(" and rti_status = ? ");
			queryData.add(inspectionTestPlanDTO.getRtiStatus());
		}

		if (null != inspectionTestPlanDTO.getCustomerStatus() && !InspectionTestPlanConstants.EMPTY_STRING
				.equalsIgnoreCase(inspectionTestPlanDTO.getCustomerStatus())) {
			query.append(" and customer_status = ? ");
			queryData.add(inspectionTestPlanDTO.getCustomerStatus());
		}

		if (null != inspectionTestPlanDTO.getyAxisStatus()
				&& !InspectionTestPlanConstants.EMPTY_STRING.equalsIgnoreCase(inspectionTestPlanDTO.getyAxisStatus())) {
			query.append(" and y_axis_status_ext = ? ");
			queryData.add(inspectionTestPlanDTO.getyAxisStatus());
		}

		if (null != inspectionTestPlanDTO.getxAxisColor()
				&& !InspectionTestPlanConstants.EMPTY_STRING.equalsIgnoreCase(inspectionTestPlanDTO.getxAxisColor())) {
			query.append(" and x_axis_ext = ? ");
			queryData.add(inspectionTestPlanDTO.getxAxisColor());
		}

	}

	private String formateData(String dateString) {

		if (null != dateString) {
			Date date = null;
			try {
				date = yearFormat.parse(dateString);
				dateString = realTrackFormat.format(date).toUpperCase();
			} catch (ParseException e) {
				log.error("Error occured when changing data format" + e.getMessage());
			}
		}

		return dateString;
	}

	private String formateTimeStamp(String dateString) {

		if (null != dateString) {
			Date date = null;
			try {
				date = timeStampFormat.parse(dateString);
				dateString = realTrackFormat.format(date).toUpperCase();
			} catch (ParseException e) {
				log.error("Error occured when changing data format" + e.getMessage());
			}
		}

		return dateString;
	}

}

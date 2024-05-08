/**
 * 
 */
package com.bh.realtrack.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.ServerErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.bh.realtrack.dao.IContractMilestoneDAO;
import com.bh.realtrack.dto.ContractMilestoneDTO;
import com.bh.realtrack.enumerators.ErrorCode;
import com.bh.realtrack.service.impl.ProjectComplexityServiceImpl;
import com.bh.realtrack.util.ContractMilestoneConstants;

/**
 * @author Anand Kumar
 *
 */
@Repository
public class ContractMilestoneDAOImpl implements IContractMilestoneDAO {
	private static Logger log = LoggerFactory
			.getLogger(ProjectComplexityServiceImpl.class.getName());
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<ContractMilestoneDTO> getContractMilestoneDetails(
			String projectId) {
		return jdbcTemplate.query(
				ContractMilestoneConstants.GET_CONTRACT_MILESTONE_DETAILS,
				new Object[] { projectId, projectId, projectId, projectId },
				new ResultSetExtractor<List<ContractMilestoneDTO>>() {
					@Override
					public List<ContractMilestoneDTO> extractData(
							final ResultSet rs) {
						List<ContractMilestoneDTO> list = new ArrayList<ContractMilestoneDTO>();
						try {
							while (rs.next()) {
								ContractMilestoneDTO contractMilestoneDTO = new ContractMilestoneDTO();
								contractMilestoneDTO.setBatchId(rs.getInt(1));
								contractMilestoneDTO.setActivityId(rs
										.getString(2));
								contractMilestoneDTO.setActivityName(rs
										.getString(3));
								contractMilestoneDTO.setForecastDate(rs
										.getString(4));
								contractMilestoneDTO.setContractualDueDate(rs
										.getString(5));
								contractMilestoneDTO.setTotalFloatDays(rs
										.getString(7));
								contractMilestoneDTO.setGrpTotalFloatDays(rs
										.getString(8));
								contractMilestoneDTO.setPreForcastDate(rs
										.getString(9));
								contractMilestoneDTO.setTotalFloatDelta(rs
										.getString(10));
								list.add(contractMilestoneDTO);
							}
						} catch (SQLException e) {
							log.error("something went wrong while iterating contract milestone:"
									+ e.getMessage());
							throw new ServerErrorException(ErrorCode.INTERNAL
									.getResponseStatus(), e);
						}
						return list;
					}
				});
	}

}

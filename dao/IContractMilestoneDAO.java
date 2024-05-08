/**
 * 
 */
package com.bh.realtrack.dao;

import java.util.List;

import com.bh.realtrack.dto.ContractMilestoneDTO;

/**
 * @author Anand Kumar
 *
 */
public interface IContractMilestoneDAO {
    List<ContractMilestoneDTO> getContractMilestoneDetails(String projectId);
}

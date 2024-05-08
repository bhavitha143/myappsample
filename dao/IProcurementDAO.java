package com.bh.realtrack.dao;

import java.util.List;

import com.bh.realtrack.dto.ItemBuyControlDTO;
import com.bh.realtrack.dto.MaterialListDetailsDTO;
import com.bh.realtrack.dto.ProjectTeamDetailsDTO;

public interface IProcurementDAO {

	List<ItemBuyControlDTO> getItemBuyControlFlowDetails(String jobNumber, String dummyCode, String activityFilter);

	List<ProjectTeamDetailsDTO> getItemBuyControlFlowChatDetails(String jobNumber, String dummyCode,
			String activityFilter);

	List<MaterialListDetailsDTO> getMaterialListDetails(String projectId, String viewConsideration, String train,
			String subProject, String componentCode, String activityFilter);

}

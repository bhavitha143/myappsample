package com.bh.realtrack.dao;

import java.util.List;

import com.bh.realtrack.dto.KickOffDTO;
import com.bh.realtrack.dto.KickOffLogDetailsDTO;
import com.bh.realtrack.dto.SaveKickOffResponseDTO;

/**
 *
 * @author Anand Kumar
 *
 */
public interface IKickOffDAO {

    int saveKickOffData(String projectId, String matrix, String sso);

    KickOffLogDetailsDTO getKickOffLogDetails(String projectId);

    SaveKickOffResponseDTO getKickOffSaveLogDetails(String projectId);

    List<KickOffDTO> getKickOffData(String projectId);
    
    KickOffDTO getMatrixDetails(String projectId);
}

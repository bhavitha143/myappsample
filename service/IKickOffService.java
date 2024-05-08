package com.bh.realtrack.service;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

import com.bh.realtrack.dto.KickOffLogDetailsDTO;
import com.bh.realtrack.dto.SaveKickOffDTO;
import com.bh.realtrack.dto.SaveKickOffResponseDTO;

import java.io.IOException;

/**
 *
 * @author Anand Kumar
 *
 */
public interface IKickOffService {
    KickOffLogDetailsDTO getKickOffDetails(String projectId);

    SaveKickOffResponseDTO saveKickOffDetails(SaveKickOffDTO saveKickOffDTO);

    ResponseEntity<InputStreamResource> exportKickOffExcel(String projectId);
    
    ResponseEntity<InputStreamResource> exportKickOffPDF(String projectId) throws IOException;

}

/**
 * 
 */
package com.bh.realtrack.service;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

/**
 * @author Anand Kumar
 *
 */
public interface IContractMilestoneService {
    ResponseEntity<InputStreamResource> exportContractMilestoneExcel(final String projectId);
}

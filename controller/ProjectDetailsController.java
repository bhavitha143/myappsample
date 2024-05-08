package com.bh.realtrack.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class ProjectDetailsController {
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<String> getHealthcheck() {
		return new ResponseEntity<>("SUCCESS",HttpStatus.OK);
	}
	
}

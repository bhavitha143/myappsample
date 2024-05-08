package com.bh.realtrack.controller;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.S3Object;
import com.bh.realtrack.blobstore.BlobstoreServiceConnectorCreator;
import com.bh.realtrack.blobstore.BlobstoreServiceInfo;
import com.bh.realtrack.blobstore.BlobstoreServiceInfoCreator;
import com.bh.realtrack.model.CallerContext;
import com.bh.realtrack.service.impl.BlobstoreService;
import com.bh.realtrack.util.AssertUtils;

/**
 * Primary Controller for the BlobStore Demo app
 */
@RestController
@CrossOrigin
public class BlobstoreController {

	private static final Logger log = LoggerFactory.getLogger(BlobstoreController.class);

	@Autowired
	BlobstoreServiceInfoCreator blobstoreServiceInfoCreator;

	@Autowired
	CallerContext callerContext;

	@Autowired
	BlobstoreServiceConnectorCreator blobstoreServiceConnectorCreator;

	@RequestMapping(value = "/getImageFromBucket", method = RequestMethod.GET)
	public ResponseEntity<?> getImageFromBucket(@RequestParam String imagePath) throws Exception {
		try {
			HttpHeaders respHeaders = new HttpHeaders();
			if (null != imagePath && !imagePath.isEmpty()) {
				log.info("getImageFromBucket :: imagePath :: " + imagePath);
				respHeaders.setContentDispositionFormData("attachment", imagePath);
				BlobstoreServiceInfo objectStoreInfo = blobstoreServiceInfoCreator.createServiceInfo();
				BlobstoreService objectStoreService = blobstoreServiceConnectorCreator.create(objectStoreInfo);
				InputStreamResource isr = new InputStreamResource(objectStoreService.get(imagePath, ""));
				InputStream inputStream = isr.getInputStream();
				byte[] imageBytes = IOUtils.toByteArray(inputStream);
				inputStream.read(imageBytes, 0, imageBytes.length);
				inputStream.close();
				String encodedString = Base64.getEncoder().encodeToString(imageBytes);
				return new ResponseEntity<String>(encodedString, respHeaders, HttpStatus.OK);
			}
		} catch (Exception e) {
			log.error("getImageFromBucket() :: Exception occurred : " + e.getMessage());
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/uploadImageToBucket", method = RequestMethod.POST)
	public Map<String, Object> uploadImageToBucket(@RequestHeader HttpHeaders headers,
			@RequestParam("image") MultipartFile image, @RequestParam String module, @RequestParam String section,
			@RequestParam String subModule) throws Exception {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		String modifiedFileName = "", sso = "";
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		try {
			sso = callerContext.getName();
			if (null != module && !module.isEmpty() && null != subModule && !subModule.isEmpty() && null != section
					&& !section.isEmpty() && AssertUtils.ifFileNotEmpty(image)) {
				log.info("uploadImageToBucket :: File Name :: " + image.getOriginalFilename());
				BlobstoreServiceInfo objectStoreInfo = blobstoreServiceInfoCreator.createServiceInfo();
				BlobstoreService objectStoreService = blobstoreServiceConnectorCreator.create(objectStoreInfo);
				modifiedFileName = module + "/" + subModule + "/" + section + "/" + sso + "_" + timestamp.getTime()
						+ "_" + image.getOriginalFilename();
				log.info("uploadImageToBucket :: modifiedFileName :: " + modifiedFileName);
				S3Object s3obj = new S3Object();
				s3obj.setKey(modifiedFileName);
				s3obj.setObjectContent(image.getInputStream());
				objectStoreService.put(s3obj);
				responseMap.put("imagePath", modifiedFileName);
			}
		} catch (Exception e) {
			log.error("Error in uploadImageToBucket :: " + e.getMessage());
			responseMap.put("imagePath", "");
			return responseMap;
		}
		return responseMap;
	}

}

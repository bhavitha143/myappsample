package com.bh.realtrack.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AbortMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.SSEAlgorithm;
import com.amazonaws.services.s3.model.SSEAwsKeyManagementParams;
import com.amazonaws.services.s3.model.UploadPartRequest;

public class BlobstoreService {

	Log log = LogFactory.getLog(BlobstoreService.class);

	/**
	 * Instance of BlobStore
	 */
	private AmazonS3 s3Client;

	/**
	 * Name of the bucket created in BlobStore
	 */
	private String bucket;

	/**
	 * Serverside Encryption
	 */
	private boolean enableSSE;

	public BlobstoreService(AmazonS3 s3Client, String bucket, boolean enableSSE) {
		super();
		this.s3Client = s3Client;
		this.bucket = bucket;
		this.enableSSE = enableSSE;
	}

	public static final String APPLICATION_OCTET_STREAM = "application/octet-stream";

	/**
	 * Adds a new Blob to the binded bucket in the Object Store
	 *
	 * @param obj S3Object to be added
	 * @throws Exception
	 */
	public void put(S3Object obj) throws Exception {
		if (obj == null) {
			log.error("put(): Empty file provided");
			throw new Exception("File is null");
		}
		InputStream is = obj.getObjectContent();

		List<PartETag> partETags = new ArrayList<PartETag>();

		InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucket, obj.getKey());
		initRequest.withSSEAwsKeyManagementParams(new SSEAwsKeyManagementParams());
		InitiateMultipartUploadResult initResponse = s3Client.initiateMultipartUpload(initRequest);
		try {

			int i = 1;
			int currentPartSize = 0;
			ByteArrayOutputStream tempBuffer = new ByteArrayOutputStream();
			int byteValue;
			while ((byteValue = is.read()) != -1) {
				tempBuffer.write(byteValue);
				currentPartSize = tempBuffer.size();
				if (currentPartSize == (50 * 1024 * 1024)) // make this a const
				{
					byte[] b = tempBuffer.toByteArray();
					ByteArrayInputStream byteStream = new ByteArrayInputStream(b);

					UploadPartRequest uploadPartRequest = new UploadPartRequest().withBucketName(bucket)
							.withKey(obj.getKey()).withUploadId(initResponse.getUploadId()).withPartNumber(i++)
							.withInputStream(byteStream).withPartSize(currentPartSize);
					uploadPartRequest.putCustomRequestHeader("x-amz-server-side-encryption", "aws:kms");

					partETags.add(s3Client.uploadPart(uploadPartRequest).getPartETag());

					tempBuffer.reset();
				}
			}
			log.info("currentPartSize: " + currentPartSize);
			ObjectMetadata objectMetadata = new ObjectMetadata();
			objectMetadata.setContentLength(currentPartSize);
			objectMetadata.setSSEAlgorithm(SSEAlgorithm.KMS.getAlgorithm());
			obj.setObjectMetadata(objectMetadata);

			if (i == 1 && currentPartSize < (5 * 1024 * 1024)) // make this a const
			{
				AbortMultipartUploadRequest abortRequest = new AbortMultipartUploadRequest(bucket, obj.getKey(),
						initResponse.getUploadId());

				s3Client.abortMultipartUpload(abortRequest);

				byte[] b = tempBuffer.toByteArray();
				ByteArrayInputStream byteStream = new ByteArrayInputStream(b);
				objectMetadata.setContentType(getContentType(b));

				obj.setObjectMetadata(objectMetadata);

				PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, obj.getKey(), byteStream,
						obj.getObjectMetadata());

				putObjectRequest.withSSEAwsKeyManagementParams(new SSEAwsKeyManagementParams());

				s3Client.putObject(putObjectRequest);
				ObjectMetadata meta = s3Client.getObjectMetadata(bucket, obj.getKey());
				Map<String, Object> headers = meta.getRawMetadata();
				for (Map.Entry<String, Object> entry : headers.entrySet()) {
					log.info("Object Metadata -- " + entry.getKey() + ": " + entry.getValue().toString());
				}

				return;
			}

			if (currentPartSize > 0 && currentPartSize <= (50 * 1024 * 1024)) // make this a const
			{
				byte[] b = tempBuffer.toByteArray();
				ByteArrayInputStream byteStream = new ByteArrayInputStream(b);

				log.info("currentPartSize: " + currentPartSize);
				log.info("byteArray: " + b);

				UploadPartRequest uploadPartRequest = new UploadPartRequest().withBucketName(bucket)
						.withKey(obj.getKey()).withUploadId(initResponse.getUploadId()).withPartNumber(i)
						.withInputStream(byteStream).withPartSize(currentPartSize);
				uploadPartRequest.putCustomRequestHeader("x-amz-server-side-encryption", "aws:kms");
				partETags.add(s3Client.uploadPart(uploadPartRequest).getPartETag());
			}

			CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest()
					.withBucketName(bucket).withPartETags(partETags).withUploadId(initResponse.getUploadId())
					.withKey(obj.getKey());
			completeMultipartUploadRequest.putCustomRequestHeader("x-amz-server-side-encryption", "aws:kms");
			s3Client.completeMultipartUpload(completeMultipartUploadRequest);
		} catch (Exception e) {
			log.error("put(): Exception occurred in put(): " + e.getMessage());
			s3Client.abortMultipartUpload(
					new AbortMultipartUploadRequest(bucket, obj.getKey(), initResponse.getUploadId()));
			throw e;
		} finally {
			is.close();
			obj.close();
		}
	}

	private String getContentType(byte[] b) {
		String contentType;
		try {
			contentType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(b));
		} catch (IOException e) {
			if (log.isDebugEnabled()) {
				log.debug("unable to determine content type", e);
			}
			return APPLICATION_OCTET_STREAM;
		}
		if (log.isDebugEnabled()) {
			log.debug("content type inferred is" + contentType);
		}
		return contentType;
	}

	/**
	 * Get the Blob from the binded bucket
	 *
	 * @param fileName String
	 * @throws Exception
	 */
	public InputStream get(String fileName, String range) throws Exception {

		if (range != null && !range.isEmpty()) {
			String[] r = range.split(":");
			if (r.length != 2) {
				throw new Exception("Invalid range format");
			}

			try {
				long start = Long.parseLong(r[0]);
				long end = Long.parseLong(r[1]);

				GetObjectRequest rangeObjectRequest = new GetObjectRequest(bucket, fileName);
				rangeObjectRequest.setRange(start, end);
				S3Object objectPortion = s3Client.getObject(rangeObjectRequest);

				InputStream objectData = objectPortion.getObjectContent();

				return objectData;

			} catch (NumberFormatException e) {
				throw new Exception("Invalid range specified ", e);
			}
		} else {
			try {
				S3Object object = s3Client.getObject(new GetObjectRequest(bucket, fileName));
				InputStream objectData = object.getObjectContent();

				return objectData;

			} catch (Exception e) {
				log.error("Exception Occurred in get(): " + e.getMessage());
				throw e;
			}
		}

	}

	/**
	 * Gets the list of available Blobs for the binded bucket from the BlobStore.
	 *
	 * @return List<BlobFile> List of Blobs
	 * @throws Exception
	 */
	public List<String> get() throws Exception {
		List<String> objs = new ArrayList<String>();
		try {
			// Get the List from BlobStore
			ObjectListing objectList = s3Client.listObjects(bucket);

			for (S3ObjectSummary objectSummary : objectList.getObjectSummaries()) {

				objs.add(objectSummary.getKey());
			}

		} catch (Exception e) {
			log.error("Exception occurred in get(): " + e.getMessage());
			throw e;
		}

		return objs;
	}

	/**
	 * Delete the Blob from the binded bucket
	 *
	 * @param fileName String of file to be removed
	 * @throws Exception
	 */
	public void delete(String fileName) throws Exception {
		try {
			s3Client.deleteObject(bucket, fileName);
			if (log.isDebugEnabled())
				log.debug("delete(): Successfully deleted the file = " + fileName);
		} catch (Exception e) {
			log.error("delete(): Exception Occurred in delete(): " + e.getMessage());
			throw e;
		}
	}
}

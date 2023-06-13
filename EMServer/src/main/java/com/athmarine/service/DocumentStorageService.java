package com.athmarine.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.athmarine.exception.FileNotFoundException;
import com.athmarine.request.FileUploadResponseModel;

@Service
public class DocumentStorageService {

	@Autowired
	private AmazonS3 amazonS3;

	@Value("${aws.s3.bucket}")
	private String bucketName;

	@Async
	public String uploadFile(final MultipartFile multipartFile) {

		File file;
		String path;
		file = convertMultiPartFileToFile(multipartFile);
		path = uploadFileToS3Bucket(bucketName, file);
		file.delete();
		return path;

	}

	private File convertMultiPartFileToFile(final MultipartFile multipartFile) {
		File file = new File(multipartFile.getOriginalFilename());
		try (FileOutputStream outputStream = new FileOutputStream(file)) {
			outputStream.write(multipartFile.getBytes());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return file;
	}

	@SuppressWarnings("unused")
	private String uploadFileToS3Bucket(final String bucketName, final File file) {

		final String uniqueFileName = LocalDateTime.now() + file.getPath();
		PutObjectRequest putObjectRequest = null;
		try {
			putObjectRequest = new PutObjectRequest(bucketName, uniqueFileName, file);
		} catch (Exception ex) {
			ex.printStackTrace();

		}

		try {
			PutObjectResult s = amazonS3.putObject(putObjectRequest);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return uniqueFileName;
	}

	public S3Object downloadFileFromS3Bucket(final String fileName) {

		try {
			S3Object s3object = amazonS3.getObject(bucketName, fileName);

			return s3object;
		} catch (Exception ex) {
			throw new FileNotFoundException(ex.getMessage(), ex.getMessage(), ex.getMessage());
		}

	}

	@Value("${aws.s3.bucket}")
	private String s3BucketName;

	private String generateUrl(String fileName, HttpMethod httpMethod) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, 1); // Generated URL will be valid for 24 hours

		String response = amazonS3.generatePresignedUrl(s3BucketName, fileName, calendar.getTime(), httpMethod)
				.toString();
		return response;
	}

	@Async
	public String findByName(String fileName) {
		if (!amazonS3.doesObjectExist(s3BucketName, fileName))
			return "File does not exist";
		return generateUrl(fileName, HttpMethod.GET);
	}

	@Async
	public FileUploadResponseModel save(String extension) {
		String fileName = UUID.randomUUID().toString() + extension;
		String url = generateUrl(fileName, HttpMethod.PUT);

		FileUploadResponseModel response = new FileUploadResponseModel();
		response.setFilename(fileName);
		response.setUrl(url);
		return response;
	}

}

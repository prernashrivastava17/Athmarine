package com.athmarine.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.athmarine.exception.AppException;
import com.athmarine.repository.CustomerCompanyRepository;
import com.athmarine.repository.EngineerCertificateRepository;
import com.athmarine.repository.TurnoverRepository;
import com.athmarine.repository.VendorCompanyRepository;
import com.athmarine.request.DownloadFileResponse;
import com.athmarine.request.UploadFileResponse;
import com.athmarine.resources.AppConstant;
import com.athmarine.util.JwtTokenUtil;

@Transactional(rollbackOn = Exception.class)
@Service
public class FileServiceImpl {

	@Autowired
	private AmazonS3 amazonS3;

	@Value("${aws.s3.bucket}")
	private String bucketName;

	@Value("${file.upload.dir}")
	private String imagePath;

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@Autowired
	CustomerCompanyRepository customerCompanyRepository;

	@Autowired
	VendorCompanyRepository vendorCompanyRepository;

	@Autowired
	TurnoverRepository turnoverRepository;

	@Autowired
	EngineerCertificateRepository engineerCertificateRepository;

	// /**************************

	public String generatePreSignedUrlForFileDownload(String fileKey) {

		if (fileKey == null || fileKey.isEmpty()) {
			throw new AppException(AppConstant.ErrorTypes.PATH_NULL_FOR_FILE,
					AppConstant.ErrorCodes.PATH_NULL_FOR_FILE_ERROR_CODE,
					AppConstant.ErrorMessages.PATH_NULL_FOR_FILE_ERROR_MESSAGE);
		}
		Date expirationDate = new Date();
		long expTimeMillis = expirationDate.getTime();
		/** Expire time for 15 min. */
		expTimeMillis += 900000;
		/** Expire time for I hour */
		// expTimeMillis += 1000 * 60 * 60;
		expirationDate.setTime(expTimeMillis);
		GeneratePresignedUrlRequest presignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, fileKey)
				.withMethod(HttpMethod.GET).withExpiration(expirationDate);
		URL presignedUrl = amazonS3.generatePresignedUrl(presignedUrlRequest);
		String presignedUrlInString = presignedUrl.toString();
		return presignedUrlInString;

	}

	public DownloadFileResponse downloadFile(String documentRequest) {

		String downloadFile = documentRequest;
		

		String presignedUrlForDownload = generatePreSignedUrlForFileDownload(downloadFile);

		DownloadFileResponse downloadFileResponse = new DownloadFileResponse();
		downloadFileResponse.setPresignedUrlForDownload(presignedUrlForDownload);
		return downloadFileResponse;
	}

	
	public UploadFileResponse uploadFile(String documentRequest, String token, MultipartFile multipartFile)
			throws IOException {

		String toString = jwtTokenUtil.getUsernameFromToken(token);
		
		String temp = documentRequest.concat(toString);


		UploadFileResponse uploadFileResponse = null;

		uploadFileResponse = generatePreSignedUrl(temp, multipartFile);

		return uploadFileResponse;
	}

// /********************
	public UploadFileResponse generatePreSignedUrl(String temp, MultipartFile multipartFile) throws IOException {

		UploadFileResponse uploadFileResponse = null;
		String amazonObject = null;
		String extension = null;

		Date expirationDate = new Date(); 
		long expTimeMillis =
				  expirationDate.getTime();

		ObjectMetadata metaData = new ObjectMetadata();
		metaData.setContentType(multipartFile.getContentType());
		metaData.setContentLength(multipartFile.getSize());

		InputStream streamToUpload = multipartFile.getInputStream();

		String name = multipartFile.getOriginalFilename();
		extension = name.substring(name.lastIndexOf("."));

		amazonObject = imagePath + temp + expTimeMillis + extension;

		amazonS3.putObject(new PutObjectRequest(bucketName, amazonObject, streamToUpload, metaData)
				.withCannedAcl(CannedAccessControlList.PublicReadWrite));

		uploadFileResponse = new UploadFileResponse();
		uploadFileResponse.setFileKey(amazonObject);

		return uploadFileResponse;
	}
}

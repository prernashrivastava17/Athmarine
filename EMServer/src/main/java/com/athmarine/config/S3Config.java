package com.athmarine.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;

@Configuration
public class S3Config {
	@Value("${aws.access_key_id}")
	String accessKeyId; // = "AKIARDQVTZPPLKBZPJ3I";
	@Value("${aws.secret_access_key}")
	String secretAccessKey;// = "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";

	@Primary
	@Bean
	public AmazonS3 getS3() {
		AmazonS3 amazons = AmazonS3ClientBuilder.standard()
				.withCredentials(
						new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKeyId, secretAccessKey)))
				.withRegion(Regions.AP_SOUTH_1).build();
		return amazons;
	}

	@Bean
	public TransferManager getTransferManager(@Autowired AmazonS3 amazons) {
		TransferManager tm = TransferManagerBuilder.standard().withS3Client(amazons).build();
		return tm;
	}

}
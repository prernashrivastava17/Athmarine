package com.athmarine.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.athmarine.service.DocumentStorageService;

@RestController
@RequestMapping("/api/v1/files")
public class FileUploadController {

	@Autowired
	private DocumentStorageService service;

	/*
	 * @PostMapping(value = "/upload") public ResponseEntity<String>
	 * uploadFile(@RequestPart(value = "file") final MultipartFile multipartFile) {
	 * 
	 * String path = service.uploadFile(multipartFile); final String response = "["
	 * + path + "] uploaded successfully."; return new ResponseEntity<>(response,
	 * HttpStatus.OK);
	 * 
	 * }
	 * 
	 * @GetMapping(value = "/getFile") public ResponseEntity<String>
	 * downloadFile(@RequestParam String fileName) {
	 * 
	 * S3Object object= service.downloadFileFromS3Bucket(fileName);
	 * S3ObjectInputStream file = object.getObjectContent();
	 * 
	 * return new ResponseEntity<>("Downloaded", HttpStatus.OK);
	 * 
	 * }
	 */
	// private static final String FILE_NAME = "fileName";

	@GetMapping("/{filName}")
	public ResponseEntity<Object> findByName(HttpServletRequest request, @PathVariable("filName") String params) {

		return new ResponseEntity<>(service.findByName(params), HttpStatus.OK);

	}

	@PostMapping(value = "/upload")
	public ResponseEntity<Object> saveFile(@RequestParam("extension") String extension) {
		return new ResponseEntity<>(service.save(extension), HttpStatus.OK);
	}

}

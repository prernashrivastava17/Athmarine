package com.athmarine.request;

public class UploadFileResponse {
	
	private String fileKey;

	public String getFileKey() {
		return fileKey;
	}

	public void setFileKey(String fileKey) {
		this.fileKey = fileKey;
	}

	@Override
	public String toString() {
		return "UploadFileResponse [fileKey=" + fileKey + "]";
	}

	public UploadFileResponse(String fileKey) {
		super();
		this.fileKey = fileKey;
	}

	public UploadFileResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

}

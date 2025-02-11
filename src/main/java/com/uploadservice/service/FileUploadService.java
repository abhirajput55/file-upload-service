package com.uploadservice.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {

	void processCsvFile(MultipartFile file);

}

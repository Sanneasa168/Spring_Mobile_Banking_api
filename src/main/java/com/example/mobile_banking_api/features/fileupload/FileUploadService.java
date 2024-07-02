package com.example.mobile_banking_api.features.fileupload;

import jakarta.validation.constraints.FutureOrPresent;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileUploadService {

    void deletedByFileName(String fileName);

   List<FileUploadResponse> uploadMultiple(List<MultipartFile> files);

   FileUploadResponse upload(MultipartFile file);


}

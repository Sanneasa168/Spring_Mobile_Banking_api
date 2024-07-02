package com.example.mobile_banking_api.features.fileupload;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.lang.management.MemoryUsage;
import java.util.List;

@RestController
@RequestMapping("/api/v1/upload")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileUploadService fileUploadService;

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{fileName}")
    void  deleteByFileName(@PathVariable String fileName){
        fileUploadService.deletedByFileName(fileName);
    }
    
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    FileUploadResponse upload(@RequestPart MultipartFile file){
        return fileUploadService.upload(file);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/multiple")
    List<FileUploadResponse>  uploadMultiple(@RequestPart List<MultipartFile> files){

        System.out.println("Test upload Multiple ");
        return  fileUploadService.uploadMultiple(files);
    }
}

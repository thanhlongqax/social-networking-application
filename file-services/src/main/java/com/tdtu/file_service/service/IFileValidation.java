package com.tdtu.file_service.service;

import com.tdtu.file_service.enums.EFileUploadStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

public interface IFileValidation {
    public EFileUploadStatus validate(MultipartFile[] files);
    public boolean isCorrectFormat(MultipartFile file);
}

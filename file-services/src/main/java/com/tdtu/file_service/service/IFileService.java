package com.tdtu.file_service.service;

import com.tdtu.file_service.enums.EUploadFolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IFileService {
    String uploadFile(MultipartFile multipartFile, EUploadFolder folder) throws IOException;
    List<String> uploadMultipleFile(MultipartFile[] multipartFiles, EUploadFolder folder) throws IOException;
    boolean deleteFile(String url, EUploadFolder folder) throws IOException;
    String updateFile(String oldUrl, MultipartFile newFile, EUploadFolder folder) throws IOException;
}

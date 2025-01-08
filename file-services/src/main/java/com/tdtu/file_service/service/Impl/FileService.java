package com.tdtu.file_service.service.Impl;

import com.tdtu.file_service.enums.EFileUploadStatus;
import com.tdtu.file_service.enums.EUploadFolder;
import com.tdtu.file_service.service.IFileService;
import com.cloudinary.Cloudinary;
import com.tdtu.file_service.service.IFileValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
@Service
@RequiredArgsConstructor
@Slf4j
public class FileService implements IFileService {
    private final IFileValidation filesValidation;
    private final Cloudinary cloudinary;
    @Override
    public String uploadFile(MultipartFile multipartFile, EUploadFolder folder) throws IOException {
        if(multipartFile.isEmpty() || Objects.requireNonNull(multipartFile.getOriginalFilename()).isEmpty()
                || !filesValidation.isCorrectFormat(multipartFile)){
            return null;
        }
        return cloudinary.uploader()
                .upload(multipartFile.getBytes(),
                        Map.of("public_id", UUID.randomUUID().toString(),
                                "folder", folder.getFolderName(),
                                "resource_type", "auto",
                                "type", "upload"))
                .get("url")
                .toString();
    }

    @Override
    public List<String> uploadMultipleFile(MultipartFile[] files, EUploadFolder folder) throws IOException {
        EFileUploadStatus eStatus = filesValidation.validate(files);
        if (Objects.requireNonNull(eStatus) == EFileUploadStatus.STATUS_EMPTY_FILE) {
            throw new IOException("Lack of files");
        }
        List<String> fileNames = new ArrayList<>();
        for (MultipartFile file : files) {
            fileNames.add(uploadFile(file, folder));
        }
        return fileNames;
    }

    @Override
    public boolean deleteFile(String url, EUploadFolder folder) throws IOException {
        String publicId = parsePublicId(url, folder);
        log.info("ton tai " + publicId );
        if(publicId != null){
            log.info(folder + url);
            return cloudinary.uploader()
                    .destroy(publicId,  Map.of(
                            "resource_type", folder.getResourceType()
                    )).get("result").equals("ok");
        }
        return false;
    }

    @Override
    public String updateFile(String oldUrl, MultipartFile newFile, EUploadFolder folder) throws IOException {
        if(newFile.isEmpty() || Objects.requireNonNull(newFile.getOriginalFilename()).isEmpty()
                || !filesValidation.isCorrectFormat(newFile)){
            return null;
        }
        String publicId = parsePublicId(oldUrl, folder);
        return cloudinary.uploader()
                .upload(newFile.getBytes(),
                        Map.of("public_id", publicId != null ? publicId : UUID.randomUUID().toString(),
                                "invalidate", true,
                                "resource_type", "auto"))
                .get("url")
                .toString();
    }

    private String parsePublicId(String url, EUploadFolder folder){
        if(url.contains("http://res.cloudinary.com/dwkryoyyc")){
            String[] splitUrl = url.split("/");
            return Strings.concat(
                    folder.getFolderName() + "/",
                    splitUrl[splitUrl.length - 1].split("\\.")[0]
            );
        }
        return null;
    }
}

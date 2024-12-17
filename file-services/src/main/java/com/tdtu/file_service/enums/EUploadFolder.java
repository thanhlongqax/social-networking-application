package com.tdtu.file_service.enums;

import lombok.Getter;

@Getter
public enum EUploadFolder {
    FOLDER_VIDEO("tkids/videos", "video"),
    FOLDER_IMG("tkids/images", "image"),
    FOLDER_OTHERS("tkids/others", "raw");
    private final String folderName;
    private final String resourceType;
    EUploadFolder(String folderName, String resourceType){
        this.folderName = folderName;
        this.resourceType = resourceType;
    }
}

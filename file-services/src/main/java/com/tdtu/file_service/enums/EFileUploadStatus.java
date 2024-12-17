package com.tdtu.file_service.enums;

public enum EFileUploadStatus {
    STATUS_OK(0),
    STATUS_EMPTY_FILE(1),
    STATUS_WRONG_EXT(2);

    private final int status;
    EFileUploadStatus(int status){
        this.status = status;
    }

    public int getStatus(){
        return this.status;
    }
}

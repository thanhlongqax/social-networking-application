package com.tdtu.friend_recommendation_services.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
@Getter
public enum EFileType {
    TYPE_IMG("img"), TYPE_VIDEO("video");

    private final String type;
}

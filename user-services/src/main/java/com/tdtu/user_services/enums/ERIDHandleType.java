package com.tdtu.user_services.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ERIDHandleType {
    TYPE_CREATE("create"), TYPE_ADD("add"), TYPE_REMOVE("remove");
    private final String name;
}

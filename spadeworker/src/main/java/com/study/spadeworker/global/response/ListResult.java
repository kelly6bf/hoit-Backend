package com.study.spadeworker.global.response;

import lombok.Getter;

import java.util.List;

@Getter
public class ListResult<T> extends CommonResult {

    private List<T> data;

    public ListResult(Boolean isSuccess, Integer status, String message, List<T> data) {
        super(isSuccess, status, message);
        this.data = data;
    }
}

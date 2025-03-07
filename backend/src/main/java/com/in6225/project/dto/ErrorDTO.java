package com.in6225.project.dto;

import com.in6225.project.exception.ErrorCode;
import com.in6225.project.exception.SystemErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorDTO<T> {
    private Integer code;
    private String message;
    private T data;

    public ErrorDTO(ErrorCode errorCode, String msg, T data) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMsg();
        this.message = (msg != null && !msg.isEmpty()) ? msg : errorCode.getMsg(); // 使用传入的 msg 或者默认消息
        this.data = data;
    }

    public static <T> ErrorDTO<T> of(ErrorCode errorCode) {
        return new ErrorDTO<>(errorCode, null, null); // 不传 msg，使用默认消息
    }

    public static <T> ErrorDTO<T> of(ErrorCode errorCode, String msg) {
        return new ErrorDTO<>(errorCode, msg, null);
    }

    public static <T> ErrorDTO<T> of(ErrorCode errorCode, String msg, T data) {
        return new ErrorDTO<>(errorCode, msg, data);
    }

}

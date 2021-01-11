package com.fw.core.exception;

/**
 * @author yqf
 * @date 2020 上午10:51
 */
public class FileException extends RuntimeException {

    public FileException(String message) {
        super(message);
    }

    public FileException(String message, Throwable cause) {
        super(message, cause);
    }
}


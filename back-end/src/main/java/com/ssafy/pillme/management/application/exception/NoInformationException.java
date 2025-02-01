package com.ssafy.pillme.management.application.exception;

import com.ssafy.pillme.global.code.ErrorCode;
import com.ssafy.pillme.global.exception.CommonException;

public class NoInformationException extends CommonException {
    public NoInformationException(final ErrorCode errorCode) {
        super(errorCode);
    }
}

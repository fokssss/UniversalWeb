package com.yonyou.xjson.exception;

import xyy.http.framework.XBusinessException;

/**
 * Created by xyy on 2017/6/2.
 */
public class YYIMException extends XBusinessException {
    private int errorCode = 1;

    public YYIMException(String message) {
        super(message);
    }

    public YYIMException(int code, String message) {
        super(message);
        if (code < 1) {
            code = 1;
        }
        errorCode = code;
    }

    public int getErrorCode() {
        return errorCode;
    }
}

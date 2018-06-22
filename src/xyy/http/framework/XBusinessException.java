package xyy.http.framework;

/**
 * Created by xyy on 2017/4/14.
 */
public class XBusinessException extends Exception {

    private int errorCode = 1;

    public XBusinessException(String message) {
        super(message);
    }

    public XBusinessException(int code, String message) {
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

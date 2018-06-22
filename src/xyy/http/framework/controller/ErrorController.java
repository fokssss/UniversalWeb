package xyy.http.framework.controller;

import xyy.http.framework.XJSONServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by xyy on 2017/3/26.
 */
public class ErrorController implements IController {

    private String errorMessage = "";

    public ErrorController(String message) {
        errorMessage = message;
    }

    @Override
    public String exec(String method, XJSONServlet servlet, HttpServletRequest req, HttpServletResponse resp) {
        return errorMessage;
    }

}

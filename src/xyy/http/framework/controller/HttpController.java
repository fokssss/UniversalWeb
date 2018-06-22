package xyy.http.framework.controller;

import xyy.http.framework.XJSONServlet;
import xyy.http.framework.XBusinessException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by xyy on 2017/4/5.
 */
public class HttpController implements IController {

    protected XJSONServlet servlet;

    protected HttpServletRequest request;
    protected HttpServletResponse response;

    @Override
    public String exec(String method, XJSONServlet servlet, HttpServletRequest req, HttpServletResponse resp)
            throws XBusinessException {
        this.servlet = servlet;
        this.request = req;
        this.response = resp;

        try {
            Method invoker = this.getClass().getMethod(method, new Class[]{});
            return invoker.invoke(this).toString();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return XJSONServlet.getERRORResult(e.getClass().getName() + " - " + e.getMessage());

        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return XJSONServlet.getERRORResult(e.getClass().getName() + " - " + e.getMessage());

        } catch (InvocationTargetException e) {
            e.printStackTrace();
            if (e.getTargetException() instanceof XBusinessException) {
                return XJSONServlet.getERRORResult(e.getTargetException().getMessage());
            }
            Throwable target = e.getTargetException();
            if (target == null) {
                return XJSONServlet.getERRORResult(e.getClass().getName() + " - " + e.getMessage());
            } else {
                return XJSONServlet.getERRORResult(target.getClass().getName() + " - " + target.getMessage());
            }
        }
    }
}

package xyy.http.framework.controller;

import xyy.http.framework.XBodyData;
import xyy.http.framework.XBusinessException;
import xyy.http.framework.XJSONServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by xyy on 2017/4/22.
 */
public class XHttpController extends HttpController {

    @Override
    public String exec(String method,
                       XJSONServlet servlet,
                       HttpServletRequest req,
                       HttpServletResponse resp) {
        this.servlet = servlet;
        this.request = req;
        this.response = resp;

        try {
            Method invoker = this.getClass().getMethod(method, new Class[]{XBodyData.class});
            Object[] params = new Object[]{servlet.getBodyData(request, response)};
            return invoker.invoke(this, params).toString();
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
        } catch (XBusinessException e) {
            return XJSONServlet.getERRORResult(e.getMessage());
        }
    }
}

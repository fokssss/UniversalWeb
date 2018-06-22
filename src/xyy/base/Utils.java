package xyy.base;

import xyy.entity.EntityBase;
import xyy.http.framework.XJSONServlet;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by xyy on 2017/3/28.
 */
public class Utils {
    public static String genericKey() {
        String rs = UUID.randomUUID().toString();
        return rs.replace("-", "");
    }

    public static String returnResult(String inspectorResult, Object data) {
        if (EntityBase.isSuccessed(inspectorResult)) {
            return XJSONServlet.getSUCCESSResult(data);
        } else {
            return XJSONServlet.getERRORResult(inspectorResult);
        }
    }

    public static int getDays() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return Integer.parseInt(sdf.format(date));
    }
}

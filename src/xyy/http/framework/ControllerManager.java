package xyy.http.framework;

import org.apache.ibatis.io.Resources;
import org.json.JSONArray;
import org.json.JSONObject;
import xyy.base.TextUtils;
import xyy.http.framework.controller.ErrorController;
import xyy.http.framework.controller.IController;

import java.io.Reader;
import java.util.HashMap;

/**
 * Created by xyy on 2017/3/26.
 */
public class ControllerManager {
    private static HashMap<String, String> controllers = new HashMap<>();

    static {

        try {
            Reader reader = Resources.getResourceAsReader("Controller.json");
            char[] buffer = new char[4096];
            int count = reader.read(buffer);
            reader.close();

            String s = String.valueOf(buffer, 0, count);
            JSONArray json = new JSONArray(s);
            for (int i = 0; i < json.length(); i++) {
                JSONObject item = (JSONObject) json.get(i);
                controllers.put(item.optString("name"), item.optString("controller"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private static HashMap<String, IController> controllerPool = new HashMap<>();

    public static IController getController(String name) {
        String clsname = controllers.get(name);
        if (TextUtils.isEmpty(clsname)) {
            return new ErrorController("empty name");
        }
//        if (controllerPool.containsKey(clsname)) {
//            return controllerPool.get(clsname);
//        }
        try {
            IController controller = (IController) Class.forName(clsname).newInstance();
//            controllerPool.put(clsname, controller);
            return controller;
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorController(e.getClass().getName() + " - " + e.getMessage());
        }
    }
}

package xyy.base;

import java.util.HashMap;

/**
 * Created by xyy on 2017/3/26.
 */
public class TextUtils {

    public static boolean isEmpty(String s) {
        if (s == null) {
            return true;
        }
        return s.isEmpty();
    }

    public static String getString(HashMap<String, ?> data, String key, String defvalue) {
        Object value = data.get(key);
        if (value == null) {
            return defvalue;
        }
        return value.toString();
    }

    public static int getInt(HashMap<String, ?> data, String key, int defvalue) {
        Object value = data.get(key);
        if (value == null) {
            return defvalue;
        }
        try {
            return Integer.parseInt(value.toString());
        } catch (Exception e) {
            return defvalue;
        }
    }

    public static long getLong(HashMap<String, ?> data, String key, long defvalue) {
        Object value = data.get(key);
        if (value == null) {
            return defvalue;
        }
        try {
            return Long.parseLong(value.toString());
        } catch (Exception e) {
            return defvalue;
        }
    }

    public static double getDouble(HashMap<String, ?> data, String key, double defvalue) {
        Object value = data.get(key);
        if (value == null) {
            return defvalue;
        }
        try {
            return Double.parseDouble(value.toString());
        } catch (Exception e) {
            return defvalue;
        }
    }
}

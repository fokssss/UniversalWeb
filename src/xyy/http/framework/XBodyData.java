package xyy.http.framework;

import org.json.JSONArray;
import org.json.JSONObject;
import xyy.base.TextUtils;

import java.util.DoubleSummaryStatistics;
import java.util.HashMap;

/**
 * Created by xyy on 2017/3/29.
 */
public class XBodyData {

    private HashMap<String, String> innerData = new HashMap<>();


    public void put(String fieldName, String data) {
        innerData.put(fieldName, data);
    }

    public String get(String key) {
        return innerData.get(key);
    }

    public HashMap<String, ?> getData() {
        return innerData;
    }

    public String getString(String key) {
        return TextUtils.getString(innerData, key, "");
    }

    public String getString(String key, String defValue) {
        return TextUtils.getString(innerData, key, defValue);
    }

    public int getInt(String key) {
        return TextUtils.getInt(innerData, key, 0);
    }

    public int getInt(String key, int defaultValue) {
        return TextUtils.getInt(innerData, key, defaultValue);
    }

    public JSONArray getJSONArray(String key) {
        String s = this.getString(key, "[]");
        try {
            return new JSONArray(s);
        } catch (Exception e) {
            return new JSONArray();
        }
    }

    public JSONObject getJSONObject(String key) {
        String s = this.getString(key, "{}");
        try {
            return new JSONObject(s);
        } catch (Exception e) {
            return new JSONObject();
        }
    }

    public boolean getBoolean(String key, boolean defValue) {
        String value = this.getString(key, "" + defValue).toLowerCase();
        return value.equals("true") || value.equals("yes") || value.equals("1");
    }

    public long getLong(String key, long defValue) {
        String s = this.getString(key, "" + defValue);
        try {
            return Long.parseLong(s);
        } catch (Exception e) {

        }
        return defValue;
    }

    public double getDouble(String key, double defValue) {
        String s = this.getString(key, "" + defValue);
        try {
            return Double.parseDouble(s);
        } catch (Exception e) {

        }
        return defValue;
    }

    public void set(String key, String value) {
        innerData.put(key, value);
    }
}

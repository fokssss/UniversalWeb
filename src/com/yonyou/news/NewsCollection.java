package com.yonyou.news;

import org.apache.http.util.TextUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by xyy on 2018/6/27.
 */
public class NewsCollection {

    private HashMap<String, NewsObject> data = new HashMap<>();

    public void put(NewsObject news) {
        if (TextUtils.isEmpty(news.getTitle())) {
            return;
        }
        data.put(news.getCode(), news);
    }


    //    [
//    {
//        "id": "12345",
//            "title": "xxxxxxxxx",
//            "icon": "xxxxxxx.png"
//    },
//    {
//        "id": "12345",
//            "title": "xxxxxxxxx",
//            "icon": "xxxxxxx.png"
//    }
//   ]
    public JSONArray toJSONArray(boolean includedContent) {
        JSONArray rs = new JSONArray();
        for (NewsObject item : data.values()) {
            JSONObject j = new JSONObject();
            j.put("id", item.getCode());
            j.put("title", item.getTitle());
            j.put("icon", item.getIcon());
            if (includedContent) {
                j.put("content", item.getContent());
            }
            rs.put(j);
        }
        return rs;
    }

    public JSONArray toJSONArray() {
        return toJSONArray(false);
    }

    public NewsCollection find(String key) {
        NewsCollection rs = new NewsCollection();
        for (NewsObject item : data.values()) {
            if (item.getTitle().indexOf(key) > 0) {
                rs.put(item);
            }
        }
        return rs;
    }

    public NewsObject get(String code) {
        if (data.containsKey(code)) {
            return data.get(code);
        }
        return null;
    }
}

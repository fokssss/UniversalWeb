package com.yonyou.news;

import org.json.JSONObject;

/**
 * Created by xyy on 2018/6/27.
 */
public class NewsObject {

    private String code;
    private String title;
    private String icon;
    private String content;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public NewsObject(String code, String title, String icon, String content) {
        setCode(code);
        setTitle(title);
        setIcon(icon);
        setContent(content);
    }

    public JSONObject toJSONObject() {
        JSONObject rs = new JSONObject();
        rs.put("id", this.getCode());
        rs.put("title", this.getTitle());
        rs.put("icon", this.getIcon());
        rs.put("content", this.getContent());
        return rs;
    }
}

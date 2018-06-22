package com.yonyou.digi;

/**
 * Created by xyy on 2018/6/13.
 */
public class DigiObject {

    private String token = "";

    private long createTime = System.currentTimeMillis();

    public DigiObject(String token) {
        this.token = token;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

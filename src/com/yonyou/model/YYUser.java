package com.yonyou.model;

import xyy.base.TextUtils;
import xyy.entity.EntityBase;
import xyy.entity.FieldInfo;

import java.util.HashMap;

/**
 * CSLog 实体类
 * Mon Jul 17 14:31:49 CST 2017 xiongyy
 */


public class YYUser extends EntityBase {

    public YYUser() {
        super.setEntityName("YYUser");
        super.setPackageName("com.yonyou.model");
        super.setMappingName("com.yonyou.mapping.YYUser_MAPPING");
        super.addField("pkid", new FieldInfo("pkid", "INT", "int"));
        super.addField("user_info", new FieldInfo("user_info", "VARCHAR", "String"));
        super.addField("iflykey", new FieldInfo("iflykey", "VARCHAR", "String"));
    }


    private int pkid = -1;
    private String user_info;
    private String iflykey;

    public void setPkid(int pkid) {
        this.pkid = pkid;
    }

    public int getPkid() {
        return pkid;
    }

    public String getUserInfo() {
        return user_info;
    }

    public void setUserInfo(String user_info) {
        this.user_info = user_info;
    }

    public String getIflykey() {
        return iflykey;
    }

    public void setIflykey(String iflykey) {
        this.iflykey = iflykey;
    }

    public static YYUser from(YYUser from, HashMap<String, ?> data) {
        YYUser rs = new YYUser();
        rs.setPkid(TextUtils.getInt(data, "pkid", from.getPkid()));
        rs.setUserInfo(TextUtils.getString(data, "user_info", from.getUserInfo()));
        rs.setIflykey(TextUtils.getString(data, "iflykey", from.getIflykey()));
        return rs;
    }

    public static YYUser from(HashMap<String, ?> data) {
        YYUser rs = new YYUser();
        rs.setPkid(TextUtils.getInt(data, "pkid", -1));
        rs.setUserInfo(TextUtils.getString(data, "user_info", ""));
        rs.setIflykey(TextUtils.getString(data, "iflykey", ""));
        return rs;
    }

}


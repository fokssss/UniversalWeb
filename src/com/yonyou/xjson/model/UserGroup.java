package com.yonyou.xjson.model;

import com.yonyou.xjson.controller.F2FController;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashSet;

/**
 * Created by xyy on 2017/6/2.
 */
public class UserGroup extends HashSet<String> {

    private String groupId = "";

    private String uuid = "";
    private long datetime;

    public UserGroup() {
        this.datetime = System.currentTimeMillis();
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public JSONObject toJSONObject() {
        JSONObject rs = new JSONObject();
        rs.put(F2FController.KEY_UUID, getUuid());
        JSONArray users = new JSONArray();
        for (String item : this) {
            users.put(item);
        }
        rs.put(F2FController.KEY_GROUP_ID, getGroupId());
        rs.put(F2FController.KEY_USERS, users);
        return rs;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }
}

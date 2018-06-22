package com.yonyou.xjson.controller;

import com.yonyou.xjson.exception.YYIMException;
import com.yonyou.xjson.model.UserGroup;
import org.json.JSONObject;
import xyy.base.TextUtils;
import xyy.http.framework.XBusinessException;
import xyy.http.framework.XJSONServlet;
import xyy.http.framework.controller.HttpController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * Created by xyy on 2017/6/2.
 */
public class F2FController extends HttpController {
    public static final String KEY_UUID = "uuid";
    public static final String KEY_USERS = "users";
    public static final String KEY_GROUP_ID = "groupId";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_SERIAL_NUMBER = "serialNumber";
    private static final long TIME_LENGTH = 1000 * 60 * 60 * 2; //两小时

    private static HashMap<String, UserGroup> userGroup = new HashMap<>();

    @Override
    public String exec(String method, XJSONServlet servlet,
                       HttpServletRequest req, HttpServletResponse resp) throws XBusinessException {
        super.exec(method, servlet, req, resp);
        if (method.equals("joinGroup")) {
            return joinGroup();
        }
        if (method.equals("getGroupId")) {
            return getGroupId();
        }
        if (method.equals("updateGroupId")) {
            return updateGroupId();
        }
        return XJSONServlet.getERRORResult(99, "没有这个方法");
    }

    private String updateGroupId() throws YYIMException {
        String uuid = request.getParameter(KEY_UUID);
        String groupId = request.getParameter(KEY_GROUP_ID);

        if (TextUtils.isEmpty(uuid)) {
            throw new YYIMException("UUID不能为空");
        }
        UserGroup group = getUserGroup(uuid);
        if (group == null) {
            throw new YYIMException("无效的uuid");
        }
        if (TextUtils.isEmpty(group.getGroupId())) {
            group.setGroupId(groupId);
        }
        if (group.getGroupId().equals("-1")) {
            group.setGroupId(groupId);
        }
        JSONObject rs = new JSONObject();
        rs.put(KEY_GROUP_ID, group.getGroupId());
        return XJSONServlet.getSUCCESSResult(rs);
    }

    private String getGroupId() throws YYIMException {
        String uuid = request.getParameter(KEY_UUID);
        if (TextUtils.isEmpty(uuid)) {
            throw new YYIMException("UUID不能为空");
        }
        UserGroup group = getUserGroup(uuid);
        if (group == null) {
            throw new YYIMException("无效的uuid");
        }
        JSONObject rs = new JSONObject();
        rs.put(KEY_GROUP_ID, group.getGroupId());
        return XJSONServlet.getSUCCESSResult(rs);
    }

    private String joinGroup() throws YYIMException {
        String uuid = request.getParameter(KEY_UUID);
        if (TextUtils.isEmpty(uuid)) {
            String location = request.getParameter(KEY_LOCATION);
            String serial = request.getParameter(KEY_SERIAL_NUMBER);
            if (TextUtils.isEmpty(serial)) {
                throw new YYIMException(KEY_SERIAL_NUMBER + "不能是空");
            }
            //{"latitude": 100.1, "longitude": 80.9}
            try {
                JSONObject loc = new JSONObject(location);
                double latitude = loc.optDouble("latitude");
                double longitude = loc.optDouble("longitude");
                uuid = getUUID(serial, latitude, longitude);
                UserGroup group = getUserGroup(uuid);
                if (group != null) {
                    //更新一下有效时间
                    group.setDatetime(System.currentTimeMillis());
                }
            } catch (Exception e) {
                throw new YYIMException(e.getMessage());
            }
        }
        String userId = request.getParameter("userId");
        return joinGroupByUUID(uuid, userId);
    }

    private String getUUID(String serial, double latitude, double longitude) {
        double s = 50d;
        long lat = Math.round(latitude * s);
        long lng = Math.round(longitude * s);
        return serial + lat + lng;
    }

    private synchronized String joinGroupByUUID(String uuid, String userId) {
        UserGroup group = getUserGroup(uuid);
        if (group == null) {
            group = new UserGroup();
            group.setUuid(uuid);
            userGroup.put(uuid, group);
        }
        if (!TextUtils.isEmpty(userId)) {
            group.add(userId);
        }
        return XJSONServlet.getSUCCESSResult(group.toJSONObject());
    }

    private UserGroup getUserGroup(String uuid) {
        UserGroup group = userGroup.get(uuid);
        if (group != null) {
            if (System.currentTimeMillis() - group.getDatetime() > TIME_LENGTH) {
                userGroup.remove(uuid);
                return null;
            }
        }
        return group;
    }
}

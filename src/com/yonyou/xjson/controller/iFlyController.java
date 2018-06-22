package com.yonyou.xjson.controller;

import com.yonyou.model.YYUser;
import xyy.entity.EntityBase;
import xyy.http.framework.XBodyData;
import xyy.http.framework.XBusinessException;
import xyy.http.framework.XJSONServlet;
import xyy.http.framework.controller.XHttpController;

/**
 * Created by xyy on 2017/9/8.
 */
public class iFlyController extends XHttpController {

    public String registerIFlyUser(XBodyData data) {
        YYUser user = YYUser.from(data.getData());
        user.setPkid(-1);
        return EntityBase.autoReturn(user.save(), user);
    }

    public String getUserByIFly(XBodyData data) throws XBusinessException {
        YYUser user = EntityBase.queryOne(YYUser.class, "selectByiFlykey", data.getString("iflykey"));
        if (user == null) {
            throw new XBusinessException("没有找到用户 - " + data.getString("iflykey"));
        }
        return XJSONServlet.getSUCCESSResult(user);
    }
}

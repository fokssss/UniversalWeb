package com.yonyou.xjson.controller;

import com.baidu.aip.face.AipFace;
import com.baidu.aip.util.Util;
import org.json.JSONObject;
import xyy.base.TextUtils;
import xyy.base.Utils;
import xyy.http.framework.XBodyData;
import xyy.http.framework.XBusinessException;
import xyy.http.framework.controller.XHttpController;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by xyy on 2017/9/4.
 */
public class BaiduFaceController extends XHttpController {


    //设置APPID/AK/SK
    private static final String APP_ID = "9230316";
    private static final String API_KEY = "rGf7Mx8pfnEO7tOl23cyEh2U";
    private static final String SECRET_KEY = "YmGByQ9ct1m3nbQsTFhLIq39Y1wOUPz0";
    private static final String DEFAULT_GROUP = "default";

    private static AipFace client = null;

    static {

        // 初始化一个FaceClient
        client = new AipFace(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 调用API
//        String image = "test.jpg";
//        JSONObject res = client.detect(path, new HashMap<String, String>());
//        System.out.println(res.toString(2));
    }

    public String addUser(XBodyData data) throws XBusinessException {
//        String imagePath = data.getString("image").replaceAll("SM_", "");
//        if (TextUtils.isEmpty(imagePath)) {
        String imagePath = data.getString("imgs0").replaceAll("SM_", "");
//        }
        String group = data.getString("group", DEFAULT_GROUP);
        String userInfo = data.getString("userInfo");
        String uid = data.getString("uid");
        if (TextUtils.isEmpty(uid)) {
            throw new XBusinessException("uid不能为空");
        }

        HashMap<String, String> options = new HashMap<String, String>();
        JSONObject res = client.addUser(uid, userInfo, Arrays.asList(group), imagePath, options);
        res.put("group", group);
        res.put("userInfo", userInfo);
        res.put("uid", uid);
        res.put("image", imagePath);
        return res.toString();
    }

    public String getUser(XBodyData data) {
        String group = data.getString("group", DEFAULT_GROUP);
        String uid = data.getString("uid");
        // 查询一个用户在所有组内的信息
//        JSONObject res = client.getUser("uid1");
//        System.out.println(res.toString(2));

        // 查询一个用户在指定组内的信息
        JSONObject res = client.getUser(uid, Arrays.asList(group));
        return res.toString();
    }

    public String findUser(XBodyData data) {
        String imagePath = data.getString("image").replaceAll("SM_", "");
        String group = data.getString("group", DEFAULT_GROUP);

        HashMap<String, Object> options = new HashMap<String, Object>(1);
        options.put("user_top_num", 1);
        JSONObject res = client.identifyUser(Arrays.asList(group), imagePath, options);
        return res.toString();
    }


    public String deleteUser(XBodyData data) throws XBusinessException {
        String group = data.getString("group", DEFAULT_GROUP);
        String uid = data.getString("uid");
        if (TextUtils.isEmpty(uid)) {
            throw new XBusinessException("uid不能为空");
        }
        // 只从指定组中删除用户
        JSONObject res = client.deleteUser(uid, Arrays.asList(group));
        return res.toString();
    }

    public String joinGroup(XBodyData data) {
        String imagePath = data.getString("image").replaceAll("SM_", "");
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("max_face_num", "1");
        options.put("face_fields", data.getString("face_fields"));
//        String imagePath = "picture.jpg";
        JSONObject response = client.detect(imagePath, options);
//        System.out.println(response.toString());
//
//        // 参数为本地图片文件二进制数组
//        byte[] file = readImageFile(imagePath);    // readImageFile函数仅为示例
//        JSONObject response = client.detect(file);
//        System.out.println(response.toString());
        return response.toString();
    }

    private byte[] readImageFile(String path) {
        byte[] buffer = null;
        try {
            File file = new File(path);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }
}

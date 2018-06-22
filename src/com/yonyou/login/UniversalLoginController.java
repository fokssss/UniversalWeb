package com.yonyou.login;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import xyy.base.TextUtils;
import xyy.http.framework.XBodyData;
import xyy.http.framework.XJSONServlet;
import xyy.http.framework.controller.XHttpController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.Buffer;
import java.nio.charset.Charset;

/**
 * Created by xyy on 2018/6/22.
 */
public class UniversalLoginController extends XHttpController {

    public static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
    private static String WX_URL = "https://api.weixin.qq.com/sns/jscode2session?" +
            "appid=%s" +
            "&secret=%s" +
            "&js_code=%s" +
            "&grant_type=authorization_code";
    //011L5m4N1oqFX41Szv7N1A9b4N1L5m4q    (js_code)
    private static String WX_APP_ID = "wx08d0d04b4ba0fcca";

    private static String WX_SECRET = "93cb740698a9eaa363fbfc02fc13417f";

    /**
     * @param data
     * @return
     */
    public String loginWX(XBodyData data) throws IOException {
        String jscode = data.getString("js_code");
        if (TextUtils.isEmpty(jscode)) {
            return XJSONServlet.getERRORResult("js_code is empty");
        }
        String url = String.format(WX_URL, WX_APP_ID, WX_SECRET, jscode);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpGet);

            System.out.println(response.getStatusLine());
            HttpEntity entity1 = response.getEntity();

            String s = EntityUtils.toString(entity1, UTF8_CHARSET);
            JSONObject r = new JSONObject(s);
            /*
            r 的值
            {
                "session_key": "B0U+4KZA6U0Pa997Ewyhdw==",
                    "openid": "oNfl65dc2fx6PyRX9Rw-EEj1If2Q"
            }*/
            EntityUtils.consume(entity1);
            String openid = r.optString("openid");
            if(TextUtils.isEmpty(openid)) {
                //获取 openid 失败了
                return XJSONServlet.getERRORResult(s);
            }
            return XJSONServlet.getERRORResult(10009, "当前微信帐号未绑定到友户通帐号");
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return XJSONServlet.getSUCCESSResult("");
    }
}

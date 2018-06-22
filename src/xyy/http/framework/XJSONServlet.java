package xyy.http.framework;

import com.google.gson.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONArray;
import org.json.JSONObject;
import xyy.base.TextUtils;
import xyy.base.image.ImageZipUtil;
import xyy.http.framework.controller.IController;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by xyy on 2017/3/26.
 */
public class XJSONServlet extends HttpServlet {


    public static final int SUCCESS = 0;
    public static final int ERROR = 1;
    public static final int ERROR_SYSTEM = 99;

    private static final int BUFFER_SIZE = 1024 * 6; //缓存区大小
    private static final long MAX_SIZE = 1024 * 1024 * 8;//设置文件的大小为2M
    private static final String ALLOW_EXT_NAMES = "png,jpeg,jpg,gif,bmp,rar,rar,txt,docx";
    public static final String KEY_DATA = "data";
    public static final String PATH_UPLOAD = "/upload/";


    private static Gson gson = null;

    static {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(JSONArray.class, new JsonSerializer<JSONArray>() {
            @Override
            public JsonElement serialize(JSONArray objects, Type type, JsonSerializationContext jsonSerializationContext) {
                JsonParser parser = new JsonParser();
                return parser.parse(objects.toString()).getAsJsonArray();
            }
        });
        builder.registerTypeAdapter(JSONObject.class, new JsonSerializer<JSONObject>() {
            @Override
            public JsonElement serialize(JSONObject objects, Type type, JsonSerializationContext jsonSerializationContext) {
                JsonParser parser = new JsonParser();
                return parser.parse(objects.toString()).getAsJsonObject();
            }
        });
        gson = builder.create();
    }

    public static String getSUCCESSResult(Object data) {
        return getSUCCESSResultObject(data).toString();
    }

    public static JSONObject getSUCCESSResultObject(Object data) {
        JSONObject result = new JSONObject();
        result.put("code", SUCCESS);
        result.put("msg", "success");
        if (data != null) {
            if (data instanceof String || data instanceof Integer || data instanceof Boolean) {
                result.put(KEY_DATA, data.toString());
            } else if (data instanceof JSONObject) {
                result.put(KEY_DATA, data);
            } else if (data instanceof JSONArray) {
                result.put(KEY_DATA, data);
            } else {
                String s = getGson().toJson(data);
                if (s.startsWith("[")) {
                    result.put(KEY_DATA, new JSONArray(s));
                } else {
                    result.put(KEY_DATA, new JSONObject(s));
                }
            }
        }
        return result;
    }


    private static Gson getGson() {
        return gson;
    }

    public static String getERRORResult(int errorCode, String errorMessage) {
        JSONObject result = new JSONObject();
        if (errorCode == SUCCESS) {
            throw new Error("错误码不能为" + SUCCESS);
        }
        result.put("code", errorCode);
        result.put("msg", errorMessage);
        return result.toString();
    }

    public static String getERRORResult(String errorMessage) {
        return getERRORResult(ERROR, errorMessage);
    }

    public void mustPost() {

    }

    public XBodyData getBodyData(HttpServletRequest request, HttpServletResponse response) throws XBusinessException {
        XBodyData result = new XBodyData();
        String basePath = this.getServletContext().getRealPath(PATH_UPLOAD);
        ServletFileUpload servletFileUpload = new ServletFileUpload(getDiskFileFactory(basePath));
        servletFileUpload.setSizeMax(MAX_SIZE);
        try {
            List<FileItem> fileItems = servletFileUpload.parseRequest(request);
            for (FileItem fileItem : fileItems) {
                if (fileItem.isFormField()) {
                    //普通字段
                    result.put(fileItem.getFieldName(), fileItem.getString("UTF-8"));
                } else {
                    XUploadImageInfo url = saveToFile(fileItem);
                    result.put(fileItem.getFieldName(), url.getUrl());
                }
            }
        } catch (Exception e) {
//            e.printStackTrace();
//            throw new XBusinessException("getBodyData" + e.getMessage());
        }
        for (Object key : request.getParameterMap().keySet()) {
            String skey = key.toString();
            result.put(skey, request.getParameter(skey));
        }
        return result;

    }

    private XUploadImageInfo saveToFile(FileItem item) throws Exception {
        XUploadImageInfo url = XUploadImageInfo.build(this, PATH_UPLOAD, item);
        if (ALLOW_EXT_NAMES.indexOf(url.getExtName()) != -1) {
            item.write(new File(url.getFileName()));
            ImageZipUtil.zipImageFile(new File(url.getFileName()), new File(url.getSmallFileName()), 80, 0, 0.5f);
//            item.write(new File(url.getSmallFile()));
        } else {
            throw new FileUploadException("file type is not allowed");
        }
        return url;
    }

    private DiskFileItemFactory getDiskFileFactory(String basePath) {
        File repository = new File(basePath); //缓存区目录
        if (!repository.exists()) {
            if (!repository.mkdirs()) {
                throw new Error("创建目录失败 - " + basePath);
            }
        } else if (!repository.isDirectory()) {
            throw new Error(basePath + "已经存在，并且不是目录");
        }
        DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
        diskFileItemFactory.setRepository(repository);
        diskFileItemFactory.setSizeThreshold(BUFFER_SIZE);
        return diskFileItemFactory;
    }

    private enum REQ_TYPE {
        POST, GET
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String s = xjsonDispatcher(REQ_TYPE.POST, req, resp);
        ServletOutputStream out = resp.getOutputStream();
        out.write(s.getBytes("UTF-8"));
        out.flush();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String s = xjsonDispatcher(REQ_TYPE.GET, req, resp);
        ServletOutputStream out = resp.getOutputStream();
        out.write(s.getBytes("UTF-8"));
        out.flush();
    }

    private String xjsonDispatcher(REQ_TYPE type, HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.setCharacterEncoding("UTF-8");//防止中文名乱码
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=utf-8");
        InvokerObject rs = getInvokerObject(req);
        IController controller = ControllerManager.getController(rs.getClsname());
        try {
            return controller.exec(rs.getMethod(), this, req, resp);
        } catch (XBusinessException e) {
            return XJSONServlet.getERRORResult(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return XJSONServlet.getERRORResult(ERROR_SYSTEM, e.getMessage());
        }
    }

    private InvokerObject getInvokerObject(HttpServletRequest req) {
        String b = req.getRequestURI();
//        String c = req.getServletPath();
//        String urlPath = b.substring(c.length());
//        System.out.println("url path is " + urlPath);
        String[] ww = b.split("/");

        InvokerObject rs = new InvokerObject();
        for (int i = ww.length - 1; i >= 0; i--) {
            if (TextUtils.isEmpty(ww[i])) {
                continue;
            }
            if (TextUtils.isEmpty(rs.getMethod())) {
                rs.setMethod(ww[i].trim());
                continue;
            }
            rs.setClsname(ww[i].trim());
            break;
        }
        return rs;
    }

}

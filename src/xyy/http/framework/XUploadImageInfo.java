package xyy.http.framework;

import org.apache.commons.fileupload.FileItem;
import xyy.base.Utils;

import java.io.File;

/**
 * Created by xyy on 2017/4/12.
 */
public class XUploadImageInfo {

    //    private static final String ROOT_URL = "/Users/xyy/yygit/f2f/out/artifacts/f2f_war_exploded";
    private static final String ROOT_URL = "http://47.92.67.238:7080/f2f";
    private String mFileName = "";
    private String url = "";
    private String extName = "";
    private String bigFile;
    private String smallFile;
    private String smallFileName = "";

    public String getFileName() {
        return mFileName;
    }

    public void setFileName(String filename) {
        this.mFileName = filename;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static XUploadImageInfo build(XJSONServlet servlet, String path, FileItem item) {
        XUploadImageInfo url = new XUploadImageInfo();

        String filePath = item.getName();
        String fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
        url.extName = filePath.substring(filePath.lastIndexOf(".") + 1);
        String fname = fileName.replace(".", "");
        fname = fname.length() > 8 ? fname.substring(0, 8) : fname;
        fname = fname + Utils.genericKey();
        url.bigFile = fname + "." + url.extName;
        url.smallFile = "SM_" + fname + "." + url.extName;

        String basePath = servlet.getServletContext().getRealPath(path);
        new File(basePath).getParentFile().mkdirs();

        String saveToDaFile = basePath + url.bigFile;
        String saveToXiFile = basePath + url.smallFile;

//
        url.setFileName(saveToDaFile);
        url.setSmallFileName(saveToXiFile);
        url.setUrl(XUploadImageInfo.ROOT_URL + path + url.smallFile);
        return url;
    }

    public static XUploadImageInfo build(XJSONServlet servlet, String path, String name) {
        XUploadImageInfo url = new XUploadImageInfo();

        String basePath = servlet.getServletContext().getRealPath(path);
        new File(basePath).getParentFile().mkdirs();

        url.bigFile = name;
        String saveToDaFile = basePath + url.bigFile;

        url.setFileName(saveToDaFile);
        url.setUrl(XUploadImageInfo.ROOT_URL + path + url.bigFile);
        return url;
    }


    public String getExtName() {
        return extName;
    }

    public String getBigFile() {
        return bigFile;
    }

    public void setBigFile(String bigFile) {
        this.bigFile = bigFile;
    }

    public String getSmallFile() {
        return smallFile;
    }

    public void setSmallFile(String smallFile) {
        this.smallFile = smallFile;
    }

    public String getSmallFileName() {
        return smallFileName;
    }

    public void setSmallFileName(String smallFileName) {
        this.smallFileName = smallFileName;
    }
}

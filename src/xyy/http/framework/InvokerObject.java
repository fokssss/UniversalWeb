package xyy.http.framework;

/**
 * Created by xyy on 2017/3/26.
 */
public class InvokerObject {

    private String clsname;

	
	
    private String method;

    public String getMethod() {
        return method;
    }

    public String getClsname() {
        return clsname;
    }

    public void setClsname(String clsname) {
        this.clsname = clsname;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}

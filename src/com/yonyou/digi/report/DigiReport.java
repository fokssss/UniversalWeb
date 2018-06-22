package com.yonyou.digi.report;

import com.yonyou.digi.DigiObject;

/**
 * Created by xyy on 2018/6/13.
 */
public class DigiReport extends DigiObject {

    private String name; //项目名称
    private String company; //签约公司
    private long amounts; //金额

    private String photo; //图片
    private String sign; //签名

    public DigiReport(String token) {
        super(token);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public long getAmounts() {
        return amounts;
    }

    public void setAmounts(long amounts) {
        this.amounts = amounts;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}

package xyy.entity;

/**
 * Created by xyy on 2017/4/4.
 */
public class FieldInfo {

    private String fieldName;
    private String sqlType;
    private String javaType;

    public FieldInfo(String fieldName, String sqlType, String javaType) {
        this.fieldName = fieldName;
        this.sqlType = sqlType;
        this.javaType = javaType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getSqlType() {
        return sqlType;
    }

    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }
}

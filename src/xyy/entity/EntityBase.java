package xyy.entity;


import com.google.gson.Gson;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONObject;
import xyy.http.framework.XBusinessException;
import xyy.http.framework.XJSONServlet;
import xyy.utils.MyBatis;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xyy on 2017/4/4.
 */
public abstract class EntityBase implements Serializable {

    public static final int ADD_NEW = -1;
    public static final String SUCCESS = "success";
    public static final String KEY_ADD_NEW = "addnew";
    public static final String KEY_EDIT = "edit";
    public static final String KEY_DELETE = "delete";
    public static final String ERROR = "error";
    private transient String mEntityName;
    private transient HashMap<String, FieldInfo> mFields = new HashMap<>();
    private transient String mPackageName;
    private transient String mMappingName;
    private transient int startPage;          //支持分页的扩展属性
    private transient int pageLength;

    public EntityBase() {
        setPkid(ADD_NEW);
    }

    public String getEntityName() {
        return mEntityName;
    }

    public void setEntityName(String entityName) {
        this.mEntityName = entityName;
    }

    public HashMap<String, FieldInfo> getFields() {
        return mFields;
    }

    public void setFields(HashMap<String, FieldInfo> fields) {
        this.mFields = fields;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public void setPackageName(String packageName) {
        this.mPackageName = packageName;
    }

    public String getMappingName() {
        return mMappingName;
    }

    public void setMappingName(String mappingName) {
        this.mMappingName = mappingName;
    }

    public void addField(String fieldName, FieldInfo fieldInfo) {
        this.mFields.put(fieldName, fieldInfo);
    }

    public abstract int getPkid();

    public abstract void setPkid(int value);

    public boolean isNew() {
        return getPkid() < 0;
    }

    public synchronized String save() {
        return save(true);
    }

    public synchronized String update() {
        return save(false);
    }

    /**
     * 保存并新增
     *
     * @param allowAddNew 是否允许自动新增
     * @return
     */
    public synchronized String save(boolean allowAddNew) {
        if (isNew() && !allowAddNew) {
            return "不能保存一条不存在的记录";
        }
        SqlSession session = null;

        try {
            session = MyBatis.openSession();
            if (isNew()) {
                session.insert(this.getMappingName() + ".addItem", this);
            } else {
                session.update(this.getMappingName() + ".updateItem", this);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
            e.printStackTrace();
            return e.getMessage();
        } finally {
            session.close();
        }
        return EntityBase.SUCCESS;
    }

    public static boolean isSuccessed(String value) {
        return EntityBase.SUCCESS.equals(value);
    }

    public static String autoReturn(String value, EntityBase object) {
        if (EntityBase.isSuccessed(value)) {
            return XJSONServlet.getSUCCESSResult(object);
        } else {
            return XJSONServlet.getERRORResult(value);
        }
    }

    public synchronized String delete() {
        SqlSession session = MyBatis.openSession();
        if (isNew()) {
            return "不能删除一条不存在的记录";
        }
        session.delete(this.getMappingName() + ".deleteItem", this);
        session.commit();
        session.close();
        return EntityBase.SUCCESS;
    }

    public JSONObject toJson() {
        return new JSONObject(new Gson().toJson(this));
    }

    public static <T extends EntityBase> T queryById(Class<T> cls, int pkId) throws XBusinessException {
        return queryOne(cls, "selectByID", pkId);
    }

    public static <T extends EntityBase> List<T> getAll(Class<T> cls) throws XBusinessException {
        return queryList(cls, "getAll", null);
    }

    public static <T extends EntityBase> List<T> queryList(Class<T> cls, String queryId, Object params) throws XBusinessException {
        try {
            EntityBase instance = cls.newInstance();
            SqlSession session = MyBatis.openSession();
            List<T> rs;
            if (params == null) {
                rs = session.selectList(instance.getMappingName() + "." + queryId);
            } else {
                rs = session.selectList(instance.getMappingName() + "." + queryId, params);
            }
            session.close();
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            throw new XBusinessException(e.getMessage());
        }
    }

    public static <T extends EntityBase> List<T>
    queryListByPage(Class<T> cls,
                    String queryId, EntityBase params,
                    int start, int length) throws XBusinessException {
        params.setStartPage(start);
        params.setPageLength(length);
        return queryList(cls, queryId, params);
    }

    public static <T extends EntityBase> T queryOne(Class<T> cls, String queryId, Object params) throws XBusinessException {
        List<T> rs = queryList(cls, queryId, params);
        if (rs == null || rs.size() < 1) {
            return null;
        }
        return rs.get(0);
    }

    public static <T extends EntityBase> T checkId(Class<T> cls, int pkid, String errorMessage) throws XBusinessException {
        try {
            T r = EntityBase.queryById(cls, pkid);
            if (r == null) throw new XBusinessException(errorMessage);
            return r;
        } catch (XBusinessException e) {
            e.printStackTrace();
            throw new XBusinessException(e.getMessage());
        }
    }

    public static <T extends EntityBase> int deleteById(Class<T> cls, int pkid) throws XBusinessException {
        try {
            EntityBase instance = cls.newInstance();
            instance.setPkid(pkid);
            SqlSession session = MyBatis.openSession();
            int rs = session.delete(instance.getMappingName() + ".deleteItem", instance);
            session.commit();
            session.close();
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            throw new XBusinessException(e.getMessage());
        }
    }

    public static <T extends EntityBase> int delete(Class<T> cls, String deleteId, Object params) throws XBusinessException {
        try {
            EntityBase instance = cls.newInstance();
            SqlSession session = MyBatis.openSession();
            int rs = 0;
            if (params == null) {
                rs = session.delete(instance.getMappingName() + "." + deleteId);
            } else {
                rs = session.delete(instance.getMappingName() + "." + deleteId, params);
            }
            session.commit();
            session.close();
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            throw new XBusinessException(e.getMessage());
        }
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public int getStartPage() {
        return startPage;
    }

    public void setPageLength(int pageLength) {
        this.pageLength = pageLength;
    }

    public int getPageLength() {
        return pageLength;
    }
}

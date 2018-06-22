package xyy.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by xyy on 2017/3/29.
 */
public class MyBatis {
    private static SqlSessionFactory sqlSessionFactory;
    private static Reader reader;

    static {
        try {
            reader = Resources.getResourceAsReader("Configuration.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SqlSessionFactory getSession() {
        return sqlSessionFactory;
    }

    public static SqlSession openSession() {
        if (sqlSessionFactory == null) {
            try {
                reader = Resources.getResourceAsReader("Configuration.xml");
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sqlSessionFactory.openSession();
    }
}

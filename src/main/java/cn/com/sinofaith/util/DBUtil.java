package cn.com.sinofaith.util;

/**
 * Created by Me. on 2018/5/23
 */
import javax.swing.text.html.parser.Entity;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Created by Me. on 2018/5/15
 */
public class DBUtil {
    private static final String DRIVER;
    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    private DBUtil(){}

    static {

        ResourceBundle bundle = ResourceBundle.getBundle("jdbc");

        DRIVER = bundle.getString("jdbc.driverClassName");
        URL = bundle.getString("jdbc.databaseurl");
        USER = bundle.getString("jdbc.username");
        PASSWORD = bundle.getString("jdbc.password");

        /**
         * 驱动注册
         */
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * 获取 Connetion
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() {
        Connection con = null;
        try{
            con = DriverManager.getConnection(URL, USER, PASSWORD);
        }catch (SQLException e){
            e.getStackTrace();
            System.out.println("数据库连接失败");
        }
        return con;
    }

    /**
     * 释放资源
     * @param conn
     * @param st
     * @param rs
     */
    public static void colseResource(Connection conn,Statement st,ResultSet rs) {
        closeResultSet(rs);
        closeStatement(st);
        closeConnection(conn);
    }

    /**
     * 释放连接 Connection
     * @param conn
     */
    public static void closeConnection(Connection conn) {
        if(conn !=null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //等待垃圾回收
        conn = null;
    }

    /**
     * 释放语句执行者 Statement
     * @param st
     */
    public static void closeStatement(Statement st) {
        if(st !=null) {
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //等待垃圾回收
        st = null;
    }

    /**
     * 释放结果集 ResultSet
     * @param rs
     */
    public static void closeResultSet(ResultSet rs) {
        if(rs !=null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //等待垃圾回收
        rs = null;
    }
}


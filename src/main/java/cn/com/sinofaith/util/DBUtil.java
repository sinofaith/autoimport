package cn.com.sinofaith.util;

/**
 * Created by Me. on 2018/5/23
 */
import java.sql.*;

import java.util.Properties;

/**
 * Created by Me. on 2018/5/15
 */
public class DBUtil {
    private static Connection con = null;

    public static Properties props;

    public static Connection getConn() {
        if(con == null){
            try {
                props = new Properties();
                props.load(DBUtil.class.getResourceAsStream("/jdbc.properties"));
                Class.forName(props.getProperty("jdbc.driverClassName"));
                con = (Connection) DriverManager.getConnection(props.getProperty("jdbc.databaseurl"),
                        props.getProperty("jdbc.username"), props.getProperty("jdbc.password"));

            } catch (Exception e) {
                System.out.println("数据库连接失败" + e.getMessage());
            }
        }
        return con; // 返回所建立的数据库连接
    }

    public static void main(String[] args) {
        System.out.println(getConn());
    }
}
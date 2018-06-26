package cn.com.sinofaith.util;

/**
 * Created by Me. on 2018/5/23
 */
import javax.swing.text.html.parser.Entity;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

import java.util.Properties;

/**
 * Created by Me. on 2018/5/15
 */
public class DBUtil {
    private static Connection con = null;

    private static Properties props;

    public static Connection getConn() {
        InputStream in = DBUtil.class.getResourceAsStream("/jdbc.properties");
        if(con == null){
            try {
                props = new Properties();
                props.load(in);
                Class.forName(props.getProperty("jdbc.driverClassName"));
                con = (Connection) DriverManager.getConnection(props.getProperty("jdbc.databaseurl"),
                        props.getProperty("jdbc.username"), props.getProperty("jdbc.password"));
                props.clear();
                in.close();
            } catch (Exception e) {
                System.out.println("数据库连接失败" + e.getMessage());
            }finally {
                try{
                    if(in!=null){
                        props.clear();
                        in.close();
                    }
                }catch (Exception e){
                   e.getStackTrace();
                }
            }
        }
        return con; // 返回所建立的数据库连接
    }

}
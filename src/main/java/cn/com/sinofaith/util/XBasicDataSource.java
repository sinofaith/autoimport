package cn.com.sinofaith.util;

import org.apache.commons.dbcp.BasicDataSource;

import java.sql.DriverManager;
import java.sql.SQLException;

public class XBasicDataSource extends BasicDataSource {
    @Override
    public synchronized void close() throws SQLException {
//  System.out.println("......输出数据源Driver的url："+DriverManager.getDriver(url));
        DriverManager.deregisterDriver(DriverManager.getDriver(url));
        super.close();
    }
}

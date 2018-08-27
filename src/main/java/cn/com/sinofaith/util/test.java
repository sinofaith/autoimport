package cn.com.sinofaith.util;

import cn.com.sinofaith.bean.bankBean.BankZzxxEntity;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class test {
    public static void main(String[] args) throws Exception{
//        List<BankZzxxEntity> list = new ArrayList<>();
//        BankZzxxEntity b1 = new BankZzxxEntity();
//        b1.setJyxm("肖升");
//        b1.setSfbz(null);
//        list.add(b1);
//        BankZzxxEntity b2 = new BankZzxxEntity();
//        b2.setJyxm("肖升");
//        b2.setSfbz("");
//        if("".equals(b2.getSfbz())){
//            b2.setSfbz(null);
//        }
//        System.out.println(list.contains(b2));
        Date date = HSSFDateUtil.getJavaDate(Double.valueOf("0.89"));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(date));

    }
}

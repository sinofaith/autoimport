package cn.com.sinofaith.util;



import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Created by Me. on 2018/5/22
 */
public class TimeFormatUtil {

    public static String getDate(String style){
        String time = "";
        if("/".equals(style)){
            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            time = df.format(System.currentTimeMillis());
        }else if("-".equals(style)){
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            time = df.format(System.currentTimeMillis());
        }else if("".equals(style)){
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            time = df.format(System.currentTimeMillis());
        }else {
            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            time = df.format(System.currentTimeMillis());
        }
        return time;
    }


    public static String DateFormat(String time){
        Date date = new Date(time);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(date);
    }
    public static void main(String[] args) {
        String time = "2018/01/01";
        Date data = new Date(time);
        System.out.println(data);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(data));
    }
}
package cn.com.sinofaith.util;



import java.text.SimpleDateFormat;

/**
 * Created by Me. on 2018/5/22
 */
public class TimeFormatUtil {

    public static String getDate(String style){
        String time = "";
        if(style.equals("/")){
            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            time = df.format(System.currentTimeMillis());
        }else if(style.equals("-")){
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            time = df.format(System.currentTimeMillis());
        }else if(style.equals("")){
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            time = df.format(System.currentTimeMillis());
        }else {
            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            time = df.format(System.currentTimeMillis());
        }
        return time;
    }

    public static void main(String[] args) {
    }
}
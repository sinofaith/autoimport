package cn.com.sinofaith.util;



import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Created by Me. on 2018/5/22
 */
public class TimeFormatUtil {
    private static SimpleDateFormat formatA = new SimpleDateFormat("yyyyMMdd");
    private static SimpleDateFormat formatB = new SimpleDateFormat("yyyyMMddHHmmss");
    private static SimpleDateFormat formatC = new SimpleDateFormat("yyyyMMddHHmm");
    private static SimpleDateFormat formatD = new SimpleDateFormat("yyyyMMddHH");

    public static boolean getSy(String loginTime){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");//格式化输出日期
        Date dt = null;
        try {
            dt = sdf.parse(loginTime);
        } catch (ParseException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
        if(dt.compareTo(new Date())==-1){
            return true;
        }
        return false;
    }

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

    public static String getDateSwitchTimestamp(String time){
        time = time.replaceAll("\\D","");
        int len = time.length();
        Date result =null;
        try {
            switch (len) {
                case 8:
                    result = formatA.parse(time);
                    break;
                case 10:
                    result = formatD.parse(time);
                    break;
                case 12:
                    result = formatC.parse(time);
                    break;
                case 14:
                    result = formatB.parse(time);
                    break;
                default:
                    result =null;
            }
        }catch (Exception e){
        }
        if(result == null){
            return time;
        }else {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(result);
        }
    }
    public static void main(String[] args) throws Exception{

        System.out.println(getDateSwitchTimestamp("20160811tttttt"));

        Date date = new SimpleDateFormat("yyyyMMdd").parse("20130704");
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        System.out.println(now);
        System.out.println(getDateSwitchTimestamp("1"));
        /*String time = "2018/01/01";
        Date data = new Date(time);
        System.out.println(data);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(data));*/

    }
}
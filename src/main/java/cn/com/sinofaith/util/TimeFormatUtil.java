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


    public static Date DateFormat(String time){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try{
             date = df.parse(time);
        }catch (Exception e){
            e.printStackTrace();

        }
        return date;
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

    public static String sjjg(String sdate,String sdate1){
        String result = "0";
        try {
            Date date = DateFormat(sdate);
            Date date1 = DateFormat(sdate1);
            Double resultD =(double) (date.getTime()-date1.getTime())/1000/60/60/24;
            result = String.format("%.2f", resultD);
        } catch (Exception e){
            result = "-";
        }

        return result;
    }
    public static void main(String[] args) throws Exception{
        Date a = DateFormat("");
        Date b = DateFormat("2018-04-24 01:19:10");
        System.out.println(a.compareTo(b));
        System.out.println(sjjg("2018-04-24 16:19:10","2018-04-24 01:19:10"));
    }
}
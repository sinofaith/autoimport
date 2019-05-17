package cn.com.sinofaith.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
    去除乱码
 */
public class RemoveMessy {
    public static String rMessy(String str_VarMboxRead) {
        StringBuffer str_Result = new StringBuffer();String str_OneStr = "";
        for (int z = 0; z < str_VarMboxRead.length(); z++) {
            str_OneStr = str_VarMboxRead.substring(z, z + 1);
            if (str_OneStr.matches("[\\x00-\\x7F]+")||str_OneStr.matches("[\u4e00-\u9fa5]+")) {
                str_Result = str_Result .append(str_OneStr);
            }
        }
        return str_Result.toString();
    }

    public static void main(String[] args) {
        //
//       String regTel="^(0[0-9]{2,3})?(-{0,1})([2-9][0-9]{6,7})$";
//        Pattern r = Pattern.compile(regTel);
//
//        // 现在创建 matcher 对象
//        Matcher m = r.matcher("86-755-33225898");
//        System.out.println(m.matches());
        String values = "19919866446\n";
        String sjReg = "[1](([3][0-9])|([4][5,7,9])|([5][4,6,9])|([6][6])|([7][3,5,6,7,8])|([8][0-9])|([9][8,9]))[0-9]{8}";
        String dhReg = "([2-9][0-9]{6,7})";
        Pattern sjp = Pattern.compile(sjReg);
        Matcher sjm = sjp.matcher(values);
        Pattern dhp = Pattern.compile(dhReg);
        Matcher dhm = dhp.matcher(values);
        List<String> sjhm = new ArrayList<String>();
        List<String> dhhm = new ArrayList<>();
        while (sjm.find()) {
            sjhm.add(sjm.group());
        }
        while(dhm.find()){
            dhhm.add(dhm.group());
        }
        HashSet h = new HashSet(sjhm);
        sjhm.clear();
        sjhm.addAll(h);
        h = new HashSet(dhhm);
        h.clear();
        dhhm.addAll(h);
        System.out.println(sjhm);
        System.out.println(dhhm);
    }
}

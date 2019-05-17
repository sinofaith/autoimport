package cn.com.sinofaith.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

public class ReadCsv {
    public static void main(String[] args) throws Exception{
//        File file = null;
//        //输入缓冲区
//        BufferedReader br = null;
//        FileInputStream fis = null;
//        InputStreamReader isr = null;
//        List<String> zcxxStr = null;
//        file = new File("D:\\work\\数据模型\\支付宝\\1\\湖北省黄冈市浠水县关于浠水414非法经营案-27792-交易数据.csv");
//        fis = new FileInputStream(file);
//        isr = new InputStreamReader(fis, "GBK");
//        br = new BufferedReader(isr);
//        String txtString = "";
//        List<String> zzxxStr = new ArrayList<>();
//
//        while ((txtString = br.readLine()) != null){
//            String[] s = txtString.split("\t");
//            for(String a : s){
//                System.out.print(a);
//            }
//            System.out.println();
//        }
        List<tets> list = new ArrayList<>();
        Set<tets> set = new HashSet<>();
        tets a = new tets();
        a.setA("1");
        a.setB("2");
        set.add(a);
        tets b = new tets();
        b.setA("2");
        b.setB("2");
        set.add(b);
        System.out.println(set.size());
    }
    static class tets{
        private String a="";
        private String b="";

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

        public String getB() {
            return b;
        }

        public void setB(String b) {
            this.b = b;
        }

        public tets() {
        }

        public tets(String a, String b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            tets tets = (tets) o;

            return a != null ? a.equals(tets.a) : tets.a == null;
        }

        @Override
        public int hashCode() {
            return a != null ? a.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "tets{" +
                    "a='" + a + '\'' +
                    ", b='" + b + '\'' +
                    '}';
        }
    }
}

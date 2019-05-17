package cn.com.sinofaith.util;


import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.util.PoiPublicUtil;
import cn.afterturn.easypoi.word.WordExportUtil;
import cn.afterturn.easypoi.word.entity.WordImageEntity;
import cn.com.sinofaith.bean.bankBean.BankTjjgEntity;
import cn.com.sinofaith.bean.bankBean.BankZcxxEntity;
import cn.com.sinofaith.bean.cftBean.CftZzxxEntity;
import cn.com.sinofaith.service.UploadService;
import com.csvreader.CsvReader;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

public class test {
    static long time;
    static class Person{
        private long id;
        private long pid;
        private long bhcj=0;

        public Person() {
        }

        public Person(long id, long pid) {
            this.id = id;
            this.pid = pid;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getPid() {
            return pid;
        }

        public void setPid(long pid) {
            this.pid = pid;
        }

        public long getBhcj() {
            return bhcj;
        }

        public void setBhcj(long bhcj) {
            this.bhcj = bhcj;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "id=" + id +
                    ", pid=" + pid +
                    ", bhcj=" + bhcj +
                    '}';
        }
    }
    public static void ttttt (List<Person> list,Person p){
        List<Person> next = list.stream().filter(x->p.getId()==x.getPid()).collect(Collectors.toList());
        if(next.size()>0){
            for(Person p1 : next){
                ttttt(list,p1);
                System.out.println(next.size());
            }
            p.setBhcj(p.getBhcj()+1);
        }else{
            System.out.println();
        }
    }
    public static void main(String[] args) throws Exception{

//            String[] time = TimeFormatUtil.getDate("/").split("/");
//            Map<String, Object> map = new HashMap<String, Object>();
//            WordImageEntity image = new WordImageEntity();
//            image.setHeight((int) (551 / 2.6));
//            image.setWidth((int) (1378 / 2.6));
//            image.setUrl("C:\\Users\\47435\\Desktop\\wordTest\\model.png");
//            image.setType(WordImageEntity.URL);
//
//            map.put("wordImg", image);
//            map.put("title", "洪湖公安局");
//            map.put("title1", "湖北农商银行");
//            map.put("y", time[0]);
//            map.put("m", time[1]);
//            map.put("d", time[2].substring(0, time[2].indexOf(" ")));
//            map.put("sfzhmList", "42220119960807、42220119960807、42220119960807、42220119960807、42220119960807");
//            try {
//                XWPFDocument doc = WordExportUtil.exportWord07(
//                        "C:\\Users\\47435\\Desktop\\wordTest\\model.docx", map);
//                FileOutputStream fos = new FileOutputStream("C:\\Users\\47435\\Desktop\\wordTest\\image.docx");
//                doc.write(fos);
//                fos.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }


        //        List<CftZzxxEntity> map = new ArrayList<>();
//        int a = 0;
//        try {
//            for (int i = 0; i < 10000000; i++) {
//                CftZzxxEntity c = new CftZzxxEntity();
//                c.setZh("a993476816");
//                c.setJydh("1000039501170801003207010601800747669065"+i);
//                c.setJdlx("入");
//                c.setJylx("转帐");
//                c.setJyje(new BigDecimal(20));
//                c.setZhye(new BigDecimal(30));
//                c.setJysj("2017-08-01 11:49:31");
//                c.setYhlx("余额支付");
//                c.setJysm("微信红包(1000039501)");
//                c.setShmc("");
//                c.setFsf("chen604002370");
//                c.setFsje(new BigDecimal(20));
//                c.setJsf("a993476816");
//                c.setJssj("2017-08-01 11:49:31");
//                c.setJsje(new BigDecimal(20));
//                a=i;
//                map.add(c);
//            }
//        }catch (OutOfMemoryError e){
//            System.out.println(a);
//        }
        String s = "保险";
        boolean temp = s.contains("财付通")||s.contains("支付")||s.contains("清算")
                || s.contains("特约") || s.contains("备付金")|| s.contains("银行")
                || s.contains("银联") || s.contains("保险") || s.contains("过渡")
                || s.contains("美团");
        System.out.println(temp);
    }

    static class Student{
        private String name;
        private String addr;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddr() {
            return addr;
        }

        public void setAddr(String addr) {
            this.addr = addr;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Student student = (Student) o;

            if (name != null ? !name.equals(student.name) : student.name != null) return false;
            return addr != null ? addr.equals(student.addr) : student.addr == null;
        }

        @Override
        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + (addr != null ? addr.hashCode() : 0);
            return result;
        }
    }
}

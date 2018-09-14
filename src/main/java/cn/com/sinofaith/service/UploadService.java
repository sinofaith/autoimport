package cn.com.sinofaith.service;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.bankBean.BankPersonEntity;
import cn.com.sinofaith.bean.bankBean.BankZcxxEntity;
import cn.com.sinofaith.bean.bankBean.BankZzxxEntity;
import cn.com.sinofaith.bean.cftBean.CftPersonEntity;
import cn.com.sinofaith.bean.cftBean.CftZcxxEntity;
import cn.com.sinofaith.bean.cftBean.CftZzxxEntity;
import cn.com.sinofaith.bean.wlBean.WuliuEntity;
import cn.com.sinofaith.dao.bankDao.BankPersonDao;
import cn.com.sinofaith.dao.bankDao.BankZcxxDao;
import cn.com.sinofaith.dao.bankDao.BankZzxxDao;
import cn.com.sinofaith.dao.cftDao.CftPersonDao;
import cn.com.sinofaith.dao.cftDao.CftZcxxDao;
import cn.com.sinofaith.dao.cftDao.CftZzxxDao;
import cn.com.sinofaith.dao.wuliuDao.WuliuJjxxDao;
import cn.com.sinofaith.util.Excel2007Reader;
import cn.com.sinofaith.util.ExcelReader;
import cn.com.sinofaith.util.ReadExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.border.TitledBorder;
import java.io.*;
import java.util.*;

/**
 * Created by Me. on 2018/5/23
 */
@Service
public class UploadService {

    @Autowired
    private CftZcxxDao zcd;

    @Autowired
    private CftZzxxDao zzd;

    @Autowired
    private CftPersonDao cpd;

    @Autowired
    private BankZcxxDao bzcd;

    @Autowired
    private BankZzxxDao bzzd;

    @Autowired
    private WuliuJjxxDao wljjd;

    @Autowired
    private BankPersonDao bpd;

    public int deleteAll(String uploadPath) {
        try {
            File files = new File(uploadPath);
            String[] filep = files.list();
            File temps = null;
            for (int i = 0; i < filep.length; i++) {
                temps = new File(uploadPath + filep[i]);
                if (temps.isFile()) {
                    temps.delete();
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return 0;
    }

    public int insertZcxx(String filepath, String filter, long aj) {
        List<String> listPath = getFileList(filepath, filter);
        List<CftZcxxEntity> listZcxx = getZcxxByTxt(listPath);
        int i = zcd.saveZcxx(listZcxx, aj);
        return i;
    }

    public int insertZzxx(String filepath, String filter, long aj) {
        List<String> listPath = getFileList(filepath, filter);
        List<CftZzxxEntity> listZzxx = getZzxxByTxt(listPath);
        int i = zzd.insertZzxx(listZzxx, aj);
        return i;
    }

    public List<CftZzxxEntity> getZzxxByTxt(List<String> listPath) {
        List<CftZzxxEntity> zzxxs = new ArrayList<>();
        File file = null;
        BufferedReader br = null;
        FileInputStream fis = null;
        InputStreamReader isr = null;
        if (listPath.size() > 0) {
            for (int i = 0; i < listPath.size(); i++) {
                try {
                    file = new File(listPath.get(i));
                    fis = new FileInputStream(file);
                    isr = new InputStreamReader(fis, "UTF-8");
                    br = new BufferedReader(isr);
                    String txtStr = "";
                    List<String> zzxxStr = new ArrayList<>();
                    while ((txtStr = br.readLine()) != null) {
                        if (txtStr.contains("用户ID")) {
                            continue;
                        }
                        String[] s = txtStr.split("\t");
                        zzxxStr = Arrays.asList(s);
                        if (zzxxStr.size() > 8) {
                            zzxxs.add(CftZzxxEntity.listToObj(zzxxStr));
                        }
                    }
                    br.close();
                    file.delete();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (br != null) {
                        try {
                            br.close();
                            file.delete();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return zzxxs;
    }

    public List<CftZcxxEntity> getZcxxByTxt(List<String> listPath) {
        List<CftZcxxEntity> zcxxs = new ArrayList<CftZcxxEntity>();
        File file = null;
        //输入缓冲区
        BufferedReader br = null;
        FileInputStream fis = null;
        InputStreamReader isr = null;
        List<String> zcxxStr = null;
        List<CftZcxxEntity> listZcxx = null;
        CftPersonEntity cp = null;
        int line = 0;
        if (listPath.size() > 0) {
            for (int i = 0; i < listPath.size(); i++) {
                try {
                    file = new File(listPath.get(i));
                    fis = new FileInputStream(file);
                    isr = new InputStreamReader(fis, "UTF-8");
                    br = new BufferedReader(isr);
                    Map<String, Integer> map = new HashMap<>();
                    String txtString = "";
                    listZcxx = new ArrayList<CftZcxxEntity>();
                    line = 0;
                    while ((txtString = br.readLine()) != null) {
                        String[] s = txtString.split("\t");
                        zcxxStr = Arrays.asList(s);
                        if (txtString.contains("账户状态")) {
                            for (int j = 0; j < zcxxStr.size(); j++) {
                                if (zcxxStr.get(j).contains("账户状态")) {
                                    map.put("zhzt", j);
                                } else if (zcxxStr.get(j).contains("账号") && !zcxxStr.get(j).contains("银行")) {
                                    map.put("zh", j);
                                } else if (zcxxStr.get(j).contains("注册姓名")) {
                                    map.put("xm", j);
                                } else if (zcxxStr.get(j).contains("注册时间")) {
                                    map.put("zcsj", j);
                                } else if (zcxxStr.get(j).contains("注册身份证号")) {
                                    map.put("sfzhm", j);
                                } else if (zcxxStr.get(j).contains("绑定手机")) {
                                    map.put("bdsj", j);
                                } else if (zcxxStr.get(j).contains("开户行信息")) {
                                    map.put("khh", j);
                                } else if (zcxxStr.get(j).contains("银行账号")) {
                                    map.put("yhzh", j);
                                }
                            }
                            line++;
                            continue;
                        }
                        //---------------------------------
                        if (line > 1) {
                            if (zcxxStr.get(0).isEmpty()) {
                                zcxxStr.set(0, listZcxx.get(0).getZhzt());
                            }
                            if (zcxxStr.get(1).isEmpty()) {
                                zcxxStr.set(1, listZcxx.get(0).getZh());
                            }
                            if (zcxxStr.get(2).isEmpty()) {
                                zcxxStr.set(2, listZcxx.get(0).getXm());
                            }
                            if (zcxxStr.get(3).isEmpty()) {
                                zcxxStr.set(3, listZcxx.get(0).getZcsj());
                            }
                            if (zcxxStr.get(4).isEmpty()) {
                                zcxxStr.set(4, listZcxx.get(0).getSfzhm());
                            }
                            if (zcxxStr.get(5).isEmpty()) {
                                zcxxStr.set(5, listZcxx.get(0).getBdsj());
                            }
                        }
                        if (zcxxStr.size() > 7) {
                            listZcxx.add(CftZcxxEntity.listToObj(zcxxStr, map));
                        }
                        cp = CftPersonEntity.listToObj(zcxxStr);
                        if (cp.getXm().length() > 0 && cp.getZh().length() > 0) {
                            cpd.insert(cp);
                        }
                        line++;
                    }
                    zcxxs.addAll(listZcxx);
                    br.close();
                    file.delete();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (br != null) {
                        try {
                            br.close();
                            file.delete();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return zcxxs;
    }

    public int insertBankZzxx(String filePath, long aj_id, List<BankZzxxEntity> all) {
        List<String> listPath = getFileLists(filePath, "交易明细");
        List<BankZzxxEntity> listZzxx = getByExcel(listPath);
        int i = bzzd.insertZzxx(listZzxx, aj_id, all);

        List<BankPersonEntity> allbp = bpd.find("from BankPersonEntity");
        Map<String,String> allBp = new HashMap<>();
        for(int j=0;j<allbp.size();j++){
            allBp.put((allbp.get(j).getYhkkh()).replace("null",""),null);
        }
        Map<String,BankPersonEntity> mapZ= new HashMap<>();
            for (int g = 0; g < listZzxx.size(); g++) {
                BankPersonEntity bp = new BankPersonEntity();
                bp.setYhkkh(listZzxx.get(g).getDskh());
                bp.setYhkzh(String.valueOf(aj_id));
                bp.setXm(listZzxx.get(g).getDsxm());
                if (bp.getYhkkh()!=null && bp.getXm()!=null&&bp.getYhkkh().length() > 0 && bp.getXm().length() > 0) {
                    if (bp.getXm().contains("支付宝")) {
                        bp.setXm("支付宝（中国）网络技术有限公司");
                    } else if (bp.getXm().contains("微信") || bp.getXm().contains("财付通")) {
                        bp.setXm("财付通支付科技有限公司");
                    }
                    mapZ.put((bp.getYhkkh()).replace("null", ""), bp);
                } else {
                    continue;
                }
            }
        List<String> str = new ArrayList<>();
        for (String o : mapZ.keySet()) {
            if (allBp.containsKey(o)) {
                str.add(o);
            }
        }
        for (int s = 0; s < str.size();s++) {
                mapZ.remove(str.get(s));
            }
        allbp=new ArrayList<>(mapZ.values());


        bpd.add(allbp,String.valueOf(aj_id));



        return i;
    }

    /**
     * 物流寄件添加数据
     * @param uploadPath
     */
    public int insertWuliuJjxx(String uploadPath, long aj_id, List<WuliuEntity> all) {
        List<String> listPath = getWlFileList(uploadPath);
        List<WuliuEntity> listJjxx = null;
        int i = 0;
        for (String list : listPath) {
            if(list.endsWith(".xlsx")){
                listJjxx = getByJjxxExcel(list);
            }else{
                listJjxx = getBy2003Excel(list);
            }
            i = wljjd.insertJjxx(listJjxx, aj_id, all);
        }
        return i;
    }

    private  List<WuliuEntity> getBy2003Excel(String listPath){
        List<WuliuEntity> wls = new ArrayList<>();
        try {
            ReadExcelUtils excelReader = new ReadExcelUtils(listPath);
            String[] titles = excelReader.readExcelTitle();
            titles = excelReader.readExcelTitle();
            List<String> strTitle = new ArrayList<>(Arrays.asList(titles));
            Map<String,Integer> title = new HashMap<>();
            for(int i=0;i<strTitle.size();i++){
                String temp=strTitle.get(i);
                String lb = "";
                if(temp!=null&&temp.length()>0) {
                    // 进行筛选
                    if (temp.contains("运单号") || temp.contains("单号")) {
                        title.put("waybill_id",i);
                    } else if (temp.contains("寄件时间") || temp.contains("寄时间")) {
                        title.put("ship_time",i);
                    } else if (temp.contains("寄件地址") || temp.contains("寄地址")) {
                        title.put("ship_address",i);
                    } else if (temp.contains("寄件人") || temp.contains("寄件联系人")) {
                        title.put("sender",i);
                    } else if (temp.contains("寄件电话") || temp.contains("寄电话")) {
                        title.put("ship_phone",i);
                    } else if (temp.contains("寄件手机") || temp.contains("寄客户编码") || temp.contains("寄方客户编码")) {
                        title.put("ship_mobilephone",i);
                    } else if (temp.contains("收件地址") || temp.contains("收地址")) {
                        title.put("sj_address",i);
                    } else if (temp.contains("收件人") || temp.contains("收件联系人")) {
                        title.put("addressee",i);
                    } else if (temp.contains("收件电话") || temp.contains("收电话")) {
                        title.put("sj_phone",i);
                    } else if (temp.contains("收件手机") || temp.contains("到客户编码") || temp.contains("派方客户编码")) {
                        title.put("sj_mobilephone",i);
                    } else if (temp.contains("收件员")) {
                        title.put("collector",i);
                    } else if (temp.contains("托寄物") || temp.contains("托寄内容") || temp.contains("托物")) {
                        title.put("tjw",i);
                    } else if (temp.contains("付款方式") || temp.contains("付款")) {
                        title.put("payment",i);
                    } else if (temp.contains("代收货款金额") || temp.contains("代收货款")) {
                        title.put("dshk",i);
                    } else if (temp.contains("计费重量") || temp.contains("重量")) {
                        title.put("weight",i);
                    } else if (temp.contains("件数")) {
                        title.put("number_cases",i);
                    } else if (temp.contains("运费") || temp.contains("费用")) {
                        title.put("freight",i);
                    }
                    title.put(lb, i);
                }
            }
            Map<Integer, Map<Integer,Object>> map = excelReader.readExcelContent();
            Iterator<Integer> iterator = map.keySet().iterator();
            while (iterator.hasNext()) {
                List<String> list = new ArrayList<>();
                Integer key1 = iterator.next();
                Map<Integer, Object> map2 = map.get(key1);
                Iterator<Integer> iterator1 = map2.keySet().iterator();
                while(iterator1.hasNext()){
                    Integer key2 = iterator1.next();
                    String value = (String) map2.get(key2);
                    list.add(value);
                }
                WuliuEntity wuliuEntity = WuliuEntity.listToObj(list, title);
                wls.add(wuliuEntity);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return wls;
    }
    /**
     * 解析2007excel
     * @param listPath
     * @return
     */
    private List<WuliuEntity>  getByJjxxExcel(String listPath) {
        // 用于存放表格中列号
        final Map<String,Integer> title = new HashMap<>();
        final List<WuliuEntity> wls = new ArrayList<>();

        Excel2007Reader reader = new Excel2007Reader() {
            @Override
            public void getRows(int sheetIndex, int curRow, List<String> rowList) {
                // 第一行
                if (curRow == 0) {
                    for(int i =0;i<rowList.size();i++){
                        // 获取每一列的标题
                        String temp = rowList.get(i);
                        // 进行筛选
                        if (temp.contains("运单号") || temp.contains("单号")) {
                            title.put("waybill_id",i);
                        } else if (temp.contains("寄件时间") || temp.contains("寄时间")) {
                            title.put("ship_time",i);
                        } else if (temp.contains("寄件地址") || temp.contains("寄地址")) {
                            title.put("ship_address",i);
                        } else if (temp.contains("寄件人") || temp.contains("寄件联系人")) {
                            title.put("sender",i);
                        } else if (temp.contains("寄件电话") || temp.contains("寄电话")) {
                            title.put("ship_phone",i);
                        } else if (temp.contains("寄件手机") || temp.contains("寄客户编码") || temp.contains("寄方客户编码")) {
                            title.put("ship_mobilephone",i);
                        } else if (temp.contains("收件地址") || temp.contains("收地址")) {
                            title.put("sj_address",i);
                        } else if (temp.contains("收件人") || temp.contains("收件联系人")) {
                            title.put("addressee",i);
                        } else if (temp.contains("收件电话") || temp.contains("收电话")) {
                            title.put("sj_phone",i);
                        } else if (temp.contains("收件手机") || temp.contains("到客户编码") || temp.contains("派方客户编码")) {
                            title.put("sj_mobilephone",i);
                        } else if (temp.contains("收件员")) {
                            title.put("collector",i);
                        } else if (temp.contains("托寄物") || temp.contains("托寄内容") || temp.contains("托物")) {
                            title.put("tjw",i);
                        } else if (temp.contains("付款方式") || temp.contains("付款")) {
                            title.put("payment",i);
                        } else if (temp.contains("代收货款金额") || temp.contains("代收货款")) {
                            title.put("dshk",i);
                        } else if (temp.contains("计费重量") || temp.contains("重量")) {
                            title.put("weight",i);
                        } else if (temp.contains("件数")) {
                            title.put("number_cases",i);
                        } else if (temp.contains("运费") || temp.contains("费用")) {
                            title.put("freight",i);
                        }
                    }
                } else {
                    WuliuEntity wl = WuliuEntity.listToObj(rowList, title);
                    if(wl!=null){
                        wls.add(wl);
                    }
                }
            }
        };

        try {
            reader.process(listPath);
            new File(listPath).delete();
        } catch (Exception e) {
            e.getStackTrace();
        }

        return wls;
    }

    public List<BankZzxxEntity> getByExcel(List<String> filepath) {
        final Map<String, Integer> title = new HashMap();
        final List<BankZzxxEntity> listB = new ArrayList<>();

        ExcelReader reader = new ExcelReader() {
            public void getRows(int sheetIndex, int curRow, List<String> rowList) {
                if (curRow == 0) {
                    for (int i = 0; i < rowList.size(); i++) {
                        String temp = rowList.get(i);
                        if (temp.contains("交易账卡号")) {
                            title.put("yhkkh", i);
                        } else if (temp.contains("交易账号")) {
                            title.put("yhkzh", i);
                        } else if (rowList.get(i).contains("交易户名")) {
                            title.put("jyxm", i);
                        } else if (temp.contains("交易证件号")) {
                            title.put("jyzjh", i);
                        } else if (temp.contains("交易日期")) {
                            title.put("jysj", i);
                        } else if (temp.contains("交易时间")) {
                            title.put("jysfm", i);
                        } else if (temp.contains("交易金额")) {
                            title.put("jyje", i);
                        } else if (temp.contains("交易余额") && !temp.contains("对手")) {
                            title.put("jyye", i);
                        } else if (temp.contains("收付标志")) {
                            title.put("sfbz", i);
                        } else if (temp.contains("对手账号")) {
                            title.put("dskh", i);
                        } else if (temp.contains("对手卡号")) {
                            title.put("dszh", i);
                        } else if (temp.contains("对手户名")) {
                            title.put("dsxm", i);
                        } else if (temp.contains("对手身份证号")) {
                            title.put("dssfzh", i);
                        } else if (temp.contains("对手开户银行")) {
                            title.put("dskhh", i);
                        } else if (temp.contains("摘要说明")) {
                            title.put("zysm", i);
                        } else if (temp.contains("交易网点名称")) {
                            title.put("jywdmc", i);
                        } else if (temp.contains("交易发生地")) {
                            title.put("jyfsd", i);
                        } else if (temp.contains("交易是否成功")) {
                            title.put("jysfcg", i);
                        } else if (temp.contains("对手交易余额")) {
                            title.put("dsjyye", i);
                        } else if (temp.contains("对手余额")) {
                            title.put("dsye", i);
                        } else if (temp.contains("备注")) {
                            title.put("bz", i);
                        }
                    }
                } else {
                    listB.add(BankZzxxEntity.listToObj(rowList, title));
                }
            }
        };

        for (int i = 0; i < filepath.size(); i++) {
            try {
                reader.process(filepath.get(i));
                new File(filepath.get(i)).delete();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
//        Set<BankZzxxEntity> setB = new HashSet<>(listB);

        return listB;
    }

    public int insertBankZcxx(String filePath, long aj_id) {
        List<String> listPath = getFileLists(filePath, "开户");
        List<BankZcxxEntity> listZcxx = getBzcxxByExcel(listPath);
        int i = bzcd.saveZcxx(listZcxx, aj_id);
        BankPersonEntity bpe = new BankPersonEntity();
        for (BankZcxxEntity bce : listZcxx) {
            bpe.setXm(bce.getKhxm());
            bpe.setYhkkh(bce.getYhkkh());
            bpe.setYhkzh(bce.getYhkzh());
            if (bpe.getYhkkh().trim().length() > 0) {
                bpd.insert(bpe);
            }
        }
        return i;
    }

    public List<BankZcxxEntity> getBzcxxByExcel(List<String> listPath) {
        List<BankZcxxEntity> zcxxs = new ArrayList<>();
        for (String path : listPath) {
            try {
                ReadExcelUtils excelReader = new ReadExcelUtils(path);
                String[] titles = excelReader.readExcelTitle();
                List<String> strTitle = new ArrayList<>(Arrays.asList(titles));
                Map<String, Integer> title = new HashMap<>();
                for (int i = 0; i < strTitle.size(); i++) {
                    String str = strTitle.get(i);
                    String lb = "";
                    if (str != null && str.length() > 0) {
                        if (str.contains("账户开户名称")) {
                            lb = "khxm";
                        } else if (str.contains("开户人证件号码")) {
                            lb = "khzjh";
                        } else if (str.contains("交易卡号")) {
                            lb = "yhkkh";
                        } else if (str.contains("交易账号")) {
                            lb = "yhkzh";
                        } else if (str.contains("账号开户时间")) {
                            lb = "khsj";
                        } else if (str.contains("开户网点")) {
                            lb = "khh";
                        } else if (str.contains("账户状态")) {
                            lb = "zhzt";
                        } else if (str.contains("账户余额")) {
                            lb = "zhye";
                        } else if (str.contains("可用余额")) {
                            lb = "kyye";
                        }
                        if (!"".endsWith(lb)) {
                            title.put(lb, i);
                        }
                    }
                }

                Map<Integer, Map<Integer, Object>> map = excelReader.readExcelContent();
                for (int i = 1; i <= map.size(); i++) {
                    if (!zcxxs.contains(BankZcxxEntity.mapToObj(map.get(i), title))) {
                        zcxxs.add(BankZcxxEntity.mapToObj(map.get(i), title));
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("未找到指定路径的文件!");
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return zcxxs;
    }

    public static List<String> getFileLists(String filePath, String filter) {
        List<String> listPath = new ArrayList<String>();
        File dir = new File(filePath);
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.getName().contains(filter)) {
                listPath.add(file.getAbsolutePath());
            }
        }
        return listPath;
    }


    public static List<String> getFileList(String filepath, String filter) {
        List<String> listPath = new ArrayList<String>();
        File dir = new File(filepath);//获取到D:/tomcat/webapps/SINOFAITH/upload/temp/1535376809258目录下
        File[] files = dir.listFiles();//获取文件夹下所有文件
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                if (files[i].isDirectory()) {//如果是一个目录则返回true
                    getFileList(files[i].getAbsolutePath(), filter);
                } else if (fileName.endsWith(filter)) {
                    String strFileName = files[i].getAbsolutePath();
                    listPath.add(strFileName);
                }
            }
        }
        return listPath;
    }

    /**
     * 获取文件
     * @param uploadPath
     * @return
     */
    private static List<String> getWlFileList(String uploadPath) {
        List<String> listPath = new ArrayList<String>();
        File dir = new File(uploadPath);
        File[] files = dir.listFiles();
        for (File file : files) {
            listPath.add(file.getAbsolutePath());
        }
        return listPath;
    }

    /**
     *
     */

}
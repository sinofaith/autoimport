package cn.com.sinofaith.service;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.bankBean.BankCustomerEntity;
import cn.com.sinofaith.bean.bankBean.BankPersonEntity;
import cn.com.sinofaith.bean.bankBean.BankZcxxEntity;
import cn.com.sinofaith.bean.bankBean.BankZzxxEntity;
import cn.com.sinofaith.bean.cftBean.CftPersonEntity;
import cn.com.sinofaith.bean.cftBean.CftZcxxEntity;
import cn.com.sinofaith.bean.cftBean.CftZzxxEntity;
import cn.com.sinofaith.bean.zfbBean.*;
import cn.com.sinofaith.dao.bankDao.BankCustomerDao;
import cn.com.sinofaith.dao.zfbDao.*;
import cn.com.sinofaith.dao.bankDao.BankPersonDao;
import cn.com.sinofaith.dao.bankDao.BankZcxxDao;
import cn.com.sinofaith.dao.bankDao.BankZzxxDao;
import cn.com.sinofaith.dao.cftDao.CftPersonDao;
import cn.com.sinofaith.dao.cftDao.CftZcxxDao;
import cn.com.sinofaith.dao.cftDao.CftZzxxDao;
import cn.com.sinofaith.dao.wuliuDao.WuliuJjxxDao;
import cn.com.sinofaith.form.zfbForm.ZfbJyjlTjjgsForm;
import cn.com.sinofaith.form.zfbForm.ZfbZzmxTjjgForm;
import cn.com.sinofaith.form.zfbForm.ZfbZzmxTjjgsForm;
import cn.com.sinofaith.util.DBUtil;
import cn.com.sinofaith.util.ExcelReader;
import cn.com.sinofaith.util.ReadExcelUtils;
import cn.com.sinofaith.util.TimeFormatUtil;
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

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
    private BankCustomerDao bcd;

    @Autowired
    private WuliuJjxxDao wljjd;

    @Autowired
    private BankPersonDao bpd;
    @Autowired
    private ZfbZcxxDao zfbZcxxDao;
    @Autowired
    private ZfbZhmxDao zfbZhmxDao;
    @Autowired
    private ZfbZhmxQxsjDao zfbZhmxQxsjDao;
    @Autowired
    private ZfbDlrzDao zfbDlrzDao;
    @Autowired
    private ZfbJyjlDao zfbJyjlDao;
    @Autowired
    private ZfbZzmxDao zfbZzmxDao;
    @Autowired
    private ZfbZzmxTjjgDao zfbZzmxTjjgDao;
    @Autowired
    private ZfbZzmxTjjgsDao zfbZzmxTjjgsDao;
    @Autowired
    private ZfbJyjlTjjgsDao zfbJyjlTjjgsDao;
    @Autowired
    private ZfbJyjlSjdzsDao zfbJyjlSjdzsDao;
    @Autowired
    private ZfbZhmxTjjgDao zfbZhmxTjjgDao;
    @Autowired
    private ZfbZhmxTjjgsDao zfbZhmxTjjgsDao;
    @Autowired
    private ZfbZhmxJczzDao zfbZhmxJczzDao;
    @Autowired
    private ZfbZhmxJylxDao zfbZhmxJylxDao;


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
        return  getZzxxByTxt(listPath,aj);
//        int i = zzd.insertZzxx(listZzxx, aj);
    }

    public int getZzxxByTxt(List<String> listPath,long aj) {
        Set<String> zhList = zzd.getGroupByZh(aj);
        List<CftZzxxEntity> zzxxs = new ArrayList<>();
        int count = 0;
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
                    if((i+1)%30==0){
                        if(zhList.size()>0) {
                            zzxxDb(zzxxs,zhList,aj);
                        }
                        if(zzxxs.size()>0) {
                            zzxxs = new ArrayList<>(new HashSet<>(zzxxs));
                            count += zzd.insertZzxx(zzxxs, aj);
                        }
                        zzxxs.clear();
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
                if(zhList.size()>0) {
                    zzxxDb(zzxxs,zhList,aj);
                }
                if(zzxxs.size()>0) {
                    zzxxs = new ArrayList<>(new HashSet<>(zzxxs));
                    count += zzd.insertZzxx(zzxxs, aj);
                }
                zzxxs.clear();
            }
        return count;
    }

    public List<CftZzxxEntity> zzxxDb (List<CftZzxxEntity> zzxxs,Set<String> zhList,long aj){
        List<CftZzxxEntity> all  = new ArrayList<>();
        String sql = " and zh in (";
        Map<String,List<CftZzxxEntity>> map = zzxxs.stream().collect(Collectors.groupingBy(CftZzxxEntity::getZh));
        List<CftZzxxEntity> flag = new ArrayList<>();
        zzxxs.clear();
        for(String key:map.keySet()){
            if(!zhList.contains(key)){
                zzxxs.addAll(map.get(key));
            }else{
                sql += ",'"+key+"'";
                flag.addAll(map.get(key));
            }
        }
        if(sql.contains(",")) {
            all = zzd.getAlla(aj, sql.replaceFirst(",", "") + ")");
        }
        if(all.size()>0) {
            Set<CftZzxxEntity> set = new HashSet<>(all);
            all.clear();
            for (CftZzxxEntity x : flag) {
                if (!set.contains(x)) {
                    zzxxs.add(x);
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
                        if(txtString.equals("")||txtString.length()==0){
                            continue;
                        }
                        if(txtString.startsWith("注销信息")){
                            break;
                        }
                        if (txtString.contains("账户状态")||txtString.contains("账号")) {
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
                        if (line > 1&&listZcxx.size()>0) {
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

    public int insertBankCustomer(String filepath,AjEntity aj){
        List<String> listPath = getFileLists(filepath,"人员信息");
        listPath.addAll(getFileLists(filepath,"客户基本信息"));
        String inserttime = TimeFormatUtil.getDate("/");
        List<BankCustomerEntity> listcust = getBankCustByFile(listPath);
        List<String> zjhm = new ArrayList<>();
        for(BankCustomerEntity c:listcust){
            c.setInserttime(inserttime);
            bcd.saveOrUpdate(c);
            zjhm.add(c.getZjhm());
        }
        bcd.saveRel(zjhm,aj.getId());
        return listcust.size();
    }


    public int insertBankZzxx(String filePath, long aj_id, List<BankZzxxEntity> all) {
        List<String> listPath = getFileLists(filePath, "交易明细");
        listPath.addAll(getFileLists(filePath,"详细信息"));
        List<BankZzxxEntity> listZzxx = getByExcel(listPath);
        int i = bzzd.insertZzxx(listZzxx, aj_id, all);

        List<BankPersonEntity> allbp = bpd.find("from BankPersonEntity");
        Map<String, String> allBp = new HashMap<>();
        for (int j = 0; j < allbp.size(); j++) {
            allBp.put((allbp.get(j).getYhkkh()).replace("null", ""), null);
        }
        Map<String,BankPersonEntity> mapZ= new HashMap<>();
        for (int g = 0; g < listZzxx.size(); g++) {
            BankPersonEntity bp = new BankPersonEntity();
            bp.setYhkkh(listZzxx.get(g).getDskh());
            bp.setYhkzh(String.valueOf(aj_id));
            bp.setXm(listZzxx.get(g).getDsxm());
//                bp.setKhh(bankName(GetBank.getBankname(bp.getYhkkh()).split("·")[0], listZzxx.get(g).getDskhh()));

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
        for (int s = 0; s < str.size(); s++) {
            mapZ.remove(str.get(s));
        }
        allbp = new ArrayList<>(mapZ.values());
        bpd.add(allbp, String.valueOf(aj_id));
        return i;
    }


    /**
     * 物流寄件添加数据
     * @param uploadPath
     */
    /*public int insertWuliuJjxx(String uploadPath, long aj_id, List<WuliuEntity> all) {
        List<String> listPath = getWlFileList(uploadPath);
        List<WuliuEntity> listJjxx = null;
        int i = 0;
        for (String list : listPath) {
            if(list.endsWith(".xlsx")){
                listJjxx = getByJjxxExcel(list);
            }else{
                listJjxx = getBy2003Excel(list);
            }
            i = wljjd.insertJjxx(listJjxx, aj_id);
        }
        return i;
    }
    private  List<WuliuEntity> getBy2003Excel(String listPath){
        List<WuliuEntity> wls = new ArrayList<>();
        try {
            ReadExcelUtils excelReader = new ReadExcelUtils(listPath);
            String[] titles = excelReader.readExcelTitle();
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
        new File(listPath).delete();
        return wls;
    }
    */

    /**
     * 解析2007excel
     *
     * @return
     *//*
    private List<WuliuEntity> getByJjxxExcel(String listPath) {
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
    }*/

    public void getBCustomer(String temp,int i,Map<String,Integer> title){
        if(temp.contains("客户名称")){
            title.put("name",i);
        }else if(temp.equals("证照类型")||temp.equals("证件类型")){
            title.put("zjlx",i);
        }else if(temp.equals("证照号码")||temp.equals("证件号码")){
            title.put("zjhm",i);
        }else if(temp.contains("现住址_行政区划")){
            title.put("xzz",i);
        }else if(temp.contains("单位地址")){
            title.put("dwdz",i);
        }else if(temp.contains("联系电话")){
            title.put("lxdh",i);
        }else if(temp.contains("联系手机")){
            title.put("lxsj",i);
        }else if(temp.contains("单位电话")) {
            title.put("dwdh", i);
        }else if(temp.contains("住宅电话")) {
            title.put("zzdh", i);
        }else if(temp.contains("工作单位")) {
            title.put("gzdw", i);
        }else if(temp.contains("邮箱")||temp.contains("Email")) {
            title.put("email", i);
        }
    }

    public  void getBzzxxTitle(String temp,int i,Map<String,Integer> title){
            if (temp.contains("交易账卡号")||temp.contains("交易卡号")) {
                title.put("yhkkh", i);
            } else if (temp.contains("交易账号")) {
                title.put("yhkzh", i);
            } else if (temp.contains("交易户名")) {
                title.put("jyxm", i);
            } else if (temp.contains("交易证件号")) {
                title.put("jyzjh", i);
            } else if (temp.contains("交易日期")||temp.contains("交易时间")) {
                title.put("jysj", i);
            }
//                        else if (temp.contains("交易时间")) {
//                            title.put("jysfm", i);
//                        }
            else if (temp.contains("交易金额")) {
                title.put("jyje", i);
            } else if (temp.contains("交易余额") && !temp.contains("对手")) {
                title.put("jyye", i);
            } else if (temp.contains("收付标志")) {
                title.put("sfbz", i);
            } else if (temp.contains("对手账号")||temp.contains("对手账卡号")) {
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

    public List<BankCustomerEntity> getBankCustByFile(List<String> listPath){
        final Map<String, Integer> title = new HashMap();
        final List<BankCustomerEntity> result = new ArrayList<>();
        CsvReader csv = null;
        for(int i=0;i<listPath.size();i++){
            try{
                if(listPath.get(i).endsWith(".xlsx")){
                    ExcelReader reader = new ExcelReader() {
                        public void getRows(int sheetIndex, int curRow, List<String> rowList) {
                            if (curRow == 0) {
                                for (int i = 0; i < rowList.size(); i++) {
                                    String temp = rowList.get(i);
                                    getBCustomer(temp,i,title);
                                }
                            } else {
                                result.add(BankCustomerEntity.listToObj(rowList, title));
                            }
                        }
                    };
                    reader.process(listPath.get(i));
                }
                if(listPath.get(i).endsWith(".csv")){
                    csv = new CsvReader(listPath.get(i),',',Charset.forName("GBK"));
                    csv.readHeaders();
                    csv.setSafetySwitch(false);
                    String[] titles = csv.getHeaders();
                    for(int j=0;j<titles.length;j++){
                        getBCustomer(titles[j].replace("\t",""),j,title);
                    }
                    csv.setSafetySwitch(false);
                    while (csv.readRecord()){
                        result.add(BankCustomerEntity.listToObj(Arrays.asList(csv.getValues()), title));
                    }
                    csv.close();
                }
                Map<String,List<BankCustomerEntity>> map = result.stream().collect(groupingBy(BankCustomerEntity ::getZjhm));
                result.clear();
                map.forEach((key, value)->{
                    BankCustomerEntity temp = new BankCustomerEntity();
                    temp.setName(StringUtils.strip(value.stream().filter(p->!"".equals(p.getName())).map(p->p.getName()).collect(Collectors.toSet()).toString(),"[]"));
                    temp.setZjlx(StringUtils.strip(value.stream().filter(p->!"".equals(p.getZjlx())).map(p->p.getZjlx()).collect(Collectors.toSet()).toString(),"[]"));
                    temp.setZjhm(StringUtils.strip(value.stream().filter(p->!"".equals(p.getZjhm())).map(p->p.getZjhm()).collect(Collectors.toSet()).toString(),"[]"));
                    temp.setXzz_xzqh(StringUtils.strip(value.stream().filter(p->!"".equals(p.getXzz_xzqh())).map(p->p.getXzz_xzqh()).collect(Collectors.toSet()).toString(),"[]"));
                    temp.setDwdz(StringUtils.strip(value.stream().filter(p->!"".equals(p.getDwdz())).map(p->p.getDwdz()).collect(Collectors.toSet()).toString(),"[]"));
                    temp.setLxdh(StringUtils.strip(value.stream().filter(p->!"".equals(p.getLxdh())).map(p->p.getLxdh()).collect(Collectors.toSet()).toString(),"[]"));
                    temp.setLxsj(StringUtils.strip(value.stream().filter(p->!"".equals(p.getLxsj())).map(p->p.getLxsj()).collect(Collectors.toSet()).toString(),"[]"));
                    temp.setDwdh(StringUtils.strip(value.stream().filter(p->!"".equals(p.getDwdh())).map(p->p.getDwdh()).collect(Collectors.toSet()).toString(),"[]"));
                    temp.setZzdh(StringUtils.strip(value.stream().filter(p->!"".equals(p.getZzdh())).map(p->p.getZzdh()).collect(Collectors.toSet()).toString(),"[]"));
                    temp.setGzdw(StringUtils.strip(value.stream().filter(p->!"".equals(p.getGzdw())).map(p->p.getGzdw()).collect(Collectors.toSet()).toString(),"[]"));
                    temp.setEmail(StringUtils.strip(value.stream().filter(p->!"".equals(p.getEmail())).map(p->p.getEmail()).collect(Collectors.toSet()).toString(),"[]"));
                    result.add(temp);
                });
                new File(listPath.get(i)).delete();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if(csv!=null) {
                    csv.close();
                }
            }
        }
        return result;
    }


    public List<BankZzxxEntity> getByExcel(List<String> filepath) {
        Map<String, Integer> title = new HashMap();
        List<BankZzxxEntity> listB = new ArrayList<>();
        CsvReader csv = null;

        for (int i = 0; i < filepath.size(); i++) {
            try {
                if(filepath.get(i).endsWith(".xlsx")) {
                    if(filepath.get(i).contains("详细信息")){
                        final StringBuffer temp = new StringBuffer();
                        ExcelReader reader = new ExcelReader() {
                            public void getRows(int sheetIndex, int curRow, List<String> rowList) {
                                if(rowList.get(0).startsWith("(")){
                                    temp.delete(0,temp.length());
                                    if(rowList.get(0).contains(":")){
                                        temp.append(rowList.get(0).split(":")[1]);
                                    }else {
                                        temp.append(rowList.get(0).substring(rowList.get(0).indexOf(")")+1,rowList.get(0).length()));
                                    }
                                }
                                if(rowList.size()==20&&!rowList.get(0).contains("序号")){
                                    BankZzxxEntity zz = new BankZzxxEntity();
                                    zz.setJysj(TimeFormatUtil.getDateSwitchTimestamp(rowList.get(1)));
                                    if(rowList.get(3).equals(temp.toString())||rowList.get(4).equals(temp.toString())){
                                        zz.setYhkkh(rowList.get(3));
                                        zz.setJyxm(rowList.get(4));
                                        zz.setJyzjh(rowList.get(5));
                                        zz.setDskh(rowList.get(7));
                                        zz.setDsxm(rowList.get(8));
                                        zz.setDssfzh(rowList.get(9));
                                        zz.setSfbz("出");
                                    }
                                    if(rowList.get(7).equals(temp.toString())||rowList.get(8).equals(temp.toString())){
                                        zz.setYhkkh(rowList.get(7));
                                        zz.setJyxm(rowList.get(8));
                                        zz.setJyzjh(rowList.get(9));
                                        zz.setDskh(rowList.get(3));
                                        zz.setDsxm(rowList.get(4));
                                        zz.setDssfzh(rowList.get(5));
                                        zz.setSfbz("进");
                                    }
                                    zz.setJyje(new BigDecimal(rowList.get(12)));
                                    zz.setJyfsd(rowList.get(15));
                                    zz.setBz(rowList.get(16));
                                    if(zz.getDskh()==null || "".contains(zz.getDskh().trim())){
                                        String bcsm = zz.getYhkkh()+"-";
                                        if(zz.getDsxm()!=null && zz.getDsxm().trim().length()>0){
                                            bcsm+=zz.getDsxm();
                                        }else if(zz.getZysm()!=null && zz.getZysm().trim().length()>0){
                                            bcsm+=zz.getZysm();
                                        }else if(zz.getBz()!=null && zz.getBz().trim().length()>0){
                                            bcsm+=zz.getBz();
                                        }else {
                                            bcsm += "空账户";
                                        }
                                        zz.setBcsm(bcsm);
                                    }
                                    listB.add(zz);
                                }
                            }
                        };
                        reader.processOneSheet(filepath.get(i),2);
                    }else {
                        ExcelReader reader = new ExcelReader() {
                            public void getRows(int sheetIndex, int curRow, List<String> rowList) {
                                if (curRow == 0) {
                                    for (int i = 0; i < rowList.size(); i++) {
                                        String temp = rowList.get(i);
                                        getBzzxxTitle(temp, i, title);
                                    }
                                } else {
                                    listB.add(BankZzxxEntity.listToObj(rowList, title));
                                }
                            }
                        };
                        reader.process(filepath.get(i));
                    }
                }
                if(filepath.get(i).endsWith(".csv")){
                    csv = new CsvReader(filepath.get(i),',',Charset.forName("GBK"));
                    csv.readHeaders();
                    csv.setSafetySwitch(false);
                    String[] titles = csv.getHeaders();
                    for(int j=0;j<titles.length;j++){
                        getBzzxxTitle(titles[j],j,title);
                    }
                    csv.setSafetySwitch(false);
                    while (csv.readRecord()){
                        listB.add(BankZzxxEntity.listToObj(Arrays.asList(csv.getValues()), title));
                    }
                    csv.close();
                }
                new File(filepath.get(i)).delete();
            } catch (Exception e) {
                e.getStackTrace();
            }finally {
                if(csv!=null) {
                    csv.close();
                }
            }
        }
//        Set<BankZzxxEntity> setB = new HashSet<>(listB);

        return listB;
    }

    public List<BankZcxxEntity> insertBankZcxx(String filePath, long aj_id) {
        List<String> listPath = getFileLists(filePath, "开户");
        listPath.addAll(getFileLists(filePath,"账户信息"));
        listPath.addAll(getFileLists(filePath,"详细信息"));
        List<BankZcxxEntity> listZcxx = getBzcxxByFile(listPath);
//        int i = bzcd.saveZcxx(listZcxx, aj_id);
        BankPersonEntity bpe = new BankPersonEntity();
        for (BankZcxxEntity bce : listZcxx) {
            bpe = bpe.getByZcxx(bce);
            if (bpe.getYhkkh().trim().length() > 0) {
                bpd.insert(bpe);
            }
        }
        return listZcxx;
    }

    public  Map<String,Integer> getBzcxxTitle(String[] titles){
        Map<String, Integer> title = new HashMap<>();
        for (int i = 0; i < titles.length; i++) {
            String str = titles[i];
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
        return title;
    }

    public List<BankZcxxEntity> getBzcxxByFile(List<String> listPath) {
        Set<BankZcxxEntity> zcxxs = new HashSet<>();
        Map<String, Integer> title = new HashMap<>();
        CsvReader csv = null;
        for (String path : listPath) {
            try {


                if (path.endsWith(".xls") || path.endsWith(".xlsx")) {
                    if(path.contains("详细信息")){
                        ExcelReader reader = new ExcelReader() {
                            public void getRows(int sheetIndex, int curRow, List<String> rowList) {
                                if(rowList.size()>10&&!rowList.get(0).contains("序号")){
                                    BankZcxxEntity bc = new BankZcxxEntity();
                                    bc.setYhkkh(rowList.get(1));
                                    bc.setKhxm(rowList.get(2));
                                    bc.setKhzjh(rowList.get(3));
                                    bc.setKhh(StringUtils.strip(rowList.get(4),"()"));
                                    if(!zcxxs.contains(bc)) {
                                        zcxxs.add(bc);
                                    }
                                }
                            }
                        };
                        reader.processOneSheet(path,1);
                    }else {
                        ReadExcelUtils excelReader = new ReadExcelUtils(path);
                        String[] titles = excelReader.readExcelTitle();
                        title = getBzcxxTitle(titles);
                        Map<Integer, Map<Integer, Object>> map = excelReader.readExcelContent();
                        for (int i = 1; i <= map.size(); i++) {
                            if (!zcxxs.contains(BankZcxxEntity.mapToObj(map.get(i), title))) {
                                zcxxs.add(BankZcxxEntity.mapToObj(map.get(i), title));
                            }
                        }
                    }
                }
                if(path.endsWith(".csv")){
                    csv = new CsvReader(path,',',Charset.forName("GBK"));
                    csv.readHeaders();
                    csv.setSafetySwitch(false);
                    title = getBzcxxTitle(csv.getHeaders());
                    BankZcxxEntity zcxx = null;
                    while (csv.readRecord()){
                        zcxx = new BankZcxxEntity();
                        zcxx.setZhzt(csv.get(title.get("zhzt")).replace("\t","").trim());
                        zcxx.setKhxm(csv.get(title.get("khxm")).replace("\t","").trim());
                        zcxx.setYhkkh(zcxx.remove_(csv.get(title.get("yhkkh"))).replace("\t","").trim());
                        zcxx.setYhkzh(zcxx.remove_(csv.get(title.get("yhkzh"))).replace("\t","").trim());
                        if ("".equals(zcxx.getYhkkh())) {
                            zcxx.setYhkkh(zcxx.getYhkzh());
                        }
                        zcxx.setKhxm(csv.get(title.get("khxm")).replace("\t","").trim());
                        zcxx.setKhzjh(csv.get(title.get("khzjh")).replace("\t","").trim());
                        zcxx.setKhsj(csv.get(title.get("khsj")).replace("\t","").trim());
                        zcxx.setKhh(csv.get(title.get("khh")).replace("\t","").trim());
                        zcxx.setKyye(new BigDecimal(csv.get(title.get("kyye")).replace("\t","").trim().equals("")? "0":csv.get(title.get("kyye")).replace("\t","").trim()));
                        zcxx.setZhye(new BigDecimal(csv.get(title.get("zhye")).replace("\t","").trim().equals("")? "0":csv.get(title.get("zhye")).replace("\t","").trim()));
                        if (!zcxxs.contains(zcxx)) {
                            zcxxs.add(zcxx);
                        }
                    }
                    csv.close();
                }
                new File(path).delete();
            } catch (FileNotFoundException e) {
                System.out.println("未找到指定路径的文件!");
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if(csv!=null) {
                    csv.close();
                }
            }
        }

        return new ArrayList<>(zcxxs);
    }

    public static List<String> getFileLists(String filePath, String filter) {
        List<String> listPath = new ArrayList<String>();
        File dir = new File(filePath);
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.getName().contains(filter)&&!file.getName().contains("子账户信息")) {
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
    private static List<String> getZfbFileList(String uploadPath) {
        List<String> listPath = new ArrayList<String>();
        File dir = new File(uploadPath);
        File[] files = dir.listFiles();
        for (File file : files) {
            listPath.add(file.getAbsolutePath());
        }
        return listPath;
    }

    /**
     * 支付宝数据导入
     *
     * @param uploadPath
     * @param id
     * @return
     */
    public int insertZfb(String uploadPath, long id) {
        List<String> listPath = getZfbFileList(uploadPath);
        int sum = 0;
        for (String path : listPath) {
            if (path.contains("注册信息")) {
                List<ZfbZcxxEntity> zcxxList = (List<ZfbZcxxEntity>) getZfbByCsv(path, 1);
                sum += zfbZcxxDao.insertZcxx(zcxxList, id);
            } else if (path.contains("登陆日志")) {
                List<ZfbDlrzEntity> dlrzList = (List<ZfbDlrzEntity>) getZfbByCsv(path, 2);
                sum += zfbDlrzDao.insertDlrz(dlrzList, id);
            } else if (path.contains("交易记录")) {
                List<ZfbJyjlEntity> jyjlList = (List<ZfbJyjlEntity>) getZfbByCsv(path, 3);
                sum += zfbJyjlDao.insertJyjl(jyjlList, id);
            } else if (path.contains("账户明细")) {
                List<ZfbZhmxEntity> zhmxList = (List<ZfbZhmxEntity>) getZfbByCsv(path, 4);
                sum += zfbZhmxDao.insertZhmx(zhmxList, id);
            } else if (path.contains("转账明细")) {
                List<ZfbZzmxEntity> zzmxList = (List<ZfbZzmxEntity>) getZfbByCsv(path, 5);
                sum += zfbZzmxDao.insertZzmx(zzmxList, id);
            }
        }
        if (sum > 0) {
            // 修改交易记录表数据
            String sql = "update zfbjyjl j set j.jyzt='交易成功' where j.jyzt='TRADE_SUCCESS' and j.aj_id=" + id;
            zfbJyjlDao.updateBySql(sql);
        }
        // 账户明细清洗表数据添加
        int num = zfbZhmxQxsjDao.insertZhmxQxsj(id);
        // 添加账户明细点对点统计(支付宝与支付宝交易)
        List<ZfbZhmxTjjgEntity> zhmxTjjgList = zfbZhmxQxsjDao.selectTjjgList(id);
        zfbZhmxTjjgDao.delAll(id);
        zfbZhmxTjjgDao.insertZhmxTjjg(zhmxTjjgList, id);
        // 添加账户明细账户与银行账户点对点统计
        List<ZfbZhmxTjjgsEntity> zhmxTjjgsList = zfbZhmxQxsjDao.selectTjjgsList(id);
        zfbZhmxTjjgsDao.delAll(id);
        zfbZhmxTjjgsDao.insertZhmxTjjgs(zhmxTjjgsList, id);
        // 支付宝账户明细进出总账统计
        List<ZfbZhmxJczzEntity> zhmxJczzList = zfbZhmxQxsjDao.selectJczzList(id);
        zfbZhmxJczzDao.delAll(id);
        zfbZhmxJczzDao.insertZhmxJczz(zhmxJczzList, id);
        // 支付宝账户与银行账户按交易类型进出总账统计
        List<ZfbZhmxJylxEntity> zhmxJylxList = zfbZhmxQxsjDao.selectJylxList(id);
        zfbZhmxJylxDao.delAll(id);
        zfbZhmxJylxDao.insertZhmxJylx(zhmxJylxList, id);
        // 删除数据清洗表数据
        zfbZhmxQxsjDao.delAll();

        // 添加转账明细统计数据
        List<ZfbZzmxTjjgForm> tjjgForms = zfbZzmxTjjgDao.selectZzmxTjjg(id);
        List<ZfbZzmxTjjgEntity> zzmxTjjgList = ZfbZzmxTjjgEntity.FormToList(tjjgForms, id);
        zfbZzmxTjjgDao.delAll(id);
        zfbZzmxTjjgDao.insertZzmxTjjg(zzmxTjjgList);
        // 添加转账明细对手账户统计数据
        List<ZfbZzmxTjjgsForm> tjjgsForms = zfbZzmxTjjgsDao.selectZzmxTjjgs(id);
        List<ZfbZzmxTjjgsEntity> zzmxTjjgsList = ZfbZzmxTjjgsEntity.FormToList(tjjgsForms, id);
        zfbZzmxTjjgsDao.delAll(id);
        zfbZzmxTjjgsDao.insertZzmxTjjgs(zzmxTjjgsList);
        // 添加交易记录对手账户统计数据
        List<ZfbJyjlTjjgsForm> jyjlTjjgsForms = zfbJyjlTjjgsDao.selectJyjlTjjgs(id);
        List<ZfbJyjlTjjgsEntity> jyjlTjjgsList = ZfbJyjlTjjgsEntity.FormToList(jyjlTjjgsForms, id);
        zfbJyjlTjjgsDao.delAll(id);
        zfbJyjlTjjgsDao.insertJyjlTjjgs(jyjlTjjgsList);
        // 添加交易记录收件人地址统计数据
        List<ZfbJyjlSjdzsEntity> jyjlSjdzsForms = zfbJyjlSjdzsDao.selectJyjlSjdzs(id, null);
        zfbJyjlSjdzsDao.delAll(id);
        zfbJyjlSjdzsDao.insertJyjlSjdzs(jyjlSjdzsForms, id);
        return sum > 0 && num > 0 ? 1 : 0;
    }

    /**
     * 支付宝数据读取
     *
     * @param path
     * @param flag
     * @return
     */
    private static Object getZfbByCsv(String path, int flag) {
        List<ZfbZcxxEntity> zcxxList = new ArrayList<>();
        List<ZfbDlrzEntity> dlrzList = new ArrayList<>();
        List<ZfbJyjlEntity> jyjlList = new ArrayList<>();
        List<ZfbZhmxEntity> zhmxList = new ArrayList<>();
        List<ZfbZzmxEntity> zzmxList = new ArrayList<>();
        // 创建CSV读对象
        CsvReader csvReader = null;
        try {
            // 创建CSV读对象
            csvReader = new CsvReader(path, ',', Charset.forName("GBK"));
            // 读表头 不需要则跳过
            csvReader.readHeaders();
            // 读取内容
            while (csvReader.readRecord()) {
                if (flag == 1) {
                    // 注册信息
                    ZfbZcxxEntity zfbZcxxEntity = new ZfbZcxxEntity();
                    zfbZcxxEntity.setYhId(csvReader.get("用户Id").trim());
                    zfbZcxxEntity.setDlyx(csvReader.get("登陆邮箱").trim());
                    zfbZcxxEntity.setDlsj(csvReader.get("登陆手机").trim());
                    zfbZcxxEntity.setZhmc(csvReader.get("账户名称").trim());
                    zfbZcxxEntity.setZjlx(csvReader.get("证件类型").trim());
                    zfbZcxxEntity.setZjh(csvReader.get("证件号").trim());
                    zfbZcxxEntity.setKyye(Double.parseDouble(!"".equals(csvReader.get("可用余额"))?csvReader.get("可用余额"):"0.0"));
                    zfbZcxxEntity.setBdsj(csvReader.get("绑定手机").trim());
                    zfbZcxxEntity.setBdyhk(csvReader.get("绑定银行卡").trim());
                    zfbZcxxEntity.setDyxcsj(csvReader.get("对应的协查数据").trim());
                    zcxxList.add(zfbZcxxEntity);
                } else if (flag == 2) {
                    // 登陆日志
                    ZfbDlrzEntity zfbDlrzEntity = new ZfbDlrzEntity();
                    zfbDlrzEntity.setDlzh(csvReader.get("登陆账号").trim());
                    zfbDlrzEntity.setZfbyhId(csvReader.get("支付宝用户ID").trim());
                    zfbDlrzEntity.setZhm(csvReader.get("账户名").trim());
                    zfbDlrzEntity.setKhdIp(csvReader.get("客户端ip").trim());
                    zfbDlrzEntity.setCzfssj(csvReader.get("操作发生时间").trim());
                    zfbDlrzEntity.setDyxcsj(csvReader.get("对应的协查数据").trim());
                    dlrzList.add(zfbDlrzEntity);
                } else if (flag == 3) {
                    // 交易记录
                    ZfbJyjlEntity zfbJyjlEntity = new ZfbJyjlEntity();
                    zfbJyjlEntity.setJyh(csvReader.get("交易号").trim());
                    zfbJyjlEntity.setWbjyh(csvReader.get("外部交易号").trim());
                    zfbJyjlEntity.setJyzt(csvReader.get("交易状态").trim());
                    zfbJyjlEntity.setHzhbId(csvReader.get("合作伙伴ID").trim());
                    zfbJyjlEntity.setMjyhId(csvReader.get("买家用户id").trim());
                    zfbJyjlEntity.setMjxx(csvReader.get("买家信息").trim());
                    zfbJyjlEntity.setMijyhId(csvReader.get("卖家用户id").trim());
                    zfbJyjlEntity.setMijxx(csvReader.get("卖家信息").trim());
                    zfbJyjlEntity.setJyje(Double.parseDouble(!"".equals(csvReader.get("交易金额（元）"))?csvReader.get("交易金额（元）"):"0.0"));
                    zfbJyjlEntity.setSksj(csvReader.get("收款时间").trim());
                    zfbJyjlEntity.setZhxgsj(csvReader.get("最后修改时间").trim());
                    zfbJyjlEntity.setCjsj(csvReader.get("创建时间").trim());
                    zfbJyjlEntity.setJylx(csvReader.get("交易类型").trim());
                    zfbJyjlEntity.setLyd(csvReader.get("来源地").trim());
                    zfbJyjlEntity.setSpmc(csvReader.get("商品名称").trim());
                    zfbJyjlEntity.setShrdz(csvReader.get("收货人地址").trim());
                    zfbJyjlEntity.setDyxcsj(csvReader.get("对应的协查数据").trim());
                    jyjlList.add(zfbJyjlEntity);
                } else if (flag == 4) {
                    // 账户明细
                    ZfbZhmxEntity zfbZhmxEntity = new ZfbZhmxEntity();
                    zfbZhmxEntity.setJyh(csvReader.get("交易号").trim());
                    zfbZhmxEntity.setShddh(csvReader.get("商户订单号").trim());
                    zfbZhmxEntity.setJycjsj(csvReader.get("交易创建时间").trim());
                    zfbZhmxEntity.setFksj(csvReader.get("付款时间").trim());
                    zfbZhmxEntity.setZjxgsj(csvReader.get("最近修改时间").trim());
                    zfbZhmxEntity.setJylyd(csvReader.get("交易来源地").trim());
                    zfbZhmxEntity.setLx(csvReader.get("类型").trim());
                    zfbZhmxEntity.setYhxx(csvReader.get("用户信息").trim());
                    zfbZhmxEntity.setJydfxx(csvReader.get("交易对方信息").trim());
                    zfbZhmxEntity.setXfmc(csvReader.get("消费名称").trim());
                    zfbZhmxEntity.setJe(Double.parseDouble(!"".equals(csvReader.get("金额（元）"))?csvReader.get("金额（元）"):"0.0"));
                    zfbZhmxEntity.setSz(csvReader.get("收/支").trim());
                    zfbZhmxEntity.setJyzt(csvReader.get("交易状态").trim());
                    zfbZhmxEntity.setBz(csvReader.get("备注").trim());
                    zfbZhmxEntity.setDyxcsj(csvReader.get("对应的协查数据").trim());
                    zhmxList.add(zfbZhmxEntity);
                } else if (flag == 5) {
                    // 转账明细
                    ZfbZzmxEntity zfbZzmxEntity = new ZfbZzmxEntity();
                    zfbZzmxEntity.setJyh(csvReader.get("交易号").trim());
                    zfbZzmxEntity.setFkfzfbzh(csvReader.get("付款方支付宝账号").trim());
                    zfbZzmxEntity.setSkfzfbzh(csvReader.get("收款方支付宝账号").trim());
                    zfbZzmxEntity.setSkjgxx(csvReader.get("收款机构信息").trim());
                    zfbZzmxEntity.setDzsj(csvReader.get("到账时间").trim());
                    zfbZzmxEntity.setZzje(Double.parseDouble(!"".equals(csvReader.get("转账金额（元）"))?csvReader.get("转账金额（元）"):"0.0"));
                    zfbZzmxEntity.setZzcpmc(csvReader.get("转账产品名称").trim());
                    zfbZzmxEntity.setJyfsd(csvReader.get("交易发生地").trim());
                    zfbZzmxEntity.setTxlsh(csvReader.get("提现流水号").trim());
                    zfbZzmxEntity.setDyxcsj(csvReader.get("对应的协查数据").trim());
                    zzmxList.add(zfbZzmxEntity);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (csvReader != null) {
                csvReader.close();
            }
        }

        if (zcxxList.size() > 0) {
            return zcxxList;
        } else if (dlrzList.size() > 0) {
            return dlrzList;
        } else if (jyjlList.size() > 0) {
            return jyjlList;
        } else if (zhmxList.size() > 0) {
            return zhmxList;
        } else if (zzmxList.size() > 0) {
            return zzmxList;
        }
        return null;
    }
}
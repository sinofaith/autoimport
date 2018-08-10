package cn.com.sinofaith.service;

import cn.com.sinofaith.bean.bankBean.BankPersonEntity;
import cn.com.sinofaith.bean.bankBean.BankZcxxEntity;
import cn.com.sinofaith.bean.bankBean.BankZzxxEntity;
import cn.com.sinofaith.bean.cftBean.CftPersonEntity;
import cn.com.sinofaith.bean.cftBean.CftZcxxEntity;
import cn.com.sinofaith.bean.cftBean.CftZzxxEntity;
import cn.com.sinofaith.dao.bankDao.BankPersonDao;
import cn.com.sinofaith.dao.bankDao.BankZcxxDao;
import cn.com.sinofaith.dao.bankDao.BankZzxxDao;
import cn.com.sinofaith.dao.cftDao.CftPersonDao;
import cn.com.sinofaith.dao.cftDao.CftZcxxDao;
import cn.com.sinofaith.dao.cftDao.CftZzxxDao;
import cn.com.sinofaith.util.ExcelReader;
import cn.com.sinofaith.util.ReadExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private BankPersonDao bpd;

    public int deleteAll(String uploadPath){
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
        }catch (Exception e){
            e.getMessage();
        }
        return 0;
    }

    public int insertZcxx(String filepath, String filter,long aj){
        List<String> listPath = getFileList(filepath,filter);
        List<CftZcxxEntity> listZcxx = getZcxxByTxt(listPath);
        int i = zcd.saveZcxx(listZcxx,aj);
        return i;
    }

    public int insertZzxx(String filepath,String filter,long aj){
        List<String> listPath = getFileList(filepath,filter);
        List<CftZzxxEntity> listZzxx = getZzxxByTxt(listPath);
        int i = zzd.insertZzxx(listZzxx,aj);
        return i;
    }

    public List<CftZzxxEntity> getZzxxByTxt(List<String> listPath){
        List<CftZzxxEntity> zzxxs = new ArrayList<>();
        File file = null;
        BufferedReader br = null;
        FileInputStream fis = null;
        InputStreamReader isr = null;
        if(listPath.size()>0){
            for(int i=0; i<listPath.size();i++){
                try {
                    file = new File(listPath.get(i));
                    fis = new FileInputStream(file);
                    isr = new InputStreamReader(fis,"UTF-8");
                    br = new BufferedReader(isr);
                    String txtStr="";
                    List<String> zzxxStr = new ArrayList<>();
                    while ((txtStr=br.readLine())!=null){
                        if (txtStr.contains("用户ID")) {
                            continue;
                        }
                        String[] s = txtStr.split("\t");
                        zzxxStr = Arrays.asList(s);
                        if(zzxxStr.size()>8){
                            zzxxs.add(CftZzxxEntity.listToObj(zzxxStr));
                        }
                    }
                    br.close();
                    file.delete();
                }catch (IOException e){
                    e.printStackTrace();
                }finally {
                    if(br != null){
                        try {
                            br.close();
                            file.delete();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return zzxxs;
    }

    public List<CftZcxxEntity> getZcxxByTxt(List<String> listPath){
        List<CftZcxxEntity> zcxxs = new ArrayList<CftZcxxEntity>();
        File file = null;
        BufferedReader br = null;
        FileInputStream fis = null;
        InputStreamReader isr = null;
        List<String> zcxxStr = null;
        List<CftZcxxEntity> listZcxx = null;
        CftPersonEntity cp = null;
        int line = 0;
        if(listPath.size()>0){
            for(int i=0;i< listPath.size();i++){
                try{
                     file = new File(listPath.get(i));
                     fis = new FileInputStream(file);
                     isr = new InputStreamReader(fis,"UTF-8");
                    br = new BufferedReader(isr);
                    Map<String,Integer> map = new HashMap<>();
                    String txtString = "";
                    listZcxx = new ArrayList<CftZcxxEntity>();
                    line = 0;
                    while ((txtString = br.readLine()) != null){
                        String[] s = txtString.split("\t");
                        zcxxStr= Arrays.asList(s);
                        if (txtString.contains("账户状态")) {
                            for (int j=0;j<zcxxStr.size();j++) {
                                if(zcxxStr.get(j).contains("账户状态")){
                                    map.put("zhzt",j);
                                }else if(zcxxStr.get(j).contains("账号")&&!zcxxStr.get(j).contains("银行")){
                                    map.put("zh",j);
                                }else if(zcxxStr.get(j).contains("注册姓名")){
                                    map.put("xm",j);
                                }else if(zcxxStr.get(j).contains("注册时间")){
                                    map.put("zcsj",j);
                                }else if(zcxxStr.get(j).contains("注册身份证号")){
                                    map.put("sfzhm",j);
                                }else if(zcxxStr.get(j).contains("绑定手机")){
                                    map.put("bdsj",j);
                                }else if(zcxxStr.get(j).contains("开户行信息")){
                                    map.put("khh",j);
                                }else if(zcxxStr.get(j).contains("银行账号")){
                                    map.put("yhzh",j);
                                }
                            }
                            line++;
                            continue;
                        }
                        if(line>1){
                            if(zcxxStr.get(0).isEmpty()){
                                zcxxStr.set(0,listZcxx.get(0).getZhzt());
                            }
                            if(zcxxStr.get(1).isEmpty()){
                                zcxxStr.set(1,listZcxx.get(0).getZh());
                            }
                            if(zcxxStr.get(2).isEmpty()){
                                zcxxStr.set(2,listZcxx.get(0).getXm());
                            }
                            if(zcxxStr.get(3).isEmpty()){
                                zcxxStr.set(3,listZcxx.get(0).getZcsj());
                            }
                            if(zcxxStr.get(4).isEmpty()){
                                zcxxStr.set(4,listZcxx.get(0).getSfzhm());
                            }
                            if(zcxxStr.get(5).isEmpty()){
                                zcxxStr.set(5,listZcxx.get(0).getBdsj());
                            }
                        }
                        if(zcxxStr.size()>7){
                            listZcxx.add(CftZcxxEntity.listToObj(zcxxStr,map));
                        }
                        cp = CftPersonEntity.listToObj(zcxxStr);
                        if(cp.getXm().length()>0&&cp.getZh().length()>0){
                            cpd.insert(cp);
                        }
                        line++;
                    }
                    zcxxs.addAll(listZcxx);
                    br.close();
                    file.delete();
                }catch (IOException e){
                    e.printStackTrace();
                }finally {
                    if(br!=null){
                        try {
                            br.close();
                            file.delete();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return zcxxs;
    }

    public int insertBankZzxx(String filePath,long aj_id){
        List<String> listPath = getFileLists(filePath,"交易明细");
        List<BankZzxxEntity> listZzxx = getByExcel(listPath);
        int i = bzzd.insertZzxx(listZzxx,aj_id);
        return i;
    }
    public List<BankZzxxEntity> getByExcel(List<String> filepath){
        final Map<String,Integer> title=new HashMap();
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
                            } else if(temp.contains("交易时间")){
                                title.put("jysfm",i);
                            }else if (temp.contains("交易金额")) {
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
                    }else{
                        listB.add(BankZzxxEntity.listToObj(rowList, title));
                    }
                }
            };
        for(int i=0;i<filepath.size();i++) {
            try {
                reader.process(filepath.get(i));

                new File(filepath.get(i)).delete();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
        Set<BankZzxxEntity> setB = new HashSet<>(listB);

        return new ArrayList<>(setB);
    }

    public int insertBankZcxx(String filePath,long aj_id){
        List<String> listPath = getFileLists(filePath,"开户");
        List<BankZcxxEntity> listZcxx = getBzcxxByExcel(listPath);
        int i = bzcd.saveZcxx(listZcxx,aj_id);
        BankPersonEntity bpe=new BankPersonEntity();
        for(BankZcxxEntity bce:listZcxx){
            bpe.setXm(bce.getKhxm());
            bpe.setYhkkh(bce.getYhkkh());
            bpe.setYhkzh(bce.getYhkzh());
            if(bpe.getYhkkh().trim().length()>0) {
                bpd.insert(bpe);
            }
        }
        return i;
    }

    public List<BankZcxxEntity> getBzcxxByExcel(List<String> listPath){
        List<BankZcxxEntity> zcxxs = new ArrayList<>();
        for(String path:listPath){
            try {
                ReadExcelUtils excelReader = new ReadExcelUtils(path);
                String[] titles = excelReader.readExcelTitle();
                List<String> strTitle = new ArrayList<>(Arrays.asList(titles));
                Map<String,Integer> title = new HashMap<>();
                for(int i=0;i<strTitle.size();i++){
                    String str=strTitle.get(i);
                    String lb = "";
                    if(str!=null&&str.length()>0) {
                        if (str.contains("账户开户名称")) {
                            lb = "khxm";
                        } else if (str.contains("开户人证件号码")) {
                            lb = "khzjh";
                        } else if (str.contains("交易卡号")) {
                            lb = "yhkkh";
                        } else if (str.contains("交易账号")){
                            lb = "yhkzh";
                        } else if (str.contains("账号开户时间")) {
                            lb = "khsj";
                        } else if (str.contains("开户网点")) {
                            lb = "khh";
                        } else if (str.contains("账户状态")) {
                            lb = "zhzt";
                        } else if(str.contains("账户余额")) {
                            lb = "zhye";
                        } else if(str.contains("可用余额")) {
                            lb = "kyye";
                        }
                        if(!"".endsWith(lb)) {
                            title.put(lb, i);
                        }
                    }
                }

                Map<Integer, Map<Integer,Object>> map = excelReader.readExcelContent();
                for(int i=1;i<=map.size();i++){
                    if(!zcxxs.contains(BankZcxxEntity.mapToObj(map.get(i),title))){
                        zcxxs.add(BankZcxxEntity.mapToObj(map.get(i),title));
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("未找到指定路径的文件!");
                e.printStackTrace();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        return  zcxxs;
    }

    public static List<String> getFileLists(String filePath,String filter){
        List<String> listPath = new ArrayList<String>();
        File dir = new File(filePath);
        File[] files = dir.listFiles();
        for(File file:files){
            if(file.getName().contains(filter)){
                listPath.add(file.getAbsolutePath());
            }
        }
        return listPath;
    }


    public static List<String> getFileList(String filepath, String filter) {
        List<String> listPath = new ArrayList<String>();
        File dir= new File(filepath);
        File[] files = dir.listFiles();//获取文件夹下所有文件
        if(files!=null){
            for(int i=0;i<files.length;i++){
                String fileName = files[i].getName();
                if(files[i].isDirectory()){
                    getFileList(files[i].getAbsolutePath(),filter);
                }else if(fileName.endsWith(filter)){
                    String strFileName = files[i].getAbsolutePath();
                    listPath.add(strFileName);
                }
            }
        }
        return listPath;
    }
}
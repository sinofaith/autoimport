package cn.com.sinofaith.service;

import cn.com.sinofaith.bean.cftBean.CftPersonEntity;
import cn.com.sinofaith.bean.cftBean.CftZcxxEntity;
import cn.com.sinofaith.bean.cftBean.CftZzxxEntity;
import cn.com.sinofaith.dao.cftDao.CftPersonDao;
import cn.com.sinofaith.dao.cftDao.CftZcxxDao;
import cn.com.sinofaith.dao.cftDao.CftZzxxDao;
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
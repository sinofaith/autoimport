package cn.com.sinofaith.service;

import cn.com.sinofaith.bean.CftZcxxEntity;
import cn.com.sinofaith.bean.CftZzxxEntity;
import cn.com.sinofaith.dao.CftZcxxDao;
import cn.com.sinofaith.dao.CftZzxxDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Me. on 2018/5/23
 */
@Service
public class UploadService {

    @Autowired
    private CftZcxxDao zcd;

    @Autowired
    private CftZzxxDao zzd;

    public int insertZcxx(String filepath, String filter){
        List<String> listPath = getFileList(filepath,filter);
        List<CftZcxxEntity> listZcxx = getZcxxByTxt(listPath);
        int i = zcd.saveZcxx(listZcxx);
        return i;
    }

    public int insertZzxx(String filepath,String filter){
        List<String> listPath = getFileList(filepath,filter);
        List<CftZzxxEntity> listZzxx = getZzxxByTxt(listPath);
        int i = zzd.saveZzxx(listZzxx);
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
//                    String txtPath = listPath.get(i);
                    isr = new InputStreamReader(fis,"UTF-8");
                    br = new BufferedReader(isr);
                    String txtStr="";
                    List<String> zzxxStr = new ArrayList<>();
                    while ((txtStr=br.readLine())!=null){
                        if (txtStr.startsWith("用户ID")) {
                            continue;
                        }
                        String[] s = txtStr.split("\t");
                        zzxxStr = Arrays.asList(s);
                        if(zzxxStr.size()>8){
                            zzxxs.add(CftZzxxEntity.listToObj(zzxxStr));
                        }
                    }
                    br.close();
                    isr.close();
                    fis.close();
                    file.delete();
                }catch (IOException e){
                    e.printStackTrace();
                }finally {
                    if(br != null){
                        try {
                            br.close();
                            isr.close();
                            fis.close();
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
        if(listPath.size()>0){
            for(int i=0;i< listPath.size();i++){
                try{
                     file = new File(listPath.get(i));
                     fis = new FileInputStream(file);
//                    String txtPath = listPath.get(i);
                     isr = new InputStreamReader(fis,"UTF-8");
                    br = new BufferedReader(isr);
                    String txtString = "";
                    List<String> zcxxStr = new ArrayList<String>();
                    List<CftZcxxEntity> listZcxx = new ArrayList<CftZcxxEntity>();
                    int line = 0;
                    while ((txtString = br.readLine()) != null){
                        if (txtString.startsWith("账户状态")) {
                            line++;
                            continue;
                        }
                        String[] s = txtString.split("\t");
                        zcxxStr= Arrays.asList(s);
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
                            listZcxx.add(CftZcxxEntity.listToObj(zcxxStr));
                            zcxxs.addAll(listZcxx);
                        }
                        line++;
                    }
                    br.close();
                    isr.close();
                    fis.close();
                    file.delete();
                }catch (IOException e){
                    e.printStackTrace();
                }finally {
                    if(br!=null){
                        try {
                            br.close();
                            isr.close();
                            fis.close();
                            file.delete();
                        }catch (IOException e){

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
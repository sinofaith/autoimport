package cn.com.sinofaith.controller;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.bankBean.BankZzxxEntity;
import cn.com.sinofaith.bean.cftBean.CftZzxxEntity;
import cn.com.sinofaith.bean.wlBean.WuliuEntity;
import cn.com.sinofaith.bean.wlBean.WuliuRelationEntity;
import cn.com.sinofaith.service.*;
import cn.com.sinofaith.service.bankServices.BankTjjgServices;
import cn.com.sinofaith.service.bankServices.BankTjjgsService;
import cn.com.sinofaith.service.bankServices.BankZzxxServices;
import cn.com.sinofaith.service.cftServices.CftTjjgService;
import cn.com.sinofaith.service.cftServices.CftTjjgsService;
import cn.com.sinofaith.service.cftServices.CftZzxxService;
import cn.com.sinofaith.service.wlService.WuliuJjxxService;
import cn.com.sinofaith.service.wlService.WuliuShipService;
import cn.com.sinofaith.service.wlService.WuliuSjService;
import cn.com.sinofaith.util.TimeFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Me. on 2018/5/23
 */
@Controller
public class UploadController {
    @Autowired
    private UploadService us;
    @Autowired
    private CftTjjgService tjs;
    @Autowired
    private CftTjjgsService tjss;
    @Autowired
    private CftZzxxService zzs;
    @Autowired
    private BankZzxxServices bzs;
    @Autowired
    private BankTjjgServices btjs;
    @Autowired
    private BankTjjgsService btjss;
    @Autowired
    private WuliuJjxxService wjs;
    @Autowired
    private WuliuShipService wlsService;
    @Autowired
    private WuliuSjService wlsjService;
    @Autowired
    private AjServices ajs;

    @RequestMapping(value = "/uploadCft",method = RequestMethod.POST,produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String uploadFileFolder(@RequestParam("file") List<MultipartFile> file, @RequestParam("aj") String aj,
                                   @RequestParam("checkBox") long checkBox,HttpServletRequest request){
        String uploadPath = request.getSession().getServletContext().getRealPath("/")+"upload/temp/"+System.currentTimeMillis()+"/";
        String filePath ="";
        String fileName="";
        String result = "";
        File uploadFile = null;
        File uploadPathd = new File(uploadPath);
        if(!uploadPathd.exists()){
            uploadPathd.mkdirs();
        }
        for(int i=0;i<file.size();i++){
            fileName =System.currentTimeMillis() +file.get(i).getOriginalFilename();
            filePath = uploadPath + fileName;
            if(fileName.endsWith(".txt")){
                uploadFile = new File(filePath);
                try {
                    file.get(i).transferTo(uploadFile);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        int a = 0;
        int b = 0;

        AjEntity aje = ajs.findByName(aj).get(0);
        if(uploadPathd.listFiles()!=null) {
            if (aje.getFlg() != checkBox) {
                aje.setFlg(checkBox);
                ajs.updateAj(aje);
                aje = ajs.findByName(aj).get(0);
            }

            a= us.insertZcxx(uploadPath, "info.txt", aje.getId());
            b=us.insertZzxx(uploadPath, "trades.txt", aje.getId());

            us.deleteAll(uploadPath);
            uploadPathd.delete();

            List<CftZzxxEntity> listZzxx = zzs.getAll(aje.getId(),checkBox);
            tjs.count(listZzxx,aje.getId());
            tjss.count(listZzxx,aje.getId());
        }

        if(a+b>0){
            result = String.valueOf(a+b);
        }else {
            result = "";
        }
        request.getSession().setAttribute("aj",aje);
        return result;
    }

    @RequestMapping(value = "/uploadBank",method = RequestMethod.POST,produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String uploadBank(@RequestParam("file") List<MultipartFile> file, @RequestParam("aj") String aj,
                             HttpServletRequest request) {
        String uploadPath = request.getSession().getServletContext().getRealPath("/")+"upload/temp/"+System.currentTimeMillis()+"/";
        String filePath ="";
        String fileName="";
        String result = "";
        File uploadFile = null;
        File uploadPathd = new File(uploadPath);
        if(!uploadPathd.exists()){
            uploadPathd.mkdirs();
        }
        for(int i=0;i<file.size();i++){
            fileName =System.currentTimeMillis()+file.get(i).getOriginalFilename();
            filePath = uploadPath + fileName;
            if(fileName.endsWith(".xlsx")||fileName.endsWith(".xls")){
                uploadFile = new File(filePath);
                try {
                    file.get(i).transferTo(uploadFile);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        int a = 0;
        int b = 0;

        AjEntity aje = ajs.findByName(aj).get(0);
        if(uploadPathd.listFiles()!=null) {
            List<BankZzxxEntity> listZzxx = bzs.getAll(aje.getId());
            us.insertBankZcxx(uploadPath,aje.getId());
            us.insertBankZzxx(uploadPath,aje.getId(),listZzxx);
            us.deleteAll(uploadPath);
            uploadPathd.delete();
            listZzxx = bzs.getAll(aje.getId());
            Set<BankZzxxEntity> setB = new HashSet<>(listZzxx);
            listZzxx = new ArrayList<>(setB);
            setB = null;
            btjs.count(listZzxx,aje.getId());
            btjss.count(listZzxx,aje.getId());
        }

        if(a+b>0){
            result = String.valueOf(a+b);
        }else {
            result = "";
        }
        request.getSession().setAttribute("aj",aje);
        return result;
    }

    @RequestMapping(value = "/uploadWuliu",method = RequestMethod.POST,produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String uploadWuliu(@RequestParam("file") List<MultipartFile> file, @RequestParam("aj") String aj,
                              HttpServletRequest req) {
        // 创建一个路径
        String uploadPath = req.getSession().getServletContext().getRealPath("/") + "upload/temp/" + System.currentTimeMillis() + "/";
        String filePath = "";
        String fileName = "";
        String result = "";
        File uploadFile = null;
        File uploadPathd = new File(uploadPath);
        if (!uploadPathd.exists()) {
            uploadPathd.mkdirs();
        }

        // 将文件上传到服务器
        for(int i=0;i<file.size();i++) {
            fileName =System.currentTimeMillis()+file.get(i).getOriginalFilename();
            filePath = uploadPath + fileName;
            if(fileName.endsWith(".xlsx")||fileName.endsWith(".xls")){
                uploadFile = new File(filePath);
                try {
                    file.get(i).transferTo(uploadFile);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        int a = 0;
        int b = 0;

        AjEntity aje = ajs.findByName(aj).get(0);
        if(uploadPathd.listFiles()!=null) {
            List<WuliuEntity> listJjxx = wjs.getAll(aje.getId());
            // wuliu表添加数据
            us.insertWuliuJjxx(uploadPath,aje.getId(),listJjxx);
            // wuliu_relation表添加数据
            wjs.insertRelation(aje.getId());
            // wuliu_ship表添加数据
            wlsService.insertShip(aje.getId());
            // wuliu_sj表添加数据
            wlsjService.insertSj(aje.getId());
            us.deleteAll(uploadPath);
            uploadPathd.delete();
        }
        if(a+b>0){
            result = String.valueOf(a+b);
        }else {
            result = "";
        }
        req.getSession().setAttribute("aj",aje);
        return result;
    }
}

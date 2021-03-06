package cn.com.sinofaith.controller;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.UserEntity;
import cn.com.sinofaith.bean.bankBean.BankCustomerEntity;
import cn.com.sinofaith.bean.bankBean.BankZcxxEntity;
import cn.com.sinofaith.bean.bankBean.BankZzxxEntity;
import cn.com.sinofaith.bean.bankBean.MappingBankzzxxEntity;
import cn.com.sinofaith.bean.cftBean.CftZzxxEntity;
import cn.com.sinofaith.bean.wlBean.WuliuEntity;
import cn.com.sinofaith.service.*;
import cn.com.sinofaith.service.PyramidSaleService.PyramidSaleService;
import cn.com.sinofaith.service.bankServices.*;
import cn.com.sinofaith.service.cftServices.CftTjjgService;
import cn.com.sinofaith.service.cftServices.CftTjjgsService;
import cn.com.sinofaith.service.cftServices.CftZzxxService;
import cn.com.sinofaith.service.wlService.WuliuJjxxService;
import cn.com.sinofaith.service.wlService.WuliuShipService;
import cn.com.sinofaith.service.wlService.WuliuSjService;
import cn.com.sinofaith.service.zfbService.*;
import org.apache.xpath.SourceTree;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.stream.Collectors;


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
    private BankZcxxServices bankZcxxServices;
    @Autowired
    private BankZzxxServices bzs;
    @Autowired
    private BankCustomerServices bcs;
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
    private PyramidSaleService pyramidSaleService;
    @Autowired
    private ZfbZzmxService zfbZzmxService;
    /*@Autowired
    private ZfbZcxxService zfbZcxxService;
    @Autowired
    private ZfbDlrzService zfbDlrzService;
    @Autowired
    private ZfbZhmxService zfbZhmxService;
    @Autowired
    private ZfbJyjlService zfbJyjlService;*/
    @Autowired
    private AjServices ajs;

    @RequestMapping(value = "/uploadCfttxt",method = RequestMethod.POST,produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String uploadFileFolder(@RequestParam("file") List<MultipartFile> file, @RequestParam("aj") String aj,
                                   @RequestParam("checkBox") long checkBox, HttpServletRequest request){
        UserEntity user =(UserEntity) request.getSession().getAttribute("user");
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

        AjEntity aje = ajs.findByName(aj,user.getId()).get(0);
        if(uploadPathd.listFiles()!=null) {
            if (aje.getFlg() != checkBox) {
                aje.setFlg(checkBox);
                ajs.updateAj(aje);
                aje = ajs.findByName(aj,user.getId()).get(0);
            }

            a= us.insertZcxx(uploadPath, "info.txt", aje.getId());
            b=us.insertZzxx(uploadPath, "trades.txt", aje.getId());

            us.deleteAll(uploadPath);
            uploadPathd.delete();
            if(b!=0) {
//                List<CftZzxxEntity> listZzxx = zzs.getAll(aje.getId(), checkBox);
//                tjs.count(listZzxx, aje.getId());
//                tjss.count(listZzxx, aje.getId());
                String seach = "";
                if(checkBox==1){
                    seach=" and shmc not like '%红包%'";
                }
                if(aje.getCftminsj().length()>1){
                    seach += " and jysj >= '"+aje.getCftminsj()+"'";
                }
                if(aje.getCftmaxsj().length()>1){
                    seach+=" and jysj <= '"+aje.getCftmaxsj()+"'";
                }
                zzs.countTjjgAndTjjgs(aje.getId(),seach);
            }
        }

        if(a+b>0){
            result = String.valueOf(a+b);
        }else {
            result = "";
        }
        request.getSession().setAttribute("aj",aje);
        return result;
    }

    /**
     * 财付通字段映射
     * @param file
     * @param req
     * @return
     */
    @RequestMapping(value = "/uploadCftxlsx",method = RequestMethod.POST)
    public @ResponseBody Map<String,Map<String,List<String>>> fieldMappingCft(@RequestParam("file") List<MultipartFile> file,
                     @RequestParam("aj") String aj,@RequestParam("checkBox") long checkBox,HttpServletRequest req){
        // 创建一个路径
        UserEntity user =(UserEntity) req.getSession().getAttribute("user");
        String uploadPath = req.getSession().getServletContext().getRealPath("/") + "upload/temp/" + System.currentTimeMillis() + "/";
        String filePath = "";
        String fileName = "";
        File uploadFile = null;
        File uploadPathd = new File(uploadPath);
        if (!uploadPathd.exists()) {
            uploadPathd.mkdirs();
        }
        // 将文件上传到服务器
        for(int i=0;i<file.size();i++) {
            fileName = file.get(i).getOriginalFilename();
            filePath = uploadPath + fileName;
            if(filePath.endsWith(".xlsx") || filePath.endsWith(".xls")){
                uploadFile = new File(filePath);
                try {
                    file.get(i).transferTo(uploadFile);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        AjEntity aje = ajs.findByName(aj,user.getId()).get(0);
        if(uploadPathd.listFiles()!=null) {
            if (aje.getFlg() != checkBox) {
                aje.setFlg(checkBox);
                ajs.updateAj(aje);
            }
        }
        req.getSession().setAttribute("cftPath",uploadPath);
        // 读取服务器的excel表
        Map<String,Map<String,List<String>>> excelMap = zfbZzmxService.readExcel(uploadPath);
        if(excelMap!=null){
            return excelMap;
        }
        return null;
    }

    /**
     * 财付通数据存入数据库
     * @param field
     * @return
     */
    @RequestMapping(value = "/uploadCftExcel",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public @ResponseBody String uploadCftExcel(@RequestBody List<List<String>> field, HttpSession session) {
        // 读取域中数据
        String path = (String) session.getAttribute("cftPath");
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        // 将数据插入数据库
        int sum = zzs.insertCft(path,field,aj.getId());
        // 删除文件
//        String uploadPath = session.getServletContext().getRealPath("/") + "upload/temp/";
        File uploadPathd = new File(path);
        zzs.deleteFile(uploadPathd);
        // 统计数据
        String seach = " and shmc not like '%红包%' ";
        if(aj.getCftminsj().length()>1){
            seach += " and jysj >= '"+aj.getCftminsj()+"'";
        }
        if(aj.getCftmaxsj().length()>1){
            seach+=" and jysj <= '"+aj.getCftmaxsj()+"'";
        }
        zzs.countTjjgAndTjjgs(aj.getId(),seach);
        if(sum>0){
            return "200";
        } else {
            return "400";
        }
    }

    /**
     * 资金数据字段映射
     * @param file
     * @param req
     * @return
     */
    @RequestMapping(value = "/uploadBankExcel",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Map<String,List<String>>> uploadBank(@RequestParam("file") List<MultipartFile> file, HttpServletRequest req) {
        String uploadPath = req.getSession().getServletContext().getRealPath("/")+"upload/temp/"+System.currentTimeMillis()+"/";
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
        if(uploadPathd.listFiles()!=null) {
            req.getSession().setAttribute("bankPath",uploadPath);
            Map<String,Map<String,List<String>>> excelMap = bzs.readExcel(uploadPath);
            if(excelMap!=null){
                return excelMap;
            }
        }
        return null;
    }

    /**
     * 资金数据存入数据库
     * @param field
     * @return
     */
    @RequestMapping(value = "/insertBankExcel",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public @ResponseBody String insertBankExcel(@RequestBody List<List<String>> field, HttpSession session) {
        // 读取域中数据
        String path = (String) session.getAttribute("bankPath");
        List<String> listPath = bankZcxxServices.getBankFileList(path);
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        // 取出excel表数据
        List<BankZcxxEntity> bankZcxxList = bankZcxxServices.getBankZcxxAll(listPath, field,aj.getId());
        int num = bzs.getBankZzxxAll(listPath, field, aj);
        List<BankCustomerEntity> bankCustList = bcs.getBankCustAll(listPath, field, aj);
//        listZzxx = bzs.getAll(aj.getId());
//        Set<BankZzxxEntity> setB = new HashSet<>(listZzxx);
//        listZzxx = new ArrayList<>(setB);
//        int count1 = btjs.count(listZzxx, aj.getId(), bankZcxxList);
//        int count2 = btjss.count(listZzxx,aj.getId());
        String sql = "";
        if(aj.getZjminsj().length()>1){
            sql += " and jysj >= '"+aj.getZjminsj()+"' ";
        }
        if(aj.getZjminsj().length()>1){
            sql+=" and jysj <= '"+aj.getZjmaxsj()+"' ";
        }
        bzs.countTjjgAndTjjgs(aj.getId(),bankZcxxList,sql);
        bankZcxxList.clear();
        // 删除文件
        File uploadPathd = new File(path);
        zfbZzmxService.deleteFile(uploadPathd);
        if(num>0){
            return "200";
        } else {
            return "400";
        }
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
        UserEntity user =(UserEntity) request.getSession().getAttribute("user");
        if(!uploadPathd.exists()){
            uploadPathd.mkdirs();
        }
        for(int i=0;i<file.size();i++){
            fileName =System.currentTimeMillis()+file.get(i).getOriginalFilename();
            filePath = uploadPath + fileName;
            if(fileName.endsWith(".xlsx")||fileName.endsWith(".xls")||fileName.endsWith(".csv")){
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

        AjEntity aje = ajs.findByName(aj,user.getId()).get(0);
        if(uploadPathd.listFiles()!=null) {
            List<BankZcxxEntity> listzcxx = us.insertBankZcxx(uploadPath,aje.getId());
            us.insertBankCustomer(uploadPath,aje);
            a=us.insertBankZzxx(uploadPath,aje.getId());
            us.deleteAll(uploadPath);
            uploadPathd.delete();
//            listZzxx = bzs.getAll(aje.getId());
//            Set<BankZzxxEntity> setB = new HashSet<>(listZzxx);
//            listZzxx = new ArrayList<>(setB);
//            setB = null;
//            btjs.count(listZzxx,aje.getId(),listzcxx);
//            btjss.count(listZzxx,aje.getId());
            String sql = "";
            if(aje.getZjminsj().length()>1){
                sql += " and jysj >= '"+aje.getZjminsj()+"' ";
            }
            if(aje.getZjminsj().length()>1){
                sql+=" and jysj <= '"+aje.getZjmaxsj()+"' ";
            }
            bzs.countTjjgAndTjjgs(aje.getId(),listzcxx,sql);
            listzcxx.clear();
        }

        if(a+b>0){
            result = String.valueOf(a+b);
        }else {
            result = "";
        }
        request.getSession().setAttribute("aj",aje);
        return result;
    }

    /**
     * 物流字段映射
     * @param file
     * @param req
     * @return
     */
    @RequestMapping(value = "/uploadWuliu",method = RequestMethod.POST)
    public @ResponseBody Map<String,Map<String,List<String>>> fieldMappingWuliu(
            @RequestParam("file") List<MultipartFile> file,HttpServletRequest req){
        // 创建一个路径
        String uploadPath = req.getSession().getServletContext().getRealPath("/") + "upload/temp/" + System.currentTimeMillis() + "/";
        String filePath = "";
        String fileName = "";
        File uploadFile = null;
        File uploadPathd = new File(uploadPath);
        if (!uploadPathd.exists()) {
            uploadPathd.mkdirs();
        }
        // 将文件上传到服务器
        for(int i=0;i<file.size();i++) {
            fileName = file.get(i).getOriginalFilename();
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
        req.getSession().setAttribute("wuliuPath",uploadPath);
        // 读取服务器的excel表
        Map<String,Map<String,List<String>>> excelMap = wjs.readExcel(uploadPath);
        if(excelMap!=null){
            return excelMap;
        }
        return null;
    }

    /**
     * 物流数据存入数据库
     * @param field
     * @return
     */
    @RequestMapping(value = "/uploadWuliuExcel",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public @ResponseBody String uploadWuliuExcel(@RequestBody List<List<String>> field, HttpSession session) {
        // 读取域中数据
        String path = (String) session.getAttribute("wuliuPath");
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        // 将数据插入数据库
        int sum = wjs.insertWuliu(path,field,aj.getId());
        // wuliu_relation表添加数据
        wjs.insertRelation(aj.getId());
        // wuliu_ship表添加数据
        wlsService.insertShip(aj.getId());
        // wuliu_sj表添加数据
        wlsjService.insertSj(aj.getId());
        // 删除文件
//        String uploadPath = session.getServletContext().getRealPath("/") + "upload/temp/";
        File uploadPathd = new File(path);
        wjs.deleteFile(uploadPathd);
        if(sum>0){
            return "200";
        } else {
            return "400";
        }
    }

    /**
     * 传销Excel字段与表映射
     * @param file
     * @param aj
     * @param req
     * @return
     */
    @RequestMapping(value = "/uploadPyramidSale", method = RequestMethod.POST)
    public @ResponseBody Map<String,Map<String,List<String>>> uploadPyramidSale(@RequestParam("file") List<MultipartFile> file, @RequestParam("aj") String aj,
                              HttpServletRequest req) {
        // 设置服务器文件上传路径
        String uploadPath = req.getSession().getServletContext().getRealPath("/") + "upload/temp/" + System.currentTimeMillis() + "/";
        String filePath = "";
        String fileName = "";
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
        req.getSession().setAttribute("path",uploadPath);
        // 读取服务器的excel表
        Map<String,Map<String,List<String>>> excelMap = pyramidSaleService.readExcel(uploadPath);
        if(excelMap!=null){
            return excelMap;
        }
        return null;
    }

    /**
     * 传销数据存入数据库
     * @param field
     * @return
     */
    @RequestMapping(value = "/uploadPsSheet",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public @ResponseBody String uploadPsSheet(@RequestBody List<List<String>> field, HttpSession session){
        // 读取域中数据
        String path = (String) session.getAttribute("path");
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        // 将数据插入数据库
        int sum = pyramidSaleService.insertPyramidSale(path,field,aj.getId());
        // 插入计算会员当前层级和推荐路径数据
//        int sum1 = pyramidSaleService.insertPsHierarchy(aj.getId());
        int sum1 = 1;
        // 删除文件
//        String uploadPath = session.getServletContext().getRealPath("/") + "upload/temp/";
        File uploadPathd = new File(path);
        pyramidSaleService.deleteFile(uploadPathd);
        if(sum>0 && sum1>0){
            return "200";
        }if(sum>0 && sum1==0){
            return "403";
        }else{
            return "404";
        }
    }

    /**
     * 支付宝数据导入
     * @param file
     * @param aj
     * @param req
     * @return
     */
    @RequestMapping(value = "/uploadZfbcsv",method = RequestMethod.POST,produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String uploadZfb(@RequestParam("file") List<MultipartFile> file, @RequestParam("aj") String aj,
                              HttpServletRequest req) {
        // 创建一个路径
        UserEntity user =(UserEntity) req.getSession().getAttribute("user");
        String uploadPath = req.getSession().getServletContext().getRealPath("/") + "upload/temp/" + System.currentTimeMillis() + "/";
        String filePath = "";
        String fileName = "";
        File uploadFile = null;
        File uploadPathd = new File(uploadPath);
        if (!uploadPathd.exists()) {
            uploadPathd.mkdirs();
        }
        // 将文件上传到服务器
        for(int i=0;i<file.size();i++) {
            fileName =System.currentTimeMillis()+file.get(i).getOriginalFilename();
            filePath = uploadPath + fileName;
            if(fileName.endsWith(".csv")){
                uploadFile = new File(filePath);
                try {
                    file.get(i).transferTo(uploadFile);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        int a=0;
        AjEntity aje = ajs.findByName(aj,user.getId()).get(0);
        if(uploadPathd.listFiles()!=null) {
            // Zfb表添加数据
            a = us.insertZfb(uploadPath, aje.getId());
            us.deleteAll(uploadPath);
            uploadPathd.delete();
        }
        req.getSession().setAttribute("aj",aje);
        if(a>0){
            return "";
        }else {
            return null;
        }
    }

    /**
     * 支付宝字段映射
     * @param file
     * @param req
     * @return
     */
    @RequestMapping(value = "/uploadZfbxlsx",method = RequestMethod.POST)
    public @ResponseBody Map<String,Map<String,List<String>>> fieldMappingZfb(
            @RequestParam("file") List<MultipartFile> file,HttpServletRequest req){
        // 创建一个路径
        String uploadPath = req.getSession().getServletContext().getRealPath("/") + "upload/temp/" + System.currentTimeMillis() + "/";
        String filePath = "";
        String fileName = "";
        File uploadFile = null;
        File uploadPathd = new File(uploadPath);
        if (!uploadPathd.exists()) {
            uploadPathd.mkdirs();
        }
        // 将文件上传到服务器
        for(int i=0;i<file.size();i++) {
            fileName = file.get(i).getOriginalFilename();
            filePath = uploadPath + fileName;
            if(filePath.endsWith(".xlsx") || filePath.endsWith(".xls")){
                uploadFile = new File(filePath);
                try {
                    file.get(i).transferTo(uploadFile);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        req.getSession().setAttribute("zfbPath",uploadPath);
        // 读取服务器的excel表
        Map<String,Map<String,List<String>>> excelMap = zfbZzmxService.readExcel(uploadPath);
        if(excelMap!=null){
            return excelMap;
        }
        return null;
    }

    /**
     * 支付宝数据存入数据库
     * @param field
     * @return
     */
    @RequestMapping(value = "/uploadZfbExcel",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public @ResponseBody String uploadZfbExcel(@RequestBody List<List<String>> field, HttpSession session) {
        // 读取域中数据
        String path = (String) session.getAttribute("zfbPath");
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        // 将数据插入数据库
        int sum = zfbZzmxService.insertZfb(path,field,aj.getId());
        // 删除文件
//        String uploadPath = session.getServletContext().getRealPath("/") + "upload/temp/";
        File uploadPathd = new File(path);
        zfbZzmxService.deleteFile(uploadPathd);
        if(sum>0){
            return "200";
        } else {
            return "400";
        }
    }
}

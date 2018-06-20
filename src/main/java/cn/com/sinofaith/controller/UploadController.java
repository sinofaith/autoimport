package cn.com.sinofaith.controller;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.CftZzxxEntity;
import cn.com.sinofaith.service.*;
import cn.com.sinofaith.util.DBUtil;
import cn.com.sinofaith.util.TimeFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

import java.util.List;

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
    private AjServices ajs;

    @RequestMapping(value = "/uploadFolder",method = RequestMethod.POST,produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String uploadFileFolder(@RequestParam("file") List<MultipartFile> file, @RequestParam("aj") String aj, HttpSession httpSession,HttpServletRequest request){
        String uploadPath = request.getSession().getServletContext().getRealPath("/")+"upload/temp/";
        String filePath ="";
        String fileName="";
        String result = "";
        File uploadFile = null;
        for(int i=0;i<file.size();i++){
            fileName =TimeFormatUtil.getDate("") +file.get(i).getOriginalFilename();
             filePath = uploadPath + fileName;
            if(!fileName.endsWith(".txt")){
                continue;
            }
            uploadFile = new File(filePath);
            try {
                file.get(i).transferTo(uploadFile);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        List<AjEntity> aje = ajs.findByName(aj);
        if(aje.size()==0){
            ajs.save(new AjEntity(0L,aj,TimeFormatUtil.getDate("/")));
            aje = ajs.findByName(aj);
        }

        int a = us.insertZcxx(uploadPath,"info.txt",aje.get(0).getId());
        int b = us.insertZzxx(uploadPath,"trades.txt",aje.get(0).getId());
        if(a+b>0){
            us.deleteAll(uploadPath);
        }
        List<CftZzxxEntity> listZzxx = zzs.getAll();

        int c = tjs.count(listZzxx,aje.get(0).getId());
        int d = tjss.count(listZzxx,aje.get(0).getId());


        if(a+b+c+d>0){
            result = String.valueOf(a);
        }else {
            result = "";
        }

        return result;
    }
}
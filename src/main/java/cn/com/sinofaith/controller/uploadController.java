package cn.com.sinofaith.controller;

import cn.com.sinofaith.bean.CftZzxxEntity;
import cn.com.sinofaith.service.CftTjjgService;
import cn.com.sinofaith.service.CftTjjgsService;
import cn.com.sinofaith.service.CftZzxxService;
import cn.com.sinofaith.service.uploadService;
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
public class uploadController {
    @Autowired
    private uploadService us;
    @Autowired
    private CftTjjgService tjs;
    @Autowired
    private CftTjjgsService tjss;
    @Autowired
    private CftZzxxService zzs;

    @RequestMapping(value = "/uploadFolder",method = RequestMethod.POST,produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String uploadFileFolder(@RequestParam("file") List<MultipartFile> file, HttpSession httpSession,HttpServletRequest request){
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
                System.out.println(fileName);
            }
        }
        int a = us.insertZcxx(uploadPath,"info.txt");
        int b = us.insertZzxx(uploadPath,"trades.txt");

        List<CftZzxxEntity> listZzxx = zzs.getAll();

        int c = tjs.count(listZzxx);
        int d = tjss.count(listZzxx);

        if(a+b+c+d>0){
            result = String.valueOf(a);
        }else {
            result = "";
        }

        return result;
    }
}
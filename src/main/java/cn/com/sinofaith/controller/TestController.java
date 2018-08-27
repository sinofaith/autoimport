package cn.com.sinofaith.controller;

import cn.com.sinofaith.bean.cftBean.CftZzxxEntity;
import cn.com.sinofaith.service.cftServices.CftZzxxService;
import cn.com.sinofaith.util.TimeFormatUtil;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TestController {


    @Autowired
    private CftZzxxService zzs;

    @RequestMapping(value = "/json",method = RequestMethod.POST,produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String a(){
        Gson gson = new Gson();
        List<CftZzxxEntity> listZzxx = null;
//        listZzxx = zzs.getAll().subList(0,10);
        Map<String,List<CftZzxxEntity>> map = new HashMap<>();
        map.put("aaa",listZzxx);
        return gson.toJson(map);
    }

    @RequestMapping(value = "/test")
    public ModelAndView aa(){
        ModelAndView mav = new ModelAndView("cft/test");
        return mav;
    }

    @RequestMapping(value = "/upload/progress",method = RequestMethod.POST,produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String uploadFileFolder(@RequestParam("file") List<MultipartFile> file, HttpSession httpSession, HttpServletRequest request){
        String uploadPath = request.getSession().getServletContext().getRealPath("/")+"upload/temp/";
        System.out.println(uploadPath);
        String filePath ="";
        String fileName="";
        String result = "";
        File uploadFile = null;

        for(int i=0;i<file.size();i++){
            fileName = TimeFormatUtil.getDate("") +file.get(i).getOriginalFilename();
            filePath = uploadPath + fileName;
            uploadFile = new File(filePath);
            try {
                file.get(i).transferTo(uploadFile);
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        return result;
    }
}

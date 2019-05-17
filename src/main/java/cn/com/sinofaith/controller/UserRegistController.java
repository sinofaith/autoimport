package cn.com.sinofaith.controller;

import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;

import static java.lang.Integer.parseInt;

@Controller
@RequestMapping("/userregist")
public class UserRegistController {

    @Autowired
    private UserServices us;

    @RequestMapping()
    public ModelAndView redirectAj(HttpSession httpSession) {
        ModelAndView mav = new ModelAndView("redirect:/userregist/seach?pageNo=1");
        httpSession.removeAttribute("urseachCondition"); //查询条件
        httpSession.removeAttribute("urseachCode");//查询内容
        return mav;
    }

    @RequestMapping(value = "/SeachCode" , method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView seachCode(String seachCode,String seachCondition,HttpSession httpSession){
        ModelAndView mav = new ModelAndView("redirect:/userregist/seach?pageNo=1");
        if(seachCode == null || seachCode.isEmpty()){
            httpSession.removeAttribute("urseachCode");
            httpSession.removeAttribute("urseachCondition");
            return mav;
        }

        httpSession.setAttribute("urseachCondition",seachCondition);
        httpSession.setAttribute("urseachCode",seachCode);
        return mav;
    }

    @RequestMapping(value = "/seach")
    public ModelAndView getAj(@RequestParam("pageNo") String pageNo, HttpServletRequest req){
        ModelAndView mav = new ModelAndView("/userregist");
        String seachCondition = (String) req.getSession().getAttribute("urseachCondition");
        String seach = "";
        String seachCode = (String) req.getSession().getAttribute("urseachCode");
        if(seachCode!=null){
            String temp  = seachCode.replace("，","")
                    .replace(" ","")
                    .replace(" ","")
                    .replace("\t","");
            temp = Arrays.asList(temp.split("\r\n")).toString().replace("[","").
                    replace(", ","%' or "+seachCondition+ " like '%").replace("]","");

            seach += " and "+ seachCondition+" like '%"+ temp +"%'";
        }else{
            seach += " and ( 1=1 ) ";
        }

        seach += " and zcpz = 0 ";

        Page page = us.queryForPage(parseInt(pageNo),10,seach);
        mav.addObject("page",page);
        mav.addObject("urseachCode",seachCode);
        mav.addObject("urseachCondition",seachCondition);
        mav.addObject("detailinfo",page.getList());
        return mav;
    }
}

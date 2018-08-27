package cn.com.sinofaith.controller;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.cftBean.CftZzxxEntity;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.*;
import cn.com.sinofaith.service.cftServices.CftTjjgService;
import cn.com.sinofaith.service.cftServices.CftTjjgsService;
import cn.com.sinofaith.util.TimeFormatUtil;
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
import java.util.List;

import static java.lang.Integer.parseInt;


@Controller
@RequestMapping("/aj")
public class AjController {
    @Autowired
    private AjServices ajs;
    @Autowired
    private CftTjjgService tjs;
    @Autowired
    private CftTjjgsService tjss;

    @RequestMapping()
    public ModelAndView redirectAj(HttpSession httpSession) {
        ModelAndView mav = new ModelAndView("redirect:/aj/seach?pageNo=1");
        httpSession.removeAttribute("ajseachCondition"); //查询条件
        httpSession.removeAttribute("ajseachCode");//查询内容
        return mav;
    }
    @RequestMapping(value = "/seach")
    public ModelAndView getAj(@RequestParam("pageNo") String pageNo, HttpServletRequest req){
        ModelAndView mav = new ModelAndView("aj/aj");

        String seachCondition = (String) req.getSession().getAttribute("ajseachCondition");
        String seach = "";
        String seachCode = (String) req.getSession().getAttribute("ajseachCode");
        if(seachCode!=null){
            String temp  = seachCode.replace("，","")
                    .replace(" ","")
                    .replace(" ","")
                    .replace("\t","");
            temp = Arrays.asList(temp.split("\r\n")).toString().replace("[","").
                    replace(", ","%' or "+seachCondition+ " like '%").replace("]","");
            seach = " and "+ seachCondition+" like "+"'%"+ temp +"%'";
        }else{
            seach = " and ( 1=1 ) ";
        }
        Page page = ajs.queryForPage(parseInt(pageNo),10,seach);
        mav.addObject("page",page);
        mav.addObject("ajseachCode",seachCode);
        mav.addObject("ajseachCondition",seachCondition);
        mav.addObject("detailinfo",page.getList());
        return mav;
    }

    @RequestMapping(value = "/SeachCode" , method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView seachCode(String seachCode,String seachCondition,HttpSession httpSession){
        ModelAndView mav = new ModelAndView("redirect:/aj/seach?pageNo=1");
        if(seachCode == null || seachCode.isEmpty()){
            httpSession.removeAttribute("ajseachCode");
            httpSession.removeAttribute("ajseachCondition");
            return mav;
        }

        httpSession.setAttribute("ajseachCondition",seachCondition);
        httpSession.setAttribute("ajseachCode",seachCode);
        return mav;
    }

    @RequestMapping(value = "/ajm")
    public ModelAndView jump(@RequestParam("aj") String aj,@RequestParam("type") long type, HttpSession httpSession){
        ModelAndView mav = null;
        if(type==1){
             mav = new ModelAndView("redirect:/cftzzxx/seach?pageNo=1");
        } else if(type==2){
            mav = new ModelAndView("redirect:/bankzzxx/seach?pageNo=1");
        } else{
            mav = new ModelAndView("redirect:/zfb/seach?pageNo=1");
        }

        httpSession.removeAttribute("zcseachCode");
        httpSession.removeAttribute("zcseachCondition");
        httpSession.setAttribute("aj",ajs.findByName(aj).get(0));
        return mav;
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST,produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String add(@RequestParam("aj") String aj){
        String result = "404";
        aj= aj.replace(",","");
        if(ajs.findByName(aj).size()>0){
            result = "303";
        }else {
            ajs.save(new AjEntity(0,aj, 0,TimeFormatUtil.getDate("/")));
            result = "200";
        }
        return result;
    }

    @RequestMapping(value = "/delete",method = RequestMethod.GET,produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String delete(@RequestParam("aj") String aj,HttpSession httpSession){
        AjEntity aje = ajs.findByName(aj).get(0);
        if(ajs.findByLike(aj).size()>0){
            return "303";
        }else{
            ajs.deleteByAj(aje.getId());
            httpSession.removeAttribute("aj");
            return "200";
        }
    }

    @RequestMapping(value = "/ajsCount",method = RequestMethod.GET,produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String ajsCount(@RequestParam("ajm") String ajm){
        List<AjEntity> ajlist = ajs.findByName(ajm);
        if(ajlist.size()<1) {
            ajs.save(new AjEntity(0, ajm,0, TimeFormatUtil.getDate("/")));
            AjEntity aje = ajs.findByName(ajm).get(0);
            List<CftZzxxEntity> listZz = ajs.getCftList(aje);
            tjs.count(listZz, aje.getId());
            tjss.count(listZz, aje.getId());
            return "200";
        }else{
            return "303";
        }
    }
    @RequestMapping(value = "/ajCount",method = RequestMethod.GET,produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String ajCount(@RequestParam("ajm") String ajm,@RequestParam("flg") int flg,HttpServletRequest req){
        AjEntity aje = ajs.findByName(ajm).get(0);
        if(aje.getFlg()!=flg) {
            aje.setFlg(flg);
            ajs.updateAj(aje);
            aje = ajs.findByName(ajm).get(0);
            req.getSession().setAttribute("aj",aje);
            List<CftZzxxEntity> listZz = ajs.getCftList(aje);
            tjs.count(listZz, aje.getId());
            tjss.count(listZz, aje.getId());
            return "200";
        }else {
            return "303";
        }
    }
}

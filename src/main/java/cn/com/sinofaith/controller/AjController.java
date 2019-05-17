package cn.com.sinofaith.controller;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.UserEntity;
import cn.com.sinofaith.bean.cftBean.CftZzxxEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZzmxEntity;
import cn.com.sinofaith.bean.zfbBean.ZfbZzmxTjjgsEntity;
import cn.com.sinofaith.form.zfbForm.ZfbZzmxTjjgsForm;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.*;
import cn.com.sinofaith.service.cftServices.CftTjjgService;
import cn.com.sinofaith.service.cftServices.CftTjjgsService;
import cn.com.sinofaith.util.TimeFormatUtil;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.text.html.HTML;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
        UserEntity user = (UserEntity) req.getSession().getAttribute("user");
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

            seach += " and "+ seachCondition+" like '%"+ temp +"%'";
        }else{
            seach += " and ( 1=1 ) ";
        }
        seach+= " and (zc.userid = "+user.getId()+" or zc.id in (select a.ajid from rel_grand_aj a where a.userid="+user.getId()+")) ";
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

    @RequestMapping(value = "/grandAj",method = RequestMethod.POST)
    @ResponseBody
    public String grandAj(@RequestParam("ajid") String ajid,@RequestParam(value = "listUserId",required = false) List<String> listUserId,HttpServletRequest request){
        UserEntity user =(UserEntity) request.getSession().getAttribute("user");
        ajs.grandAj(ajid,listUserId,user);
        return "";
    }

    @RequestMapping(value = "/ajm")
    public ModelAndView jump(@RequestParam("aj") String aj,@RequestParam("type") long type, HttpSession httpSession){
        UserEntity user = (UserEntity) httpSession.getAttribute("user");
        ModelAndView mav = null;
        if(type==1){
             mav = new ModelAndView("redirect:/cft/seach?pageNo=1");
        } else if(type==2){
            mav = new ModelAndView("redirect:/bank/seach?pageNo=1");
            httpSession.removeAttribute("bzzseachCondition"); //查询条件
            httpSession.removeAttribute("bzzseachCode");//查询内容

            httpSession.removeAttribute("bzorderby");
            httpSession.removeAttribute("bzlastOrder");
            httpSession.removeAttribute("bzdesc");
        } else if(type==3){
            mav = new ModelAndView("redirect:/wuliujjxx/seach?pageNo=1");
        } else if(type==4){
            mav = new ModelAndView("redirect:/pyramidSale/seach?pageNo=1");
        } else if(type==5){
            mav = new ModelAndView("redirect:/zfbZhmx?flag=a2");
        }

        httpSession.removeAttribute("zcseachCode");
        httpSession.removeAttribute("zcseachCondition");
        httpSession.setAttribute("aj",ajs.findByName(aj,user.getId()).get(0));
        return mav;
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST,produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String add(@RequestParam("aj") String aj,HttpServletRequest request){
        UserEntity user =(UserEntity) request.getSession().getAttribute("user");
        String result = "404";
        aj= aj.replace(",","");
        if(ajs.findByName(aj,user.getId()).size()>0){
            result = "303";
        }else {
            ajs.save(new AjEntity(0,aj, 1,"",TimeFormatUtil.getDate("/"),user.getId()));
            result = "200";
        }
        return result;
    }

    @RequestMapping(value = "/delete",method = RequestMethod.POST,produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String delete( String ajm,String list,HttpSession httpSession){
        UserEntity user = (UserEntity) httpSession.getAttribute("user");
        AjEntity aje = ajs.findByName(ajm,user.getId()).get(0);
        if(ajs.findByLike(ajm).size()>0){
            return "303";
        }else{
            String [] type = list.split(",");
            ajs.deleteByAj(aje.getId(),type);
            if(type.length==5) {
                httpSession.removeAttribute("aj");
            }
            return "200";
        }
    }

    @RequestMapping(value = "/ajsCount",method = RequestMethod.GET,produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String ajsCount(@RequestParam("ajm") String ajm, HttpServletRequest request){
        UserEntity user =(UserEntity) request.getSession().getAttribute("user");
        List<AjEntity> ajlist = ajs.findByName(ajm,user.getId());
        if(ajlist.size()<1) {
            ajs.save(new AjEntity(0, ajm,0,"", TimeFormatUtil.getDate("/"),user.getId()));
            AjEntity aje = ajs.findByName(ajm,user.getId()).get(0);
            List<CftZzxxEntity> listZz = ajs.getCftList(aje,user.getId());
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
        UserEntity user =(UserEntity) req.getSession().getAttribute("user");

        AjEntity aje = ajs.findByName(ajm,user.getId()).get(0);
        if(aje.getFlg()!=flg) {
            aje.setFlg(flg);
            ajs.updateAj(aje);
            aje = ajs.findByName(ajm,user.getId()).get(0);
            req.getSession().setAttribute("aj",aje);
            List<CftZzxxEntity> listZz = ajs.getCftList(aje,user.getId());
            tjs.countHb(listZz,aje.getId(),flg);
//            tjs.count(listZz, aje.getId());
//            tjss.count(listZz, aje.getId());
            return "200";
        }else {
            return "303";
        }
    }

    @RequestMapping(value = "/filterJyjlBySpmc",method = RequestMethod.POST,produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String filterJyjlBySpmc(String aj,String filterInput,HttpSession session){
        UserEntity user = (UserEntity) session.getAttribute("user");
        AjEntity aje = ajs.findByName(aj,user.getId()).get(0);
        // 是否与上次筛选一致
        if(filterInput.equals(aje.getFilter()) || (filterInput.equals("") && aje.getFilter()==null)){
            return "200";
        }else{
            int sum = ajs.getZfbZzmxList(aje,filterInput);
            if(sum>0){
                aje = ajs.findByName(aj,user.getId()).get(0);
                session.setAttribute("aj",aje);
                return "200";
            }else{
                return "500";
            }
        }
    }
}

package cn.com.sinofaith.controller.bankController;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.bankBean.BankTjjgEntity;
import cn.com.sinofaith.form.cftForm.CftTjjgForm;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.AjServices;
import cn.com.sinofaith.service.bankServices.BankTjjgServices;
import cn.com.sinofaith.service.bankServices.BankTjjgsService;
import cn.com.sinofaith.service.cftServices.CftTjjgsService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.xmlbeans.impl.xb.ltgfmt.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static java.lang.Integer.parseInt;

/**
 * Created by Me. on 2018/5/23
 */
@Controller
@RequestMapping("/banktjjgs")
public class BankTjjgsController {
    @Autowired
    private BankTjjgsService banktjss;
    @Autowired
    private BankTjjgServices tjs;
    @Autowired
    private AjServices ajs;

    @RequestMapping()
    public ModelAndView redirectBanknfo(HttpSession httpSession) {
        ModelAndView mav = new ModelAndView("redirect:/banktjjgs/seach?pageNo=1");
        //查询条件
        httpSession.removeAttribute("tjsseachCondition");
        //查询内容
        httpSession.removeAttribute("tjsseachCode");
        httpSession.setAttribute("code",-1);
        httpSession.setAttribute("hcode",0);

        httpSession.setAttribute("sorderby","jyzcs");
        httpSession.setAttribute("slastOrder","jyzcs");
        httpSession.setAttribute("sdesc"," desc ");
        return mav;
    }

    @RequestMapping(value = "/seachByUrl")
    public ModelAndView getByUrl(@RequestParam("yhkkh") String yhkkh,HttpSession ses){
        ModelAndView mav = new ModelAndView("redirect:/banktjjgs/seach?pageNo=1");
        ses.setAttribute("tjsseachCondition","jyzh");
        ses.setAttribute("tjsseachCode",yhkkh.replace("#",""));
        ses.setAttribute("code",-1);
        ses.setAttribute("hcode",0);
        ses.setAttribute("sorderby","jyzcs");
        ses.setAttribute("slastOrder","jyzcs");
        ses.setAttribute("sdesc"," desc ");
        return mav;
    }

    @RequestMapping(value = "/order")
    public ModelAndView order(@RequestParam("orderby") String orderby,HttpSession ses){
        ModelAndView mav = new ModelAndView("redirect:/banktjjgs/seach?pageNo=1");
        String desc = (String) ses.getAttribute("sdesc");
        String lastOrder = (String) ses.getAttribute("slastOrder");
        if(orderby.equals(lastOrder)){
            if(desc==null||" ,c.id ".equals(desc)){
                desc = " desc";
            }else{
                desc = " ,c.id ";
            }
        }else{
            desc = " desc ";
        }

        ses.setAttribute("sorderby",orderby);
        ses.setAttribute("slastOrder",orderby);
        ses.setAttribute("sdesc",desc);
        return mav;
    }
    @RequestMapping(value = "/seach")
    public ModelAndView getbanktj(@RequestParam("pageNo") String pageNo, HttpServletRequest req) {
        ModelAndView mav = new ModelAndView("bank/banktjjgs");
        String seachCondition = (String) req.getSession().getAttribute("tjsseachCondition");
        String seachCode = (String) req.getSession().getAttribute("tjsseachCode");
        String orderby = (String) req.getSession().getAttribute("sorderby");
        String desc = (String) req.getSession().getAttribute("sdesc");
        int code = (Integer) req.getSession().getAttribute("code");
        int hcode = (Integer) req.getSession().getAttribute("hcode");
        AjEntity aj = (AjEntity) req.getSession().getAttribute("aj");
        String seach = banktjss.getSeach(seachCondition,seachCode,orderby,desc,aj!=null?aj:new AjEntity(),code,hcode);
        Page page = banktjss.queryForPage(parseInt(pageNo),10,seach);
        mav.addObject("page",page);
        mav.addObject("tjsseachCode",seachCode);
        mav.addObject("tjsseachCondition",seachCondition);
        mav.addObject("detailinfo",page.getList());
        if(ajs.findByName(aj!=null ? aj.getAj():"").size()>0) {
            mav.addObject("aj", ajs.findByName(aj.getAj()).get(0));
        }
        mav.addObject("code",code);
        mav.addObject("hcode",hcode);
        return mav;
    }

    @RequestMapping(value = "/SeachCode" , method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView seachCode(String seachCode,String seachCondition,HttpSession httpSession){
        ModelAndView mav = new ModelAndView("redirect:/banktjjgs/seach?pageNo=1");
        if(seachCode == null || seachCode.isEmpty()){
            httpSession.removeAttribute("tjsseachCode");
            httpSession.removeAttribute("tjsseachCondition");
            return mav;
        }

        String seach = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
        httpSession.setAttribute("tjsseachCondition",seachCondition);
        httpSession.setAttribute("tjsseachCode",seachCode);
        return mav;
    }

    @RequestMapping(value = "/getByZhzt")
    public ModelAndView getByZhzt(int code ,HttpSession httpSession){
        ModelAndView mav = new ModelAndView("redirect:/banktjjgs/seach?pageNo=1");
        httpSession.setAttribute("code",code);
        return mav;
    }
    @RequestMapping(value = "/hiddenZfbCft")
    public ModelAndView hiddenZfbCft(int code ,HttpSession httpSession){
        ModelAndView mav = new ModelAndView("redirect:/banktjjgs/seach?pageNo=1");
        httpSession.setAttribute("hcode",code);
        return mav;
    }
    @RequestMapping("/download")
    public void getTjjgDownload(HttpServletResponse rep, HttpServletRequest req) throws Exception{
        String seachCondition = (String) req.getSession().getAttribute("tjsseachCondition");
        String seachCode = (String) req.getSession().getAttribute("tjsseachCode");
        String orderby = (String) req.getSession().getAttribute("sorderby");
        String desc = (String) req.getSession().getAttribute("sdesc");
        int code = (Integer) req.getSession().getAttribute("code");
        int hcode = (Integer) req.getSession().getAttribute("hcode");
        AjEntity aj = (AjEntity) req.getSession().getAttribute("aj");
        String seach = banktjss.getSeach(seachCondition,seachCode,orderby,desc,aj!=null?aj:new AjEntity(),code,hcode);
        banktjss.downloadFile(seach, rep,aj!=null?aj.getAj():"","对手",req);
    }

    @RequestMapping("/getSeach")
    @ResponseBody
    public String getSeach(String zh,String type,HttpServletResponse rep, HttpServletRequest req){
        Gson gson = new  GsonBuilder().serializeNulls().create();
        AjEntity aj = (AjEntity) req.getSession().getAttribute("aj");
        String seach = banktjss.getSeach(type,zh.replace("\n","").trim()," jyzcs "," desc ",aj,-1,0);
        CftTjjgForm bf = tjs.getByJyzh(zh.replace("\n","").trim(),aj);
        Page page = banktjss.queryForPage(1,10000,seach);
        page.setResult(bf);
        return gson.toJson(page);
    }
}
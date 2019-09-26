package cn.com.sinofaith.controller.bankController;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.UserEntity;
import cn.com.sinofaith.bean.bankBean.BankZzSeachEntity;
import cn.com.sinofaith.bean.bankBean.MappingBankzzxxEntity;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.bankServices.BankZzxxServices;
import cn.com.sinofaith.service.bankServices.MappingBankzzxxService;
import cn.com.sinofaith.service.cftServices.CftZzxxService;
import cn.com.sinofaith.util.MappingUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;

@Controller
@RequestMapping("/bankzzxx")
public class BankZzxxController {
    @Autowired
    private BankZzxxServices bankzzs;
    @Autowired
    private CftZzxxService cftzzs;
    @Autowired
    private MappingBankzzxxService mbs;

    @RequestMapping()
    public ModelAndView redirectCftinfo(HttpSession httpSession) {
        ModelAndView mav = new ModelAndView("redirect:/bankzzxx/seach?pageNo=1");
        httpSession.removeAttribute("seach"); //查询条件

        httpSession.removeAttribute("bzorderby");
        httpSession.removeAttribute("bzlastOrder");
        httpSession.removeAttribute("bzdesc");
        return mav;
    }

    @RequestMapping(value = "/seachByUrl")
    public ModelAndView getByUrl(@RequestParam("yhkkh") String yhkkh,@RequestParam("zhlx") long zhlx,HttpSession ses){
        ModelAndView mav = new ModelAndView("redirect:/bankzzxx/seach?pageNo=1");
        ses.setAttribute("bzzseachCondition","yhkkh");
        ses.setAttribute("bzzseachCode",yhkkh.replace("#",""));
        if(zhlx==1){
            ses.setAttribute("bzzseachCondition","dskh");
        }
        return mav;
    }

    @RequestMapping(value = "/seach")
    public ModelAndView getCftzzxx(@RequestParam("pageNo") String pageNo, HttpServletRequest req) {
        ModelAndView mav = new ModelAndView("bank/bankzzxx");
        BankZzSeachEntity seachE = (BankZzSeachEntity) req.getSession().getAttribute("seach");
        AjEntity aj = (AjEntity) req.getSession().getAttribute("aj");
        UserEntity user = (UserEntity) req.getSession().getAttribute("user");
        String orderby = (String) req.getSession().getAttribute("bzorderby");
        String desc = (String) req.getSession().getAttribute("bzdesc");
        String seach = bankzzs.getSeach(seachE,orderby,desc,aj!=null ? aj:new AjEntity(),user.getId());
        Page page = bankzzs.queryForPage(parseInt(pageNo),10,seach);
        mav.addObject("page",page);
        mav.addObject("detailinfo",page.getList());
        mav.addObject("aj",aj);
        return mav;
    }

    @RequestMapping(value = "/SeachCode" , method = RequestMethod.POST)
    @ResponseBody
    public void seachCode(@RequestBody BankZzSeachEntity yhkkh, HttpSession httpSession){
        ModelAndView mav = new ModelAndView("redirect:/bankzzxx/seach?pageNo=1");
        httpSession.setAttribute("seach",yhkkh);
//        return mav;
    }

    @RequestMapping(value = "/order")
    public ModelAndView order(@RequestParam("orderby") String orderby,HttpSession ses){
        ModelAndView mav = new ModelAndView("redirect:/bankzzxx/seach?pageNo=1");
        String desc = (String) ses.getAttribute("bzdesc");
        String lastOrder = (String) ses.getAttribute("bzlastOrder");
        if(orderby.equals(lastOrder)){
            if(desc==null||" ,c.id ".equals(desc)){
                desc = " desc ";
            }else{
                desc = " ,c.id ";
            }
        }else{
            desc = " desc ";
        }

        ses.setAttribute("bzorderby",orderby);
        ses.setAttribute("bzlastOrder",orderby);
        ses.setAttribute("bzdesc",desc);
        return mav;
    }

    @RequestMapping(value = "/download")
    public void getZzxxDownload(HttpServletResponse rep, HttpServletRequest req) throws Exception{
        BankZzSeachEntity seachCondition = (BankZzSeachEntity) req.getSession().getAttribute("seach");
        AjEntity aj = (AjEntity) req.getSession().getAttribute("aj");
        UserEntity user = (UserEntity) req.getSession().getAttribute("user");
        String orderby = (String) req.getSession().getAttribute("bzorderby");
        String desc = (String) req.getSession().getAttribute("bzdesc");
        String seach = bankzzs.getSeach(seachCondition,orderby,desc,aj!=null ? aj:new AjEntity(),user.getId());
        bankzzs.doloadFilezz(seach,rep,aj!=null?aj.getAj():"");
    }

    @RequestMapping(value = "/removeDesc",method = RequestMethod.GET,produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String removeDesc(HttpSession ses){
        // 读取域中数据
        String path = (String) ses.getAttribute("bankPath");
        if(path!=null){
            File uploadPathd = new File(path);
            MappingUtils.deleteFile(uploadPathd);
            ses.removeAttribute("bankPath");
        }
        ses.removeAttribute("xqdesc");
        return "200";
    }

    @RequestMapping(value = "/getYl",method = RequestMethod.POST,produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String getYl(){
        Gson gson = new GsonBuilder().serializeNulls().create();

        List<MappingBankzzxxEntity> zzFs = mbs.getAll();
        return gson.toJson(zzFs);
    }

    @RequestMapping(value = "/getDetails",method = RequestMethod.POST,produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String getDetails(@RequestParam("yhkkh") String yhkkh,@RequestParam("dfkh")String dfkh,
                             @RequestParam("type") String type,@RequestParam("sum") String sum,
                             @RequestParam("zhlx") String zhlx,@RequestParam("page")int page,
                             @RequestParam("order") String order, HttpServletRequest req,HttpSession ses){
        AjEntity aj = (AjEntity) req.getSession().getAttribute("aj");
        UserEntity user = (UserEntity) req.getSession().getAttribute("user");
        String desc = (String) ses.getAttribute("xqdesc");
        String lastOrder = (String) ses.getAttribute("xqlastOrder");
        String orders ="";
        if(!"xx".equals(order)) {
            if (order.equals(lastOrder)) {
                if (desc == null || " ,c.id ".equals(desc)) {
                    desc = " desc ";
                } else {
                    desc = " ,c.id ";
                }
            } else {
                desc = " desc ";
            }
             orders = " order by c." + order + desc+" nulls last ";
        }else{
            orders = " order by c."+lastOrder+desc+" nulls last";
            order=lastOrder;
        }
        ses.setAttribute("xqOrder",order);
        ses.setAttribute("xqlastOrder",order);
        ses.setAttribute("xqdesc",desc);
        return bankzzs.getByYhkkh(yhkkh.replace("\n","").trim(),
                dfkh.replace("\n","").trim(),type,sum,zhlx,aj!=null ? aj:new AjEntity(),page,orders,user.getId());
    }

    @RequestMapping(value = "/downDetailZh")
    public void downDetailZh(@RequestParam("yhkkh") String yhkkh,@RequestParam("dskh") String dskh,@RequestParam("zhlx") String zhlx,
                             HttpServletRequest req,HttpServletResponse rep,HttpSession ses)throws Exception{
        UserEntity user = (UserEntity) ses.getAttribute("user");
        dskh = dskh.replace("\n","").trim();
        yhkkh = yhkkh.replace("\n","").trim();
        AjEntity aj = (AjEntity) req.getSession().getAttribute("aj");
        String ajid=cftzzs.getAjidByAjm(aj,user.getId());
        String seach = "";
        String lastOrder = (String) ses.getAttribute("xqlastOrder");
        String desc = (String) ses.getAttribute("xqdesc");
        if(!"".equals(dskh)){
            seach += " and (c.yhkkh='"+yhkkh+"') and ( c.dskh='"+dskh+"' or c.bcsm = '"+dskh+"')";
        }else{
            if("0".equals(zhlx)){
                seach="and c.sfbz is not null and (c.yhkkh='"+yhkkh+"') ";
            }else{
                seach="and c.sfbz is not null and (c.dskh='"+yhkkh+"') ";
            }
        }
        //时间限制
        if(aj.getZjminsj().length()>1){
            seach+=" and c.jysj >= '"+aj.getZjminsj()+"' ";
        }
        if(aj.getZjmaxsj().length()>1){
            seach+=" and c.jysj <= '"+aj.getZjmaxsj()+"' ";
        }
        seach+=" and c.aj_id in("+ajid+") ";
        seach+=" order by c."+lastOrder+desc +" nulls last ";
        bankzzs.downloadFile(seach,rep, aj!=null?aj.getAj():"");
    }
}

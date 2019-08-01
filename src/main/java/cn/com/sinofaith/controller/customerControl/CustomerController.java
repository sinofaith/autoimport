package cn.com.sinofaith.controller.customerControl;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.UserEntity;
import cn.com.sinofaith.bean.customerProBean.PersonCompanyEntity;
import cn.com.sinofaith.bean.customerProBean.PersonNumberEntity;
import cn.com.sinofaith.bean.customerProBean.PersonRelationEntity;
import cn.com.sinofaith.form.customerForm.PersonRelationForm;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.customerServices.CustomerService;
import cn.com.sinofaith.service.customerServices.PersonCompanyService;
import cn.com.sinofaith.service.customerServices.PersonNumberService;
import cn.com.sinofaith.service.customerServices.PersonRelationService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;

@Controller
@RequestMapping("/customerPro")
public class CustomerController {
    @Autowired
    private CustomerService cs;
    @Autowired
    private PersonCompanyService pcs;
    @Autowired
    private PersonNumberService pns;
    @Autowired
    private PersonRelationService prs;

    @RequestMapping()
    public ModelAndView redirectCftinfo(HttpSession httpSession,@RequestParam("aj") String aj) {
        ModelAndView mav = new ModelAndView("redirect:/customerPro/seach?pageNo=1");
        //查询条件
        httpSession.removeAttribute("cpseachCondition");
        //查询内容
        httpSession.removeAttribute("cpseachCode");
//        if("".equals(aj)){
//            httpSession.removeAttribute("ajid");
//            httpSession.removeAttribute("aj");
//        }else{
//            httpSession.setAttribute("ajid",aj);
//        }
        return mav;
    }

    @RequestMapping(value = "/SeachCode" , method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView seachCode(String seachCode,String seachCondition,HttpSession httpSession){
        ModelAndView mav = new ModelAndView("redirect:/customerPro/seach?pageNo=1");
        if(seachCode == null || seachCode.isEmpty()){
            httpSession.removeAttribute("cpseachCondition");
            httpSession.removeAttribute("cpseachCode");
            return mav;
        }

        httpSession.setAttribute("cpseachCondition",seachCondition);
        httpSession.setAttribute("cpseachCode",seachCode);
        return mav;
    }

    @RequestMapping(value = "/seach")
    public ModelAndView getCustomer(@RequestParam("pageNo") String pageNo, HttpServletRequest req) {
        ModelAndView mav = new ModelAndView("customer/customerPro");
        String seachCondition = (String) req.getSession().getAttribute("cpseachCondition");
        String seachCode = (String) req.getSession().getAttribute("cpseachCode");
        UserEntity user = (UserEntity) req.getSession().getAttribute("user");
        Page page = new Page();
        AjEntity aje = (AjEntity) req.getSession().getAttribute("aj");
        if(aje!=null){
            String seach = cs.getSeachAj(seachCode,seachCondition,aje.getId());
            page = cs.queryForPageAj(parseInt(pageNo),10,seach);
        }else{
            String seach = cs.getSeach(seachCode,seachCondition);
            page = cs.queryForPage(parseInt(pageNo),10,seach);
        }
        mav.addObject("page",page);
        mav.addObject("cpseachCode",seachCode);
        mav.addObject("cpseachCondition",seachCondition);
        mav.addObject("detailinfo",page.getList());
        return mav;
    }

    @RequestMapping(value = "/getDetails",method = RequestMethod.POST,produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String getDetails(@RequestParam("name") String name, HttpServletRequest req,HttpSession ses){
        AjEntity aj = (AjEntity) req.getSession().getAttribute("aj");
        List<PersonCompanyEntity> listpce = pcs.getByName(name,aj.getId());
        List<PersonNumberEntity> listpne = pns.getByName(name,aj.getId());
        Gson gson = new GsonBuilder().serializeNulls().create();
        Map<String,List> map = new HashMap<>();
        map.put("personCompany",listpce);
        map.put("personNumber",listpne);
        return gson.toJson(map).replace("null","\"\"");
    }

    @RequestMapping(value = "/download")
    @ResponseBody
    public void download(HttpServletResponse rep, HttpServletRequest req) throws Exception{
        AjEntity aj = (AjEntity) req.getSession().getAttribute("aj");
        List<PersonCompanyEntity> listpce = pcs.getByaj(aj.getId());
        List<PersonNumberEntity> listpne = pns.getByaj(aj.getId());
        List<PersonRelationForm> listpre = prs.getByaj(aj.getId());
        cs.downloadFile(rep,listpce,listpne,listpre,aj);
    }

    @RequestMapping(value = "/addCompany",method = RequestMethod.POST)
    @ResponseBody
    public String addCompany(HttpServletRequest req,@RequestBody PersonCompanyEntity pce){
        AjEntity aje = (AjEntity) req.getSession().getAttribute("aj");
        pce.setAj_id(aje.getId());
        pcs.add(pce);
        return "200";
    }

    @RequestMapping(value = "/addNumbers",method = RequestMethod.POST)
    @ResponseBody
    public String addNumbers(HttpServletRequest req,@RequestBody PersonNumberEntity pne){
        AjEntity aje = (AjEntity) req.getSession().getAttribute("aj");
        pne.setAj_id(aje.getId());
        pns.add(pne);
        return "200";
    }

    @RequestMapping(value = "/getCompany")
    @ResponseBody
    public String getCompany(long id){
        Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(pcs.getById(id).get(0)).replace("null","\"\"");
    }

    @RequestMapping(value = "/getNumber")
    @ResponseBody
    public String getNumber(long id){
        Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(pns.getById(id).get(0)).replace("null","\"\"");
    }

    @RequestMapping(value = "/editCompany",method = RequestMethod.POST)
    @ResponseBody
    public String editCompany(HttpServletRequest req,@RequestBody PersonCompanyEntity pce){
        AjEntity aje = (AjEntity) req.getSession().getAttribute("aj");
        pce.setAj_id(aje.getId());
        pcs.edit(pce);
        return "200";
    }

    @RequestMapping(value = "/editNumbers",method = RequestMethod.POST)
    @ResponseBody
    public String editNumbers(HttpServletRequest req,@RequestBody PersonNumberEntity pne){
        AjEntity aje = (AjEntity) req.getSession().getAttribute("aj");
        pne.setAj_id(aje.getId());
        pns.edit(pne);
        return "200";
    }

    @RequestMapping(value = "/deleteCompany")
    @ResponseBody
    public String deleteCompany(long id){
        pcs.removeCompany(id);
        return "200";
    }

    @RequestMapping(value = "/deleteNumbers")
    @ResponseBody
    public String deleteNumbers(long id){
        pns.removeNumbers(id);
        return "200";
    }

}

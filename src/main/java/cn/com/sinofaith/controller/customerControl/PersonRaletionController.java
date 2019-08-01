package cn.com.sinofaith.controller.customerControl;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.customerProBean.PersonRelationEntity;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.customerServices.PersonRelationService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static java.lang.Integer.parseInt;

@Controller
@RequestMapping("/customerRelation")
public class PersonRaletionController {
    @Autowired
    private PersonRelationService prs;

    @RequestMapping()
    public ModelAndView redirectCftinfo(HttpSession httpSession) {
        ModelAndView mav = new ModelAndView("redirect:/customerRelation/seach?pageNo=1");
        //查询条件
        httpSession.removeAttribute("crseachCondition");
        //查询内容
        httpSession.removeAttribute("crseachCode");

        return mav;
    }

    @RequestMapping(value = "/SeachCode" , method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView seachCode(String seachCode,String seachCondition,HttpSession httpSession){
        ModelAndView mav = new ModelAndView("redirect:/customerRelation/seach?pageNo=1");
        if(seachCode == null || seachCode.isEmpty()){
            httpSession.removeAttribute("crseachCondition");
            httpSession.removeAttribute("crseachCode");
            return mav;
        }

        String seach = seachCode.replace("\r\n","").replace("，","").
                replace(" ","").replace(" ","").
                replace("\t","");
        httpSession.setAttribute("crseachCondition",seachCondition);
        httpSession.setAttribute("crseachCode",seachCode);
        return mav;
    }

    @RequestMapping(value = "/seach")
    public ModelAndView getCustomer(@RequestParam("pageNo") String pageNo, HttpServletRequest req) {
        ModelAndView mav = new ModelAndView("customer/customerRelation");
        String seachCondition = (String) req.getSession().getAttribute("crseachCondition");
        String seachCode = (String) req.getSession().getAttribute("crseachCode");
        Page page = new Page();
        AjEntity aje = (AjEntity) req.getSession().getAttribute("aj");
        String seach = prs.getSeach(seachCode,seachCondition);
        page = prs.queryForPage(parseInt(pageNo),10,seach,aje.getId());

        mav.addObject("page",page);
        mav.addObject("crseachCondition",seachCondition);
        mav.addObject("crseachCode",seachCode);
        mav.addObject("detailinfo",page.getList());
        return mav;
    }
    @RequestMapping(value = "/addRelation",method = RequestMethod.POST)
    @ResponseBody
    public String addRelation(HttpServletRequest req,@RequestBody PersonRelationEntity pre){
        AjEntity aje = (AjEntity) req.getSession().getAttribute("aj");
        pre.setAj_id(aje.getId());
        prs.addRelation(pre);
        return "200";
    }


    @RequestMapping(value = "/deleteRelation")
    @ResponseBody
    public String deleteRelation(long id){
        prs.removeRelation(id);
        return "200";
    }

    @RequestMapping(value = "/getRelation")
    @ResponseBody
    public String getRelation(HttpSession ses, long id){
        AjEntity aje = (AjEntity)ses.getAttribute("aj");
        Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(prs.getByaj(aje.getId(),id).get(0)).replace("null","\"\"");
    }
    @RequestMapping(value = "/editRelation",method = RequestMethod.POST)
    @ResponseBody
    public String editRelation(HttpServletRequest req,@RequestBody PersonRelationEntity pre){
        AjEntity aje = (AjEntity) req.getSession().getAttribute("aj");
        pre.setAj_id(aje.getId());
        prs.editRelation(pre);
        return "200";
    }

}

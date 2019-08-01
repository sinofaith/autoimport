package cn.com.sinofaith.controller.customerControl;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.UserEntity;
import cn.com.sinofaith.page.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static java.lang.Integer.parseInt;

@Controller
@RequestMapping("/customerRaletion")
public class CustomerRaletionController {
    @RequestMapping()
    public ModelAndView redirectCftinfo(HttpSession httpSession) {
        ModelAndView mav = new ModelAndView("redirect:/customerRelation/seach?pageNo=1");
        //查询条件
        httpSession.removeAttribute("cprseachCondition");
        //查询内容
        httpSession.removeAttribute("cprseachCode");

        return mav;
    }

    @RequestMapping(value = "/SeachCode" , method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView seachCode(String seachCode,String seachCondition,HttpSession httpSession){
        ModelAndView mav = new ModelAndView("redirect:/customerRelation/seach?pageNo=1");
        if(seachCode == null || seachCode.isEmpty()){
            httpSession.removeAttribute("cprseachCondition");
            httpSession.removeAttribute("cprseachCode");
            return mav;
        }

        String seach = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
        httpSession.setAttribute("cprseachCondition",seachCondition);
        httpSession.setAttribute("cprseachCode",seachCode);
        return mav;
    }

    @RequestMapping(value = "/seach")
    public ModelAndView getCustomer(@RequestParam("pageNo") String pageNo, HttpServletRequest req) {
        ModelAndView mav = new ModelAndView("customer/customerPro");
        String seachCondition = (String) req.getSession().getAttribute("cpseachCondition");
        String seachCode = (String) req.getSession().getAttribute("cpseachCode");
//        UserEntity user = (UserEntity) req.getSession().getAttribute("user");
        Page page = new Page();
//        String aj = (String) req.getSession().getAttribute("ajid");
        AjEntity aje = (AjEntity) req.getSession().getAttribute("aj");
//        String seach = cs.getSeachAj(seachCode,seachCondition,aje.getId());
//        page = cs.queryForPageAj(parseInt(pageNo),10,seach);
//        mav.addObject("ajid",aj);
//        mav.addObject(aj,aje);

        mav.addObject("page",page);
        mav.addObject("cprseachCondition",seachCondition);
        mav.addObject("cprseachCode",seachCode);
        mav.addObject("detailinfo",page.getList());
        return mav;
    }
}

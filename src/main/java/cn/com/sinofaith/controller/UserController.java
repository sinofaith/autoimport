package cn.com.sinofaith.controller;

import cn.com.sinofaith.bean.UserEntity;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.UserServices;
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

import static java.lang.Integer.parseInt;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServices us;

    @RequestMapping()
    public ModelAndView redirectAj(HttpSession httpSession) {
        ModelAndView mav = new ModelAndView("redirect:/user/seach?pageNo=1");
        httpSession.removeAttribute("useachCondition"); //查询条件
        httpSession.removeAttribute("useachCode");//查询内容
        return mav;
    }

    @RequestMapping(value = "/SeachCode" , method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView seachCode(String seachCode,String seachCondition,HttpSession httpSession){
        ModelAndView mav = new ModelAndView("redirect:/user/seach?pageNo=1");
        if(seachCode == null || seachCode.isEmpty()){
            httpSession.removeAttribute("useachCode");
            httpSession.removeAttribute("useachCondition");
            return mav;
        }

        httpSession.setAttribute("useachCondition",seachCondition);
        httpSession.setAttribute("useachCode",seachCode);
        return mav;
    }

    @RequestMapping(value = "/getGrandUser",method = RequestMethod.POST)
    @ResponseBody
    public String getUserGrand(@RequestParam String ajid){
        return us.getUserGrand(Long.parseLong(ajid));
    }

    @RequestMapping(value = "/checkUsername",method = RequestMethod.POST)
    @ResponseBody
    public int checkUsername(@RequestParam String username){
        return us.findUser(username);
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public String addUser(@RequestParam String name,@RequestParam String username,
                          @RequestParam String password,@RequestParam int role,@RequestParam long zcpz){
        if(us.findUser(username)>0){
            return "303";
        }
        UserEntity user = new UserEntity();
        user.setName(name);
        user.setUsername(username);
        user.setPassword(password);
        user.setInserttime(TimeFormatUtil.getDate("/"));
        user.setRole(role);
        user.setZcpz(zcpz);
        if(zcpz == 1){
            user.setLoginTime("2099/12/31");
        }else{
            user.setLoginTime(TimeFormatUtil.getDate("/").split(" ")[0]);
        }
        us.saveUser(user);
        return "200";
    }

    @RequestMapping(value = "/zcpz",method = RequestMethod.POST)
    @ResponseBody
    public String addUser(@RequestParam long userId,@RequestParam String loginTime){
        if(us.zcpz(userId,loginTime,1)>0){
            return "200";
        }
        return "404";
    }

    @RequestMapping(value = "/seach")
    public ModelAndView getAj(@RequestParam("pageNo") String pageNo, HttpServletRequest req){
        ModelAndView mav = new ModelAndView("/user");
        String seachCondition = (String) req.getSession().getAttribute("useachCondition");
        String seach = "";
        String seachCode = (String) req.getSession().getAttribute("useachCode");
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
        seach += " and zcpz = 1 ";
        Page page = us.queryForPage(parseInt(pageNo),10,seach);
        mav.addObject("page",page);
        mav.addObject("useachCode",seachCode);
        mav.addObject("useachCondition",seachCondition);
        mav.addObject("detailinfo",page.getList());
        return mav;
    }
}

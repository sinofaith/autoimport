package cn.com.sinofaith.controller;

import cn.com.sinofaith.bean.UserEntity;
import cn.com.sinofaith.service.UserServices;
import cn.com.sinofaith.util.TimeFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    @Autowired
    private UserServices us;

    @RequestMapping(value = "/homepage")
    public String redirectHome() {

        return "newlogin";
    }

    @RequestMapping(value = "/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "newlogin";
    }

    @RequestMapping(value = "/login")
    public ModelAndView validateLogin(@ModelAttribute("userForm") UserEntity userEntity, HttpSession session) {

        ModelAndView mav = new ModelAndView();
        UserEntity user = us.login(userEntity);
        if(user!=null){
            mav = new ModelAndView("redirect:/aj");
            session.setAttribute("user",user);
                if (user.getZcpz()==0){
                    mav = new ModelAndView("newlogin");
                    session.setAttribute("user",null);
                    mav.addObject("username",userEntity.getUsername());
                    mav.addObject("result", "请联系管理员批准登陆");
                } else if(TimeFormatUtil.getSy(user.getLoginTime())){
                    mav = new ModelAndView("newlogin");
                    session.setAttribute("user",null);
                    mav.addObject("username", userEntity.getUsername());
                    mav.addObject("result", "试用时间结束");
                }else {
                    return mav;
                }

        }else{
            mav = new ModelAndView("newlogin");
            mav.addObject("username",userEntity.getUsername());
            mav.addObject("result", "用户名或密码错误");
        }
        session.setAttribute("sFlg",1);
        return mav;
    }
}
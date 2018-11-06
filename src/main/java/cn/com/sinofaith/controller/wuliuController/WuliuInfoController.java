package cn.com.sinofaith.controller.wuliuController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/wuliu")
public class WuliuInfoController {

    @RequestMapping()
    public ModelAndView redirectWuliuInfo() {
        ModelAndView mav = new ModelAndView("redirect:/wuliujjxx");
        return mav;
    }
}

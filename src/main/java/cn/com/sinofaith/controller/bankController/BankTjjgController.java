package cn.com.sinofaith.controller.bankController;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.AjServices;
import cn.com.sinofaith.service.bankServices.BankTjjgServices;
import cn.com.sinofaith.service.bankServices.BankZzxxServices;
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

@Controller
@RequestMapping("/banktjjg")

public class BankTjjgController {
        @Autowired
        private BankTjjgServices tjs;
        @Autowired
        private BankZzxxServices zzs;
        @Autowired
        private AjServices ajs;

        @RequestMapping()
        public ModelAndView redirectCftinfo(HttpSession httpSession) {
            ModelAndView mav = new ModelAndView("redirect:/banktjjg/seach?pageNo=1");
            httpSession.removeAttribute("btjseachCondition"); //查询条件
            httpSession.removeAttribute("btjseachCode");//查询内容
            httpSession.setAttribute("borderby", "khxm");
            httpSession.setAttribute("blastOrder", "khxm");
            httpSession.setAttribute("bdesc", " desc ");
            return mav;
        }

        @RequestMapping(value = "/order")
        public ModelAndView order(@RequestParam("orderby") String orderby, HttpSession ses) {
            ModelAndView mav = new ModelAndView("redirect:/banktjjg/seach?pageNo=1");
            String desc = (String) ses.getAttribute("bdesc");
            String lastOrder = (String) ses.getAttribute("blastOrder");
            if (orderby.equals(lastOrder)) {
                if (desc == null || " ,c.id ".equals(desc)) {
                    desc = " desc";
                } else {
                    desc = " ,c.id ";
                }
            } else {
                desc = " desc ";
            }

            ses.setAttribute("borderby", orderby);
            ses.setAttribute("blastOrder", orderby);
            ses.setAttribute("bdesc", desc);
            return mav;
        }

        @RequestMapping(value = "/seach")
        public ModelAndView getcfttj(@RequestParam("pageNo") String pageNo, HttpServletRequest req) {
            ModelAndView mav = new ModelAndView("bank/banktjjg");
            String seachCondition = (String) req.getSession().getAttribute("btjseachCondition");
            String seach = "";
            String seachCode = (String) req.getSession().getAttribute("btjseachCode");
            String orderby = (String) req.getSession().getAttribute("borderby");
            String desc = (String) req.getSession().getAttribute("bdesc");
            AjEntity aj = (AjEntity) req.getSession().getAttribute("aj");
            seach = tjs.getSeach(seachCondition, seachCode, orderby, desc, aj != null ? aj : new AjEntity());
            Page page = tjs.queryForPage(parseInt(pageNo), 10, seach);
            mav.addObject("page", page);
            mav.addObject("btjseachCode", seachCode);
            mav.addObject("btjseachCondition", seachCondition);
            mav.addObject("detailinfo", page.getList());
            if (ajs.findByName(aj != null ? aj.getAj() : "").size() > 0) {
                mav.addObject("aj", ajs.findByName(aj.getAj()).get(0));
            }
            return mav;
        }

        @RequestMapping(value = "/SeachCode", method = RequestMethod.POST)
        @ResponseBody
        public ModelAndView seachCode(String seachCode, String seachCondition, HttpSession httpSession) {
            ModelAndView mav = new ModelAndView("redirect:/banktjjg/seach?pageNo=1");
            if (seachCode == null || seachCode.isEmpty()) {
                httpSession.removeAttribute("btjseachCode");
                httpSession.removeAttribute("btjseachCondition");
                return mav;
            }

            String seach = seachCode.replace("\r\n", "").replace("，", "").replace(" ", "").replace(" ", "").replace("\t", "");
            httpSession.setAttribute("btjseachCondition", seachCondition);
            httpSession.setAttribute("btjseachCode", seachCode);
            return mav;
        }

        @RequestMapping("/download")
        public void getTjjgDownload(HttpServletResponse rep, HttpServletRequest req) throws Exception {
            String seachCondition = (String) req.getSession().getAttribute("btjseachCondition");
            String seach = "";
            String seachCode = (String) req.getSession().getAttribute("btjseachCode");
            String orderby = (String) req.getSession().getAttribute("borderby");
            String desc = (String) req.getSession().getAttribute("bdesc");
            AjEntity aj = (AjEntity) req.getSession().getAttribute("aj");
            seach = tjs.getSeach(seachCondition, seachCode, orderby, desc, aj != null ? aj : new AjEntity());

            tjs.downloadFile(seach, rep, aj!=null?aj.getAj():"");
        }
}

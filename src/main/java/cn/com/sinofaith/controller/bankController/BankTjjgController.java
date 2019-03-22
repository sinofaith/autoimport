package cn.com.sinofaith.controller.bankController;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.AjServices;
import cn.com.sinofaith.service.UploadService;
import cn.com.sinofaith.service.bankServices.BankTjjgServices;
import cn.com.sinofaith.service.bankServices.BankZzxxServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
            httpSession.setAttribute("borderby", "jyzcs");
            httpSession.setAttribute("blastOrder", "jyzcs");
            httpSession.setAttribute("bdesc", " desc ");
            httpSession.setAttribute("code",-1);
            httpSession.setAttribute("hcode",0);
            return mav;
        }

        @RequestMapping(value = "/seachByUrl")
        public ModelAndView getByUrl(@RequestParam("yhkkh") String yhkkh,HttpSession ses){
            ModelAndView mav = new ModelAndView("redirect:/banktjjg/seach?pageNo=1");
            ses.setAttribute("btjseachCondition","jyzh");
            ses.setAttribute("btjseachCode",yhkkh.replace("#",""));
            ses.setAttribute("code",-1);
            ses.setAttribute("hcode",0);
            ses.setAttribute("borderby", "jyzcs");
            ses.setAttribute("blastOrder", "jyzcs");
            ses.setAttribute("bdesc", " desc ");
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
            int code = (Integer) req.getSession().getAttribute("code");
            int hcode = (Integer) req.getSession().getAttribute("hcode");
            seach = tjs.getSeach(seachCondition, seachCode, orderby, desc, aj != null ? aj : new AjEntity(),code,hcode);
            Page page = tjs.queryForPage(parseInt(pageNo), 10, seach);
            mav.addObject("page", page);
            mav.addObject("btjseachCode", seachCode);
            mav.addObject("btjseachCondition", seachCondition);
            mav.addObject("detailinfo", page.getList());
            if (ajs.findByName(aj != null ? aj.getAj() : "").size() > 0) {
                mav.addObject("aj", ajs.findByName(aj.getAj()).get(0));
            }
            mav.addObject("code",code);
            mav.addObject("hcode",hcode);
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

        @RequestMapping(value = "/getZhxx",method = RequestMethod.GET)
        @ResponseBody
        public String getZhxx(long czje,long jzje,int page,HttpServletRequest req){
            Gson gson = new GsonBuilder().serializeNulls().create();
            AjEntity aj = (AjEntity) req.getSession().getAttribute("aj");
            StringBuffer seach = new StringBuffer();
            seach.append(" and c.aj_id=").append(aj.getId());
            seach.append(" and (c.czzje >=").append(czje).append(" or c.jzzje >=").append(jzje).append(") ");
            seach.append(" and c.zhlb != '第三方账户'");
            seach.append(" order by c.jyzcs desc ");
            return gson.toJson(tjs.queryForPage(page,100,seach.toString()));
        }

        @RequestMapping(value = "/getByZhzt")
        public ModelAndView getByZhzt(int code ,HttpSession httpSession){
            ModelAndView mav = new ModelAndView("redirect:/banktjjg/seach?pageNo=1");
            httpSession.setAttribute("code",code);
            return mav;
        }

        @RequestMapping(value = "/hiddenZfbCft")
        public ModelAndView hiddenZfbCft(int code ,HttpSession httpSession){
            ModelAndView mav = new ModelAndView("redirect:/banktjjg/seach?pageNo=1");
            httpSession.setAttribute("hcode",code);
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
            int code = (Integer) req.getSession().getAttribute("code");
            int hcode = (Integer) req.getSession().getAttribute("hcode");
            seach = tjs.getSeach(seachCondition, seachCode, orderby, desc, aj != null ? aj : new AjEntity(),code,hcode);

            tjs.downloadFile(seach, rep, aj!=null?aj.getAj():"");
        }

        @RequestMapping("/downWs")
        public void downWs(HttpServletRequest req,long czje,long jzje,String wstitle,HttpServletResponse rep){
            String downPath = req.getSession().getServletContext().getRealPath("/");
            AjEntity aj = (AjEntity) req.getSession().getAttribute("aj");
            tjs.downWs(czje,jzje,wstitle,downPath+"download/",aj != null ? aj : new AjEntity(),rep);
        }
}

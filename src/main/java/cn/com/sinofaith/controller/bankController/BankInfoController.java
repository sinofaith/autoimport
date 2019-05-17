package cn.com.sinofaith.controller.bankController;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.UserEntity;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.bankServices.BankCustomerServices;
import cn.com.sinofaith.service.bankServices.BankTjjgServices;
import cn.com.sinofaith.service.bankServices.BankTjjgsService;
import cn.com.sinofaith.service.bankServices.BankZcxxServices;
import com.google.gson.Gson;
import com.itextpdf.text.Document;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cn.com.sinofaith.util.ZipFile.ZipFiles;
import static java.lang.Integer.parseInt;

@Controller
@RequestMapping("/bank")
public class BankInfoController {

    @Autowired
    private BankZcxxServices bankzcs;
    @Autowired
    private BankCustomerServices cs;
    @Autowired
    private BankTjjgServices tjs;
    @Autowired
    private BankTjjgsService banktjss;

    @RequestMapping()
    public ModelAndView redirectCftinfo(HttpSession httpSession) {
        ModelAndView mav = new ModelAndView("redirect:/bank/seach?pageNo=1");
        httpSession.removeAttribute("bzcseachCondition"); //查询条件
        httpSession.removeAttribute("bzcseachCode");//查询内容
        httpSession.setAttribute("bzcorderby", "kyye");
        httpSession.setAttribute("bzclastOrder", "kyye");
        httpSession.setAttribute("bzcdesc", " desc ");
        return mav;
    }

    @RequestMapping(value = "/order")
    public ModelAndView order(@RequestParam("orderby") String orderby, HttpSession ses) {
        ModelAndView mav = new ModelAndView("redirect:/bank/seach?pageNo=1");
        String desc = (String) ses.getAttribute("bzcdesc");
        String lastOrder = (String) ses.getAttribute("bzclastOrder");
        if (orderby.equals(lastOrder)) {
            if (desc == null || "".equals(desc)) {
                desc = " desc";
            } else {
                desc = "";
            }
        } else {
            desc = " desc ";
        }

        ses.setAttribute("bzcorderby", orderby);
        ses.setAttribute("bzclastOrder", orderby);
        ses.setAttribute("bzcdesc", desc);
        return mav;
    }

    @RequestMapping(value = "/seach")
    public ModelAndView getCftzcxx(@RequestParam("pageNo") String pageNo, HttpServletRequest req) {
        ModelAndView mav = new ModelAndView("bank/bankInfo");
        String orderby = (String) req.getSession().getAttribute("bzcorderby");
        String desc = (String) req.getSession().getAttribute("bzcdesc");
        String seachCondition = (String) req.getSession().getAttribute("bzcseachCondition");
        String seachCode = (String) req.getSession().getAttribute("bzcseachCode");
        AjEntity aj = (AjEntity) req.getSession().getAttribute("aj");
        UserEntity user = (UserEntity) req.getSession().getAttribute("user");
        String seach = bankzcs.getSeach(seachCode,seachCondition,orderby, desc,aj!=null? aj : new AjEntity(),user.getId());
        Page page = bankzcs.queryForPage(parseInt(pageNo),10,seach);
        mav.addObject("page",page);
        mav.addObject("bzcseachCode",seachCode);
        mav.addObject("bzcseachCondition",seachCondition);
        mav.addObject("detailinfo",page.getList());
        mav.addObject("aj",aj);
        return mav;
    }

    @RequestMapping(value = "/SeachCode" , method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView seachCode(String seachCode,String seachCondition,HttpSession httpSession){
        ModelAndView mav = new ModelAndView("redirect:/bank/seach?pageNo=1");
        if(seachCode == null || seachCode.isEmpty()){
            httpSession.removeAttribute("bzcseachCode");
            httpSession.removeAttribute("bzcseachCondition");
            return mav;
        }
        httpSession.setAttribute("bzcseachCondition",seachCondition);
        httpSession.setAttribute("bzcseachCode",seachCode);
        return mav;
    }

    @RequestMapping(value = "/download")
    public void getZcxxDownload(HttpServletResponse rep, HttpServletRequest req, HttpSession session) throws Exception{

        String orderby = (String) req.getSession().getAttribute("bzcorderby");
        String desc = (String) req.getSession().getAttribute("bzcdesc");
        String seachCondition = (String)session.getAttribute("bzcseachCondition");
        String seachCode = (String) req.getSession().getAttribute("bzcseachCode");
        AjEntity aj = (AjEntity) req.getSession().getAttribute("aj");
        UserEntity user = (UserEntity) req.getSession().getAttribute("user");
        String seach = bankzcs.getSeach(seachCode,seachCondition,orderby,desc,aj!=null? aj : new AjEntity(),user.getId());
        bankzcs.downloadFile(seach,rep,aj!=null?aj.getAj():"");
    }

    @RequestMapping(value =  "/getBq",method = RequestMethod.GET,produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String getBq(@RequestParam("yhkh") String yhkh,HttpSession ses){
        AjEntity aj = (AjEntity) ses.getAttribute("aj");

        return bankzcs.getBq(yhkh,aj.getId());
    }

    /**
     * 生成pdf文档
     * @param resp
     * @param session
     * @throws IOException
     */
    @RequestMapping("/createPDF")
    public void createPDF(HttpServletResponse resp,HttpSession session) throws IOException {
        // 取出域中对象
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        String fileName = "资金数据分析报告("+aj.getAj()+").pdf";
        resp.setContentType("application/force-download");
        resp.setHeader("Content-Disposition","attachment;filename="+new String((fileName).getBytes(), "ISO8859-1"));
        OutputStream op = resp.getOutputStream();
        Document doc = bankzcs.createPDF(op, aj);
        op.flush();
        op.close();
    }

    /**
     * 一键导出预览
     * @param currentPage
     * @param name
     * @param czje
     * @param jzje
     * @param session
     * @return
     */
    @RequestMapping("/previewTable")
    public @ResponseBody String previewTable(int currentPage, String name, String czje,String jzje,HttpSession session){
        Gson gson = new Gson();
        Page page = null;
        // 获取域中对象
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        if(aj==null){
            return "304";
        }
        String search = " and aj_id="+aj.getId();

        if(jzje!=null && !jzje.equals("")){
            search += " and c.jzzje > " + jzje;
        }
        if(czje!=null && !czje.equals("")){
            search += " and c.czzje > " + czje;
        }
        if(name.equals("bankTjjg")){
            search += " and (s.dsfzh =0 or s.dsfzh is null) order by c.jzzje desc,c.czzje desc";
            page = tjs.queryForPage(currentPage, 1000, search);
        }else if(name.equals("bankTjjgs")){
            search += " and (d.dsfzh =0 or d.dsfzh is null) order by c.jzzje desc,c.czzje desc";
            page = banktjss.queryForPage(currentPage, 1000, search);
        }else if(name.equals("bankGtzh")){
            search += " and (d.dsfzh =0 or d.dsfzh is null) order by a.num desc";
            page = banktjss.queryForPageGt(currentPage, 1000, search,aj.getId());
        }
        return gson.toJson(page);
    }

    /**
     * 批量导出  .zip
     * @param resp
     * @param session
     */
    @RequestMapping(value="/batchExport", method=RequestMethod.POST)
    public void batchExport(String yjdcList, HttpServletResponse resp, HttpSession session) throws Exception{
        Gson gson = new Gson();
        List<Map<String, String>> yjdcLists = gson.fromJson(yjdcList, List.class);
        String downPath = session.getServletContext().getRealPath("/");
        List<String> listPath = new ArrayList<>();
        // 1.获取域中对象
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        // 2.分别查出分析结果数据
        // 2.1 bankTjjg
        Map<String, String> bankMap = yjdcLists.get(0);
        String search = getSearch(bankMap, aj.getId());
        search += " and (s.dsfzh =0 or s.dsfzh is null) order by c.jzzje desc,c.czzje desc";
        List bankTjjg = tjs.getbankTjjgAll(search);
        if(bankTjjg.size()>0){
            HSSFWorkbook bankTjjgExcel = tjs.createExcel(bankTjjg);
            writerExcel(downPath, bankTjjgExcel,"账户统计信息("+aj.getAj()+")", listPath);
        }
        // 2.2 bankTjjgs
        bankMap = yjdcLists.get(1);
        search = getSearch(bankMap, aj.getId());
        search += " and (d.dsfzh = 0 or d.dsfzh is null) order by c.jzzje desc,c.czzje desc";
        List bankTjjgs = banktjss.getbankTjjgsAll(search);
        if(bankTjjgs.size()>0){
            HSSFWorkbook bankTjjgsExcel = banktjss.createExcel(bankTjjgs,"点对点");
            writerExcel(downPath, bankTjjgsExcel,"账户点对点统计信息("+aj.getAj()+")", listPath);
        }
        // 2.3 bankGtzh
        bankMap = yjdcLists.get(2);
        search = getSearch(bankMap, aj.getId());
        search += " and (d.dsfzh = 0 or d.dsfzh is null) order by a.num desc";
        List bankGtzh = banktjss.getbankGtzhAll(search, aj);
        if(bankTjjgs.size()>0){
            HSSFWorkbook bankGtzhExcel = banktjss.createExcel(bankGtzh,"共同");
            writerExcel(downPath, bankGtzhExcel,"公共账户统计信息("+aj.getAj()+")", listPath);
        }
        // 3. 打包
        File zip = new File(downPath+"upload/temp/bank/资金数据分析结果.zip");
        File srcfile[] = new File[listPath.size()];
        for (int j = 0, n1 = listPath.size(); j < n1; j++) {
            srcfile[j] = new File(listPath.get(j));
        }
        ZipFiles(srcfile, zip);
        try {
            resp.setContentType("application/zip");
            resp.setHeader("Location",zip.getName());
            resp.setHeader("Content-Disposition", "attachment; filename=" +
                    new String(("资金数据分析结果.zip").getBytes(), "ISO8859-1"));
            OutputStream outputStream = resp.getOutputStream();
            InputStream inputStream = new FileInputStream(downPath+"upload/temp/bank/资金数据分析结果.zip");
            byte[] buffer = new byte[1024];
            int i = -1;
            while ((i = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, i);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            File files = new File(downPath+"upload/temp/bank/");
            String[] filep = files.list();
            File temps = null;
            for (int a = 0; a < filep.length; a++) {
                temps = new File(downPath + "upload/temp/bank/" + filep[a]);
                if (temps.isFile()) {
                    temps.delete();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getSearch(Map<String, String> bankMap, long ajId){
        String search = " and aj_id=" + ajId;
        if (bankMap.get("jzje") != null && !bankMap.get("jzje").equals("")) {
            search += " and c.jzzje > " + bankMap.get("jzje");
        }
        if (bankMap.get("czje") != null && !bankMap.get("czje").equals("")) {
            search += " and c.czzje > " + bankMap.get("czje");
        }
        return search;
    }

    public static void writerExcel(String downPath, HSSFWorkbook excel, String excelName, List<String> listPath){
        try {
            // 判断是否存在文件夹
            File uploadPathd = new File(downPath+"upload/temp/bank");
            if(!uploadPathd.exists()){
                uploadPathd.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(downPath+"upload/temp/bank/"+excelName+".xls");
            excel.write(fos);
            fos.flush();
            fos.close();
            listPath.add(downPath+"upload/temp/bank/"+excelName+".xls");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

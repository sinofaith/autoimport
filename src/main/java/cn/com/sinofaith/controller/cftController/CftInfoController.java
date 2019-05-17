package cn.com.sinofaith.controller.cftController;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.UserEntity;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.cftServices.CftTjjgService;
import cn.com.sinofaith.service.cftServices.CftTjjgsService;
import cn.com.sinofaith.service.cftServices.CftZcxxService;
import com.google.gson.Gson;
import com.itextpdf.text.Document;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.criterion.DetachedCriteria;
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


/**
 * Created by Me. on 2018/5/21
 * 财付通注册信息
 */
@Controller
@RequestMapping("/cft")
public class CftInfoController {

    @Autowired
    private CftZcxxService cftzcs;
    @Autowired
    private CftTjjgService cfttjs;
    @Autowired
    private CftTjjgsService cfttjss;

    @RequestMapping()
    public ModelAndView redirectCftinfo(HttpSession httpSession) {
        ModelAndView mav = new ModelAndView("redirect:/cft/seach?pageNo=1");
        httpSession.removeAttribute("zcseachCondition"); //查询条件
        httpSession.removeAttribute("zcseachCode");//查询内容
        return mav;
    }

    @RequestMapping(value = "/seach")
    public ModelAndView getCftzcxx(@RequestParam("pageNo") String pageNo, HttpServletRequest req) {
        ModelAndView mav = new ModelAndView("cft/cftInfo");
        String seachCondition = (String) req.getSession().getAttribute("zcseachCondition");
        String seachCode = (String) req.getSession().getAttribute("zcseachCode");
        AjEntity aj = (AjEntity) req.getSession().getAttribute("aj");
        UserEntity user = (UserEntity) req.getSession().getAttribute("user");
        String seach = cftzcs.getSeach(seachCode,seachCondition,aj!=null? aj : new AjEntity(),user.getId());

        Page page = cftzcs.queryForPage(parseInt(pageNo),10,seach);
        mav.addObject("page",page);
        mav.addObject("zcseachCode",seachCode);
        mav.addObject("zcseachCondition",seachCondition);
        mav.addObject("detailinfo",page.getList());
        mav.addObject("aj",aj);
        return mav;
    }

    @RequestMapping(value = "/SeachCode" , method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView seachCode(String seachCode,String seachCondition,HttpSession httpSession){
        ModelAndView mav = new ModelAndView("redirect:/cft/seach?pageNo=1");
        if(seachCode == null || seachCode.isEmpty()){
            httpSession.removeAttribute("zcseachCode");
            httpSession.removeAttribute("zcseachCondition");
            return mav;
        }
        httpSession.setAttribute("zcseachCondition",seachCondition);
        httpSession.setAttribute("zcseachCode",seachCode);
        return mav;
    }

    @RequestMapping(value = "/download")
    public void getZcxxDownload(HttpServletResponse rep,HttpServletRequest req,HttpSession session) throws Exception{
        String seachCondition = (String)session.getAttribute("zcseachCondition");
        String seachCode = (String) req.getSession().getAttribute("zcseachCode");
        AjEntity aj = (AjEntity) req.getSession().getAttribute("aj");
        UserEntity user = (UserEntity) req.getSession().getAttribute("user");
        String seach = cftzcs.getSeach(seachCode,seachCondition,aj!=null? aj : new AjEntity(),user.getId());
        cftzcs.downloadFile(seach,rep,aj!=null?aj.getAj():"");
    }
    @RequestMapping(value =  "/getBq",method = RequestMethod.GET,produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String getBq(@RequestParam("wxzh") String wxzh,HttpSession ses){
        AjEntity aj = (AjEntity) ses.getAttribute("aj");

        return cftzcs.getBq(wxzh,aj.getId());
    }

    @RequestMapping("/createPDF")
    public void createPDF(HttpServletResponse resp,HttpSession session) throws IOException {
        // 取出域中对象
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        String fileName = "财付通分析报告("+aj.getAj()+").pdf";
        resp.setContentType("application/force-download");
        resp.setHeader("Content-Disposition","attachment;filename="+new String((fileName).getBytes(), "ISO8859-1"));
        OutputStream op = resp.getOutputStream();
        Document doc = cftzcs.createPDF(op, aj);
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
        if(name.equals("cftTjjg")){
            search += " order by c.jzzje desc,c.czzje desc";
            page = cfttjs.queryForPage(currentPage, 1000, search);
        }else if(name.equals("cftTjjgs")){
            search += " order by c.jzzje desc,c.czzje desc";
            page = cfttjss.queryForPage(currentPage, 1000, search);
        }else if(name.equals("cftGtzh")){
            search += " order by a.num desc";
            page = cfttjss.queryForPageGt(currentPage, 1000, search,aj.getId());
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
        // 2.1 cftTjjg
        Map<String, String> cftTjjgMap = yjdcLists.get(0);
        String search = " and aj_id=" + aj.getId();
        if (cftTjjgMap.get("jzje") != null && !cftTjjgMap.get("jzje").equals("")) {
            search += " and c.jzzje > " + cftTjjgMap.get("jzje");
        }
        if (cftTjjgMap.get("czje") != null && !cftTjjgMap.get("czje").equals("")) {
            search += " and c.czzje > " + cftTjjgMap.get("czje");
        }
        List listTjjg = cfttjs.getCftTjjgAll(search);
        if(listTjjg.size()>0){
            HSSFWorkbook cftTjjgExcel = cfttjs.createExcel(listTjjg);
            writerExcel(downPath, cftTjjgExcel,"财付通账户信息("+aj.getAj()+")", listPath);
        }
        // 2.2 cftTjjgs
        Map<String, String> cftTjjgsMap = yjdcLists.get(1);
        search = " and aj_id=" + aj.getId();
        if (cftTjjgsMap.get("jzje") != null && !cftTjjgsMap.get("jzje").equals("")) {
            search += " and c.jzzje > " + cftTjjgsMap.get("jzje");
        }
        if (cftTjjgsMap.get("czje") != null && !cftTjjgsMap.get("czje").equals("")) {
            search += " and c.czzje > " + cftTjjgsMap.get("czje");
        }
        List listTjjgs = cfttjss.getCftTjjgAll(search);
        if(listTjjgs.size()>0){
            HSSFWorkbook cftTjjgsExcel = cfttjss.createExcel(listTjjgs,"对手");
            writerExcel(downPath, cftTjjgsExcel,"财付通对手账户信息("+aj.getAj()+")", listPath);
        }
        // 2.3 cftGtzh
        Map<String, String> cftGtzhMap = yjdcLists.get(2);
        search = " and aj_id=" + aj.getId();
        if (cftGtzhMap.get("jzje") != null && !cftGtzhMap.get("jzje").equals("")) {
            search += " and c.jzzje > " + cftGtzhMap.get("jzje");
        }
        if (cftGtzhMap.get("czje") != null && !cftGtzhMap.get("czje").equals("")) {
            search += " and c.czzje > " + cftGtzhMap.get("czje");
        }
        List listGtzh = cfttjss.getCftTjjgsAll(search, aj);
        if(listTjjgs.size()>0){
            HSSFWorkbook cftGtzhExcel = cfttjss.createExcel(listGtzh,"共同");
            writerExcel(downPath, cftGtzhExcel,"财付通共同账户信息("+aj.getAj()+")", listPath);
        }
        // 3. 打包
        File zip = new File(downPath+"upload/temp/cft/财付通分析结果.zip");
        File srcfile[] = new File[listPath.size()];
        for (int j = 0, n1 = listPath.size(); j < n1; j++) {
            srcfile[j] = new File(listPath.get(j));
        }
        ZipFiles(srcfile, zip);
        try {
            resp.setContentType("application/zip");
            resp.setHeader("Location",zip.getName());
            resp.setHeader("Content-Disposition", "attachment; filename=" +new String(("财付通分析结果.zip").getBytes(), "ISO8859-1"));
            OutputStream outputStream = resp.getOutputStream();
            InputStream inputStream = new FileInputStream(downPath+"upload/temp/cft/财付通分析结果.zip");
            byte[] buffer = new byte[1024];
            int i = -1;
            while ((i = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, i);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            File files = new File(downPath+"upload/temp/cft/");
            String[] filep = files.list();
            File temps = null;
            for (int a = 0; a < filep.length; a++) {
                temps = new File(downPath + "upload/temp/cft/" + filep[a]);
                if (temps.isFile()) {
                    temps.delete();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void writerExcel(String downPath, HSSFWorkbook excel, String excelName, List<String> listPath){
        try {
            // 判断是否存在文件夹
            File uploadPathd = new File(downPath+"upload/temp/cft");
            if(!uploadPathd.exists()){
                uploadPathd.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(downPath+"upload/temp/cft/"+excelName+".xls");
            excel.write(fos);
            fos.flush();
            fos.close();
            listPath.add(downPath+"upload/temp/cft/"+excelName+".xls");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
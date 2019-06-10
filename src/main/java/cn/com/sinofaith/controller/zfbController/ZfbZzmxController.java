package cn.com.sinofaith.controller.zfbController;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.zfbBean.*;
import cn.com.sinofaith.form.zfbForm.ZfbJyjlTjjgForm;
import cn.com.sinofaith.form.zfbForm.ZfbZzmxGtzhForm;
import cn.com.sinofaith.page.Page;
import cn.com.sinofaith.service.zfbService.*;
import cn.com.sinofaith.util.ZipFile;
import com.google.gson.Gson;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.NullPrecedence;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cn.com.sinofaith.util.ZipFile.ZipFiles;

/**
 * 支付宝转账明细控制器
 * @author zd
 * create by 2018.11.30
 */
@Controller
@RequestMapping("/zfbZzmx")
public class ZfbZzmxController {
    @Autowired
    private ZfbZzmxService zfbZzmxService;
    @Autowired
    private ZfbZhmxJczzService zfbZhmxJczzService;
    @Autowired
    private ZfbZhmxTjjgService zfbZhmxTjjgService;
    @Autowired
    private ZfbZhmxTjjgsService zfbZhmxTjjgsService;
    @Autowired
    private ZfbZhmxJylxService zfbZhmxJylxService;
    @Autowired
    private ZfbZzmxTjjgService zfbZzmxTjjgService;
    @Autowired
    private ZfbZzmxTjjgsService zfbZzmxTjjgsService;
    @Autowired
    private ZfbZzmxGtzhService zfbZzmxGtzhService;
    @Autowired
    private ZfbJyjlTjjgService zfbJyjlTjjgService;
    @Autowired
    private ZfbJyjlTjjgsService zfbJyjlTjjgsService;
    @Autowired
    private ZfbJyjlSjdzsService zfbJyjlSjdzsService;


    @RequestMapping()
    public ModelAndView zfbZzmx(String flag, HttpSession session){
        ModelAndView mav = new ModelAndView("redirect:/zfbZzmx/seach?pageNo=1");
        session.removeAttribute("zzmxSeachCondition"); //查询条件
        session.removeAttribute("zzmxSeachCode");//查询内容
        session.removeAttribute("zzmxLastOrder");
        session.removeAttribute("zzmxDesc");
        session.setAttribute("flag",flag);
        return mav;
    }

    @RequestMapping("/seach")
    public String seach(int pageNo, String orderby, HttpSession session, Model model){
        // 创建离线查询对象
        DetachedCriteria dc = DetachedCriteria.forClass(ZfbZzmxEntity.class);
        // 取出域中对象
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        if(aj==null){
            return "/zfb/zfbZzmx";
        }
        dc.add(Restrictions.eq("aj_id",aj.getId()));
        String seachCondition = (String) session.getAttribute("zzmxSeachCondition");
        String seachCode = (String) session.getAttribute("zzmxSeachCode");
        String lastOrder = (String) session.getAttribute("zzmxLastOrder");
        String desc = (String) session.getAttribute("zzmxDesc");
        // 创建离线查询语句
        if(seachCode!=null){
            seachCode = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
            dc.add(Restrictions.like(seachCondition,"%"+seachCode+"%"));
        }
        if(orderby==null && desc==null){
            dc.addOrder(Order.desc("zzje").nulls(NullPrecedence.LAST));
        }
        if(orderby!=null){
            if(orderby.equals(lastOrder)){
                if(desc==null || desc.equals("desc")){
                    dc.addOrder(Order.asc(orderby));
                    desc = "";
                }else{
                    dc.addOrder(Order.desc(orderby).nulls(NullPrecedence.LAST));
                    desc = "desc";
                }
            }else{
                dc.addOrder(Order.asc(orderby));
                desc = "";
            }
        }else if("".equals(desc)){
            dc.addOrder(Order.asc(lastOrder));
        }else if("desc".equals(desc)){
            dc.addOrder(Order.desc(lastOrder).nulls(NullPrecedence.LAST));
        }
        // 封装分页数据
        // 获取分页数据
        Page page = zfbZzmxService.queryForPage(pageNo,10,dc);
        // 将数据存入request域中
        model.addAttribute("zzmxSeachCode", seachCode);
        model.addAttribute("zzmxSeachCondition", seachCondition);
        if(page!=null){
            model.addAttribute("page", page);
            model.addAttribute("detailinfo", page.getList());
        }
        if(orderby!=null){
            session.setAttribute("zzmxLastOrder",orderby);
        }
        session.setAttribute("zzmxOrder",orderby);
        session.setAttribute("zzmxDesc",desc);
        return "/zfb/zfbZzmx";
    }

    /**
     * 查询字段内容
     * @param seachCondition
     * @param seachCode
     * @param session
     * @return
     */
    @RequestMapping(value = "/SeachCode",method = RequestMethod.POST)
    public String SeachCode(String seachCondition, String seachCode, HttpSession session){
        // 若seachCode(查询内容)为空或为null
        if(seachCode==null || seachCode.trim().isEmpty()){
            session.removeAttribute("zzmxSeachCondition");
            session.removeAttribute("zzmxSeachCode");
            return "redirect:/zfbZzmx/seach?pageNo=1";
        }
        // 将查询字段与查询内容封装到session中
        session.setAttribute("zzmxSeachCondition",seachCondition);
        session.setAttribute("zzmxSeachCode",seachCode);
        return "redirect:/zfbZzmx/seach?pageNo=1";
    }

    @RequestMapping("/download")
    public void download(HttpServletResponse resp, HttpSession session) throws IOException {
        // 创建离线查询对象
        DetachedCriteria dc = DetachedCriteria.forClass(ZfbZzmxEntity.class);
        // 获得session中对象
        String seachCondition = (String) session.getAttribute("zzmxSeachCondition");
        String seachCode = (String) session.getAttribute("zzmxSeachCode");
        if(seachCode!=null){
            seachCode = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
            dc.add(Restrictions.like(seachCondition,"%"+seachCode+"%"));
        }
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        dc.add(Restrictions.eq("aj_id",aj.getId()));
        String lastOrder = (String) session.getAttribute("zzmxLastOrder");
        String desc = (String) session.getAttribute("zzmxDesc");
        if(lastOrder!=null && desc!=null){
            if(desc.equals("desc")){
                dc.addOrder(Order.desc(lastOrder).nulls(NullPrecedence.LAST));
                dc.addOrder(Order.desc("id").nulls(NullPrecedence.LAST));
            }else{
                dc.addOrder(Order.asc(lastOrder));
                dc.addOrder(Order.asc("id"));
            }
        }else{
            dc.addOrder(Order.desc("zzje").nulls(NullPrecedence.LAST));
            dc.addOrder(Order.desc("id").nulls(NullPrecedence.LAST));
        }
        // 获取所有数据数据
        List<ZfbZzmxEntity> zzmxs = zfbZzmxService.getZfbZzmxAll(dc);
        // 创建工作簿
        HSSFWorkbook wb = null;
        if(zzmxs!=null){
            wb = zfbZzmxService.createExcel(zzmxs);
        }
        resp.setContentType("application/force-download");
        resp.setHeader("Content-Disposition","attachment;filename="+new String(("支付宝转账明细(\""+aj.getAj()+").xls").getBytes(), "ISO8859-1"));
        OutputStream op = resp.getOutputStream();
        wb.write(op);
        op.flush();
        op.close();
    }

    /**
     * 一键筛选接口
     * @param currentPage
     * @param name
     * @param czje
     * @param jzje
     * @param session
     * @return
     */
    @RequestMapping(value = "/previewTable",method = RequestMethod.POST)
    public @ResponseBody String previewTable(int currentPage,String name,String czje,String jzje,HttpSession session){
        Gson gson = new Gson();
        Page page = null;
        // 获取域中对象
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        if(aj==null){
            return "304";
        }
        if(name.equals("zhmxJczz")){
            DetachedCriteria dc = DetachedCriteria.forClass(ZfbZhmxJczzEntity.class);
            getDetachedCriteria(dc,aj.getId(),czje,jzje,name);
            page = zfbZhmxJczzService.queryForPage(currentPage, 1000, dc);
        }else if(name.equals("zhmxTjjg")){
            DetachedCriteria dc = DetachedCriteria.forClass(ZfbZhmxTjjgEntity.class);
            getDetachedCriteria(dc,aj.getId(),czje,jzje,name);
            page = zfbZhmxTjjgService.queryForPage(currentPage, 1000, dc);
        }else if(name.equals("zhmxTjjgs")){
            DetachedCriteria dc = DetachedCriteria.forClass(ZfbZhmxTjjgsEntity.class);
            getDetachedCriteria(dc,aj.getId(),czje,jzje,name);
            page = zfbZhmxTjjgsService.queryForPage(currentPage, 1000, dc);
        }else if(name.equals("zhmxJylx")){
            DetachedCriteria dc = DetachedCriteria.forClass(ZfbZhmxJylxEntity.class);
            getDetachedCriteria(dc,aj.getId(),czje,jzje,name);
            page = zfbZhmxJylxService.queryForPage(currentPage, 1000, dc);
        }else if(name.equals("zzmxTjjg")){
            DetachedCriteria dc = DetachedCriteria.forClass(ZfbZzmxTjjgEntity.class);
            getDetachedCriteria(dc,aj.getId(),czje,jzje,name);
            page = zfbZzmxTjjgService.queryForPage(currentPage, 1000, dc);
        }else if(name.equals("zzmxTjjgs")){
            DetachedCriteria dc = DetachedCriteria.forClass(ZfbZzmxTjjgsEntity.class);
            getDetachedCriteria(dc,aj.getId(),czje,jzje,name);
            page = zfbZzmxTjjgsService.queryForPage(currentPage, 1000, dc);
        }else if(name.equals("zzmxGtzh")){
            String search = " where (1=1)";
            if(czje!=null && !czje.equals(""))
                search += " and fkzje >" + new BigDecimal(czje);
            if(jzje!=null && !jzje.equals(""))
                search += " and skzje >" + new BigDecimal(jzje);
            search += " order by gthys desc";
            page = zfbZzmxGtzhService.queryForPage(currentPage, 1000, search, aj.getId());
        }else if(name.equals("jyjlTjjg")){
            String search = "";
            if(czje!=null && !czje.equals(""))
                search += " and fkzje >" + new BigDecimal(czje);
            if(jzje!=null && !jzje.equals(""))
                search += " and skzje >" + new BigDecimal(jzje);
            search += " order by fkzje desc";
            page = zfbJyjlTjjgService.queryForPage(currentPage, 1000, search, aj.getId());
        }else if(name.equals("jyjlTjjgs")){
            DetachedCriteria dc = DetachedCriteria.forClass(ZfbJyjlTjjgsEntity.class);
            getDetachedCriteria(dc,aj.getId(),czje,"",name);
            page = zfbJyjlTjjgsService.queryForPage(currentPage, 1000, dc);
        }else if(name.equals("jyjlSjdzs")){
            DetachedCriteria dc = DetachedCriteria.forClass(ZfbJyjlSjdzsEntity.class);
            dc.addOrder(Order.desc("sjdzs"));
            Disjunction disjunction = Restrictions.disjunction();
            if(czje!=null && !czje.equals(""))
                disjunction.add(Restrictions.gt("czzje",new BigDecimal(czje)));
//            if(jzje!=null && !jzje.equals(""))
//                disjunction.add(Restrictions.gt("jzzje",new BigDecimal(jzje)));
            dc.add(disjunction);
            page = zfbJyjlSjdzsService.queryForPage(currentPage, 1000, dc);
        }
        String jsonList = gson.toJson(page);
        System.out.println(jsonList);
        return jsonList;
    }

    private static void getDetachedCriteria(DetachedCriteria dc, long id, String czje, String jzje, String name){
        dc.add(Restrictions.eq("aj_id",id));
        if(name.equals("zhmxJczz") || name.equals("zhmxTjjg")||name.equals("zhmxTjjgs")||name.equals("zhmxJylx")||name.equals("jyjlSjdzs")){
            dc.addOrder(Order.desc("czzje"));
            // 添加逻辑或的关系
            Disjunction disjunction = Restrictions.disjunction();
            if(czje!=null && !czje.equals(""))
                disjunction.add(Restrictions.gt("czzje",new BigDecimal(czje)));
            if(jzje!=null && !jzje.equals(""))
                disjunction.add(Restrictions.gt("jzzje",new BigDecimal(jzje)));
            dc.add(disjunction);
        }else{
            dc.addOrder(Order.desc("fkzje"));
            // 添加逻辑或的关系
            Disjunction disjunction = Restrictions.disjunction();
            if(czje!=null && !czje.equals(""))
                disjunction.add(Restrictions.gt("fkzje", new BigDecimal(czje)));
            if(jzje!=null && !jzje.equals(""))
                disjunction.add(Restrictions.gt("skzje",new BigDecimal(jzje)));
            dc.add(disjunction);
        }

    }

    /**
     * 批量导出  .zip
     * @param resp
     * @param session
     */
    @RequestMapping(value="/batchExport", method=RequestMethod.POST)
    public void batchExport(String yjdcList, HttpServletResponse resp, HttpSession session){
        Gson gson = new Gson();
        List<Map<String,String>> yjdcLists = gson.fromJson(yjdcList, List.class);
        String downPath = session.getServletContext().getRealPath("/");
        List<String> listPath = new ArrayList<>();
        DetachedCriteria dc = null;
        // 1.获取域中对象
        AjEntity aj = (AjEntity) session.getAttribute("aj");
        // 2.分别查出分析结果数据
        // 2.1 zhmxJczz
        Map<String, String> zhmxJczzMap = yjdcLists.get(0);
        dc = DetachedCriteria.forClass(ZfbZhmxJczzEntity.class);
        getDetachedCriteria(dc,aj.getId(),zhmxJczzMap.get("czje"),zhmxJczzMap.get("jzje"),zhmxJczzMap.get("field"));
        List<ZfbZhmxJczzEntity> zfbZhmxJczzAll = zfbZhmxJczzService.getZfbZhmxJczzAll(dc);
        if(zfbZhmxJczzAll.size()>0) {
            HSSFWorkbook zfbZhmxJczzExcel = zfbZhmxJczzService.createExcel(zfbZhmxJczzAll);
            writerExcel(downPath, zfbZhmxJczzExcel, "支付宝账户明细进出总账统计(" + aj.getAj() + ")", listPath);
        }
        // 2.2 zhmxTjjg
        Map<String, String> zhmxTjjgMap = yjdcLists.get(1);
        dc = DetachedCriteria.forClass(ZfbZhmxTjjgEntity.class);
        getDetachedCriteria(dc,aj.getId(),zhmxTjjgMap.get("czje"),zhmxTjjgMap.get("jzje"),zhmxTjjgMap.get("field"));
        List<ZfbZhmxTjjgEntity> zfbZhmxTjjgAll = zfbZhmxTjjgService.getZfbZhmxTjjgAll(dc);
        if(zfbZhmxTjjgAll.size()>0){
            HSSFWorkbook zfbZhmxTjjgExcel = zfbZhmxTjjgService.createExcel(zfbZhmxTjjgAll);
            writerExcel(downPath, zfbZhmxTjjgExcel,"账户明细账户与账户统计("+aj.getAj()+")", listPath);
        }
        // 2.3 zhmxTjjgs
        Map<String, String> zhmxTjjgsMap = yjdcLists.get(2);
        dc = DetachedCriteria.forClass(ZfbZhmxTjjgsEntity.class);
        getDetachedCriteria(dc,aj.getId(),zhmxTjjgsMap.get("czje"),zhmxTjjgsMap.get("jzje"),zhmxTjjgsMap.get("field"));
        List<ZfbZhmxTjjgsEntity> zfbZhmxTjjgsAll = zfbZhmxTjjgsService.getZfbZhmxTjjgsAll(dc);
        if(zfbZhmxTjjgsAll.size()>0){
            HSSFWorkbook zfbZhmxTjjgsExcel = zfbZhmxTjjgsService.createExcel(zfbZhmxTjjgsAll);
            writerExcel(downPath, zfbZhmxTjjgsExcel,"账户明细账户与银行卡统计("+aj.getAj()+")", listPath);
        }
        // 2.4 zhmxJylx
        Map<String, String> zhmxJylxMap = yjdcLists.get(3);
        dc = DetachedCriteria.forClass(ZfbZhmxJylxEntity.class);
        getDetachedCriteria(dc,aj.getId(),zhmxJylxMap.get("czje"),zhmxJylxMap.get("jzje"),zhmxJylxMap.get("field"));
        List<ZfbZhmxJylxEntity> zfbZhmxJylxAll = zfbZhmxJylxService.getZfbZhmxJylxAll(dc);
        if(zfbZhmxJylxAll.size()>0){
            HSSFWorkbook zfbZhmxJylxExcel = zfbZhmxJylxService.createExcel(zfbZhmxJylxAll);
            writerExcel(downPath, zfbZhmxJylxExcel,"账户明细账户交易类型统计("+aj.getAj()+")", listPath);
        }
        // 2.5 zzmxTjjg
        Map<String, String> zzmxTjjgMap = yjdcLists.get(4);
        dc = DetachedCriteria.forClass(ZfbZzmxTjjgEntity.class);
        getDetachedCriteria(dc,aj.getId(),zzmxTjjgMap.get("czje"),zzmxTjjgMap.get("jzje"),zzmxTjjgMap.get("field"));
        List<ZfbZzmxTjjgEntity> zfbZzmxTjjgAll = zfbZzmxTjjgService.getZfbZzmxTjjgAll(dc);
        if(zfbZzmxTjjgAll.size()>0){
            HSSFWorkbook zfbZzmxTjjgExcel = zfbZzmxTjjgService.createExcel(zfbZzmxTjjgAll);
            writerExcel(downPath, zfbZzmxTjjgExcel,"转账明细统计结果("+aj.getAj()+")", listPath);
        }
        // 2.6 zzmxTjjgs
        Map<String, String> zzmxTjjgsMap = yjdcLists.get(5);
        dc = DetachedCriteria.forClass(ZfbZzmxTjjgsEntity.class);
        getDetachedCriteria(dc,aj.getId(),zzmxTjjgsMap.get("czje"),zzmxTjjgsMap.get("jzje"),zzmxTjjgsMap.get("field"));
        List<ZfbZzmxTjjgsEntity> zfbZzmxTjjgsAll = zfbZzmxTjjgsService.getZfbZzmxTjjgAll(dc);
        if(zfbZzmxTjjgsAll.size()>0){
            HSSFWorkbook zfbZzmxTjjgsExcel = zfbZzmxTjjgsService.createExcel(zfbZzmxTjjgsAll);
            writerExcel(downPath, zfbZzmxTjjgsExcel,"转账明细对手账户("+aj.getAj()+")", listPath);
        }
        // 2.7 zzmxGtzh
        Map<String, String> zzmxGtzhMap = yjdcLists.get(6);
        String czje = zzmxGtzhMap.get("czje");
        String jzje = zzmxGtzhMap.get("jzje");
        String search = " where (1=1)";
        if(czje!=null && !czje.equals(""))
            search += " and fkzje >" + new BigDecimal(czje);
        if(jzje!=null && !jzje.equals(""))
            search += " and skzje >" + new BigDecimal(jzje);
        search += " order by gthys desc";
        List<ZfbZzmxGtzhForm> zfbZzmxGtzhAll = zfbZzmxGtzhService.getZfbZzmxGtzhAll(aj.getId(), search);
        if(zfbZzmxGtzhAll.size()>0){
            HSSFWorkbook zfbZzmxGtzhExcel = zfbZzmxGtzhService.createExcel(zfbZzmxGtzhAll);
            writerExcel(downPath, zfbZzmxGtzhExcel,"转账明细共同账户("+aj.getAj()+")", listPath);
        }
        // 2.8 jyjlTjjg
        Map<String, String> jyjlTjjgMap = yjdcLists.get(7);
        czje = jyjlTjjgMap.get("czje");
        jzje = jyjlTjjgMap.get("jzje");
        search = "";
        if(czje!=null && !czje.equals(""))
            search += " and fkzje >" + new BigDecimal(czje);
        if(jzje!=null && !jzje.equals(""))
            search += " and skzje >" + new BigDecimal(jzje);
        search += " order by fkzje desc";
        List<ZfbJyjlTjjgForm> zfbjyjlTjjgAll = zfbJyjlTjjgService.getZfbjyjlTjjgAll(search, aj.getId());
        if(zfbjyjlTjjgAll.size()>0){
            HSSFWorkbook zfbjyjlTjjgExcel = zfbJyjlTjjgService.createExcel(zfbjyjlTjjgAll);
            writerExcel(downPath, zfbjyjlTjjgExcel,"交易卖家账户信息("+aj.getAj()+")", listPath);
        }
        // 2.9 jyjlTjjgs
        Map<String, String> jyjlTjjgsMap = yjdcLists.get(8);
        dc = DetachedCriteria.forClass(ZfbJyjlTjjgsEntity.class);
        getDetachedCriteria(dc,aj.getId(),jyjlTjjgsMap.get("czje"),"",jyjlTjjgsMap.get("field"));
        List<ZfbJyjlTjjgsEntity> zfbJyjlTjjgsAll = zfbJyjlTjjgsService.getZfbjyjlTjjgAll(dc);
        if(zfbJyjlTjjgsAll.size()>0){
            HSSFWorkbook zfbjyjlTjjgsExcel = zfbJyjlTjjgsService.createExcel(zfbJyjlTjjgsAll);
            writerExcel(downPath, zfbjyjlTjjgsExcel,"交易买家账户信息("+aj.getAj()+")", listPath);
        }
        // 2.10 jyjlSjdzs
        Map<String, String> jyjlSjdzsMap = yjdcLists.get(9);
        czje = jyjlSjdzsMap.get("czje");
        jzje = jyjlSjdzsMap.get("jzje");
        dc = DetachedCriteria.forClass(ZfbJyjlSjdzsEntity.class);
        dc.addOrder(Order.desc("sjdzs"));
        Disjunction disjunction = Restrictions.disjunction();
        if(czje!=null && !czje.equals(""))
            disjunction.add(Restrictions.gt("czzje",new BigDecimal(czje)));
//        if(jzje!=null && !jzje.equals(""))
//            disjunction.add(Restrictions.gt("jzzje",new BigDecimal(jzje)));
        dc.add(disjunction);
        List<ZfbJyjlSjdzsEntity> zfbjyjlSjdzsAll = zfbJyjlSjdzsService.getZfbjyjlSjdzsAll(dc);
        if(zfbjyjlSjdzsAll.size()>0){
            HSSFWorkbook zfbjyjlSjdzsExcel = zfbJyjlSjdzsService.createExcel(zfbjyjlSjdzsAll);
            writerExcel(downPath, zfbjyjlSjdzsExcel,"交易记录地址统计("+aj.getAj()+")", listPath);
        }
        // 3. 打包
        File zip = new File(downPath+"upload/temp/zfb/支付宝分析结果.zip");
        File srcfile[] = new File[listPath.size()];
        for (int j = 0, n1 = listPath.size(); j < n1; j++) {
            srcfile[j] = new File(listPath.get(j));
        }
        ZipFiles(srcfile, zip);
        try {
            resp.setContentType("application/zip");
            resp.setHeader("Location",zip.getName());
            resp.setHeader("Content-Disposition", "attachment; filename=" +new String(("支付宝分析结果.zip").getBytes(), "ISO8859-1"));
            OutputStream outputStream = resp.getOutputStream();
            InputStream inputStream = new FileInputStream(downPath+"upload/temp/zfb/支付宝分析结果.zip");
            byte[] buffer = new byte[1024];
            int i = -1;
            while ((i = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, i);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            File files = new File(downPath+"upload/temp/zfb/");
            String[] filep = files.list();
            File temps = null;
            for (int a = 0; a < filep.length; a++) {
                temps = new File(downPath + "upload/temp/zfb/" + filep[a]);
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
            // 判断是否存在zfb文件夹
            File uploadPathd = new File(downPath+"upload/temp/zfb");
            if(!uploadPathd.exists()){
                uploadPathd.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(downPath+"upload/temp/zfb/"+excelName+".xls");
            excel.write(fos);
            fos.flush();
            fos.close();
            listPath.add(downPath+"upload/temp/zfb/"+excelName+".xls");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

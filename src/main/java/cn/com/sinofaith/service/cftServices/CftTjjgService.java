package cn.com.sinofaith.service.cftServices;

import cn.com.sinofaith.bean.AjEntity;
import cn.com.sinofaith.bean.cftBean.CftTjjgEntity;
import cn.com.sinofaith.bean.cftBean.CftTjjgsEntity;
import cn.com.sinofaith.bean.cftBean.CftZzxxEntity;
import cn.com.sinofaith.dao.cftDao.CftTjjgDao;
import cn.com.sinofaith.dao.cftDao.CftTjjgsDao;
import cn.com.sinofaith.form.cftForm.CftTjjgForm;
import cn.com.sinofaith.page.Page;

import cn.com.sinofaith.util.TimeFormatUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Me. on 2018/5/23
 */
@Service
public class CftTjjgService {
    @Autowired
    private CftTjjgDao cfttjd;
    @Autowired
    private CftTjjgsDao cfttjsd;

    public Page queryForPage(int currentPage, int pageSize, String seach){
        Page page = new Page();
        //总记录数
        int allRow = cfttjd.getAllRowCount(seach);
        List<CftTjjgForm> cfttjs = new ArrayList<CftTjjgForm>();
        CftTjjgForm tjjgForm = new CftTjjgForm();;
        List cftList = cfttjd.getDoPage(seach,currentPage,pageSize);
        if(allRow != 0) {
            int xh = 1;
            for(int i=0;i<cftList.size();i++){
                Map map = (Map)cftList.get(i);
                tjjgForm = tjjgForm.mapToForm(map);
                tjjgForm.setId(xh+(currentPage-1)*pageSize);

                cfttjs.add(tjjgForm);
                xh++;
            }

        }
        page.setList(cfttjs);
        page.setPageNo(currentPage);
        page.setPageSize(pageSize);
        page.setTotalRecords(allRow);
        return page;
    }

    public String getSeach(String seachCondition, String seachCode, String orderby, String desc, AjEntity aj){
        StringBuffer seach = new StringBuffer(" and aj_id ="+aj.getId());
        if(seachCode!=null){
            seachCode = seachCode.replace("\r\n","").replace("，","").replace(" ","").replace(" ","").replace("\t","");
            if("jzzje".equals(seachCondition)||"czzje".equals(seachCondition)){
                seach.append( " and c."+ seachCondition + seachCode.replace("大于等于",">=")
                        .replace("等于","=").replace("小于等于","<=")
                        .replace("大于",">").replace("小于","<"));
            }else if("xm".equals(seachCondition)){
                seach.append(" and s."+ seachCondition+" like "+"'%"+ seachCode+"%'");
            }else{
                seach.append(" and c."+ seachCondition+" like "+"'%"+ seachCode +"%'");
            }
        }else{
            seach.append(" and ( 1=1 ) ");
        }
        if(orderby!=null){
            if("xm".equals(orderby)){
                seach.append(" order by s." + orderby + desc +"  nulls last ,c.jylx");
            }else{
                seach.append(" order by c." +orderby + desc);
            }
        }
        return seach.toString();
    }

    public int countHb(List<CftZzxxEntity> listZzxx,long aj,int flg){
        List<CftTjjgEntity> listTjjg = cfttjd.find(" from CftTjjgEntity where aj_id = " + aj);
        List<CftTjjgsEntity> listTjjgs = cfttjd.find(" from CftTjjgsEntity where aj_id ="+aj);
        Map<String,CftTjjgEntity> map = listTjjg.stream().filter(c->c!=null).collect(Collectors.toMap(k->k.getJyzh()+k.getJylx(), cftTjjgEntity -> cftTjjgEntity));
        Map<String,CftTjjgsEntity> maps = listTjjgs.stream().filter(c->c!=null).collect(Collectors.toMap(k-> k.getJyzh()+k.getDfzh(),cftTjjgsEntity->cftTjjgsEntity));
        CftTjjgEntity tj = null;
        CftTjjgsEntity tjjgs = null;
        CftZzxxEntity zz = null;
        int temp = -1;
        if(flg<1){
            temp = 1;
        }
        for(int i=0; i<listZzxx.size();i++){
            zz = listZzxx.get(i);
            if(zz.getJylx().equals("微信提现手续费")){
                zz.setJylx("提现");
            }

            if("出".equals(zz.getJdlx())){
                if(!zz.getJylx().equals("转账")){
                    if(map.containsKey(zz.getZh()+zz.getJylx())){
                        tj = map.get(zz.getZh()+zz.getJylx());
                        tj.setJyzcs(tj.getJyzcs().add(new BigDecimal(1*temp)));
                        tj.setCzzcs(tj.getCzzcs().add(new BigDecimal(1*temp)));
                        tj.setCzzje(tj.getCzzje().add(zz.getJyje().multiply(BigDecimal.valueOf(temp))));
                        if(TimeFormatUtil.DateFormat(zz.getJysj())!=null){
                            if(tj.getMinsj()!=null&&TimeFormatUtil.DateFormat(zz.getJysj()).compareTo(TimeFormatUtil.DateFormat(tj.getMinsj()))==-1){
                                tj.setMinsj(zz.getJysj());
                            }else if(tj.getMaxsj()!=null&&TimeFormatUtil.DateFormat(zz.getJysj()).compareTo(TimeFormatUtil.DateFormat(tj.getMaxsj()))==1){
                                tj.setMaxsj(zz.getJysj());
                            }
                        }
                    }else{
                        CftTjjgEntity tj1 = new CftTjjgEntity();
                        tj1.setJyzh(zz.getZh());
                        tj1.setJylx(zz.getJylx());
                        tj1.setJyzcs(new BigDecimal(1));
                        tj1.setCzzcs(new BigDecimal(1));
                        tj1.setCzzje(zz.getJyje());
                        tj1.setAj_id(aj);
                        if (TimeFormatUtil.DateFormat(zz.getJysj()) != null) {
                            tj1.setMinsj(zz.getJysj());
                            tj1.setMaxsj(zz.getJysj());
                        }
                        map.put(zz.getZh() + zz.getJylx(), tj1);
                    }
                }
                if(zz.getJsf()!=null&&zz.getFsf()!=null){
                    if (map.containsKey(zz.getZh() + "转帐(有对手账户)")) {
                        tj = map.get(zz.getZh() + "转帐(有对手账户)");
                        tj.setJyzcs(tj.getJyzcs().add(new BigDecimal(1*temp)));
                        tj.setCzzcs(tj.getCzzcs().add(new BigDecimal(1*temp)));
                        tj.setCzzje(tj.getCzzje().add(zz.getJyje().multiply(BigDecimal.valueOf(temp))));
                        if(TimeFormatUtil.DateFormat(zz.getJysj())!=null){
                            if(tj.getMinsj()!=null&&TimeFormatUtil.DateFormat(zz.getJysj()).compareTo(TimeFormatUtil.DateFormat(tj.getMinsj()))==-1){
                                tj.setMinsj(zz.getJysj());
                            }else if(tj.getMaxsj()!=null&&TimeFormatUtil.DateFormat(zz.getJysj()).compareTo(TimeFormatUtil.DateFormat(tj.getMaxsj()))==1){
                                tj.setMaxsj(zz.getJysj());
                            }
                        }
                    } else {
                        CftTjjgEntity tj1 = new CftTjjgEntity();
                        tj1.setJyzh(zz.getZh());
                        tj1.setJylx("转帐(有对手账户)");
                        tj1.setJyzcs(new BigDecimal(1));
                        tj1.setCzzcs(new BigDecimal(1));
                        tj1.setCzzje(zz.getJyje());
                        tj1.setAj_id(aj);
                        if (TimeFormatUtil.DateFormat(zz.getJysj()) != null) {
                            tj1.setMinsj(zz.getJysj());
                            tj1.setMaxsj(zz.getJysj());
                        }
                        map.put(zz.getZh() + "转帐(有对手账户)", tj1);
                    }
                }else {
                    if (map.containsKey(zz.getZh() + "转帐(无对手账户)")) {
                        tj = map.get(zz.getZh() + "转帐(无对手账户)");
                        tj.setJyzcs(tj.getJyzcs().add(new BigDecimal(1*temp)));
                        tj.setCzzcs(tj.getCzzcs().add(new BigDecimal(1*temp)));
                        tj.setCzzje(tj.getCzzje().add(zz.getJyje().multiply(BigDecimal.valueOf(temp))));
                        if(TimeFormatUtil.DateFormat(zz.getJysj())!=null){
                            if(tj.getMinsj()!=null&&TimeFormatUtil.DateFormat(zz.getJysj()).compareTo(TimeFormatUtil.DateFormat(tjjgs.getMinsj()))==-1){
                                tj.setMinsj(zz.getJysj());
                            }else if(tj.getMaxsj()!=null&&TimeFormatUtil.DateFormat(zz.getJysj()).compareTo(TimeFormatUtil.DateFormat(tjjgs.getMaxsj()))==1){
                                tj.setMaxsj(zz.getJysj());
                            }
                        }
                    } else {
                        CftTjjgEntity tj1 = new CftTjjgEntity();
                        tj1.setJyzh(zz.getZh());
                        tj1.setJylx("转帐(无对手账户)");
                        tj1.setJyzcs(new BigDecimal(1));
                        tj1.setCzzcs(new BigDecimal(1));
                        tj1.setCzzje(zz.getJyje());
                        tj1.setAj_id(aj);
                        if(TimeFormatUtil.DateFormat(zz.getJysj())!=null){
                            tj1.setMinsj(zz.getJysj());
                            tj1.setMaxsj(zz.getJysj());
                        }
                        map.put(zz.getZh() + "转帐(无对手账户)", tj1);
                    }
                }
            }

            if("入".equals(zz.getJdlx())){
                if(!zz.getJylx().equals("转帐")) {
                    if (map.containsKey(zz.getZh() + zz.getJylx())) {
                        tj = map.get(zz.getZh() + zz.getJylx());
                        tj.setJyzcs(tj.getJyzcs().add(new BigDecimal(1*temp)));
                        tj.setJzzcs(tj.getJzzcs().add(new BigDecimal(1*temp)));
                        tj.setJzzje(tj.getJzzje().add(zz.getJyje().multiply(BigDecimal.valueOf(temp))));
                        if(TimeFormatUtil.DateFormat(zz.getJysj())!=null){
                            if(tj.getMinsj()!=null&&TimeFormatUtil.DateFormat(zz.getJysj()).compareTo(TimeFormatUtil.DateFormat(tj.getMinsj()))==-1){
                                tj.setMinsj(zz.getJysj());
                            }else if(tj.getMaxsj()!=null&&TimeFormatUtil.DateFormat(zz.getJysj()).compareTo(TimeFormatUtil.DateFormat(tj.getMaxsj()))==1){
                                tj.setMaxsj(zz.getJysj());
                            }
                        }
                    } else {
                        CftTjjgEntity tj2 = new CftTjjgEntity();
                        tj2.setJyzh(zz.getZh());
                        tj2.setJylx(zz.getJylx());
                        tj2.setJyzcs(new BigDecimal(1));
                        tj2.setJzzcs(new BigDecimal(1));
                        tj2.setJzzje(zz.getJyje());
                        tj2.setAj_id(aj);
                        if (TimeFormatUtil.DateFormat(zz.getJysj()) != null) {
                            tj2.setMinsj(zz.getJysj());
                            tj2.setMaxsj(zz.getJysj());
                        }
                        map.put(zz.getZh() + zz.getJylx(), tj2);
                    }
                }

                if(zz.getJsf()!=null&&zz.getFsf()!=null){
                    if (map.containsKey(zz.getZh() + "转帐(有对手账户)")) {
                        tj = map.get(zz.getZh() + "转帐(有对手账户)");
                        tj.setJyzcs(tj.getJyzcs().add(new BigDecimal(1*temp)));
                        tj.setJzzcs(tj.getJzzcs().add(new BigDecimal(1*temp)));
                        tj.setJzzje(tj.getJzzje().add(zz.getJyje().multiply(BigDecimal.valueOf(temp))));
                        if(TimeFormatUtil.DateFormat(zz.getJysj())!=null){
                            if(tj.getMinsj()!=null&&TimeFormatUtil.DateFormat(zz.getJysj()).compareTo(TimeFormatUtil.DateFormat(tj.getMinsj()))==-1){
                                tj.setMinsj(zz.getJysj());
                            }else if(tj.getMaxsj()!=null&&TimeFormatUtil.DateFormat(zz.getJysj()).compareTo(TimeFormatUtil.DateFormat(tj.getMaxsj()))==1){
                                tj.setMaxsj(zz.getJysj());
                            }
                        }
                    } else {
                        CftTjjgEntity tj2 = new CftTjjgEntity();
                        tj2.setJyzh(zz.getZh());
                        tj2.setJylx("转帐(有对手账户)");
                        tj2.setJyzcs(new BigDecimal(1));
                        tj2.setJzzcs(new BigDecimal(1));
                        tj2.setJzzje(zz.getJyje());
                        tj2.setAj_id(aj);
                        if (TimeFormatUtil.DateFormat(zz.getJysj()) != null) {
                            tj2.setMinsj(zz.getJysj());
                            tj2.setMaxsj(zz.getJysj());
                        }
                        map.put(zz.getZh() + "转帐(有对手账户)", tj2);
                    }
                }else {
                    if (map.containsKey(zz.getZh() + "转帐(无对手账户)")) {
                        tj = map.get(zz.getZh() + "转帐(无对手账户)");
                        tj.setJyzcs(tj.getJyzcs().add(new BigDecimal(1*temp)));
                        tj.setJzzcs(tj.getJzzcs().add(new BigDecimal(1*temp)));
                        tj.setJzzje(tj.getJzzje().add(zz.getJyje().multiply(BigDecimal.valueOf(temp))));
                        if(TimeFormatUtil.DateFormat(zz.getJysj())!=null){
                            if(tj.getMinsj()!=null&&TimeFormatUtil.DateFormat(zz.getJysj()).compareTo(TimeFormatUtil.DateFormat(tj.getMinsj()))==-1){
                                tj.setMinsj(zz.getJysj());
                            }else if(tj.getMaxsj()!=null&&TimeFormatUtil.DateFormat(zz.getJysj()).compareTo(TimeFormatUtil.DateFormat(tj.getMaxsj()))==1){
                                tj.setMaxsj(zz.getJysj());
                            }
                        }
                    } else {
                        CftTjjgEntity tj1 = new CftTjjgEntity();
                        tj1.setJyzh(zz.getZh());
                        tj1.setJylx("转帐(无对手账户)");
                        tj1.setJyzcs(new BigDecimal(1));
                        tj1.setJzzcs(new BigDecimal(1));
                        tj1.setJzzje(zz.getJyje());
                        tj1.setAj_id(aj);
                        if (TimeFormatUtil.DateFormat(zz.getJysj()) != null) {
                            tj1.setMinsj(zz.getJysj());
                            tj1.setMaxsj(zz.getJysj());
                        }
                        map.put(zz.getZh() + "转帐(无对手账户)", tj1);
                    }
                }
            }

            if (zz.getFsf()!=null && zz.getJsf()!=null) {
                if (zz.getZh().equals(zz.getFsf()) && "出".equals(zz.getJdlx())) {
                    if (maps.containsKey(zz.getZh() + zz.getJsf())) {
                        tjjgs = maps.get(zz.getZh() + zz.getJsf());
                        tjjgs.setJyzcs(tjjgs.getJyzcs().add(new BigDecimal(1*temp)));
                        tjjgs.setCzzcs(tjjgs.getCzzcs().add(new BigDecimal(1*temp)));
                        tjjgs.setCzzje(tjjgs.getCzzje().add(zz.getJyje().multiply(BigDecimal.valueOf(temp))));
                        if(TimeFormatUtil.DateFormat(zz.getJysj())!=null){
                            if(tjjgs.getMinsj()!=null&&TimeFormatUtil.DateFormat(zz.getJysj()).compareTo(TimeFormatUtil.DateFormat(tjjgs.getMinsj()))==-1){
                                tjjgs.setMinsj(zz.getJysj());
                            }else if(tjjgs.getMaxsj()!=null&&TimeFormatUtil.DateFormat(zz.getJysj()).compareTo(TimeFormatUtil.DateFormat(tjjgs.getMaxsj()))==1){
                                tjjgs.setMaxsj(zz.getJysj());
                            }
                        }
                    } else {
                        CftTjjgsEntity tjs1 = new CftTjjgsEntity();
                        tjs1.setJyzh(zz.getZh());
                        tjs1.setDfzh(zz.getJsf());
                        tjs1.setJyzcs(new BigDecimal(1));
                        tjs1.setCzzcs(new BigDecimal(1));
                        tjs1.setCzzje(zz.getJyje());
                        tjs1.setAj_id(aj);
                        if (TimeFormatUtil.DateFormat(zz.getJysj()) != null) {
                            tjs1.setMinsj(zz.getJysj());
                            tjs1.setMaxsj(zz.getJysj());
                        }
                        maps.put(zz.getZh() + zz.getJsf(), tjs1);
                    }
                }
                if (zz.getZh().equals(zz.getJsf()) && "入".equals(zz.getJdlx())) {
                    if (maps.containsKey(zz.getZh() + zz.getFsf())) {
                        tjjgs = maps.get(zz.getZh() + zz.getFsf());
                        tjjgs.setJyzcs(tjjgs.getJyzcs().add(new BigDecimal(1*temp)));
                        tjjgs.setJzzcs(tjjgs.getJzzcs().add(new BigDecimal(1*temp)));
                        tjjgs.setJzzje(tjjgs.getJzzje().add(zz.getJyje().multiply(BigDecimal.valueOf(temp))));
                        if(TimeFormatUtil.DateFormat(zz.getJysj())!=null){
                            if(tjjgs.getMinsj()!=null&&TimeFormatUtil.DateFormat(zz.getJysj()).compareTo(TimeFormatUtil.DateFormat(tjjgs.getMinsj()))==-1){
                                tjjgs.setMinsj(zz.getJysj());
                            }else if(tjjgs.getMaxsj()!=null&&TimeFormatUtil.DateFormat(zz.getJysj()).compareTo(TimeFormatUtil.DateFormat(tjjgs.getMaxsj()))==1){
                                tjjgs.setMaxsj(zz.getJysj());
                            }
                        }
                    } else {
                        CftTjjgsEntity tjs2 = new CftTjjgsEntity();
                        tjs2.setJyzh(zz.getZh());
                        tjs2.setDfzh(zz.getFsf());
                        tjs2.setJyzcs(new BigDecimal(1));
                        tjs2.setJzzcs(new BigDecimal(1));
                        tjs2.setJzzje(zz.getJyje());
                        tjs2.setAj_id(aj);
                        if (TimeFormatUtil.DateFormat(zz.getJysj()) != null) {
                            tjs2.setMinsj(zz.getJysj());
                            tjs2.setMaxsj(zz.getJysj());
                        }
                        maps.put(zz.getZh() + zz.getFsf(), tjs2);
                    }
                }
            }
        }
        listTjjg = new ArrayList<>(map.values());
        listTjjg = listTjjg.stream().filter(a ->a.getJyzcs().compareTo(BigDecimal.ZERO)==1).collect(Collectors.toList());
        listTjjgs = new ArrayList<>(maps.values());
        listTjjgs = listTjjgs.stream().filter(a->a.getJyzcs().compareTo(BigDecimal.ZERO)==1).collect(Collectors.toList());
        int i = 0;
        cfttjd.delAll(aj);
        cfttjd.save(listTjjg);
        cfttjsd.delAll(aj);
        cfttjsd.save(listTjjgs);
        return i;
    }

    public int count(List<CftZzxxEntity> listZzxx,long aj){
        List<CftTjjgEntity> listTjjg = null;
        Map<String,CftTjjgEntity> map = new HashMap();
        CftTjjgEntity tjjg = null;
        CftZzxxEntity zzxx = null;
        for(int i=0;i<listZzxx.size();i++){
            zzxx = listZzxx.get(i);
            if(zzxx.getJylx().equals("微信提现手续费")){
                zzxx.setJylx("提现");
            }

            if("出".equals(zzxx.getJdlx())){
                if(!zzxx.getJylx().equals("转帐")) {
                    if (map.containsKey(zzxx.getZh() + zzxx.getJylx())) {
                        tjjg = map.get(zzxx.getZh() + zzxx.getJylx());
                        tjjg.setJyzcs(tjjg.getJyzcs().add(new BigDecimal(1)));
                        tjjg.setCzzcs(tjjg.getCzzcs().add(new BigDecimal(1)));
                        tjjg.setCzzje(tjjg.getCzzje().add(zzxx.getJyje()));
                    } else {
                        CftTjjgEntity tj1 = new CftTjjgEntity();
                        tj1.setJyzh(zzxx.getZh());
                        tj1.setJylx(zzxx.getJylx());
                        tj1.setJyzcs(new BigDecimal(1));
                        tj1.setCzzcs(new BigDecimal(1));
                        tj1.setCzzje(zzxx.getJyje());
                        tj1.setAj_id(aj);
                        map.put(zzxx.getZh() + zzxx.getJylx(), tj1);
                    }
                }

                if(zzxx.getJsf()!=null&&zzxx.getFsf()!=null){
                    if (map.containsKey(zzxx.getZh() + "转帐(有对手账户)")) {
                        tjjg = map.get(zzxx.getZh() + "转帐(有对手账户)");
                        tjjg.setJyzcs(tjjg.getJyzcs().add(new BigDecimal(1)));
                        tjjg.setCzzcs(tjjg.getCzzcs().add(new BigDecimal(1)));
                        tjjg.setCzzje(tjjg.getCzzje().add(zzxx.getJyje()));
                    } else {
                        CftTjjgEntity tj1 = new CftTjjgEntity();
                        tj1.setJyzh(zzxx.getZh());
                        tj1.setJylx("转帐(有对手账户)");
                        tj1.setJyzcs(new BigDecimal(1));
                        tj1.setCzzcs(new BigDecimal(1));
                        tj1.setCzzje(zzxx.getJyje());
                        tj1.setAj_id(aj);
                        map.put(zzxx.getZh() + "转帐(有对手账户)", tj1);
                    }
                }else {
                    if (map.containsKey(zzxx.getZh() + "转帐(无对手账户)")) {
                        tjjg = map.get(zzxx.getZh() + "转帐(无对手账户)");
                        tjjg.setJyzcs(tjjg.getJyzcs().add(new BigDecimal(1)));
                        tjjg.setCzzcs(tjjg.getCzzcs().add(new BigDecimal(1)));
                        tjjg.setCzzje(tjjg.getCzzje().add(zzxx.getJyje()));
                    } else {
                        CftTjjgEntity tj1 = new CftTjjgEntity();
                        tj1.setJyzh(zzxx.getZh());
                        tj1.setJylx("转帐(无对手账户)");
                        tj1.setJyzcs(new BigDecimal(1));
                        tj1.setCzzcs(new BigDecimal(1));
                        tj1.setCzzje(zzxx.getJyje());
                        tj1.setAj_id(aj);
                        map.put(zzxx.getZh() + "转帐(无对手账户)", tj1);
                    }
                }

            }
            if("入".equals(zzxx.getJdlx())){
                if(!zzxx.getJylx().equals("转帐")) {
                    if (map.containsKey(zzxx.getZh() + zzxx.getJylx())) {
                        tjjg = map.get(zzxx.getZh() + zzxx.getJylx());
                        tjjg.setJyzcs(tjjg.getJyzcs().add(new BigDecimal(1)));
                        tjjg.setJzzcs(tjjg.getJzzcs().add(new BigDecimal(1)));
                        tjjg.setJzzje(tjjg.getJzzje().add(zzxx.getJyje()));
                    } else {
                        CftTjjgEntity tj2 = new CftTjjgEntity();
                        tj2.setJyzh(zzxx.getZh());
                        tj2.setJylx(zzxx.getJylx());
                        tj2.setJyzcs(new BigDecimal(1));
                        tj2.setJzzcs(new BigDecimal(1));
                        tj2.setJzzje(zzxx.getJyje());
                        tj2.setAj_id(aj);
                        map.put(zzxx.getZh() + zzxx.getJylx(), tj2);
                    }
                }

                if(zzxx.getJsf()!=null&&zzxx.getFsf()!=null){
                    if (map.containsKey(zzxx.getZh() + "转帐(有对手账户)")) {
                        tjjg = map.get(zzxx.getZh() + "转帐(有对手账户)");
                        tjjg.setJyzcs(tjjg.getJyzcs().add(new BigDecimal(1)));
                        tjjg.setJzzcs(tjjg.getJzzcs().add(new BigDecimal(1)));
                        tjjg.setJzzje(tjjg.getJzzje().add(zzxx.getJyje()));
                    } else {
                        CftTjjgEntity tj2 = new CftTjjgEntity();
                        tj2.setJyzh(zzxx.getZh());
                        tj2.setJylx("转帐(有对手账户)");
                        tj2.setJyzcs(new BigDecimal(1));
                        tj2.setJzzcs(new BigDecimal(1));
                        tj2.setJzzje(zzxx.getJyje());
                        tj2.setAj_id(aj);
                        map.put(zzxx.getZh() + "转帐(有对手账户)", tj2);
                    }
                }else {
                    if (map.containsKey(zzxx.getZh() + "转帐(无对手账户)")) {
                        tjjg = map.get(zzxx.getZh() + "转帐(无对手账户)");
                        tjjg.setJyzcs(tjjg.getJyzcs().add(new BigDecimal(1)));
                        tjjg.setJzzcs(tjjg.getJzzcs().add(new BigDecimal(1)));
                        tjjg.setJzzje(tjjg.getJzzje().add(zzxx.getJyje()));
                    } else {
                        CftTjjgEntity tj1 = new CftTjjgEntity();
                        tj1.setJyzh(zzxx.getZh());
                        tj1.setJylx("转帐(无对手账户)");
                        tj1.setJyzcs(new BigDecimal(1));
                        tj1.setJzzcs(new BigDecimal(1));
                        tj1.setJzzje(zzxx.getJyje());
                        tj1.setAj_id(aj);
                        map.put(zzxx.getZh() + "转帐(无对手账户)", tj1);
                    }
                }
            }

//            if(map.containsKey(zzxx.getZh()+zzxx.getJylx())){
//                if("出".equals(zzxx.getJdlx())){
//                    tjjg = map.get(zzxx.getZh()+zzxx.getJylx());
//                    tjjg.setJyzcs(tjjg.getJyzcs().add(new BigDecimal(1)));
//                    tjjg.setCzzcs(tjjg.getCzzcs().add(new BigDecimal(1)));
//                    tjjg.setCzzje(tjjg.getCzzje().add(zzxx.getJyje()));
//                }
//                if("入".equals(zzxx.getJdlx())){
//                    tjjg = map.get(zzxx.getZh()+zzxx.getJylx());
//                    tjjg.setJyzcs(tjjg.getJyzcs().add(new BigDecimal(1)));
//                    tjjg.setJzzcs(tjjg.getJzzcs().add(new BigDecimal(1)));
//                    tjjg.setJzzje(tjjg.getJzzje().add(zzxx.getJyje()));
//                }
//            }else{
//                if("出".equals(zzxx.getJdlx())){
//                    CftTjjgEntity tj1 = new CftTjjgEntity();
//                    tj1.setJyzh(zzxx.getZh());
//                    tj1.setJylx(zzxx.getJylx());
//                    tj1.setJyzcs(new BigDecimal(1));
//                    tj1.setCzzcs(new BigDecimal(1));
//                    tj1.setCzzje(zzxx.getJyje());
//                    tj1.setAj_id(aj);
//                    map.put(zzxx.getZh()+zzxx.getJylx(),tj1);
//                }
//                if("入".equals(zzxx.getJdlx())){
//                    CftTjjgEntity tj2 = new CftTjjgEntity();
//                    tj2.setJyzh(zzxx.getZh());
//                    tj2.setJylx(zzxx.getJylx());
//                    tj2.setJyzcs(new BigDecimal(1));
//                    tj2.setJzzcs(new BigDecimal(1));
//                    tj2.setJzzje(zzxx.getJyje());
//                    tj2.setAj_id(aj);
//                    map.put(zzxx.getZh()+zzxx.getJylx(),tj2);
//                }
//            }
        }
        listTjjg = new ArrayList<>(map.values());
        int i = 0;
        cfttjd.delAll(aj);
        cfttjd.save(listTjjg);

        return i;
    }

    public void downloadFile(String seach, HttpServletResponse rep,String aj) throws Exception{
        List listTjxx = cfttjd.findBySQL("select s.xm,c.* from cft_tjjg c left join cft_person s on c.jyzh = s.zh where 1=1 "+seach);
        HSSFWorkbook wb = createExcel(listTjxx);
        rep.setContentType("application/force-download");
        rep.setHeader("Content-disposition","attachment;filename="+new String(("财付通账户信息(\""+aj+").xls").getBytes(), "ISO8859-1"));
        OutputStream op = rep.getOutputStream();
        wb.write(op);
        op.flush();
        op.close();
    }

    public HSSFWorkbook createExcel(List listTjjg){
        HSSFWorkbook wb = new HSSFWorkbook();
        int b = 1;
        Sheet sheet = wb.createSheet("财付通账户信息");
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("序号");
        cell = row.createCell(1);
        cell.setCellValue("姓名");
        cell = row.createCell(2);
        cell.setCellValue("交易账户");
        cell = row.createCell(3);
        cell.setCellValue("交易类型");
        cell = row.createCell(4);
        cell.setCellValue("交易总次数");
        cell = row.createCell(5);
        cell.setCellValue("进账总次数");
        cell = row.createCell(6);
        cell.setCellValue("进账总金额(元)");
        cell = row.createCell(7);
        cell.setCellValue("出账总次数");
        cell = row.createCell(8);
        cell.setCellValue("出账总金额(元)");
        cell = row.createCell(9);
        cell.setCellValue("最早交易时间");
        cell = row.createCell(10);
        cell.setCellValue("最晚交易时间");
        cell = row.createCell(11);
        cell.setCellValue("间隔天数");
        for(int i=0;i<listTjjg.size();i++){
            if(i>=65535&& i%65535==0){
                sheet = wb.createSheet("财付通账户信息("+b+")");
                row = sheet.createRow(0);
                cell = row.createCell(0);
                cell.setCellValue("序号");
                cell = row.createCell(1);
                cell.setCellValue("姓名");
                cell = row.createCell(2);
                cell.setCellValue("交易账户");
                cell = row.createCell(3);
                cell.setCellValue("交易类型");
                cell = row.createCell(4);
                cell.setCellValue("交易总次数");
                cell = row.createCell(5);
                cell.setCellValue("进账总次数");
                cell = row.createCell(6);
                cell.setCellValue("进账总金额(元)");
                cell = row.createCell(7);
                cell.setCellValue("出账总次数");
                cell = row.createCell(8);
                cell.setCellValue("出账总金额(元)");
                cell = row.createCell(9);
                cell.setCellValue("最早交易时间");
                cell = row.createCell(10);
                cell.setCellValue("最晚交易时间");
                cell = row.createCell(11);
                cell.setCellValue("间隔天数");
                b+=1;
            }
            Map map = (Map)listTjjg.get(i);
            row = sheet.createRow(i%65535 + 1);
            cell = row.createCell(0);
            cell.setCellValue(i+1);
            cell = row.createCell(1);
            if(map.get("XM") != null && map.get("XM").toString().length()>0){
                cell.setCellValue(map.get("XM").toString());
            }
            cell = row.createCell(2);
            cell.setCellValue(map.get("JYZH").toString());
            cell = row.createCell(3);
            cell.setCellValue(map.get("JYLX").toString());
            cell = row.createCell(4);
            cell.setCellValue(map.get("JYZCS").toString());
            cell = row.createCell(5);
            cell.setCellValue(map.get("JZZCS").toString());
            cell = row.createCell(6);
            cell.setCellValue(map.get("JZZJE").toString());
            cell = row.createCell(7);
            cell.setCellValue(map.get("CZZCS").toString());
            cell = row.createCell(8);
            cell.setCellValue(map.get("CZZJE").toString());
            cell = row.createCell(9);
            cell.setCellValue(map.get("MINSJ").toString());
            cell = row.createCell(10);
            cell.setCellValue(map.get("MAXSJ").toString());
            cell = row.createCell(11);
            cell.setCellValue(TimeFormatUtil.sjjg(map.get("MAXSJ").toString(),map.get("MINSJ").toString()));
            if(i%65536==0) {
                for (int a = 0; a < 10; a++) {
                    sheet.autoSizeColumn(a);
                }
            }
        }


        return wb;
    }

    public void deleteByAjid(long id){
        cfttjd.delAll(id);
    }

    /**
     * 全部数据导出
     * @param search
     * @return
     */
    public List getCftTjjgAll(String search) {
        List listTjxx = cfttjd.findBySQL("select s.xm,c.* from cft_tjjg c left join cft_person s on c.jyzh = s.zh where 1=1 "+search);
        return listTjxx;
    }
}
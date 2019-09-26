package cn.com.sinofaith.bean.bankBean;

import cn.com.sinofaith.util.MappingUtils;
import cn.com.sinofaith.util.TimeFormatUtil;
import org.apache.poi.ss.usermodel.Row;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.prefs.NodeChangeEvent;

import static cn.com.sinofaith.service.bankServices.BankZzxxServices.isStartWithNumber;

@Entity
@Table(name = "bank_zzxx", schema = "", catalog = "")
public class BankZzxxEntity {
    private long id;
    private String yhkkh="";
    private String yhkzh;
    private String jysj;
    private BigDecimal jyje = new BigDecimal(0);
    private BigDecimal jyye = new BigDecimal(-1);
    private String sfbz;
    private String dskh;
    private String dszh;
    private String dsxm;
    private String dssfzh;
    private String dskhh;
    private String zysm;
    private String jysfcg;
    private String jywdmc;
    private BigDecimal dsjyye = new BigDecimal(-1);
    private BigDecimal dsye =new BigDecimal(-1);
    private String bz;
    private String jyzjh;
    private String jyxm;
    private String jyfsd;
    private long aj_id;
    private String bcsm;
    private String inserttime;

    public BankZzxxEntity() {
        super();
    }

    public BankZzxxEntity(long id, String yhkkh, String yhkzh, String jysj, BigDecimal jyje, BigDecimal jyye, String sfbz, String dskh, String dszh, String dsxm, String dssfzh, String dskhh, String zysm, String jysfcg, String jywdmc, BigDecimal dsjyye, BigDecimal dsye, String bz, String jyzjh,String jyxm, String jyfsd, long aj_id, String inserttime) {
        this.id = id;
        this.yhkkh = yhkkh;
        this.yhkzh = yhkzh;
        this.jysj = jysj;
        this.jyje = jyje;
        this.jyye = jyye;
        this.sfbz = sfbz;
        this.dskh = dskh;
        this.dszh = dszh;
        this.dsxm = dsxm;
        this.dssfzh = dssfzh;
        this.dskhh = dskhh;
        this.zysm = zysm;
        this.jysfcg = jysfcg;
        this.jywdmc = jywdmc;
        this.dsjyye = dsjyye;
        this.dsye = dsye;
        this.bz = bz;
        this.jyzjh = jyzjh;
        this.jyxm = jyxm;
        this.jyfsd = jyfsd;
        this.aj_id = aj_id;
        this.inserttime = inserttime;
    }

    @Id
    @Column(name = "id", nullable = false, precision = 0)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "yhkkh", length = 100)
    public String getYhkkh() {
        return yhkkh;
    }

    public void setYhkkh(String yhkkh) {
        this.yhkkh = yhkkh;
    }

    @Basic
    @Column(name = "yhkzh", length = 100)
    public String getYhkzh() {
        return yhkzh;
    }

    public void setYhkzh(String yhkzh) {
        this.yhkzh = yhkzh;
    }

    @Basic
    @Column(name = "jysj", length = 50)
    public String getJysj() {
        return jysj;
    }

    public void setJysj(String jysj) {
        if(jysj!=null&&jysj.indexOf("/")>0) {
            jysj= TimeFormatUtil.getDateSwitchTimestamp(jysj);
        }
        this.jysj = jysj;
    }

    @Basic
    @Column(name = "jyje", precision = 2)
    public BigDecimal getJyje() {
        return jyje;
    }

    public void setJyje(BigDecimal jyje) {
        this.jyje = jyje;
    }

    @Basic
    @Column(name = "jyye", precision = 2)
    public BigDecimal getJyye() {
        return jyye;
    }

    public void setJyye(BigDecimal jyye) {
        this.jyye = jyye;
    }

    @Basic
    @Column(name = "sfbz", length = 10)
    public String getSfbz() {
        return sfbz;
    }

    public void setSfbz(String sfbz) {
        this.sfbz = sfbz;
    }

    @Basic
    @Column(name = "dszh", length = 100)
    public String getDszh() {
        return dszh;
    }

    public void setDszh(String dszh) {
        this.dszh = dszh;
    }

    @Basic
    @Column(name = "dskh", length = 200)
    public String getDskh() {
        return dskh;
    }

    public void setDskh(String dskh) {
        this.dskh = dskh;
    }

    @Basic
    @Column(name = "dsxm", length = 100)
    public String getDsxm() {
        return dsxm;
    }

    public void setDsxm(String dsxm) {
        this.dsxm = dsxm;
    }

    @Basic
    @Column(name = "dssfzh", length = 50)
    public String getDssfzh() {
        return dssfzh;
    }

    public void setDssfzh(String dssfzh) {
        this.dssfzh = dssfzh;
    }

    @Basic
    @Column(name = "dskhh", length = 100)
    public String getDskhh() {
        return dskhh;
    }

    public void setDskhh(String dskhh) {
        this.dskhh = dskhh;
    }

    @Basic
    @Column(name = "zysm", length = 200)
    public String getZysm() {
        return zysm;
    }

    public void setZysm(String zysm) {
        this.zysm = zysm;
    }

    @Basic
    @Column(name = "jysfcg", length = 50)
    public String getJysfcg() {
        return jysfcg;
    }

    public void setJysfcg(String jysfcg) {
        this.jysfcg = jysfcg;
    }

    @Basic
    @Column(name = "jywdmc", length = 200)
    public String getJywdmc() {
        return jywdmc;
    }

    public void setJywdmc(String jywdmc) {
        this.jywdmc = jywdmc;
    }

    @Basic
    @Column(name = "dsjyye", precision = 2)
    public BigDecimal getDsjyye() {
        return dsjyye;
    }

    public void setDsjyye(BigDecimal dsjyye) {
        this.dsjyye = dsjyye;
    }

    @Basic
    @Column(name = "dsye", precision = 2)
    public BigDecimal getDsye() {
        return dsye;
    }

    public void setDsye(BigDecimal dsye) {
        this.dsye = dsye;
    }

    @Basic
    @Column(name = "bz", length = 200)
    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    @Basic
    @Column(name = "jyzjh", length = 50)
    public String getJyzjh() {
        return jyzjh;
    }

    public void setJyzjh(String jyzjh) {
        this.jyzjh = jyzjh;
    }
    @Basic
    @Column(name="jyxm",length = 50)
    public String getJyxm() {
        return jyxm;
    }

    public void setJyxm(String jyxm) {
        this.jyxm = jyxm;
    }

    @Basic
    @Column(name="jyfsd",length = 200)
    public String getJyfsd() {
        return jyfsd;
    }

    public void setJyfsd(String jyfsd) {
        this.jyfsd = jyfsd;
    }

    @Basic
    @Column(name = "aj_id", precision = 0)
    public long getAj_id() {
        return aj_id;
    }

    public void setAj_id(long aj_id) {
        this.aj_id = aj_id;
    }
    @Basic
    @Column(name="bcsm",length = 200)
    public String getBcsm() {return bcsm;}
    public void setBcsm(String bcsm){this.bcsm=bcsm;};
    @Basic
    @Column(name = "inserttime", length = 50)
    public String getInserttime() {
        return inserttime;
    }

    public void setInserttime(String inserttime) {
        this.inserttime = inserttime;
    }

    @Override
    public String toString() {
        return "BankZzxxEntity{" +
                "id=" + id +
                ", yhkkh='" + yhkkh + '\'' +
                ", yhkzh='" + yhkzh + '\'' +
                ", jysj='" + jysj + '\'' +
                ", jyje=" + jyje +
                ", jyye=" + jyye +
                ", sfbz='" + sfbz + '\'' +
                ", dskh='" + dskh + '\'' +
                ", dszh='" + dszh + '\'' +
                ", dsxm='" + dsxm + '\'' +
                ", dssfzh='" + dssfzh + '\'' +
                ", dskhh='" + dskhh + '\'' +
                ", zysm='" + zysm + '\'' +
                ", jysfcg='" + jysfcg + '\'' +
                ", jywdmc='" + jywdmc + '\'' +
                ", dsjyye=" + dsjyye +
                ", dsye=" + dsye +
                ", bz='" + bz + '\'' +
                ", jyzjh='" + jyzjh + '\'' +
                ", jyxm='" + jyxm + '\'' +
                ", jyfsd='" + jyfsd + '\'' +
                ", aj_id=" + aj_id +
                ", inserttime='" + inserttime + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankZzxxEntity that = (BankZzxxEntity) o;
        if (yhkkh != null ? !yhkkh.equals(that.yhkkh) : that.yhkkh != null) return false;
        if (jysj != null ? !jysj.equals(that.jysj) : that.jysj != null) return false;
        if (jyje != null ? !jyje.equals(that.jyje) : that.jyje != null) return false;
        if (jyye != null ? !jyye.equals(that.jyye) : that.jyye != null) return false;
        if (sfbz != null ? !sfbz.equals(that.sfbz) : that.sfbz != null) return false;
        return dskh != null ? dskh.equals(that.dskh) : that.dskh == null;
    }
    public String getHash(BankZzxxEntity b){
        DecimalFormat df = new DecimalFormat("#0.00");
        return b.getYhkkh()+b.getDskh()+b.getJysj()+df.format(b.getJyje())+df.format(b.getJyye()).replace("null","");
    }
    @Override
    public int hashCode() {
        DecimalFormat df = new DecimalFormat("#0.00");
        int result = yhkkh != null ? yhkkh.hashCode() : 0;
        result = 31 * result + (jysj != null ? jysj.hashCode() : 0);
        result = 31 * result + (jyje != null ? df.format(jyje).hashCode() : 0);
        result = 31 * result + (jyye != null ? df.format(jyye).hashCode() : 0);
        result = 31 * result + (sfbz != null ? sfbz.hashCode() : 0);
        result = 31 * result + (dskh != null ? dskh.hashCode() : 0);
        return result;
    }
    public  String remove_(String yhkkh){
        yhkkh = yhkkh.replace("\\N","").replaceFirst("-","").replace("暂无法获知","");
        if(yhkkh.indexOf("_")>5){
            return yhkkh.split("_")[0];
        }else {
            return yhkkh;
        }
    }
    //电信诈骗数据转实体
    public static BankZzxxEntity listDzToObj(List<String> list, Map<String,Integer> title){
        BankZzxxEntity b = new BankZzxxEntity();
        b.setYhkkh("".equals(list.get(title.get("yhkkh")).trim()) ? null:b.remove_(list.get(title.get("yhkkh"))).trim());
        b.setDskh("".equals(list.get(title.get("dskh")).trim())? null:b.remove_(list.get(title.get("dskh"))).trim());
        b.setDsxm("".equals(list.get(title.get("dsxm")).trim())? null:list.get(title.get("dsxm")).trim());
        b.setJyje(new BigDecimal(list.get(title.get("jyje")).trim().length()>0 ? list.get(title.get("jyje")).trim():"0" ));
        b.setJyye(new BigDecimal(list.get(title.get("jyye")).trim().length()>0 ? list.get(title.get("jyye")).trim():"0" ));
        b.setSfbz("".equals(list.get(title.get("sfbz")).trim())? null:list.get(title.get("sfbz")).replace("借","出").replace("贷","进").trim());
        b.setJysfcg("".equals(list.get(title.get("jysfcg")).trim())? null:list.get(title.get("jysfcg")).trim());
        b.setDskhh("".equals(list.get(title.get("dskhh")).trim())? null:list.get(title.get("dskhh")).trim());
        b.setJywdmc("".equals(list.get(title.get("jywdmc")).trim())? null:list.get(title.get("jywdmc")).trim());
        b.setZysm("".equals(list.get(title.get("zysm")).trim())? null:list.get(title.get("zysm")).trim());
        b.setJysj("".equals(list.get(title.get("jysj")).trim())? null:TimeFormatUtil.getDateSwitchTimestamp(list.get(title.get("jysj")).trim()));

        if(b.getDskh()==null||"".equals(b.getDskh())){
            String bcsm = b.getYhkkh()+"-";
            if(b.getDsxm()!=null&&!"".equals(b.getDsxm())){
                bcsm+=b.getDsxm();
            }else if(b.getZysm()!=null&&!"".equals(b.getZysm())){
                bcsm+=b.getZysm();
            }else if(b.getBz()!=null&&!"".equals(b.getBz())){
                bcsm+=b.getBz();
            }else {
                bcsm += "空账户";
            }
            b.setBcsm(bcsm);
        }

        return b;
    }

    public static BankZzxxEntity listToObj(List<String> list, Map<String,Integer> title){
        BankZzxxEntity b = new BankZzxxEntity();
        b.setYhkkh("".equals(list.get(title.get("yhkkh")).trim()) ? null:b.remove_(list.get(title.get("yhkkh"))).trim());
        b.setYhkzh("".equals(list.get(title.get("yhkzh")).trim()) ? null:b.remove_(list.get(title.get("yhkzh"))).trim());
        if(b.getYhkkh()==null){
            b.setYhkkh(b.getYhkzh());
        }
        if(title.containsKey("jysfm")){
            b.setJysj(
                    ("".equals(list.get(title.get("jysj")).trim())? null:list.get(title.get("jysj")).trim()) + " " +
                            ("".equals(list.get(title.get("jysfm")).trim()) ? null:list.get(title.get("jysfm")).trim()));
        }else{
            b.setJysj("".equals(list.get(title.get("jysj")).trim())? null:list.get(title.get("jysj")).trim());
        }
        b.setJyje(new BigDecimal(list.get(title.get("jyje")).trim().length()>0 ? list.get(title.get("jyje")).trim():"0" ));
        b.setJyye(new BigDecimal(list.get(title.get("jyye")).trim().length()>0 ? list.get(title.get("jyye")).trim():"0" ));
        b.setSfbz("".equals(list.get(title.get("sfbz")).trim())? null:list.get(title.get("sfbz")).replace("付","出").replace("收","进").trim());
        b.setDskh("".equals(list.get(title.get("dskh")).trim())? null:b.remove_(list.get(title.get("dskh"))).trim());
        if(title.containsKey("dszh")) {
            b.setDszh("".equals(list.get(title.get("dszh")).trim()) ? null : b.remove_(list.get(title.get("dszh"))).trim());
        }
        b.setDsxm("".equals(list.get(title.get("dsxm")).trim())? null:list.get(title.get("dsxm")).trim());
        b.setDssfzh("".equals(list.get(title.get("dssfzh")).trim())? null:list.get(title.get("dssfzh")).trim());
        b.setDskhh("".equals(list.get(title.get("dskhh")).trim())? null:list.get(title.get("dskhh")).trim());
        b.setZysm("".equals(list.get(title.get("zysm")).trim())? null:list.get(title.get("zysm")).trim());
        b.setJysfcg("".equals(list.get(title.get("jysfcg")).trim())? null:list.get(title.get("jysfcg")).trim());
        b.setJywdmc("".equals(list.get(title.get("jywdmc")).trim())? null:list.get(title.get("jywdmc")).trim());
        b.setDsjyye(new BigDecimal(list.get(title.get("dsjyye")).trim().length()>0 ?  list.get(title.get("dsjyye")).trim():"0"));
        if (title.containsKey("dsye")) {
            b.setDsye(new BigDecimal(list.get(title.get("dsye")).trim().length() > 0 ? list.get(title.get("dsye")).trim() : "0"));
        }
        b.setBz("".equals(list.get(title.get("bz")).trim())? null:list.get(title.get("bz")).trim());
        b.setJyzjh("".equals(list.get(title.get("jyzjh")).trim())? null:list.get(title.get("jyzjh")).trim());
        b.setJyxm("".equals(list.get(title.get("jyxm")).trim())? null:list.get(title.get("jyxm")).trim());
        b.setJyfsd("".equals(list.get(title.get("jyfsd")).trim())? null:list.get(title.get("jyfsd")).trim());
        if(b.getDskh()==null||"".equals(b.getDskh())){
            String bcsm = b.getYhkkh()+"-";
            if(b.getDsxm()!=null&&!"".equals(b.getDsxm())){
                bcsm+=b.getDsxm();
            }else if(b.getZysm()!=null&&!"".equals(b.getZysm())){
                bcsm+=b.getZysm();
            }else if(b.getBz()!=null&&!"".equals(b.getBz())){
                bcsm+=b.getBz();
            }else {
                bcsm += "空账户";
            }
            b.setBcsm(bcsm);
        }
        return b;
    }

    public BankZzxxEntity RowToEntity(Row xssfRow, Map<String,String> field, Map<String, Integer> title) {
        BankZzxxEntity bankZzxx = new BankZzxxEntity();
        String YHKKH = bankZzxx.remove_(MappingUtils.mappingFieldString(xssfRow,field.get("jykh"),title));
        bankZzxx.setYhkkh(YHKKH);
        bankZzxx.setJyxm(MappingUtils.mappingFieldString(xssfRow,field.get("jyxm"),title));
        bankZzxx.setJyzjh(MappingUtils.mappingFieldString(xssfRow,field.get("jyzjh"),title));
        String jyrq = MappingUtils.mappingFieldString(xssfRow,field.get("jyrq"),title);
        if(jyrq.length()>0){
            bankZzxx.setJysj(jyrq+" "+MappingUtils.mappingFieldString(xssfRow,field.get("jysj"),title));
        }else{
            bankZzxx.setJysj(MappingUtils.mappingFieldString(xssfRow,field.get("jysj"),title));
        }

        bankZzxx.setJyje(MappingUtils.mappingFieldBigDecimal(xssfRow,field.get("jyje"),title));
        bankZzxx.setJyye(MappingUtils.mappingFieldBigDecimal(xssfRow,field.get("jyye"),title));
        String sfbz = MappingUtils.mappingFieldString(xssfRow,field.get("sfbz"),title);
        if("".equals(sfbz)){
            sfbz ="0";
        }
        if(isStartWithNumber(sfbz)){
            if(new BigDecimal(sfbz).abs().compareTo(BigDecimal.ZERO)==0){
                bankZzxx.setSfbz("出");
                if(bankZzxx.getJyje().compareTo(BigDecimal.ZERO)==0){
                    bankZzxx.setSfbz(null);
                }
            }else{
                bankZzxx.setSfbz("进");
                bankZzxx.setJyje(new BigDecimal(sfbz));
            }
        }else{
            bankZzxx.setSfbz(sfbz.replace("收","进").replace("付","出")
                    .replace("贷","进").replace("借","出"));
        }

        bankZzxx.setDskh(bankZzxx.remove_(MappingUtils.mappingFieldString(xssfRow,field.get("dskh"),title)));
        bankZzxx.setDsxm(MappingUtils.mappingFieldString(xssfRow,field.get("dsxm"),title));
        bankZzxx.setDssfzh(MappingUtils.mappingFieldString(xssfRow,field.get("dszjh"),title));
        bankZzxx.setDskhh(MappingUtils.mappingFieldString(xssfRow,field.get("dskhh"),title));

        bankZzxx.setZysm(MappingUtils.mappingFieldString(xssfRow,field.get("zysm"),title));
//        bankZzxx.setJysfcg(MappingUtils.mappingFieldString(xssfRow,field.get(12),title));
//        bankZzxx.setYhkzh(bankZzxx.remove_(MappingUtils.mappingFieldString(xssfRow,field.get(13),title)));
//        bankZzxx.setDszh(bankZzxx.remove_(MappingUtils.mappingFieldString(xssfRow,field.get(8),title)));
        bankZzxx.setJywdmc(MappingUtils.mappingFieldString(xssfRow,field.get("jywdmc"),title));
//        bankZzxx.setDsjyye(MappingUtils.mappingFieldBigDecimal(xssfRow,field.get(17),title));
//        bankZzxx.setDsye(MappingUtils.mappingFieldBigDecimal(xssfRow,field.get(18),title));
        bankZzxx.setJyfsd(MappingUtils.mappingFieldString(xssfRow,field.get("jyfsd"),title));
        bankZzxx.setBz(MappingUtils.mappingFieldString(xssfRow,field.get("bz"),title));
        if(bankZzxx.getDskh()==null || "".equals(bankZzxx.getDskh())){
            String bcsm = bankZzxx.getYhkkh()+"-";
            if(bankZzxx.getDsxm()!=null && !"".equals(bankZzxx.getDsxm())){
                bcsm+=bankZzxx.getDsxm();
            }else if(bankZzxx.getZysm()!=null && !"".equals(bankZzxx.getZysm())){
                bcsm+=bankZzxx.getZysm();
            }else if(bankZzxx.getBz()!=null && !"".equals(bankZzxx.getBz())){
                bcsm+=bankZzxx.getBz();
            }else {
                bcsm += "空账户";
            }
            bankZzxx.setBcsm(bcsm);
        }
        return bankZzxx;
    }
}

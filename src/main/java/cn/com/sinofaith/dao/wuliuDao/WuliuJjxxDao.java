package cn.com.sinofaith.dao.wuliuDao;

import cn.com.sinofaith.bean.bankBean.BankZzxxEntity;
import cn.com.sinofaith.bean.wlBean.WuliuEntity;
import cn.com.sinofaith.dao.BaseDao;
import cn.com.sinofaith.util.DBUtil;
import cn.com.sinofaith.util.TimeFormatUtil;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class WuliuJjxxDao extends BaseDao<WuliuEntity> {

    /**
     * 物流数据导入
     * @param listJjxx
     * @param aj_id
     * @param all
     * @return
     */
    public int insertJjxx(List<WuliuEntity> listJjxx, long aj_id, List<WuliuEntity> all) {
        List<WuliuEntity> w = null;
        // 数据去重
        // 根据运单号+寄件时间去重
        if(all!=null && all.size()>0){
            Map<String,WuliuEntity> map1 = new HashMap<>();
            for (int i=0;i<all.size();i++){
                WuliuEntity wuliu = all.get(i);
                map1.put(wuliu.getWaybill_id()+wuliu.getShip_time().replace("null", ""),null);
            }
            Map<String,WuliuEntity> map = new HashMap<>();
            for(int i=0;i<listJjxx.size();i++){
                WuliuEntity wuliu = listJjxx.get(i);
                map.put(wuliu.getWaybill_id()+wuliu.getShip_time().replace("null", ""),wuliu);
            }

            // 将相同key添加
            List<String> str = new ArrayList<>();
            for(String key : map.keySet()){
                if(map1.containsKey(key)){
                    str.add(key);
                }
            }
            map1 = null;
            if(map.size()==str.size()){
                return -1;
            }

            for(int i=0;i<str.size();i++){
                // 去重
                map.remove(str.get(i));
            }
            w = new ArrayList<>(map.values());
            map = null;
        }else{
            w = listJjxx;
        }

        // 使用原生sql
        Connection conn = DBUtil.getConnection();
        String sql = "insert into wuliu(waybill_id,ship_time,ship_address,sender,ship_phone,ship_mobilephone,sj_address," +
                "addressee,sj_phone,sj_mobilephone,collector,tjw,payment,dshk,weightm,number_cases,freight,insert_time,aj_id) " +
                "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        int a = 0;
        PreparedStatement pstm = null;
        WuliuEntity zzxx = new WuliuEntity();
        // 做批处理
        try {
            // 关闭自动提交
            conn.setAutoCommit(false);
            pstm = conn.prepareStatement(sql);
            for(int i=0;i<w.size();i++){
                WuliuEntity wl = w.get(i);
                pstm.setString(1,wl.getWaybill_id());
                pstm.setString(2,wl.getShip_time());
                pstm.setString(3,wl.getShip_address());
                pstm.setString(4,wl.getSender());
                pstm.setString(5,wl.getShip_phone());
                pstm.setString(6,wl.getShip_mobilephone());
                pstm.setString(7,wl.getSj_address());
                pstm.setString(8,wl.getAddressee());
                pstm.setString(9,wl.getSj_phone());
                pstm.setString(10,wl.getSj_mobilephone());
                pstm.setString(11,wl.getCollector());
                pstm.setString(12,wl.getTjw());
                pstm.setString(13,wl.getPayment());
                pstm.setString(14,wl.getDshk());
                pstm.setString(15,wl.getWeight());
                pstm.setString(16,wl.getNumber_cases());
                pstm.setString(17,wl.getFreight());
                pstm.setString(18, TimeFormatUtil.getDate("/"));
                pstm.setLong(19,aj_id);
                pstm.addBatch();
                a++;
                // 有5000条添加一次
                if ((i + 1) % 50000 == 0) {
                    pstm.executeBatch();
                    pstm.clearParameters();
                    conn.commit();
                }
            }

            pstm.executeBatch();
            conn.commit();
            DBUtil.closeStatement(pstm);
            DBUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        listJjxx = null;
        return a;
    }
}

package cn.com.sinofaith.service.wlService;

import cn.com.sinofaith.bean.wlBean.WuliuEntity;
import cn.com.sinofaith.dao.wuliuDao.WuliuDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WuliuJjxxService {

    @Autowired
    private WuliuDao wld;

    /**
     * 获取案件id获取物流数据
     * @param id
     * @return
     */
    public List<WuliuEntity> getAll(long id) {
        String hql = "from WuliuEntity where aj_id = "+id;
        List<WuliuEntity> wulius = wld.find(hql);
        return wulius;
    }
}

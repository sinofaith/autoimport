package cn.com.sinofaith.dao;

/**
 * Created by Me. on 2018/5/21
 */

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.hibernate.*;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Transactional
public class BaseDao<T>{

    @Autowired
    private SessionFactory sessionFactory;

    private Class<T> entityClass;
    /**
     * 通过反射获取子类确定的泛型类
     */
    public BaseDao() {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        entityClass = (Class) params[0];
    }

    /**
     * 根据ID加载PO实例
     *
     * @param id
     * @return 返回相应的持久化PO实例
     */
    public T load(Serializable id) {
        Session session = getSession();
        session.beginTransaction();
        T entity = (T) session.load(entityClass, id);
        session.getTransaction().commit();
        return entity;
    }

    /**
     * 根据ID获取PO实例
     *
     * @param id
     * @return 返回相应的持久化PO实例
     */
    public T get(Serializable id) {
        Session session = getSession();
        session.beginTransaction();
        T entity = (T) session.get(entityClass, id);
        session.getTransaction().commit();
        return entity;
    }

    /**
     * @param
     * @return 返回所有相应的持久化实例
     */
    public List<T> getAll() {

        return find("from " + entityClass.getSimpleName());
    }

    /**
     * 保存PO
     *
     * @param entity
     */
    public Object save(T entity) {
        Session session = getSession();
        Object sb = null;
        try{
            session.beginTransaction();
            sb = session.save(entity);
            session.getTransaction().commit();

        }catch (Exception e){
            session.close();
        }
        return sb;
    }
    public void saveOrUpdate(T entity){
        Session session = getSession();
        try{
            session.beginTransaction();
            session.saveOrUpdate(entity);
            session.getTransaction().commit();

        }catch (Exception e){
            session.close();
        }
    }
    /**
     * 删除PO
     *
     * @param entity
     */
    public void delete(T entity) {
        Session session = getSession();
        session.beginTransaction();
        session.delete(entity);
        session.getTransaction().commit();
    }

    /**
     * 删除PO
     *
     * @param hql
     */
    public void delete(String hql) {
        Session session = getSession();
        session.beginTransaction();
        Query query = session.createQuery(hql);
        query.executeUpdate();
        session.getTransaction().commit();
    }

    /**
     * 更改PO
     *
     * @param entity
     */
    public void update(T entity) {
        Session session = getSession();
        try{
            session.beginTransaction();
            session.update(entity);
            session.getTransaction().commit();

        }catch (Exception e){
            e.printStackTrace();
            session.close();
        }
    }

    /**
     *
     * @param hql
     * @param pageNum  当前页码
     * @param pageCapacity   每页容量
     * @return
     */
    public List doPage(String hql, int pageNum, int pageCapacity) {
        Session session = getSession();
        session.beginTransaction();
        Query query = session.createQuery(hql);
        query.setFirstResult((pageNum-1)*pageCapacity);
        query.setMaxResults(pageCapacity);
        List list = query.list();
        session.getTransaction().commit();

        return list;
    }

    /**
     * 执行HQL查询
     *
     * @param hql
     * @return 查询结果
     */
    public List find(String hql) {
        Session session = getSession();
        List resultList = null;
        try{
            session.beginTransaction();
            resultList = session.createQuery(hql).list();
            session.getTransaction().commit();

        }catch (Exception e){
            e.printStackTrace();
            session.close();
        }
        return resultList;
    }

    /**
     * 创建Query对象. 对于需要first,max,fetchsize,cache,cacheRegion等诸多设置的函数,可以在返回Query后自行设置.
     * 留意可以连续设置,如下：
     * <pre>
     * dao.getQuery(hql).setMaxResult(100).setCacheable(true).list();
     * </pre>
     * 调用方式如下：
     * <pre>
     *        dao.createQuery(hql)
     *        dao.createQuery(hql,arg0);
     *        dao.createQuery(hql,arg0,arg1);
     *        dao.createQuery(hql,new Object[arg0,arg1,arg2])
     * </pre>
     *
     * @param values 可变参数.
     */
    public Query createQuery(String hql, Object... values) {
        Assert.hasText(hql);
        Query query = getSession().createQuery(hql);
        for (int i = 0; i < values.length; i++) {
            query.setParameter(i, values[i]);
        }
        return query;
    }

    /**
     * 使用原生的SQL执行查询，返回List<Map<String,Object>>
     */
    public List findBySQL(String sql){
        Session session = getSession();
        List<Map<String, Object>> list = null;
        try{
            session.beginTransaction();
            Query query = session.createSQLQuery(sql);
            list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
            session.getTransaction().commit();

        }catch (Exception e){
            e.printStackTrace();
            session.close();
        }
        return list;
    }

    /**
     * 使用原生的SQL执行查询，只返回其中一列
     */
    public List findOneColumnBySQL(String sql){
        Session session = getSession();
        List<String> list = null;
        try{
            session.beginTransaction();
            list = session.createSQLQuery(sql).list();
            session.getTransaction().commit();

        }catch (Exception e){
            e.printStackTrace();
            session.close();
        }
        return list;
    }

    public int updateBySql(String sql){
        Session session = getSession();
        int num = 0;
        try {
            session.beginTransaction();
            num = session.createSQLQuery(sql).executeUpdate();
            session.getTransaction().commit();
        }catch (Exception e){
            e.printStackTrace();
            session.close();
        }
        return num;
    }

    public  Session getSession() {
        return sessionFactory.getCurrentSession();
    }
    public  Session openSession() {
        return sessionFactory.openSession();
    }

    /**
     * 获取所有条目
     * @return
     */
    public int getRowAll(DetachedCriteria dc1) {
        Session session = getSession();
        Long rowAll = 0l;
        try {
            Transaction tx = session.beginTransaction();
            Criteria criteria = dc1.getExecutableCriteria(session);
            // 设置聚合查询函数
            criteria.setProjection(Projections.rowCount());
            rowAll = (Long) criteria.uniqueResult();
            // 将条件清空，用于dc查询分页数据
            criteria.setProjection(null);
            tx.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.close();
        }
        return rowAll.intValue();
    }

    /**
     * 分页查询
     * @param currentPage
     * @param pageSize
     * @param dc
     * @return
     */
    public List<T> getDoPage(int currentPage, int pageSize, DetachedCriteria dc) {
        Session session = getSession();
        List<T> zhxxs = null;
        try {
            // 开启事务
            Transaction tx = session.beginTransaction();
            // 关联session
            Criteria criteria = dc.getExecutableCriteria(session);
            criteria.setFirstResult((currentPage-1)*pageSize);
            criteria.setMaxResults(pageSize);
            // 创建对象
            zhxxs = criteria.list();
            tx.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.close();
        }
        return zhxxs;
    }
}
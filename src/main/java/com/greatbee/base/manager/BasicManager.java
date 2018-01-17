package com.greatbee.base.manager;

import com.greatbee.base.bean.DBException;
import com.greatbee.base.bean.DataPage;
import com.greatbee.core.bean.view.Condition;

import java.util.List;

/**
 * BasicManager
 * <p/>
 * Created by CarlChen on 2016/12/20.
 */
public interface BasicManager {
    /**
     * 添加
     *
     * @param object
     * @return
     */
    public int add(Object object) throws DBException;

    /**
     * 批量添加
     *
     * @param bean
     * @return
     * @throws DBException
     */
    public int add(List<Object> bean) throws DBException;

    /**
     * 更新
     *
     * @param object
     */
    public void update(Object object) throws DBException;


    /**
     * 根据字段条件来更新
     *
     * @param id
     * @param columnName
     * @param columnValue
     * @throws DBException
     */
    public void update(int id, String columnName, Object columnValue) throws DBException;

    /**
     * 读取
     */
    public Object read(int id) throws DBException;

    /**
     * 读取多个
     *
     * @param ids
     * @return
     * @throws DBException
     */
    public List read(int[] ids) throws DBException;

    /**
     * 列表
     *
     * @return
     */
    public List list() throws DBException;

    /**
     * 根据列值来获取列表
     *
     * @param columnName
     * @param columnValue
     * @return
     * @throws DBException
     */
    public List list(String columnName, Object columnValue) throws DBException;

    /**
     * 根据列值来获取列表
     *
     * @param columnName
     * @param columnValue
     * @param orderColumn
     * @param asc
     * @return
     * @throws DBException
     */
    public List list(String columnName, Object columnValue, String orderColumn, boolean asc) throws DBException;

    /**
     * 根据排序来列表
     *
     * @param orderColumn
     * @param asc
     * @return
     * @throws DBException
     */
    public List list(String orderColumn, boolean asc) throws DBException;

    /**
     * 列表搜索
     *
     * @param condition
     * @return
     * @throws DBException
     */
    public List list(Condition condition) throws DBException;

    /**
     * 列表搜索 带排序
     *
     * @param condition
     * @return
     * @throws DBException
     */
    public List list(Condition condition,String orderColumn, boolean asc) throws DBException;

    /**
     * 翻页
     *
     * @param page
     * @param pageSize
     * @return
     */
    public DataPage page(int page, int pageSize) throws DBException;

    /**
     * 搜索翻页
     *
     * @param page
     * @param pageSize
     * @param condition
     * @return
     * @throws DBException
     */
    public DataPage page(int page, int pageSize, Condition condition) throws DBException;

    /**
     * 搜索翻页
     *
     * @param page
     * @param pageSize
     * @param orderColumn
     * @param isAsc
     * @return
     * @throws DBException
     */
    public DataPage page(int page, int pageSize, Condition condition, String orderColumn, boolean isAsc) throws DBException;

    /**
     * 删除
     *
     * @param id
     */
    public void delete(int id) throws DBException;

    /**
     * 批量删除
     *
     * @param operationBeanIds
     * @throws DBException
     */
    public void delete(int[] operationBeanIds) throws DBException;
}

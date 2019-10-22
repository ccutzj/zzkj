package com.zzkj.sales.dao;

import com.zzkj.sales.entity.Product;
import com.zzkj.sales.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface ProductDao {
    int deleteByPrimaryKey(Integer productId);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer productId);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> selectByUserId (Integer userId);

    List<Product> selectByShopId (Integer shopId);

    int updateByProductId(Product record);

    List<Product> selectByStatus(Product record);

    //分页查询
    List<Product> selectByPage(Integer shopId, Integer pageNum, Integer pageSize);
    //查询数据个数
    int selectByCount(Integer shopId);
    //按时间查询个数
    int selectByTimeCount(Integer shopId, String startTime);
    //按时间查询
    List<Product> selectByTime(@Param("shopId") Integer shopId,
                               @Param("startTime") String startTime,
                               @Param("pageNum") Integer pageNum,
                               @Param("pageSize") Integer pageSize);
    //返回所有海报月份
    List<String> selectTime(Integer shopId);
}
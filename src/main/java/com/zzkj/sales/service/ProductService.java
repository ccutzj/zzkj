package com.zzkj.sales.service;

import com.zzkj.sales.entity.Product;
import com.zzkj.sales.entity.User;
import org.omg.CORBA.INTERNAL;

import java.util.List;

public interface ProductService {
    List<Product> insert(User operator, Product product);
    List<Product> queryByUserId(Integer userId);
    List<Product> queryByShopId(Integer shopId);
    List<Product> updateByProductId(User operator, Product product);
    List<Product> deleteByProductId(User operator, Product product);

    List<Product> selectByStatus(User operator, Product product);
    List<Product> examineUpdate(User operator, Product product);

    Integer getTotalPage(Integer shopId, int pageSize);
    List<Product> findByPage(Integer shopId, Integer pageNum, Integer pageSize);
    List<Product> findByTime(Integer shopId, String startTime, Integer pageNum, Integer pageSize);
    Integer getTimeTotalPage(Integer shopId, int pageSize, String startTime);

    List<String> selectTime(Integer shopId);
}

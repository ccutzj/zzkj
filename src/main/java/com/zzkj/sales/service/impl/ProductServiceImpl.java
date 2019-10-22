package com.zzkj.sales.service.impl;

import com.zzkj.sales.dao.ProductDao;
import com.zzkj.sales.entity.Product;
import com.zzkj.sales.entity.User;
import com.zzkj.sales.exception.ParamException;
import com.zzkj.sales.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductDao productDao;

    @Override
    @Transactional
    public List<Product> insert(User operator, Product product) {
        if (product != null){
            product.setShopId(operator.getShopId());
            product.setUserId(operator.getUserId());
            product.setCreateTime(new Date());
            product.setStatus(0);
            int count = productDao.insert(product);
            if (count <= 0){
                throw new ParamException("添加失败");
            }
            List<Product> products = new ArrayList<>();
            if (operator.getRole() == 0){
                products = queryByShopId(operator.getShopId());
            }else {
                products = queryByUserId(operator.getUserId());
            }
            if (products != null){
                return products;
            }
        }
        return null;
    }

    @Override
    public List<Product> queryByUserId(Integer userId) {
        if (userId != null){
            List<Product> products = productDao.selectByUserId(userId);
            if (products != null){
                return products;
            }
        }
        return null;
    }

    @Override
    public List<Product> queryByShopId(Integer shopId) {
        if (shopId != null){
            List<Product> products = productDao.selectByShopId(shopId);
            if (products != null){
                return products;
            }
        }
        return null;
    }

    @Override
    @Transactional
    public List<Product> updateByProductId(User operator, Product product) {
        if (product != null){
            product.setStatus(0);
            int count = productDao.updateByPrimaryKeySelective(product);
            if (count <= 0){
                throw new ParamException("更新失败");
            }
            List<Product> productList = new ArrayList<>();
            if (operator.getRole() == 0){
                productList = productDao.selectByShopId(operator.getShopId());
            }else {
                productList = productDao.selectByUserId(operator.getUserId());
            }
            return productList;
        }
        return null;
    }

    @Override
    @Transactional
    public List<Product> deleteByProductId(User operator, Product product) {
        if (product != null){
            product.setStatus(3);
            System.out.println("service product:" + product.toString());
            int count = productDao.updateByProductId(product);
            System.out.println("count：" + count);
            if (count <= 0){
                throw new ParamException("Dao删除失败");
            }
            List<Product> productList = new ArrayList<>();
            if (operator.getRole() == 0){
                productList = productDao.selectByShopId(operator.getShopId());

            }else {
                productList = productDao.selectByUserId(operator.getUserId());

            }
            return productList;
        }
        return null;
    }

    @Override
    public List<Product> selectByStatus(User operator, Product product) {
        if (product != null){
            product.setShopId(operator.getShopId());
            List<Product> productList = productDao.selectByStatus(product);
            return productList;
        }
        return null;
    }

    @Override
    @Transactional
    public List<Product> examineUpdate(User operator, Product product) {
        if (product != null){
            int count = productDao.updateByProductId(product);
            if (count <= 0){
                throw new ParamException("更新失败");
            }
        }
        return null;
    }


    //微信端service
    @Override
    public Integer getTotalPage(Integer shopId, int pageSize) {
        int totalCount = productDao.selectByCount(shopId);
        return totalCount % pageSize == 0 ? totalCount/pageSize : totalCount/pageSize + 1;
    }

    @Override
    public Integer getTimeTotalPage(Integer shopId, int pageSize, String startTime) {
        int totalCount = productDao.selectByTimeCount(shopId, startTime);
        return totalCount % pageSize == 0 ? totalCount/pageSize : totalCount/pageSize + 1;
    }

    @Override
    public List<Product> findByPage(Integer shopId, Integer pageNum, Integer pageSize) {
        List<Product> productList = productDao.selectByPage(shopId, pageNum*pageSize, pageSize);
        return productList;
    }

    @Override
    public List<Product> findByTime(Integer shopId, String startTime, Integer pageNum, Integer pageSize) {
        List<Product> productList = productDao.selectByTime(shopId, startTime, pageNum*pageSize, pageSize);
        return productList;
    }

    @Override
    public List<String> selectTime(Integer shopId) {
        List<String> timeList = productDao.selectTime(shopId);
        return timeList;
    }
}

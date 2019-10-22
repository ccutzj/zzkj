package com.zzkj.sales.service.impl;

import com.zzkj.sales.dao.ShopDao;
import com.zzkj.sales.entity.Shop;
import com.zzkj.sales.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    ShopDao shopDao;

    @Override
    public int getTotalPage(int pageSize) {
        int totalCount = shopDao.selectByCount();
        return totalCount % pageSize == 0 ? totalCount/pageSize : totalCount/pageSize + 1;
    }

    @Override
    public List<Shop> findByPage(int pageNum, int pageSize) {
        List<Shop> shopList = shopDao.selectByPage(pageNum*pageSize, pageSize);
        return shopList;
    }
}

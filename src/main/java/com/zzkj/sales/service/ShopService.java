package com.zzkj.sales.service;

import com.zzkj.sales.entity.Shop;

import java.util.List;

public interface ShopService {
    int getTotalPage(int pageSize);
    List<Shop> findByPage(int pageNum, int pageSize);
}

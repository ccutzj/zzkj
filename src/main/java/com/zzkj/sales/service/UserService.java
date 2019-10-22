package com.zzkj.sales.service;

import com.zzkj.sales.entity.User;

import java.util.List;

public interface UserService {

    //通过用户名称获取user信息
    User queryByUsername(String userName);
    List<User> queryByShopId(Integer shopId);

    String selectUuid();

    List<User> insert(User record, User operator);

    List<User> update(User record, User operator);

    List<User> delete(User record, User operator);


}

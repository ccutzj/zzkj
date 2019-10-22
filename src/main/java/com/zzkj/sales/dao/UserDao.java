package com.zzkj.sales.dao;

import com.zzkj.sales.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserDao {
    int deleteByPrimaryKey(Integer userId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer userId);

    User selectByUserName(String userName, Integer status);

    List<User> selectByShopId(Integer shopId, Integer status);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int updateByUsernameSelective(User record);

    int deleteByUsername(User record);

    String selectUuid();

    int deleteByUserId(User record);
}
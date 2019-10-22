package com.zzkj.sales.service.impl;

import com.zzkj.sales.dao.UserDao;
import com.zzkj.sales.entity.User;
import com.zzkj.sales.exception.ParamException;
import com.zzkj.sales.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

//    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private UserDao userDao;
    @Override
    public User queryByUsername(String userName) {
        if (userName != null){
            int status = 1;
            User user = userDao.selectByUserName(userName, status);
            if (user != null){
                return user;
            }
        }
        return null;
    }

    @Override
    public List<User> queryByShopId(Integer shopId) {
        if(shopId != null){
            int status = 1;
            List<User> users = userDao.selectByShopId(shopId, status);
            if (users != null){
                return users;
            }
        }
        return null;
    }

    @Override
    public String selectUuid() {
        return userDao.selectUuid();
    }

    @Override
    @Transactional
    public List<User> insert(User record, User operator) {
       if (record != null){
           String uuid = selectUuid();
           System.out.println(uuid);
           if (StringUtils.isEmpty(uuid)){
               throw new RuntimeException("system error: uuid get error!");
           }
           record.setUuid(uuid);
           record.setCreateTime(new Date());
           int count = userDao.insertSelective(record);
           if (count <= 0){
               throw new ParamException("新增用户失败");
           }
           List<User> users = queryByShopId(operator.getShopId());
           return users;
       }
       return null;
    }

    @Override
    @Transactional
    public List<User> update(User record, User operator) {
        if (record != null){
            int count = userDao.updateByUsernameSelective(record);
            if (count <= 0){
                throw new ParamException("更新失败");
            }
            List<User> users = new ArrayList<>();
            if(operator.getRole() == 0){
                users = queryByShopId(operator.getShopId());
            }else{
                User user = queryByUsername(operator.getUserName());
                users.add(user);
            }

            return users;
        }
        return null;
    }

    @Override
    @Transactional
    public List<User> delete(User record, User operator) {
        if (record != null){
            record.setStatus(2);
            int count = userDao.deleteByUserId(record);
            if (count <= 0){
                throw new ParamException("删除失败");
            }
            List<User> users = null;
            if(operator.getRole() == 0){
                users = queryByShopId(operator.getShopId());
            }else{
                User user = queryByUsername(operator.getUserName());
                users.add(user);
            }

            return users;
        }
        return null;
    }
}

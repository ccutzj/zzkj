package com.zzkj.sales.controller.admin;

import com.zzkj.sales.annotation.LoginToken;
import com.zzkj.sales.common.JsonData;
import com.zzkj.sales.entity.User;
import com.zzkj.sales.exception.ParamException;
import com.zzkj.sales.service.UserService;
import com.zzkj.sales.util.GetUserUtil;
import com.zzkj.sales.util.JsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/usermanage")
public class UserManagementController {
    @Autowired
    UserService userService;

    @RequestMapping("/query")
    @ResponseBody
    @LoginToken
    //查询用户信息
    public JsonData query(HttpServletRequest request, HttpServletResponse response){
//        String username = HttpServletRequestUtil.getString(request, "username");
        String url = request.getRequestURL().toString();
        url = url.substring(0, url.lastIndexOf(request.getServletPath())) + "/image/";
        String token = request.getHeader("token");
        //通过token获取用户名
        String username = GetUserUtil.getUserName(token);
        User user = userService.queryByUsername(username);
        if (user != null){
            List<User> users = new ArrayList<>();
            //如果用户是管理员则返回所有用户信息，否则返回自己的信息
            if(user.getRole() == 0){
                users = userService.queryByShopId(user.getShopId());
                for (User u: users) {
                    u.setPassword(null);
                    u.getShop().setShopImg(url + u.getShop().getShopImg());
                }
                return JsonData.success(users);
            }else {
                user.setPassword(null);
                user.getShop().setShopImg(url + user.getShop().getShopImg());
                users.add(user);
                return JsonData.success(users);
            }
        }
        return JsonData.fail("查询失败");
    }

    //新增用户信息
    @RequestMapping("/createuser")
    @ResponseBody
    @LoginToken
    public JsonData createUser(HttpServletRequest request, HttpServletResponse response,@RequestBody(required = false) String userInfo){
        if (StringUtils.isEmpty(userInfo)){
            throw new ParamException("未接收到请求参数");
        }
        String url = request.getRequestURL().toString();
        url = url.substring(0, url.lastIndexOf(request.getServletPath())) + "/image/";
        String token = request.getHeader("token");
        String userName = GetUserUtil.getUserName(token);
        User operator = userService.queryByUsername(userName);
        if (operator != null){
            if (operator.getRole() != 0){
                throw new ParamException("您没有权限，请联系管理员");
            }
            User user = JsonMapper.string2Obj(userInfo, User.class);
            if (user == null){
                throw new ParamException("数据信息获取异常");
            }
            User usertemp = userService.queryByUsername(user.getUserName());
            if (usertemp != null){
                return JsonData.fail("用户名已经存在，请更换用户名!");
            }
            List<User> users = userService.insert(user, operator);

            if (users != null){
                for(User u : users){
                    u.setPassword(null);
                    u.getShop().setShopImg(url + u.getShop().getShopImg());
                }
                return JsonData.success(users);
            }
        }
        return JsonData.fail("系统异常，添加失败!");
    }
    //修改用户信息
    @RequestMapping("/update")
    @ResponseBody
    @LoginToken
    public JsonData updateUser(HttpServletRequest request, HttpServletResponse response,@RequestBody(required = false) String userInfo) {
        String token = request.getHeader("token");
        String url = request.getRequestURL().toString();
        url = url.substring(0, url.lastIndexOf(request.getServletPath())) + "/image/";
        //通过token获取用户名
        String username = GetUserUtil.getUserName(token);
        User operator = userService.queryByUsername(username);
        if (operator != null){
//            if (operator.getRole() != 0){
//                throw new ParamException("您没有权限，请联系管理员");
//            }
            User user = JsonMapper.string2Obj(userInfo, User.class);
            if (user == null){
                throw new ParamException("数据信息获取异常");
            }
            List<User> users = userService.update(user, operator);
            if (users != null){
                for(User u : users){
                    u.setPassword(null);
                    u.getShop().setShopImg(url + u.getShop().getShopImg());
                }
                return JsonData.success(users);
            }
        }
        return JsonData.fail("系统异常，更新失败!");
    }

        //删除用户信息
        @RequestMapping("/delete")
        @ResponseBody
        @LoginToken
        public JsonData deleteUser(HttpServletRequest request, HttpServletResponse response,@RequestBody(required = false) String userInfo) {
            String token = request.getHeader("token");
            String url = request.getRequestURL().toString();
            url = url.substring(0, url.lastIndexOf(request.getServletPath())) + "/image/";
            //通过token获取用户名
            String username = GetUserUtil.getUserName(token);
            User operator = userService.queryByUsername(username);
            if (operator != null){
                if (operator.getRole() != 0){
                    throw new ParamException("您没有权限，请联系管理员");
                }
                User user = JsonMapper.string2Obj(userInfo, User.class);
                if (user == null){
                    throw new ParamException("数据信息获取异常");
                }
                List<User> users = userService.delete(user, operator);
                if (users != null){
                    for(User u : users){
                        u.setPassword(null);
                        u.getShop().setShopImg(url + u.getShop().getShopImg());
                    }
                    return JsonData.success(users);
                }
            }
            return JsonData.fail("系统异常，删除失败!");
        }
}

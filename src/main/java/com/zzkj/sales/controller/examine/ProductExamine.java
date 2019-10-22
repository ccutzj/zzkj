package com.zzkj.sales.controller.examine;

import com.zzkj.sales.annotation.LoginToken;
import com.zzkj.sales.common.JsonData;
import com.zzkj.sales.entity.Product;
import com.zzkj.sales.entity.User;
import com.zzkj.sales.exception.ParamException;
import com.zzkj.sales.service.ProductService;
import com.zzkj.sales.service.UserService;
import com.zzkj.sales.util.GetUserUtil;
import com.zzkj.sales.util.JsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/productexamine")
public class ProductExamine {

    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;

    @RequestMapping("/query")
    @ResponseBody
    @LoginToken
    public JsonData queryExamine(HttpServletRequest request, HttpServletResponse response, @RequestBody(required = false) String productInfo){
        String token = request.getHeader("token");
        String url = request.getRequestURL().toString();
        url = url.substring(0, url.lastIndexOf(request.getServletPath())) + "/image/";
        //通过token获取用户名
        String username = GetUserUtil.getUserName(token);
        User operator = userService.queryByUsername(username);
        if (operator != null){
            Product product = JsonMapper.string2Obj(productInfo, Product.class);
            if (product == null){
                throw new ParamException("数据信息获取异常");
            }
            if (operator.getRole() != 0){
                throw new ParamException("您没有权限，请联系管理员");
            }
            List<Product> productList = productService.selectByStatus(operator, product);
//            operator.setProductList(productList);
            for (Product p : productList){
                p.setProductImg(url + p.getProductImg());
            }
            return JsonData.success(productList);
        }
        return JsonData.fail("查询失败");
    }

    @RequestMapping("/update")
    @ResponseBody
    @LoginToken
    public JsonData updateExamine(HttpServletRequest request, HttpServletResponse response, @RequestBody(required = false) String productInfo){
        String token = request.getHeader("token");
//        response.setHeader("token", token);
        //通过token获取用户名
        String username = GetUserUtil.getUserName(token);
        User operator = userService.queryByUsername(username);
        if (operator != null){
            Product product = JsonMapper.string2Obj(productInfo, Product.class);
            System.out.println(product.toString());
            if (product == null){
                throw new ParamException("数据信息获取异常");
            }
            if (operator.getRole() != 0){
                throw new ParamException("您没有权限，请联系管理员");
            }
            List<Product> productList = productService.examineUpdate(operator, product);
//            operator.setProductList(productList);
            return JsonData.success(null, "更新成功");
        }
        return JsonData.fail("更新失败");
    }

}

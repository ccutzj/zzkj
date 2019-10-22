package com.zzkj.sales.controller.weixin;

import com.zzkj.sales.annotation.PassToken;
import com.zzkj.sales.common.JsonData;
import com.zzkj.sales.entity.Product;
import com.zzkj.sales.entity.Shop;
import com.zzkj.sales.exception.ParamException;
import com.zzkj.sales.service.ProductService;
import com.zzkj.sales.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/weixin")
public class WeixinHomeController {

    @Autowired
    ShopService shopService;
    @Autowired
    ProductService productService;

    @RequestMapping("/shoplist")
    @ResponseBody
    @PassToken
    public JsonData shopList(HttpServletRequest request, HttpServletResponse response){
        String url = request.getRequestURL().toString();
        url = url.substring(0, url.lastIndexOf(request.getServletPath())) + "/image/";
        String pageNumStr = request.getParameter("pageNum");
        String pageSizeStr = request.getParameter("pageSize");
        System.out.println( pageNumStr + "------->" + pageSizeStr);
        //这是默认当前页与页面显示数据大小
        int pageNum = 0;
        int pageSize = 4;
        //验证当前页与数据大小
        if (pageSizeStr != null){
            pageSize = Integer.parseInt(pageSizeStr);
            System.out.println("pageSize:" + pageSize);
        }
        if(pageNumStr != null){
            pageNum = Integer.parseInt(pageNumStr);
            System.out.println("pageNum:" + pageNum);
            //验证上一页与下一页溢出
            if (pageNum < 0){
                pageNum = 0;
            }
            int totalPage = shopService.getTotalPage(pageSize);
            if (pageNum > totalPage){
//                pageNum = totalPage;
                return JsonData.fail("没有数据");
            }
        }
        List<Map<String, String>> date = new ArrayList<>();
        Map<String, String> shop;
        List<Shop> shopList = shopService.findByPage(pageNum, pageSize);
        if (shopList != null){
            for (Shop list : shopList){
                shop = new HashMap<>();
                shop.put("shopId", list.getShopId().toString());
                shop.put("shopName", list.getShopName());
                shop.put("shopImg", url + list.getShopImg());
                date.add(shop);
            }
            Integer totalPage = shopService.getTotalPage(pageSize);
            return JsonData.success(date,"查询成功", totalPage);
        }
        return JsonData.fail("没有数据");
    }

    @RequestMapping("/productlist")
    @ResponseBody
    @PassToken
    public JsonData productList(HttpServletRequest request, HttpServletResponse response){
        String url = request.getRequestURL().toString();
        url = url.substring(0, url.lastIndexOf(request.getServletPath())) + "/image/";
        String shopIdStr = request.getParameter("shopId");
        String pageNumStr = request.getParameter("pageNum");
        String pageSizeStr = request.getParameter("pageSize");
        System.out.println( pageNumStr + "------->" + pageSizeStr + "-------->" + shopIdStr);
        int totalPage;
        //这是默认当前页与页面显示数据大小
        int pageNum = 0;
        int pageSize = 4;
        Integer shopId = null;
        if (shopIdStr != null){
            shopId = Integer.parseInt(shopIdStr);
        }
        //验证当前页与数据大小
        if (pageSizeStr != null){
            pageSize = Integer.parseInt(pageSizeStr);
            System.out.println("pageSize:" + pageSize);
        }
        if(pageNumStr != null){
            pageNum = Integer.parseInt(pageNumStr);
            System.out.println("pageNum:" + pageNum);
            //验证上一页与下一页溢出
            if (pageNum < 0){
                pageNum = 0;
            }
            totalPage = productService.getTotalPage(shopId, pageSize);
            if (pageNum > totalPage){
                return JsonData.fail("没有数据啦");
            }
        }
        List<Map<String, String>> date = new ArrayList<>();
        Map<String, String> product = null;
        List<Product> productList = productService.findByPage(shopId, pageNum, pageSize);
        if (productList != null){
            for (Product p : productList){
                product = new HashMap<>();
                product.put("productId", p.getProductId().toString());
                product.put("productImg", url + p.getProductImg());
                product.put("shopId", p.getShop().getShopId().toString());
                product.put("shopName", p.getShop().getShopName());
                product.put("shopImg", url + p.getShop().getShopImg());
                date.add(product);
            }
            totalPage = productService.getTotalPage(shopId, pageSize);
            List<String> stringList = productService.selectTime(shopId);
            Map<String, Object> dateMap = new HashMap<>();
            dateMap.put("data", date);
            dateMap.put("startTime", stringList);

            return JsonData.success(dateMap,"查询成功", totalPage);
        }
        return JsonData.fail("没有数据");
    }

    @RequestMapping("/timelist")
    @ResponseBody
    @PassToken
    public JsonData timeList(HttpServletRequest request, HttpServletResponse response){
        String url = request.getRequestURL().toString();
        url = url.substring(0, url.lastIndexOf(request.getServletPath())) + "/image/";
        String shopIdStr = request.getParameter("shopId");
        String pageNumStr = request.getParameter("pageNum");
        String pageSizeStr = request.getParameter("pageSize");
        String startTime = request.getParameter("startTime");
        int totalPage;
        //这是默认当前页与页面显示数据大小
        int pageNum = 0;
        int pageSize = 4;
        Integer shopId = null;
        if (shopIdStr != null){
            shopId = Integer.parseInt(shopIdStr);
        }
        if (startTime == null && startTime.trim().equals("")){
            throw new ParamException("搜索时间不能为空");
        }
        //验证当前页与数据大小
        if (pageSizeStr != null){
            pageSize = Integer.parseInt(pageSizeStr);
            System.out.println("pageSize:" + pageSize);
        }
        if(pageNumStr != null){
            pageNum = Integer.parseInt(pageNumStr);
            System.out.println("pageNum:" + pageNum);
            //验证上一页与下一页溢出
            if (pageNum < 0){
                pageNum = 0;
            }
            totalPage = productService.getTimeTotalPage(shopId, pageSize, startTime);
            if (pageNum > totalPage){
                return JsonData.fail("没有数据啦");
            }
        }
        List<Map<String, String>> date = new ArrayList<>();
        Map<String, String> product = null;
        List<Product> productList = productService.findByTime(shopId, startTime, pageNum, pageSize);
        if (productList != null){
            for (Product p : productList){
                product = new HashMap<>();
                product.put("productId", p.getProductId().toString());
                product.put("productImg", url + p.getProductImg());
                product.put("shopId", p.getShop().getShopId().toString());
                product.put("shopName", p.getShop().getShopName());
                product.put("shopImg", url + p.getShop().getShopImg());
                date.add(product);
            }
            totalPage = productService.getTimeTotalPage(shopId, pageSize, startTime);
            return JsonData.success(date,"查询成功", totalPage);
        }
        return JsonData.fail("没有数据");
    }
}

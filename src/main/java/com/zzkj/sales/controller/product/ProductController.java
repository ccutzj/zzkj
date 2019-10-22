package com.zzkj.sales.controller.product;

import com.zzkj.sales.annotation.LoginToken;
import com.zzkj.sales.common.JsonData;
import com.zzkj.sales.entity.Product;
import com.zzkj.sales.entity.User;
import com.zzkj.sales.exception.ParamException;
import com.zzkj.sales.service.ProductService;
import com.zzkj.sales.service.UserService;
import com.zzkj.sales.util.BeanValidator;
import com.zzkj.sales.util.FileUtils;
import com.zzkj.sales.util.GetUserUtil;
import com.zzkj.sales.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/productmanage")
@Slf4j
public class ProductController {

//    private final ResourceLoader resourceLoader;
//
//    @Autowired
//    public ProductController(ResourceLoader resourceLoader){
//        this.resourceLoader = resourceLoader;
//    }

    @Value("${web.upload-path}")
    private String path;

    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;

    @RequestMapping("/query")
    @ResponseBody
    @LoginToken
    public JsonData queryProduct(HttpServletRequest request, HttpServletResponse response){
        String token = request.getHeader("token");
        String url = request.getRequestURL().toString();
        url = url.substring(0, url.lastIndexOf(request.getServletPath())) + "/image/";

        //通过token获取用户名
        String username = GetUserUtil.getUserName(token);
        User operator = userService.queryByUsername(username);
        if (operator != null){
            List<Product> products = new ArrayList<>();
            //如果用户是管理员则返回所有用户信息，否则返回自己的信息
            if(operator.getRole() == 0){
                products = productService.queryByShopId(operator.getShopId());
                for (Product p : products){
                    p.setProductImg(url + p.getProductImg());
                }
//                operator.setProductList(products);
//                return JsonData.success(operator);
                return JsonData.success(products);
            }else {
                products = productService.queryByUserId(operator.getUserId());
                for (Product p : products){
                    p.setProductImg(url + p.getProductImg());
                }
//
//                operator.setProductList(products);
//                return JsonData.success(operator);
                return JsonData.success(products);

            }
        }
        return JsonData.fail("查询失败");
    }

    @RequestMapping("/create")
    @ResponseBody
    @LoginToken
    public JsonData createProduct(HttpServletRequest request, HttpServletResponse response, @RequestParam("file") MultipartFile file){
        String token = request.getHeader("token");
        String url = request.getRequestURL().toString();
        url = url.substring(0, url.lastIndexOf(request.getServletPath())) + "/image/";
        //通过token获取用户名
        String username = GetUserUtil.getUserName(token);
        User operator = userService.queryByUsername(username);
        if (operator != null){
            String productstr = request.getParameter("product");
            log.info(productstr);
            Product product = JsonMapper.string2Obj(productstr, Product.class);
            if (product == null){
                throw new ParamException("数据信息获取异常");
            }
            //验证比传字段是否为空
            BeanValidator.check(productstr);
            if(file == null){
                throw new ParamException("未获取到文件信息！");
            }
            try {
                String filename = uploadFile(file);
                product.setProductImg(filename);
                List<Product> productList = productService.insert(operator, product);
                if (productList != null){
                    for (Product p : productList){
                        p.setProductImg(url + p.getProductImg());
                    }
//                    operator.setProductList(productList);
                    return JsonData.success(productList);
                }
            }catch (Exception e){
                throw new ParamException(e);
            }
        }
        return JsonData.fail("获取信息异常，请重新登录");
    }

    @RequestMapping("/update")
    @ResponseBody
    @LoginToken
    public JsonData updateProduct(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "file", required = false) MultipartFile file){
        String token = request.getHeader("token");
        String url = request.getRequestURL().toString();
        url = url.substring(0, url.lastIndexOf(request.getServletPath())) + "/image/";
        //通过token获取用户名
        String username = GetUserUtil.getUserName(token);
        User operator = userService.queryByUsername(username);
        if (operator != null){
            String productstr = request.getParameter("product");
//            System.out.println(productstr);
//            log.info(productstr);
            Product product = JsonMapper.string2Obj(productstr, Product.class);
            if (product == null){
                throw new ParamException("数据信息获取异常");
            }
            if(file == null){
                if (product.getProductImg() == null){
                    throw new ParamException("未获取到文件信息！");
                }else {
                    product.setProductImg(product.getProductImg().substring(product.getProductImg().lastIndexOf("/") + 1));
                    List<Product> productList = productService.updateByProductId(operator,product);
                    if (productList != null){
                        for (Product p : productList){
                            p.setProductImg(url + p.getProductImg());
                        }
//                        operator.setProductList(productList);
                        return JsonData.success(productList);
                    }
                }
            }else {
                try {
                    String filename = uploadFile(file);
                    product.setProductImg(filename);
                    List<Product> productList = productService.updateByProductId(operator,product);
                    if (productList != null){
                            for (Product p : productList){
                                p.setProductImg(url + p.getProductImg());
                            }
//                        operator.setProductList(productList);
                        return JsonData.success(productList);
                    }
                }catch (Exception e){
                    throw new ParamException(e);
                }
            }
        }
        return JsonData.fail("更新失败");
    }

    @RequestMapping("/delete")
    @ResponseBody
    @LoginToken
    public JsonData updateProduct(HttpServletRequest request, HttpServletResponse response, @RequestBody(required = false) String productInfo){
        String token = request.getHeader("token");
        String url = request.getRequestURL().toString();
        url = url.substring(0, url.lastIndexOf(request.getServletPath())) + "/image/";
        //通过token获取用户名
        String username = GetUserUtil.getUserName(token);
        User operator = userService.queryByUsername(username);
        if (operator != null){
//            String productId = request.getParameter("productId");
//            System.out.println(productId);
//            log.info(productId);

            Product product = JsonMapper.string2Obj(productInfo, Product.class);
            if (product == null){
                throw new ParamException("数据信息获取异常");
            }
            System.out.println(product.toString());
//            Product product = new Product();
//            product.setProductId(Integer.valueOf(productId));
            try {
                List<Product> productList = productService.deleteByProductId(operator, product);
                if (productList != null){
                    for (Product p : productList){
                        p.setProductImg(url + p.getProductImg());
                    }
//                    operator.setProductList(productList);
                    return JsonData.success(productList);
                }
            }catch (Exception e){
                throw new ParamException("接口删除失败");
            }
        }
        return JsonData.fail("操作者空-删除失败");
    }

    private String uploadFile(MultipartFile file){
        //获取图片信息
//        String localPath="E:/image";
//        String localPath = "/home/image";
        //2获得文件名字
        String fileName=file.getOriginalFilename();
        //文件后缀名
        String str = fileName.substring(fileName.lastIndexOf("."));
        if (".jpg".equals(str) || ".png".equals(str)){
            String newFileName = FileUtils.upload(file, path, fileName);
            if(newFileName != null){
                System.out.println("上传成功");
            }else{
                System.out.println("上传失败");
                throw new ParamException("图片上传失败");
            }
            return newFileName;
        }else {
            throw new ParamException("图片格式不正确！");
        }
    }
}

package com.zzkj.sales.common;

import com.zzkj.sales.exception.ParamException;
import com.zzkj.sales.exception.TokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 捕获异常并通过ExceptionController返回客户端
 */

@Slf4j
@Component
public class SpringExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse httpServletResponse, Object o, Exception ex) {

        String url = request.getRequestURL().toString();
        System.out.println(url);
        ModelAndView mv;
        String defaultMsg = "System error";
        MappingJackson2JsonView view = new MappingJackson2JsonView();
        ParamException paramException = null;

            if (ex instanceof ParamException) {
//                JsonData result = JsonData.fail(ex.getMessage());
//                mv = new ModelAndView();
//                mv.setView(view);
//                mv.addObject(result);
                paramException = ((ParamException) ex);
            } else {
                log.error("unknown json exception, url:" + url, ex);
//                JsonData result = JsonData.fail(defaultMsg);
//                mv = new ModelAndView();
//                mv.setView(view);
//                mv.addObject(result);
                paramException = new ParamException(defaultMsg);
            }
        return null;
    }
}

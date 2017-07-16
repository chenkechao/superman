package com.keke.superman.esb.interceptor;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.keke.superman.esb.configuration.ServerConfiguration;
import com.keke.superman.esb.request.Request;
import com.keke.superman.esb.request.RequestHeader;
import com.keke.superman.esb.response.Response;
import com.keke.superman.esb.response.ResponseHeader;
import com.keke.superman.esb.util.EntityUtils;
import com.keke.superman.esb.plugins.ServerPlugin;
import org.apache.catalina.connector.ResponseFacade;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class ESBMessageInterceptor implements HandlerInterceptor {
    private static final Logger a = LoggerFactory.getLogger(ESBMessageInterceptor.class);
    private ServerConfiguration b;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception
    {
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception
    {
        HttpServletResponse localHttpServletResponse = response;
        localHttpServletResponse.setContentType("application/json");

        Object localObject1 = request.getAttribute("isAlreadyWrite");
        if (((localObject1 != null) && (Boolean.FALSE.equals(localObject1))) || (localObject1 == null))
        {
            if (!(localHttpServletResponse instanceof ResponseFacade))
            {
//                localObject2 = FieldUtils.getField(localHttpServletResponse.getClass(), "response", true).get(localHttpServletResponse);
//                localHttpServletResponse = (HttpServletResponse)FieldUtils.getField(localObject2.getClass(), "response", true).get(localObject2);
                localHttpServletResponse = (HttpServletResponse)FieldUtils.getField(localHttpServletResponse.getClass(), "response", true).get(localHttpServletResponse);
            }
            if (localHttpServletResponse.getStatus() == 404) {
                return;
            }
            Object localObject2 = localHttpServletResponse.getOutputStream().getClass();
            Field localField1 = FieldUtils.getField((Class)localObject2, "ob", true);
            Object localObject3 = localField1.get(localHttpServletResponse.getOutputStream());
            Field localField2 = FieldUtils.getField(localObject3.getClass(), "bytesWritten", true);
            Long localLong = (Long)localField2.get(localObject3);
            if (localLong.longValue() == 0L) {
                a(request, localHttpServletResponse);
            }
            request.setAttribute("isAlreadyWrite", Boolean.valueOf(true));
        }
    }

    private void a(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
            throws IOException
    {
        Request localRequest = (Request)paramHttpServletRequest.getAttribute("ESB_REQUEST");
        Response localResponse = buildResponse(localRequest);
        paramHttpServletResponse.setStatus(200);
        if ((this.b != null) && (this.b.getServerPlugins() != null) && (!this.b.getServerPlugins().isEmpty()))
        {
            //localObject = this.b.getServerPlugins();
            for (ServerPlugin localServerPlugin : (List<ServerPlugin>)this.b.getServerPlugins()) {
                localServerPlugin.beforeResponse(localResponse);
            }
        }
        Object localObject = EntityUtils.toJSONString(localResponse);
        if (a.isDebugEnabled()) {
            a.debug("ESB响应报文为：\n{}", localObject);
        }
        paramHttpServletResponse.getOutputStream().write(((String)localObject).getBytes());
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception
    {}

    public Response buildResponse(Request request)
    {
        Response localResponse = new Response();
        ResponseHeader localResponseHeader = new ResponseHeader();
        if (request != null)
        {
            RequestHeader localRequestHeader = request.getHeader();
            String str1 = localRequestHeader.getServiceCode();
            String str2 = localRequestHeader.getReqSysCode();
            localResponseHeader.setReqSysCode(str2);
            localResponseHeader.setServiceCode(str1);
            localResponseHeader.setReturnCode(2000000);
            localResponseHeader.setSerialNumber(localRequestHeader.getSerialNumber());
            localResponse.setHeader(localResponseHeader);
        }
        else
        {
            localResponseHeader.setReturnCode(5000000);
            localResponseHeader.setReturnMessage("ESB获取请求头异常，请检查服务端访问地址是否出错或服务端 是否正常启动");
            localResponse.setHeader(localResponseHeader);
        }
        localResponse.setBody(null);

        return localResponse;
    }

    public ServerConfiguration getServerConfiguration()
    {
        return this.b;
    }

    public void setServerConfiguration(ServerConfiguration serverConfiguration)
    {
        this.b = serverConfiguration;
    }
}

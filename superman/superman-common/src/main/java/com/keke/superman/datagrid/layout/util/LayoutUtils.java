package com.keke.superman.datagrid.layout.util;

import javax.servlet.http.HttpServletRequest;

import com.keke.superman.datagrid.layout.domain.GridLayout;
import com.keke.superman.security.RoleInfoCollector;
import com.keke.superman.security.UserInfoCollector;
import com.keke.superman.security.domain.IRole;
import com.keke.superman.util.ContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

public class LayoutUtils
{
    private static Logger a = LoggerFactory.getLogger(LayoutUtils.class);

    public static String getRefererEncode(HttpServletRequest request)
    {
        String str = request.getHeader("Referer");
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        int i = str.indexOf("?");
        if (i > 0) {
            str = str.substring(0, i);
        }
        Md5PasswordEncoder localMd5PasswordEncoder = new Md5PasswordEncoder();
        return localMd5PasswordEncoder.encodePassword(str, null);
    }

    public static String getPageUrlEncode(String pathname)
    {
        Md5PasswordEncoder localMd5PasswordEncoder = new Md5PasswordEncoder();
        return localMd5PasswordEncoder.encodePassword(pathname, null);
    }

    public static void setUserInfo(GridLayout gridLayout)
    {
        UserInfoCollector localUserInfoCollector = (UserInfoCollector) ContextHolder.getBean(UserInfoCollector.class);
        Object localObject = localUserInfoCollector.getUserId();
        gridLayout.setUserId(localObject);
        try
        {
            RoleInfoCollector localRoleInfoCollector = (RoleInfoCollector)ContextHolder.getBean(RoleInfoCollector.class);
            IRole localIRole = localRoleInfoCollector.getCurrentRole();
            if (localIRole != null) {
                gridLayout.setRoleId(String.valueOf(localIRole.getRoleId()));
            }
        }
        catch (Exception localException1)
        {
            a.info("没有实现RoleInfoCollector接口，在保存布局时role_id将为空");
        }
    }

    public static Object getUserId()
    {
        UserInfoCollector localUserInfoCollector = (UserInfoCollector)ContextHolder.getBean(UserInfoCollector.class);
        return localUserInfoCollector.getUserId();
    }

    public static Object getRoleId()
    {
        try
        {
            RoleInfoCollector localRoleInfoCollector = (RoleInfoCollector)ContextHolder.getBean(RoleInfoCollector.class);
            IRole localIRole = localRoleInfoCollector.getCurrentRole();
            if (localIRole != null) {
                return localIRole.getRoleId();
            }
        }
        catch (Exception localException)
        {
            a.info("没有实现RoleInfoCollector接口，在保存布局时role_id将为空");
        }
        return "";
    }
}
package com.keke.superman.web.session;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Splitter;
import java.util.Iterator;
import java.util.List;

import com.keke.superman.esb.util.JsonMapperHolder;
import com.keke.superman.security.UserInfoCollector;
import com.keke.superman.security.domain.IUser;
import com.keke.superman.util.ContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class UserInfoAttributesConverter
{
    private static final Logger logger = LoggerFactory.getLogger(UserInfoAttributesConverter.class);
    private String userInfoCollectorName;
    private List<AttrMap> attrMaps;

    public static final class AttrMap
    {
        private final String a;
        private final String b;

        public AttrMap(String readProp, String writeProp)
        {
            this.a = readProp;
            this.b = writeProp;
        }

        public AttrWriter read(IUser user)
        {
            if ((StringUtils.isNotBlank(this.a)) && (user != null))
            {
                SpelExpressionParser localSpelExpressionParser = new SpelExpressionParser();
                Object localObject;
                try
                {
                    localObject = localSpelExpressionParser.parseExpression(this.a).getValue(user);
                }
                catch (EvaluationException localEvaluationException)
                {
                    UserInfoAttributesConverter.logger.error("根据表达式获取用户属性值失败", this.a, localEvaluationException);
                    localObject = null;
                }
                catch (ParseException localParseException)
                {
                    UserInfoAttributesConverter.logger.error("根据表达式获取用户属性值失败", this.a, localParseException);
                    localObject = null;
                }
                return new AttrWriter(localObject);
            }
            return new AttrWriter(null);
        }

        final class AttrWriter
        {
            public final Object a;

            public AttrWriter(Object value)
            {
                this.a = value;
            }

            public final void writeTo(ObjectNode desc)
            {
//                if ((StringUtils.isNotBlank(UserInfoAttributesConverter.AttrMap.a(UserInfoAttributesConverter.AttrMap.this))) && (this.a != null))
//                {
//                    Iterator localIterator = Splitter.on('.').split(UserInfoAttributesConverter.AttrMap.a(UserInfoAttributesConverter.AttrMap.this)).iterator();
//                    a(localIterator, desc);
//                }
            }

            private void a(Iterator<String> paramIterator, ObjectNode paramObjectNode)
            {
                String str = (String)paramIterator.next();
                ObjectNode localObjectNode;
                if (paramIterator.hasNext())
                {
                    localObjectNode = paramObjectNode.with(str);
                    a(paramIterator, localObjectNode);
                }
                else if (paramObjectNode.has(str))
                {
                    localObjectNode = (ObjectNode) JsonMapperHolder.convert(this.a, ObjectNode.class);
                    paramObjectNode.with(str).setAll(localObjectNode);
                }
                else
                {
                    paramObjectNode.set(str, (JsonNode)JsonMapperHolder.convert(this.a, JsonNode.class));
                }
            }
        }
    }

    public final ObjectNode getUserInfoAttributes()
    {
        UserInfoCollector localUserInfoCollector = (UserInfoCollector) ContextHolder.getBean(this.userInfoCollectorName);
        ObjectNode localObjectNode = JsonMapperHolder.createObjectNode();
        IUser localIUser = localUserInfoCollector != null ? localUserInfoCollector.getCurrentLoginUser() : null;
        for (AttrMap localAttrMap : this.attrMaps) {
            localAttrMap.read(localIUser).writeTo(localObjectNode);
        }
        return localObjectNode;
    }

    public String getUserInfoCollectorName()
    {
        return this.userInfoCollectorName;
    }

    public void setUserInfoCollectorName(String userInfoCollectorName)
    {
        this.userInfoCollectorName = userInfoCollectorName;
    }

    public void setAttrMaps(List<AttrMap> attrMaps)
    {
        this.attrMaps = attrMaps;
    }
}

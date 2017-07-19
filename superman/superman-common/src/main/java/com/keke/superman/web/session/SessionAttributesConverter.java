package com.keke.superman.web.session;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Splitter;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpSession;

import com.keke.superman.esb.util.JsonMapperHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public final class SessionAttributesConverter
{
    private static final Logger logger = LoggerFactory.getLogger(SessionAttributesConverter.class);
    private List<AttrMap> b;

    public static final class AttrMap
    {
        private final String a;
        private final String b;

        public AttrMap(String readProp, String writeProp)
        {
            this.a = readProp;
            this.b = writeProp;
        }

        public AttrWriter read(HttpSession session)
        {
            if (StringUtils.isNotBlank(this.a))
            {
                String[] arrayOfString = StringUtils.split(this.a, '.');
                Object localObject = session.getAttribute(arrayOfString[0]);
                if ((localObject != null) && (arrayOfString.length > 1))
                {
                    SpelExpressionParser localSpelExpressionParser = new SpelExpressionParser();
                    String str = StringUtils.removeStart(this.a, arrayOfString[0] + '.');
                    try
                    {
                        localObject = localSpelExpressionParser.parseExpression(str).getValue(localObject);
                    }
                    catch (EvaluationException localEvaluationException)
                    {
                        SessionAttributesConverter.logger.error("根据表达式获取session值失败", str, localEvaluationException);
                        localObject = null;
                    }
                    catch (ParseException localParseException)
                    {
                        SessionAttributesConverter.logger.error("根据表达式获取session值失败", str, localParseException);
                        localObject = null;
                    }
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
//                if ((StringUtils.isNotBlank(SessionAttributesConverter.AttrMap.a(SessionAttributesConverter.AttrMap.this))) && (this.a != null))
//                {
//                    Iterator localIterator = Splitter.on('.').split(SessionAttributesConverter.AttrMap.a(SessionAttributesConverter.AttrMap.this)).iterator();
//                    a(localIterator, desc);
//                }
            }

            private final void a(Iterator<String> paramIterator, ObjectNode paramObjectNode)
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

    public final ObjectNode getSessionAttributes()
    {
        ObjectNode localObjectNode = JsonMapperHolder.createObjectNode();
        HttpSession localHttpSession = SessionManager.getSession();
        for (AttrMap localAttrMap : this.b) {
            localAttrMap.read(localHttpSession).writeTo(localObjectNode);
        }
        return localObjectNode;
    }

    public void setAttrMaps(List<AttrMap> attrMaps)
    {
        this.b = attrMaps;
    }
}

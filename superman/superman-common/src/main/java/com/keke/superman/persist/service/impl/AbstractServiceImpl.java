package com.keke.superman.persist.service.impl;

import com.keke.superman.persist.mapper.BaseMapper;
import com.keke.superman.persist.model.BaseExample;
import com.keke.superman.persist.model.BaseModel;
import com.keke.superman.persist.model.NotCountPage;
import com.keke.superman.persist.model.Page;
import com.keke.superman.persist.service.AbstractService;
import com.keke.superman.util.Reflections;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/4/9 0009.
 */
public class AbstractServiceImpl<PK extends Serializable, Model extends BaseModel, Example extends BaseExample, Mapper extends BaseMapper>
        extends AbstractService<PK, Model, Example, Mapper>
{
    public int countByExample(Example example)
    {
        return a().countByExample(example);
    }

    public int deleteByExample(Example example)
    {
        return a().deleteByExample(example);
    }

    public int deleteByPrimaryKey(PK primaryKey)
    {
        return a().deleteByPrimaryKey(primaryKey);
    }

    public Model insertSelective(Model record)
    {
        a(record);
        a().insertSelective(record);
        return record;
    }

    private void a(Model paramModel)
    {
        if (paramModel != null)
        {
            String str1 = JpaAnnotatedModelInfo.of(paramModel.getClass()).getPkFieldName();
            Reflections.getAccessibleField(paramModel, str1);
            Object localObject = Reflections.getFieldValue(paramModel, str1);
            if (localObject == null)
            {
                Field localField = FieldUtils.getField(paramModel.getClass(), str1, true);
                Generator localGenerator = (Generator)localField.getAnnotation(Generator.class);
                String str2 = localGenerator.value();
                if (StringUtils.isNotEmpty(str2))
                {
                    IGenerator localIGenerator = (IGenerator)ContextHolder.getBean(str2);
                    localObject = localIGenerator.generate(paramModel.getClass());
                    Reflections.setFieldValue(paramModel, str1, localObject);
                }
            }
        }
    }

    public List<Model> selectByExample(Example example)
    {
        return a().selectByExample(example);
    }

    public Page selectPageByExample(Example example)
    {
        Page localPage = example.getPage();
        List localList = selectByExample(example);
        Integer localInteger = Integer.valueOf(countByExample(example));
        localPage.setTotalRecord(localInteger.intValue());
        localPage.setRecords(localList);
        localPage.getTotalPage();
        return localPage;
    }

    public Model selectOneByExample(Example example)
    {
        return a().selectOneByExample(example);
    }

    public Model selectByPrimaryKey(PK primaryKey)
    {
        return a().selectByPrimaryKey(primaryKey);
    }

    public Model selectByPrimaryKeyInMasterDb(PK primaryKey)
    {
        return a().selectByPrimaryKeyInMasterDb(primaryKey);
    }

    public int updateByExampleSelective(Model record, Example example)
    {
        return a().updateByExampleSelective(record, example);
    }

    public Model updateByPrimaryKeySelective(Model record)
    {
        if (a().updateByPrimaryKeySelective(record) > 0) {
            return record;
        }
        return null;
    }

    public Model updateByPrimaryKey(Model record)
    {
        if (a().updateByPrimaryKey(record) > 0) {
            return record;
        }
        return null;
    }

    public Model save(Model record)
    {
        String str = JpaAnnotatedModelInfo.of(record.getClass()).getPkFieldName();
        Object localObject = Reflections.getFieldValue(record, str);
        if (localObject != null)
        {
            updateByPrimaryKey(record);
        }
        else
        {
            a(record);
            insertSelective(record);
        }
        return record;
    }

    public NotCountPage selectPageNotCountByExample(Example example)
    {
        Object localObject = example.getPage();
        if (localObject == null)
        {
            localObject = new NotCountPage();
            example.setPage((Page)localObject);
        }
        if (!(localObject instanceof NotCountPage)) {
            localObject = NotCountPage.wrap((Page)localObject);
        }
        Integer localInteger = Integer.valueOf(((Page)localObject).getPageSize());
        ((Page)localObject).setPageSize(localInteger.intValue() + 1);
        List localList = selectByExample(example);
        if (localList.size() == ((Page)localObject).getPageSize())
        {
            ((NotCountPage)localObject).setHasNextPage(Boolean.valueOf(true));
            localList.remove(localList.size() - 1);
        }
        else
        {
            ((NotCountPage)localObject).setHasNextPage(Boolean.valueOf(false));
        }
        ((Page)localObject).setRecords(localList);
        ((Page)localObject).setPageSize(localInteger.intValue());
        ((Page)localObject).getRecordIndex();
        return (NotCountPage)localObject;
    }
}


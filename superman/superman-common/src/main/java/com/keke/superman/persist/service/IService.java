package com.keke.superman.persist.service;


import com.keke.superman.persist.mapper.BaseMapper;
import com.keke.superman.persist.model.BaseExample;
import com.keke.superman.persist.model.BaseModel;
import com.keke.superman.persist.model.NotCountPage;
import com.keke.superman.persist.model.Page;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/4/9 0009.
 */
public abstract interface IService<PK extends Serializable, Model extends BaseModel, Example extends BaseExample, Mapper extends BaseMapper>
{
    public abstract int countByExample(Example paramExample);

    public abstract int deleteByExample(Example paramExample);

    public abstract int deleteByPrimaryKey(PK paramPK);

    public abstract Model insertSelective(Model paramModel);

    public abstract List<Model> selectByExample(Example paramExample);

    public abstract Page selectPageByExample(Example paramExample);

    public abstract Model selectOneByExample(Example paramExample);

    public abstract Model selectByPrimaryKey(PK paramPK);

    public abstract Model selectByPrimaryKeyInMasterDb(PK paramPK);

    public abstract int updateByExampleSelective(Model paramModel, Example paramExample);

    public abstract Model updateByPrimaryKeySelective(Model paramModel);

    public abstract Model updateByPrimaryKey(Model paramModel);

    public abstract Model save(Model paramModel);

    public abstract NotCountPage selectPageNotCountByExample(Example paramExample);
}



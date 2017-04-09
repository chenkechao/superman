package com.keke.superman.persist.mapper;

import com.keke.superman.persist.model.BaseExample;
import com.keke.superman.persist.model.BaseModel;
import org.apache.ibatis.annotations.Param;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/4/9 0009.
 */
public abstract interface BaseMapper<PK extends Serializable, Model extends BaseModel, Example extends BaseExample>
{
    public abstract int countByExample(Example paramExample);

    public abstract int deleteByExample(Example paramExample);

    public abstract int deleteByPrimaryKey(PK paramPK);

    public abstract int insert(Model paramModel);

    public abstract int insertSelective(Model paramModel);

    public abstract List<Model> selectByExample(Example paramExample);

    public abstract Model selectOneByExample(Example paramExample);

    public abstract Model selectByPrimaryKey(PK paramPK);

    public abstract Model selectByPrimaryKeyInMasterDb(PK paramPK);

    public abstract int updateByExampleSelective(@Param("record") Model paramModel, @Param("example") Example paramExample);

    public abstract int updateByExample(@Param("record") Model paramModel, @Param("example") Example paramExample);

    public abstract int updateByPrimaryKeySelective(Model paramModel);

    public abstract int updateByPrimaryKey(Model paramModel);
}


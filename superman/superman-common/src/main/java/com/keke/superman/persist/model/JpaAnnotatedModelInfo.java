package com.keke.superman.persist.model;

import com.keke.superman.util.Predicates2;
import com.google.common.base.CaseFormat;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableList.Builder;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class JpaAnnotatedModelInfo {
    public static final Logger logger = LoggerFactory.getLogger(JpaAnnotatedModelInfo.class);
    private String pkColumnName;
    private String pkFieldName;
    private Iterable<String> allColumnNames;
    private Iterable<String> allFieldNames;
    private Iterable<String> allColumnNamesWithoutPk;
    private Iterable<String> allFieldNamesWithoutPk;
    private String tableName;
    private String entityName;
    private Pair<String, String> tableNameEntityNamePair;//{qc_arb_complaints,ArbComplaints}元祖对象
    private Pair<String, String> pkColumnNameFieldNamePair;//{complaints_id,complaintId}元祖对象
    private Pair<String, String> versionFieldPair;//{record_version,recordVersion}元祖对象
    private Iterable<Pair<String, String>> columnNameFieldNamePairs;//所有field的的Pair包括pk
    private Iterable<Pair<String, String>> columnNameFieldNamePairsWithoutPk;
    private ImmutableBiMap<String, String> allColumnNameFieldNameMap;
    private Class<?> modelClazz;
    private final Function<Field, String>  fieldToGetterNameTransformer = new Function<Field, String>() {//未注解的字段进行转换得到get方法
        @Nullable
        public String apply(@Nullable Field columnUnAnnotatedField) {
            return "get" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, columnUnAnnotatedField.getName());
        }
    };
    private final Function<Pair<String, String>, String> toColumnNamesTransformer = new Function<Pair<String, String>, String>() {//根据pair得到相应的columnName
        @Nullable
        public String apply(@Nullable Pair<String, String> columnNameFieldNamePair) {
            return (String)columnNameFieldNamePair.getValue0();
        }
    };
    private final Function<Pair<String, String>, String> toFieldNamesTransformer = new Function<Pair<String, String>, String>() {
        @Nullable
        public String apply(@Nullable Pair<String, String> columnNameFieldNamePair) {
            return (String)columnNameFieldNamePair.getValue1();
        }
    };

    private JpaAnnotatedModelInfo() {
    }

    private JpaAnnotatedModelInfo(Class<?> modelClazz) {
        this.modelClazz = modelClazz;
        this.tableNameEntityNamePair = this.pairTableEntity(modelClazz);
        this.tableName = (String)this.tableNameEntityNamePair.getValue0();
        this.entityName = (String)this.tableNameEntityNamePair.getValue1();
        this.pkColumnNameFieldNamePair = this.pairIdField(modelClazz);
        this.pkColumnName = (String)this.pkColumnNameFieldNamePair.getValue0();
        this.pkFieldName = (String)this.pkColumnNameFieldNamePair.getValue1();
        this.versionFieldPair = this.pairVersionField(modelClazz);
        this.columnNameFieldNamePairsWithoutPk = this.columnNameFieldNamePairWithoutPk(modelClazz);
        this.allColumnNamesWithoutPk = Iterables.transform(this.columnNameFieldNamePairsWithoutPk, this.toColumnNamesTransformer);
        this.allFieldNamesWithoutPk = Iterables.transform(this.columnNameFieldNamePairsWithoutPk, this.toFieldNamesTransformer);
        this.columnNameFieldNamePairs = this.columnNameFieldNamePair();
        this.allColumnNames = Iterables.transform(this.columnNameFieldNamePairs, this.toColumnNamesTransformer);
        this.allFieldNames = Iterables.transform(this.columnNameFieldNamePairs, this.toFieldNamesTransformer);
        this.allColumnNameFieldNameMap = this.allColumnNameFieldNameMap();
    }

    public static JpaAnnotatedModelInfo of(Class<?> modelClazz) {
        return new JpaAnnotatedModelInfo(modelClazz);
    }

    public JpaAnnotatedModelInfo excludeAnyFields(final Iterable<String> fieldNames) {
        JpaAnnotatedModelInfo var2 = new JpaAnnotatedModelInfo();
        var2.modelClazz = this.modelClazz;
        var2.tableNameEntityNamePair = this.tableNameEntityNamePair;
        var2.tableName = this.tableName;
        var2.entityName = this.entityName;
        var2.pkColumnNameFieldNamePair = this.pkColumnNameFieldNamePair;
        var2.pkColumnName = this.pkColumnName;
        var2.pkFieldName = this.pkFieldName;
        var2.versionFieldPair = this.versionFieldPair;
        var2.columnNameFieldNamePairsWithoutPk = Iterables.filter(this.columnNameFieldNamePairsWithoutPk, new Predicate<Pair<String, String>>() {
            public boolean apply(@Nullable Pair<String, String> input) {
                String var2 = (String)input.getValue1();
                return JpaAnnotatedModelInfo.this.hasVersionField() && var2.equals(JpaAnnotatedModelInfo.this.getVersionFieldName())?true:!Iterables.contains(fieldNames, var2);
            }
        });
        var2.allColumnNamesWithoutPk = Iterables.transform(var2.columnNameFieldNamePairsWithoutPk, this.toColumnNamesTransformer);
        var2.allFieldNamesWithoutPk = Iterables.transform(var2.columnNameFieldNamePairsWithoutPk, this.toFieldNamesTransformer);
        var2.columnNameFieldNamePairs = var2.columnNameFieldNamePair();
        var2.allColumnNames = Iterables.transform(var2.columnNameFieldNamePairs, this.toColumnNamesTransformer);
        var2.allFieldNames = Iterables.transform(var2.columnNameFieldNamePairs, this.toFieldNamesTransformer);
        var2.allColumnNameFieldNameMap = var2.allColumnNameFieldNameMap();
        return var2;
    }

    private ImmutableList<Pair<String, String>> columnNameFieldNamePair() {
        Builder var1 = ImmutableList.builder();
        var1.add(Pair.with(this.pkColumnName, this.pkFieldName));
        var1.addAll(this.columnNameFieldNamePairsWithoutPk);
        return var1.build();
    }

    private Pair<String, String> pairTableEntity(Class<?> var1) {
        if(var1.isAnnotationPresent(Table.class)) {//该target是否被某个annotation修饰
            String var2 = ((Table)var1.getAnnotation(Table.class)).name();//注解Table里面的name值
            String var3 = ClassUtils.getShortClassName(var1);//类名的简称
            return Pair.with(var2, var3);
        } else {
            logger.error("Model: {} 没有未被 javax.persistence.Table 注解， 可能不符合预期。", ClassUtils.getSimpleName(var1));
            return null;
        }
    }

    private Pair<String, String> pairIdField(Class<?> var1) {
        List var2 = FieldUtils.getAllFieldsList(var1);//获取所有field
        ArrayList var3 = Lists.newArrayList(var1.getMethods());//获取所有方法
        Optional var4 = Iterables.tryFind(var2, Predicates.or(Predicates2.isAnnotationPresent(Id.class), this.pairTableEntity((Class)Id.class, (Iterable)var3)));
        if(var4.isPresent()) {
            Field var5 = (Field)var4.get();
            return this.pairColumnField(var5);
        } else {
            logger.warn("Model: {} 没有javax.persistence.Id 字段信息， 可能不符合预期。", ClassUtils.getSimpleName(var1));
            return null;
        }
    }

    private Pair<String, String> pairVersionField(Class<?> var1) {
        List var2 = FieldUtils.getAllFieldsList(var1);
        ArrayList var3 = Lists.newArrayList(var1.getMethods());
        Optional var4 = Iterables.tryFind(var2, Predicates.or(Predicates2.isAnnotationPresent(Version.class), this.pairTableEntity((Class)Version.class, (Iterable)var3)));
        if(var4.isPresent()) {
            Field var5 = (Field)var4.get();
            return this.pairColumnField(var5);
        } else {
            return null;
        }
    }

    //Predicates.compose 将Predicate和Function进行组合。将Function的结果作为Predicate的输入，然后进行判断过滤操作。
    private Predicate<Field> pairTableEntity(final Class<? extends Annotation> var1, final Iterable<Method> var2) {
        return Predicates.compose(new Predicate<String>() {
            public boolean apply(@Nullable String getterName) {
                return Iterables.any(var2, Predicates.and(Predicates2.isAnnotationPresent(var1), Predicates.compose(Predicates.equalTo(getterName), new Function<Method,String>() {
                    @Nullable
                    public String apply(@Nullable Method modelMethod) {
                        return modelMethod.getName();
                    }
                })));
            }
        }, this.fieldToGetterNameTransformer);
    }

    private ImmutableList<Pair<String, String>> columnNameFieldNamePairWithoutPk(Class<?> var1) {
        return ImmutableList.copyOf(Iterables.transform(this.fieldIterableWithoutPk(var1), new Function<Field,Pair<String, String>>() {
            @Nullable
            public Pair<String, String> apply(@Nullable Field columnAnnotatedField) {
                return JpaAnnotatedModelInfo.this.pairColumnField(columnAnnotatedField);
            }
        }));
    }

    private Iterable<Field> fieldIterableWithoutPk(Class<?> var1) {//不含pk的Field Iterable对象
        final ArrayList var2 = Lists.newArrayList(var1.getMethods());
        return ImmutableList.copyOf(Iterables.filter(FieldUtils.getAllFieldsList(var1), Predicates.or(Predicates.and(Predicates.not(Predicates2.isAnnotationPresent(Id.class)), Predicates2.isAnnotationPresent(Column.class)), Predicates.compose(new Predicate<String>() {
            public boolean apply(@Nullable String getterName) {
                return Iterables.any(var2, Predicates.and(Predicates.and(Predicates.not(Predicates2.isAnnotationPresent(Id.class)), Predicates2.isAnnotationPresent(Column.class)), Predicates.compose(Predicates.equalTo(getterName), new Function<Method,String>() {
                    @Nullable
                    public String apply(@Nullable Method modelMethod) {
                        return modelMethod.getName();
                    }
                })));
            }
        }, this.fieldToGetterNameTransformer))));
    }

    private Pair<String, String> pairColumnField(Field var1) {
        String var2 = var1.getName();
        if(var1.isAnnotationPresent(Column.class)) {
            String var3 = ((Column)var1.getAnnotation(Column.class)).name();
            return Pair.with(var3, var2);
        } else {
            return Pair.with(CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, var2), var2);
        }
    }

    private ImmutableBiMap<String, String> allColumnNameFieldNameMap() {
        com.google.common.collect.ImmutableBiMap.Builder var1 = ImmutableBiMap.builder();
        Iterator var2 = this.columnNameFieldNamePairs.iterator();

        while(var2.hasNext()) {
            Pair var3 = (Pair)var2.next();
            var1.put(Maps.immutableEntry(var3.getValue0(), var3.getValue1()));
        }

        return var1.build();
    }

    public Pair<String, String> getPkColumnNameFieldNamePair() {
        return this.pkColumnNameFieldNamePair;
    }

    public String getPkColumnName() {
        return this.pkColumnName;
    }

    public String getPkFieldName() {
        return this.pkFieldName;
    }

    public Iterable<String> getAllColumnNames() {
        return this.allColumnNames;
    }

    public Iterable<String> getAllFieldNames() {
        return this.allFieldNames;
    }

    public Iterable<String> getAllColumnNamesWithoutPk() {
        return this.allColumnNamesWithoutPk;
    }

    public Iterable<String> getAllFieldNamesWithoutPk() {
        return this.allFieldNamesWithoutPk;
    }

    public String getTableName() {
        return this.tableName;
    }

    public String getEntityName() {
        return this.entityName;
    }

    public String getFieldNameByColumnName(String columnName) {
        return (String)this.allColumnNameFieldNameMap.get(columnName);
    }

    public String getColumnNameByFieldName(String fieldName) {
        return (String)this.allColumnNameFieldNameMap.inverse().get(fieldName);
    }

    public Field getPkField() {
        return StringUtils.isNotEmpty(this.pkFieldName)?FieldUtils.getField(this.modelClazz, this.pkFieldName, true):null;
    }

    public boolean hasVersionField() {
        return this.versionFieldPair != null;
    }

    public boolean isVersionColumnName(String columnName) {
        return this.hasVersionField() && StringUtils.equals(columnName, this.getVersionColumnName());
    }

    public boolean isVersionFieldName(String columnName) {
        return this.hasVersionField() && StringUtils.equals(columnName, this.getVersionFieldName());
    }

    public String getVersionFieldName() {
        return (String)this.versionFieldPair.getValue1();
    }

    public String getVersionColumnName() {
        return (String)this.versionFieldPair.getValue0();
    }

    public Optional<Field> getPkFieldOpt() {
        Field var1 = this.getPkField();
        //return var1 != null?Optional.of(var1):Optional.absent();
        //TODO 三元写法
        if(var1 != null){
            return Optional.of(var1);
        }else{
            return Optional.absent();
        }
    }

    public Class<?> getModelClazz() {
        return this.modelClazz;
    }

    public String toString() {
        return (new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)).append("pkColumnName", this.pkColumnName).append("pkFieldName", this.pkFieldName).append("allColumnNames", this.allColumnNames).append("allFieldNames", this.allFieldNames).append("allColumnNamesWithoutPk", this.allColumnNamesWithoutPk).append("allFieldNamesWithoutPk", this.allFieldNamesWithoutPk).append("tableName", this.tableName).append("entityName", this.entityName).append("tableNameEntityNamePair", this.tableNameEntityNamePair).append("pkColumnNameFieldNamePair", this.pkColumnNameFieldNamePair).append("columnNameFieldNamePairs", this.columnNameFieldNamePairs).append("columnNameFieldNamePairsWithoutPk", this.columnNameFieldNamePairsWithoutPk).append("allColumnNameFieldNameMap", this.allColumnNameFieldNameMap).append("modelClazz", this.modelClazz).append("fieldToGetterNameTransformer", this.fieldToGetterNameTransformer).append("toColumnNamesTransformer", this.toColumnNamesTransformer).append("toFieldNamesTransformer", this.toFieldNamesTransformer).toString();
    }
}

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
    private Pair<String, String> tableEntityPair;//{qc_arb_complaints,ArbComplaints}元祖对象
    private Pair<String, String> idFieldPair;//{complaints_id,complaintId}元祖对象
    private Pair<String, String> versionFieldPair;//{record_version,recordVersion}元祖对象
    private Iterable<Pair<String, String>> l;
    private Iterable<Pair<String, String>> m;
    private ImmutableBiMap<String, String> n;
    private Class<?> modelClazz;
    private final Function<Field, String> p = new Function<Field, String>() {
        @Nullable
        public String apply(@Nullable Field columnUnAnnotatedField) {
            return "get" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, columnUnAnnotatedField.getName());
        }
    };
    private final Function<Pair<String, String>, String> q = new Function<Pair<String, String>, String>() {
        @Nullable
        public String apply(@Nullable Pair<String, String> columnNameFieldNamePair) {
            return (String)columnNameFieldNamePair.getValue0();
        }
    };
    private final Function<Pair<String, String>, String> r = new Function<Pair<String, String>, String>() {
        @Nullable
        public String apply(@Nullable Pair<String, String> columnNameFieldNamePair) {
            return (String)columnNameFieldNamePair.getValue1();
        }
    };

    private JpaAnnotatedModelInfo() {
    }

    private JpaAnnotatedModelInfo(Class<?> modelClazz) {
        this.modelClazz = modelClazz;
        this.tableEntityPair = this.pairTableEntity(modelClazz);
        this.tableName = (String)this.tableEntityPair.getValue0();
        this.entityName = (String)this.tableEntityPair.getValue1();
        this.idFieldPair = this.pairIdField(modelClazz);
        this.a = (String)this.j.getValue0();
        this.b = (String)this.j.getValue1();
        this.versionFieldPair = this.pairVersionField(modelClazz);
        this.m = this.d(modelClazz);
        this.e = Iterables.transform(this.m, this.q);
        this.f = Iterables.transform(this.m, this.r);
        this.l = this.a();
        this.c = Iterables.transform(this.l, this.q);
        this.d = Iterables.transform(this.l, this.r);
        this.n = this.b();
    }

    public static JpaAnnotatedModelInfo of(Class<?> modelClazz) {
        return new JpaAnnotatedModelInfo(modelClazz);
    }

    public JpaAnnotatedModelInfo excludeAnyFields(final Iterable<String> fieldNames) {
        JpaAnnotatedModelInfo var2 = new JpaAnnotatedModelInfo();
        var2.o = this.o;
        var2.i = this.i;
        var2.g = this.g;
        var2.h = this.h;
        var2.j = this.j;
        var2.a = this.a;
        var2.b = this.b;
        var2.k = this.k;
        var2.m = Iterables.filter(this.m, new Predicate<Pair<String, String>>() {
            public boolean apply(@Nullable Pair<String, String> input) {
                String var2 = (String)input.getValue1();
                return JpaAnnotatedModelInfo.this.hasVersionField() && var2.equals(JpaAnnotatedModelInfo.this.getVersionFieldName())?true:!Iterables.contains(fieldNames, var2);
            }
        });
        var2.e = Iterables.transform(var2.m, this.q);
        var2.f = Iterables.transform(var2.m, this.r);
        var2.l = var2.a();
        var2.c = Iterables.transform(var2.l, this.q);
        var2.d = Iterables.transform(var2.l, this.r);
        var2.n = var2.b();
        return var2;
    }

    private ImmutableList<Pair<String, String>> pairTableEntity() {
        Builder var1 = ImmutableList.builder();
        var1.add(Pair.with(this.pkColumnName, this.pkFieldName));
        var1.addAll(this.m);
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
        Optional var4 = Iterables.tryFind(var2, Predicates.or(Predicates2.isAnnotationPresent(Id.class), this.a((Class)Id.class, (Iterable)var3)));
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
        return Predicates.compose(new Predicate() {
            public boolean apply(@Nullable String getterName) {
                return Iterables.any(var2, Predicates.and(Predicates2.isAnnotationPresent(var1), Predicates.compose(Predicates.equalTo(getterName), new Function() {
                    @Nullable
                    public String apply(@Nullable Method modelMethod) {
                        return modelMethod.getName();
                    }
                })));
            }
        }, this.p);
    }

    private ImmutableList<Pair<String, String>> d(Class<?> var1) {
        return ImmutableList.copyOf(Iterables.transform(this.e(var1), new Function<Field,Pair<String, String>>() {
            @Nullable
            public Pair<String, String> apply(@Nullable Field columnAnnotatedField) {
                return JpaAnnotatedModelInfo.this.a(columnAnnotatedField);
            }
        }));
    }

    private Iterable<Field> e(Class<?> var1) {
        final ArrayList var2 = Lists.newArrayList(var1.getMethods());
        return ImmutableList.copyOf(Iterables.filter(FieldUtils.getAllFieldsList(var1), Predicates.or(Predicates.and(Predicates.not(Predicates2.isAnnotationPresent(Id.class)), Predicates2.isAnnotationPresent(Column.class)), Predicates.compose(new Predicate() {
            public boolean apply(@Nullable String getterName) {
                return Iterables.any(var2, Predicates.and(Predicates.and(Predicates.not(Predicates2.isAnnotationPresent(Id.class)), Predicates2.isAnnotationPresent(Column.class)), Predicates.compose(Predicates.equalTo(getterName), new Function() {
                    @Nullable
                    public String apply(@Nullable Method modelMethod) {
                        return modelMethod.getName();
                    }
                })));
            }
        }, this.p))));
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

    private ImmutableBiMap<String, String> b() {
        com.google.common.collect.ImmutableBiMap.Builder var1 = ImmutableBiMap.builder();
        Iterator var2 = this.l.iterator();

        while(var2.hasNext()) {
            Pair var3 = (Pair)var2.next();
            var1.put(Maps.immutableEntry(var3.getValue0(), var3.getValue1()));
        }

        return var1.build();
    }

    public Pair<String, String> getPkColumnNameFieldNamePair() {
        return this.j;
    }

    public String getPkColumnName() {
        return this.a;
    }

    public String getPkFieldName() {
        return this.b;
    }

    public Iterable<String> getAllColumnNames() {
        return this.c;
    }

    public Iterable<String> getAllFieldNames() {
        return this.d;
    }

    public Iterable<String> getAllColumnNamesWithoutPk() {
        return this.e;
    }

    public Iterable<String> getAllFieldNamesWithoutPk() {
        return this.f;
    }

    public String getTableName() {
        return this.g;
    }

    public String getEntityName() {
        return this.h;
    }

    public String getFieldNameByColumnName(String columnName) {
        return (String)this.n.get(columnName);
    }

    public String getColumnNameByFieldName(String fieldName) {
        return (String)this.n.inverse().get(fieldName);
    }

    public Field getPkField() {
        return StringUtils.isNotEmpty(this.b)?FieldUtils.getField(this.o, this.b, true):null;
    }

    public boolean hasVersionField() {
        return this.k != null;
    }

    public boolean isVersionColumnName(String columnName) {
        return this.hasVersionField() && StringUtils.equals(columnName, this.getVersionColumnName());
    }

    public boolean isVersionFieldName(String columnName) {
        return this.hasVersionField() && StringUtils.equals(columnName, this.getVersionFieldName());
    }

    public String getVersionFieldName() {
        return (String)this.k.getValue1();
    }

    public String getVersionColumnName() {
        return (String)this.k.getValue0();
    }

    public Optional<Field> getPkFieldOpt() {
        Field var1 = this.getPkField();
        return var1 != null?Optional.of(var1):Optional.absent();
    }

    public Class<?> getModelClazz() {
        return this.o;
    }

    public String toString() {
        return (new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)).append("pkColumnName", this.a).append("pkFieldName", this.b).append("allColumnNames", this.c).append("allFieldNames", this.d).append("allColumnNamesWithoutPk", this.e).append("allFieldNamesWithoutPk", this.f).append("tableName", this.g).append("entityName", this.h).append("tableNameEntityNamePair", this.i).append("pkColumnNameFieldNamePair", this.j).append("columnNameFieldNamePairs", this.l).append("columnNameFieldNamePairsWithoutPk", this.m).append("allColumnNameFieldNameMap", this.n).append("modelClazz", this.o).append("fieldToGetterNameTransformer", this.p).append("toColumnNamesTransformer", this.q).append("toFieldNamesTransformer", this.r).toString();
    }
}

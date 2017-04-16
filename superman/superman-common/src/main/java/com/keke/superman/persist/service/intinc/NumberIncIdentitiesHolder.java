//package com.keke.superman.persist.service.intinc;
//
//import com.google.common.cache.Cache;
//import com.google.common.cache.CacheBuilder;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.text.MessageFormat;
//import java.util.concurrent.Callable;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.atomic.AtomicLong;
//import java.util.concurrent.locks.ReentrantLock;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.dao.DataAccessException;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.StatementCallback;
//
//public class NumberIncIdentitiesHolder
//{
//    private JdbcTemplate a;
//    private int b = 1000;
//    private String c = "c_ids_default";
//    private String d = "MAPPING_CODE";
//    private String e = "LAST_ID";
//    private String f = "REGION_SIZE";
//    private DBType g = DBType.MYSQL;
//
//    public void setDbType(DBType dbType)
//    {
//        this.g = dbType;
//    }
//
//    private ReentrantLock h = new ReentrantLock(true);
//    private static final Logger i = LoggerFactory.getLogger(NumberIncIdentities.class);
//    private Cache<String, NumberIncIdentities> j = CacheBuilder.newBuilder().build();
//
//    public NumberIncIdentities getByMappingCode(final String pkMappingCode)
//            throws ExecutionException
//    {
//        (NumberIncIdentities)this.j.get(pkMappingCode, new Callable()
//        {
//            public NumberIncIdentitiesHolder.NumberIncIdentities call()
//                    throws Exception
//            {
//                return new NumberIncIdentitiesHolder.NumberIncIdentities(NumberIncIdentitiesHolder.this, pkMappingCode);
//            }
//        });
//    }
//
//    public void setDefaultIncTableName(String defaultIncTableName)
//    {
//        this.c = defaultIncTableName;
//    }
//
//    public void setPkCodeMappingColumnName(String pkCodeMappingColumnName)
//    {
//        this.d = pkCodeMappingColumnName;
//    }
//
//    public void setLastIdColumnName(String lastIdColumnName)
//    {
//        this.e = lastIdColumnName;
//    }
//
//    public void setRegionSizeColumnName(String regionSizeColumnName)
//    {
//        this.f = regionSizeColumnName;
//    }
//
//    public void setJdbcTemplate(JdbcTemplate jdbcTemplate)
//    {
//        this.a = jdbcTemplate;
//    }
//
//    public void setDefaultRegionSize(int defaultRegionSize)
//    {
//        this.b = defaultRegionSize;
//    }
//
//    public class NumberIncIdentities
//            implements IncrementIdentities<Number>
//    {
//        private AtomicLong b = new AtomicLong(0L);
//        private Number c = Integer.valueOf(0);
//        private Number d = Integer.valueOf(0);
//        private final String e;
//        private int f = 0;
//        private StatementCallback<Number> g = new StatementCallback()
//        {
//            public Number doInStatement(Statement statement)
//                    throws SQLException, DataAccessException
//            {
//                long l1 = NumberIncIdentitiesHolder.NumberIncIdentities.a(NumberIncIdentitiesHolder.NumberIncIdentities.this).longValue();
//
//                long l2 = 1L;
//                try
//                {
//                    String str;
//                    ResultSet localResultSet;
//                    if (NumberIncIdentitiesHolder.NumberIncIdentities.b(NumberIncIdentitiesHolder.NumberIncIdentities.this) == 0)
//                    {
//                        str = "SELECT " + NumberIncIdentitiesHolder.a(NumberIncIdentitiesHolder.this) + " from " + NumberIncIdentitiesHolder.b(NumberIncIdentitiesHolder.this) + " WHERE " + NumberIncIdentitiesHolder.c(NumberIncIdentitiesHolder.this) + " = '" + NumberIncIdentitiesHolder.NumberIncIdentities.c(NumberIncIdentitiesHolder.NumberIncIdentities.this) + "'";
//                        localResultSet = statement.executeQuery(str);
//                        if (localResultSet.next()) {
//                            NumberIncIdentitiesHolder.NumberIncIdentities.a(NumberIncIdentitiesHolder.NumberIncIdentities.this, localResultSet.getInt(1));
//                        } else {
//                            NumberIncIdentitiesHolder.NumberIncIdentities.a(NumberIncIdentitiesHolder.NumberIncIdentities.this, NumberIncIdentitiesHolder.d(NumberIncIdentitiesHolder.this));
//                        }
//                        localResultSet.close();
//                    }
//                    while (statement.executeUpdate(NumberIncIdentitiesHolder.NumberIncIdentities.a(NumberIncIdentitiesHolder.NumberIncIdentities.this, Long.valueOf(l1))) < 1)
//                    {
//                        str = "SELECT " + NumberIncIdentitiesHolder.e(NumberIncIdentitiesHolder.this) + " from " + NumberIncIdentitiesHolder.b(NumberIncIdentitiesHolder.this) + " WHERE " + NumberIncIdentitiesHolder.c(NumberIncIdentitiesHolder.this) + " = '" + NumberIncIdentitiesHolder.NumberIncIdentities.c(NumberIncIdentitiesHolder.NumberIncIdentities.this) + "'";
//                        localResultSet = statement.executeQuery(str);
//                        localResultSet.next();
//                        l1 = localResultSet.getLong(1);
//                        if (NumberIncIdentitiesHolder.a().isDebugEnabled()) {
//                            NumberIncIdentitiesHolder.a().debug(MessageFormat.format("����������������{0}��������������{1}����������", new Object[] { Long.valueOf(l1), Long.valueOf(l2++) }));
//                        }
//                        localResultSet.close();
//                    }
//                }
//                catch (Exception localException1)
//                {
//                    throw new RuntimeException("������������������������" + l1, localException1);
//                }
//                return Long.valueOf(++l1);
//            }
//        };
//
//        public NumberIncIdentities(String mappingCode)
//        {
//            this.e = mappingCode;
//        }
//
//        private String a(Long paramLong)
//        {
//            return "UPDATE " + NumberIncIdentitiesHolder.b(NumberIncIdentitiesHolder.this) + " SET " + NumberIncIdentitiesHolder.e(NumberIncIdentitiesHolder.this) + " = " + NumberIncIdentitiesHolder.e(NumberIncIdentitiesHolder.this) + " + " + 1 + " WHERE " + NumberIncIdentitiesHolder.c(NumberIncIdentitiesHolder.this) + "='" + this.e + "' AND " + NumberIncIdentitiesHolder.e(NumberIncIdentitiesHolder.this) + " = " + paramLong;
//        }
//
//        public synchronized Number get()
//        {
//            Long localLong = Long.valueOf(this.b.getAndIncrement());
//            if (localLong.longValue() >= this.d.longValue()) {
//                try
//                {
//                    NumberIncIdentitiesHolder.f(NumberIncIdentitiesHolder.this).lock();
//                    if (localLong.longValue() >= this.d.longValue())
//                    {
//                        if (NumberIncIdentitiesHolder.a().isDebugEnabled()) {
//                            NumberIncIdentitiesHolder.a().debug("������������{}������������������{}��������������������", localLong, this.d);
//                        }
//                        a();
//                        if (NumberIncIdentitiesHolder.a().isDebugEnabled()) {
//                            NumberIncIdentitiesHolder.a().debug(MessageFormat.format("��������������������������{0},����������{1}������������������{2}", new Object[] { this.c,
//
//                                    Long.valueOf(this.b.get()), this.d }));
//                        }
//                    }
//                    localLong = Long.valueOf(this.b.getAndIncrement());
//                }
//                catch (Exception localException)
//                {
//                    throw new RuntimeException("������ID����", localException);
//                }
//                finally
//                {
//                    NumberIncIdentitiesHolder.f(NumberIncIdentitiesHolder.this).unlock();
//                }
//            }
//            if (NumberIncIdentitiesHolder.a().isDebugEnabled()) {
//                NumberIncIdentitiesHolder.a().debug("������������ID����{}", localLong);
//            }
//            return localLong;
//        }
//
//        private boolean a()
//        {
//            Number localNumber = (Number)NumberIncIdentitiesHolder.g(NumberIncIdentitiesHolder.this).execute(this.g);
//            this.c = localNumber;
//            this.b = new AtomicLong(this.c.longValue() * this.f);
//            this.d = Long.valueOf((this.c.longValue() + 1L) * this.f);
//            return true;
//        }
//    }
//}
//

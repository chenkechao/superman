package com.keke.superman.datagrid.layout.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.keke.superman.datagrid.layout.domain.GridLayout;
import com.keke.superman.datagrid.layout.service.GridLayoutService;
import com.keke.superman.datagrid.layout.util.LayoutUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

public class GridLayoutServiceImpl
        implements GridLayoutService, ApplicationContextAware
{
    private JdbcTemplate a;
    private String b;

    public GridLayout saveOrUpdateLayout(GridLayout gridLayout)
    {
        String str1 = gridLayout.getTableId();
        String str2 = gridLayout.getName();
//        int i = getLayoutCountByName(str1, str2);
//        if (i == 0) {
//            return saveLayout(gridLayout);
//        }
        return updateLayout(gridLayout);
    }

    public GridLayout saveLayout(final GridLayout gridLayout)
    {
        LayoutUtils.setUserInfo(gridLayout);
        final String str = "INSERT INTO " + this.b + " (SYS_GRID_LAYOUT_ID,USER_ID,ROLE_ID,TABLE_ID,CONTENT,NAME,MODIFY_TIME) VALUES (?,?,?,?,?,?,?)";
        this.a.update(new PreparedStatementCreator()
        {
            public PreparedStatement createPreparedStatement(Connection con)
                    throws SQLException
            {
                PreparedStatement localPreparedStatement = con.prepareStatement(str);
                //String str = (String)IdMaker.get(GridLayout.class);
                localPreparedStatement.setString(1, str);
                localPreparedStatement.setString(2, String.valueOf(gridLayout.getUserId()));
                localPreparedStatement.setString(3, gridLayout.getRoleId());
                localPreparedStatement.setString(4, gridLayout.getTableId());
                localPreparedStatement.setString(5, gridLayout.getContent());
                localPreparedStatement.setString(6, gridLayout.getName());
                Timestamp localTimestamp = new Timestamp(new Date().getTime());
                localPreparedStatement.setTimestamp(7, localTimestamp);
                return localPreparedStatement;
            }
        });
        return gridLayout;
    }

    public GridLayout updateLayout(final GridLayout gridLayout)
    {
        LayoutUtils.setUserInfo(gridLayout);
        String str = "UPDATE " + this.b + " set CONTENT=?,MODIFY_TIME=? WHERE  USER_ID=? AND ROLE_ID=? AND TABLE_ID=? AND NAME=?";
        this.a.update(str, new PreparedStatementSetter()
        {
            public void setValues(PreparedStatement ps)
                    throws SQLException
            {
                ps.setString(1, gridLayout.getContent());
                Timestamp localTimestamp = new Timestamp(new Date().getTime());
                ps.setTimestamp(2, localTimestamp);
                ps.setString(3, String.valueOf(gridLayout.getUserId()));
                ps.setString(4, gridLayout.getRoleId());
                ps.setString(5, gridLayout.getTableId());
                ps.setString(6, gridLayout.getName());
            }
        });
        return gridLayout;
    }

    public List<GridLayout> findAllLayout(final String tableId)
    {
        final Object localObject1 = LayoutUtils.getUserId();
        final Object localObject2 = LayoutUtils.getRoleId();
        String str = " SELECT A.SYS_GRID_LAYOUT_ID,A.USER_ID,A.ROLE_ID,A.TABLE_ID,A.CONTENT,A.NAME,A.MODIFY_TIME FROM " + this.b + " A WHERE A.USER_ID=? AND A.ROLE_ID=? AND A.TABLE_ID=? ";
//        this.a.query(str, new PreparedStatementSetter()

//        new GridLayoutMapper
//        {
//            public void setValues(PreparedStatement ps)
//          throws SQLException
//            {
//                ps.setString(1, String.valueOf(localObject1));
//                ps.setString(2, String.valueOf(localObject2));
//                ps.setString(3, tableId);
//            }
//        }, new GridLayoutMapper());
                return null;
    }

    public GridLayout findLastLayout(final String tableId)
    {
        final Object localObject1 = LayoutUtils.getUserId();
        final Object localObject2 = LayoutUtils.getRoleId();
        String str = " SELECT A.SYS_GRID_LAYOUT_ID,A.USER_ID,A.ROLE_ID,A.TABLE_ID,A.CONTENT,A.NAME,A.MODIFY_TIME FROM " + this.b + " A WHERE A.USER_ID=? AND A.ROLE_ID=? AND A.TABLE_ID=? " + " AND A.MODIFY_TIME=(SELECT MAX(MODIFY_TIME) FROM " + this.b + " B WHERE A.USER_ID=B.USER_ID AND A.ROLE_ID=B.ROLE_ID AND A.TABLE_ID=B.TABLE_ID) ";

//        (GridLayout)this.a.query(str, new PreparedStatementSetter()
//
//        new ResultSetExtractor
//        {
//            public void setValues(PreparedStatement ps)
//          throws SQLException
//            {
//                ps.setString(1, String.valueOf(localObject1));
//                ps.setString(2, String.valueOf(localObject2));
//                ps.setString(3, tableId);
//            }
//        }, new ResultSetExtractor()
//    {
//        public GridLayout extractData(ResultSet rs)
//                throws SQLException, DataAccessException
//        {
//            if (rs.next())
//            {
//                GridLayout localGridLayout = new GridLayout();
//                localGridLayout.setGridLayoutId(rs.getString("SYS_GRID_LAYOUT_ID"));
//                localGridLayout.setContent(rs.getString("CONTENT"));
//                localGridLayout.setName(rs.getString("NAME"));
//                localGridLayout.setRoleId(rs.getString("ROLE_ID"));
//                localGridLayout.setTableId(rs.getString("TABLE_ID"));
//                localGridLayout.setUserId(rs.getString("USER_ID"));
//                localGridLayout.setModify(rs.getTimestamp("MODIFY_TIME"));
//                return localGridLayout;
//            }
            return null;
//        }
//    });
    }

    public GridLayout findLayoutByName(final String tableId, final String name)
    {
        final Object localObject1 = LayoutUtils.getUserId();
        final Object localObject2 = LayoutUtils.getRoleId();
        String str = "SELECT DISTINCT SYS_GRID_LAYOUT_ID,USER_ID,ROLE_ID,TABLE_ID,CONTENT,NAME,MODIFY_TIME FROM " + this.b + " WHERE USER_ID=? AND ROLE_ID=? AND TABLE_ID=? AND NAME=? ";
//        (GridLayout)this.a.query(str, new PreparedStatementSetter()
//
//        new ResultSetExtractor
//        {
//            public void setValues(PreparedStatement ps)
//          throws SQLException
//            {
//                ps.setString(1, String.valueOf(localObject1));
//                ps.setString(2, String.valueOf(localObject2));
//                ps.setString(3, tableId);
//                ps.setString(4, name);
//            }
//        }, new ResultSetExtractor()
//    {
//        public GridLayout extractData(ResultSet rs)
//                throws SQLException, DataAccessException
//        {
//            if (rs.next())
//            {
//                GridLayout localGridLayout = new GridLayout();
//                localGridLayout.setGridLayoutId(rs.getString("SYS_GRID_LAYOUT_ID"));
//                localGridLayout.setContent(rs.getString("CONTENT"));
//                localGridLayout.setName(rs.getString("NAME"));
//                localGridLayout.setRoleId(rs.getString("ROLE_ID"));
//                localGridLayout.setTableId(rs.getString("TABLE_ID"));
//                localGridLayout.setUserId(rs.getString("USER_ID"));
//                localGridLayout.setModify(rs.getTimestamp("MODIFY_TIME"));
//                return localGridLayout;
//            }
            return null;
//        }
//    });
    }

    public void deleteLayout(final String tableId)
    {
        final Object localObject1 = LayoutUtils.getUserId();
        final Object localObject2 = LayoutUtils.getRoleId();
        String str = "DELETE FROM " + this.b + " WHERE USER_ID=? AND ROLE_ID=? AND TABLE_ID=?";
        this.a.update(str, new PreparedStatementSetter()
        {
            public void setValues(PreparedStatement ps)
                    throws SQLException
            {
                ps.setString(1, String.valueOf(localObject1));
                ps.setString(2, String.valueOf(localObject2));
                ps.setString(3, tableId);
            }
        });
    }

    public void deleteLayout(final String tableId, final String name)
    {
        final Object localObject1 = LayoutUtils.getUserId();
        final Object localObject2 = LayoutUtils.getRoleId();
        String str = "DELETE FROM " + this.b + " WHERE USER_ID=? AND ROLE_ID=? AND TABLE_ID=? AND NAME=?";
        this.a.update(str, new PreparedStatementSetter()
        {
            public void setValues(PreparedStatement ps)
                    throws SQLException
            {
                ps.setString(1, String.valueOf(localObject1));
                ps.setString(2, String.valueOf(localObject2));
                ps.setString(3, tableId);
                ps.setString(4, name);
            }
        });
    }

//    public int getLayoutCountByName(final String tableId, final String name)
//    {
//        final Object localObject1 = LayoutUtils.getUserId();
//        final Object localObject2 = LayoutUtils.getRoleId();
//        String str = "SELECT COUNT(*) FROM " + this.b + " A WHERE A.USER_ID=? AND A.ROLE_ID=? AND A.TABLE_ID=? AND A.NAME=?";
//        ((Integer)this.a.query(str, new PreparedStatementSetter()
//
//        new ResultSetExtractor
//        {
//            public void setValues(PreparedStatement ps)
//          throws SQLException
//            {
//                ps.setString(1, String.valueOf(localObject1));
//                ps.setString(2, String.valueOf(localObject2));
//                ps.setString(3, tableId);
//                ps.setString(4, name);
//            }
//        }, new ResultSetExtractor()
//    {
//        public Integer extractData(ResultSet rs)
//                throws SQLException, DataAccessException
//        {
//            if (rs.next()) {
//                return Integer.valueOf(rs.getInt(1));
//            }
//            return Integer.valueOf(0);
//        }
//    })).intValue();
//    }

    class GridLayoutMapper
            implements RowMapper<GridLayout>
    {
        GridLayoutMapper() {}

        public GridLayout mapRow(ResultSet rs, int index)
                throws SQLException
        {
            GridLayout localGridLayout = new GridLayout();
            localGridLayout.setGridLayoutId(rs.getString("SYS_GRID_LAYOUT_ID"));
            localGridLayout.setContent(rs.getString("CONTENT"));
            localGridLayout.setName(rs.getString("NAME"));
            localGridLayout.setRoleId(rs.getString("ROLE_ID"));
            localGridLayout.setTableId(rs.getString("TABLE_ID"));
            localGridLayout.setUserId(rs.getString("USER_ID"));
            localGridLayout.setModify(rs.getTimestamp("MODIFY_TIME"));
            return localGridLayout;
        }
    }

    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException
    {
        //this.a = ValidationJdbcTemplateHolder.getValidationJdbcTemplate(applicationContext);
        this.b = ((AbstractRefreshableApplicationContext)applicationContext).getBeanFactory().resolveEmbeddedValue("${grid.layout.table}");
    }
}

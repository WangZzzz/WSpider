package com.wz.homelink.dao;

import com.wz.homelink.bean.DealBean;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by wz on 2017/4/14.
 */
public class DealDao extends NamedParameterJdbcDaoSupport {
    public void create() {
        String sql = "CREATE TABLE IF NOT EXISTS deal_house_info (id int(10) primary key not null auto_increment, " +
                "title varchar(200), houseInfo varchar(200), dealDate varchar(50), showTotalPrice float,dealTotalPrice float," +
                " dealUnitPrice float,dealCircleTime int(5), tag varchar(200), source varchar(30), positionInfo varchar(100)," +
                " district varchar(100), detailUrl varchar(150), area float, community varchar(100));";
        getJdbcTemplate().execute(sql);
    }

    public void insert(Map<String, DealBean> dealBeanMap) {
        String sql = "INSERT INTO deal_house_info (title, houseInfo, dealDate, showTotalPrice, dealTotalPrice," +
                "dealUnitPrice, dealCircleTime, tag, source, positionInfo, district, detailUrl, area, community) " +
                "values(:title, :houseInfo, :dealDate, :showTotalPrice, :dealTotalPrice," +
                ":dealUnitPrice, :dealCircleTime, :tag, :source, :positionInfo, :district, :detailUrl, :area, :community);";
        List<SqlParameterSource> sqlParameterSources = new ArrayList<SqlParameterSource>();
        Set<Map.Entry<String, DealBean>> entrySet = dealBeanMap.entrySet();
        for (Map.Entry<String, DealBean> entry : entrySet) {
            DealBean dealBean = entry.getValue();
            sqlParameterSources.add(new BeanPropertySqlParameterSource(dealBean));
        }
        getNamedParameterJdbcTemplate().batchUpdate(sql, sqlParameterSources.toArray(new SqlParameterSource[0]));
    }
}

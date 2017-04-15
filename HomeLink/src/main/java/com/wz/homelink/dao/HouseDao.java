package com.wz.homelink.dao;

import com.wz.homelink.bean.HouseBean;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by wz on 2017/4/12.
 */
public class HouseDao extends NamedParameterJdbcDaoSupport {


    public HouseDao() {

    }

    public void create() {
        String sql = "CREATE TABLE IF NOT EXISTS house_info (id int(10) not null primary key auto_increment," +
                "title varchar(50), detailUrl varchar(100), tag varchar(30), description varchar(200), positionInfo varchar(200)," +
                "community varchar(30), totalPrice float, unitPrice float, area float, district varchar(30)) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
        getJdbcTemplate().execute(sql);
    }

    public void insert(HouseBean houseBean) {

    }

    public void insert(Map<String, HouseBean> houseMap) {
        String sql = "INSERT INTO house_info (title, detailUrl, tag, description, positionInfo, community, totalPrice, unitPrice, area, district)" +
                " VALUES (:title,:detailUrl,:tag,:description,:positionInfo,:community,:totalPrice,:unitPrice,:area,:district);";
        List<SqlParameterSource> sqlParameterSources = new ArrayList<SqlParameterSource>();
        if (houseMap != null && houseMap.size() > 0) {
            Set<Map.Entry<String, HouseBean>> entrySet = houseMap.entrySet();
            for (Map.Entry<String, HouseBean> entry : entrySet) {
                sqlParameterSources.add(new BeanPropertySqlParameterSource(entry.getValue()));
            }
        }
        getNamedParameterJdbcTemplate().batchUpdate(sql, sqlParameterSources.toArray(new SqlParameterSource[0]));
    }

    public List<HouseBean> queryByCommunity(String community) {
        return null;
    }

    public List<HouseBean> queryByTotalPrice(int low, int high) {
        return null;
    }
}

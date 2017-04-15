package com.wz.homelink.dao;

import com.wz.homelink.bean.LinkBean;
import com.wz.homelink.util.CommonUtil;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wz on 2017/4/13.
 */
public class LinkDao extends NamedParameterJdbcDaoSupport {
    public LinkDao() {

    }

    public void create() {
        String sql = "CREATE TABLE IF NOT EXISTS house_overview_link ( id int(10) primary key not null auto_increment, url varchar(200), " +
                "district varchar(20), type int(10));";
        getJdbcTemplate().execute(sql);
    }

    public void insert(List<String> urls, int type) {
        String sql = "insert into house_overview_link (url, district, type) values(:url, :district, :type);";
        if (urls != null && urls.size() > 0) {
            List<SqlParameterSource> sqlParameterSources = new ArrayList<>();
            for (String url : urls) {
                LinkBean linkBean = new LinkBean();
                linkBean.setDistrict(CommonUtil.getDistrictFromUrl(url));
                linkBean.setUrl(url);
                linkBean.setType(type);
                sqlParameterSources.add(new BeanPropertySqlParameterSource(linkBean));
            }
            getNamedParameterJdbcTemplate().batchUpdate(sql, sqlParameterSources.toArray(new SqlParameterSource[0]));
        }
    }

    public List<String> queryByType(int type) {
        String sql = "select * from  house_overview_link where type = ?";
        List<LinkBean> links = getJdbcTemplate().query(sql, new BeanPropertyRowMapper(LinkBean.class), type);
        List<String> urls = new ArrayList<String>();
        if(links != null && links.size() > 0){
            System.out.println("数据库查询到--->" + links.size() + "条结果");
            for(LinkBean linkBean : links){
                urls.add(linkBean.getUrl());
            }
        }else{
            System.out.println("数据库未查询到结果--->");
        }
        return urls;
    }
}

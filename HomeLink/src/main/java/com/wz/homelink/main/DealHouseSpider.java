package com.wz.homelink.main;

import com.wz.homelink.bean.HouseBean;
import com.wz.homelink.util.CommonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wz on 2017/4/13.
 * 查询链家成交数据
 */
public class DealHouseSpider {


    private static int sTotalPage = 0;
    private static int sTotalNum = 0;
    private static List<String> sUrls;
    private static Map<String, HouseBean> sHouses;
    private static int sTmpSize = 0;

    public static void start() {
        init();
        CommonUtil.login();

    }

    private static void init() {
        sUrls = new ArrayList<String>();
        sHouses = new HashMap<String, HouseBean>();
    }
}

package com.wz.homelink;

/**
 * Created by WangZ on 2017-04-10.
 */
public class Constants {

    public static final String BASE_URL = "http://bj.lianjia.com/";

    //二手房源信息地址
    public static final String ERSHOUFANG_URL = BASE_URL + "ershoufang/";
    //预登陆地址，获取data
    public static final String PRE_LOGIN_URL = "http://passport.lianjia.com/cas/prelogin/loginTicket";
    //登录地址
    public static final String LOGIN_URL = "http://passport.lianjia.com/cas/login";
    //验证码地址
    public static final String CAPTCHA_INFO_URL = "http://captcha.lianjia.com/human";
    //查询成交的地址
    public static final String CHENGJIAO_URL = BASE_URL + "chengjiao/";


    public static final String[] DISTRICTS = {"dongcheng", "xicheng", "haidian", "fengtai", "shijingshan", "tongzhou"
            , "changping", "daxing", "yizhuangkaifaqu", "shunyi", "fangshan", "mentougou", "pingu", "huairou"
            , "miyun", "yanqing", "yanjiao"};
    public static final String[] DISTRICTS_CHINESE = {"东城", "西城", "海淀", "丰台", "石景山", "通州"
            , "昌平", "大兴", "亦庄开发区", "顺义", "房山", "门头沟", "平谷", "怀柔"
            , "密云", "延庆", "燕郊"};

    public static final String JSON_DATA = "data";
    public static final String JSON_UUID = "uuid";
    public static final String JSON_SUCCESS = "success";
    public static final String JSON_IMAGES = "images";
    public static final String JSON_TOTAL_PAGE = "totalPage";

}

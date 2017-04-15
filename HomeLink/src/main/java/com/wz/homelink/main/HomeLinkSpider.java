package com.wz.homelink.main;

import com.wz.common.ThreadPoolManger;
import com.wz.common.network.NetClient;
import com.wz.common.util.StringUtil;
import com.wz.homelink.Constants;
import com.wz.homelink.bean.DealBean;
import com.wz.homelink.bean.HouseBean;
import com.wz.homelink.dao.DealDao;
import com.wz.homelink.dao.HouseDao;
import com.wz.homelink.dao.LinkDao;
import com.wz.homelink.util.CommonUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wz on 2017/4/11.
 * 查询链家二手房源数据，并保存数据库
 */
public class HomeLinkSpider {

    private static int sTotalPage = 0;
    private static int sTotalNum = 0;
    private static List<String> sUrls;
    private static Map<String, HouseBean> sHouses;
    private static Map<String, DealBean> sDealHouses;
    private static int sTmpSize = 0;

    private static int sType = 1;

    //爬取二手房源信息
    public static final int TYPE_ERSHOUFANG = 1;
    //爬取成交信息
    public static final int TYPE_DEAL = 2;


    public static void start(int type, boolean refresh) {

        sType = type;
        System.out.println("开始爬取链家房源数据--->");

        init();
        CommonUtil.login();
        if (refresh) {
            for (int i = 0; i < Constants.DISTRICTS.length; i++) {
                try {
                    getTotalInfo(i);
                } catch (Exception e) {
                    continue;
                }
                initUrls(i);
            }
        } else {
            sUrls = geturlsFromDbByType(sType);
        }

        System.out.println("爬取到--->" + sUrls.size() + "条链接");


        if (sUrls.size() > 0) {
            if (refresh) {
                saveOverviewLinkDb();
                System.out.println("保存链接到数据库成功--->");
            }
            System.out.println("休眠10s--->");
            CommonUtil.sleep(10000);
            for (final String url : sUrls) {
                ThreadPoolManger.getInstance()
                        .execute(() -> getDetailInfo(url));
            }
        } else {
            System.out.println("未爬取到房源信息，程序退出--->");
            return;
        }
        ThreadPoolManger.getInstance().await(10, 100);
        System.out.println("回归主线程--->");
        System.out.println("应共有房源--->" + sTotalNum + "套");
        System.out.println("总共爬取到--->" + sHouses.size() + "套房源");
        saveHouseInfoDb();
        System.out.println("插入数据完毕--->爬取结束！");
    }

    /**
     * 做一些初始化的工作
     */
    private static void init() {
        sUrls = new ArrayList<String>();
        sHouses = new HashMap<String, HouseBean>();
        sDealHouses = new HashMap<String, DealBean>();
    }

    /**
     * 获取总共的二手房源数以及总共页数
     */
    public static void getTotalInfo(int districtIndex) {
        String url = Constants.ERSHOUFANG_URL + Constants.DISTRICTS[districtIndex] + "/";
        if (sType == TYPE_DEAL) {
            url = Constants.CHENGJIAO_URL + Constants.DISTRICTS[districtIndex] + "/";
        }
        System.out.println("正在爬取--->" + Constants.DISTRICTS_CHINESE[districtIndex]);
        CloseableHttpResponse response = NetClient.doGet(url);
        if (response != null && response.getStatusLine().getStatusCode() == 200) {
            String html = StringUtil.responseToString(response);
            Document document = Jsoup.parse(html);
            Element element = null;
            if (sType == TYPE_ERSHOUFANG) {
                element = document.select("h2[class=total fl] > span").first();
            } else if (sType == TYPE_DEAL) {
                element = document.select("div[class=total fl] > span").first();
            }
            if (element == null) {
                return;
            }
            String totalNum = element.text();
            int districtNum = Integer.valueOf(totalNum);
            System.out.println(Constants.DISTRICTS_CHINESE[districtIndex] + "房源共有--->" + districtNum + "套");
            sTotalNum = sTotalNum + districtNum;

            Element element1 = document.select("div[class=page-box house-lst-page-box]").first();
            String pageInfo = element1.attr("page-data");
            JSONObject json = new JSONObject(pageInfo);
            sTotalPage = Integer.valueOf(json.optString(Constants.JSON_TOTAL_PAGE));
            System.out.println("共有--->" + sTotalPage + "页");
            CommonUtil.sleep(1000);
        } else if (response != null && response.getStatusLine().getStatusCode() == 302) {
            System.out.println("发生异常，URL--->" + url + "\n错误码--->302");
            CommonUtil.processException(response);
        } else {
            if (response != null) {
                int code = response.getStatusLine().getStatusCode();
                System.out.println("发生异常，URL--->" + url + "\n错误码--->" + code);
            }
        }
    }

    /**
     * 添加详情页url
     */
    private static void initUrls(int districtIndex) {
        for (int i = 1; i <= sTotalPage; i++) {
            String url = Constants.ERSHOUFANG_URL + Constants.DISTRICTS[districtIndex] + "/pg" + i + "/";
            if (sType == TYPE_DEAL) {
                url = Constants.CHENGJIAO_URL + Constants.DISTRICTS[districtIndex] + "/pg" + i + "/";
            }
            System.out.println("新加入链接--->" + url);
            sUrls.add(url);
        }
    }

    private static void getDetailInfo(String url) {
        try {
            sTmpSize = sTmpSize + 1;
            System.out.println("目前已爬取--->" + sTmpSize + "/" + sUrls.size());
            System.out.println("当前链接为--->" + url);
            CommonUtil.sleep(200);
            CloseableHttpResponse response = NetClient.doGet(url);
            if (response != null && 200 == response.getStatusLine().getStatusCode()) {
                String html = StringUtil.responseToString(response);
                Document document = Jsoup.parse(html);
                if (sType == TYPE_ERSHOUFANG) {
                    Elements elements = document.select("li[class=clear]");
                    if (elements != null && elements.size() > 0) {
                        for (Element element : elements) {
                            HouseBean houseBean = getHouseBean(element, url);
                            if (houseBean != null) {
                                if (!sHouses.containsKey(houseBean.getDetailUrl())) {
                                    System.out.println("新加入二手房源--->" + houseBean.getTitle());
                                    sHouses.put(houseBean.getDetailUrl(), houseBean);
                                } else {
                                    System.out.println("已经添加--->" + houseBean.getTitle());
                                }
                            }
                        }
                    }
                } else if (sType == TYPE_DEAL) {
                    Elements elements = document.select("div[class=info]");
                    if (elements != null && elements.size() > 0) {
                        for (Element element : elements) {
                            DealBean dealBean = getDealBean(element, url);
                            if (dealBean != null) {
                                if (!sDealHouses.containsKey(dealBean.getDetailUrl())) {
                                    System.out.println("新加入成交房源--->" + dealBean.getTitle());
                                    sDealHouses.put(dealBean.getDetailUrl(), dealBean);
                                } else {
                                    System.out.println("已经添加--->" + dealBean.getTitle());
                                }
                            }
                        }
                    }
                } else {
                    if (response != null) {
                        System.out.println("发生异常，错误码--->" + response.getStatusLine().getStatusCode());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("发生异常--->" + url);
        }
    }

    private static HouseBean getHouseBean(Element element, String url) {
        if (element != null) {
            HouseBean houseBean = new HouseBean();
            String title = element.select("div[class=title] > a").first().text();
            houseBean.setTitle(title);
            houseBean.setDetailUrl(element.select("div[class=title] > a").first().attr("href"));
            houseBean.setTotalPrice(Float.valueOf(element.select("div[class=totalPrice] > span").first().text()));
            String unitPriceStr = element.select("div[class=unitPrice] > span").first().text();
            houseBean.setUnitPrice(Float.valueOf(unitPriceStr.substring(unitPriceStr.indexOf("价") + 1, unitPriceStr.indexOf("元"))));
            houseBean.setDescription(element.select("div[class=houseInfo]").first().text());
            houseBean.setArea(CommonUtil.getAreaFromDescription(houseBean.getDescription()));
            houseBean.setCommunity(element.select("div[class=houseInfo] > a").first().text());
            houseBean.setPositionInfo(element.select("div[class=positionInfo]").text());
            houseBean.setTag(element.select("div[class=tag]").first().text());
            houseBean.setDistict(CommonUtil.getDistrictFromUrl(url));
            return houseBean;
        }
        return null;
    }

    private static DealBean getDealBean(Element element, String url) {
        if (element != null) {
            DealBean dealBean = new DealBean();
            String title = element.select("div[class=title] > a").first().text();
            dealBean.setTitle(title);
            dealBean.setHouseInfo(element.select("div[class=houseInfo]").first().text());
            dealBean.setDealDate(element.select("div[class=dealDate]").first().text());
            String showTotalPriceStr = element.select("span[class=dealCycleTxt] > span").first().text();
            float showTotalPrice = Float.parseFloat(showTotalPriceStr.substring(showTotalPriceStr.indexOf("挂牌") + 2, showTotalPriceStr.indexOf("万")));
            dealBean.setShowTotalPrice(showTotalPrice);
            Elements totalPriceElements = element.select("div[class=totalPrice] > span[class=number]");
            if (totalPriceElements != null) {
                dealBean.setDealTotalPrice(Float.parseFloat(element.select("div[class=totalPrice] > span[class=number]").first().text()));
            } else {
                dealBean.setDealTotalPrice(-1);
            }
            dealBean.setDealUnitPrice(Float.parseFloat(element.select("div[class=unitPrice] > span[class=number]").text()));
            String dealTimeStr = element.select("span[class=dealCycleTxt] > *").last().text();
            dealBean.setDealCircleTime(Integer.parseInt(dealTimeStr.substring(dealTimeStr.indexOf("期") + 1, dealTimeStr.indexOf("天"))));
            dealBean.setTag(element.select("div[class=houseInfo]").first().text());
            dealBean.setSource(element.select("div[class=source]").first().text());
            dealBean.setPositionInfo(element.select("div[class=positionInfo").first().text());
            dealBean.setDistrict(CommonUtil.getDistrictFromUrl(url));
            dealBean.setDetailUrl(element.select("div[class=title] > a").first().attr("href"));
            dealBean.setArea(CommonUtil.getAreaFromTitle(title));
            dealBean.setCommunity(CommonUtil.getCommunityFromTitle(title));
            return dealBean;

        }
        return null;
    }


    private static void saveOverviewLinkDb() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-module.xml");
        LinkDao linkDao = (LinkDao) applicationContext.getBean("linkDao");
        linkDao.create();
        linkDao.insert(sUrls, sType);
    }

    /**
     * 保存数据库
     */
    private static void saveHouseInfoDb() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-module.xml");
        if (sType == TYPE_ERSHOUFANG) {
            HouseDao houseDao = (HouseDao) applicationContext.getBean("houseDao");
            houseDao.create();
            houseDao.insert(sHouses);
        } else if (sType == TYPE_DEAL) {
            DealDao dealDao = (DealDao) applicationContext.getBean("dealDao");
            dealDao.create();
            dealDao.insert(sDealHouses);
        }
    }

    private static List<String> geturlsFromDbByType(int type) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-module.xml");
        LinkDao linkDao = (LinkDao) applicationContext.getBean("linkDao");
        return linkDao.queryByType(type);
    }
}

package com.wz.homelink.util;

import com.wz.common.network.NetClient;
import com.wz.common.util.ImageUtil;
import com.wz.common.util.StringUtil;
import com.wz.homelink.Constants;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by wz on 2017/4/13.
 */
public class CommonUtil {

    /**
     * 模拟链家登录
     */
    public static void login() {
        CloseableHttpResponse response = NetClient.doGet(Constants.PRE_LOGIN_URL);
        if (response != null && response.getStatusLine().getStatusCode() == 200) {
            String res = StringUtil.responseToString(response);
            JSONObject json = new JSONObject(res);
            String data = json.optString(Constants.JSON_DATA);
            Map<String, String> loginParams = new HashMap<String, String>();
            loginParams.put("username", "13381456863");
            loginParams.put("password", "wztc001");
            loginParams.put("verifycode", "");
            loginParams.put("service", "http://bj.lianjia.com/");
            loginParams.put("isajax", "true");
            loginParams.put("code", "");
            loginParams.put("lt", data);
            CloseableHttpResponse loginResponse = NetClient.doPost(Constants.LOGIN_URL, loginParams);
            String loginResStr = StringUtil.responseToString(loginResponse);
            JSONObject loginJson = new JSONObject(loginResStr);
            int success = loginJson.optInt(Constants.JSON_SUCCESS);
            if (success == 1) {
                System.out.println("登录成功 ---> " + loginResStr);
            } else {
                System.out.println("登录失败 ---> " + loginResStr);
            }

        }
    }

    /**
     * 根据url获取区域名称
     *
     * @param url
     * @return
     */
    public static String getDistrictFromUrl(String url) {
        if (url != null && !"".equals(url)) {
            for (int i = 0; i < Constants.DISTRICTS.length; i++) {
                if (url.contains(Constants.DISTRICTS[i])) {
                    return Constants.DISTRICTS_CHINESE[i];
                }
            }
        }
        return null;
    }

    /**
     * 根据描述获取面积大小
     *
     * @param des
     * @return
     */
    public static float getAreaFromDescription(String des) {
        if (des != null && !"".equals(des)) {
            String[] tmps = des.trim().split("\\|");
            if (tmps != null && tmps.length > 0) {
                for (String tmp : tmps) {
                    if (tmp.contains("平米")) {
                        return Float.valueOf(tmp.substring(0, tmp.indexOf("平")));
                    }
                }
            }
        }
        return -1;
    }

    /**
     * 根据标题获取面积大小
     *
     * @param title
     * @return
     */
    public static float getAreaFromTitle(String title) {
        if (title != null && !"".equals(title)) {
            String[] tmps = title.split(" ");
            if (tmps != null && tmps.length > 0) {
                for (String tmp : tmps) {
                    if (tmp.contains("平米")) {
                        return Float.valueOf(tmp.substring(0, tmp.indexOf("平")));
                    }
                }
            }
        }
        return -1;
    }

    /**
     * 根据标题获取小区名称
     *
     * @param title
     * @return
     */
    public static String getCommunityFromTitle(String title) {
        if (title != null && !"".equals(title)) {
            String[] tmps = title.split(" ");
            if (tmps.length > 0) {
                return tmps[0];
            }
        }
        return null;
    }

    /**
     * 处理链家302重定向到验证码的页面
     *
     * @param response
     */
    public static void processException(CloseableHttpResponse response) {
        //发生异常跳转
        if (302 == response.getStatusLine().getStatusCode()) {
            //重定向
            Header header = response.getFirstHeader("Location");
            String redirectUrl = header.getValue();
            System.out.println("重定向地址--->" + redirectUrl);
            if (redirectUrl.toLowerCase().contains("captcha")) {
                CommonUtil.processCaptcha(redirectUrl);
            }
        }
    }

    /**
     * 处理验证码
     *
     * @param url
     */
    public static void processCaptcha(String url) {
        System.out.println("处理验证码--->");
        CloseableHttpResponse response = NetClient.doGet(url);
        if (response != null && response.getStatusLine().getStatusCode() == 200) {
            String html = StringUtil.responseToString(response);
            Document document = Jsoup.parse(html);
            String csrf = document.select("input[name=_csrf]").first().attr("value");
            String uuid = getCaptchaImage();
            Scanner scanner = new Scanner(System.in);
            System.out.println("请输入序号1,2,4,8--->");
            String bitValue = scanner.nextInt() + "";
            scanner.close();
            Map<String, String> params = new HashMap<>();
            params.put("uuid", uuid);
            params.put("_csrf", csrf);
            params.put("bitvalue", bitValue);
            CloseableHttpResponse checkResponse = NetClient.doPost(Constants.CAPTCHA_INFO_URL, params);
            if (checkResponse != null) {
                String checkResStr = StringUtil.responseToString(checkResponse);
                System.out.println("验证码验证结果--->" + checkResStr);
            }
        }
    }

    private static String getCaptchaImage() {
        CloseableHttpResponse response = NetClient.doGet(Constants.CAPTCHA_INFO_URL);
        if (response != null && response.getStatusLine().getStatusCode() == 200) {
            String html = StringUtil.responseToString(response);
            JSONObject json = new JSONObject(html);
            String uuid = json.optString(Constants.JSON_UUID);
            JSONArray jsonArray = json.optJSONArray(Constants.JSON_IMAGES);
            for (int i = 0; i < jsonArray.length(); i++) {
                String tmp = jsonArray.optString(i);
                String imgStr = tmp.replace("data:image/jpeg;base64,", "");
                ImageUtil.generateImageFromBase64(imgStr, "D:/captcha" + i + ".jpg");
                System.out.println("验证码图片已生成--->");
            }
            return uuid;
        }
        return null;
    }

    public static void sleep(int time) {
        try {
            System.out.println("休眠--->" + (time / (float) 1000) + "s");
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

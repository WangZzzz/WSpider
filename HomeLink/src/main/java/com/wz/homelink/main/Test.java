package com.wz.homelink.main;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.nlp.tokenizer.ChineseWordTokenizer;
import com.kennycason.kumo.palette.ColorPalette;
import com.wz.common.ThreadPoolManger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleInsets;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by wz on 2017/4/11.
 */
public class Test {
    public static void main(String[] args) {
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("id", "abc311");
//        params.put("passwd", "wztc001");
//        params.put("save", "on");
//        CloseableHttpResponse response = NetClient.doPost("http://m.byr.cn/user/login", params);
//        System.out.println(response.getStatusLine().getStatusCode());
//        String res = StringUtil.responseToString(response);
//        System.out.println(res);
//
//        CloseableHttpResponse response1 = NetClient.doGet("http://m.byr.cn/article/Health/203628");
//        String res1 = StringUtil.responseToString(response1);
//        System.out.println(res1);
//
//        Document document = Jsoup.parse(res1);
//        Elements elements = document.select("div[class=sp]");
//        for (Element element : elements) {
//            System.out.println(element.text());
//            System.out.println("----------------------------------------");
//        }
        initFont();
//        testDrawChart();
//        testDrawChart2();
//        testDrawChart3();
//        testGetUnitPrice();
//        testGetArea();
//        testThread();
    }


    private static void testThread() {
        for (int i = 0; i < 100; i++) {
            final int finalI = i;
            ThreadPoolManger.getInstance().getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        System.out.println(finalI);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        int checkTimes = 0;
        ThreadPoolManger.getInstance().getThreadPool().shutdown();
        try {
            while (!ThreadPoolManger.getInstance().getThreadPool().awaitTermination(2, TimeUnit.SECONDS) && checkTimes < 100) {
                // 每十秒检查一次线程是否执行完毕
                System.out.println("尚未全部爬取完毕！");
                checkTimes++;
            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            ThreadPoolManger.getInstance().getThreadPool().shutdownNow();
            e.printStackTrace();
        }
        System.out.println("主线程回归。");
        ThreadPoolManger.getInstance().getThreadPool().shutdownNow();
    }

    /**
     * 初始化字体，防止中文乱码
     */
    private static void initFont() {
        //创建主题样式
        StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
        //设置标题字体
        standardChartTheme.setExtraLargeFont(new Font("隶书", Font.BOLD, 20));
        //设置图例的字体
        standardChartTheme.setRegularFont(new Font("隶书", Font.PLAIN, 15));
        //设置轴向的字体
        standardChartTheme.setLargeFont(new Font("隶书", Font.PLAIN, 15));
        //应用主题样式
        ChartFactory.setChartTheme(standardChartTheme);
    }

    private static void testDrawChart() {
        DefaultPieDataset dpd = new DefaultPieDataset(); //建立一个默认的饼图
        dpd.setValue("管理人员", 25);  //输入数据
        dpd.setValue("市场人员", 25);
        dpd.setValue("开发人员", 45);
        dpd.setValue("其他人员", 10);
        JFreeChart chart = ChartFactory.createPieChart("某公司人员组织数据图", dpd, true, true, false);
        //可以查具体的API文档,第一个参数是标题，第二个参数是一个数据集，第三个参数表示是否显示Legend，第四个参数表示是否显示提示，第五个参数表示图中是否存在URL

        ChartFrame chartFrame = new ChartFrame("某公司人员组织数据图", chart);
        //chart要放在Java容器组件中，ChartFrame继承自java的Jframe类。该第一个参数的数据是放在窗口左上角的，不是正中间的标题。
        chartFrame.pack(); //以合适的大小展现图形
        chartFrame.setVisible(true);//图形是否可见
    }

    private static void testDrawChart2() {
        // TODO Auto-generated method stub
        //时间序列图
        TimeSeries timeseries = new TimeSeries("L&G European Index Trust", Month.class);
        timeseries.add(new Month(2, 2001), 181.8D);//这里用的是Month.class，同样还有Day.class Year.class 等等
        timeseries.add(new Month(3, 2001), 167.3D);
        timeseries.add(new Month(4, 2001), 153.8D);
        timeseries.add(new Month(5, 2001), 167.6D);
        timeseries.add(new Month(6, 2001), 158.8D);
        timeseries.add(new Month(7, 2001), 148.3D);
        timeseries.add(new Month(8, 2001), 153.9D);
        timeseries.add(new Month(9, 2001), 142.7D);
        timeseries.add(new Month(10, 2001), 123.2D);
        timeseries.add(new Month(11, 2001), 131.8D);
        timeseries.add(new Month(12, 2001), 139.6D);
        timeseries.add(new Month(1, 2002), 142.9D);
        timeseries.add(new Month(2, 2002), 138.7D);
        timeseries.add(new Month(3, 2002), 137.3D);
        timeseries.add(new Month(4, 2002), 143.9D);
        timeseries.add(new Month(5, 2002), 139.8D);
        timeseries.add(new Month(6, 2002), 137D);
        timeseries.add(new Month(7, 2002), 132.8D);
        TimeSeries timeseries1 = new TimeSeries("L&G UK Index Trust曾召帅", Month.class);

        timeseries1.add(new Month(2, 2001), 129.6D);
        timeseries1.add(new Month(3, 2001), 123.2D);
        timeseries1.add(new Month(4, 2001), 117.2D);
        timeseries1.add(new Month(5, 2001), 124.1D);
        timeseries1.add(new Month(6, 2001), 122.6D);
        timeseries1.add(new Month(7, 2001), 119.2D);
        timeseries1.add(new Month(8, 2001), 116.5D);
        timeseries1.add(new Month(9, 2001), 112.7D);
        timeseries1.add(new Month(10, 2001), 101.5D);
        timeseries1.add(new Month(11, 2001), 106.1D);
        timeseries1.add(new Month(12, 2001), 110.3D);
        timeseries1.add(new Month(1, 2002), 111.7D);
        timeseries1.add(new Month(2, 2002), 111D);
        timeseries1.add(new Month(3, 2002), 109.6D);
        timeseries1.add(new Month(4, 2002), 113.2D);
        timeseries1.add(new Month(5, 2002), 111.6D);
        timeseries1.add(new Month(6, 2002), 108.8D);
        timeseries1.add(new Month(7, 2002), 101.6D);
        TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();

        timeseriescollection.addSeries(timeseries);
        timeseriescollection.addSeries(timeseries1);
        timeseriescollection.setDomainIsPointsInTime(true); //domain轴上的刻度点代表的是时间点而不是时间段
        JFreeChart jfreechart = ChartFactory.createTimeSeriesChart("合法 & General Unit Trust Prices",
                "日期",
                "暗示的话发神经提防",
                timeseriescollection,
                true,
                true,
                false);
        jfreechart.setBackgroundPaint(Color.white);
        TextTitle textTitle = jfreechart.getTitle();
        textTitle.setFont(new Font("宋体", Font.BOLD, 20));
        LegendTitle legend = jfreechart.getLegend();
        if (legend != null) {
            legend.setItemFont(new Font("宋体", Font.BOLD, 20));
        }
        XYPlot xyplot = (XYPlot) jfreechart.getPlot(); //获得 plot : XYPlot!!
        ValueAxis domainAxis = xyplot.getDomainAxis();
        domainAxis.setTickLabelFont(new Font("宋体", Font.BOLD, 20));//设置x轴坐标上的字体
        domainAxis.setLabelFont(new Font("宋体", Font.BOLD, 20));//设置x轴坐标上的标题的字体
        ValueAxis rangeAxis = xyplot.getRangeAxis();
        rangeAxis.setTickLabelFont(new Font("宋体", Font.BOLD, 20));//设置y轴坐标上的字体
        rangeAxis.setLabelFont(new Font("宋体", Font.BOLD, 20));//设置y轴坐标上的标题的字体
        xyplot.setBackgroundPaint(Color.lightGray);
        xyplot.setDomainGridlinePaint(Color.white);
        xyplot.setRangeGridlinePaint(Color.white);
        xyplot.setAxisOffset(new RectangleInsets(5D, 5D, 5D, 5D));
        xyplot.setDomainCrosshairVisible(true);
        xyplot.setRangeCrosshairVisible(true);
        ChartFrame frame = new ChartFrame("折线图 ", jfreechart, true);
        frame.pack();
        frame.setVisible(true);
    }

    private static void testDrawChart3() {
        //     创建类别图（Category）数据对象
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(155, "苹果", "一月");
        dataset.addValue(120, "梨", "一月");
        dataset.addValue(200, "荔枝", "一月");

        dataset.addValue(210, "苹果", "二月");
        dataset.addValue(120, "梨", "二月");
        dataset.addValue(220, "荔枝", "二月");

        dataset.addValue(100, "苹果", "三月");
        dataset.addValue(300, "梨", "三月");
        dataset.addValue(210, "荔枝", "三月");

        dataset.addValue(410, "苹果", "四月");
        dataset.addValue(500, "梨", "四月");
        dataset.addValue(700, "荔枝", "四月");

        dataset.addValue(320, "苹果", "五月");
        dataset.addValue(220, "梨", "五月");
        dataset.addValue(380, "荔枝", "五月");
        //创建主题样式
        StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
        //设置标题字体
        standardChartTheme.setExtraLargeFont(new Font("微软雅黑", Font.BOLD, 20));
        //设置图例的字体
        standardChartTheme.setRegularFont(new Font("微软雅黑", Font.PLAIN, 15));
        //设置轴向的字体
        standardChartTheme.setLargeFont(new Font("微软雅黑", Font.PLAIN, 15));
        //应用主题样式
        ChartFactory.setChartTheme(standardChartTheme);
        JFreeChart chart = ChartFactory.createBarChart3D("水果产量图", "月份", "产量（万吨）", dataset, PlotOrientation.VERTICAL, true, true, true);
        ChartFrame frame = new ChartFrame("水果产量图 ", chart, true);
        frame.pack();
        frame.setVisible(true);
    }


    private static void genWordCloud() throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(600);
        frequencyAnalyzer.setMinWordLength(2);
        frequencyAnalyzer.setWordTokenizer(new ChineseWordTokenizer());

        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load("D:/错误.txt");
        final Dimension dimension = new Dimension(600, 600);
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(2);
        wordCloud.setBackground(new CircleBackground(300));
        wordCloud.setColorPalette(new ColorPalette(new Color(0xD5CFFA), new Color(0xBBB1FA), new Color(0x9A8CF5), new Color(0x806EF5)));
        wordCloud.setFontScalar(new SqrtFontScalar(12, 45));
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile("D:/test_circle.png");
    }


    private static void testGetUnitPrice() {
        String str = "单价55027元/平米";
        System.out.println(str.indexOf("价"));
        System.out.println(str.indexOf("元"));
        System.out.println(str.substring(str.indexOf("价") + 1, str.indexOf("元")));
    }

    private static void testGetArea() {
        String str = "富临上上城 | 2室1厅 | 103.26平米 | 南 北 | 精装 | 有电梯";
        String[] tmps = str.split("\\|");
        for (String tmp : tmps) {
            if (tmp.contains("平米")) {
                System.out.println(Float.valueOf(tmp.substring(0, tmp.indexOf("平"))));
            }
        }
    }
}

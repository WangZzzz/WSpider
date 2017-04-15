package com.wz.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by wz on 2017/4/12.
 */
public class ThreadPoolManger {

    private ExecutorService mExecutorService;
    private static final int MAX_THREAD_SIZE = 20;
    private static volatile ThreadPoolManger sInstance;


    public static ThreadPoolManger getInstance() {
        if (sInstance == null) {
            synchronized (ThreadPoolManger.class) {
                if (sInstance == null) {
                    sInstance = new ThreadPoolManger();
                }
            }
        }
        return sInstance;
    }

    private ThreadPoolManger() {
        mExecutorService = Executors.newFixedThreadPool(MAX_THREAD_SIZE);
    }

    public ExecutorService getThreadPool() {
        return mExecutorService;
    }

    /**
     * 执行任务
     *
     * @param runnable
     */
    public void execute(Runnable runnable) {
        mExecutorService.execute(runnable);
    }

    public void await(int unitTime, int checkTimes) {
        // 执行一次，不再接收新的任务，继续执行之前的任务
        ThreadPoolManger.getInstance().getThreadPool().shutdown();
        try {
            while (!ThreadPoolManger.getInstance().getThreadPool().awaitTermination(unitTime, TimeUnit.SECONDS) && checkTimes < 100) {
                // 每隔一定时间检查一次线程是否执行完毕
                System.out.println("尚未全部爬取完毕！");
                checkTimes++;
            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            ThreadPoolManger.getInstance().getThreadPool().shutdownNow();
            e.printStackTrace();
        }
        ThreadPoolManger.getInstance().getThreadPool().shutdownNow();
    }
}

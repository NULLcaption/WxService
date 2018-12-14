package com.cxg.weChat;

import java.util.concurrent.CountDownLatch;

/**
 * @Description 多线程测试之CountDownLatch 等待指定数目的进程运行结束 统一进行下一步操作
 * @Author xg.chen
 * @Date 9:42 2018/12/10
**/

public class CountDownLatchDemo {
    public static void main(String[] args) {
        final CountDownLatch latch= new CountDownLatch(2);
        //任务1
        new Thread(){
            public void run() {
                try {
                    System.out.println("任务1正在执行任务...");
                    Thread.sleep((long)(Math.random()*10000));
                    System.out.println("任务1执行完毕...");
                    latch.countDown();

                } catch (InterruptedException e) {

                    e.printStackTrace();
                }
            }
        }.start();

        //任务2
        new Thread(){
            public void run() {
                try {
                    System.out.println("任务2正在执行任务...");
                    Thread.sleep((long)(Math.random()*10000));
                    System.out.println("任务2执行完毕...");
                    latch.countDown();

                } catch (InterruptedException e) {

                    e.printStackTrace();
                }
            };
        }.start();
        //主线程
        System.out.println("等待其他两个任务执行完毕，主线程才开始执行:"+Thread.currentThread().getName());
        try {
            latch.await();//同步点  阻塞点
            System.out.println("两个任务执行完毕:"+Thread.currentThread().getName());
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}

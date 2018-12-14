package com.cxg.weChat;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description 多线程测试之CyclicBarrier 统一处理结果
 * @Author xg.chen
 * @Date 9:50 2018/12/10
**/

public class CyclicBarrierDemo {
    public static void main(String[] args){
        //3个人聚餐
        final CyclicBarrier cb =new CyclicBarrier(3, new Runnable() {
            public void run() {
                System.out.println("人员全部到齐了，拍照留念。。。");
                try {
                    Thread.sleep((long)(Math.random()*10000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        //线程池
        ExecutorService threadPool= Executors.newCachedThreadPool();

        //模拟3个用户
        for (int i = 0;i < 20; i++) {
            final int user =i+1;
            Runnable r=new Runnable() {
                public void run() {
                    //模拟每个人来的时间不一样
                    try {
                        Thread.sleep((long)(Math.random()*10000));
                        System.out.println(user+"到达聚餐点，当前已有"+(cb.getNumberWaiting()+1)+"人达到");
                        //阻塞
                        cb.await();
                        if(user==1){ //打印一句
                            System.out.println("拍照结束，开始吃饭...");
                        }
                        Thread.sleep((long)(Math.random()*10000));
                        System.out.println(user+"吃完饭..准备回家.");
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            };
            threadPool.execute(r);
        }
        threadPool.shutdown();
    }

}

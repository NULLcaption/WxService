package com.cxg.weChat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @Description 多线程测试之Semaphore 限制同事处理的进程数量
 * 排队买票 同时进行的进程数量
 * @Author xg.chen
 * @Date 9:27 2018/12/10
**/
public class SemaphoreDemo {

    class SemaphoreRunable implements Runnable{
        private Semaphore semaphore;//信号量
        private int user;//记录第几个用户

        public SemaphoreRunable(Semaphore semaphore,int user){
            this.semaphore=semaphore;
            this.user=user;
        }
        public void run() {
            try {
                //获取型号量许可
                semaphore.acquire();
                System.out.println("用户【"+user+"】进入窗口，准备买票");
                Thread.sleep((long)(Math.random()*10000));
                System.out.println("用户【"+user+"】买票完成，即将离开...");
                Thread.sleep((long)(Math.random()*10000));
                System.out.println("用户【"+user+"】离开售票窗口...");
                //释放
                semaphore.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private void execute(){
        //定义窗口个数
        final Semaphore semaphore=new Semaphore(10);   //信号量 控制并发线程数
        //线程池
        ExecutorService threadPool= Executors.newCachedThreadPool();
        //模拟20个用户
        for (int i=0;i<20;i++){
            //执行买票
            threadPool.execute(new SemaphoreRunable(semaphore,(i+1)));
        }
    }

    public static void main(String[] args) {
        SemaphoreDemo sd=new SemaphoreDemo();
        sd.execute();
    }
}

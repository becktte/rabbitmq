package com.yyj.email.consumer.test;

import java.sql.Array;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

/**
 * @Description TODO
 * @Author becktte
 * @Date 2019/4/24
 * @Version 1.0
 **/
public class CountDownlatchTest {

    public static void main(String[] args) {

        final CountDownLatch countDownLatch = new CountDownLatch(4);
        for (int i = 0; i < 4; i++) {
            new Thread(new Excutor(countDownLatch)).start();
        }

        try {
            countDownLatch.await();
            System.out.println("all thread finished");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    static class Excutor implements Runnable {
        private CountDownLatch countDownLatch;

        public Excutor(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName() + "started");
                Thread.sleep(2000);
                System.out.println(Thread.currentThread().getName() + "finished");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                countDownLatch.countDown();
            }
        }
    }
}
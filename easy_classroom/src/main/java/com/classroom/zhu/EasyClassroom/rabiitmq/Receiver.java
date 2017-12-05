package com.classroom.zhu.EasyClassroom.rabiitmq;

import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

/**
 * Created by 12801 on 2017/11/23.
 */
@Component
public class Receiver {
    private CountDownLatch latch = new CountDownLatch(1);

    public void receiveMessage(String message){
        System.out.println("Received <"+message+">");
        latch.countDown();
    }
    public CountDownLatch getLatch(){
        return latch;
    }

}

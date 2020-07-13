package com.gun.rxjavapractice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;

@RunWith(JUnit4.class)
public class RxjavaOperatorTest {

    @Test
    public void rangeTest() {
        Observable source = Observable.range(1, 10)
                .filter(data -> data % 2 == 0);
        source.subscribe(System.out::println);
    }

    @Test
    public void intervalRangeTest() throws InterruptedException {
        Observable<Long> source = Observable.intervalRange(1,
                5,
                100L,
                100L,
                TimeUnit.MILLISECONDS);

        source.subscribe(System.out::println);
        Thread.sleep(3000);
    }

    @Test
    public void heartBeatTest() throws InterruptedException {
        String serverUrl = "https://api.github.com/zen";
        Observable.timer(2, TimeUnit.SECONDS)
                .map(val -> serverUrl)
                .map(OkHttpHelper::get)
                .repeat()
                .subscribe(System.out::println);
        Thread.sleep(10000);
    }
}

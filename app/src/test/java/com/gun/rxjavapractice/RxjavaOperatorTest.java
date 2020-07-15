package com.gun.rxjavapractice;

import android.graphics.drawable.shapes.Shape;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observables.GroupedObservable;

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

    @Test
    public void concatMapTest() throws InterruptedException {
        //interleaving 불가능
        String[] balls = {"1", "3", "5"};
        Observable<String> source = Observable.interval(100L, TimeUnit.MILLISECONDS)
                .map(Long::intValue)
                .map(idx -> balls[idx])
                .take(balls.length)
                .concatMap(ball -> Observable.interval(200L, TimeUnit.MILLISECONDS)
                        .map(notUsed -> ball + "!!!")
                        .take(2));
        source.subscribe(System.out::println);
        Thread.sleep(5000);
    }

    @Test
    public void flatMapV2Test() throws InterruptedException {
        //interleaving 가능
        String[] balls = {"1", "3", "5"};
        Observable<String> source = Observable.interval(100L, TimeUnit.MILLISECONDS)
                .map(Long::intValue)
                .map(idx -> balls[idx])
                .take(balls.length)
                .flatMap(ball -> Observable.interval(200L, TimeUnit.MILLISECONDS)
                        .map(notUsed -> ball + "!!!")
                        .take(2));
        source.subscribe(System.out::println);
        Thread.sleep(5000);
    }

    @Test
    public void switchMapTest() throws InterruptedException {
        //interleaving 가능
        String[] balls = {"1", "3", "5"};
        Observable<String> source = Observable.interval(100L, TimeUnit.MILLISECONDS)
                .map(Long::intValue)
                .map(idx -> balls[idx])
                .take(balls.length)
                .doOnNext(System.out::println)
                .switchMap(ball -> Observable.interval(200L, TimeUnit.MILLISECONDS)
                        .map(notUsed -> ball + "!!!")
                        .take(2));
        source.subscribe(System.out::println);
        Thread.sleep(5000);
    }

    @Test
    public void groupByTest() {
        String[] objs = {"6", "4", "2-T", "2", "6-T", "4-T"};
        @NonNull Observable<GroupedObservable<String, String>> source =
                Observable.fromArray(objs).groupBy(RxjavaOperatorTest::getShape);

        source.subscribe(obj -> {
            obj.subscribe(val -> System.out.println("Group : " + obj.getKey() + "\t Value : " + val));
        });
    }

    @Test
    public void groupByExampleTest() {
        String[] objs = {"6", "4", "2-T", "2", "6-T", "4-T"};
        @NonNull Observable<GroupedObservable<String, String>> source =
                Observable.fromArray(objs).groupBy(RxjavaOperatorTest::getShape);

        source.subscribe(obj -> {
            obj.filter(val -> obj.getKey().equals("Ball"))
                    .subscribe(val -> System.out.println("Group : " + obj.getKey() + "\t Value : " + val));
        });
    }

    @Test
    public void scanTest() {
        String[] balls = {"1", "3", "5"};
        Observable source = Observable.fromArray(balls)
                .scan((ball1, ball2) -> ball2 + "(" + ball1 + ")");
        source.subscribe(System.out::println);
    }

    private static String getShape(String obj) {
        if (obj == null || obj.equals("")) return "No-Shape";
        if (obj.endsWith("-H")) return "Hexagon";
        if (obj.endsWith("-O")) return "Octagon";
        if (obj.endsWith("-R")) return "Rectangle";
        if (obj.endsWith("-T")) return "Triangle";
        if (obj.endsWith("-D")) return "Diamond";
        return "Ball";
    }

    @Test
    public void zipTest() {
        String[] shapes = {"BALL", "PENTAGON", "STAR"};
        String[] coloredTriangles = {"2-T", "6-T", "4-T"};
        Observable source = Observable.zip(
                Observable.fromArray(shapes).map(com.gun.rxjavapractice.Shape::getSuffix),
                Observable.fromArray(coloredTriangles).map(com.gun.rxjavapractice.Shape::getColor),
                (suffix, color) -> color + suffix);
        source.subscribe(System.out::println);
    }

    @Test
    public void combineExampleTest() {
        Observable source = Observable.zip(
                Observable.just(100, 200, 300),
                Observable.just(10, 20, 30),
                Observable.just(1, 2, 3),
                (a, b, c) -> a + b + c);
        source.subscribe(System.out::println);
    }

    @Test
    public void combineExample2Test() throws InterruptedException {
        Observable source = Observable.zip(
                Observable.just("RED", "GREEN", "BLUE"),
                Observable.interval(200L, TimeUnit.MILLISECONDS),
                (value, i)-> value
        );
        source.subscribe(System.out::println);
        Thread.sleep(1000);
    }
//
//    @Test
//    public void combineElectriBillsTest() {
//        String[] data = {
//                "100",
//                "300"
//        };
//        Observable basePrice = Observable.fromArray(data)
//                .map(Integer::parseInt)
//                .map(val -> {
//                    if (val <= 200) return 910;
//                    if (val<=400) return 1600;
//                    return 7300;
//                });
//
//        Observable usagePrice = Observable.fromArray(data)
//                .map()
//
//    }
}
